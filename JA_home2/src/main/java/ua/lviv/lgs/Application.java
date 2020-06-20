package ua.lviv.lgs;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Application {

	public static void main(String[] args)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {

		UsersDao usersDao = new UsersDao(ConectionUtils.openConnection());
		MagazineDao magazineDao = new MagazineDao(ConectionUtils.openConnection());
		MagazineUsersDao magazineUsersDao = new MagazineUsersDao(ConectionUtils.openConnection());

		usersDao.readAll().stream().forEach(System.out::println);
		magazineDao.readAll().stream().forEach(System.out::println);
		magazineUsersDao.readAll().stream().forEach(System.out::println);

		System.out.println("-------********-------------------------");

		List<MagazineUsers> allMagazineUsers = magazineUsersDao.readAll().stream().filter(o -> o.getUsersId() == 3)
				.collect(Collectors.toList());
		allMagazineUsers.forEach(System.out::println);

		List<Users> listOfUsers = new ArrayList<>();
		listOfUsers.add(new Users("logos", "loglviv25", "Peter", "Petrov", "good user"));
		listOfUsers.add(new Users("Mixos", "5552225", "Misha", "Sudor", "bad user"));
		listOfUsers.add(new Users("Amigo", "5521155", "Olia", "Ivanova", "good user"));
		listOfUsers.add(new Users("admin", "admin", "Roman", "Romaniv", "admin"));

		listOfUsers.forEach(users -> {
			try {
				usersDao.insert(users);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		List<Magazine> listOfMagazine = new ArrayList<>();
		listOfMagazine.add(new Magazine("Supercar", "about car", 150, "asc2020t"));
		listOfMagazine.add(new Magazine("People", "about people", 90, "peo2020ty"));
		listOfMagazine.add(new Magazine("travel", "about travel", 170, "trv2020t2"));

		listOfMagazine.forEach(magazine -> {
			try {
				magazineDao.insert(magazine);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		List<MagazineUsers> listOfMagazineUser = new ArrayList<>();
		listOfMagazineUser.add(new MagazineUsers(3, 4));
		listOfMagazineUser.add(new MagazineUsers(3, 3));
		listOfMagazineUser.add(new MagazineUsers(3, 2));

		listOfMagazineUser.forEach(magazineUser -> {
			try {
				magazineUsersDao.insert(magazineUser);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

//		Users readUser =  usersDao.read("Mixos");
//		Magazine readMagazine = magazineDao.read(2);
//		MagazineUsers readMagazineUsersDao = magazineUsersDao.read(3);
//		
//		System.out.println("---------------------------------------------");
//		System.out.println(readMagazineUsersDao);

//		readUser.setLastName(readUser.getLastName() + "-Gresko");
//		usersDao.update(readUser);

//		readMagazine.setDescription(readMagazine.getDescription() + "- about O");
//		magazineDao.update(readMagazine);

//		usersDao.delete("Amigo");
//		magazineDao.delete(3);

		System.out.println("---------------------------------------------");
//		magazineUsersDao.readAll().stream()
//				.filter(o -> o.getUsersId() == 3).forEach(System.out::println);
		magazineUsersDao.delete(3, 2);

		// ---------------------------------------------------------------------
		while (true) {
			boolean again = true;
			boolean userRegistered = false;
			boolean userAdmin = false;
			String userLogin = "";
			String userPassword = "";

		

			if (!userRegistered && !userAdmin) {
				while (again) {
					Menu.unregistered();
					switch (scanInt("")) {
					case 0:
						magazineDao.readAll().stream().forEach(System.out::println);
						break;
					case 1:
						userLogin = scanLine("login");
						try {
							if (usersDao.read(userLogin).getUserName().equals(userLogin)) {
								userPassword = scanLine("password");
								if (usersDao.read(userLogin).getPassword().equals(userPassword)) {
									userRegistered = true;
									userAdmin = false;
									again = false;
									if (userLogin.equals("admin")) {
										userRegistered = false;
										userAdmin = true;
									}
								}
							}
						} catch (NullPointerException e) {
							System.out.println("No user with such name");
						}
						break;
					case 2:
						String userLog = scanLine("enter new user name");
						try {
							if (usersDao.read(userLog).getUserName().equals(userLog)) {
								System.out.println("a user with that name exists");
							}
						} catch (NullPointerException e) {
							String userPass = scanLine("enter new user password");
							String userFirst = scanLine("enter new user first name");
							String userLast = scanLine("enter new user last name");
							String userDetail = "";

							usersDao.insert(new Users(userLog, userPass, userFirst, userLast, userDetail));
						}
						break;
					default:
						break;
					}
				}
			}

			if (userRegistered) {
				again = true;
				while (again) {
					Menu.registered(userLogin);
					switch (scanInt("")) {
					case 0:
						magazineDao.readAll().stream().forEach(System.out::println);
						break;
					case 1:
						Menu.addSubscriptionMagazine(usersDao.read(userLogin).getId(), magazineDao, magazineUsersDao);
						break;
					case 2:
						Menu.removeSubscriptionMagazine(usersDao.read(userLogin).getId(), magazineDao,
								magazineUsersDao);
						break;
					case 3:
						Menu.showSubscriptionMagazine(usersDao.read(userLogin).getId(), magazineDao, magazineUsersDao);
						break;
					case 4:
						String userPass = scanLine("enter new user password");
						String userFirst = scanLine("enter new user first name");
						String userLast = scanLine("enter new user last name");
						String userDetail = "";

						usersDao.update(new Users(userLogin, userPass, userFirst, userLast, userDetail));
						break;
					case 5:
						userRegistered = false;
						userAdmin = false;
						again = false;
						userLogin = "";
						userPassword = "";
						break;
					default:
						break;
					}
				}
			}

			if (userAdmin) {
				again = true;
				while (again) {
					Menu.admin();
					switch (scanInt("")) {
					case 0:
						magazineDao.readAll().stream().forEach(System.out::println);
						break;
					case 1:
						Menu.addMagazine(magazineDao);
						break;
					case 2:
						Menu.removeMagazine(magazineDao, magazineUsersDao);
						break;
					case 3:
						String userPass = scanLine("enter new user password");
						String userFirst = scanLine("enter new user first name");
						String userLast = scanLine("enter new user last name");
						String userDetail = "";

						usersDao.update(new Users(userLogin, userPass, userFirst, userLast, userDetail));
						break;
					case 4:
						userRegistered = false;
						userAdmin = false;
						again = false;
						userLogin = "";
						userPassword = "";
						break;
					default:
						break;
					}
				}
			}
		}
		// ---------------------------------------------------------------------

	}

	public static int scanInt(String message) {

		System.out.println(message);
		Scanner sc = new Scanner(System.in);
		int scanInt = -1;
		try {
			scanInt = sc.nextInt();
		} catch (java.util.InputMismatchException e) {
			System.out.println("enter digit");
		}
		return scanInt;

	}

	public static String scanLine(String message) {

		System.out.println(message);
		Scanner sc = new Scanner(System.in);
		return sc.nextLine();
	}

}
