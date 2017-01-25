package com.francois.algo.pdb.data;

import com.francois.algo.pdb.core.domain.InvalidPdbChainMapException;
import com.francois.algo.pdb.core.domain.PdbToEzymeMap;
import helpers.NullLogger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.jupiter.api.Assertions.assertThrows;

final class PdbChainToEnzymeParserTest {

    @BeforeAll
    public static void onceExecutedBeforeAll() {
        NullLogger.configure();
    }

    @Test
    public void GivenEmptyListWhenParsedThenShouldReturnNoMappings() throws InvalidPdbChainMapException {
        // Given
        List<String> data = Collections.EMPTY_LIST;
        IDataParser<PdbToEzymeMap> sut = new PdbChainToEnzymeParser();

        // When
        List<PdbToEzymeMap> result = sut.parseToModel(data);

        // Then
        assertThat(result, empty());
    }

    @Test
    public void GivenListWithNoHeaderWhenParsedShouldReturnNoMappings() throws InvalidPdbChainMapException {
        // Given
        List<String> data = Collections.singletonList("# 2017/01/16 - 13:55");
        IDataParser<PdbToEzymeMap> sut = new PdbChainToEnzymeParser();

        // When
        List<PdbToEzymeMap> result = sut.parseToModel(data);

        // Then
        assertThat(result, empty());
    }

    @Test
    public void GivenListWithHeaderButNoValuesWhenParsedShouldReturnNoMappings() throws InvalidPdbChainMapException {
        // Given
        List<String> data = Arrays.asList("# 2017/01/16 - 13:55", "PDB\tCHAIN\tACCESSION\tEC_NUMBER");
        IDataParser<PdbToEzymeMap> sut = new PdbChainToEnzymeParser();

        // When
        List<PdbToEzymeMap> result = sut.parseToModel(data);

        // Then
        assertThat(result, empty());
    }

    @Test
    public void GivenListWithValueWhenParsedShouldReturnMapping() throws InvalidPdbChainMapException {
        // Given
        List<String> data = Arrays.asList("# 2017/01/16 - 13:55",
                "PDB\tCHAIN\tACCESSION\tEC_NUMBER",
                "102l\tA\tP00720\t3.2.1.17");
        IDataParser<PdbToEzymeMap> sut = new PdbChainToEnzymeParser();
        PdbToEzymeMap expectedMap = new PdbToEzymeMap("102l", "A", "P00720", "3.2.1.17");

        // When
        List<PdbToEzymeMap> result = sut.parseToModel(data);

        // Then
        assertThat(result, hasSize(1));
        assertThat(result, hasItem(expectedMap));
    }

    @Test
    public void GivenListWithInvalidValueWhenParsedShouldThrow() {
        // Given
        List<String> data = Arrays.asList("# 2017/01/16 - 13:55",
                "PDB\tCHAIN\tACCESSION\tEC_NUMBER",
                "102l\tA\t3.2.1.17");
        IDataParser<PdbToEzymeMap> sut = new PdbChainToEnzymeParser();

        // When
        // Then
        assertThrows(InvalidPdbChainMapException.class, () -> sut.parseToModel(data));
    }

    @Test
    public void GivenListWithMultipleValuesWhenParsedShouldReturnMappingsForAll() throws InvalidPdbChainMapException {
        // Given
        List<String> data = Arrays.asList("# 2017/01/16 - 13:55",
                "PDB\tCHAIN\tACCESSION\tEC_NUMBER",
                "102l\tA\tP00720\t3.2.1.17",
                "103l\tA\tP00720\t3.2.1.17",
                "104l\tA\tP00720\t3.2.1.17");
        IDataParser<PdbToEzymeMap> sut = new PdbChainToEnzymeParser();

        // When
        List<PdbToEzymeMap> result = sut.parseToModel(data);

        // Then
        assertThat(result, hasItems(new PdbToEzymeMap("102l", "A", "P00720", "3.2.1.17"),
                new PdbToEzymeMap("103l", "A", "P00720", "3.2.1.17"),
                new PdbToEzymeMap("104l", "A", "P00720", "3.2.1.17")));
    }

    @Test
    public void GivenListWhenValueMissingAccessionThenAccessionShouldBeEmpty() throws InvalidPdbChainMapException {
        // Given
        List<String> data = Arrays.asList("# 2017/01/16 - 13:55",
                "PDB\tCHAIN\tACCESSION\tEC_NUMBER",
                "3it1\tA\t \t3.1.3.2");
        IDataParser<PdbToEzymeMap> sut = new PdbChainToEnzymeParser();

        // When
        List<PdbToEzymeMap> result = sut.parseToModel(data);

        // Then
        assertThat(result, hasItem(new PdbToEzymeMap("3it1", "A", "", "3.1.3.2")));
    }
}