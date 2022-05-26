package ATMproject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class userIn {
	
	private BigDecimal balance = new BigDecimal(0.00);
	private BigDecimal zeroComparer = new BigDecimal(0.00);
	private BigDecimal depositMoney = new BigDecimal(0.00);
	private BigDecimal withdrawMoney = new BigDecimal(0.00);
	private ArrayList<Character> options = new ArrayList<>();
	private char previous;
	private BigDecimal finalMoney = new BigDecimal(0);
	DecimalFormat df = new DecimalFormat("#,###.00");

	
	public void line() {
		System.out.println("==========================");
	}

	public void showMenu(String name) {
		sqliteDB sql = new sqliteDB();
		balance = sql.cash(name);
		System.out.printf("Welcome %s!\n",name).println();;
		char option;
		do {
			System.out.println("What would you like to do?\n"
					+ "A. Check your balance\n"
					+ "B. Make a deposit\n"
					+ "C. Make a withdraw\n"
					+ "D. View previous transaction\n"
					+ "E. Calculate interest\n"
					+ "F. Exit\n");
			line();
			Scanner scanner = new Scanner(System.in);
			System.out.print("Enter your option: ");
			option = Character.toLowerCase(scanner.next().charAt(0));
			options.add(option);
			switch(option) {
			case 'a':
				line();
				if (balance.equals(zeroComparer)) {
					System.out.println("Balance = $0.00");
					break;
				}
				System.out.println("Balance = $"+df.format(balance));
				line();
				break;
			case 'b':
				System.out.printf("Enter an amount to deposit: ");
				depositMoney = scanner.nextBigDecimal();
				balance = balance.add(depositMoney);
				sql.updateCash(name, balance);
				break;
			case 'c':
				System.out.printf("Enter an amount to withdraw: ");
				withdrawMoney = scanner.nextBigDecimal();
				if (withdrawMoney.compareTo(balance)==1) {
					line();
					System.out.println("Insufficient balance!");
					line();
					break;
				}
				balance = balance.subtract(withdrawMoney);
				sql.updateCash(name, balance);
				break;
			case 'd':
				previous = options.get(options.size()-2);
				if (previous == 'b') {
					line();
					System.out.println("Deposit : $"+df.format(depositMoney));
					line();
				}
				else if (previous == 'c') {
					line();
					System.out.println("Withdraw : $"+df.format(withdrawMoney));
					line();
				}
				else if (previous == 'a') {
					int a = 0;
					for (int i = 3;options.size() > i; i++) {
						previous = options.get(options.size()-i);
						if (previous == 'b') {
							line();
							System.out.println("Deposit : $"+df.format(depositMoney));
							line();
							a++;
							break;
						}
						else if (previous == 'c') {
							line();
							System.out.println("Withdraw : $"+df.format(withdrawMoney));							
							line();
							a++;
							break;
						}
					}
					if (a==0) {
						line();
						System.out.println("No Transaction occured");
						line();						
					}
				}
				else{
					System.out.println("No Transaction occured");
				}
				break;
			case 'e':
				System.out.printf("Enter how many years of accrued interest: ");
				int year = scanner.nextInt();
				BigDecimal balanceBD = balance;
				BigDecimal wholeInterest = new BigDecimal(year).multiply(new BigDecimal(0.015)).multiply(balanceBD);
				finalMoney = balanceBD.add(wholeInterest);
				line();
				System.out.printf("The current interest rate is 1.5\n"
						+ "After %dyears, your balance wll be $%.1f",year,finalMoney).println();
				line();
				break;
			case 'f':
				scanner.close();
				break;
			default:
				line();
				System.out.println("Invalid input");
				line();
			}
		}while(option != 'f');
		line();
		System.out.println("Thank you for Banking with us!");
	}

	
}
