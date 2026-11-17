import java.util.ArrayList;

/**
 * Stores all tyres in an ArrayList and provides add, find and search operations.
 */
public class Inventory {

    private ArrayList<Tyre> tyres = new ArrayList<Tyre>();

    /** Adds a new tyre to the inventory. */
    public void add(Tyre tyre) {
        tyres.add(tyre);
    }

    /** Returns the tyre at the given position (0 = first). */
    public Tyre get(int index) {
        return tyres.get(index);
    }

    public int size() {
        return tyres.size();
    }

    public boolean isEmpty() {
        return tyres.isEmpty();
    }

    public ArrayList<Tyre> getAll() {
        return tyres;
    }

    /** Finds a tyre with exactly this type, brand and size (ignoring capital letters). */
    public Tyre findExact(String type, String brand, String size) {
        for (Tyre tyre : tyres) {
            if (tyre.getType().equalsIgnoreCase(type)
                    && tyre.getBrand().equalsIgnoreCase(brand)
                    && tyre.getSize().equalsIgnoreCase(size)) {
                return tyre;
            }
        }
        return null;
    }

    /** Returns all tyres whose brand contains the given text. */
    public ArrayList<Tyre> searchByBrand(String text) {
        ArrayList<Tyre> results = new ArrayList<Tyre>();
        for (Tyre tyre : tyres) {
            if (tyre.getBrand().toLowerCase().contains(text.toLowerCase())) {
                results.add(tyre);
            }
        }
        return results;
    }

    /** Returns all tyres whose size contains the given text. */
    public ArrayList<Tyre> searchBySize(String text) {
        ArrayList<Tyre> results = new ArrayList<Tyre>();
        for (Tyre tyre : tyres) {
            if (tyre.getSize().toLowerCase().contains(text.toLowerCase())) {
                results.add(tyre);
            }
        }
        return results;
    }

    /** Returns all tyres whose quantity is below the low-stock limit. */
    public ArrayList<Tyre> getLowStockTyres() {
        ArrayList<Tyre> results = new ArrayList<Tyre>();
        for (Tyre tyre : tyres) {
            if (tyre.isLowStock()) {
                results.add(tyre);
            }
        }
        return results;
    }
}
