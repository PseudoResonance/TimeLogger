package io.github.joshotake.timelogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
	
	private static List<User> users = new ArrayList<User>();
	
	public static void addUser(User u) {
		users.add(u);
		TimeLogger.outListModel.addElement(u);
	}
	
	public static void removeUser(User u) {
		users.remove(u);
		TimeLogger.outListModel.removeElement(u);
		TimeLogger.inListModel.removeElement(u);
	}
	
	public static List<User> getUsers() {
		return users;
	}
	
	public static List<User> getInUsers() {
		List<User> ret = new ArrayList<User>();
		for (User u : users) {
			if (u.logged) {
				ret.add(u);
			}
		}
		return ret;
	}
	
	public static List<User> getOutUsers() {
		List<User> ret = new ArrayList<User>();
		for (User u : users) {
			if (!u.logged) {
				ret.add(u);
			}
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public static void deserializeUsers() {
		List<User> users = new ArrayList<User>();
		try {
			FileInputStream fis = new FileInputStream("users.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			users = (ArrayList<User>) ois.readObject();
			ois.close();
			fis.close();
		} catch (FileNotFoundException fe) {
			return;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return;
		} catch (ClassNotFoundException c) {
			System.out.println("Class not found");
			c.printStackTrace();
			return;
		}
		for (User u : users) {
			UserManager.users.add(u);
		}
		File importFile = new File("import.ser");
		if (importFile.exists()) {
			List<timeLogger.User> importUsers;
			try {
				FileInputStream fis = new FileInputStream("import.ser");
				ObjectInputStream ois = new ObjectInputStream(fis);
				importUsers = (ArrayList<timeLogger.User>) ois.readObject();
				ois.close();
				fis.close();
			} catch (FileNotFoundException fe) {
				return;
			} catch (IOException ioe) {
				ioe.printStackTrace();
				return;
			} catch (ClassNotFoundException c) {
				System.out.println("Class not found");
				c.printStackTrace();
				return;
			}
			for (timeLogger.User u : importUsers) {
				User newUser = new User(u.getName(), u.getPassword());
				newUser.setTimeTotal(u.getTimeTotal());
				newUser.setTotalMeetings(u.getTotalMeetings());
				UserManager.users.add(newUser);
			}
		}
	}

	public static void serializeUsers() {
		try {
			FileOutputStream fos = new FileOutputStream("users.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(users);
			oos.close();
			fos.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}
