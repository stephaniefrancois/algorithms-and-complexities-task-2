package com.francois.algo.pdb.core.search;

import com.francois.algo.pdb.core.domain.AppException;
import com.francois.algo.pdb.core.domain.PdbChainDescriptor;
import helpers.NullLogger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public final class PartialEcNumberBasedPdbChainMatcherTest {
    @BeforeAll
    public static void onceExecutedBeforeAll() {
        NullLogger.configure();
    }

    @Test
    public void GivenEcNumberWhenMatcherMatchesThenReturnTrue() throws AppException {
        // Given
        SearchCriteriaValidator validatorMock = Mockito.mock(SearchCriteriaValidator.class);
        when(validatorMock.valid(anyString())).thenReturn(true);
        PdbChainMatcher sut = new PartialEcNumberBasedPdbChainMatcher("3.2.1.17", validatorMock);
        PdbChainDescriptor pdbChain = getPdbChain("103l", "A", "3.2.1.17");

        // When
        boolean result = sut.doesMatch(pdbChain);

        // Then
        assertThat(result, is(equalTo(true)));
    }

    @Test
    public void GivenNullWhenMatchingThenReturnFalse() throws AppException {
        // Given
        SearchCriteriaValidator validatorMock = Mockito.mock(SearchCriteriaValidator.class);
        when(validatorMock.valid(anyString())).thenReturn(true);
        PdbChainMatcher sut = new PartialEcNumberBasedPdbChainMatcher("3.2.1.17", validatorMock);

        // When
        boolean result = sut.doesMatch(null);

        // Then
        assertThat(result, is(equalTo(false)));
    }

    @Test
    public void GivenEcNumberWhenSearchArgumentIsInvalidThenReturnFalse() throws AppException {
        // Given
        SearchCriteriaValidator validatorMock = Mockito.mock(SearchCriteriaValidator.class);
        when(validatorMock.valid(anyString())).thenReturn(false);
        // When
        // Then
        assertThrows(InvalidSearchArgumentException.class,
                () -> new PartialEcNumberBasedPdbChainMatcher("3.2.1.17", validatorMock));
    }

    @TestFactory
    Stream<DynamicTest> MatchCases() {
        List<TestCase> testCases = Arrays.asList(
                new TestCase("2.5.1.7", "2.5.1.-", true),
                new TestCase("2.5.1.7", "2.5.-.7", true),
                new TestCase("2.5.1.7", "2.-.1.7", true),
                new TestCase("2.5.1.7", "2.5.-.-", true),
                new TestCase("2.5.1.7", "2.-.-.-", true),
                new TestCase("2.5.1.7", "2.5.1.7", true),
                new TestCase("2.5.1.-", "2.5.1.-", true),
                new TestCase("2.5.1.-", "2.5.-.7", true),
                new TestCase("2.5.1.-", "2.-.1.7", true),
                new TestCase("2.5.1.-", "2.5.-.-", true),
                new TestCase("2.5.1.-", "2.-.-.-", true),
                new TestCase("2.5.1.-", "2.5.1.0", true),
                new TestCase("2.5.1.-", "2.5.1.9", true),
                new TestCase("2.5.-.7", "2.5.1.-", true),
                new TestCase("2.5.-.7", "2.5.-.7", true),
                new TestCase("2.5.-.7", "2.-.1.7", true),
                new TestCase("2.5.-.7", "2.5.-.-", true),
                new TestCase("2.5.-.7", "2.-.-.-", true),
                new TestCase("2.5.-.7", "2.5.0.7", true),
                new TestCase("2.5.-.7", "2.5.9.7", true),
                new TestCase("2.-.1.7", "2.5.1.-", true),
                new TestCase("2.-.1.7", "2.5.-.7", true),
                new TestCase("2.-.1.7", "2.-.1.7", true),
                new TestCase("2.-.1.7", "2.5.-.-", true),
                new TestCase("2.-.1.7", "2.-.-.-", true),
                new TestCase("2.-.1.7", "2.0.1.7", true),
                new TestCase("2.-.1.7", "2.9.1.7", true),
                new TestCase("2.5.-.-", "2.5.1.-", true),
                new TestCase("2.5.-.-", "2.5.-.7", true),
                new TestCase("2.5.-.-", "2.-.1.7", true),
                new TestCase("2.5.-.-", "2.5.-.-", true),
                new TestCase("2.5.-.-", "2.-.-.-", true),
                new TestCase("2.5.-.-", "2.5.0.0", true),
                new TestCase("2.5.-.-", "2.5.9.9", true),
                new TestCase("2.-.-.7", "2.5.1.-", true),
                new TestCase("2.-.-.7", "2.5.-.7", true),
                new TestCase("2.-.-.7", "2.-.1.7", true),
                new TestCase("2.-.-.7", "2.5.-.-", true),
                new TestCase("2.-.-.7", "2.-.-.-", true),
                new TestCase("2.-.-.7", "2.0.0.7", true),
                new TestCase("2.-.-.7", "2.9.9.7", true),
                new TestCase("2.5.1.7", "0.5.1.7", false),
                new TestCase("2.5.1.7", "2.0.1.7", false),
                new TestCase("2.5.1.7", "2.5.0.7", false),
                new TestCase("2.5.1.7", "2.5.1.0", false),
                new TestCase("2.-.1.-", "2.0.1.0", true),
                new TestCase("2.-.1.-", "2.9.1.9", true)
        );

        return testCases.stream()
                .map(testCase -> DynamicTest.dynamicTest(
                        String.format("Testing if '%s' criteria matches given '%s' EC NUMBER. Expected result is '%b'!",
                                testCase.searchCriteria, testCase.ecNumber, testCase.expectedResult),
                        assertSearchCriteria(testCase)));
    }

    private Executable assertSearchCriteria(TestCase testCase) {
        return () -> {
            SearchCriteriaValidator validatorMock = Mockito.mock(SearchCriteriaValidator.class);
            when(validatorMock.valid(anyString())).thenReturn(true);
            PdbChainMatcher sut = new PartialEcNumberBasedPdbChainMatcher(testCase.getSearchCriteria(), validatorMock);
            PdbChainDescriptor pdbChain = getPdbChain("103l", "A", testCase.getEcNumber());
            assertThat(sut.doesMatch(pdbChain), is(equalTo(testCase.isExpectedResult())));
        };
    }

    private PdbChainDescriptor getPdbChain(String pdb, String chain, String ecNumber) {
        return new PdbChainDescriptor(pdb, chain, "", ecNumber, "", "");
    }

    private final class TestCase {
        private final String searchCriteria;
        private final String ecNumber;
        private final boolean expectedResult;

        public String getSearchCriteria() {
            return searchCriteria;
        }

        public String getEcNumber() {
            return ecNumber;
        }

        public boolean isExpectedResult() {
            return expectedResult;
        }

        public TestCase(String searchCriteria, String ecNumber, boolean expectedResult) {
            this.searchCriteria = searchCriteria;
            this.ecNumber = ecNumber;
            this.expectedResult = expectedResult;
        }
    }
}