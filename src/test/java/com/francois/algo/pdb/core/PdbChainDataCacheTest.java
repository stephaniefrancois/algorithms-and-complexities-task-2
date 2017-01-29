package com.francois.algo.pdb.core;

import com.francois.algo.pdb.core.domain.AppException;
import com.francois.algo.pdb.core.domain.PdbChainDescriptor;
import com.francois.algo.pdb.data.IRepository;
import helpers.NullLogger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public final class PdbChainDataCacheTest {

    @BeforeAll
    public static void onceExecutedBeforeAll() {
        NullLogger.configure();
    }

    @Test
    public void GivenFirstRequestWhenDataIsRequestThenLoadItFromUnderlyingProvider()
            throws AppException {

        // Given
        IRepository<PdbChainDescriptor> chainDataProvider = Mockito.mock(IRepository.class);
               List<PdbChainDescriptor> data = Collections.emptyList();

        when(chainDataProvider.getAll()).thenReturn(data);

        IRepository<PdbChainDescriptor> sut = new PdbChainDataCache(chainDataProvider);

        // When
        List<PdbChainDescriptor> result = sut.getAll();

        // Then
        verify(chainDataProvider, times(1)).getAll();
        assertThat(result, is(data));
    }

    @Test
    public void GivenSecondRequestWhenDataIsRequestThenLoadItFromCache()
            throws AppException {

        // Given
        IRepository<PdbChainDescriptor> chainDataProvider = Mockito.mock(IRepository.class);
               List<PdbChainDescriptor> data = Collections.emptyList();

        when(chainDataProvider.getAll()).thenReturn(data);

        IRepository<PdbChainDescriptor> sut = new PdbChainDataCache(chainDataProvider);

        // When
        sut.getAll();
        List<PdbChainDescriptor> result = sut.getAll();

        // Then
        verify(chainDataProvider, times(1)).getAll();
        assertThat(result, is(data));
    }
}