package com.francois.algo.pdb.data;

import com.francois.algo.pdb.data.FileBasedRepository;
import com.francois.algo.pdb.data.IDataParser;
import com.francois.algo.pdb.data.IRepository;
import com.francois.algo.pdb.core.domain.AppException;
import com.francois.algo.pdb.core.domain.DataFileNotFoundException;
import helpers.FakeDataModel;
import helpers.NullLogger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public final class FileBasedRepositoryTests {
    @BeforeAll
    public static void onceExecutedBeforeAll() {
        NullLogger.configure();
    }

    @Test
    public void ShouldReadFileWithSingleLineOfData() throws AppException {
        // given
        String dataFile = "src/test/resources/sample.txt";
        IDataParser<FakeDataModel> parserMock = Mockito.mock(IDataParser.class);
        IRepository<FakeDataModel> sut = new FileBasedRepository<>(dataFile, parserMock);

        // when
        sut.getAll();

        // then
        final ArgumentCaptor<List<String>> argument = ArgumentCaptor.forClass((Class) List.class);
        verify(parserMock).parseToModel(argument.capture());
        assertThat(argument.getValue(), hasItem("# 2017/01/16 - 13:43"));
    }

    @Test
    public void ShouldReadFileWithTwoLinesOfData() throws AppException {
        // given
        String dataFile = "src/test/resources/sample.txt";
        IDataParser<FakeDataModel> parserMock = Mockito.mock(IDataParser.class);
        IRepository<FakeDataModel> sut = new FileBasedRepository<>(dataFile, parserMock);

        // when
        sut.getAll();

        // then
        final ArgumentCaptor<List<String>> argument = ArgumentCaptor.forClass((Class) List.class);
        verify(parserMock).parseToModel(argument.capture());
        assertThat(argument.getValue(), hasItem("PDB\tCHAIN\tSP_PRIMARY\tPFAM_ID"));
    }

    @Test
    public void ShouldReadFileWithAnyNumberLinesOfData() throws AppException {
        // given
        String dataFile = "src/test/resources/sample.txt";
        IDataParser<FakeDataModel> parserMock = Mockito.mock(IDataParser.class);
        IRepository<FakeDataModel> sut = new FileBasedRepository<>(dataFile, parserMock);

        // when
       sut.getAll();

        // then
        final ArgumentCaptor<List<String>> argument = ArgumentCaptor.forClass((Class) List.class);
        verify(parserMock).parseToModel(argument.capture());
        assertThat(argument.getValue().size(),is(10));
        assertThat(argument.getValue(), hasItem("104m\tA\tP02185\tPF00042"));
    }

    @Test
    public void ShouldReturnParsedData() throws AppException {
        // given
        String dataFile = "src/test/resources/sample.txt";
        IDataParser<FakeDataModel> parserMock = Mockito.mock(IDataParser.class);
        IRepository<FakeDataModel> sut = new FileBasedRepository<>(dataFile, parserMock);
        List<FakeDataModel> parsedData = Collections.EMPTY_LIST;
        when(parserMock.parseToModel(anyList())).thenReturn(parsedData);

        // when
        List<FakeDataModel> result = sut.getAll();

        // then
        assertThat(result, is(parsedData));
    }

    @Test
    public void ShouldThrowIfDataFileIsNotFound() throws IOException, DataFileNotFoundException {
        // given
        String dataFile = "src/test/resources/invalid-file.txt";
        IDataParser<FakeDataModel> parserMock = Mockito.mock(IDataParser.class);
        IRepository<FakeDataModel> sut = new FileBasedRepository<>(dataFile, parserMock);
        // when
        // then
        DataFileNotFoundException thrown = assertThrows(DataFileNotFoundException.class, () -> sut.getAll());
        assertThat(thrown.getMessage(), containsString("invalid-file.txt"));
    }
}
