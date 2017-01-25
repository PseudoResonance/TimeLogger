package io.github.joshotake.timelogger;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
	
	private static List<User> users = new ArrayList<User>();
	
	public static void addUser(User u) {
		users.add(u);
	}
	
	public static void removeUser(User u) {
		users.remove(u);
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

}
