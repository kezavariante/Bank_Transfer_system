public class TransferTask implements Runnable {
    private BankAccount sender;
    private BankAccount receiver;
    private double amount;

    public TransferTask(BankAccount sender, BankAccount receiver, double amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

    @Override
    public void run() {
        boolean withdrawn = sender.withdraw(amount);

        if (withdrawn) {
            receiver.deposit(amount);
        }
    }
}
