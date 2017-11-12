package com.boris.model.oneToN;

import com.boris.model.Origin;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class E extends Origin {

    private String name;

    private A master;

    public E() {

    }

    public E(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaster(A master) {
        this.master = master;
    }

    @ManyToOne
    @JoinColumn(name = "a_id", insertable = false, updatable = false)
    public A getMaster() {
        return master;
    }
}
