package com.example.reteasocialagui.Repository;

import com.example.reteasocialagui.Domain.Friend;
import com.example.reteasocialagui.Domain.Friendship;
import com.example.reteasocialagui.Domain.User;
import com.example.reteasocialagui.Domain.Validation.FriendshipValidator;
import com.example.reteasocialagui.Repository.Repository;
import javafx.util.Pair;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FriendshipDbRepository implements Repository<Long, Friendship>{
    private String url;

    private String id;
    private String username;
    private String password;
    private FriendshipValidator validator;

    public FriendshipDbRepository(String url, String username, String password, FriendshipValidator validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Optional<Friendship> findOne(Long idFriendship) {
        Optional<Friendship> friends = Optional.empty();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendship where id="
                     + idFriendship);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long id1 = resultSet.getLong("first_user");
                Long id2 = resultSet.getLong("second_user");

                Friendship f = new Friendship(getUserById(id1), getUserById(id2));
                f.setId(id);
                friends = Optional.of(f);
            }
            return friends;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;
    }

    public User getUserById(Long idUser){
        User user = new User();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from \"user\" where id=" + idUser);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String nume = resultSet.getString("nume");
                String oras = resultSet.getString("oras");
                int varsta = resultSet.getInt("varsta");

                user.setId(id);
                user.setNume(nume);
                user.setOras(oras);
                user.setVarsta(varsta);

            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friends = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendship WHERE status='acceptat'");
             ResultSet resultSet = statement.executeQuery()) {

            return getFriendships(friends, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;
    }

    private Iterable<Friendship> getFriendships(Set<Friendship> friends, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            Long id = resultSet.getLong("id");
            Long id1 = resultSet.getLong("first_user");
            Long id2 = resultSet.getLong("second_user");

            Friendship f = new Friendship(getUserById(id1), getUserById(id2));
            f.setId(id);
            friends.add(f);
        }
        return friends;
    }

    public Iterable<Friendship> findAllPendings() {
        Set<Friendship> friends = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendship WHERE status='pending'");
             ResultSet resultSet = statement.executeQuery()) {

            return getFriendships(friends, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;
    }
    public Iterable<Friend> getOfUser(Long idUser){
        User friend;
        Set<Friend> friends = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendship " +
                     "WHERE (first_user=" + idUser + " or second_user=" + idUser + ") and status='acceptat'");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long id1 = resultSet.getLong("first_user");
                Long id2 = resultSet.getLong("second_user");

                if(id1 == idUser)
                     friend = getUserById(id2);
                else
                     friend = getUserById(id1);
                Friend fr = new Friend(id, friend);
                friends.add(fr);
            }
            return friends;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;
    }

    @Override
    public Optional<Friendship> save(Friendship entity) {
        String sql = "insert into friendship(first_user, second_user, status) values (?, ?, ?)";
        validator.validate(entity);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getUser1().getId());
            ps.setLong(2, entity.getUser2().getId());
            ps.setString(3, "acceptat");

            ps.executeUpdate();
        } catch (SQLException e) {
            //e.printStackTrace();
            return Optional.ofNullable(entity);
        }
        return Optional.empty();
    }
    public Optional<Friendship> sendRequest(Friendship entity) {
        String sql = "insert into friendship(first_user, second_user, status) values (?, ?, ?)";
        validator.validate(entity);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getUser1().getId());
            ps.setLong(2, entity.getUser2().getId());
            ps.setString(3, "pending");
            ps.executeUpdate();
        } catch (SQLException e) {
            //e.printStackTrace();
            return Optional.ofNullable(entity);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Friendship> delete(Long idFriendship) {
        String sql = "delete from friendship where id =" + idFriendship;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.executeQuery();
        }
        catch (SQLException e) {
            //e.printStackTrace();
            return findOne(idFriendship);
        }
        return Optional.empty();
    }

    public Iterable<Friend> findSentRequests(Long idUser){
        Set<Friend> friends = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT id, second_user from friendship WHERE status='pending' " +
                     "and first_user=" + idUser);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long id1 = resultSet.getLong("second_user");

                User u = getUserById(id1);
                Friend f = new Friend(id, u);
                f.setId(id);
                friends.add(f);
            }
            return friends;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;
    }
    public Iterable<Friend> findRequests(Long idUser) {
        Set<Friend> friends = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT id, first_user from friendship WHERE status='pending' " +
                     "and second_user=" + idUser);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long id1 = resultSet.getLong("first_user");

                User u = getUserById(id1);
                Friend f = new Friend(id, u);
                f.setId(id);
                friends.add(f);
            }
            return friends;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;
    }

    @Override
    public Optional<Friendship> update(Friendship f) {
        Optional<Friendship> before = findOne(f.getId());
        String sql = "update friendship set status='acceptat'" +
                "where id =" + f.getId();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.executeUpdate();
        }
        catch (SQLException e) {
            //e.printStackTrace();
            return before;
        }
        return Optional.empty();
    }
}
