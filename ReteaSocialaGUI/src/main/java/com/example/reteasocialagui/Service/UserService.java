package com.example.reteasocialagui.Service;

import com.example.reteasocialagui.Domain.User;
import com.example.reteasocialagui.Repository.UserDbRepository;

import java.util.Optional;

public class UserService {
    private UserDbRepository usersRepo;

    public UserService(UserDbRepository usersRepo) {
        this.usersRepo = usersRepo;
    }

    public void addUser(String nume, String oras, int varsta){
        User u = new User(nume, oras, varsta);
        usersRepo.save(u);
    }
    public void delUser(Long idUser){
        usersRepo.delete(idUser);
    }

    public void updateUser(Long idUser, String nume, String oras, int varsta){
        User u = new User(nume, oras, varsta);
        u.setId(idUser);
        usersRepo.update(u);
    }

    public Iterable<User> findAll(){
        return usersRepo.findAll();
    }

    public Optional<User> findOne(Long idUser){
        return usersRepo.findOne(idUser);
    }
}
