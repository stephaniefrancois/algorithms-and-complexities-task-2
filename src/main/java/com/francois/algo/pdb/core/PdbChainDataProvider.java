package com.francois.algo.pdb.core;

import com.francois.algo.pdb.core.domain.PdbChainDescriptor;

import java.util.Set;

public interface PdbChainDataProvider {
    Set<PdbChainDescriptor> getDataSortedByPdbChain();
    Set<PdbChainDescriptor> getDataSortedByEcNumber();
}
