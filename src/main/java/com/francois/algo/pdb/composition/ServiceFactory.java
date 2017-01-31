package com.francois.algo.pdb.composition;

import com.francois.algo.pdb.app.DashboardUI;
import com.francois.algo.pdb.app.SearchEventListener;
import com.francois.algo.pdb.app.SearchResultsPrinterUI;
import com.francois.algo.pdb.app.SearchUI;
import com.francois.algo.pdb.common.ListenersManager;
import com.francois.algo.pdb.core.PdbChainDataAggregator;
import com.francois.algo.pdb.core.PdbChainDataCache;
import com.francois.algo.pdb.core.PdbChainFinder;
import com.francois.algo.pdb.core.domain.PdbChainDescriptor;
import com.francois.algo.pdb.core.domain.PdbToEnzymeMap;
import com.francois.algo.pdb.core.domain.PdbToPfamMap;
import com.francois.algo.pdb.core.search.*;
import com.francois.algo.pdb.core.sorting.AscendingPdbAndChainComparator;
import com.francois.algo.pdb.data.*;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Comparator;

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

    public final Comparator<PdbChainDescriptor> createComparator() {
        return new AscendingPdbAndChainComparator();
    }

    public final PdbChainMatcher createMatcherByPdbAndChain(String pdb, String chain) {
        return new PdbAndChainBasedPdbChainMatcher(pdb, chain);
    }

    public final PdbChainMatcher createMatcherByEcNumber(String partialEcNumber) throws InvalidSearchArgumentException {
        return new PartialEcNumberBasedPdbChainMatcher(partialEcNumber,
                new EcNumberBasedSearchCriteriaValidator());
    }

    public DashboardUI createDashboard(InputStream in, PrintStream out) {
        SearchResultsPrinterUI searchResultsPrinter = new SearchResultsPrinterUI(out);
        ListenersManager<SearchEventListener> listenersManager = new ListenersManager<>();
        SearchUI search = new SearchUI(in,
                out,
                this.createPdbChainFinder(),
                searchResultsPrinter,
                listenersManager,
                this);
        return new DashboardUI(in, out, search);
    }
}
