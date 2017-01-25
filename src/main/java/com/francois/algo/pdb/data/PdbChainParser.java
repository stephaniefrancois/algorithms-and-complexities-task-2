package com.francois.algo.pdb.data;

import com.francois.algo.pdb.RootLogger;
import com.francois.algo.pdb.core.domain.InvalidPdbChainMapException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class PdbChainParser<TModel> implements IDataParser<TModel> {

    private static final Logger log = RootLogger.get();
    private final List<String> headerColumns;
    private final Function<String, InvalidPdbChainMapException> parsingExceptionFactory;

    protected PdbChainParser(List<String> headerColumns, Function<String, InvalidPdbChainMapException> parsingExceptionFactory) {
        Objects.requireNonNull(headerColumns);
        Objects.requireNonNull(parsingExceptionFactory);
        this.headerColumns = headerColumns;
        this.parsingExceptionFactory = parsingExceptionFactory;
    }

    @Override
    public final List<TModel> parseToModel(List<String> dataToParse) throws InvalidPdbChainMapException {
        Objects.requireNonNull(dataToParse);

        if (containsHeader(dataToParse)) {
            return parseData(dataToParse);
        }
        logNoHeader();

        return new ArrayList<>();
    }

    private boolean containsHeader(List<String> dataToParse) {
        return !findHeader(dataToParse).isEmpty();
    }

    private List<String> findHeader(List<String> dataToParse) {
        return dataToParse.stream()
                .filter(line -> headerColumns.stream()
                        .allMatch(column -> line.toLowerCase()
                                .contains(column.toLowerCase()))
                ).collect(Collectors.toList());
    }

    private List<TModel> parseData(List<String> dataToParse) throws InvalidPdbChainMapException {
        List<TModel> result = new ArrayList<>();

        List<String> headers = this.findHeader(dataToParse);
        int headerIndex = dataToParse.lastIndexOf(headers.get(0));
        List<String> filteredData = dataToParse.subList(headerIndex + 1, dataToParse.size());

        logHeaderFound(headerIndex);

        for (String line : filteredData) {

            logParsingLine(headerIndex, line);
            String[] tokens = line.split("\t");

            if (tokens.length != 4) {
                throw this.parsingExceptionFactory.apply(line);
            }

            result.add(buildMap(tokens));
            logParsedLine();
        }

        logParsed(result.size());

        return result;
    }

    protected abstract TModel buildMap(String[] tokens);

    private void logNoHeader() {
        log.warning(() -> "No HEADER information found! Will not parse!");
    }

    private void logHeaderFound(int headerIndex) {
        log.info(() -> String.format("HEADER found at '%d', will parse everything starting at next line ...", headerIndex));
    }

    private void logParsingLine(int headerIndex, String line) {
        log.fine(() -> String.format("Parsing '%s' line at index '%d' ...", line, headerIndex));
    }

    private void logParsedLine() {
        log.fine(() -> String.format("Line at index '%d' parsed successfully!"));
    }

    private void logParsed(int mappingsCount) {
        log.info(() -> String.format("Finished parsing all data! '%d' mappings extracted.", mappingsCount));
    }
}
