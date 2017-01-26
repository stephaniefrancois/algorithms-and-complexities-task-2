package com.francois.algo.pdb.core;

import com.francois.algo.pdb.core.domain.AppException;
import com.francois.algo.pdb.core.domain.PdbChainDescriptor;
import com.francois.algo.pdb.core.domain.PdbToEnzymeMap;
import com.francois.algo.pdb.core.domain.PdbToPfamMap;
import com.francois.algo.pdb.data.IRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class PdbChainDataAggregator implements IRepository<PdbChainDescriptor> {

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
        List<PdbToPfamMap> pfamMaps = this.pfamRepository.getAll();
        List<PdbToEnzymeMap> enzymeMaps = this.enzymeRepository.getAll();
        List<PdbChain> identifiers = extractPdbChainIdentifiers(pfamMaps, enzymeMaps);

        return identifiers.stream()
                .map(id -> {
                    Optional<PdbToPfamMap> pfam =
                            pfamMaps.stream()
                                    .filter(m -> Objects.equals(m.getPdb(), id.getPdb()) &&
                                            Objects.equals(m.getChain(), id.getChain()))
                                    .findFirst();
                    Optional<PdbToEnzymeMap> enzyme =
                            enzymeMaps.stream()
                                    .filter(m -> Objects.equals(m.getPdb(), id.getPdb()) &&
                                            Objects.equals(m.getChain(), id.getChain()))
                                    .findFirst();

                    String accession = "";
                    String ecNumber = "";
                    String spPrimary = "";
                    String pfamId = "";

                    if (pfam.isPresent()) {
                        spPrimary = pfam.get().getSpPrimary();
                        pfamId = pfam.get().getPfamId();
                    }

                    if (enzyme.isPresent()) {
                        accession = enzyme.get().getAccession();
                        ecNumber = enzyme.get().getEcNumber();
                    }

                    return new PdbChainDescriptor(id.getPdb(), id.getChain(), accession, ecNumber, spPrimary, pfamId);
                }).collect(Collectors.toList());
    }

    private List<PdbChain> extractPdbChainIdentifiers(List<PdbToPfamMap> pfamMaps, List<PdbToEnzymeMap> enzymeMaps) {
        Stream<PdbChain> pfamIds = pfamMaps.stream()
                .map(m -> new PdbChain(m.getPdb(), m.getChain()));

        Stream<PdbChain> enzymeIds = enzymeMaps.stream()
                .map(m -> new PdbChain(m.getPdb(), m.getChain()));

        return Stream.of(pfamIds, enzymeIds)
                .flatMap(s -> s)
                .distinct()
                .collect(Collectors.toList());
    }

    private final class PdbChain {
        private final String pdb;
        private final String chain;

        public PdbChain(String pdb, String chain) {
            this.pdb = pdb;
            this.chain = chain;
        }

        public String getPdb() {
            return pdb;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PdbChain pdbChain = (PdbChain) o;

            if (!getPdb().equals(pdbChain.getPdb())) return false;
            return getChain().equals(pdbChain.getChain());
        }

        @Override
        public int hashCode() {
            int result = getPdb().hashCode();
            result = 31 * result + getChain().hashCode();
            return result;
        }

        public String getChain() {
            return chain;
        }
    }
}
