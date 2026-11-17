/**
 * Truck tyre.
 * Discount rule (bulk buying):
 *   6 or more tyres -> 8% off
 *   2 to 5 tyres    -> 3% off
 *   1 tyre          -> no discount
 */
public class TruckTyre extends Tyre {

    public TruckTyre(String brand, String size, double price, int quantity) {
        super(brand, size, price, quantity);
    }

    @Override
    public String getType() {
        return "Truck";
    }

    @Override
    public double calculateSalePrice(int quantity) {
        double total = getPrice() * quantity;
        if (quantity >= 6) {
            total = total * 0.92;       // 8% discount
        } else if (quantity >= 2) {
            total = total * 0.97;       // 3% discount
        }
        return round2(total);
    }
}
