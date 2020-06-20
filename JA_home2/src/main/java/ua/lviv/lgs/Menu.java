package ua.lviv.lgs;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class Menu {
	
	public static void unregistered() {
		System.out.println();
		System.out.println("You aren't unregistered ");
		System.out.println("View magazine		Enter 0");
		System.out.println("log in			Enter 1");
		System.out.println("registration		Enter 2");		
	}
	
	public static void registered(String userName) {
		System.out.println();
		System.out.println(userName + " you are registered ");
		System.out.println("View magazine    				Enter 0");
		System.out.println("Add a magazine subscription			Enter 1");
		System.out.println("Remove a magazine subscription 			Enter 2");	
		System.out.println("Show magazine subscription 			Enter 3");
		System.out.println("Edit registration			 	Enter 4");
		System.out.println("Exit 						 Enter 5");
	}
	
	public static void admin() {
		System.out.println();
		System.out.println("You are admin ");
		System.out.println("View magazine    		Enter 0");
		System.out.println("Add magazine			Enter 1");
		System.out.println("Remove magazine 		Enter 2");
		System.out.println("Edit registration 		Enter 3");
		System.out.println("Exit 				Enter 4");
	}
	
	public static void addMagazine(MagazineDao magazineDao) throws SQLException {
		
		String name = Application.scanLine("name magazine");
		String description = Application.scanLine("description magazine");
		double price = Double.parseDouble(Application.scanLine("price magazine"));
		String isbn = Application.scanLine("isbn magazine");
		
		magazineDao.insert(new Magazine(name, description, price, isbn));
	}
	
	public static void removeMagazine(MagazineDao magazineDao, MagazineUsersDao magazineUsersDao) throws SQLException {
		
		int id = Application.scanInt("id magazine for remove");
		magazineUsersDao.deleteAllMagazine(id);
		magazineDao.delete(id);
	}
	
	public static void addSubscriptionMagazine(
			int userId,MagazineDao magazineDao, MagazineUsersDao magazineUsersDao) throws SQLException {
		int magazineId = 0;
		boolean again = true;
		double summ = 0;
		while(again) {
			magazineId = Application.scanInt("id magazine for subscription, enter 0 for exit");
			if(magazineId != 0) {
				magazineUsersDao.insert(new MagazineUsers(userId, magazineId));
				try {
					summ += magazineDao.read(magazineId).getPrice();
				} catch(NullPointerException e) {
					System.out.println("here is no film with such id");
				}
			} else {
				again = false;
			}
		}
		System.out.println("subscription is made for the amount " + summ);
		
	}
	
	public static void removeSubscriptionMagazine(
			int userId,MagazineDao magazineDao, MagazineUsersDao magazineUsersDao) throws SQLException {
		int magazineId = 0;
		boolean again = true;
		double summ = 0;
		while(again) {
			magazineId = Application.scanInt("id magazine for remove subscription, enter 0 for exit");
			if(magazineId != 0) {
				magazineUsersDao.delete(userId, magazineId);
				try {
					summ += magazineDao.read(magazineId).getPrice();
				} catch(NullPointerException e) {
					System.out.println("here is no film with such id");
				}
			} else {
				again = false;
			}
		}
		System.out.println("subscription is made for the amount " + summ);		
	}
	
	public static void showSubscriptionMagazine(
			int userId,MagazineDao magazineDao, MagazineUsersDao magazineUsersDao) throws SQLException {
		//magazineDao.readAll().stream().forEach(System.out::println);
		List<MagazineUsers> subscriptionUser = magazineUsersDao.readAll().stream()
				.filter(o -> o.getUsersId() == userId).collect(Collectors.toList());
		subscriptionUser.stream().forEach(o -> {
			try {
				System.out.println(magazineDao.read(o.getMagazineId()));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
	}
	

}
