import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * Small reusable validation helpers. Every numeric value is checked as a
 * String (format first) before it is parsed.
 */
public class InputValidator {

    private static final Pattern TRACKING_NUMBER = Pattern.compile("^ABC[0-9]{4}$");
    // Positive decimal: no sign, no exponent, no ".5" or "1.", no unnecessary leading zeros.
    private static final Pattern DECIMAL = Pattern.compile("^(0|[1-9][0-9]*)(\\.[0-9]+)?$");
    private static final Pattern PAGES = Pattern.compile("^(?:[1-9]|[1-9][0-9]|100)$");
    // Positive whole number without leading zeros.
    private static final Pattern POSITIVE_INTEGER = Pattern.compile("^[1-9][0-9]*$");
    private static final Pattern MENU_CHOICE = Pattern.compile("^[1-5]$");

    private InputValidator() {
    }

    public static boolean isNotBlank(String text) {
        return text != null && !text.trim().isEmpty();
    }

    public static boolean isValidTrackingNumber(String text) {
        return text != null && TRACKING_NUMBER.matcher(text).matches();
    }

    public static boolean isValidMenuChoice(String text) {
        return text != null && MENU_CHOICE.matcher(text).matches();
    }

    public static boolean isValidDecimalFormat(String text) {
        return text != null && DECIMAL.matcher(text).matches();
    }

    public static boolean isValidPagesFormat(String text) {
        return text != null && PAGES.matcher(text).matches();
    }

    public static boolean isPositiveIntegerFormat(String text) {
        return text != null && POSITIVE_INTEGER.matcher(text).matches();
    }

    /** Parses a decimal that already passed isValidDecimalFormat. */
    public static BigDecimal parseDecimal(String text) throws NumberFormatException {
        return new BigDecimal(text);
    }

    /** Parses an integer that already passed a format check. Overflow throws NumberFormatException. */
    public static int parseInteger(String text) throws NumberFormatException {
        return Integer.parseInt(text);
    }
}
