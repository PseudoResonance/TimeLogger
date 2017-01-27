package io.github.joshotake.timelogger;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

import io.github.joshotake.timelogger.UserManager.LoggingType;

public class Listeners {

	public static void list(int id, ListSelectionEvent e) {
		@SuppressWarnings("unchecked")
		JList<User> list = (JList<User>) e.getSource();
		ListSelectionModel lsm = list.getSelectionModel();
		if (lsm.isSelectionEmpty()) {
			TimeLogger.setTime("");
		} else {
			int min = lsm.getMinSelectionIndex();
			int max = lsm.getMaxSelectionIndex();
			for (int i = min; i <= max; i++) {
				if (lsm.isSelectedIndex(i)) {
					User u = (User) list.getModel().getElementAt(i);
					TimeLogger.setTime(u.getTimeString());
				}
			}
		}
	}
	
	public static void close(WindowEvent e) {
		String ObjButtons[] = { "Save and Exit", "Save without Exiting", "Cancel" };
		int PromptResult = JOptionPane.showOptionDialog(null, "Are you sure you want to exit?", "Time Logger", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[0]);
		if (PromptResult == 0) {
			TimeLogger.close();
			System.exit(0);
		} else if (PromptResult == 1) {
			System.exit(0);
		}
	}
	
	public static void checkPassword() {
		if (UserManager.logging == LoggingType.IN || UserManager.logging == LoggingType.OUT) {
			User u = UserManager.loggingUser;
			if (u.checkPassword(new String(TimeLogger.password.getPassword()))) {
				if (UserManager.logging == LoggingType.IN) {
					u.logIn();
				} else if (UserManager.logging == LoggingType.OUT) {
					u.logOut();
				}
				UserManager.logging = LoggingType.NONE;
				UserManager.loggingUser = null;
				TimeLogger.passwordCheck.setVisible(false);
			} else {
				JOptionPane.showMessageDialog(TimeLogger.passwordCheck, "Incorrect Password!", "Incorrect Password", JOptionPane.ERROR_MESSAGE);
				TimeLogger.password.setText("");
			}
		}
	}
	
	public static void newUser() {
		String name = TimeLogger.name.getText();
		String password = new String(TimeLogger.newPassword.getPassword());
		String passwordCheck = new String(TimeLogger.newPasswordCheck.getPassword());
		if (name.equalsIgnoreCase("")) {
			JOptionPane.showMessageDialog(null, "Please Enter a Name!", "Enter a Name", JOptionPane.ERROR_MESSAGE);
			TimeLogger.setNewUser(name, password);
			return;
		}
		if (password.equalsIgnoreCase("")) {
			JOptionPane.showMessageDialog(null, "Please Enter a Password!", "Enter a Password", JOptionPane.ERROR_MESSAGE);
			TimeLogger.setNewUser(name, password);
			return;
		}
		if (passwordCheck.equalsIgnoreCase("")) {
			JOptionPane.showMessageDialog(null, "Please Enter Your Password Again", "Enter Password Again", JOptionPane.ERROR_MESSAGE);
			TimeLogger.setNewUser(name, password);
			return;
		}
		for (User u : UserManager.getUsers()) {
			if (u.getName().equalsIgnoreCase(name)) {
				JOptionPane.showMessageDialog(null, "That Name is Already in Use!", "Name in Use", JOptionPane.ERROR_MESSAGE);
				TimeLogger.setNewUser(name, password);
				return;
			}
		}
		if (password.equals(passwordCheck)) {
			User user = new User(name, password);
			UserManager.addUser(user);
			TimeLogger.newUser.setVisible(false);
		} else {
			JOptionPane.showMessageDialog(null, "The Passwords Do Not Match!", "Passwords Do Not Match", JOptionPane.ERROR_MESSAGE);
			TimeLogger.setNewUser(name, password);
			return;
		}
	}
	
	public static void action(ActionType id, ActionEvent e) {
		switch (id) {
		case LOGIN:
			if (TimeLogger.outList.getSelectedValue() != null) {
				User selected = TimeLogger.outList.getSelectedValue();
				UserManager.logging = LoggingType.IN;
				UserManager.loggingUser = selected;
				TimeLogger.setPasswordCheck();
			}
			break;
		case LOGOUT:
			if (TimeLogger.inList.getSelectedValue() != null) {
				User selected = TimeLogger.inList.getSelectedValue();
				UserManager.logging = LoggingType.OUT;
				UserManager.loggingUser = selected;
				TimeLogger.setPasswordCheck();
			}
			break;
		case NEWUSER:
			TimeLogger.setNewUser();
			break;
		case EXPORT:
			TimeLogger.makeExcelSheet();
		default:
			break;
		}
	}
	
	public static void mouseClick(int listid, MouseEvent e) {
		@SuppressWarnings("unchecked")
		JList<User> list = (JList<User>) e.getSource();
		if (listid == 0) {
			if (e.getClickCount() >= 2) {
				int i = list.locationToIndex(e.getPoint());
				if (list.getMaxSelectionIndex() >= i) {
					User u = (User) list.getModel().getElementAt(i);
					UserManager.logging = LoggingType.IN;
					UserManager.loggingUser = u;
					TimeLogger.setPasswordCheck();
				}
			} else if (e.getClickCount() == 1) {
				int i = list.locationToIndex(e.getPoint());
				if (list.getMaxSelectionIndex() >= i) {
					User u = (User) list.getModel().getElementAt(i);
					TimeLogger.setTime(u.getTimeString());
				}
			}
		} else if (listid == 1) {
			if (e.getClickCount() >= 2) {
				int i = list.locationToIndex(e.getPoint());
				if (list.getMaxSelectionIndex() >= i) {
					User u = (User) list.getModel().getElementAt(i);
					UserManager.logging = LoggingType.OUT;
					UserManager.loggingUser = u;
					TimeLogger.setPasswordCheck();
				}
			} else if (e.getClickCount() == 1) {
				int i = list.locationToIndex(e.getPoint());
				if (list.getMaxSelectionIndex() >= i) {
					User u = (User) list.getModel().getElementAt(i);
					TimeLogger.setTime(u.getTimeString());
				}
			}
		}
	}
	
	enum ActionType {
		LOGOUT,
		LOGIN,
		NEWUSER,
		EXPORT;
	}
	
}
