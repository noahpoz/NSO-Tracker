package client;

import javax.swing.SwingUtilities;
import client.view.MainView;

public class ClientDriver {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new MainView());
	}
}
