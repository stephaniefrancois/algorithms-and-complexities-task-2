package com.francois.algo.pdb.data;

import com.francois.algo.pdb.core.IDataParser;
import com.francois.algo.pdb.core.domain.InvalidPdbChainMapException;
import com.francois.algo.pdb.core.domain.PdbToPfamMap;
import com.francois.algo.pdb.data.PdbChainToPfamParser;
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

final class PdbChainToPfamParserTest {

    @BeforeAll
    public static void onceExecutedBeforeAll() {
        NullLogger.configure();
    }

    @Test
    public void GivenEmptyListWhenParsedThenShouldReturnNoMappings() throws InvalidPdbChainMapException {
        // Given
        List<String> data = Collections.EMPTY_LIST;
        IDataParser<PdbToPfamMap> sut = new PdbChainToPfamParser();

        // When
        List<PdbToPfamMap> result = sut.parseToModel(data);

        // Then
        assertThat(result, empty());
    }

    @Test
    public void GivenListWithNoHeaderWhenParsedShouldReturnNoMappings() throws InvalidPdbChainMapException {
        // Given
        List<String> data = Collections.singletonList("# 2017/01/16 - 13:55");
        IDataParser<PdbToPfamMap> sut = new PdbChainToPfamParser();

        // When
        List<PdbToPfamMap> result = sut.parseToModel(data);

        // Then
        assertThat(result, empty());
    }

    @Test
    public void GivenListWithHeaderButNoValuesWhenParsedShouldReturnNoMappings() throws InvalidPdbChainMapException {
        // Given
        List<String> data = Arrays.asList("# 2017/01/16 - 13:55", "PDB\tCHAIN\tSP_PRIMARY\tPFAM_ID");
        IDataParser<PdbToPfamMap> sut = new PdbChainToPfamParser();

        // When
        List<PdbToPfamMap> result = sut.parseToModel(data);

        // Then
        assertThat(result, empty());
    }

    @Test
    public void GivenListWithValueWhenParsedShouldReturnMapping() throws InvalidPdbChainMapException {
        // Given
        List<String> data = Arrays.asList("# 2017/01/16 - 13:55",
                "PDB\tCHAIN\tSP_PRIMARY\tPFAM_ID",
                "101m\tA\tP02185\tPF00042");
        IDataParser<PdbToPfamMap> sut = new PdbChainToPfamParser();
        PdbToPfamMap expectedMap = new PdbToPfamMap("101m", "A", "P02185", "PF00042");

        // When
        List<PdbToPfamMap> result = sut.parseToModel(data);

        // Then
        assertThat(result, hasSize(1));
        assertThat(result, hasItem(expectedMap));
    }

    @Test
    public void GivenListWithInvalidValueWhenParsedShouldThrow() {
        // Given
        List<String> data = Arrays.asList("# 2017/01/16 - 13:55",
                "PDB\tCHAIN\tSP_PRIMARY\tPFAM_ID",
                "102l\tA\t3.2.1.17");
        IDataParser<PdbToPfamMap> sut = new PdbChainToPfamParser();

        // When
        // Then
        assertThrows(InvalidPdbChainMapException.class, () -> sut.parseToModel(data));
    }

    @Test
    public void GivenListWithMultipleValuesWhenParsedShouldReturnMappingsForAll() throws InvalidPdbChainMapException {
        // Given
        List<String> data = Arrays.asList("# 2017/01/16 - 13:55",
                "PDB\tCHAIN\tSP_PRIMARY\tPFAM_ID",
                "101m\tA\tP02185\tPF00042",
                "102l\tA\tP00720\tPF00959",
                "102m\tA\tP02185\tPF00042");
        IDataParser<PdbToPfamMap> sut = new PdbChainToPfamParser();

        // When
        List<PdbToPfamMap> result = sut.parseToModel(data);

        // Then
        assertThat(result, hasItems(new PdbToPfamMap("101m", "A", "P02185", "PF00042"),
                new PdbToPfamMap("102l", "A", "P00720", "PF00959"),
                new PdbToPfamMap("102m", "A", "P02185", "PF00042")));
    }
}