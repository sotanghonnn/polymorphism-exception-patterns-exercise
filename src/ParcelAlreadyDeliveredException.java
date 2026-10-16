/** Thrown when an operation needs a pending parcel but it is already delivered. */
public class ParcelAlreadyDeliveredException extends Exception {

    public ParcelAlreadyDeliveredException(String trackingNumber) {
        super("Parcel " + trackingNumber + " is already delivered.");
    }
}
