package com.francois.algo.pdb.core.sorting;

import com.francois.algo.pdb.core.domain.PdbChainDescriptor;

import java.util.Comparator;

/*
    Comparator sorts values in Ascending order, first by PDB and then by CHAIN
 */
public final class AscendingPdbAndChainComparator implements Comparator<PdbChainDescriptor> {
    @Override
    public int compare(PdbChainDescriptor left, PdbChainDescriptor right) {
        if (left.getPdb().equals(right.getPdb())) {
            return left.getChain().compareTo(right.getChain());
        }

        return left.getPdb().compareTo(right.getPdb());
    }
}