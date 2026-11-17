/**
 * Bike tyre.
 * Discount rule: a flat 5% off on every sale.
 */
public class BikeTyre extends Tyre {

    public BikeTyre(String brand, String size, double price, int quantity) {
        super(brand, size, price, quantity);
    }

    @Override
    public String getType() {
        return "Bike";
    }

    @Override
    public double calculateSalePrice(int quantity) {
        double total = getPrice() * quantity;
        total = total * 0.95;           // flat 5% discount
        return round2(total);
    }
}
