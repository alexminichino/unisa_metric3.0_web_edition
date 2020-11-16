package it.unisa.metric.web.utils.login;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.util.ArrayList;

import org.apache.poi.ddf.EscherColorRef.SysIndexSource;

import it.unisa.metric.web.WebConstants;
import it.unisa.metric.web.utils.FileUtils;



public class AccountManager {
	

	

	private static final String FILENAME = "profili.dat";



	public AccountManager() {

	}


	public static void registerAccount(Account a) {
		ArrayList<Account> accountsList;
		if((accountsList= FileUtils.deserializeObj(WebConstants.USERS_PROFILE_PATH,FILENAME)) == null) {
			accountsList = new ArrayList<Account>();
		}
		accountsList.add(a);
		FileUtils.serializeObj(accountsList, WebConstants.USERS_PROFILE_PATH , FILENAME);
		
	}
	
	

	public static boolean login(Account a) {
		ArrayList<Account> accountsList;
		if((accountsList= FileUtils.deserializeObj(WebConstants.USERS_PROFILE_PATH,FILENAME)) == null) {
			return false;
		}
		for (Account account : accountsList) {
			if(account.equals(a))
				return true;
		}
		return false;
	}

	public static boolean userExists(String username) {
		ArrayList<Account> accountsList;
		if((accountsList= FileUtils.deserializeObj(WebConstants.USERS_PROFILE_PATH,FILENAME)) == null) {
			return false;
		}
		for (Account account : accountsList) {
			if(account.getUsername().equals(username))
				return true;
		}
		return false;
	}
	
	
	
	
	
	

	
	
}
