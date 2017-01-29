package com.francois.algo.pdb.core.search;

import com.francois.algo.pdb.core.domain.PdbChainDescriptor;

public interface PdbChainMatcher {
    boolean doesMatch(PdbChainDescriptor pdbChainToMatch);
}
