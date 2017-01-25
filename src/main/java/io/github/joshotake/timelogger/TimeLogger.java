package io.github.joshotake.timelogger;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class TimeLogger {
	
	static JFrame jf;
	static Dimension listSize = new Dimension(150, 300);
	static Dimension listFrame = new Dimension(150, 330);
	static Dimension button = new Dimension(150, 190);
	static Dimension fullSize = new Dimension(550, 500);
	static ImageIcon icon;
	static JList<User> outList;
	static JList<User> inList;
	
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
			public void windowActivated(WindowEvent arg0) {}
			public void windowClosed(WindowEvent arg0) {}
			public void windowDeactivated(WindowEvent arg0) {}
			public void windowDeiconified(WindowEvent arg0) {}
			public void windowIconified(WindowEvent arg0) {}
			public void windowOpened(WindowEvent arg0) {}
		});
		jf.add(addOutList());
		jf.add(addButtons());
		jf.add(addInList());
		jf.setIconImage(icon.getImage());
		jf.setVisible(true);
	}
	
	public static long getDifference(long first, long second) {
		return Math.abs(second - first);
	}
	
	public static long getCurrentDifference(long difference) {
		return Math.abs(System.currentTimeMillis() - difference);
	}
	
	private static JPanel addOutList() {
		JPanel panel = new JPanel();
		FlowLayout layout = new FlowLayout();
		panel.setLayout(layout);
		panel.setPreferredSize(listFrame);
		DefaultListModel<User> listModel = new DefaultListModel<User>();
		outList = new JList<User>(listModel);
		outList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		outList.setLayoutOrientation(JList.VERTICAL);
		outList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				Listeners.list(0, e);
			}
		});
		for (User u : UserManager.getOutUsers()) {
			listModel.addElement(u);
		}
		JScrollPane scroll = new JScrollPane(outList);
		scroll.setPreferredSize(listSize);
		JLabel label = new JLabel("Logged Out Members", SwingConstants.LEFT);
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
				Listeners.action(0, e);
			}
		});
		JButton logout = new JButton("Log Out");
		logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Listeners.action(1, e);
			}
		});
		panel.add(login);
		panel.add(logout);
		return panel;
	}
	
	private static JPanel addInList() {
		JPanel panel = new JPanel();
		FlowLayout layout = new FlowLayout();
		panel.setLayout(layout);
		panel.setPreferredSize(listFrame);
		DefaultListModel<User> listModel = new DefaultListModel<User>();
		inList = new JList<User>(listModel);
		inList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		inList.setLayoutOrientation(JList.VERTICAL);
		inList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				Listeners.list(1, e);
			}
		});
		for (User u : UserManager.getInUsers()) {
			listModel.addElement(u);
		}
		JScrollPane scroll = new JScrollPane(inList);
		scroll.setPreferredSize(listSize);
		JLabel label = new JLabel("Logged In Members", SwingConstants.LEFT);
		panel.add(label);
		panel.add(scroll);
		return panel;
	}
	
	public static void close() {
		//Closing Code
	}

}
