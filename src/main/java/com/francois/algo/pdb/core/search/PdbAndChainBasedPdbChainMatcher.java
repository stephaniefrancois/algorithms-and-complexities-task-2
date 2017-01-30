package com.francois.algo.pdb.core.search;

import com.francois.algo.pdb.core.domain.PdbChainDescriptor;

public final class PdbAndChainBasedPdbChainMatcher implements PdbChainMatcher {
    private final String pdb;
    private final String chain;

    public PdbAndChainBasedPdbChainMatcher(String pdb, String chain) {
        this.pdb = pdb;
        this.chain = chain;
    }

    @Override
    public final boolean doesMatch(PdbChainDescriptor pdbChainToMatch) {
        return pdbChainToMatch != null &&
                pdbChainToMatch.getPdb().equals(pdb) &&
                pdbChainToMatch.getChain().equals(chain);
    }

    @Override
    public String toString() {
        return "PdbAndChainBasedPdbChainMatcher{" +
                "pdb='" + pdb + '\'' +
                ", chain='" + chain + '\'' +
                '}';
    }
}
