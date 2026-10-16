import java.math.BigDecimal;
import java.math.RoundingMode;

/** Standard delivery: delivery fee = base fee. */
public class StandardDeliveryStrategy implements DeliveryStrategy {

    @Override
    public BigDecimal calculateDeliveryFee(BigDecimal baseFee) {
        return baseFee.setScale(2, RoundingMode.HALF_UP);
    }
}
