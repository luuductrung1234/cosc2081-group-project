/*
  RMIT University Vietnam
  Course: COSC2081 Programming 1
  Semester: 2022C
  Assessment: Assignment 3
  Author: Luu Duc Trung
  ID: s3951127
  Acknowledgement:
    - Colin Hebert, Stackoverflow, https://stackoverflow.com/a/3598792
    - Eduardo Dennis, Stackoverflow, https://stackoverflow.com/a/43294412
*/

package kratos.oms.seedwork;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

public class Helpers {
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static String passwordHash(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    public static void requestSelect(Scanner scanner, String label, List<InputOption<Runnable>> options) {
        System.out.println();
        if (options == null || options.size() == 0)
            throw new IllegalArgumentException("options is required");
        for (int i = 0; i < options.size(); i++) {
            System.out.printf("[%d] %s%n", i, options.get(i).getLabel());
        }
        while (true) {
            System.out.print(label);
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice < 0 || choice >= options.size())
                System.err.printf("There is no option [%d]%n", choice);
            options.get(choice).getAction().run();
            return;
        }
    }

    public static <TClass> void requestInput(Scanner scanner, String label, String fieldName, TClass obj) {
        boolean isValid = false;
        while (!isValid) {
            System.out.print(label);
            String input = scanner.nextLine();
            ValidationResult validationResult = validate(fieldName, input, obj.getClass());
            isValid = validationResult.isValid();
            if (!isValid) {
                System.out.printf("Fail! %s%n", validationResult.getErrorMessage());
                continue;
            }
            Method setter = Arrays.stream(obj.getClass().getMethods())
                    .filter(m -> m.getName().equalsIgnoreCase("set" + fieldName) && m.canAccess(obj))
                    .findFirst().orElse(null);
            if (setter == null)
                throw new IllegalStateException(String.format("Class: %s has no setter for Field: %s", obj.getClass().getName(), fieldName));
            try {
                setter.invoke(obj, input);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException(String.format("Can not access Setter: %s", setter.getName()));
            }
        }
    }

    public static <T> void requestIntInput(Scanner scanner, String label, Class<T> clazz) {

    }

    public static <T> void requestLongInput(Scanner scanner, String label, Class<T> clazz) {

    }

    public static <T> void requestFloatInput(Scanner scanner, String label, Class<T> clazz) {

    }

    public static <T> void requestDoubleInput(Scanner scanner, String label, Class<T> clazz) {

    }

    public static <T> void requestBoolInput(Scanner scanner, String label, Class<T> clazz) {

    }

    public static <TClass, TField> ValidationResult validate(String fieldName, TField value, Class<TClass> clazz) {
        Field field = Arrays.stream(clazz.getFields()).filter(f -> f.getName().equals(fieldName)).findFirst().orElse(null);
        if (field == null)
            throw new IllegalArgumentException(String.format("Field: %s not found in Class: %s", fieldName, clazz.getName()));

        Length lengthAnno = field.getAnnotation(Length.class);
        if (lengthAnno != null && value != null) {
            if (value instanceof String) {
                if (((String) value).length() < lengthAnno.min() || ((String) value).length() > lengthAnno.max())
                    return ValidationResult.inValidInstance(lengthAnno.message());
                if (lengthAnno.min() > 0)
                    return ValidationResult.validInstance();
            } else if (value instanceof List) {
                if (((List<?>) value).size() < lengthAnno.min() || ((List<?>) value).size() > lengthAnno.max())
                    return ValidationResult.inValidInstance(lengthAnno.message());
                if (lengthAnno.min() > 0)
                    return ValidationResult.validInstance();
            } else if (value instanceof Object[]) {
                if (((Object[]) value).length < lengthAnno.min() || ((Object[]) value).length > lengthAnno.max())
                    return ValidationResult.inValidInstance(lengthAnno.message());
                if (lengthAnno.min() > 0)
                    return ValidationResult.validInstance();
            }
        }

        NotBlank notBlankAnno = field.getAnnotation(NotBlank.class);
        if (notBlankAnno != null && value != null & value instanceof String) {
            return ValidationResult.getInstance(((String) value).length() > 0, notBlankAnno.message());
        }

        NotEmpty notEmptyAnno = field.getAnnotation(NotEmpty.class);
        if (notEmptyAnno != null && value != null) {
            if (value instanceof List) {
                return ValidationResult.getInstance(((List<?>) value).size() > 0, notEmptyAnno.message());
            } else if (value instanceof Object[]) {
                return ValidationResult.getInstance(((Object[]) value).length > 0, notEmptyAnno.message());
            }
        }

        NotNull notNullAnno = field.getAnnotation(NotNull.class);
        if (notNullAnno != null) {
            return ValidationResult.getInstance(value != null, notNullAnno.message());
        }
        return ValidationResult.validInstance();
    }
}
