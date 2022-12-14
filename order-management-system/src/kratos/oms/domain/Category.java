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

public class Category extends Domain<UUID>{
    private String name;

    public Category(UUID id, String name) {
        super(id);
        this.name = name;
    }

    public Category(String name) {
        this(UUID.randomUUID(), name);
    }

    @Override
    public String serialize() {
        return null;
    }

    /**
     * override static method Domain.deserialize
     * @param data serialized string data
     * @return new instance of Account
     */
    public static Account deserialize(String data) {
        return null;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
