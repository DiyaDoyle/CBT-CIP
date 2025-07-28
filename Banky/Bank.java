
import java.io.*;
import java.util.*;


public class Bank {

    Map<String, BankAccount> accounts= new HashMap<>();

    public String fileName= "accounts.txt";
    
    public void addAccount(BankAccount acc){
        accounts.put(acc.getAccountNumber(), acc);
    }

    public BankAccount getAccount(String accountNumber){
        return accounts.get(accountNumber);
    }

    public void saveAccounts() throws IOException  {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

        for(BankAccount acc: accounts.values()){
            writer.write(acc.toString());
            writer.newLine();

        }

        writer.close();
    }

    public void loadAccounts() throws IOException {
        File file = new File(fileName);
        if(!file.exists()) return;

        BufferedReader reader= new BufferedReader(new FileReader(fileName));

        String line;
        while((line = reader.readLine()) != null){
            BankAccount acc= BankAccount.fromString(line);
            accounts.put(acc.getAccountNumber(), acc);
        }

        reader.close();

    }



}
