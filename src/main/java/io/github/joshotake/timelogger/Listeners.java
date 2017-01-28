package io.github.joshotake.timelogger;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

import io.github.joshotake.timelogger.UserManager.LoggingType;

public class Listeners {
	
	static JDialog incorrectPasswordD;
	static JDialog oldPasswordD;
	static JDialog newPasswordD;
	static JDialog newPasswordCheckD;
	static JDialog passwordMatchD;
	static JDialog nameD;
	static JDialog firstPassD;
	static JDialog passAgainD;
	static JDialog nameInUseD;
	
	public static void setupDialog() {
		JOptionPane options = new JOptionPane("Incorrect Password!", JOptionPane.ERROR_MESSAGE);
		incorrectPasswordD = options.createDialog(null, "Incorrect Password");
		incorrectPasswordD.setFont(TimeLogger.font);
		JOptionPane options1 = new JOptionPane("Please Enter your Old Password!", JOptionPane.ERROR_MESSAGE);
		oldPasswordD = options1.createDialog(null, "Enter Old Password");
		oldPasswordD.setFont(TimeLogger.font);
		JOptionPane options2 = new JOptionPane("Please Enter a New Password!", JOptionPane.ERROR_MESSAGE);
		newPasswordD = options2.createDialog(null, "Enter New Password");
		newPasswordD.setFont(TimeLogger.font);
		JOptionPane options3 = new JOptionPane("Please Enter a New Password Again!", JOptionPane.ERROR_MESSAGE);
		newPasswordCheckD = options3.createDialog(null, "Enter New Password Again");
		newPasswordCheckD.setFont(TimeLogger.font);
		JOptionPane options4 = new JOptionPane("The Passwords Do Not Match!", JOptionPane.ERROR_MESSAGE);
		passwordMatchD = options4.createDialog(null, "Passwords Do Not Match");
		passwordMatchD.setFont(TimeLogger.font);
		JOptionPane options5 = new JOptionPane("Please Enter a Name!", JOptionPane.ERROR_MESSAGE);
		nameD = options5.createDialog(null, "Enter a Name");
		nameD.setFont(TimeLogger.font);
		JOptionPane options6 = new JOptionPane("Please Enter a Password!", JOptionPane.ERROR_MESSAGE);
		firstPassD = options6.createDialog(null, "Enter a Password");
		firstPassD.setFont(TimeLogger.font);
		JOptionPane options7 = new JOptionPane("Please Enter Your Password Again!", JOptionPane.ERROR_MESSAGE);
		passAgainD = options7.createDialog(null, "Enter Password Again");
		passAgainD.setFont(TimeLogger.font);
		JOptionPane options8 = new JOptionPane("That Name is Already in Use!", JOptionPane.ERROR_MESSAGE);
		nameInUseD = options8.createDialog(null, "Name in Use");
		nameInUseD.setFont(TimeLogger.font);
	}

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
		if (e.getValueIsAdjusting())
			return;
		if (list.equals(TimeLogger.inList)) {
			if (!list.isSelectionEmpty()) {
				TimeLogger.outList.clearSelection();
			}
		} else if (list.equals(TimeLogger.outList)) {
			if (!list.isSelectionEmpty()) {
				TimeLogger.inList.clearSelection();
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
				incorrectPasswordD.setVisible(true);
				TimeLogger.password.setText("");
			}
		}
	}

	public static void editUser() {
		String oldPassword = new String(TimeLogger.oldPassword.getPassword());
		String password = new String(TimeLogger.editPassword.getPassword());
		String passwordCheck = new String(TimeLogger.editPasswordCheck.getPassword());
		if (oldPassword.equalsIgnoreCase("")) {
			oldPasswordD.setVisible(true);
			TimeLogger.setEditUser();
			return;
		}
		if (password.equalsIgnoreCase("")) {
			newPasswordD.setVisible(true);
			TimeLogger.setEditUser();
			return;
		}
		if (passwordCheck.equalsIgnoreCase("")) {
			newPasswordCheckD.setVisible(true);
			TimeLogger.setEditUser();
			return;
		}
		if (UserManager.editingUser.checkPassword(oldPassword)) {
			if (!password.equals(passwordCheck)) {
				passwordMatchD.setVisible(true);
				TimeLogger.setEditUser();
				return;
			}
		} else {
			incorrectPasswordD.setVisible(true);
			TimeLogger.setEditUser();
			return;
		}
		UserManager.editing = false;
		UserManager.editingUser.setPassword(password);
		TimeLogger.editUser.setVisible(false);
	}
	
	public static void newUser() {
		String name = TimeLogger.name.getText();
		String password = new String(TimeLogger.newPassword.getPassword());
		String passwordCheck = new String(TimeLogger.newPasswordCheck.getPassword());
		if (name.equalsIgnoreCase("")) {
			nameD.setVisible(true);
			TimeLogger.setNewUser(name, password);
			return;
		}
		if (password.equalsIgnoreCase("")) {
			firstPassD.setVisible(true);
			TimeLogger.setNewUser(name, password);
			return;
		}
		if (passwordCheck.equalsIgnoreCase("")) {
			passAgainD.setVisible(true);
			TimeLogger.setNewUser(name, password);
			return;
		}
		for (User u : UserManager.getUsers()) {
			if (u.getName().equalsIgnoreCase(name)) {
				nameInUseD.setVisible(true);
				TimeLogger.setNewUser(name, password);
				return;
			}
		}
		if (password.equals(passwordCheck)) {
			User user = new User(name, password);
			UserManager.addUser(user);
			TimeLogger.newUser.setVisible(false);
		} else {
			passwordMatchD.setVisible(true);
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
		case EDITUSER:
			if (TimeLogger.outList.getSelectedValue() != null) {
				User u = TimeLogger.outList.getSelectedValue();
				UserManager.editing = true;
				UserManager.editingUser = u;
				TimeLogger.setEditUser();
			} else if (TimeLogger.inList.getSelectedValue() != null) {
				User u = TimeLogger.inList.getSelectedValue();
				UserManager.editing = true;
				UserManager.editingUser = u;
				TimeLogger.setEditUser();
			}
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
			if (e.getClickCount() >= 3) {
				int i = list.locationToIndex(e.getPoint());
				if (list.getMaxSelectionIndex() >= i) {
					list.setSelectedIndex(i);
					TimeLogger.inList.clearSelection();
					User u = (User) list.getModel().getElementAt(i);
					UserManager.editing = true;
					UserManager.editingUser = u;
					TimeLogger.setEditUser();
				}
			} else if (e.getClickCount() == 1) {
				int i = list.locationToIndex(e.getPoint());
				if (list.getMaxSelectionIndex() >= i) {
					list.setSelectedIndex(i);
					TimeLogger.inList.clearSelection();
					User u = (User) list.getModel().getElementAt(i);
					TimeLogger.setTime(u.getTimeString());
				}
			} else if (e.getClickCount() == 2) {
				int i = list.locationToIndex(e.getPoint());
				if (list.getMaxSelectionIndex() >= i) {
					list.setSelectedIndex(i);
					TimeLogger.inList.clearSelection();
					User u = (User) list.getModel().getElementAt(i);
					UserManager.logging = LoggingType.IN;
					UserManager.loggingUser = u;
					TimeLogger.setPasswordCheck();
				}
			}
		} else if (listid == 1) {
			if (e.getClickCount() >= 3) {
				int i = list.locationToIndex(e.getPoint());
				if (list.getMaxSelectionIndex() >= i) {
					list.setSelectedIndex(i);
					TimeLogger.outList.clearSelection();
					User u = (User) list.getModel().getElementAt(i);
					UserManager.editing = true;
					UserManager.editingUser = u;
					TimeLogger.setEditUser();
				}
			} else if (e.getClickCount() == 1) {
				int i = list.locationToIndex(e.getPoint());
				if (list.getMaxSelectionIndex() >= i) {
					list.setSelectedIndex(i);
					TimeLogger.outList.clearSelection();
					User u = (User) list.getModel().getElementAt(i);
					TimeLogger.setTime(u.getTimeString());
				}
			} else if (e.getClickCount() == 2) {
				int i = list.locationToIndex(e.getPoint());
				if (list.getMaxSelectionIndex() >= i) {
					list.setSelectedIndex(i);
					TimeLogger.outList.clearSelection();
					User u = (User) list.getModel().getElementAt(i);
					UserManager.logging = LoggingType.OUT;
					UserManager.loggingUser = u;
					TimeLogger.setPasswordCheck();
				}
			}
		}
	}
	
	enum ActionType {
		LOGOUT,
		LOGIN,
		NEWUSER,
		EDITUSER,
		EXPORT;
	}
	
}
