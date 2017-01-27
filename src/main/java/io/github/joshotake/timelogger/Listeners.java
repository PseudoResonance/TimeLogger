package io.github.joshotake.timelogger;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

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
	
	public static void getPassword(final boolean logIn, final User user) {
		final JPanel panel = new JPanel();
		JLabel label = new JLabel("Enter Password:");
		final JPasswordField pass = new JPasswordField(20);
		panel.add(label);
		panel.add(pass);
		pass.requestFocus();
		pass.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					char[] password = pass.getPassword();
					boolean b = user.checkPassword(new String(password));
					if (b) {
						if (logIn) {
							user.logIn();
							panel.getParent().setVisible(false);
						} else {
							user.logOut();
							panel.getParent().setVisible(false);
						}
					} else {
						JOptionPane.showMessageDialog(null, "Incorrect Password!", "Incorrect Password", JOptionPane.ERROR_MESSAGE);
						getPassword(logIn, user);
					}
				}
			}
			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
		});
		String[] options = new String[] { "Okay", "Cancel" };
		int option = JOptionPane.showOptionDialog(null, panel, "Enter Password", JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);
		if (option == 0) {
			char[] password = pass.getPassword();
			boolean b = user.checkPassword(new String(password));
			if (b) {
				if (logIn) {
					user.logIn();
				} else {
					user.logOut();
				}
			} else {
				JOptionPane.showMessageDialog(null, "Incorrect Password!", "Incorrect Password", JOptionPane.ERROR_MESSAGE);
				getPassword(logIn, user);
			}
		}
	}
	
	public static void newUser() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(400, 75));
		panel.add(new JLabel("Enter Name:"));
		JTextField namei = new JTextField(20);
		namei.requestFocus();
		panel.add(namei);
		panel.add(new JLabel("Enter Password:"));
		JPasswordField passi = new JPasswordField(20);
		panel.add(passi);
		panel.add(new JLabel("Enter Password Again:"));
		JPasswordField passChecki = new JPasswordField(20);
		panel.add(passChecki);
		String[] options = new String[] { "Create", "Cancel" };
		int option = JOptionPane.showOptionDialog(null, panel, "Create New User", JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);
		if (option == 0) {
			String name = namei.getText();
			char[] passc = passi.getPassword();
			String pass = new String(passc);
			char[] passCheckc = passChecki.getPassword();
			String passCheck = new String(passCheckc);
			for (User u : UserManager.getUsers()) {
				if (u.getName().equalsIgnoreCase(name)) {
					JOptionPane.showMessageDialog(null, "Name Already Taken!", "Name Already Taken", JOptionPane.ERROR_MESSAGE);
					newUserRepeat(name, pass);
					return;
				}
			}
			if (pass.equals(passCheck)) {
				User user = new User(name, pass);
				UserManager.addUser(user);
			} else {
				JOptionPane.showMessageDialog(null, "Passwords Do Not Match!", "Paswords Do Not Match", JOptionPane.ERROR_MESSAGE);
				newUserRepeat(name, pass);
				return;
			}
		}
	}
	
	public static void newUserRepeat(String prename, String prepassword) {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(400, 75));
		panel.add(new JLabel("Enter Name:"));
		JTextField namei = new JTextField(prename, 20);
		namei.requestFocus();
		panel.add(namei);
		panel.add(new JLabel("Enter Password:"));
		JPasswordField passi = new JPasswordField(prepassword, 20);
		panel.add(passi);
		panel.add(new JLabel("Enter Password Again:"));
		JPasswordField passChecki = new JPasswordField(20);
		panel.add(passChecki);
		String[] options = new String[] { "Create", "Cancel" };
		int option = JOptionPane.showOptionDialog(null, panel, "Create New User", JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);
		if (option == 0) {
			String name = namei.getText();
			char[] passc = passi.getPassword();
			String pass = new String(passc);
			char[] passCheckc = passChecki.getPassword();
			String passCheck = new String(passCheckc);
			for (User u : UserManager.getUsers()) {
				if (u.getName().equalsIgnoreCase(name)) {
					JOptionPane.showMessageDialog(null, "Name Already Taken!", "Name Already Taken", JOptionPane.ERROR_MESSAGE);
					newUserRepeat(name, pass);
					return;
				}
			}
			if (pass.equals(passCheck)) {
				User user = new User(name, pass);
				UserManager.addUser(user);
			} else {
				JOptionPane.showMessageDialog(null, "Passwords Do Not Match!", "Passwords Do Not Match", JOptionPane.ERROR_MESSAGE);
				newUserRepeat(name, pass);
				return;
			}
		}
	}
	
	public static void action(ActionType id, ActionEvent e) {
		switch (id) {
		case LOGIN:
			if (TimeLogger.outList.getSelectedValue() != null) {
				User selected = TimeLogger.outList.getSelectedValue();
				getPassword(true, selected);
			}
			break;
		case LOGOUT:
			if (TimeLogger.inList.getSelectedValue() != null) {
				User selected = TimeLogger.inList.getSelectedValue();
				getPassword(false, selected);
			}
			break;
		case NEWUSER:
			newUser();
			break;
		case EXPORT:
			//Export spreadsheet code
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
					getPassword(true, u);
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
					getPassword(false, u);
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
