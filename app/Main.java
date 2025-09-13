import models.User;
import services.AuthenticationService;
import services.WalletService;

import java.util.Scanner;

public class Main {
    private static final AuthenticationService AuthenticationService = new AuthenticationService();
    private static final WalletService walletService = new WalletService();
    private static final Scanner scanner = new Scanner(System.in);
    private static User currentUser;

    public static void main(String[] args) {
        while (true) {
            if (currentUser == null) {
                System.out.println("\n=== GCash CLI ===");
                System.out.println("[1] Register");
                System.out.println("[2] Login");
                System.out.println("[0] Exit");
                System.out.print("Choose: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> handleRegister();
                    case 2 -> handleLogin();
                    case 0 -> System.exit(0);
                }
            } else {
                System.out.println("\n=== Dashboard (User: " + currentUser.getUsername() + ") ===");
                System.out.println("[1] View Balance");
                System.out.println("[2] Send Money");
                System.out.println("[3] Pay Bills");
                System.out.println("[4] Transaction History");
                System.out.println("[5] Logout");
                System.out.print("Choose: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> System.out.println("Balance: ₱" + currentUser.getWallet().getBalance());
                    case 2 -> handleSendMoney();
                    case 3 -> handlePayBills();
                    case 4 -> walletService.showTransactionHistory(currentUser, txn -> true);
                    case 5 -> currentUser = null;
                }
            }
        }
    }

    private static void handleRegister() {
        System.out.print("Enter username: ");
        String uname = scanner.nextLine();
        System.out.print("Enter password: ");
        String pwd = scanner.nextLine();

        if (AuthenticationService.register(uname, pwd)) {
            System.out.println("Registration successful!");
        } else {
            System.out.println("Username already exists.");
        }
    }

    private static void handleLogin() {
        System.out.print("Enter username: ");
        String uname = scanner.nextLine();
        System.out.print("Enter password: ");
        String pwd = scanner.nextLine();

        User user = AuthenticationService.login(uname, pwd);
        if (user != null) {
            currentUser = user;
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    private static void handleSendMoney() {
        System.out.print("Enter recipient username: ");
        String toUser = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        User recipient = AuthenticationService.findUser(toUser);
        if (recipient != null) {
            walletService.sendMoney(currentUser, recipient, amount);
        } else {
            System.out.println("User not found.");
        }
    }

    private static void handlePayBills() {
        System.out.print("Enter biller name: ");
        String biller = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        walletService.payBill(currentUser, biller, amount);
    }
}
