package com.example.reteasocialagui.Domain;

import java.io.Serial;
import java.io.Serializable;

public class Entity<ID> implements Serializable {

    @Serial
    private static final long serialVersionUID = 7331115341259248461L;
    private ID id;

    @Override
    public String toString() {
        return "id = " + id + " ";
    }

    public ID getId() {
        return id;
    }
    public void setId(ID id) {
        this.id = id;
    }
}