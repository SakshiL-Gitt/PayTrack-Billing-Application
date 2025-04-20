import java.io.*;
import java.util.*;

class Bill {
    private String item;
    private int rate;
    private int quantity;

    public Bill() {
        item = "";
        rate = 0;
        quantity = 0;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getItem() {
        return item;
    }

    public int getRate() {
        return rate;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return item + " : " + rate + " : " + quantity;
    }
}

public class PayTrackApp {
    static final String FILE_PATH = "Bill.txt";
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n=== Welcome To PayTrack Billing System ===");
            System.out.println("1. Add Item");
            System.out.println("2. Print Bill");
            System.out.println("3. Search Item");
            System.out.println("4. View All Items");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1: addItem();
                case 2: printBill();
                case 3: searchItem();
                case 4: viewAllItems();
                case 5 : {
                    System.out.println("Exiting... Thank you!");
                    exit = true;
                }
                default : System.out.println("Invalid choice. Try again.");
            }
        }
    }

    static void addItem() {
        Bill b = new Bill();

        System.out.print("Enter Item Name: ");
        b.setItem(sc.nextLine());

        System.out.print("Enter Item Rate: ");
        b.setRate(sc.nextInt());

        System.out.print("Enter Quantity: ");
        b.setQuantity(sc.nextInt());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(b.toString());
            writer.newLine();
            System.out.println("✅ Item added successfully!");
        } catch (IOException e) {
            System.out.println("❌ Error writing to file.");
        }
    }

    static void printBill() {
        int total = 0;
        Map<String, Bill> inventory = loadInventory();

        System.out.print("Enter Item Name to Purchase: ");
        String name = sc.nextLine();

        if (!inventory.containsKey(name)) {
            System.out.println("❌ Item not found in inventory.");
            return;
        }

        Bill item = inventory.get(name);

        System.out.print("Enter Quantity: ");
        int q = sc.nextInt();

        if (q > item.getQuantity()) {
            System.out.println("❌ Not enough quantity available.");
            return;
        }

        int amount = q * item.getRate();
        total += amount;
        item.setQuantity(item.getQuantity() - q);
        inventory.put(name, item);

        saveInventory(inventory);

        System.out.println("\n🧾 Bill Details:");
        System.out.printf("Item\tRate\tQuantity\tAmount\n");
        System.out.printf("%s\t%d\t%d\t\t%d\n", item.getItem(), item.getRate(), q, amount);
        System.out.println("Total Bill: ₹" + total);
    }

    static void searchItem() {
        System.out.print("Enter item to search: ");
        String searchItem = sc.nextLine();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith(searchItem)) {
                    System.out.println("🔍 Found: " + line);
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("❌ Item not found.");
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading file.");
        }
    }

    static void viewAllItems() {
        System.out.println("\n📦 All Available Items:");
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(" - " + line);
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading file.");
        }
    }

    static Map<String, Bill> loadInventory() {
        Map<String, Bill> map = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" : ");
                if (parts.length == 3) {
                    Bill b = new Bill();
                    b.setItem(parts[0]);
                    b.setRate(Integer.parseInt(parts[1].trim()));
                    b.setQuantity(Integer.parseInt(parts[2].trim()));
                    map.put(parts[0], b);
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Error loading inventory.");
        }
        return map;
    }

    static void saveInventory(Map<String, Bill> map) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Bill b : map.values()) {
                writer.write(b.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("❌ Error saving inventory.");
        }
    }
}
