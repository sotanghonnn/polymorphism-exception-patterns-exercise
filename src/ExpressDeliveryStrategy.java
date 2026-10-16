import java.math.BigDecimal;
import java.math.RoundingMode;

/** Express delivery: delivery fee = base fee x 1.5. */
public class ExpressDeliveryStrategy implements DeliveryStrategy {

    private static final BigDecimal MULTIPLIER = new BigDecimal("1.5");

    @Override
    public BigDecimal calculateDeliveryFee(BigDecimal baseFee) {
        return baseFee.multiply(MULTIPLIER).setScale(2, RoundingMode.HALF_UP);
    }
}
