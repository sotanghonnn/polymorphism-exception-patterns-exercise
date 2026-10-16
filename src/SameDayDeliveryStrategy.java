import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Same Day delivery: delivery fee = (base fee x 2) + Php 100.
 * Only allowed when the parcel's CHARGEABLE weight is 10 kg or less.
 */
public class SameDayDeliveryStrategy implements DeliveryStrategy {

    private static final BigDecimal MULTIPLIER = new BigDecimal("2");
    private static final BigDecimal SURCHARGE = new BigDecimal("100.00");
    private static final double MAX_CHARGEABLE_WEIGHT = 10.0;

    @Override
    public BigDecimal calculateDeliveryFee(BigDecimal baseFee) {
        return baseFee.multiply(MULTIPLIER).add(SURCHARGE).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public void validateFor(Parcel parcel) throws InvalidDeliveryServiceException {
        if (parcel.getChargeableWeight() > MAX_CHARGEABLE_WEIGHT) {
            throw new InvalidDeliveryServiceException(
                    "Same Day delivery is only for parcels up to 10 kg!");
        }
    }
}
