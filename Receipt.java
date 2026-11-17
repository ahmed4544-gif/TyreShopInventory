import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * A sales receipt: a list of sold items with quantities and the total amount.
 */
public class Receipt {

    private final int receiptNumber;
    private final LocalDateTime time;
    private final ArrayList<SaleLine> lines = new ArrayList<SaleLine>();

    public Receipt(int receiptNumber) {
        this.receiptNumber = receiptNumber;
        this.time = LocalDateTime.now();
    }

    public void addLine(SaleLine line) {
        lines.add(line);
    }

    public boolean isEmpty() {
        return lines.isEmpty();
    }

    /** Total before discounts. */
    public double getSubtotal() {
        double sum = 0;
        for (SaleLine line : lines) {
            sum += line.getFullPrice();
        }
        return sum;
    }

    /** Final amount the customer pays (after discounts). */
    public double getTotal() {
        double sum = 0;
        for (SaleLine line : lines) {
            sum += line.getLineTotal();
        }
        return sum;
    }

    public double getTotalDiscount() {
        return getSubtotal() - getTotal();
    }

    /** Prints the receipt on the screen. */
    public void print() {
        String line = "==================================================================";
        String thin = "------------------------------------------------------------------";
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        System.out.println();
        System.out.println(line);
        System.out.println("                    TYRE SHOP - SALES RECEIPT");
        System.out.println(line);
        System.out.println("Receipt No : " + receiptNumber);
        System.out.println("Date       : " + time.format(format));
        System.out.println(thin);
        System.out.printf("%-32s %4s %12s %12s%n", "Item", "Qty", "Unit Price", "Total");
        System.out.println(thin);

        for (SaleLine item : lines) {
            System.out.printf("%-32s %4d %12.2f %12.2f%n",
                    item.getDescription(), item.getQuantity(),
                    item.getUnitPrice(), item.getLineTotal());
        }

        System.out.println(thin);
        System.out.printf("%-50s %12.2f%n", "Subtotal (before discount):", getSubtotal());
        System.out.printf("%-50s %12.2f%n", "Discount:", -getTotalDiscount());
        System.out.printf("%-50s %12.2f%n", "TOTAL AMOUNT (Rs):", getTotal());
        System.out.println(line);
        System.out.println("                 Thank you for shopping with us!");
        System.out.println(line);
    }
}
