/**
 * One line of a sales receipt: one tyre type, how many were sold and what they cost.
 */
public class SaleLine {

    private final String description;
    private final int quantity;
    private final double unitPrice;
    private final double lineTotal;      // after discount

    public SaleLine(String description, int quantity, double unitPrice, double lineTotal) {
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.lineTotal = lineTotal;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getLineTotal() {
        return lineTotal;
    }

    /** Price before any discount. */
    public double getFullPrice() {
        return unitPrice * quantity;
    }
}
