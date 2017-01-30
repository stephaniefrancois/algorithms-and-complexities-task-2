package com.francois.algo.pdb.common;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.function.Consumer;

public final class ListenersManager<TListener extends EventListener>
        implements IRaiseEvents<TListener> {

    private final java.util.List<TListener> listeners;

    public ListenersManager() {
        this.listeners = new ArrayList<>();
    }

    public void notifyListeners(Consumer<TListener> listenerNotifier) {
        if (listeners.size() > 0) {
            listeners.forEach(listener -> listenerNotifier.accept(listener));
        }
    }

    @Override
    public void addListener(TListener listenerToAdd) {
        if (!listeners.contains(listenerToAdd)) {
            listeners.add(listenerToAdd);
        }
    }

    @Override
    public void removeListener(TListener listenerToRemove) {
        if (listeners.contains(listenerToRemove)) {
            listeners.remove(listenerToRemove);
        }
    }
}
