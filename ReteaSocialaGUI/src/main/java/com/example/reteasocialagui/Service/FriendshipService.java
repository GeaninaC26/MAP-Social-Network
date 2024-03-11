package com.example.reteasocialagui.Service;

import com.example.reteasocialagui.Domain.Friend;
import com.example.reteasocialagui.Domain.Friendship;
import com.example.reteasocialagui.Domain.User;
import com.example.reteasocialagui.Domain.Validation.ValidationException;
import com.example.reteasocialagui.Repository.FriendshipDbRepository;
import javafx.util.Pair;

import java.util.Objects;
import java.util.Optional;

public class FriendshipService {
    private FriendshipDbRepository friendsRepo;

    public FriendshipService(FriendshipDbRepository friends) {
        this.friendsRepo = friends;
    }
    public boolean prietenie(Long id1, Long id2){
        Iterable<Friendship> fr = friendsRepo.findAll();

        for(Friendship fr1:fr){
            if((Objects.equals(fr1.getUser1().getId(), id1) && Objects.equals(fr1.getUser2().getId(), id2)) ||
                    (Objects.equals(fr1.getUser2().getId(), id1) && Objects.equals(fr1.getUser1().getId(), id2))){
                return false;
            }
        }
        return true;
    }

    public boolean request(Long id1, Long id2){
        Iterable<Friendship> fr = friendsRepo.findAllPendings();

        for(Friendship fr1:fr){
            if((Objects.equals(fr1.getUser1().getId(), id1) && Objects.equals(fr1.getUser2().getId(), id2)) ||
                    (Objects.equals(fr1.getUser2().getId(), id1) && Objects.equals(fr1.getUser1().getId(), id2))){
                return false;
            }
        }
        return true;
    }
    public void addFriendship(Long u1, Long u2){
        if(friendsRepo.getUserById(u1)== null || friendsRepo.getUserById(u2)==null)
            throw new ValidationException("Unul dintre utilizator nu exista");
        if(!(prietenie(u1, u2))) throw new ValidationException("Exista deja o prietenie intre cei doi utilizatori");
        User user1 = friendsRepo.getUserById(u1);
        User user2 = friendsRepo.getUserById(u2);
        Friendship fr = new Friendship(user1, user2);
        friendsRepo.save(fr);
    }

    public void sendRequest(Long u1, Long u2){
        if(!(prietenie(u1, u2))) throw new ValidationException("Exista deja o prietenie intre cei doi utilizatori");
        if(u1==u2) throw new ValidationException("Nu se poate stabili o prietenie intre doi utilizatori identici");
        if(!request(u1, u2)) throw new ValidationException("Exista deja o cerere in asteptare intre cei doi!");
        User user1 = friendsRepo.getUserById(u1);
        User user2 = friendsRepo.getUserById(u2);
        Friendship fr = new Friendship(user1, user2);
        friendsRepo.sendRequest(fr);
    }

    public void deleteFriendship(Long id){
        if(friendsRepo.findOne(id).isEmpty())
            throw new ValidationException("Nu exista prietenia cu id-ul dat");
        friendsRepo.delete(id);
    }

    public Iterable<Friendship> findAll(){
        return friendsRepo.findAll();
    }

    public Optional<Friendship> findOne(Long id){
        return friendsRepo.findOne(id);
    }

    public Iterable<Friend> getOfUser(Long idUser){
        return friendsRepo.getOfUser(idUser);
    }
    public Iterable<Friend> findSentRequests(Long idUser){return friendsRepo.findSentRequests(idUser);}

    public Iterable<Friend> findRequests(Long idUser){return friendsRepo.findRequests(idUser);}
}
