package com.francois.algo.pdb.data;

import com.francois.algo.pdb.RootLogger;
import com.francois.algo.pdb.core.IDataParser;
import com.francois.algo.pdb.core.IRepository;
import com.francois.algo.pdb.core.domain.AppException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class FileBasedRepository<TModel> implements IRepository<TModel> {

    private final static Logger log = RootLogger.get();

    private final String path;
    private final IDataParser<TModel> parser;

    public FileBasedRepository(String path, IDataParser<TModel> parser) {
        Objects.requireNonNull(path);
        Objects.requireNonNull(parser);
        this.path = path;
        this.parser = parser;
    }

    @Override
    public List<TModel> getAll() throws AppException {
        final File file = new File(path);
        if (!file.exists() || !file.canRead() || !file.isFile()) {
            throw new DataFileNotFoundException(file.getAbsolutePath());
        }

        List<String> dataToParse = loadDataFromFile();
        return this.parser.parseToModel(dataToParse);
    }

    private List<String> loadDataFromFile() {
        logLoadingData();
        List<String> dataToParse = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(path),
                StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                dataToParse.add(line);
            }
            logDataLoaded();
        } catch (IOException e) {
            logFailedToLoadData(e);
        }
        return dataToParse;
    }

    private void logLoadingData() {
        log.info(() -> String.format("Loading data from '%s' ...", path));
    }

    private void logDataLoaded() {
        log.info(() -> String.format("Data from '%s' has been loaded successfully!", path));
    }

    private void logFailedToLoadData(IOException e) {
        log.log(Level.SEVERE, e, () -> String.format("Failed to load data from '%s'!", path));
    }
}
