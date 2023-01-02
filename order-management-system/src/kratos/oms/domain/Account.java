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

import kratos.oms.seedwork.Helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Account extends Domain<UUID> {
    private final String username;
    private final String hashedPassword;
    private String fullName;
    private final Role role;
    private final Profile profile;

    public Account(UUID id, String username, String hashedPassword, String fullName, Role role, Profile profile) {
        super(id);
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

    public void setMembership(Membership membership) {
        this.profile.setMembership(membership);
    }

    @Override
    public String serialize() {
        List<String> fields = new ArrayList<>() {{
            add(id.toString());
            add(username);
            add(hashedPassword);
            add(fullName);
            add(role.toString());
            add(profile == null || profile.getPhone() == null ? "" : profile.getPhone());
            add(profile == null || profile.getEmail() == null ? "" : profile.getEmail());
            add(profile == null || profile.getAddress() == null ? "" : profile.getAddress().replace(',', '#'));
            add(profile == null || profile.getMembership() == null ? "" : profile.getMembership().toString());
        }};
        return String.join(",", fields);
    }

    /**
     * override static method Domain.deserialize
     *
     * @param data serialized string data
     * @return new instance of Account
     */
    public static Account deserialize(String data) {
        if (Helpers.isNullOrEmpty(data))
            throw new IllegalArgumentException("data to deserialize should not be empty!");
        String[] fields = data.split(",", 9);
        Profile deserializedProfile = Helpers.isNullOrEmpty(fields[5]) && Helpers.isNullOrEmpty(fields[6])
                && Helpers.isNullOrEmpty(fields[7]) && Helpers.isNullOrEmpty(fields[8])
                ? null
                : new Profile(fields[5], fields[6], fields[7].replace('#', ','), Membership.valueOf(fields[8]));
        return new Account(UUID.fromString(fields[0]),
                fields[1],
                fields[2],
                fields[3],
                Role.valueOf(fields[4]),
                deserializedProfile);
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

    public void printDetail() {
        System.out.printf("%-5s: %-10s \n", "Id", this.getId());
        System.out.printf("%-5s: %-10s \n", "Username", this.getUsername());
        System.out.printf("%-5s: %-10s \n", "Full Name", this.getFullName());
        if (this.getProfile() == null)
            return;
        System.out.printf("%-5s: %-20s %-5s: %-10s \n", "Phone", Helpers.isNullOrEmpty(this.getProfile().getPhone()) ? "n/a" : this.getProfile().getPhone(),
                "Email", Helpers.isNullOrEmpty(this.getProfile().getEmail()) ? "n/a" : this.getProfile().getEmail());
        System.out.printf("%-5s: %-10s \n", "Address", Helpers.isNullOrEmpty(this.getProfile().getAddress())
                ? "n/a" : this.getProfile().getAddress());
    }
}
