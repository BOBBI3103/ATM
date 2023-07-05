import java.util.HashMap;
        import java.util.List;
        import java.util.ArrayList;
        import java.util.Map;
        import java.util.Scanner;

record Transaction(String type, String description, double amount) {
}

class ATM
{
    private final Map<String, String> userCredentials;
    private final Map<String, Double> userBalances;
    private String currentUser;
    private final List<Transaction> transactionHistory;

    public ATM()
    {
        userCredentials = new HashMap<>();
        userBalances = new HashMap<>();
        currentUser = null;
        transactionHistory = new ArrayList<>();
    }

    public void addUser(String userId, String pin, double initialBalance)
    {
        userCredentials.put(userId, pin);
        userBalances.put(userId, initialBalance);
    }

    public boolean login(String userId, String pin)
    {
        if (userCredentials.containsKey(userId) && userCredentials.get(userId).equals(pin))
        {
            currentUser = userId;
            return true;
        }
        return false;
    }

    public void showMenu()
    {
        System.out.println("ATM Menu");
        System.out.println("1. Transactions History");
        System.out.println("2. Withdraw");
        System.out.println("3. Deposit");
        System.out.println("4. Transfer");
        System.out.println("5. Quit");
    }

    public void showTransactionsHistory()
    {
        System.out.println("Transactions History");
        System.out.println("User: " + currentUser);
        for (Transaction transaction : transactionHistory)
        {
            System.out.println("Type: " + transaction.type());
            System.out.println("Description: " + transaction.description());
            System.out.println("Amount: " + transaction.amount());
            System.out.println("----------------------------");
        }
    }

    public void withdraw(double amount)
    {
        double balance = userBalances.get(currentUser);
        if (amount > balance)
        {
            System.out.println("Insufficient balance.");
        }
        else
        {
            balance -= amount;
            userBalances.put(currentUser, balance);
            System.out.println("Amount withdrawn: " + amount);
            System.out.println("Current balance: " + balance);

            Transaction transaction = new Transaction("Withdrawal", "Amount withdrawn: " + amount, amount);
            transactionHistory.add(transaction);
        }
    }

    public void deposit(double amount)
    {
        double balance = userBalances.get(currentUser);
        balance += amount;
        userBalances.put(currentUser, balance);
        System.out.println("Amount deposited: " + amount);
        System.out.println("Current balance: " + balance);

        Transaction transaction = new Transaction("Deposit", "Amount deposited: " + amount, amount);
        transactionHistory.add(transaction);
    }

    public void transfer(String recipient, double amount)
    {
        if (!userBalances.containsKey(recipient))
        {
            System.out.println("Recipient not found.");
        }
        else
        {
            double senderBalance = userBalances.get(currentUser);
            double recipientBalance = userBalances.get(recipient);

            if (amount > senderBalance)
            {
                System.out.println("Insufficient balance.");
            }
            else
            {
                senderBalance -= amount;
                recipientBalance += amount;
                userBalances.put(currentUser, senderBalance);
                userBalances.put(recipient, recipientBalance);
                System.out.println("Amount transferred: " + amount);
                System.out.println("Current balance: " + senderBalance);

                Transaction transaction = new Transaction("Transfer", "Amount transferred: " + amount + " to " + recipient, amount);
                transactionHistory.add(transaction);
            }
        }
    }

    public void quit()
    {
        currentUser = null;
        System.out.println("Thank you for using the ATM. Goodbye!");
    }
}

public class Main
{
    public static void main(String[] args)
    {
        ATM atm = new ATM();

        // Add users to the ATM
        atm.addUser("user1", "1234", 1000);
        atm.addUser("user2", "5678", 500);

        Scanner scanner = new Scanner(System.in);

        // Prompt for user id and pin
        System.out.print("Enter user id: ");
        String userId = scanner.nextLine();
        System.out.print("Enter user pin: ");
        String pin = scanner.nextLine();

        // Login to the ATM
        if (atm.login(userId, pin))
        {
            boolean quit = false;
            while (!quit)
            {
                atm.showMenu();
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> atm.showTransactionsHistory();
                    case 2 -> {
                        System.out.print("Enter withdrawal amount: ");
                        double withdrawAmount = scanner.nextDouble();
                        atm.withdraw(withdrawAmount);
                    }
                    case 3 -> {
                        System.out.print("Enter deposit amount: ");
                        double depositAmount = scanner.nextDouble();
                        atm.deposit(depositAmount);
                    }
                    case 4 -> {
                        System.out.print("Enter recipient user id: ");
                        String recipient = scanner.nextLine();
                        System.out.print("Enter transfer amount: ");
                        double transferAmount = scanner.nextDouble();
                        atm.transfer(recipient, transferAmount);
                    }
                    case 5 -> {
                        atm.quit();
                        quit = true;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }
        }
        else
        {
            System.out.println("Invalid user id or pin. Login failed.");
        }
    }
}