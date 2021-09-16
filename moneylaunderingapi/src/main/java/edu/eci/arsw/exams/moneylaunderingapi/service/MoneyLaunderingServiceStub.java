package edu.eci.arsw.exams.moneylaunderingapi.service;

import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Repository
@Qualifier("MoneyLaunderingServiceStub")
public class MoneyLaunderingServiceStub implements MoneyLaunderingService {
    final ConcurrentHashMap<String,SuspectAccount> suspectAccountsList=new ConcurrentHashMap();
    public MoneyLaunderingServiceStub(){
        SuspectAccount sp = new SuspectAccount("A1", 12000);
        SuspectAccount sp1 = new SuspectAccount("A2", 6000);
        SuspectAccount sp2 = new SuspectAccount("A3", 45000);
        SuspectAccount sp3 = new SuspectAccount("A4", 23400);
        suspectAccountsList.put(sp.getAccountId(),sp);
        suspectAccountsList.put(sp1.getAccountId(),sp1);
        suspectAccountsList.put(sp2.getAccountId(),sp2);
        suspectAccountsList.put(sp3.getAccountId(),sp3);
    }

    @Override
    public void updateAccountStatus(SuspectAccount suspectAccount) {
        SuspectAccount s = suspectAccountsList.get(suspectAccount.getAccountId());
        s.setAmountOfSmallTransactions(suspectAccount.getAmountOfSmallTransactions()  );
        suspectAccountsList.put(suspectAccount.getAccountId(),s);
    }

    @Override
    public SuspectAccount getAccountStatus(String accountId) {
        //TODO
        return null;
    }

    @Override
    public void addAccount(SuspectAccount suspectAccount) {
        if (suspectAccountsList.containsKey(suspectAccount.getAccountId())) {
            throw new Error( "The given suspectAccount already exists: " + suspectAccount );
        }else{
            System.out.println(suspectAccount.getAmountOfSmallTransactions());
            suspectAccountsList.put(suspectAccount.getAccountId(), suspectAccount);
        }
    }

    @Override
    public Set<SuspectAccount> getSuspectAccounts() {
        Set<SuspectAccount> ans = new HashSet<SuspectAccount>();
        for(String key:suspectAccountsList.keySet()){
            ans.add(suspectAccountsList.get(key));
        }
        return ans;
    }

    @Override
    public SuspectAccount getSuspectAccountsById(String accountId) {
        return suspectAccountsList.get( accountId );
    }

}
