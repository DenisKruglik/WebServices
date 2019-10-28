package by.deniskruglik.socialnetwork.data;

import by.deniskruglik.socialnetwork.exception.ConnectionSourceGettingException;
import com.j256.ormlite.support.ConnectionSource;

public abstract class AbstractConnectionSourceProvider {
    private static ConnectionSource instance = null;

    public ConnectionSource get() throws ConnectionSourceGettingException {
        if (instance == null) {
            instance = init();
        }
        return instance;
    }

    protected abstract ConnectionSource init() throws ConnectionSourceGettingException;
}
