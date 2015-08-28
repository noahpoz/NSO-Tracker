package client.view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class NameListTableHeader extends JTable {

	public NameListTableHeader() {
		setModel(new DefaultTableModel(new String[][] {{}}, 
				new String[] {MainView.spacePadding(14) + "Name", 
					MainView.spacePadding(4) + "Number of Follow Ups", 
					MainView.spacePadding(7) + "Info Gathered By"}));
		
		int thickness = 1;
		getTableHeader().setPreferredSize(
				new Dimension(MainWindow.HEADER_WIDTH - thickness - 1, MainWindow.MENU_HEIGHT - thickness - 4));
		getTableHeader().setFont(MainView.sansSerif(12));
		getTableHeader().setBorder(BorderFactory.
				createMatteBorder(thickness, thickness, thickness, thickness, Color.black));
		
		getTableHeader().setResizingAllowed(false);
		getTableHeader().setReorderingAllowed(false);
		
	}
}
