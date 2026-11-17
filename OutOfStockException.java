/**
 * Custom exception, thrown when someone tries to sell more tyres
 * than are available in stock.
 */
public class OutOfStockException extends Exception {

    private static final long serialVersionUID = 1L;

    private final int requested;
    private final int available;

    public OutOfStockException(String tyreName, int requested, int available) {
        super("Not enough stock for " + tyreName + ": requested " + requested
                + ", but only " + available + " available.");
        this.requested = requested;
        this.available = available;
    }

    public int getRequested() {
        return requested;
    }

    public int getAvailable() {
        return available;
    }
}
