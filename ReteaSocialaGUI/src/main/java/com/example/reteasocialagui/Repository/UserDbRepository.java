package com.example.reteasocialagui.Repository;

import com.example.reteasocialagui.Domain.User;
import com.example.reteasocialagui.Domain.Validation.UserValidator;
import com.example.reteasocialagui.Repository.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UserDbRepository implements Repository<Long, User> {

    private String url;

    private String id;
    private String username;
    private String password;
    private UserValidator validator;

    public UserDbRepository(String url, String username, String password, UserValidator validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }
    @Override
    public Optional<User> findOne(Long idUser) {
        Optional<User> user = Optional.empty();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from \"user\" where id=" + idUser);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String nume = resultSet.getString("nume");
                String oras = resultSet.getString("oras");
                int varsta = resultSet.getInt("varsta");

                User User = new User(nume, oras, varsta);
                User.setId(id);
                user = Optional.of(User);
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from \"user\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String nume = resultSet.getString("nume");
                String oras = resultSet.getString("oras");
                int varsta = resultSet.getInt("varsta");

                User User = new User(nume, oras, varsta);
                PreparedStatement statement1 = connection.prepareStatement("SELECT * from friendship where first_user="
                        + id
                        + " or second_user=" +id);
                ResultSet resultSet1 = statement1.executeQuery();
                while (resultSet1.next()){
                    Long id1 = resultSet1.getLong("first_user");
                    Long id2 = resultSet1.getLong("second_user");
                    if(id1 == id && !findOne(id2).isEmpty()) User.addFriend(findOne(id2).get());
                    if(id2 == id && !findOne(id1).isEmpty()) User.addFriend(findOne(id1).get());
                }
                User.setId(id);
                users.add(User);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Optional<User> save(User entity) {
        String sql = "insert into \"user\"(nume, oras, varsta) values (?, ?, ?)";
        validator.validate(entity);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getNume());
            ps.setString(2, entity.getOras());
            ps.setInt(3, entity.getVarsta());

            ps.executeUpdate();
        } catch (SQLException e) {
            //e.printStackTrace();
            return Optional.ofNullable(entity);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> delete(Long idUser) {

        Optional<User> before = findOne(idUser);
        String sql = "delete from \"user\" where id =" + idUser;
        String sql2 = "DELETE from friendship where first_user =" + idUser +" or second_user=" + idUser;
        try (Connection connection = DriverManager.getConnection(url, username, password))
              {
                  Statement stmt = connection.createStatement();
                  stmt.addBatch(sql);
                  stmt.addBatch(sql2);
                  stmt.executeBatch();
            return before;
        }
        catch (SQLException e) {
            //e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> update(User entity) {
        Optional<User> before = findOne(entity.getId());
        String sql = "update \"user\" set nume='"+ entity.getNume() +
                "', oras='" + entity.getOras() +
                "', varsta=" + entity.getVarsta() + " " +
                "where id =" + entity.getId();
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