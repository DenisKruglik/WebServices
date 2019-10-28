package by.deniskruglik.socialnetwork.data;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import java.security.ProviderException;
import java.sql.SQLException;

public class UserDAOProvider implements Provider<Dao<User, Integer>> {
    @Inject
    private ConnectionSource connectionSource;

    public Dao<User, Integer> get() {
        try {
            return DaoManager.createDao(connectionSource, User.class);
        } catch (SQLException e) {
            throw new ProviderException("UserDAO provision failed", e);
        }
    }
}
