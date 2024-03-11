package com.example.reteasocialagui.Domain;
import java.util.Objects;
import com.example.reteasocialagui.Domain.Entity;

public class Friendship extends Entity<Long> {
    private User user1;
    private User user2;

    @Override
    public String toString() {
        return "Friendship{ " + super.toString() +
                "user1 = " + user1 +
                ", user2 = " + user2 +
                '}';
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public Friendship(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Friendship that)) return false;
        return (Objects.equals(user1, that.user1) && Objects.equals(user2, that.user2)) ||
                (Objects.equals(user1, that.user2) && Objects.equals(user2, that.user1));
    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user1, user2);
    }
}