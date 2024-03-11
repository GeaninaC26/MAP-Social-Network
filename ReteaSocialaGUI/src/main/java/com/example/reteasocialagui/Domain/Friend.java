package com.example.reteasocialagui.Domain;

public class Friend {
    Long id;
    User friend;
    String data;

    public Friend(Long id, User friend) {
        this.id = id;
        this.friend = friend;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
