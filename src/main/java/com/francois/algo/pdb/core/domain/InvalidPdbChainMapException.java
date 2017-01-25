package com.francois.algo.pdb.core.domain;

public class InvalidPdbChainMapException extends AppException {
    private InvalidPdbChainMapException(String message) {
        super(message);
    }

    public static InvalidPdbChainMapException INVALID_PFAM_MAPPING(String mapping) {
        return new InvalidPdbChainMapException(String.format("Following '%s' PDB chain to PFAM mapping is NOT valid! " +
                "Valid mapping must contain values for PDB, CHAIN, SP_PRIMARY and PFAM_ID separated by TABS, " +
                "ex: '101m\tA\tP02185\tPF00042'", mapping));
    }

    public static InvalidPdbChainMapException INVALID_ENZYM_MAPPING(String mapping) {
        return new InvalidPdbChainMapException(String.format("Following '%s' PDB chain to PFAM mapping is NOT valid! " +
                "Valid mapping must contain values for PDB, CHAIN, ACCESSION and EC_NUMBER separated by TABS, " +
                "ex: '102l\tA\tP00720\t3.2.1.17'", mapping));
    }
}
