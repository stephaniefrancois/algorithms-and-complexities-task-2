package com.francois.algo.pdb.data;

import com.francois.algo.pdb.core.domain.InvalidPdbChainMapException;
import com.francois.algo.pdb.core.domain.PdbToPfamMap;

import java.util.Arrays;

public final class PdbChainToPfamParser extends PdbChainParser<PdbToPfamMap> {
    public PdbChainToPfamParser() {
        super(Arrays.asList("PDB", "CHAIN", "SP_PRIMARY", "PFAM_ID"),
                InvalidPdbChainMapException::INVALID_PFAM_MAPPING);
    }

    @Override
    protected PdbToPfamMap buildMap(String[] tokens) {
        return new PdbToPfamMap(tokens[0].trim(),
                tokens[1].trim(),
                tokens[2].trim(),
                tokens[3].trim());
    }
}