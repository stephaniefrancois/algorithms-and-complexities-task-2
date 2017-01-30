package com.francois.algo.pdb.core.sorting;

import com.francois.algo.pdb.core.domain.PdbChainDescriptor;
import helpers.NullLogger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

public final class AscendingPdbAndChainComparatorTest {

    @BeforeAll
    public static void onceExecutedBeforeAll() {
        NullLogger.configure();
    }

    @Test
    public void GivenTwoDescriptorsWhenTheyHaveSamePdbAndSameChainThenShouldReturnZero() {
        // Given
        Comparator<PdbChainDescriptor> sut = new AscendingPdbAndChainComparator();
        PdbChainDescriptor rightDescriptor = getPdbChain("102l", "A");
        PdbChainDescriptor leftDescriptor = getPdbChain("102l", "A");

        // When
        int result = sut.compare(leftDescriptor, rightDescriptor);

        // Then
        assertThat(result, is(equalTo(0)));
    }

    @Test
    public void GivenTwoDescriptorsWhenLeftHasGreaterPdbValueThenShouldReturnPositiveValue() {
        // Given
        Comparator<PdbChainDescriptor> sut = new AscendingPdbAndChainComparator();
        PdbChainDescriptor rightDescriptor = getPdbChain("102l", "B");
        PdbChainDescriptor leftDescriptor = getPdbChain("103l", "A");

        // When
        int result = sut.compare(leftDescriptor, rightDescriptor);

        // Then
        assertThat(result, is(greaterThan(0)));
    }

    @Test
    public void GivenTwoDescriptorsWhenLeftHasLesserPdbValueThenShouldReturnNegativeValue() {
        // Given
        Comparator<PdbChainDescriptor> sut = new AscendingPdbAndChainComparator();
        PdbChainDescriptor rightDescriptor = getPdbChain("103l", "B");
        PdbChainDescriptor leftDescriptor = getPdbChain("102l", "A");

        // When
        int result = sut.compare(leftDescriptor, rightDescriptor);

        // Then
        assertThat(result, is(lessThan(0)));
    }

    @Test
    public void GivenTwoDescriptorsWhenLeftHasEqualPdbAndGreaterChainValueThenShouldReturnPositiveValue() {
        // Given
        Comparator<PdbChainDescriptor> sut = new AscendingPdbAndChainComparator();
        PdbChainDescriptor rightDescriptor = getPdbChain("102l", "A");
        PdbChainDescriptor leftDescriptor = getPdbChain("102l", "B");

        // When
        int result = sut.compare(leftDescriptor, rightDescriptor);

        // Then
        assertThat(result, is(greaterThan(0)));
    }

    @Test
    public void GivenTwoDescriptorsWhenLeftHasEqualPdbAndLesserChainValueThenShouldReturnNegativeValue() {
        // Given
        Comparator<PdbChainDescriptor> sut = new AscendingPdbAndChainComparator();
        PdbChainDescriptor rightDescriptor = getPdbChain("102l", "B");
        PdbChainDescriptor leftDescriptor = getPdbChain("102l", "A");

        // When
        int result = sut.compare(leftDescriptor, rightDescriptor);

        // Then
        assertThat(result, is(lessThan(0)));
    }

    private PdbChainDescriptor getPdbChain(String pdb, String chain) {
        return new PdbChainDescriptor(pdb, chain, "", "", "", "");
    }
}