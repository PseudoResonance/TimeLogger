package io.github.joshotake.timelogger;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

public class TimeLogger {
	
	static JFrame jf;
	static JPanel jp;
	static WindowEventListener wel;
	
	public static void main(String[] args) {
		jf = new JFrame("Time Logger");
		wel = new WindowEventListener();
		jf.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		jf.addWindowListener(wel);
		panelInit();
		addComponents();
		jf.setVisible(true);
	}
	
	public static long getDifference(long first, long second) {
		return Math.abs(second - first);
	}
	
	public static long getCurrentDifference(long difference) {
		return Math.abs(System.currentTimeMillis() - difference);
	}
	
	private static void panelInit() {
		jp = new JPanel(new BorderLayout());
		jf.add(jp);
	}
	
	private static void addComponents() {
		jp.setLayout(null);
		JList<String> out = new JList<String>();
		out.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		out.setModel();
	}
	
	public static void close() {
		//Closing Code
	}

}
