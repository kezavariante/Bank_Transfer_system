package phase2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        BankAccount[] accounts = new BankAccount[10];

        for (int i = 0; i < accounts.length; i++) {
            accounts[i] = new BankAccount(i + 1, 10000);
        }

        System.out.println(
                "Initial total balance: $" + calculateTotal(accounts)
        );

        ExecutorService pool = Executors.newFixedThreadPool(50);

        List<Future<String>> results = new ArrayList<>();
        Random random = new Random();

        int numberOfTransfers = 50;

        for (int i = 0; i < numberOfTransfers; i++) {

            int senderIndex = random.nextInt(accounts.length);
            int receiverIndex = random.nextInt(accounts.length);

            while (senderIndex == receiverIndex) {
                receiverIndex = random.nextInt(accounts.length);
            }

            double amount = random.nextInt(100) + 1;

            TransferTask task = new TransferTask(
                    accounts[senderIndex],
                    accounts[receiverIndex],
                    amount
            );

            Future<String> future = (Future<String>) pool.submit(task);
            results.add(future);
        }

        pool.shutdown();

        int successfulTransfers = 0;
        int failedTransfers = 0;

        for (Future<String> future : results) {
            try {
                String result = future.get();

                System.out.println(result);

                if (result.startsWith("SUCCESS")) {
                    successfulTransfers++;
                } else if (result.startsWith("FAILED")) {
                    failedTransfers++;
                }

            } catch (ExecutionException e) {
                System.out.println(
                        "Transaction error: " + e.getCause()
                );
            }
        }

        pool.awaitTermination(1, TimeUnit.HOURS);

        System.out.println("\n--- Transaction Summary ---");
        System.out.println(
                "Successful transfers: " + successfulTransfers
        );
        System.out.println(
                "Failed transfers: " + failedTransfers
        );

        System.out.println(
                "Final total balance: $" + calculateTotal(accounts)
        );
    }

    private static double calculateTotal(BankAccount[] accounts) {
        double total = 0;

        for (BankAccount account : accounts) {
            total += account.getBalance();
        }

        return total;
    }
}