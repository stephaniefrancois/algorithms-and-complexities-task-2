package com.francois.algo.pdb.core.domain;

public final class PdbToEnzymeMap implements IHaveKey {
    private final String pdb;
    private final String chain;
    private final String accession;
    private final String ecNumber;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PdbToEnzymeMap that = (PdbToEnzymeMap) o;

        if (!getPdb().equals(that.getPdb())) return false;
        if (!getChain().equals(that.getChain())) return false;
        if (!getAccession().equals(that.getAccession())) return false;
        return getEcNumber().equals(that.getEcNumber());
    }

    @Override
    public int hashCode() {
        int result = getPdb().hashCode();
        result = 31 * result + getChain().hashCode();
        result = 31 * result + getAccession().hashCode();
        result = 31 * result + getEcNumber().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "PdbToEnzymeMap{" +
                "pdb='" + pdb + '\'' +
                ", chain='" + chain + '\'' +
                ", accession='" + accession + '\'' +
                ", ecNumber='" + ecNumber + '\'' +
                '}';
    }

    public PdbToEnzymeMap(String pdb, String chain, String accession, String ecNumber) {
        this.pdb = pdb;
        this.chain = chain;
        this.accession = accession;
        this.ecNumber = ecNumber;
    }

    @Override
    public String getKey() {
        return String.format("%s-%s-%s", this.getChain(), this.getPdb(), this.getAccession());
    }
}
