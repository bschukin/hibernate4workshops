package com.boris.model.dm;

import com.boris.model.Origin;

import javax.persistence.Entity;

/**
 * Created by b.schukin on 09.11.2017.
 */
@Entity()
public class Gender extends Origin {


  private String gender;
  private Boolean isClassic;

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public Boolean getClassic() {
    return isClassic;
  }

  public void setClassic(Boolean classic) {
    isClassic = classic;
  }
}
