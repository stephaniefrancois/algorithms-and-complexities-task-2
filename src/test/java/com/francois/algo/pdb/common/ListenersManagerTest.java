package com.francois.algo.pdb.common;

import helpers.FakeListener;
import helpers.FakeModel;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public final class ListenersManagerTest {

    @Test
    public void GivenNoListenersWhenNotifyInvokedThenOperationShouldCompleteSuccessfully() {
        // Given
        ListenersManager<FakeListener> sut = new ListenersManager<>();
        FakeModel data = new FakeModel();

        // When
        sut.notifyListeners(l -> l.operationCalled(data));
        // Then
    }

    @Test
    public void GivenSingleListenerWhenNotifyInvokedThenListenerShouldBeNotified() {
        // Given
        FakeListener listenerMock = Mockito.mock(FakeListener.class);
        ListenersManager<FakeListener> sut = new ListenersManager<>();
        FakeModel data = new FakeModel();
        sut.addListener(listenerMock);

        // When
        sut.notifyListeners(l -> l.operationCalled(data));

        // Then
        verify(listenerMock, times(1)).operationCalled(data);
    }

    @Test
    public void GivenSubscribedListenerWhenListenerUnsubscribesThenItShouldNoLongerReceiveNotifications() {
        // Given
        FakeListener listenerMock = Mockito.mock(FakeListener.class);
        ListenersManager<FakeListener> sut = new ListenersManager<>();
        FakeModel data1 = new FakeModel();
        FakeModel data2 = new FakeModel();
        sut.addListener(listenerMock);

        // When
        sut.notifyListeners(l -> l.operationCalled(data1));
        sut.removeListener(listenerMock);
        sut.notifyListeners(l -> l.operationCalled(data2));

        // Then
        verify(listenerMock, never()).operationCalled(data2);
    }

    @Test
    public void GivenListenerSubscribesMultipleTimesWhenNotificationSendThenItShouldBeNotifiedOnce() {
        // Given
        FakeListener listenerMock = Mockito.mock(FakeListener.class);
        ListenersManager<FakeListener> sut = new ListenersManager<>();
        FakeModel data = new FakeModel();
        sut.addListener(listenerMock);
        sut.addListener(listenerMock);

        // When
        sut.notifyListeners(l -> l.operationCalled(data));

        // Then
        verify(listenerMock, times(1)).operationCalled(data);
    }

    @Test
    public void GivenNoListenersWhenListenerTriesToUnsubscribeThenNoErrorShouldBeThrown() {
        // Given
        FakeListener listenerMock = Mockito.mock(FakeListener.class);
        ListenersManager<FakeListener> sut = new ListenersManager<>();
        FakeModel data = new FakeModel();

        // When
        sut.removeListener(listenerMock);

        // Then
    }

    @Test
    public void GivenMultipleListenersWhenNotificationSentThenAllListenersShouldBeNotified() {
        // Given
        FakeListener listenerMock1 = Mockito.mock(FakeListener.class);
        FakeListener listenerMock2 = Mockito.mock(FakeListener.class);
        ListenersManager<FakeListener> sut = new ListenersManager<>();
        FakeModel data = new FakeModel();
        sut.addListener(listenerMock1);
        sut.addListener(listenerMock2);

        // When
        sut.notifyListeners(l -> l.operationCalled(data));

        // Then
        verify(listenerMock1, times(1)).operationCalled(data);
        verify(listenerMock2, times(1)).operationCalled(data);
    }

    @Test
    public void GivenMultipleListenersWhenOneUnsubscribesThenOtherShouldStillReceiveNotifications() {
        // Given
        FakeListener listenerMock1 = Mockito.mock(FakeListener.class);
        FakeListener listenerMock2 = Mockito.mock(FakeListener.class);
        ListenersManager<FakeListener> sut = new ListenersManager<>();
        FakeModel data1 = new FakeModel();
        FakeModel data2 = new FakeModel();
        sut.addListener(listenerMock1);
        sut.addListener(listenerMock2);

        // When
        sut.notifyListeners(l -> l.operationCalled(data1));
        sut.removeListener(listenerMock1);
        sut.notifyListeners(l -> l.operationCalled(data2));

        // Then
        verify(listenerMock1, never()).operationCalled(data2);
        verify(listenerMock2, times(1)).operationCalled(data2);
    }
}