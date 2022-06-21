package ATMproject;

import java.sql.SQLException;
import java.util.Scanner;


public class main {

	public static void main(String[] args) {
		sqliteDB sql = new sqliteDB();
		userIn userin = new userIn();
		try (Scanner scan = new Scanner(System.in)) {
			String username = "";
			//sql.listOfAccounts();
			while (true) {
				System.out.println("Select the number\n"
						+ "1. log in\n"
						+ "2. register");
				
				Object option1 = scan.next();
				if (!check(option1)) {
					continue;
				}
				int option = Integer.valueOf((String) option1);
				if (option == 1 ) {
					System.out.println("Enter Username: ");
					username = scan.next();
					if (!sql.checkUsername(username)) {
						continue;
					}
					System.out.println("Enter Password: ");
					String password = scan.next();
					if (!sql.checkPassword(username, password)) {
						continue;
					}
					System.out.println("login successful");
					sql.close();
					break;
				}
				else if (option == 2 ){
					System.out.println("Enter Username(one word only): ");
					username = scan.next();
					if (!sql.checkDuplicate(username)) {
						continue;
					}
					System.out.println("Enter Password(one word only): ");
					String password = scan.next();
					sql.storing(username, password);
					System.out.println("registeration successful");
					sql.close();
					break;
				}
			}
			userin.showMenu(username);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static boolean check(Object a) {
		try {
		    int x = Integer.valueOf((String) a);
			System.out.println(x);
			if (x == 1 || x == 2){
				return true;
			}
			System.out.println("Your input is invalid, please try again");
		    return false;
		} catch(Exception e){
		    System.out.println("Your input is invalid, please try again");
		    return false;
		}
	}
}
