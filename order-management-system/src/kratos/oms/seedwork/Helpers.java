/*
  RMIT University Vietnam
  Course: COSC2081 Programming 1
  Semester: 2022C
  Assessment: Assignment 3
  Author: Luu Duc Trung
  ID: s3951127
  Acknowledgement:
    - Colin Hebert, "Check whether a String is not Null and not Empty", Stackoverflow, https://stackoverflow.com/a/3598792
    - Eduardo Dennis, "How to hash some String with SHA-256 in Java?", Stackoverflow, https://stackoverflow.com/a/43294412
*/

package kratos.oms.seedwork;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class Helpers {
    public static UUID emptyUuid() {
        return UUID.fromString("00000000-0000-0000-0000-000000000000");
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static String passwordHash(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    public static void loopAction(Scanner scanner, Supplier<Boolean> action) {
        while (true) {
            if (action.get())
                break;
            System.out.print("Do you want to retry? [y/n]: ");
            String answer = scanner.nextLine();
            if (answer != null && (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("y")))
                continue;
            break;
        }
    }

    public static <TOption extends InputOption> TOption requestSelect(Scanner scanner, String label, List<TOption> options) {
        System.out.println();
        if (options == null || options.size() == 0)
            throw new IllegalArgumentException("options is required");
        for (int i = 0; i < options.size(); i++) {
            System.out.printf("[%d] %s%n", i, options.get(i).getLabel());
        }
        while (true) {
            System.out.print(label);
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice < 0 || choice >= options.size()) {
                    Logger.printWarning("There is no option [%d]", choice);
                    continue;
                }
                return options.get(choice);
            } catch (NumberFormatException e) {
                Logger.printWarning("Please enter a valid number!");
            }
        }
    }

    public static <TAction extends Runnable> void requestSelectAction(Scanner scanner, String label, List<ActionOption<TAction>> options) {
        var actionOpt = requestSelect(scanner, label, options);
        actionOpt.getAction().run();
    }

    public static <TValue> TValue requestSelectValue(Scanner scanner, String label, List<ValueOption<TValue>> options) {
        var valueOpt = requestSelect(scanner, label, options);
        return valueOpt.getValue();
    }

    public static <TClass, TField> void requestSelectValue(Scanner scanner, String label, List<ValueOption<TField>> options, String fieldName, TClass obj) {
        TField input = requestSelectValue(scanner, label, options);
        setInputToObject(fieldName, input, obj);
    }

    public static <TField> TField requestInput(Scanner scanner, String label,
                                               Function<String, TField> converter,
                                               Function<TField, ValidationResult> validator) {
        while (true) {
            try {
                System.out.print(label);
                TField input = converter.apply(scanner.nextLine());
                if (validator == null)
                    return input;
                ValidationResult validationResult = validator.apply(input);
                if (!validationResult.isValid()) {
                    Logger.printWarning("Invalid! %s", validationResult.getErrorMessage());
                    continue;
                }
                return input;
            } catch (Exception e) {
                Logger.printWarning("Fail to parse your input. Please enter with valid format!");
            }
        }
    }

    public static <TClass, TField> void requestInput(Scanner scanner, String label, String fieldName, Function<String, TField> converter, TClass obj) throws RuntimeException {
        TField input = requestInput(scanner, label, converter, (value) -> {
            try {
                return validate(fieldName, value, obj.getClass());
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        });
        setInputToObject(fieldName, input, obj);
    }

    public static <TClass> void requestStringInput(Scanner scanner, String label, String fieldName, TClass obj) throws RuntimeException {
        requestInput(scanner, label, fieldName, (value) -> value, obj);
    }

    public static String requestStringInput(Scanner scanner, String label, Function<String, ValidationResult> validator) {
        return requestInput(scanner, label, (value) -> value, validator);
    }

    public static <TClass> void requestIntInput(Scanner scanner, String label, String fieldName, TClass obj) throws RuntimeException {
        requestInput(scanner, label, fieldName, (value) -> Helpers.isNullOrEmpty(value) ? null : Integer.parseInt(value), obj);
    }

    public static Integer requestIntInput(Scanner scanner, String label, Function<Integer, ValidationResult> validator) {
        return requestInput(scanner, label, (value) -> Helpers.isNullOrEmpty(value) ? null : Integer.parseInt(value), validator);
    }

    public static <TClass> void requestLongInput(Scanner scanner, String label, String fieldName, TClass obj) throws RuntimeException {
        requestInput(scanner, label, fieldName, (value) -> Helpers.isNullOrEmpty(value) ? null : Long.parseLong(value), obj);
    }

    public static Long requestLongInput(Scanner scanner, String label, Function<Long, ValidationResult> validator) {
        return requestInput(scanner, label, (value) -> Helpers.isNullOrEmpty(value) ? null : Long.parseLong(value), validator);
    }

    public static <TClass> void requestFloatInput(Scanner scanner, String label, String fieldName, TClass obj) throws RuntimeException {
        requestInput(scanner, label, fieldName, (value) -> Helpers.isNullOrEmpty(value) ? null : Float.parseFloat(value), obj);
    }

    public static Float requestFloatInput(Scanner scanner, String label, Function<Float, ValidationResult> validator) {
        return requestInput(scanner, label, (value) -> Helpers.isNullOrEmpty(value) ? null : Float.parseFloat(value), validator);
    }

    public static <TClass> void requestDoubleInput(Scanner scanner, String label, String fieldName, TClass obj) throws RuntimeException {
        requestInput(scanner, label, fieldName, (value) -> Helpers.isNullOrEmpty(value) ? null : Double.parseDouble(value), obj);
    }

    public static Double requestDoubleInput(Scanner scanner, String label, Function<Double, ValidationResult> validator) {
        return requestInput(scanner, label, (value) -> Helpers.isNullOrEmpty(value) ? null : Double.parseDouble(value), validator);
    }

    public static <TClass> void requestBoolInput(Scanner scanner, String label, String fieldName, TClass obj) throws RuntimeException {
        requestInput(scanner, label, fieldName, (value) -> Helpers.isNullOrEmpty(value) ? null : Boolean.parseBoolean(value), obj);
    }

    public static Boolean requestBooleanInput(Scanner scanner, String label, Function<Boolean, ValidationResult> validator) {
        return requestInput(scanner, label, (value) -> Helpers.isNullOrEmpty(value) ? null : Boolean.parseBoolean(value), validator);
    }

    public static <TClass> void requestUuidInput(Scanner scanner, String label, String fieldName, TClass obj) throws RuntimeException {
        requestInput(scanner, label, fieldName, (value) -> Helpers.isNullOrEmpty(value) ? null : UUID.fromString(value), obj);
    }

    public static UUID requestUuidInput(Scanner scanner, String label, Function<UUID, ValidationResult> validator) {
        return requestInput(scanner, label, (value) -> Helpers.isNullOrEmpty(value) ? null : UUID.fromString(value), validator);
    }

    private static <TClass, TField> void setInputToObject(String fieldName, TField input, TClass obj) {
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

    private static <TClass, TField> ValidationResult validate(String fieldName, TField value, Class<TClass> clazz) throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
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
            } else if (value instanceof UUID) {
                return ValidationResult.getInstance(value.equals(emptyUuid()), notEmptyAnno.message());
            } else if (value instanceof Object[]) {
                return ValidationResult.getInstance(((Object[]) value).length > 0, notEmptyAnno.message());
            }
        }

        GreaterThan greaterThanAnno = field.getAnnotation(GreaterThan.class);
        if (greaterThanAnno != null && value != null) {
            if (value instanceof Double)
                return ValidationResult.getInstance(((Double) value) > greaterThanAnno.value(), greaterThanAnno.message());
            if (value instanceof Float)
                return ValidationResult.getInstance(((Float) value) > Double.valueOf(greaterThanAnno.value()).floatValue(), greaterThanAnno.message());
            if (value instanceof Integer)
                return ValidationResult.getInstance(((Integer) value) > Double.valueOf(greaterThanAnno.value()).intValue(), greaterThanAnno.message());
            if (value instanceof Long)
                return ValidationResult.getInstance(((Long) value) > Double.valueOf(greaterThanAnno.value()).longValue(), greaterThanAnno.message());
        }

        NotNull notNullAnno = field.getAnnotation(NotNull.class);
        if (notNullAnno != null) {
            return ValidationResult.getInstance(value != null, notNullAnno.message());
        }
        return ValidationResult.validInstance();
    }

}
