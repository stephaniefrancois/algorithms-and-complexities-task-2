package com.francois.algo.pdb.core;

import com.francois.algo.pdb.RootLogger;
import com.francois.algo.pdb.core.domain.AppException;
import com.francois.algo.pdb.core.domain.PdbChainDescriptor;
import com.francois.algo.pdb.data.IRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public final class PdbChainDataCache implements IRepository<PdbChainDescriptor> {

    private static final Logger log = RootLogger.get();
    private final IRepository<PdbChainDescriptor> chainDataProvider;
    private List<PdbChainDescriptor> cache = new ArrayList<>();

    public PdbChainDataCache(IRepository<PdbChainDescriptor> chainDataProvider) {
        Objects.requireNonNull(chainDataProvider);
        this.chainDataProvider = chainDataProvider;
    }

    @Override
    public final List<PdbChainDescriptor> getAll() throws AppException {
        logLoadingData();
        if (this.cache == null || this.cache.isEmpty()) {
            logNoCache();
            this.cache = this.chainDataProvider.getAll();

            if (this.cache == null) {
                this.cache = new ArrayList<>();
            }

            logCached();
        } else {
            logCacheFound();
        }

        return cache;
    }

    private void logLoadingData() {
        log.info(() -> "Loading PDB chain data ...");
    }

    private void logNoCache() {
        log.info(() -> "No PDB chain data cached! Loading from the storage ...");
    }

    private void logCached() {
        log.info(() -> String.format("'%d' PDB chain data records have been cached!", this.cache.size()));
    }

    private void logCacheFound() {
        log.info(() -> String.format("'%d' PDB chain data records found in cache!", this.cache.size()));
    }
}
