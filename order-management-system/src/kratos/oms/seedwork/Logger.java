package kratos.oms.seedwork;

import kratos.oms.seedwork.Constants;

public class Logger {
    public static void printError(String className, String methodName, Exception e) {
        Logger.printDanger("Class: %s\nMethod: %s\nError: %s\n", className, methodName, e.getMessage());
    }

    public static void printInfo(String message, Object... args) {
        System.out.printf("%s%s%s%s%n",
                Constants.ANSI_BLACK_BACKGROUND,
                Constants.ANSI_WHITE,
                String.format(message, args),
                Constants.ANSI_RESET);
    }

    public static void printSuccess(String message, Object... args) {
        System.out.printf("%s%s%s%s%n",
                Constants.ANSI_BLACK_BACKGROUND,
                Constants.ANSI_GREEN,
                String.format(message, args),
                Constants.ANSI_RESET);
    }

    public static void printDanger(String message, Object... args) {
        System.out.printf("%s%s%s%s%n",
                Constants.ANSI_BLACK_BACKGROUND,
                Constants.ANSI_RED,
                String.format(message, args),
                Constants.ANSI_RESET);
    }

    public static void printWarning(String message, Object... args) {
        System.out.printf("%s%s%s%s%n",
                Constants.ANSI_BLACK_BACKGROUND,
                Constants.ANSI_YELLOW,
                String.format(message, args),
                Constants.ANSI_RESET);
    }
}
