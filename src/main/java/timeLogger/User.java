package timeLogger;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private String password;
	private long timeTotal = 0L;
	private transient long startTime;
	private int totalMeetings = 0;
	private transient boolean loggedIn = false;

	public User(String nameInput, String passwordInput) {
		this.name = nameInput;
		this.password = passwordInput;
	}

	public String toString() {
		return this.name;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String n) {
		this.name = n;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String p) {
		this.password = p;
	}

	public int getTotalMeetings() {
		return this.totalMeetings;
	}

	public void addMeeting() {
		this.totalMeetings += 1;
	}

	public void setTotalMeetings(int t) {
		this.totalMeetings = t;
	}

	public boolean getLoggedIn() {
		return this.loggedIn;
	}

	public void setLoggedIn(boolean l) {
		this.loggedIn = l;
	}

	public long getTimeTotal() {
		return this.timeTotal;
	}

	public void setTimeTotal(long t) {
		this.timeTotal = t;
	}

	public void addTime() {
		this.timeTotal += System.nanoTime() - this.startTime;
	}

	public void setStartTime() {
		this.startTime = System.nanoTime();
	}

	public String getTimeString() {
		long seconds = this.timeTotal / 1000000000L;
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
}