package io.github.joshotake.timelogger;

import java.io.Serializable;

import org.jasypt.util.password.StrongPasswordEncryptor;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	String name = "";
	String pass = "";
	long time = 0;
	boolean logged = false;
	transient long logIn = 0L;
	transient boolean first = false;
	private long timeTotal = 0L;
	private int totalMeetings = 0;
	
	public User(String name, String password) {
		this.name = name;
		StrongPasswordEncryptor spe = new StrongPasswordEncryptor();
		this.pass = spe.encryptPassword(password);
		logged = false;
	}
	
	protected boolean checkPassword(String password) {
		StrongPasswordEncryptor spe = new StrongPasswordEncryptor();
		return spe.checkPassword(password, this.pass);
	}
	
	protected void setPassword(String password) {
		StrongPasswordEncryptor spe = new StrongPasswordEncryptor();
		this.pass = spe.encryptPassword(password);
	}

	public String toString() {
		return this.name;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTotalMeetings() {
		return this.totalMeetings;
	}

	public void addMeeting() {
		this.totalMeetings += 1;
	}

	public void setTotalMeetings(int i) {
		this.totalMeetings = i;
	}

	public boolean getLogged() {
		return this.logged;
	}

	public void setLoggedIn(boolean b) {
		this.logged = b;
	}

	public long getTimeTotal() {
		return this.timeTotal;
	}

	public void setTimeTotal(long t) {
		this.timeTotal = t;
	}

	public void addTime() {
		this.timeTotal += System.nanoTime() - this.logIn;
	}

	public void setStartTime() {
		this.logIn = System.nanoTime();
	}

	public String getTimeString() {
		long seconds = 0L;
		if (logged) {
			seconds = (this.timeTotal + System.nanoTime() - this.logIn) / 1000000000L;
		} else {
			seconds = this.timeTotal / 1000000000L;
		}
		long hours = seconds / 3600L;
		seconds %= 3600L;
		long minutes = seconds / 60L;
		seconds %= 60L;
		return "Time: " + hours + " hours, " + minutes + " minutes, and " + seconds + " seconds. Meetings: " + this.totalMeetings;
	}

	public long getHours() {
		long hours = this.timeTotal / 3600L / 1000000000L;
		return hours;
	}

	public long getMinutes() {
		long minutes = this.timeTotal / 1000000000L % 3600L / 60L;
		return minutes;
	}

	public long getSeconds() {
		long seconds = this.timeTotal / 1000000000L % 3600L % 60L;
		return seconds;
	}
	
	protected void logIn() {
		this.logged = true;
		if (!first) {
			addMeeting();
		}
		this.first = true;
		this.logIn = System.nanoTime();
		TimeLogger.inListModel.addElement(this);
		TimeLogger.outListModel.removeElement(this);
	}
	
	protected void logOut() {
		this.logged = false;
		addTime();
		this.logIn = 0;
		TimeLogger.outListModel.addElement(this);
		TimeLogger.inListModel.removeElement(this);
	}

}
