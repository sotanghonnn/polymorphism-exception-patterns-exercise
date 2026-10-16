import java.math.BigDecimal;

/** A document parcel: chargeable weight = actual weight, flat base fee of Php 50. */
public class Document extends Parcel {

    private static final BigDecimal FLAT_FEE = new BigDecimal("50.00");
    private static final int MIN_PAGES = 1;
    private static final int MAX_PAGES = 100;

    private final int numberOfPages;

    public Document(String trackingNumber, String receiverName, double actualWeight, int numberOfPages) {
        super(trackingNumber, receiverName, actualWeight, ParcelType.DOCUMENT.getMaxWeight());
        if (numberOfPages < MIN_PAGES || numberOfPages > MAX_PAGES) {
            throw new IllegalArgumentException("Number of pages must be from 1 to 100.");
        }
        this.numberOfPages = numberOfPages;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    @Override
    public ParcelType getParcelType() {
        return ParcelType.DOCUMENT;
    }

    @Override
    public double getChargeableWeight() {
        return getActualWeight();
    }

    @Override
    public BigDecimal getBaseFee() {
        return FLAT_FEE;
    }
}
