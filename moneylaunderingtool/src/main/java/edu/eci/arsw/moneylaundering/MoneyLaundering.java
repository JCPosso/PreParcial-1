package edu.eci.arsw.moneylaundering;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MoneyLaundering
{
    private TransactionAnalyzer transactionAnalyzer;
    private TransactionReader transactionReader;
    private int amountOfFilesTotal;
    private AtomicInteger amountOfFilesProcessed;
    private static boolean isPaused= true;

    public MoneyLaundering()
    {
        transactionAnalyzer = new TransactionAnalyzer();
        transactionReader = new TransactionReader();
        amountOfFilesProcessed = new AtomicInteger();
        amountOfFilesProcessed.set(0);
    }

    private void processTransactionData(int begin, int end)
    {
        amountOfFilesProcessed.set(0);
        List<File> transactionFiles = getTransactionFileList();
        amountOfFilesTotal = transactionFiles.size();
        for(int i = begin ; i< end ; i++)
        {
            synchronized (amountOfFilesProcessed) {
                File transactionFile = transactionFiles.get( i );
                List<Transaction> transactions = transactionReader.readTransactionsFromFile( transactionFile );
                for ( Transaction transaction : transactions ) {
                    transactionAnalyzer.addTransaction( transaction );
                }
                amountOfFilesProcessed.incrementAndGet();
            }
        }
    }

    public void processTransactionData()
    {
        amountOfFilesProcessed.set(0);
        List<File> transactionFiles = getTransactionFileList();
        amountOfFilesTotal = transactionFiles.size();
        for(File transactionFile : transactionFiles)
        {
            List<Transaction> transactions = transactionReader.readTransactionsFromFile(transactionFile);
            for(Transaction transaction : transactions)
            {
                transactionAnalyzer.addTransaction(transaction);
            }
            amountOfFilesProcessed.incrementAndGet();
        }
    }

    public List<String> getOffendingAccounts()
    {
        return transactionAnalyzer.listOffendingAccounts();
    }

    private List<File> getTransactionFileList() {
        List<File> csvFiles = new ArrayList<>();
        try (Stream<Path> csvFilePaths = Files.walk(Paths.get("moneylaunderingtool/src/main/resources/"))
                .filter(path -> path.getFileName().toString().endsWith(".csv"))) {
            csvFiles = csvFilePaths.map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvFiles;
    }

    public static void main(String[] args)
    {
        MoneyLaundering moneyLaundering = new MoneyLaundering();
        int amountOfFilesTotal = moneyLaundering.getTransactionFileList().size();
        ArrayList<Thread> threadList = new ArrayList<Thread>();
        //ejecutar 5 hilos
        final int N =5;
        int  subSize = amountOfFilesTotal/N;
        for ( int i=0; i< N-1;i++){
            int iterator = i;
            Thread processingThread = new Thread(() -> moneyLaundering.processTransactionData(iterator*subSize,((iterator+1)*subSize)-1));
            threadList.add(processingThread);
        }
        Thread processingThread = new Thread(() -> moneyLaundering.processTransactionData((N-1)*subSize,amountOfFilesTotal));
        threadList.add(processingThread);
        for  ( Thread t : threadList ){
            t.start();
        }
        while(moneyLaundering.amountOfFilesProcessed.get() < amountOfFilesTotal)
        {
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if(line.contains("exit")) {
                System.exit(0);
                break;
            }else if(!isPaused){
                moneyLaundering.setPause(threadList);
                showReport(moneyLaundering);
            }
            else if(isPaused) moneyLaundering.setResume(threadList);
        }
        showReport(moneyLaundering);
    }

    private static void showReport( MoneyLaundering moneyLaundering) {
        String message = "Processed %d out of %d files.\nFound %d suspect accounts:\n%s";
        List<String> offendingAccounts = moneyLaundering.getOffendingAccounts();
        String suspectAccounts = offendingAccounts.stream().reduce("", (s1, s2)-> s1 + "\n"+s2);
        message = String.format(message, moneyLaundering.amountOfFilesProcessed.get(), moneyLaundering.amountOfFilesTotal, offendingAccounts.size(), suspectAccounts);
        System.out.println(message);
    }

    private void setPause(List<Thread> hilos) {
        isPaused=true;
        System.out.println("Pause");
        for (Thread h : hilos) {
            h.suspend();
        }
    }

    private void setResume(List<Thread> hilos) {
        isPaused= false;
        System.out.println("Resume");
        for (Thread h : hilos) {
            h.resume();
        }
    }
}
