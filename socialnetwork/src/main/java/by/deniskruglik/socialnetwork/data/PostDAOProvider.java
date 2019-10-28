package by.deniskruglik.socialnetwork.data;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import java.security.ProviderException;
import java.sql.SQLException;

public class PostDAOProvider implements Provider<Dao<Post, Integer>> {
    @Inject
    private ConnectionSource connectionSource;

    public Dao<Post, Integer> get() {
        try {
            return DaoManager.createDao(connectionSource, Post.class);
        } catch (SQLException e) {
            throw new ProviderException("PostDAO provision failed", e);
        }
    }
}
