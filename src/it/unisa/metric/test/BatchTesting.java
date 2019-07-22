package it.unisa.metric.test;

import java.util.ArrayList;
import java.util.Scanner;

import it.unisa.metric.Core;
import it.unisa.metric.LocalException;
import it.unisa.metric.Utils;
import it.unisa.metric.web.WebConstants;
import it.unisa.metric.web.utils.FileUtils;

public class BatchTesting {

	public static void main(String[] args) {
		
		ArrayList<String> s = FileUtils.getListOfSubDir(WebConstants.TESTING_DIRECTORY_PATH);
		for (String subDir : s) {
			String[] arguments = { 
		 			"-path", 
		 			WebConstants.TESTING_DIRECTORY_PATH+subDir
		 	};
			try {
				new Core(arguments);
			} catch (LocalException e) {
				Utils.print(e);
				System.exit(1);
			}
		}
		
		
		Scanner scanner = new Scanner(System.in);
		scanner.nextLine();
		System.exit(0);
		
	}

}
