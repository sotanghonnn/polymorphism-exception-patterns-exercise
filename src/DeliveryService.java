/**
 * The delivery services a customer can choose from. Each service is linked
 * to the Strategy object that knows how to calculate its fee.
 *
 * To add a new service: write a new DeliveryStrategy class and add one
 * constant here. No Parcel class needs to change.
 */
public enum DeliveryService {
    STANDARD("Standard", new StandardDeliveryStrategy()),
    EXPRESS("Express", new ExpressDeliveryStrategy()),
    SAME_DAY("Same Day", new SameDayDeliveryStrategy());

    private final String displayName;
    private final DeliveryStrategy strategy;

    DeliveryService(String displayName, DeliveryStrategy strategy) {
        this.displayName = displayName;
        this.strategy = strategy;
    }

    public String getDisplayName() {
        return displayName;
    }

    public DeliveryStrategy getStrategy() {
        return strategy;
    }

    /**
     * Converts user text (case-insensitive) to a DeliveryService.
     * "Same Day" may be typed with a space, without a space, or with an underscore.
     * Returns null when the text is not a valid service.
     */
    public static DeliveryService fromText(String text) {
        if (text == null) {
            return null;
        }
        String cleaned = text.trim().replace(" ", "").replace("_", "");
        for (DeliveryService service : values()) {
            String name = service.displayName.replace(" ", "");
            if (name.equalsIgnoreCase(cleaned)) {
                return service;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
