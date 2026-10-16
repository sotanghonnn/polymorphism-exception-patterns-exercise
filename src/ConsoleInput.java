import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Reads and validates user input. Each read method keeps asking for ONE field
 * until that field is valid, so earlier valid answers are never lost.
 * If the input stream ends (Ctrl+D / Ctrl+Z), Scanner throws
 * NoSuchElementException, which Main catches so there is no endless loop.
 */
public class ConsoleInput {

    private final Scanner scanner = new Scanner(System.in);

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public int readMenuChoice() {
        int choice = 0;
        boolean valid = false;
        while (!valid) {
            String text = readLine("Enter your choice: ");
            if (InputValidator.isValidMenuChoice(text)) {
                try {
                    choice = InputValidator.parseInteger(text);
                    valid = true;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid menu option.");
                }
            } else {
                System.out.println("Invalid menu option.");
            }
        }
        return choice;
    }

    public ParcelType readParcelType() {
        ParcelType type = null;
        while (type == null) {
            type = ParcelType.fromText(readLine("Select parcel type (Document, Box, Fragile): "));
            if (type == null) {
                System.out.println("Invalid parcel type. Please type Document, Box, or Fragile.");
            }
        }
        return type;
    }

    public DeliveryService readDeliveryService() {
        DeliveryService service = null;
        while (service == null) {
            service = DeliveryService.fromText(readLine("Select delivery service (Standard, Express, Same Day): "));
            if (service == null) {
                System.out.println("Invalid delivery service. Please type Standard, Express, or Same Day.");
            }
        }
        return service;
    }

    /** Asks until the tracking number has the format ABC + 4 digits. Does not check for duplicates. */
    public String readTrackingNumber() {
        String text = "";
        boolean valid = false;
        while (!valid) {
            text = readLine("Enter tracking number (e.g. ABC0001): ");
            valid = InputValidator.isValidTrackingNumber(text);
            if (!valid) {
                System.out.println("Invalid tracking number. It must be \"ABC\" followed by exactly 4 digits "
                        + "(uppercase, no spaces or symbols), for example ABC0001.");
            }
        }
        return text;
    }

    public String readReceiverName() {
        String text = "";
        boolean valid = false;
        while (!valid) {
            text = readLine("Enter receiver name: ");
            valid = InputValidator.isNotBlank(text);
            if (!valid) {
                System.out.println("Invalid receiver name. The name must not be empty.");
            }
        }
        return text;
    }

    public double readWeight(ParcelType type) {
        double weight = 0;
        boolean valid = false;
        BigDecimal max = BigDecimal.valueOf(type.getMaxWeight());
        while (!valid) {
            String text = readLine("Enter weight in kg (max " + max.toPlainString() + " kg): ");
            if (!InputValidator.isValidDecimalFormat(text)) {
                System.out.println("Invalid weight. Enter a positive decimal such as 1, 1.5 or 0.25 "
                        + "(no signs, commas, spaces, letters, exponents or unneeded leading zeros).");
                continue;
            }
            try {
                BigDecimal value = InputValidator.parseDecimal(text);
                if (value.signum() <= 0) {
                    System.out.println("Invalid weight. Weight must be greater than 0.");
                } else if (value.compareTo(max) > 0) {
                    System.out.println("Invalid weight. A " + type.getDisplayName()
                            + " can weigh at most " + max.toPlainString() + " kg.");
                } else {
                    weight = value.doubleValue();
                    valid = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid weight. That is not a valid number.");
            }
        }
        return weight;
    }

    public int readNumberOfPages() {
        int pages = 0;
        boolean valid = false;
        while (!valid) {
            String text = readLine("Enter number of pages (1-100): ");
            if (!InputValidator.isValidPagesFormat(text)) {
                System.out.println("Invalid number of pages. Enter a whole number from 1 to 100 "
                        + "(no signs, decimals, spaces or leading zeros).");
                continue;
            }
            try {
                pages = InputValidator.parseInteger(text);
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number of pages. That number is too large.");
            }
        }
        return pages;
    }

    /** Reads one box dimension (name is "Length", "Width" or "Height"). */
    public int readDimension(String name) {
        int value = 0;
        boolean valid = false;
        while (!valid) {
            String text = readLine("Enter " + name.toLowerCase() + " in cm: ");
            if (!InputValidator.isPositiveIntegerFormat(text)) {
                System.out.println("Invalid " + name.toLowerCase() + ". Enter a positive whole number "
                        + "(no signs, decimals, commas, spaces or leading zeros).");
                continue;
            }
            try {
                value = InputValidator.parseInteger(text);
                if (value > Box.MAX_DIMENSION) {
                    System.out.println("Invalid " + name.toLowerCase() + ". The maximum is "
                            + Box.MAX_DIMENSION + " cm.");
                } else {
                    valid = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid " + name.toLowerCase() + ". That number is too large "
                        + "(maximum " + Box.MAX_DIMENSION + " cm).");
            }
        }
        return value;
    }

    public String readHandlingNote() {
        String text = "";
        boolean valid = false;
        while (!valid) {
            text = readLine("Enter handling note: ");
            valid = InputValidator.isNotBlank(text);
            if (!valid) {
                System.out.println("Invalid handling note. The note must not be empty.");
            }
        }
        return text;
    }
}
