package com.francois.algo.pdb.data;

import com.francois.algo.pdb.core.domain.InvalidPdbChainMapException;
import com.francois.algo.pdb.core.domain.PdbToEzymeMap;

import java.util.Arrays;

public final class PdbChainToEnzymeParser extends PdbChainParser<PdbToEzymeMap> {
    public PdbChainToEnzymeParser() {
        super(Arrays.asList("PDB", "CHAIN", "ACCESSION", "EC_NUMBER"),
                InvalidPdbChainMapException::INVALID_ENZYM_MAPPING);
    }

    @Override
    protected PdbToEzymeMap buildMap(String[] tokens) {
        return new PdbToEzymeMap(tokens[0].trim(),
                tokens[1].trim(),
                tokens[2].trim(),
                tokens[3].trim());
    }
}
