package edu.eci.arsw.exams.moneylaunderingapi.service;

import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
@Qualifier("MoneyLaunderingService")
public interface MoneyLaunderingService {
    void updateAccountStatus(SuspectAccount suspectAccount);
    SuspectAccount getAccountStatus(String accountId);
    void addAccount(SuspectAccount suspectAccount);
    Set<SuspectAccount> getSuspectAccounts();
    SuspectAccount getSuspectAccountsById(String accountId);
}
