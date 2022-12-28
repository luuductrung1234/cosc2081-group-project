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

public class Product extends Domain<UUID> {
    private String name;
    private double price;
    private String currency;
    private Category category;

    public Product(UUID id, String name, double price, String currency, Category category) {
        super(id);
        this.name = name;
        this.price = price;
        this.currency = currency;
        this.category = category;
    }

    public Product(String name, double price, String currency, Category category) {
        this(UUID.randomUUID(), name, price, currency, category);
    }

    @Override
    public String serialize() {
        List<String> fields = new ArrayList<>() {{
            add(id.toString());
            add(name);
            add(String.valueOf(price));
            add(currency);
            add(category.getId().toString());
            add(category.getName());
        }};
        return String.join(",", fields);
    }

    /**
     * override static method Domain.deserialize
     *
     * @param data serialized string data
     * @return new instance of Product
     */
    public static Product deserialize(String data) {
        if (Helpers.isNullOrEmpty(data))
            throw new IllegalArgumentException("data to deserialize should not be empty!");
        String[] fields = data.split(",", 6);
        return new Product(UUID.fromString(fields[0]),
                fields[1],
                Double.parseDouble(fields[2]),
                fields[3],
                new Category(UUID.fromString(fields[4]), fields[5]));
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public Category getCategory() {
        return category;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        String category = (this.category == null) ? "none" : this.category.getName();
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                ", category=" + category +
                '}';
    }
}
