package phase2;

import java.util.concurrent.Callable;

public class TransferTask implements Callable {
    private BankAccount sender;
    private BankAccount receiver;
    private double amount;

    public TransferTask(BankAccount sender, BankAccount receiver, double amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

@Override
public String call() {

    boolean withdrawn = sender.withdraw(amount);

    if (withdrawn) {
        receiver.deposit(amount);

        return "SUCCESS: Transferred $" + amount
                + " from Account " + sender.getId()
                + " to Account " + receiver.getId();
    }

    return "FAILED: Account " + sender.getId()
            + " has insufficient funds";
}
}