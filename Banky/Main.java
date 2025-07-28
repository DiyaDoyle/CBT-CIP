
import java.util.*;
public class Main {
    public static void main(String args[]){
        Bank bank = new Bank();

        Scanner sc= new Scanner(System.in);

        try{
            bank.loadAccounts();
        }
        catch( Exception e){
            System.out.println("Could not load accounts "+e.getMessage());
        }

        while(true){
            System.out.println("Banky Menu");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. View Account");
            System.out.println("6. Exit");
            System.out.print("Select: ");

            int choice =sc.nextInt();
            sc.nextLine();

            try {
                switch(choice){
                    case 1:
                    System.out.print("Account Number: ");
                    String accNo= sc.nextLine();
                    System.out.println("Account Holder");
                    String name= sc.nextLine();
                    System.out.println("Inital Balance: ");
                    double bal= sc.nextDouble();
                    sc.nextLine();
                    bank.addAccount(new BankAccount(accNo, name, bal));
                    System.out.println("Account Sucessfully created!");
                    break;

                    case 2:
                    System.out.println("Account Number");
                    String acc= sc.nextLine();
                    BankAccount BA= bank.getAccount(acc);
                    if(BA != null){
                        System.out.println("Amount: ");
                        BA.deposit(sc.nextDouble());
                        System.out.println("Amount deposited sucessfully!");
                    }
                    else{
                        System.out.println("Account not found");
                    }
                    sc.nextLine();
                    break;

                    case 3:
                    System.out.println("Account Number: ");
                    String wdNo= sc.nextLine();
                    BankAccount wdAcc= bank.getAccount(wdNo);
                    if(wdAcc!=null){
                        System.out.println("Amount: ");
                        boolean wd= wdAcc.withdraw(sc.nextDouble());
                        if(wd){
                            System.out.println("Withdrawn amount sucessfully");
                        }
                        else{
                            System.out.println("Account not found");
                        }
                        sc.nextLine();
                        break;
                    }

                    case 4:
                    System.out.println("From Account: ");
                    String from = sc.nextLine();
                    System.out.println("To account: ");
                    String to= sc.nextLine();
                    System.out.println("Amount: ");
                    double amount= sc.nextDouble();
                    sc.nextLine();
                    BankAccount fromAcc= bank.getAccount(from);
                    BankAccount toAcc = bank.getAccount(to);
                    if (fromAcc != null && toAcc != null) {
                        boolean ok = fromAcc.transfer(toAcc, amount);
                        System.out.println(ok ? "Transfer complete." : "Transfer failed.");
                        }
                    else{
                        System.out.println("One or both accounts not found.");
                    }
                    break;

                    case 5:
                        System.out.print("Account Number: ");
                        String view = sc.nextLine();
                        BankAccount accB= bank.getAccount(view);
                        if (accB != null) {
                            System.out.println("📄 Account Details:");
                            System.out.println("Number: " + accB.getAccountNumber());
                            System.out.println("Name: " + accB.getAccountHolderName());
                            System.out.println("Balance: " + accB.getBalance());
                        } else {
                            System.out.println("Account not found.");
                        }
                        break;

                    case 6:
                        bank.saveAccounts();
                        System.out.println("Goodbye! Data saved.");
                        return;

                    default:
                        System.out.println("Invalid choice.");

                }
            } catch (Exception e) {
                System.out.println("Error: "+e.getMessage());
                sc.nextLine();
            }
        }

    }
}
