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
//    we have fixed currency for this problem. can we set the currency here?

    public Product(String name, double price, String currency, Category category) {
        this(UUID.randomUUID(), name, price, currency, category);
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

    public double getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public String toString() {
        String category=(this.category==null)?"none":this.category.getName();
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                ", category=" + category +
                '}';
    }
}
