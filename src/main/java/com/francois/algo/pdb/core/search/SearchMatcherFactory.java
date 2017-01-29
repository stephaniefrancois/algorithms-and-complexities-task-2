package com.francois.algo.pdb.core.search;

public final class SearchMatcherFactory {
    public final PdbChainMatcher createExactPdbChainMatcher(String pdb, String chain) {
        return new PdbAndChainBasedPdbChainMatcher(pdb, chain);
    }
    public final PdbChainMatcher createPartialEcNumberMatcher(String partialEcNumberSearchCriteria) {
        return null; // new PartialEcNumberBasedPdbChainMatcher(pdb, chain);
    }
}
