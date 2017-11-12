package com.boris.model.oneToN;

import com.boris.model.Origin;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class A extends Origin {
    private String name;

    private Set<B> slaves = new HashSet<>();

    private Set<C> otherSlaves = new HashSet<>();

    private Set<D> slaves3 = new HashSet<>();

    private Set<E> slaves4 = new HashSet<>();


    public A(){

    }

    public A(String name) {
        this.name = name;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "a_id")
    public Set<B> getSlaves() {
        return slaves;
    }

    public void setSlaves(Set<B> slaves) {
        this.slaves = slaves;
    }


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "a_id", nullable = false, updatable = false) //cм. тест testCascaseInsertSlavesNoBackRefsWithoutUpdates
    public Set<C> getOtherSlaves() {
        return otherSlaves;
    }

    public void setOtherSlaves(Set<C> otherSlaves) {
        this.otherSlaves = otherSlaves;
    }


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "a_id")
    public Set<D> getSlaves3() {
        return slaves3;
    }

    public void setSlaves3(Set<D> slaves3) {
        this.slaves3 = slaves3;
    }


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "a_id", nullable = false, updatable = false)
    public Set<E> getSlaves4() {
        return slaves4;
    }

    public void setSlaves4(Set<E> slaves3) {
        this.slaves4 = slaves3;
    }
}
