/*
  RMIT University Vietnam
  Course: COSC2081 Programming 1
  Semester: 2022C
  Assessment: Assignment 3
  Author: Luu Duc Trung
  ID: s3951127
  Acknowledgement:
    - Ravi K Thapliyal, "Regular expression to match standard 10-digit phone number", Stackoverflow, https://stackoverflow.com/a/16699507
    - Baeldung, "Email Validation in Java", Baeldung, https://www.baeldung.com/java-email-validation-regex
*/

package kratos.oms.model.customer;

import kratos.oms.seedwork.*;

import java.util.UUID;

public class UpdateProfileModel {
    private UUID customerId;

    @NotBlank
    @Length(max = 30, min = 1, message = "Given full-name must have valid length between 1 and 30 characters.")
    @NotContain(value = ",", message = "Given full-name should not contains ',' character")
    private String fullName;

    @Match(regex = "^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$",
            message = "Phone is not matched! Try something like '0376672168' or '+84 (037) 6672168'")
    private String phone;

    // RFC 5322 Expression
    // ^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$
    // Strict Regular Expression
    // ^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$
    @Match(regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
            message = "Email is not matched! Try something like 'username@domain.com'")
    private String email;

    @Length(max = 100, message = "Given address must have valid length, maximum 100 characters.")
    private String address;

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
