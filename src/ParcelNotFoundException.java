/** Thrown when no parcel has the requested tracking number. */
public class ParcelNotFoundException extends Exception {

    public ParcelNotFoundException() {
        super("Parcel not found!");
    }
}
