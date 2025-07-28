

public class BankAccount {

    private String accountNumber;
    private String accountHolderName;
    private double balance;

    public BankAccount(String accountNumber, String accountHolderName, double balance){
        this.accountNumber= accountNumber;
        this.accountHolderName= accountHolderName;
        this.balance= balance;
    }

    public void deposit(double amount){
        balance+=amount;
    }

    public boolean withdraw(double amount){
        if(amount <= balance){
            balance -= amount;
            return true;
        }
        return false;
    }

    public boolean transfer(BankAccount target, double amount){
        if(withdraw(amount)){
            target.deposit(amount);
            return true;
        }
        return false;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName(){
        return accountHolderName;
    }

    public double getBalance(){
        return balance;
    }

    public String toString(){
        return accountNumber+" "+accountHolderName+" "+balance;
    }

    public static BankAccount fromString(String line){
        String parts[]= line.split(" ");
        double bal= Double.parseDouble(parts[2]);
        return new BankAccount(parts[0], parts[1], bal);
    }


}
