package com.pon02.Assignment10.entity;


import java.util.Objects;

public class StaffStatus {

  private int id;
  private String staffStatus;

  public StaffStatus(int id, String staffStatus) {
    this.id = id;
    this.staffStatus = staffStatus;
  }

  public int getId() {
    return id;
  }

  public String getStaffStatus() {
    return staffStatus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    StaffStatus staffStatus = (StaffStatus) o;
    return id == staffStatus.id &&
        staffStatus.equals(staffStatus.staffStatus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, staffStatus);
  }

  @Override
  public String toString() {
    return String.format("StaffStatus{id=%d, staffStatus='%s'}", id, staffStatus);
  }
}
