package com.francois.algo.pdb.core.search;

import com.francois.algo.pdb.core.PdbChainFinder;
import com.francois.algo.pdb.core.domain.AppException;
import com.francois.algo.pdb.core.domain.PdbChainDescriptor;
import com.francois.algo.pdb.data.IRepository;
import helpers.NullLogger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.mockito.Mockito.when;

public final class ListBasedPdbChainFinderTest {

    @BeforeAll
    public static void onceExecutedBeforeAll() {
        NullLogger.configure();
    }

    @Test
    public void GivenListOfPdbChainsWhenSearchingByEcNumberThenReturnMatchingRecords() throws AppException {
        // Given
        IRepository<PdbChainDescriptor> provider = Mockito.mock(IRepository.class);
        PdbChainFinder sut = new ListBasedPdbChainFinder(provider);
        List<PdbChainDescriptor> data = Arrays.asList(getPdbChain("102l", "A", "3.2.1.17"),
                getPdbChain("10gs", "A", "2.5.1.18"),
                getPdbChain("10gs", "B", "2.5.1.18"),
                getPdbChain("103l", "A", "3.2.1.17"));

        when(provider.getAll()).thenReturn(data);

        // When
        List<PdbChainDescriptor> result = sut.findMatchingPdbChains(pdbChainToMatch ->
                pdbChainToMatch.getEcNumber().equals("2.5.1.18"), (o1, o2) -> 0);

        // Then
        assertThat(result, hasItems(
                getPdbChain("10gs", "A", "2.5.1.18"),
                getPdbChain("10gs", "B", "2.5.1.18")));
    }

    @Test
    public void GivenListOfPdbChainsWhenSearchingByEcNumberAndNoMatchesFoundThenReturnEmptyList() throws AppException {
        // Given
        IRepository<PdbChainDescriptor> provider = Mockito.mock(IRepository.class);
        PdbChainFinder sut = new ListBasedPdbChainFinder(provider);
        List<PdbChainDescriptor> data = Arrays.asList(getPdbChain("102l", "A", "3.2.1.17"),
                getPdbChain("103l", "A", "3.2.1.17"));

        when(provider.getAll()).thenReturn(data);

        // When
        List<PdbChainDescriptor> result = sut.findMatchingPdbChains(pdbChainToMatch ->
                pdbChainToMatch.getEcNumber().equals("2.5.1.18"), (o1, o2) -> 0);

        // Then
        assertThat(result, empty());
    }

    private PdbChainDescriptor getPdbChain(String pdb, String chain, String ecNumber) {
        return new PdbChainDescriptor(pdb, chain, "", ecNumber, "", "");
    }
}