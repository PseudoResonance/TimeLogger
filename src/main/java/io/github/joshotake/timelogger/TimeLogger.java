package io.github.joshotake.timelogger;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import io.github.joshotake.timelogger.Listeners.ActionType;

public class TimeLogger {

	static JFrame jf;
	static Dimension listSize = new Dimension(200, 460);
	static Dimension listFrame = new Dimension(200, 490);
	static Dimension buttonsFrame = new Dimension(186, 490);
	static Dimension fullSize = new Dimension(650, 600);
	static ImageIcon icon;
	static JList<User> outList;
	static JList<User> inList;
	static DefaultListModel<User> outListModel;
	static DefaultListModel<User> inListModel;
	static Font font;

	static JFrame passwordCheck;
	static JPasswordField password;
	static JButton passCancel;
	static JButton passOkay;

	static JFrame newUser;
	static JTextField name;
	static JPasswordField newPassword;
	static JPasswordField newPasswordCheck;
	static JButton newCancel;
	static JButton newOkay;

	static JFrame editUser;
	static JPasswordField oldPassword;
	static JPasswordField editPassword;
	static JPasswordField editPasswordCheck;
	static JButton editCancel;
	static JButton editOkay;
	
	private static JTextArea info;

	public static void main(String[] args) {
		URL url = TimeLogger.class.getResource("/icon.png");
		icon = new ImageIcon(url);
		FlowLayout layout = new FlowLayout();
		jf = new JFrame("Time Logger");
		jf.setLayout(layout);
		jf.setSize(fullSize);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int xs = (int) screenSize.getWidth() / 2;
		int ys = (int) screenSize.getHeight() / 2;
		int bx = (int) fullSize.getWidth() / 2;
		int by = (int) fullSize.getHeight() / 2;
		int x = xs - bx;
		int y = ys - by;
		jf.setLocation(x, y);
		jf.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		jf.addWindowListener(new WindowListener() {
			public void windowClosing(WindowEvent e) {
				Listeners.close(e);
			}
			public void windowActivated(WindowEvent e) {}
			public void windowClosed(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowOpened(WindowEvent e) {}
		});
		UserManager.deserializeUsers();
		jf.add(addOutList());
		jf.add(addButtons());
		jf.add(addInList());
		info = setupTime();
		jf.add(info);
		jf.setIconImage(icon.getImage());
		jf.setVisible(true);
		passwordCheck = setupPasswordCheck();
		newUser = setupNewUser();
		editUser = setupEditUser();
		jf.setFont(font);
		Listeners.setupDialog();
	}

	public static long getDifference(long first, long second) {
		return Math.abs(second - first);
	}

	public static long getCurrentDifference(long difference) {
		return Math.abs(System.nanoTime() - difference);
	}

	private static JPanel addOutList() {
		JPanel panel = new JPanel();
		FlowLayout layout = new FlowLayout();
		panel.setLayout(layout);
		panel.setPreferredSize(listFrame);
		outListModel = new DefaultListModel<User>();
		outList = new JList<User>(outListModel);
		outList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		outList.setLayoutOrientation(JList.VERTICAL);
		outList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				Listeners.list(0, e);
			}
		});
		outList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Listeners.mouseClick(0, e);
			}
		});
		for (User u : UserManager.getOutUsers()) {
			outListModel.addElement(u);
		}
		JScrollPane scroll = new JScrollPane(outList);
		scroll.setPreferredSize(listSize);
		JLabel label = new JLabel("Logged Out Members", SwingConstants.LEFT);
		label.setVerticalAlignment(SwingConstants.BOTTOM);
		panel.add(label);
		panel.add(scroll);
		Font f = label.getFont();
		font = new Font(f.getFamily(), f.getStyle(), f.getSize() + 4);
		label.setFont(font);
		outList.setFont(font);
		return panel;
	}

	private static JPanel addButtons() {
		JPanel panel = new JPanel();
		FlowLayout layout = new WrapLayout();
		panel.setLayout(layout);
		panel.setPreferredSize(buttonsFrame);
		JButton login = new JButton("Log In");
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Listeners.action(ActionType.LOGIN, e);
			}
		});
		JButton logout = new JButton("Log Out");
		logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Listeners.action(ActionType.LOGOUT, e);
			}
		});
		JButton newUser = new JButton("New User");
		newUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Listeners.action(ActionType.NEWUSER, e);
			}
		});
		JButton editUser = new JButton("Change Password");
		editUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Listeners.action(ActionType.EDITUSER, e);
			}
		});
		JButton spreadSheet = new JButton("Export Spreadsheet");
		spreadSheet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Listeners.action(ActionType.EXPORT, e);
			}
		});
		passCancel = new JButton("Cancel");
		passCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				password.setText("");
				passwordCheck.setVisible(false);
			}
		});
		passOkay = new JButton("Okay");
		passOkay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Listeners.checkPassword();
			}
		});
		newCancel = new JButton("Cancel");
		newCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				name.setText("");
				newPassword.setText("");
				newPasswordCheck.setText("");
				TimeLogger.newUser.setVisible(false);
			}
		});
		newOkay = new JButton("Okay");
		newOkay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Listeners.newUser();
			}
		});
		editCancel = new JButton("Cancel");
		editCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				oldPassword.setText("");
				editPassword.setText("");
				editPasswordCheck.setText("");
				TimeLogger.editUser.setVisible(false);
			}
		});
		editOkay = new JButton("Okay");
		editOkay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Listeners.editUser();
			}
		});
		panel.add(login);
		panel.add(logout);
		panel.add(newUser);
		panel.add(editUser);
		panel.add(spreadSheet);
		login.setFont(font);
		logout.setFont(font);
		newUser.setFont(font);
		editUser.setFont(font);
		spreadSheet.setFont(font);
		return panel;
	}

	private static JPanel addInList() {
		JPanel panel = new JPanel();
		FlowLayout layout = new FlowLayout();
		panel.setLayout(layout);
		panel.setPreferredSize(listFrame);
		inListModel = new DefaultListModel<User>();
		inList = new JList<User>(inListModel);
		inList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		inList.setLayoutOrientation(JList.VERTICAL);
		inList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				Listeners.list(1, e);
			}
		});
		inList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Listeners.mouseClick(1, e);
			}
		});
		for (User u : UserManager.getInUsers()) {
			inListModel.addElement(u);
		}
		JScrollPane scroll = new JScrollPane(inList);
		scroll.setPreferredSize(listSize);
		JLabel label = new JLabel("Logged In Members", SwingConstants.LEFT);
		label.setVerticalAlignment(SwingConstants.BOTTOM);
		panel.add(label);
		panel.add(scroll);
		label.setFont(font);
		inList.setFont(font);
		return panel;
	}
	
	public static JTextArea setupTime() {
		JTextArea text = new JTextArea(1, 20);
		text.setText("");
		text.setToolTipText("Double click on a user to view their time and meetings attended");
		text.setBackground(jf.getBackground());
		text.setFont(font);
		return text;
	}
	
	public static JFrame setupEditUser() {
		JFrame frame = new JFrame("Change Password");
		frame.setLayout(new FlowLayout());
		frame.setPreferredSize(new Dimension(350, 275));
		JPanel panel = new JPanel();
		panel.setLayout(new WrapLayout());
		oldPassword = new JPasswordField(20);
		JLabel oldLabel = new JLabel("Enter Old Passwod:");
		editPassword = new JPasswordField(20);
		JLabel editPassLabel = new JLabel("Enter Password:");
		editPasswordCheck = new JPasswordField(20);
		JLabel editPassCheckLabel = new JLabel("Enter Password Again:");
		panel.add(oldLabel);
		panel.add(oldPassword);
		panel.add(editPassLabel);
		panel.add(editPassword);
		panel.add(editPassCheckLabel);
		panel.add(editPasswordCheck);
		frame.add(panel);
		frame.add(editCancel);
		frame.add(editOkay);
		frame.pack();
		frame.setVisible(false);
		frame.getRootPane().setDefaultButton(editOkay);
		oldLabel.setFont(font);
		oldPassword.setFont(font);
		editPassLabel.setFont(font);
		editPassword.setFont(font);
		editPasswordCheck.setFont(font);
		editPassCheckLabel.setFont(font);
		editCancel.setFont(font);
		editOkay.setFont(font);
		frame.pack();
		return frame;
	}
	
	public static void setEditUser() {
		oldPassword.setText("");
		editPassword.setText("");
		editPasswordCheck.setText("");
		editUser.setPreferredSize(new Dimension(350, 275));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int xs = (int) screenSize.getWidth() / 2;
		int ys = (int) screenSize.getHeight() / 2;
		int bx = (int) editUser.getWidth() / 2;
		int by = (int) editUser.getHeight() / 2;
		int x = xs - bx;
		int y = ys - by;
		editUser.setLocation(x, y);
		editUser.pack();
		editUser.setVisible(true);
		oldPassword.requestFocus();
	}
	
	public static JFrame setupPasswordCheck() {
		JFrame frame = new JFrame("Enter Password");
		frame.setLayout(new FlowLayout());
		frame.setPreferredSize(new Dimension(425, 140));
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		password = new JPasswordField(20);
		JLabel label = new JLabel("Password:");
		panel.add(label);
		panel.add(password);
		frame.add(panel);
		frame.add(passCancel);
		frame.add(passOkay);
		frame.pack();
		frame.setVisible(false);
		frame.getRootPane().setDefaultButton(passOkay);
		label.setFont(font);
		password.setFont(font);
		passCancel.setFont(font);
		passOkay.setFont(font);
		frame.pack();
		return frame;
	}
	
	public static void setPasswordCheck() {
		password.setText("");
		passwordCheck.setSize(new Dimension(425, 140));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int xs = (int) screenSize.getWidth() / 2;
		int ys = (int) screenSize.getHeight() / 2;
		int bx = (int) passwordCheck.getWidth() / 2;
		int by = (int) passwordCheck.getHeight() / 2;
		int x = xs - bx;
		int y = ys - by;
		passwordCheck.setLocation(x, y);
		passwordCheck.pack();
		passwordCheck.setVisible(true);
		password.requestFocus();
	}
	
	public static JFrame setupNewUser() {
		JFrame frame = new JFrame("Create New User");
		frame.setLayout(new FlowLayout());
		frame.setPreferredSize(new Dimension(350, 275));
		JPanel panel = new JPanel();
		panel.setLayout(new WrapLayout());
		JLabel nameLabel = new JLabel("Enter Name:");
		name = new JTextField(20);
		newPassword = new JPasswordField(20);
		JLabel passLabel = new JLabel("Enter Password:");
		newPasswordCheck = new JPasswordField(20);
		JLabel passCheckLabel = new JLabel("Enter Password Again:");
		panel.add(nameLabel);
		panel.add(name);
		panel.add(passLabel);
		panel.add(newPassword);
		panel.add(passCheckLabel);
		panel.add(newPasswordCheck);
		frame.add(panel);
		frame.add(newCancel);
		frame.add(newOkay);
		frame.pack();
		frame.setVisible(false);
		frame.getRootPane().setDefaultButton(newOkay);
		nameLabel.setFont(font);
		name.setFont(font);
		passLabel.setFont(font);
		newPassword.setFont(font);
		passCheckLabel.setFont(font);
		newPasswordCheck.setFont(font);
		newCancel.setFont(font);
		newOkay.setFont(font);
		frame.pack();
		return frame;
	}
	
	public static void setNewUser() {
		name.setText("");
		newPassword.setText("");
		newPasswordCheck.setText("");
		name.requestFocus();
		newUser.setSize(new Dimension(350, 275));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int xs = (int) screenSize.getWidth() / 2;
		int ys = (int) screenSize.getHeight() / 2;
		int bx = (int) newUser.getWidth() / 2;
		int by = (int) newUser.getHeight() / 2;
		int x = xs - bx;
		int y = ys - by;
		newUser.setLocation(x, y);
		newUser.pack();
		newUser.setVisible(true);
	}
	
	public static void setNewUser(String name, String pass) {
		TimeLogger.name.setText(name);
		newPassword.setText(pass);
		newPasswordCheck.setText("");
		TimeLogger.name.requestFocus();
		newUser.setPreferredSize(new Dimension(350, 275));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int xs = (int) screenSize.getWidth() / 2;
		int ys = (int) screenSize.getHeight() / 2;
		int bx = (int) newUser.getWidth() / 2;
		int by = (int) newUser.getHeight() / 2;
		int x = xs - bx;
		int y = ys - by;
		newUser.setLocation(x, y);
		newUser.pack();
		newUser.setVisible(true);
		TimeLogger.name.requestFocus();
	}
	
	public static void setTime(String t) {
		info.setText(t);
	}

	public static void close() {
		UserManager.serializeUsers();
	}

	public static void makeExcelSheet() {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("TimeLogger Output");
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("Name");
		cell = row.createCell(1);
		cell.setCellValue("Meetings Attended");
		cell = row.createCell(2);
		cell.setCellValue("Hours");
		cell = row.createCell(3);
		cell.setCellValue("Minutes");
		cell = row.createCell(4);
		cell.setCellValue("Seconds");

		List<User> users = UserManager.getUsers();
		for (int rowNum = 1; rowNum < users.size() + 1; rowNum++) {
			row = sheet.createRow(rowNum);
			cell = row.createCell(0);
			cell.setCellValue(((User) users.get(rowNum - 1)).getName());
			cell = row.createCell(1);
			cell.setCellValue(((User) users.get(rowNum - 1)).getTotalMeetings());
			cell = row.createCell(2);
			cell.setCellValue(((User) users.get(rowNum - 1)).getHours());
			cell = row.createCell(3);
			cell.setCellValue(((User) users.get(rowNum - 1)).getMinutes());
			cell = row.createCell(4);
			cell.setCellValue(((User) users.get(rowNum - 1)).getSeconds());
		}
		Calendar rightNow = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");

		try {
			FileOutputStream fileOut = new FileOutputStream(dateFormat.format(rightNow.getTime()) + "_" + "timesheet.xlt");
			wb.write(fileOut);
			fileOut.close();
			wb.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
