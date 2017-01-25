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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PdbToPfamMap that = (PdbToPfamMap) o;

        if (!getPdb().equals(that.getPdb())) return false;
        if (!getChain().equals(that.getChain())) return false;
        if (!getSpPrimary().equals(that.getSpPrimary())) return false;
        return getPfamId().equals(that.getPfamId());
    }

    @Override
    public int hashCode() {
        int result = getPdb().hashCode();
        result = 31 * result + getChain().hashCode();
        result = 31 * result + getSpPrimary().hashCode();
        result = 31 * result + getPfamId().hashCode();
        return result;
    }

    public PdbToPfamMap(String pdb, String chain, String spPrimary, String pfamId) {
        this.pdb = pdb;
        this.chain = chain;
        this.spPrimary = spPrimary;
        this.pfamId = pfamId;
    }
}
