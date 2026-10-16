import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

/** Console entry point for the Parcel Delivery System. */
public class Main {

    private static final String SAME_DAY_MESSAGE = "Same Day delivery is only for parcels up to 10 kg!";

    public static void main(String[] args) {
        ConsoleInput input = new ConsoleInput();
        ParcelManager manager = ParcelManager.getInstance(); // Singleton access

        try {
            int choice = 0;
            while (choice != 5) {
                printMenu();
                choice = input.readMenuChoice();
                System.out.println();
                switch (choice) {
                    case 1:
                        addParcel(input, manager);
                        break;
                    case 2:
                        viewAllParcels(manager);
                        break;
                    case 3:
                        changeDeliveryService(input, manager);
                        break;
                    case 4:
                        markAsDelivered(input, manager);
                        break;
                    default:
                        System.out.println("Thank you for using the Parcel Delivery System. Goodbye!");
                        break;
                }
                System.out.println();
            }
        } catch (NoSuchElementException e) {
            System.out.println();
            System.out.println("No more input available. Goodbye!");
        }
    }

    private static void printMenu() {
        System.out.println("===== PARCEL DELIVERY SYSTEM =====");
        System.out.println();
        System.out.println("1. Add Parcel");
        System.out.println("2. View All Parcels");
        System.out.println("3. Change Delivery Service");
        System.out.println("4. Mark as Delivered");
        System.out.println("5. Exit");
        System.out.println();
    }

    // ---------- Option 1 ----------

    private static void addParcel(ConsoleInput input, ParcelManager manager) {
        ParcelType type = input.readParcelType();
        String trackingNumber = readUniqueTrackingNumber(input, manager);
        String receiver = input.readReceiverName();
        double weight = input.readWeight(type);

        Parcel parcel;
        try {
            parcel = createParcel(input, type, trackingNumber, receiver, weight);
        } catch (IllegalArgumentException e) {
            // Input was already validated, so this is only a safety net.
            System.out.println("Could not create parcel: " + e.getMessage());
            return;
        }

        chooseDeliveryService(input, parcel);

        try {
            manager.addParcel(parcel);
            System.out.println("Parcel added successfully! Delivery fee: "
                    + Parcel.formatMoney(parcel.getDeliveryFee()));
        } catch (DuplicateTrackingNumberException e) {
            System.out.println(e.getMessage());
        }
    }

    /** Keeps asking for a tracking number until it is valid AND unused. */
    private static String readUniqueTrackingNumber(ConsoleInput input, ParcelManager manager) {
        String trackingNumber = "";
        boolean available = false;
        while (!available) {
            trackingNumber = input.readTrackingNumber();
            try {
                manager.checkTrackingNumberAvailable(trackingNumber);
                available = true;
            } catch (DuplicateTrackingNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        return trackingNumber;
    }

    /** Reads the type-specific details, then builds the right Parcel subclass. */
    private static Parcel createParcel(ConsoleInput input, ParcelType type,
                                       String trackingNumber, String receiver, double weight) {
        if (type == ParcelType.DOCUMENT) {
            int pages = input.readNumberOfPages();
            return new Document(trackingNumber, receiver, weight, pages);
        }
        int length = input.readDimension("Length");
        int width = input.readDimension("Width");
        int height = input.readDimension("Height");
        if (type == ParcelType.FRAGILE) {
            String note = input.readHandlingNote();
            return new Fragile(trackingNumber, receiver, weight, length, width, height, note);
        }
        return new Box(trackingNumber, receiver, weight, length, width, height);
    }

    /** Asks for a service until one is accepted for this parcel. */
    private static void chooseDeliveryService(ConsoleInput input, Parcel parcel) {
        boolean accepted = false;
        while (!accepted) {
            DeliveryService service = input.readDeliveryService();
            try {
                parcel.setDeliveryService(service);
                accepted = true;
            } catch (InvalidDeliveryServiceException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // ---------- Option 2 ----------

    private static void viewAllParcels(ParcelManager manager) {
        List<Parcel> parcels = manager.getAllParcels();
        if (parcels.isEmpty()) {
            System.out.println("No parcels registered yet.");
            return;
        }
        String format = "%-16s %-10s %-22s %-18s %-10s %14s  %-10s%n";
        System.out.printf(format, "Tracking Number", "Type", "Receiver", "Chargeable Weight",
                "Service", "Delivery Fee", "Status");
        System.out.println("-".repeat(108));
        for (Parcel parcel : parcels) {
            System.out.printf(format,
                    parcel.getTrackingNumber(),
                    parcel.getParcelType(),
                    shorten(parcel.getReceiverName(), 22),
                    Parcel.formatWeight(parcel.getChargeableWeight()),
                    parcel.getDeliveryService(),
                    Parcel.formatMoney(parcel.getDeliveryFee()),
                    parcel.getStatus());
        }
    }

    /** Cuts long text so the table columns stay aligned. */
    private static String shorten(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 2) + "..";
    }

    // ---------- Option 3 ----------

    private static void changeDeliveryService(ConsoleInput input, ParcelManager manager) {
        String trackingNumber = input.readTrackingNumber();
        try {
            manager.findPendingParcel(trackingNumber); // not found / already delivered stop here
            DeliveryService newService = input.readDeliveryService();
            BigDecimal newFee = manager.changeDeliveryService(trackingNumber, newService);
            System.out.println("Delivery service changed! New fee: " + Parcel.formatMoney(newFee));
        } catch (ParcelNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (ParcelAlreadyDeliveredException e) {
            System.out.println("Parcel already delivered!");
        } catch (InvalidDeliveryServiceException e) {
            System.out.println(e.getMessage()); // parcel keeps its old service and fee
        }
    }

    // ---------- Option 4 ----------

    private static void markAsDelivered(ConsoleInput input, ParcelManager manager) {
        String trackingNumber = input.readTrackingNumber();
        try {
            manager.markAsDelivered(trackingNumber);
            System.out.println("Parcel delivered successfully!");
        } catch (ParcelNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (ParcelAlreadyDeliveredException e) {
            System.out.println("Parcel was already delivered.");
        }
    }
}
