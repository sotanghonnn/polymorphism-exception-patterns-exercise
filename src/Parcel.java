import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Abstract parent of all parcel types. It holds the common details and the
 * delivery-service logic. Each subclass decides its own chargeable weight and
 * base fee (polymorphism); the delivery fee is calculated by the selected
 * DeliveryStrategy (Strategy pattern).
 */
public abstract class Parcel {

    private final String trackingNumber;
    private final String receiverName;
    private final double actualWeight;
    private DeliveryService deliveryService;
    private ParcelStatus status;

    /**
     * @param maxWeight the largest actual weight (kg) allowed for this parcel type
     * @throws IllegalArgumentException if any attribute is invalid
     */
    protected Parcel(String trackingNumber, String receiverName, double actualWeight, double maxWeight) {
        if (!InputValidator.isValidTrackingNumber(trackingNumber)) {
            throw new IllegalArgumentException("Tracking number must be ABC followed by exactly four digits.");
        }
        if (!InputValidator.isNotBlank(receiverName)) {
            throw new IllegalArgumentException("Receiver name must not be empty.");
        }
        // "!(x > 0)" also rejects NaN.
        if (!(actualWeight > 0)) {
            throw new IllegalArgumentException("Weight must be greater than 0.");
        }
        if (actualWeight > maxWeight) {
            throw new IllegalArgumentException("Weight must not exceed " + formatWeight(maxWeight) + ".");
        }
        this.trackingNumber = trackingNumber;
        this.receiverName = receiverName.trim();
        this.actualWeight = actualWeight;
        this.deliveryService = DeliveryService.STANDARD; // always allowed; changed later via setDeliveryService
        this.status = ParcelStatus.PENDING;
    }

    // ----- Methods every parcel type must provide (polymorphism) -----

    public abstract ParcelType getParcelType();

    /** Weight in kg that the delivery charge is based on. */
    public abstract double getChargeableWeight();

    /** Fee before the delivery service is applied. */
    public abstract BigDecimal getBaseFee();

    // ----- Delivery fee through the Strategy pattern -----

    public BigDecimal getDeliveryFee() {
        return deliveryService.getStrategy().calculateDeliveryFee(getBaseFee());
    }

    /**
     * Changes the delivery service only if the new service is allowed for this parcel.
     * If it is not allowed, an exception is thrown and the parcel is left unchanged.
     */
    public void setDeliveryService(DeliveryService newService) throws InvalidDeliveryServiceException {
        if (newService == null) {
            throw new InvalidDeliveryServiceException("Please choose a delivery service.");
        }
        newService.getStrategy().validateFor(this); // check first...
        this.deliveryService = newService;          // ...then change
    }

    /** Marks the parcel as delivered, unless it already is. */
    public void markAsDelivered() throws ParcelAlreadyDeliveredException {
        if (status == ParcelStatus.DELIVERED) {
            throw new ParcelAlreadyDeliveredException(trackingNumber);
        }
        status = ParcelStatus.DELIVERED;
    }

    // ----- Getters -----

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public double getActualWeight() {
        return actualWeight;
    }

    public DeliveryService getDeliveryService() {
        return deliveryService;
    }

    public ParcelStatus getStatus() {
        return status;
    }

    public boolean isDelivered() {
        return status == ParcelStatus.DELIVERED;
    }

    // ----- Formatting helpers -----

    /** Formats money with exactly two decimals, e.g. "Php 330.00". */
    public static String formatMoney(BigDecimal amount) {
        return "Php " + amount.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    /** Formats a weight without useless zeros, e.g. 3.0 -> "3 kg", 0.5 -> "0.5 kg". */
    public static String formatWeight(double weight) {
        return new BigDecimal(weight).setScale(6, RoundingMode.HALF_UP)
                .stripTrailingZeros().toPlainString() + " kg";
    }
}
