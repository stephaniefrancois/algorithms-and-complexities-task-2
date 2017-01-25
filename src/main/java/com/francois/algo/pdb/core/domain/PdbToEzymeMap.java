package com.francois.algo.pdb.core.domain;

public final class PdbToEzymeMap {
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

    public PdbToEzymeMap(String pdb, String chain, String accession, String ecNumber) {
        this.pdb = pdb;
        this.chain = chain;
        this.accession = accession;
        this.ecNumber = ecNumber;
    }
}
