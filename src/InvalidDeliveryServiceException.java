/** Thrown when a delivery service cannot be used for a parcel (e.g. Same Day over 10 kg). */
public class InvalidDeliveryServiceException extends Exception {

    public InvalidDeliveryServiceException(String message) {
        super(message);
    }
}
