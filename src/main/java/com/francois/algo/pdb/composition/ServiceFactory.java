package com.francois.algo.pdb.composition;

import com.francois.algo.pdb.core.PdbChainDataAggregator;
import com.francois.algo.pdb.core.PdbChainDataCache;
import com.francois.algo.pdb.core.PdbChainFinder;
import com.francois.algo.pdb.core.domain.PdbChainDescriptor;
import com.francois.algo.pdb.core.domain.PdbToEnzymeMap;
import com.francois.algo.pdb.core.domain.PdbToPfamMap;
import com.francois.algo.pdb.core.search.*;
import com.francois.algo.pdb.data.*;

public final class ServiceFactory {
    private volatile static ServiceFactory ourInstance = new ServiceFactory();
    private final ListBasedPdbChainFinder chainFinder;

    public static ServiceFactory getInstance() {
        return ourInstance;
    }

    private static final String PDB_CHAIN_TO_ENZYME_MAP = "src/main/resources/pdb_chain_enzyme.tsv";
    private static final String PDB_CHAIN_TO_PFAM_MAP = "src/main/resources/pdb_chain_pfam.tsv";

    private ServiceFactory() {
        IDataParser<PdbToPfamMap> pfamMapParser = new PdbChainToPfamParser();
        IDataParser<PdbToEnzymeMap> enzymeMapParser = new PdbChainToEnzymeParser();
        IRepository<PdbToEnzymeMap> enzymeRepository = new FileBasedRepository<>(PDB_CHAIN_TO_ENZYME_MAP, enzymeMapParser);
        IRepository<PdbToPfamMap> pframRepository = new FileBasedRepository<>(PDB_CHAIN_TO_PFAM_MAP, pfamMapParser);
        IRepository<PdbChainDescriptor> aggregator = new PdbChainDataAggregator(pframRepository, enzymeRepository);
        IRepository<PdbChainDescriptor> cache = new PdbChainDataCache(aggregator);
        chainFinder = new ListBasedPdbChainFinder(cache);
    }

    public final PdbChainFinder createPdbChainFinder() {
        return chainFinder;
    }

    public final PdbChainMatcher createMatcherByPdbAndChain(String pdb, String chain) {
        return new PdbAndChainBasedPdbChainMatcher(pdb, chain);
    }

    public final PdbChainMatcher createMatcherByEcNumber(String partialEcNumber) {
        return new PartialEcNumberBasedPdbChainMatcher(partialEcNumber,
                new EcNumberBasedSearchCriteriaValidator());
    }
}
