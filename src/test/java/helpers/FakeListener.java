package helpers;

import java.util.EventListener;

public interface FakeListener extends EventListener {
    void operationCalled(FakeModel data);
}
