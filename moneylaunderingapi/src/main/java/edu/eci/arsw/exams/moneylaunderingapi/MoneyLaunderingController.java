package edu.eci.arsw.exams.moneylaunderingapi;


import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import edu.eci.arsw.exams.moneylaunderingapi.service.MoneyLaunderingService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class MoneyLaunderingController
{
    @Autowired
    @Qualifier("MoneyLaunderingServiceStub")
    MoneyLaunderingService mns;

    @RequestMapping( value = "/fraud-bank-accounts", method = RequestMethod.GET)
    public ResponseEntity<?> offendingAccounts() {
       try {

           return new ResponseEntity<>(mns.getSuspectAccounts(), HttpStatus.ACCEPTED);
       }catch (Exception ex) {
           Logger.getLogger( MoneyLaunderingController.class.getName() ).log( Level.SEVERE, null, ex );
           return new ResponseEntity<>( ex.getMessage(), HttpStatus.NOT_FOUND );
       }
    }

    @RequestMapping( value = "/fraud-bank-accounts/{accountId}", method = RequestMethod.GET)
    public ResponseEntity<?> getOffendingAccountsById( @PathVariable ("accountId") String accountId) {
        try {
            return new ResponseEntity<>(mns.getSuspectAccountsById(accountId), HttpStatus.ACCEPTED);
        }catch (Exception ex) {
            Logger.getLogger( MoneyLaunderingController.class.getName() ).log( Level.SEVERE, null, ex );
            return new ResponseEntity<>( ex.getMessage(), HttpStatus.NOT_FOUND );
        }
    }

    @RequestMapping(value = "/fraud-bank-accounts",method = RequestMethod.POST)
    @ResponseBody
    public  synchronized ResponseEntity<?> addAccount(@RequestBody SuspectAccount sa){
        try {
            System.out.print(sa.getAccountId()+ "   "+sa.getAmountOfSmallTransactions());
            mns.addAccount( sa );
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception ex) {
            Logger.getLogger(MoneyLaunderingController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/fraud-bank-accounts/{accountId}",method = RequestMethod.PUT)
    @ResponseBody
    public  synchronized ResponseEntity<?> setAccount(@RequestBody SuspectAccount sa){
        try {
            mns.updateAccountStatus( sa );
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception ex) {
            Logger.getLogger(MoneyLaunderingController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.FORBIDDEN);
        }
    }

}
