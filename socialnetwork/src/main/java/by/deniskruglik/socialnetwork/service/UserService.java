package by.deniskruglik.socialnetwork.service;

import by.deniskruglik.socialnetwork.data.FriendInvitation;
import by.deniskruglik.socialnetwork.data.FriendInvitationStatus;
import by.deniskruglik.socialnetwork.data.FriendRelation;
import by.deniskruglik.socialnetwork.data.User;
import by.deniskruglik.socialnetwork.exception.*;
import by.deniskruglik.socialnetwork.utils.Cypher;
import by.deniskruglik.socialnetwork.validation.NicknameValidator;
import by.deniskruglik.socialnetwork.validation.PasswordValidator;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static by.deniskruglik.socialnetwork.data.User.NICKNAME_FIELD;
import static by.deniskruglik.socialnetwork.data.User.PASSWORD_HASH_FIELD;

@Singleton
public class UserService {
    private static final long USER_SEARCH_PAGE_SIZE = 20;

    @Inject
    private Dao<User, Integer> userDao;

    @Inject
    private NicknameValidator nicknameValidator;

    @Inject
    private PasswordValidator passwordValidator;

    @Inject
    private Dao<FriendInvitation, Integer> friendInvitationDao;

    @Inject
    private Dao<FriendRelation, Integer> friendRelationDao;

    public User login(String login, String password) throws LoginException {
        if (!validateLoginAndPassword(login, password)) {
            throw new LoginException("Invalid login or password passed");
        }
        QueryBuilder<User, Integer> queryBuilder = userDao.queryBuilder();
        String passwordHash = Cypher.encode(password);
        PreparedQuery<User> preparedQuery;
        try {
            queryBuilder
                    .where()
                    .eq(NICKNAME_FIELD, login)
                    .and()
                    .eq(PASSWORD_HASH_FIELD, passwordHash);
            preparedQuery = queryBuilder.prepare();
        } catch (SQLException e) {
            throw new UserServiceException("Failed to login", e);
        }
        try {
            List<User> result = userDao.query(preparedQuery);
            if (result.isEmpty()) {
                throw new LoginException("Incorrect login or password");
            }
            if (result.size() > 1) {
                throw new IllegalStateException("More than 1 instance of user found");
            }
            return result.get(0);
        } catch (SQLException e) {
            throw new UserServiceException("Failed to fetch user", e);
        }
    }

    public User register(String login, String password, String repeatedPassword) throws RegistrationException {
        if (!password.equals(repeatedPassword)) {
            throw new RegistrationException("Repeated password doesn't match password");
        }
        if (!validateLoginAndPassword(login, password)) {
            throw new RegistrationException("Invalid login or password passed");
        }
        QueryBuilder<User, Integer> queryBuilder = userDao.queryBuilder();
        long recordsCount;
        try {
            queryBuilder
                    .setCountOf(true)
                    .where()
                    .eq(NICKNAME_FIELD, login);
            PreparedQuery<User> preparedQuery = queryBuilder.prepare();
            recordsCount = userDao.countOf(preparedQuery);
        } catch (SQLException e) {
            throw new UserServiceException("Failed to register", e);
        }
        if (recordsCount > 0) {
            throw new RegistrationException("User with this nickname already exists");
        }
        User user = new User();
        user.setNickname(login);
        String passwordHash = Cypher.encode(password);
        user.setPasswordHash(passwordHash);
        try {
            userDao.create(user);
            userDao.refresh(user);
            return user;
        } catch (SQLException e) {
            throw new UserServiceException("Failed to register", e);
        }
    }

    private boolean validateLoginAndPassword(String login, String password) {
        return nicknameValidator.validate(login) && passwordValidator.validate(password);
    }

    public List<FriendInvitation> getFriendInvitationsFromUser(User user) {
        Map<String, Object> fieldValues = new HashMap<>();
        fieldValues.put("sender_id", user.getId());
        try {
            return friendInvitationDao.queryForFieldValues(fieldValues);
        } catch (SQLException e) {
            throw new UserServiceException("Failed to fetch friend invitations", e);
        }
    }

    public List<FriendInvitation> getFriendInvitationsToUser(User user) {
        Map<String, Object> fieldValues = new HashMap<>();
        fieldValues.put("receiver_id", user.getId());
        fieldValues.put("status", FriendInvitationStatus.OPEN);
        try {
            return friendInvitationDao.queryForFieldValues(fieldValues);
        } catch (SQLException e) {
            throw new UserServiceException("Failed to fetch friend invitations", e);
        }
    }

    public User getUserById(int id) {
        try {
            return userDao.queryForId(id);
        } catch (SQLException e) {
            throw new UserServiceException("Failed to fetch user", e);
        }
    }

    public FriendInvitation sendFriendInvitation(int receiverId, User sender) throws FriendInvitationException {
        try {
            Map<String, Object> fieldValues = new HashMap<>();
            fieldValues.put("receiver_id", receiverId);
            fieldValues.put("sender_id", sender.getId());
            List<FriendInvitation> existingInvitations = friendInvitationDao.queryForFieldValues(fieldValues);
            if (!existingInvitations.isEmpty()) {
                throw new FriendInvitationException("Invitation to this user already exists");
            }
            FriendInvitation invitation = new FriendInvitation();
            User receiver = userDao.queryForId(receiverId);
            invitation.setReceiver(receiver);
            invitation.setSender(sender);
            invitation.setStatus(FriendInvitationStatus.OPEN);
            friendInvitationDao.create(invitation);
            friendInvitationDao.refresh(invitation);
            return invitation;
        } catch (SQLException e) {
            throw new UserServiceException("Failed to send friend invitation", e);
        }
    }

    public FriendInvitation rejectFriendInvitation(int invitationId, User user) throws FriendInvitationException {
        try {
            FriendInvitation invitation = friendInvitationDao.queryForId(invitationId);
            if (invitation.getReceiver().getId() != user.getId()) {
                throw new FriendInvitationException("Invitation is addressed to another user");
            }
            if (invitation.getStatus() == FriendInvitationStatus.REJECTED) {
                throw new FriendInvitationException("Invitation is already rejected");
            }
            invitation.setStatus(FriendInvitationStatus.REJECTED);
            friendInvitationDao.update(invitation);
            return invitation;
        } catch (SQLException e) {
            throw new UserServiceException("Failed to reject friend invitation", e);
        }
    }

    public void acceptFriendInvitation(int invitationId, User user) throws FriendInvitationException {
        try {
            FriendInvitation invitation = friendInvitationDao.queryForId(invitationId);
            if (invitation.getReceiver().getId() != user.getId()) {
                throw new FriendInvitationException("Invitation is addressed to another user");
            }
            User sender = invitation.getSender();
            FriendRelation senderToReceiver = new FriendRelation();
            senderToReceiver.setUser(sender);
            senderToReceiver.setFriend(user);
            FriendRelation receiverToSender = new FriendRelation();
            receiverToSender.setUser(user);
            receiverToSender.setFriend(sender);
            friendRelationDao.create(Arrays.asList(senderToReceiver, receiverToSender));
            friendInvitationDao.delete(invitation);
        } catch (SQLException e) {
            throw new UserServiceException("Failed to accept friend invitation", e);
        }
    }

    public void removeFriend(int friendId, User user) throws FriendException {
        QueryBuilder<FriendRelation, Integer> queryBuilder = friendRelationDao.queryBuilder();
        try {
            Where<FriendRelation, Integer> where = queryBuilder.where();
            where.or(
                    where.and(
                            where.eq("user_id", user.getId()),
                            where.eq("friend_id", friendId)
                    ),
                    where.and(
                            where.eq("user_id", friendId),
                            where.eq("friend_id", user.getId())
                    )
            );
            PreparedQuery<FriendRelation> preparedQuery = queryBuilder.prepare();
            List<FriendRelation> relations = friendRelationDao.query(preparedQuery);
            if (relations.isEmpty()) {
                throw new FriendException("No friend relations found");
            }
            friendRelationDao.delete(relations);
        } catch (SQLException e) {
            throw new UserServiceException("Failed to remove friend", e);
        }
    }

    public List<User> getFriendsForUser(User user) {
        QueryBuilder<User, Integer> userQueryBuilder = userDao.queryBuilder();
        QueryBuilder<User, Integer> friendQueryBuilder = userDao.queryBuilder();
        QueryBuilder<FriendRelation, Integer> friendRelationQueryBuilder = friendRelationDao.queryBuilder();
        try {
            userQueryBuilder.where().idEq(user.getId());
            userQueryBuilder.join("id", "user_id", friendRelationQueryBuilder);
            friendQueryBuilder.join("id", "friend_id", friendRelationQueryBuilder);
            return friendQueryBuilder.query();
        } catch (SQLException e) {
            throw new UserServiceException("Failed to fetch friends", e);
        }
    }

    public List<User> searchUsers(String query, int page) {
        Map<String, Object> fieldValues = new HashMap<>();
        QueryBuilder<User, Integer> queryBuilder = userDao.queryBuilder();
        try {
            queryBuilder
                    .limit(USER_SEARCH_PAGE_SIZE)
                    .offset(USER_SEARCH_PAGE_SIZE * page)
                    .where()
                    .like(NICKNAME_FIELD, "%" + query + "%");
            return queryBuilder.query();
        } catch (SQLException e) {
            throw new UserServiceException("Failed to search users", e);
        }
    }
}
