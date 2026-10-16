import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Singleton that stores and manages every parcel in memory.
 * Get the one instance with ParcelManager.getInstance().
 */
public class ParcelManager {

    // The only instance, created when the class is loaded (simple and thread-safe).
    private static final ParcelManager INSTANCE = new ParcelManager();

    // Different parcel types live together in one List<Parcel> (polymorphism).
    private final List<Parcel> parcels = new ArrayList<>();

    // Private constructor: nobody else can create a ParcelManager.
    private ParcelManager() {
    }

    public static ParcelManager getInstance() {
        return INSTANCE;
    }

    /** Throws if the tracking number is already used. */
    public void checkTrackingNumberAvailable(String trackingNumber) throws DuplicateTrackingNumberException {
        for (Parcel parcel : parcels) {
            if (parcel.getTrackingNumber().equals(trackingNumber)) {
                throw new DuplicateTrackingNumberException();
            }
        }
    }

    public void addParcel(Parcel parcel) throws DuplicateTrackingNumberException {
        checkTrackingNumberAvailable(parcel.getTrackingNumber());
        parcels.add(parcel);
    }

    public Parcel findParcel(String trackingNumber) throws ParcelNotFoundException {
        for (Parcel parcel : parcels) {
            if (parcel.getTrackingNumber().equals(trackingNumber)) {
                return parcel;
            }
        }
        throw new ParcelNotFoundException();
    }

    /** Finds a parcel that must still be pending. */
    public Parcel findPendingParcel(String trackingNumber)
            throws ParcelNotFoundException, ParcelAlreadyDeliveredException {
        Parcel parcel = findParcel(trackingNumber);
        if (parcel.isDelivered()) {
            throw new ParcelAlreadyDeliveredException(trackingNumber);
        }
        return parcel;
    }

    public boolean isEmpty() {
        return parcels.isEmpty();
    }

    /** Read-only view of all parcels, in the order they were added. */
    public List<Parcel> getAllParcels() {
        return Collections.unmodifiableList(parcels);
    }

    /**
     * Changes the delivery service of a pending parcel and returns the new fee.
     * If the change is not allowed, the parcel keeps its old service and fee.
     */
    public BigDecimal changeDeliveryService(String trackingNumber, DeliveryService newService)
            throws ParcelNotFoundException, ParcelAlreadyDeliveredException, InvalidDeliveryServiceException {
        Parcel parcel = findPendingParcel(trackingNumber);
        parcel.setDeliveryService(newService);
        return parcel.getDeliveryFee();
    }

    public void markAsDelivered(String trackingNumber)
            throws ParcelNotFoundException, ParcelAlreadyDeliveredException {
        Parcel parcel = findParcel(trackingNumber);
        parcel.markAsDelivered();
    }
}
