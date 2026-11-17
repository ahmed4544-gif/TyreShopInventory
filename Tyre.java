/**
 * Abstract parent class of all tyres.
 *
 * Encapsulation: the data fields are private and can only be reached
 * through getters and setters, which also check that the values are valid.
 * Abstraction: this class cannot be created directly; only CarTyre,
 * BikeTyre and TruckTyre can.
 */
public abstract class Tyre implements Sellable {

    public static final int LOW_STOCK_LIMIT = 5;   // warn when quantity falls below this

    private String brand;
    private String size;
    private double price;      // price of ONE tyre
    private int quantity;      // how many are in stock

    public Tyre(String brand, String size, double price, int quantity) {
        setBrand(brand);
        setSize(size);
        setPrice(price);
        setQuantity(quantity);
    }

    // ---------- Getters ----------

    public String getBrand() {
        return brand;
    }

    public String getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    // ---------- Setters (with validation) ----------
    // They are 'final' because the constructor calls them.

    public final void setBrand(String brand) {
        if (brand == null || brand.trim().isEmpty()) {
            throw new IllegalArgumentException("Brand cannot be empty.");
        }
        this.brand = brand.trim();
    }

    public final void setSize(String size) {
        if (size == null || size.trim().isEmpty()) {
            throw new IllegalArgumentException("Size cannot be empty.");
        }
        this.size = size.trim();
    }

    public final void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        this.price = price;
    }

    public final void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        this.quantity = quantity;
    }

    // ---------- Stock handling ----------

    /** Adds new stock. */
    public void addStock(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to add must be greater than zero.");
        }
        quantity += amount;
    }

    /** Removes stock when tyres are sold. Throws OutOfStockException if there are not enough. */
    public void reduceStock(int amount) throws OutOfStockException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to sell must be greater than zero.");
        }
        if (amount > quantity) {
            throw new OutOfStockException(getDescription(), amount, quantity);
        }
        quantity -= amount;
    }

    /** True when the quantity is below the low-stock limit (5). */
    public boolean isLowStock() {
        return quantity < LOW_STOCK_LIMIT;
    }

    // ---------- Things every tyre type must provide ----------

    /** Returns "Car", "Bike" or "Truck". Each subclass decides. */
    public abstract String getType();

    // ---------- Helpers ----------

    /** Short name used in lists and receipts, for example: Michelin 205/55R16 (Car). */
    public String getDescription() {
        return brand + " " + size + " (" + getType() + ")";
    }

    /** Rounds money to 2 decimal places. */
    protected double round2(double amount) {
        return Math.round(amount * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        return getDescription() + " - Rs " + price + " - Qty: " + quantity;
    }
}
