import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        BankAccount[] accounts = new BankAccount[10];

        for (int i = 0; i < accounts.length; i++) {
            accounts[i] = new BankAccount(i + 1, 10000);
        }

        double initialTotal = calculateTotal(accounts);

        System.out.println("Initial total balance: $" + initialTotal);

        int NUMBER_OF_TRANSFERS = 50;

        ExecutorService pool = Executors.newFixedThreadPool(50);
        Random random = new Random();

        for (int i = 0; i < NUMBER_OF_TRANSFERS; i++) {

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

            pool.execute(task);
        }

        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.HOURS);

        double finalTotal = calculateTotal(accounts);

        System.out.println("Final total balance: $" + finalTotal);

        for (BankAccount account : accounts) {
            System.out.println(
                    "Account " + account.getId()
                            + " balance: $" + account.getBalance()
            );
        }
    }

    private static double calculateTotal(BankAccount[] accounts) {
        double total = 0;

        for (BankAccount account : accounts) {
            total += account.getBalance();
        }

        return total;
    }
}

