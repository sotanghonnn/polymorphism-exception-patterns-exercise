import java.math.BigDecimal;

/** A fragile parcel: a Box with a handling note and an extra Php 100 handling fee. */
public class Fragile extends Box {

    private static final BigDecimal HANDLING_FEE = new BigDecimal("100.00");

    private final String handlingNote;

    public Fragile(String trackingNumber, String receiverName, double actualWeight,
                   int length, int width, int height, String handlingNote) {
        super(trackingNumber, receiverName, actualWeight,
                ParcelType.FRAGILE.getMaxWeight(), length, width, height);
        if (!InputValidator.isNotBlank(handlingNote)) {
            throw new IllegalArgumentException("Handling note must not be empty.");
        }
        this.handlingNote = handlingNote.trim();
    }

    public String getHandlingNote() {
        return handlingNote;
    }

    @Override
    public ParcelType getParcelType() {
        return ParcelType.FRAGILE;
    }

    /** Box base fee + Php 100. Volumetric and chargeable weight are inherited from Box. */
    @Override
    public BigDecimal getBaseFee() {
        return super.getBaseFee().add(HANDLING_FEE);
    }
}
