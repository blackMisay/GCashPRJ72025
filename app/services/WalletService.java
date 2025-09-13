package services;

import models.Transaction;
import models.User;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class WalletService {

    public void sendMoney(User sender, User receiver, double amount) {
        if (sender.getWallet().deductBalance(amount)) {
            receiver.getWallet().addBalance(amount);

            Transaction t1 = new Transaction("Send", amount, "Sent to " + receiver.getUsername());
            Transaction t2 = new Transaction("Receive", amount, "Received from " + sender.getUsername());

            sender.getWallet().addTransaction(t1);
            receiver.getWallet().addTransaction(t2);

            System.out.println("Money sent successfully!");
        } else {
            System.out.println("Insufficient balance!");
        }
    }

    public void payBill(User user, String biller, double amount) {
        if (user.getWallet().deductBalance(amount)) {
            Transaction t = new Transaction("Bill Payment", amount, "Paid to " + biller);
            user.getWallet().addTransaction(t);
            System.out.println("Bill paid successfully!");
        } else {
            System.out.println("Not enough balance!");
        }
    }

    public void showTransactionHistory(User user, Predicate<Transaction> filter) {
        List<Transaction> txns = user.getWallet().getTransactions();
        txns.stream()
                .filter(filter)
                .sorted(Comparator.comparing(Transaction::getTimestamp).reversed())
                .forEach(System.out::println);
    }
}
