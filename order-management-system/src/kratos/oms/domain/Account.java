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

import java.util.UUID;

public class Account {
    private UUID id;
    private String username;
    private String hashedPassword;
    private String fullName;
    private Role role;
    private Profile profile;

    public Account(UUID id, String username, String hashedPassword, String fullName, Role role, Profile profile) {
        this.id = id;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.fullName = fullName;
        this.role = role;
        this.profile = profile;
    }

    public Account(String username, String hashedPassword, String fullName, Role role) {
        this(UUID.randomUUID(), username, hashedPassword, fullName, role, null);
    }

    public Account(String username, String hashedPassword, String fullName, Role role, Profile profile) {
        this(UUID.randomUUID(), username, hashedPassword, fullName, role, profile);
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public Profile getProfile() {
        return profile;
    }

    public Role getRole() {
        return role;
    }

    public String getFullName() {
        return fullName;
    }
}
