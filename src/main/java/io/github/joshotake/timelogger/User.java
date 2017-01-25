package io.github.joshotake.timelogger;

public class User {
	
	String name = "";
	int hash = 0;
	long time = 0;
	boolean logged = false;
	long logIn = 0;
	
	public User(String name, int hash) {
		this.name = name;
		this.hash = hash;
		logged = false;
	}
	
	protected boolean checkPassword(String password) {
		return password.hashCode() == this.hash;
	}
	
	protected void addTime(long i) {
		this.time = this.time + i;
	}
	
	public long getTime() {
		if (logged) {
			return this.time + TimeLogger.getCurrentDifference(logIn);
		} else {
			return this.time;
		}
	}
	
	protected void logIn() {
		this.logged = true;
		this.logIn = System.currentTimeMillis();
	}
	
	protected void logOut() {
		this.logged = false;
		addTime(TimeLogger.getCurrentDifference(logIn));
		this.logIn = 0;
	}

}
