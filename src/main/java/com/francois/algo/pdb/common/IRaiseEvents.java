package com.francois.algo.pdb.common;

import java.util.EventListener;

public interface IRaiseEvents<TListener extends EventListener> {
    void addListener(TListener listenerToAdd);

    void removeListener(TListener listenerToRemove);
}
