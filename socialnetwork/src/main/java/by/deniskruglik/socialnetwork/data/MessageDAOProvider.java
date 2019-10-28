package by.deniskruglik.socialnetwork.data;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import java.security.ProviderException;
import java.sql.SQLException;

public class MessageDAOProvider implements Provider<Dao<Message, Integer>> {
    @Inject
    private ConnectionSource connectionSource;

    @Override
    public Dao<Message, Integer> get() {
        try {
            return DaoManager.createDao(connectionSource, Message.class);
        } catch (SQLException e) {
            throw new ProviderException("MessageDAO provision failed", e);
        }
    }
}
