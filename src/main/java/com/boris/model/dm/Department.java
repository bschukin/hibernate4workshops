package com.boris.model.dm;

import com.boris.model.Origin;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by b.schukin on 09.11.2017.
 */
@Entity
public class Department extends Origin{

  private String name;

  private Department parent;


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @ManyToOne()
  @JoinColumn(name = "parent_id")
  public Department getParent() {
    return parent;
  }

  public void setParent(Department parent) {
    this.parent = parent;
  }
}
