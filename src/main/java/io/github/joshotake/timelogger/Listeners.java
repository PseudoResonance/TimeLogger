package io.github.joshotake.timelogger;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

public class Listeners {

	public static void list(int id, ListSelectionEvent e) {
		ListSelectionModel lsm = (ListSelectionModel) e.getSource();
		int first = e.getFirstIndex();
		int last = e.getLastIndex();
		boolean adjusting = e.getValueIsAdjusting();
		if (lsm.isSelectionEmpty()) {
			//TODO Remove time code
		} else {
			int min = lsm.getMinSelectionIndex();
			int max = lsm.getMaxSelectionIndex();
			for (int i = min; i <= max; i++) {
				if (lsm.isSelectedIndex(i)) {
					//TODO Show time code
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
	
	public static void action(int id, ActionEvent e) {
		switch (id) {
		case 0:
			if (TimeLogger.outList.getSelectedValue() != null) {
				User selected = TimeLogger.outList.getSelectedValue();
				//TODO Log in code
			}
			break;
		case 1:
			if (TimeLogger.inList.getSelectedValue() != null) {
				User selected = TimeLogger.inList.getSelectedValue();
				//TODO Log out code
			}
			break;
		default:
			break;
		}
	}
	
}
