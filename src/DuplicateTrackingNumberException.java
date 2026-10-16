/** Thrown when a tracking number is already used by another parcel. */
public class DuplicateTrackingNumberException extends Exception {

    public DuplicateTrackingNumberException() {
        super("Tracking number already exists. Please enter a different tracking number.");
    }
}
