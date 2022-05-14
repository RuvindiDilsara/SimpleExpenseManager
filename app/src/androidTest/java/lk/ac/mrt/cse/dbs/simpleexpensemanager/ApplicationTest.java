/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.PersistentExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest {

    private static ExpenseManager expenseManager;

    @Before
    public void setUp() throws ExpenseManagerException {

        Context context = ApplicationProvider.getApplicationContext();
        expenseManager = new PersistentExpenseManager(context);
    }

    @Test
    public void testAddAccount() {
        expenseManager.addAccount("1234", "BOC", "Ruvindi", 1000.0);
        List<String> accountNumbersList = expenseManager.getAccountNumbersList();
        assertTrue(accountNumbersList.contains("1234"));
    }

    @Test
    public void testUpdateAccountBalance() throws InvalidAccountException {
        Account preAccount = expenseManager.getAccountsDAO().getAccount("1234");
        double preBalance = preAccount.getBalance();
        expenseManager.updateAccountBalance("1234", 12, 05, 2022, ExpenseType.EXPENSE, "1000");
        Account postAccount = expenseManager.getAccountsDAO().getAccount("1234");
        double postBalance = postAccount.getBalance();
        assertTrue(preBalance-postBalance == 1000.0);
    }

}