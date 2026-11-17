import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Tyre Shop Inventory System - console menu.
 * Features: add stock, sell tyres (with receipt), view inventory,
 * search by brand or size, and low-stock warnings.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Inventory inventory = new Inventory();
    private static int receiptCounter = 0;

    public static void main(String[] args) {
        loadSampleStock();

        System.out.println("===== Welcome to the Tyre Shop Inventory System =====");
        showLowStockWarnings();

        try {
            int choice;
            do {
                printMenu();
                choice = readInt("Enter your choice: ", 1, 5);

                if (choice == 1) {
                    addStock();
                } else if (choice == 2) {
                    sellTyres();
                } else if (choice == 3) {
                    viewInventory();
                } else if (choice == 4) {
                    searchTyres();
                } else {
                    System.out.println("Exiting program... Goodbye!");
                }
            } while (choice != 5);
        } catch (NoSuchElementException e) {
            // Input ended unexpectedly (for example the window was closed)
            System.out.println("\nNo more input. Exiting program...");
        }
    }

    // ---------- Menu ----------

    private static void printMenu() {
        System.out.println("\n===== Tyre Shop Inventory System =====");
        System.out.println("1. Add Stock");
        System.out.println("2. Sell Tyres");
        System.out.println("3. View Inventory");
        System.out.println("4. Search by Brand or Size");
        System.out.println("5. Exit");
    }

    // ---------- Menu option 1: add stock ----------

    private static void addStock() {
        System.out.println("\n--- Add Stock ---");
        System.out.println("Tyre type: 1. Car   2. Bike   3. Truck");
        int typeChoice = readInt("Select type: ", 1, 3);
        String type = typeName(typeChoice);

        String brand = readText("Brand: ");
        String size = readText("Size (for example 205/55R16): ");
        int quantity = readInt("Quantity to add: ", 1, 100000);

        Tyre existing = inventory.findExact(type, brand, size);

        if (existing != null) {
            // The same tyre is already in the inventory: just increase its stock
            existing.addStock(quantity);
            System.out.println("Stock updated: " + existing.getDescription()
                    + " now has " + existing.getQuantity() + " in stock.");
            warnIfLow(existing);
        } else {
            double price = readDouble("Price per tyre (Rs): ", 1);
            Tyre newTyre = createTyre(typeChoice, brand, size, price, quantity);
            inventory.add(newTyre);
            System.out.println("New tyre added: " + newTyre.getDescription()
                    + " with " + newTyre.getQuantity() + " in stock.");
            warnIfLow(newTyre);
        }
    }

    /** Creates the right kind of tyre. Polymorphism: all are used later as plain Tyre objects. */
    private static Tyre createTyre(int typeChoice, String brand, String size,
                                   double price, int quantity) {
        if (typeChoice == 1) {
            return new CarTyre(brand, size, price, quantity);
        } else if (typeChoice == 2) {
            return new BikeTyre(brand, size, price, quantity);
        } else {
            return new TruckTyre(brand, size, price, quantity);
        }
    }

    private static String typeName(int typeChoice) {
        if (typeChoice == 1) {
            return "Car";
        } else if (typeChoice == 2) {
            return "Bike";
        } else {
            return "Truck";
        }
    }

    // ---------- Menu option 2: sell tyres ----------

    private static void sellTyres() {
        System.out.println("\n--- Sell Tyres ---");

        if (inventory.isEmpty()) {
            System.out.println("The inventory is empty. Add stock first.");
            return;
        }

        Receipt receipt = new Receipt(receiptCounter + 1);
        boolean sellMore = true;

        while (sellMore) {
            printTyres(inventory.getAll());

            int number = readInt("Enter item number to sell: ", 1, inventory.size());
            Tyre tyre = inventory.get(number - 1);
            int quantity = readInt("Quantity to sell: ", 1, 100000);

            try {
                // The right discount is chosen automatically (polymorphism)
                double total = tyre.calculateSalePrice(quantity);

                tyre.reduceStock(quantity);     // throws OutOfStockException if not enough

                receipt.addLine(new SaleLine(tyre.getDescription(), quantity,
                        tyre.getPrice(), total));
                System.out.printf("Added to sale: %d x %s = Rs %.2f%n",
                        quantity, tyre.getDescription(), total);
                warnIfLow(tyre);

            } catch (OutOfStockException e) {
                System.out.println("Cannot complete this sale. " + e.getMessage());
            }

            sellMore = readYesNo("Sell another item? (y/n): ");
        }

        if (receipt.isEmpty()) {
            System.out.println("No items were sold.");
        } else {
            receipt.print();
            receiptCounter++;
        }
    }

    // ---------- Menu option 3: view inventory ----------

    private static void viewInventory() {
        System.out.println("\n--- Inventory ---");
        if (inventory.isEmpty()) {
            System.out.println("The inventory is empty.");
            return;
        }
        printTyres(inventory.getAll());
        showLowStockWarnings();
    }

    // ---------- Menu option 4: search ----------

    private static void searchTyres() {
        System.out.println("\n--- Search ---");
        int option = readInt("Search by: 1. Brand   2. Size\nEnter option: ", 1, 2);

        ArrayList<Tyre> results;
        if (option == 1) {
            String brand = readText("Enter brand (or part of it): ");
            results = inventory.searchByBrand(brand);
        } else {
            String size = readText("Enter size (or part of it): ");
            results = inventory.searchBySize(size);
        }

        if (results.isEmpty()) {
            System.out.println("No matching tyres found.");
        } else {
            System.out.println(results.size() + " tyre(s) found:");
            printTyres(results);
        }
    }

    // ---------- Display helpers ----------

    private static void printTyres(ArrayList<Tyre> list) {
        System.out.printf("%-4s %-6s %-14s %-14s %12s %5s  %s%n",
                "No.", "Type", "Brand", "Size", "Price (Rs)", "Qty", "Status");
        System.out.println("-------------------------------------------------------------------------");
        for (int i = 0; i < list.size(); i++) {
            Tyre tyre = list.get(i);
            String status = tyre.isLowStock() ? "LOW STOCK" : "";
            System.out.printf("%-4d %-6s %-14s %-14s %12.2f %5d  %s%n",
                    i + 1, tyre.getType(), tyre.getBrand(), tyre.getSize(),
                    tyre.getPrice(), tyre.getQuantity(), status);
        }
    }

    /** Prints a warning if this tyre is running low. */
    private static void warnIfLow(Tyre tyre) {
        if (tyre.isLowStock()) {
            System.out.println("*** LOW STOCK WARNING: " + tyre.getDescription()
                    + " has only " + tyre.getQuantity() + " left (below "
                    + Tyre.LOW_STOCK_LIMIT + "). ***");
        }
    }

    /** Prints a warning for every tyre that is running low. */
    private static void showLowStockWarnings() {
        ArrayList<Tyre> low = inventory.getLowStockTyres();
        if (!low.isEmpty()) {
            System.out.println("\n*** LOW STOCK WARNING (fewer than " + Tyre.LOW_STOCK_LIMIT + " left) ***");
            for (Tyre tyre : low) {
                System.out.println("  - " + tyre.getDescription() + ": " + tyre.getQuantity() + " left");
            }
        }
    }

    // ---------- Input helpers (so wrong input never crashes the program) ----------

    private static String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String text = scanner.nextLine().trim();
            if (!text.isEmpty()) {
                return text;
            }
            System.out.println("Input cannot be empty.");
        }
    }

    private static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String text = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(text);
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // not a number: the message below is shown
            }
            System.out.println("Invalid input. Enter a whole number between " + min + " and " + max + ".");
        }
    }

    private static double readDouble(String prompt, double min) {
        while (true) {
            System.out.print(prompt);
            String text = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(text);
                if (value >= min) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // not a number: the message below is shown
            }
            System.out.println("Invalid input. Enter a number that is at least " + min + ".");
        }
    }

    private static boolean readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String text = scanner.nextLine().trim().toLowerCase();
            if (text.equals("y") || text.equals("yes")) {
                return true;
            }
            if (text.equals("n") || text.equals("no")) {
                return false;
            }
            System.out.println("Please type y or n.");
        }
    }

    // ---------- Starting data ----------

    /** A few tyres so the program has something to show when it starts. */
    private static void loadSampleStock() {
        inventory.add(new CarTyre("Michelin", "205/55R16", 18500, 12));
        inventory.add(new CarTyre("Bridgestone", "195/65R15", 15200, 8));
        inventory.add(new CarTyre("Dunlop", "185/70R14", 11800, 4));
        inventory.add(new BikeTyre("Dunlop", "90/90-17", 6500, 15));
        inventory.add(new BikeTyre("Duro", "80/100-17", 4200, 6));
        inventory.add(new TruckTyre("Bridgestone", "11R22.5", 52000, 10));
        inventory.add(new TruckTyre("Goodyear", "295/80R22.5", 58500, 3));
    }
}
