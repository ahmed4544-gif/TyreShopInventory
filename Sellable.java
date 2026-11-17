/**
 * Anything that can be sold in the shop.
 * (Abstraction: it only says WHAT must be done, not HOW.)
 */
public interface Sellable {

    /**
     * Calculates the final sale price for the given number of items,
     * after any discount has been applied.
     */
    double calculateSalePrice(int quantity);
}
