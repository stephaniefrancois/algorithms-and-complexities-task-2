package com.francois.algo.pdb.core;

import com.francois.algo.pdb.core.domain.AppException;
import com.francois.algo.pdb.core.domain.PdbChainDescriptor;
import com.francois.algo.pdb.core.search.PdbChainMatcher;

import java.util.Comparator;
import java.util.List;

public interface PdbChainFinder {
    List<PdbChainDescriptor> findMatchingPdbChains(PdbChainMatcher matcher,
                                                   Comparator<PdbChainDescriptor> comparator) throws AppException;
}
