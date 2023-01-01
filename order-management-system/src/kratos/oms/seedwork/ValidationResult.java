/*
  RMIT University Vietnam
  Course: COSC2081 Programming 1
  Semester: 2022C
  Assessment: Assignment 3
  Author: Luu Duc Trung
  ID: s3951127
  Acknowledgement: n/a
*/

package kratos.oms.seedwork;

public class ValidationResult {
    private boolean isValid;
    private String errorMessage;

    private ValidationResult(boolean isValid, String errorMessage) {
        this.isValid = isValid;
        this.errorMessage = errorMessage;
    }

    public static ValidationResult getInstance(boolean isValid, String errorMessage) {
        return new ValidationResult(isValid, errorMessage);
    }

    public static ValidationResult validInstance() {
        return getInstance(true, null);
    }

    public static ValidationResult inValidInstance(String errorMessage) {
        return getInstance(false, errorMessage);
    }

    public boolean isValid() {
        return isValid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void addError(String errorMessage) {
        isValid = false;
        if (Helpers.isNullOrEmpty(this.errorMessage))
            this.errorMessage = errorMessage;
        else
            this.errorMessage = this.errorMessage + '\n' + errorMessage;
    }
}