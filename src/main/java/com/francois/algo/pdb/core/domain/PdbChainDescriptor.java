package com.francois.algo.pdb.core.domain;

public final class PdbChainDescriptor {
    private final String pdb;
    private final String chain;
    private final String accession;
    private final String ecNumber;
    private final String spPrimary;
    private final String pfamId;

    public PdbChainDescriptor(String pdb, String chain, String accession,
                              String ecNumber, String spPrimary, String pfamId) {
        this.pdb = pdb;
        this.chain = chain;
        this.accession = accession;
        this.ecNumber = ecNumber;
        this.spPrimary = spPrimary;
        this.pfamId = pfamId;
    }

    public String getPdb() {
        return pdb;
    }

    public String getChain() {
        return chain;
    }

    public String getAccession() {
        return accession;
    }

    public String getEcNumber() {
        return ecNumber;
    }

    public String getSpPrimary() {
        return spPrimary;
    }

    public String getPfamId() {
        return pfamId;
    }

    @Override
    public String toString() {
        return "PdbChainDescriptor{" +
                "pdb='" + pdb + '\'' +
                ", chain='" + chain + '\'' +
                ", accession='" + accession + '\'' +
                ", ecNumber='" + ecNumber + '\'' +
                ", spPrimary='" + spPrimary + '\'' +
                ", pfamId='" + pfamId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PdbChainDescriptor that = (PdbChainDescriptor) o;

        if (!getPdb().equals(that.getPdb())) return false;
        if (!getChain().equals(that.getChain())) return false;
        if (!getAccession().equals(that.getAccession())) return false;
        if (!getEcNumber().equals(that.getEcNumber())) return false;
        if (!getSpPrimary().equals(that.getSpPrimary())) return false;
        return getPfamId().equals(that.getPfamId());
    }

    @Override
    public int hashCode() {
        int result = getPdb().hashCode();
        result = 31 * result + getChain().hashCode();
        result = 31 * result + getAccession().hashCode();
        result = 31 * result + getEcNumber().hashCode();
        result = 31 * result + getSpPrimary().hashCode();
        result = 31 * result + getPfamId().hashCode();
        return result;
    }
}
