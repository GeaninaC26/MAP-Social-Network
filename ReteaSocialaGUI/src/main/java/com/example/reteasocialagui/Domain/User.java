package com.example.reteasocialagui.Domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends Entity<Long>{
    private String nume;
    private String oras;
    private int varsta;

    private List<Long> fr;

    public User() {
        this.nume = "";
        this.oras = "";
        this.varsta = 0;
    }

    public User(String nume, String oras, int varsta) {
        this.nume = nume;
        this.oras = oras;
        this.varsta = varsta;
        fr = new ArrayList<Long>();
    }


    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setOras(String oras) {
        this.oras = oras;
    }

    public void setVarsta(int varsta) {
        this.varsta = varsta;
    }

    public void addFriend(User u){
        fr.add(u.getId());
    }

    public void deleteFriend(User u){
        fr.remove(u.getId());
    }

    public String getNume() {
        return nume;
    }

    public String getOras() {
        return oras;
    }

    public int getVarsta() {
        return varsta;
    }

    public List<Long> getFriends(){
        return fr;
    }

    @Override
    public String toString() {
        return nume + " id(" + getId() + ")";

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return varsta == user.varsta && Objects.equals(nume, user.nume) &&
                Objects.equals(oras, user.oras) && Objects.equals(fr, user.fr);
    }
    @Override
    public int hashCode() {
        return Objects.hash(nume, oras, varsta, fr);
    }
}
