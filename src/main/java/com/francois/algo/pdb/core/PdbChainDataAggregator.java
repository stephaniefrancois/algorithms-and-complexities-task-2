package com.francois.algo.pdb.core;

import com.francois.algo.pdb.RootLogger;
import com.francois.algo.pdb.common.StringExtensions;
import com.francois.algo.pdb.core.domain.AppException;
import com.francois.algo.pdb.core.domain.PdbChainDescriptor;
import com.francois.algo.pdb.core.domain.PdbToEnzymeMap;
import com.francois.algo.pdb.core.domain.PdbToPfamMap;
import com.francois.algo.pdb.data.IRepository;

import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class PdbChainDataAggregator implements IRepository<PdbChainDescriptor> {
    private static final Logger log = RootLogger.get();
    private static final String VALUE_NOT_SUPPLIED = "-----";
    private static final String VALUE_EMPTY = "EMPTY";
    private final IRepository<PdbToPfamMap> pfamRepository;
    private final IRepository<PdbToEnzymeMap> enzymeRepository;

    public PdbChainDataAggregator(IRepository<PdbToPfamMap> pfamRepository,
                                  IRepository<PdbToEnzymeMap> enzymeRepository) {
        Objects.requireNonNull(pfamRepository);
        Objects.requireNonNull(enzymeRepository);
        this.pfamRepository = pfamRepository;
        this.enzymeRepository = enzymeRepository;
    }

    @Override
    public List<PdbChainDescriptor> getAll() throws AppException {

        Map<String, List<PdbToPfamMap>> pfamMaps = buildPfamMap();
        Map<String, List<PdbToEnzymeMap>> enzymeMaps = buildEnzymeMap();

        Set<String> keys = buildUniqueKeysForPfamAndEnzymes(pfamMaps, enzymeMaps);
        logBuildingDescriptors();

        List<PdbChainDescriptor> descriptors = keys.stream()
                .map(key -> discoverPdbChainDescriptorsForGivenKey(pfamMaps, enzymeMaps, key))
                .map(d -> d.stream())
                .flatMap(Function.identity())
                .collect(Collectors.toList());

        logDescriptorsFound(descriptors.size());
        return descriptors;
    }

    private Map<String, List<PdbToPfamMap>> buildPfamMap() throws AppException {
        logBuildingPfamMaps();
        Map<String, List<PdbToPfamMap>> pfamMaps =
                this.pfamRepository.getAll().stream()
                        .collect(
                                Collectors.groupingBy(p -> p.getKey(),
                                        Collectors.toList()
                                )
                        );

        logPfamMapsBuilt(pfamMaps.size());
        return pfamMaps;
    }

    private Map<String, List<PdbToEnzymeMap>> buildEnzymeMap() throws AppException {
        logBuildingEnzymeMaps();

        Map<String, List<PdbToEnzymeMap>> enzymeMaps =
                    this.enzymeRepository.getAll().stream()
                            .collect(
                                    Collectors.groupingBy(p -> p.getKey(),
                                            Collectors.toList()
                                    )
                            );
        logEnzymeMapsBuilt(enzymeMaps.size());
        return enzymeMaps;
    }

    private Set<String> buildUniqueKeysForPfamAndEnzymes(Map<String, List<PdbToPfamMap>> pfamMaps, Map<String, List<PdbToEnzymeMap>> enzymeMaps) {
        logBuildingCombinedKeys();

        Set<String> keys = Stream.of(pfamMaps.keySet().stream(), enzymeMaps.keySet().stream())
                .flatMap(Function.identity())
                .distinct()
                .collect(Collectors.toSet());

        logKeysDiscovered(keys.size());

        return keys;
    }

    private List<PdbChainDescriptor> discoverPdbChainDescriptorsForGivenKey(Map<String, List<PdbToPfamMap>> pfamMaps, Map<String, List<PdbToEnzymeMap>> enzymeMaps, String key) {
        List<PdbChainDescriptor> discoveredDescriptors = new ArrayList<>();
        List<PdbToPfamMap> pfams = pfamMaps.getOrDefault(key, Collections.emptyList());
        List<PdbToEnzymeMap> enzymes = enzymeMaps.getOrDefault(key, Collections.emptyList());

        if (pfams.isEmpty()) {
            for (PdbToEnzymeMap enzyme:enzymes) {
                discoveredDescriptors.add(
                        new PdbChainDescriptor(enzyme.getPdb(), enzyme.getChain(),
                                enzyme.getAccession(), enzyme.getEcNumber(),
                                VALUE_NOT_SUPPLIED, VALUE_NOT_SUPPLIED)
                );
            }
        } else if (enzymes.isEmpty()) {
            for (PdbToPfamMap pfam:pfams) {
                discoveredDescriptors.add(
                        new PdbChainDescriptor(pfam.getPdb(), pfam.getChain(),
                                VALUE_NOT_SUPPLIED, VALUE_NOT_SUPPLIED,
                                pfam.getSpPrimary(), pfam.getPfamId())
                );
            }
        } else {
            for (PdbToEnzymeMap enzyme:enzymes) {
                for (PdbToPfamMap pfam:pfams) {
                    discoveredDescriptors.add(
                            new PdbChainDescriptor(pfam.getPdb(), pfam.getChain(),
                                    getValueOrDefault(enzyme.getAccession()),
                                    getValueOrDefault(enzyme.getEcNumber()),
                                    getValueOrDefault(pfam.getSpPrimary()),
                                    getValueOrDefault(pfam.getPfamId()))
                    );
                }
            }
        }

        return discoveredDescriptors;
    }

    private String getValueOrDefault(String value) {
        return StringExtensions.getValueOrDefault(value, VALUE_EMPTY);
    }

    private void logBuildingPfamMaps() {
        log.info(() -> "Building PDB CHAIN PFAM maps ...");
    }

    private void logPfamMapsBuilt(int pfamMapsCount) {
        log.info(() -> String.format("'%d' PDB CHAIN PFAM maps have been built successfully.", pfamMapsCount));
    }

    private void logBuildingEnzymeMaps() {
        log.info(() -> "Building PDB CHAIN ENZYME maps ...");
    }

    private void logEnzymeMapsBuilt(int enzymeMapsCount) {
        log.info(() -> String.format("'%d' PDB CHAIN ENZYME maps have been built successfully.", enzymeMapsCount));
    }

    private void logBuildingCombinedKeys() {
        log.info(() -> "Building UNIQUE key map from PFAM and ENZYME collections ...");
    }

    private void logKeysDiscovered(int keysCount) {
        log.info(() -> String.format("'%d' UNIQUE keys have been discovered.", keysCount));
    }

    private void logBuildingDescriptors() {
        log.info(() -> "Building UNIQUE PDB CHAIN descriptors ...");
    }

    private void logDescriptorsFound(int descriptorsCount) {
        log.info(() -> String.format("'%d' UNIQUE PDB CHAIN descriptors have been created from given PFAM and ENZYME data.", descriptorsCount));
    }
}
