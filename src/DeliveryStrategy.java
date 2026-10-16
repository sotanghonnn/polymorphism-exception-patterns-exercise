import java.math.BigDecimal;

/**
 * Strategy interface: one way of turning a parcel's base fee into a
 * delivery fee.
 */
public interface DeliveryStrategy {

    /** Calculates the delivery fee from the parcel's base fee. */
    BigDecimal calculateDeliveryFee(BigDecimal baseFee);

    /**
     * Checks that this delivery service may be used for the given parcel.
     * By default every parcel is allowed. A strategy with restrictions
     * (such as Same Day) overrides this and throws an exception.
     */
    default void validateFor(Parcel parcel) throws InvalidDeliveryServiceException {
        // No restriction by default.
    }
}
