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
        List<String> fields = new ArrayList<>() {{
            add(id.toString());
            add(name);
        }};
        return String.join(",", fields);
    }

    /**
     * override static method Domain.deserialize
     * @param data serialized string data
     * @return new instance of Category
     */
    public static Category deserialize(String data) {
        if (Helpers.isNullOrEmpty(data))
            throw new IllegalArgumentException("data to deserialize should not be empty!");
        String[] fields = data.split(",", 2);
        return new Category(UUID.fromString(fields[0]), fields[1]);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Category{name='" + name + "'}";
    }
}
