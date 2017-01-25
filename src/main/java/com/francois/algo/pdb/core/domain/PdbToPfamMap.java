package com.francois.algo.pdb.core.domain;

public final class PdbToPfamMap {
    private final String pdb;
    private final String chain;
    private final String spPrimary;
    private final String pfamId;

    public String getPdb() {
        return pdb;
    }

    public String getChain() {
        return chain;
    }

    public String getSpPrimary() {
        return spPrimary;
    }

    public String getPfamId() {
        return pfamId;
    }

    public PdbToPfamMap(String pdb, String chain, String spPrimary, String pfamId) {
        this.pdb = pdb;
        this.chain = chain;
        this.spPrimary = spPrimary;
        this.pfamId = pfamId;
    }
}
