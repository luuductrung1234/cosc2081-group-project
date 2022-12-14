/*
  RMIT University Vietnam
  Course: COSC2081 Programming 1
  Semester: 2022C
  Assessment: Assignment 3
  Author: Luu Duc Trung
  ID: s3951127
  Acknowledgement: n/a
*/

package kratos.oms.domain;

public class Profile {
    private String phone;
    private String email;
    private String address;
    private Membership membership;

    public Profile(String phone, String email, String address, Membership membership) {
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.membership = membership;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public Membership getMembership() {
        return membership;
    }
}
