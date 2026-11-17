/**
 * Car tyre.
 * Discount rule: 10% off when the customer buys a set of 4 or more.
 */
public class CarTyre extends Tyre {

    public CarTyre(String brand, String size, double price, int quantity) {
        super(brand, size, price, quantity);
    }

    @Override
    public String getType() {
        return "Car";
    }

    @Override
    public double calculateSalePrice(int quantity) {
        double total = getPrice() * quantity;
        if (quantity >= 4) {
            total = total * 0.90;       // 10% discount
        }
        return round2(total);
    }
}
