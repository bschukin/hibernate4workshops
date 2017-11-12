package com.boris.model.oneToN;

import com.boris.model.Origin;

import javax.persistence.Entity;

@Entity
public class C extends Origin {

    private String name;

    public C() {

    }

    public C(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
