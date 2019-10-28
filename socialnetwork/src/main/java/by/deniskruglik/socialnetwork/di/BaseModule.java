package by.deniskruglik.socialnetwork.di;

import by.deniskruglik.socialnetwork.data.*;
import by.deniskruglik.socialnetwork.exception.ConnectionSourceGettingException;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

public class BaseModule extends AbstractModule {
    @Override
    protected void configure() {
        bindListener(Matchers.any(), new Log4JTypeListener());
        bind(new TypeLiteral<Dao<User, Integer>>() {})
                .toProvider(new UserDAOProvider());
        bind(new TypeLiteral<Dao<Post, Integer>>() {})
                .toProvider(new PostDAOProvider());
        bind(new TypeLiteral<Dao<Like, Integer>>() {})
                .toProvider(new LikeDAOProvider());
        bind(new TypeLiteral<Dao<FriendInvitation, Integer>>() {})
                .toProvider(new FriendInvitationDAOProvider());
        bind(new TypeLiteral<Dao<FriendRelation, Integer>>() {})
                .toProvider(new FriendRelationDAOProvider());
        bind(new TypeLiteral<Dao<Chat, Integer>>() {})
                .toProvider(new ChatDAOProvider());
        bind(new TypeLiteral<Dao<ChatParticipation, Integer>>() {})
                .toProvider(new ChatParticipationDAOProvider());
        bind(new TypeLiteral<Dao<Message, Integer>>() {})
                .toProvider(new MessageDAOProvider());
    }

    @Provides
    public ConnectionSource provideConnectionSource() throws ConnectionSourceGettingException {
        AbstractConnectionSourceProvider provider = new JdbcPooledConnectionSourceProvider();
        return provider.get();
    }
}
