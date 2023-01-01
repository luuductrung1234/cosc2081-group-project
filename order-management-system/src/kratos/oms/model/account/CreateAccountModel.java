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

package kratos.oms.model.account;

import kratos.oms.domain.Role;
import kratos.oms.seedwork.*;

public class CreateAccountModel {
    @NotBlank
    @Length(max = 30, min = 5, message = "Given username must have valid length between 5 and 30 characters.")
    @NotContain(value = ",", message = "Given username should not contains ',' character")
    private String username;
    @NotBlank
    @Length(max = 50, min = 8, message = "Given password must have valid length between 8 and 50 characters.")
    private String password;
    @NotBlank
    @Length(max = 30, min = 1, message = "Given full-name must have valid length between 1 and 30 characters.")
    @NotContain(value = ",", message = "Given full-name should not contains ',' character")
    private String fullName;
    @NotNull
    private Role role;
    @Match(regex = "^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$",
            message = "Phone is not matched! Try something like '0376672168' or '+84 (037) 6672168'")
    private String phone;
    @Match(regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
            message = "Email is not matched! Try something like 'username@domain.com'")
    private String email;
    @Length(max = 100, message = "Given address must have valid length, maximum 100 characters.")
    private String address;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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
