package by.deniskruglik.socialnetwork.data;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import java.security.ProviderException;
import java.sql.SQLException;

public class LikeDAOProvider implements Provider<Dao<Like, Integer>> {
    @Inject
    private ConnectionSource connectionSource;

    public Dao<Like, Integer> get() {
        try {
            return DaoManager.createDao(connectionSource, Like.class);
        } catch (SQLException e) {
            throw new ProviderException("LikeDAO provision failed", e);
        }
    }
}
