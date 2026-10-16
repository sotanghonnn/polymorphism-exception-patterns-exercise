/**
 * The kinds of parcels the system supports, together with each type's
 * maximum actual weight (in kg).
 */
public enum ParcelType {
    DOCUMENT("Document", 0.5),
    BOX("Box", 50.0),
    FRAGILE("Fragile", 30.0);

    private final String displayName;
    private final double maxWeight;

    ParcelType(String displayName, double maxWeight) {
        this.displayName = displayName;
        this.maxWeight = maxWeight;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getMaxWeight() {
        return maxWeight;
    }

    /**
     * Converts user text (case-insensitive) to a ParcelType.
     * Returns null when the text is not a valid parcel type.
     */
    public static ParcelType fromText(String text) {
        if (text == null) {
            return null;
        }
        String cleaned = text.trim();
        for (ParcelType type : values()) {
            if (type.displayName.equalsIgnoreCase(cleaned)) {
                return type;
            }
        }
        return null;
    }
}
