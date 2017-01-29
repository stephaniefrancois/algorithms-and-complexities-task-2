package com.francois.algo.pdb.core.search;

import com.francois.algo.pdb.core.domain.AppException;
import com.francois.algo.pdb.core.domain.PdbChainDescriptor;
import helpers.NullLogger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public final class PdbAndChainBasedPdbChainMatcherTest {
    @BeforeAll
    public static void onceExecutedBeforeAll() {
        NullLogger.configure();
    }

    @Test
    public void GivenPdbChainWhenMatcherMatchesThenReturnTrue() throws AppException {
        // Given
        PdbChainMatcher sut = new PdbAndChainBasedPdbChainMatcher("103l", "A");
        PdbChainDescriptor pdbChain = getPdbChain("103l", "A");

        // When
        boolean result = sut.doesMatch(pdbChain);

        // Then
        assertThat(result, is(equalTo(true)));
    }

    @Test
    public void GivenPdbChainWhenMatcherFailsToMatchPdbThenReturnFalse() throws AppException {
        // Given
        PdbChainMatcher sut = new PdbAndChainBasedPdbChainMatcher("103l", "A");
        PdbChainDescriptor pdbChain = getPdbChain("X", "A");

        // When
        boolean result = sut.doesMatch(pdbChain);

        // Then
        assertThat(result, is(equalTo(false)));
    }

    @Test
    public void GivenPdbChainWhenMatcherFailsToMatchChainThenReturnFalse() throws AppException {
        // Given
        PdbChainMatcher sut = new PdbAndChainBasedPdbChainMatcher("103l", "A");
        PdbChainDescriptor pdbChain = getPdbChain("103l", "X");

        // When
        boolean result = sut.doesMatch(pdbChain);

        // Then
        assertThat(result, is(equalTo(false)));
    }

    @Test
    public void GivenNullWhenMatchingThenReturnFalse() throws AppException {
        // Given
        PdbChainMatcher sut = new PdbAndChainBasedPdbChainMatcher("103l", "A");

        // When
        boolean result = sut.doesMatch(null);

        // Then
        assertThat(result, is(equalTo(false)));
    }

    private PdbChainDescriptor getPdbChain(String pdb, String chain) {
        return new PdbChainDescriptor(pdb, chain, "", "", "", "");
    }
}