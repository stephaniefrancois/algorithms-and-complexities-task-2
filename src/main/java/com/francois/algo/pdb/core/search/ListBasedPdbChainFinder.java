package com.francois.algo.pdb.core.search;

import com.francois.algo.pdb.core.PdbChainFinder;
import com.francois.algo.pdb.core.domain.AppException;
import com.francois.algo.pdb.core.domain.PdbChainDescriptor;
import com.francois.algo.pdb.data.IRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ListBasedPdbChainFinder implements PdbChainFinder {
    private final IRepository<PdbChainDescriptor> provider;

    public ListBasedPdbChainFinder(IRepository<PdbChainDescriptor> provider) {
        Objects.requireNonNull(provider);
        this.provider = provider;
    }

    @Override
    public List<PdbChainDescriptor> findMatchingPdbChains(PdbChainMatcher matcher,
                                                          Comparator<PdbChainDescriptor> comparator) throws AppException {
        Objects.requireNonNull(matcher);
        Objects.requireNonNull(comparator);

        return this.provider.getAll()
                .stream()
                .filter(matcher::doesMatch)
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}
