import java.math.BigDecimal;

/** A box parcel. Charged by the larger of actual and volumetric weight, rounded up. */
public class Box extends Parcel {

    /** Largest allowed length, width or height (cm). Keeps L x W x H safely inside a long. */
    public static final int MAX_DIMENSION = 1_000_000;

    private static final BigDecimal RATE_PER_KG = new BigDecimal("40.00");
    private static final double VOLUMETRIC_DIVISOR = 3500.0;

    private final int length;
    private final int width;
    private final int height;

    public Box(String trackingNumber, String receiverName, double actualWeight,
               int length, int width, int height) {
        this(trackingNumber, receiverName, actualWeight,
                ParcelType.BOX.getMaxWeight(), length, width, height);
    }

    /** Used by subclasses (Fragile) that have a different maximum weight. */
    protected Box(String trackingNumber, String receiverName, double actualWeight,
                  double maxWeight, int length, int width, int height) {
        super(trackingNumber, receiverName, actualWeight, maxWeight);
        checkDimension("Length", length);
        checkDimension("Width", width);
        checkDimension("Height", height);
        this.length = length;
        this.width = width;
        this.height = height;
    }

    private static void checkDimension(String name, int value) {
        if (value < 1 || value > MAX_DIMENSION) {
            throw new IllegalArgumentException(name + " must be from 1 to " + MAX_DIMENSION + " cm.");
        }
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /** (length x width x height) / 3500. The product is a long, so it cannot overflow. */
    public double getVolumetricWeight() {
        long volume = (long) length * width * height;
        return volume / VOLUMETRIC_DIVISOR;
    }

    @Override
    public ParcelType getParcelType() {
        return ParcelType.BOX;
    }

    /** The bigger of actual and volumetric weight, rounded UP to a whole kg. */
    @Override
    public double getChargeableWeight() {
        return Math.ceil(Math.max(getActualWeight(), getVolumetricWeight()));
    }

    /** Php 40 x chargeable weight. */
    @Override
    public BigDecimal getBaseFee() {
        return RATE_PER_KG.multiply(new BigDecimal(getChargeableWeight()));
    }
}
