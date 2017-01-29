package com.francois.algo.pdb.core.search;

import helpers.NullLogger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public final class EcNumberBasedSearchCriteriaValidatorTest {
    @BeforeAll
    public static void onceExecutedBeforeAll() {
        NullLogger.configure();
    }

    @Test
    public void GivenSearchCriteriaWhenHasFirstAndSecondLevelsThenPass() {
        // Given
        SearchCriteriaValidator sut = new EcNumberBasedSearchCriteriaValidator();

        // When
        boolean result = sut.valid("1.1.-.-");

        // Then
        assertThat(result, is(equalTo(true)));
    }

    @Test
    public void GivenSearchCriteriaWhenHasFirstAndThirdLevelsThenPass() {
        // Given
        SearchCriteriaValidator sut = new EcNumberBasedSearchCriteriaValidator();

        // When
        boolean result = sut.valid("1.-.1.-");

        // Then
        assertThat(result, is(equalTo(true)));
    }

    @Test
    public void GivenSearchCriteriaWhenHasFirstAndFourthLevelsThenPass() {
        // Given
        SearchCriteriaValidator sut = new EcNumberBasedSearchCriteriaValidator();

        // When
        boolean result = sut.valid("1.-.-.1");

        // Then
        assertThat(result, is(equalTo(true)));
    }

    @Test
    public void GivenSearchCriteriaWhenHasNoFirstLevelThenFail() {
        // Given
        SearchCriteriaValidator sut = new EcNumberBasedSearchCriteriaValidator();

        // When
        boolean result = sut.valid("-.1.1.1");

        // Then
        assertThat(result, is(equalTo(false)));
    }

    @Test
    public void GivenSearchCriteriaWhenHasOnlyFirstLevelThenFail() {
        // Given
        SearchCriteriaValidator sut = new EcNumberBasedSearchCriteriaValidator();

        // When
        boolean result = sut.valid("1.-.-.-");

        // Then
        assertThat(result, is(equalTo(false)));
    }

    @Test
    public void GivenSearchCriteriaWhenAnyLevelIsNotNumericThenFail() {
        // Given
        SearchCriteriaValidator sut = new EcNumberBasedSearchCriteriaValidator();

        // When
        boolean result = sut.valid("1.a.1.1");

        // Then
        assertThat(result, is(equalTo(false)));
    }

    @Test
    public void GivenSearchCriteriaWhenLessThanFourLevelsSpecifiedThenFail() {
        // Given
        SearchCriteriaValidator sut = new EcNumberBasedSearchCriteriaValidator();

        // When
        boolean result = sut.valid("1.2.3");

        // Then
        assertThat(result, is(equalTo(false)));
    }

    @Test
    public void GivenSearchCriteriaWhenMoreThanFourLevelsSpecifiedThenFail() {
        // Given
        SearchCriteriaValidator sut = new EcNumberBasedSearchCriteriaValidator();

        // When
        boolean result = sut.valid("1.2.3.4.5");

        // Then
        assertThat(result, is(equalTo(false)));
    }

}