package com.francois.algo.pdb.core;

import com.francois.algo.pdb.core.domain.AppException;
import com.francois.algo.pdb.core.domain.PdbChainDescriptor;
import com.francois.algo.pdb.core.domain.PdbToEnzymeMap;
import com.francois.algo.pdb.core.domain.PdbToPfamMap;
import com.francois.algo.pdb.data.IRepository;
import helpers.NullLogger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

public final class PdbChainDataAggregatorTest {

    @BeforeAll
    public static void onceExecutedBeforeAll() {
        NullLogger.configure();
    }

    @Test
    public void GivenBothDataSourcesHaveSingleItemWhenRequestedThenReturnCombinedResult()
            throws AppException {

        // Given
        IRepository<PdbToPfamMap> pfamRepository = Mockito.mock(IRepository.class);
        IRepository<PdbToEnzymeMap> enzymeRepository = Mockito.mock(IRepository.class);
        List<PdbToEnzymeMap> ezymes = Arrays.asList(
                new PdbToEnzymeMap("102l", "A", "P00720", "3.2.1.17"));
        List<PdbToPfamMap> pfams = Arrays.asList(
                new PdbToPfamMap("102l", "A", "P00720", "PF00959"));

        when(pfamRepository.getAll()).thenReturn(pfams);
        when(enzymeRepository.getAll()).thenReturn(ezymes);

        IRepository<PdbChainDescriptor> sut = new PdbChainDataAggregator(pfamRepository, enzymeRepository);

        // When
        List<PdbChainDescriptor> result = sut.getAll();

        // Then
        assertThat(result, hasItem(new PdbChainDescriptor("102l", "A", "P00720",
                "3.2.1.17", "P00720", "PF00959")));
    }

    @Test
    public void GivenBothDataSourcesHaveMultipleItemsWhenRequestedThenReturnCombinedResult()
            throws AppException {

        // Given
        IRepository<PdbToPfamMap> pfamRepository = Mockito.mock(IRepository.class);
        IRepository<PdbToEnzymeMap> enzymeRepository = Mockito.mock(IRepository.class);
        List<PdbToEnzymeMap> ezymes = Arrays.asList(
                new PdbToEnzymeMap("102l", "A", "P00720", "3.2.1.17"),
                new PdbToEnzymeMap("10mh", "A", "P05102", "2.1.1.37"));
        List<PdbToPfamMap> pfams = Arrays.asList(
                new PdbToPfamMap("102l", "A", "P00720", "PF00959"),
                new PdbToPfamMap("10mh", "A", "P05102", "PF00145"));

        when(pfamRepository.getAll()).thenReturn(pfams);
        when(enzymeRepository.getAll()).thenReturn(ezymes);

        IRepository<PdbChainDescriptor> sut = new PdbChainDataAggregator(pfamRepository, enzymeRepository);

        // When
        List<PdbChainDescriptor> result = sut.getAll();

        // Then
        assertThat(result, hasItems(new PdbChainDescriptor("102l", "A", "P00720",
                "3.2.1.17", "P00720", "PF00959"),
                new PdbChainDescriptor("10mh", "A", "P05102",
                "2.1.1.37", "P05102", "PF00145")));
    }

    @Test
    public void GivenOnlyPfamDataSourcesHaveMultipleItemsWhenRequestedThenReturnCombinedResult()
            throws AppException {

        // Given
        IRepository<PdbToPfamMap> pfamRepository = Mockito.mock(IRepository.class);
        IRepository<PdbToEnzymeMap> enzymeRepository = Mockito.mock(IRepository.class);
        List<PdbToEnzymeMap> ezymes = Arrays.asList(
                new PdbToEnzymeMap("102l", "A", "P00720", "3.2.1.17"));
        List<PdbToPfamMap> pfams = Arrays.asList(
                new PdbToPfamMap("102l", "A", "P00720", "PF00959"),
                new PdbToPfamMap("10mh", "A", "P05102", "PF00145"));

        when(pfamRepository.getAll()).thenReturn(pfams);
        when(enzymeRepository.getAll()).thenReturn(ezymes);

        IRepository<PdbChainDescriptor> sut = new PdbChainDataAggregator(pfamRepository, enzymeRepository);

        // When
        List<PdbChainDescriptor> result = sut.getAll();

        // Then
        assertThat(result, hasItems(
                new PdbChainDescriptor("102l", "A", "P00720",
                "3.2.1.17", "P00720", "PF00959"),
                new PdbChainDescriptor("10mh", "A", "",
                "", "P05102", "PF00145")));
    }

    @Test
    public void GivenOnlyEnzymesDataSourcesHaveMultipleItemsWhenRequestedThenReturnCombinedResult()
            throws AppException {

        // Given
        IRepository<PdbToPfamMap> pfamRepository = Mockito.mock(IRepository.class);
        IRepository<PdbToEnzymeMap> enzymeRepository = Mockito.mock(IRepository.class);
        List<PdbToEnzymeMap> ezymes = Arrays.asList(
                new PdbToEnzymeMap("102l", "A", "P00720", "3.2.1.17"),
                new PdbToEnzymeMap("10mh", "A", "P05102", "2.1.1.37"));
        List<PdbToPfamMap> pfams = Arrays.asList(
                new PdbToPfamMap("102l", "A", "P00720", "PF00959"));

        when(pfamRepository.getAll()).thenReturn(pfams);
        when(enzymeRepository.getAll()).thenReturn(ezymes);

        IRepository<PdbChainDescriptor> sut = new PdbChainDataAggregator(pfamRepository, enzymeRepository);

        // When
        List<PdbChainDescriptor> result = sut.getAll();

        // Then
        assertThat(result, hasItems(new PdbChainDescriptor("102l", "A", "P00720",
                "3.2.1.17", "P00720", "PF00959"),
                new PdbChainDescriptor("10mh", "A", "P05102",
                "2.1.1.37", "", "")));
    }

    @Test
    public void GivenMultiplePfamRecordsForSamePdbChainWhenRequestedThenReturnCombinedResult()
            throws AppException {

        // Given
        IRepository<PdbToPfamMap> pfamRepository = Mockito.mock(IRepository.class);
        IRepository<PdbToEnzymeMap> enzymeRepository = Mockito.mock(IRepository.class);
        List<PdbToEnzymeMap> ezymes = Arrays.asList(
                new PdbToEnzymeMap("11gs", "A", "P09211", "2.5.1.18"),
                new PdbToEnzymeMap("11gs", "B", "P09211", "2.5.1.18"));
        List<PdbToPfamMap> pfams = Arrays.asList(
                new PdbToPfamMap("11gs", "A", "P09211", "PF02798"),
                new PdbToPfamMap("11gs", "A", "P09211", "PF14497"),
                new PdbToPfamMap("11gs", "B", "P09211", "PF02798"));

        when(pfamRepository.getAll()).thenReturn(pfams);
        when(enzymeRepository.getAll()).thenReturn(ezymes);

        IRepository<PdbChainDescriptor> sut = new PdbChainDataAggregator(pfamRepository, enzymeRepository);

        // When
        List<PdbChainDescriptor> result = sut.getAll();

        // Then
        assertThat(result, hasItems(
                new PdbChainDescriptor("11gs", "A", "P09211","2.5.1.18", "P09211", "PF02798"),
                new PdbChainDescriptor("11gs", "A", "P09211","2.5.1.18", "P09211", "PF14497"),
                new PdbChainDescriptor("11gs", "B", "P09211","2.5.1.18", "P09211", "PF02798")));
    }
}