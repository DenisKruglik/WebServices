package by.deniskruglik.socialnetwork.data;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import java.security.ProviderException;
import java.sql.SQLException;

public class FriendRelationDAOProvider implements Provider<Dao<FriendRelation, Integer>> {
    @Inject
    private ConnectionSource connectionSource;

    @Override
    public Dao<FriendRelation, Integer> get() {
        try {
            return DaoManager.createDao(connectionSource, FriendRelation.class);
        } catch (SQLException e) {
            throw new ProviderException("FriendRelationDAO provision failed", e);
        }
    }
}
