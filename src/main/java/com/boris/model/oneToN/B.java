package com.boris.model.oneToN;

import com.boris.model.Origin;

import javax.persistence.*;

@Entity
public class B extends Origin {

    private String name;

    public B() {

    }

    public B(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
