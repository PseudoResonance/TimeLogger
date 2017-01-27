package io.github.joshotake.timelogger;

import java.awt.Dimension;
import java.awt.FlowLayout;
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
	static Dimension listSize = new Dimension(150, 300);
	static Dimension listFrame = new Dimension(150, 330);
	static Dimension button = new Dimension(150, 190);
	static Dimension fullSize = new Dimension(550, 500);
	static ImageIcon icon;
	static JList<User> outList;
	static JList<User> inList;
	static DefaultListModel<User> outListModel;
	static DefaultListModel<User> inListModel;
	
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
		return panel;
	}

	private static JPanel addButtons() {
		JPanel panel = new JPanel();
		FlowLayout layout = new FlowLayout();
		panel.setLayout(layout);
		panel.setPreferredSize(listFrame);
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
		JButton spreadSheet = new JButton("Export Spreadsheet");
		spreadSheet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Listeners.action(ActionType.EXPORT, e);
			}
		});
		panel.add(login);
		panel.add(logout);
		panel.add(newUser);
		panel.add(spreadSheet);
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
		return panel;
	}
	
	public static JTextArea setupTime() {
		JTextArea text = new JTextArea(1, 20);
		text.setText("");
		text.setToolTipText("Double click on a user to view their time and meetings attended");
		return text;
	}
	
	public static JFrame setupPasswordCheck() {
		JFrame check = new JFrame();
		check.setLayout(new FlowLayout());
		check.setPreferredSize(new Dimension(350, 150));
		JPanel passInput2 = new JPanel();
		this.passwordInput2 = new JPasswordField(20);
		JLabel pass2 = new JLabel("Password:");
		passInput2.add(pass2);
		passInput2.add(this.passwordInput2);
		check.add(passInput2);
		check.add(cancel2);
		check.add(verify);
		check.pack();
		check.setVisible(false);
		check.getRootPane().setDefaultButton(verify);
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
