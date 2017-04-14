package client.view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.*;
import javax.swing.plaf.*;

import client.model.MainModel;
import client.model.UpdateException;

public class MainView implements Runnable {

	MainWindow _mainWindow;
	
	MainModel _model;
	
	public MainView() {
		
		//set some system-wide UI defaults so that everything appears correctly
		UIManager.put("TextField.inactiveForeground", new ColorUIResource(Color.BLACK));
		UIManager.put("TextArea.inactiveForeground", new ColorUIResource(Color.BLACK));
		UIManager.put("ComboBox.disabledForeground", new ColorUIResource(Color.BLACK));
		
		_model = new MainModel(this); //initialize the under-the-hood functionality of the client
		
	}
	
	@Override 
	public void run() {
		//set up UI elements here so that swing functions are thread-safe
		//Remember, the model has not necessarily been initialized yet. Model.updateAll() notifies all observers
		//which in turn completes initial GUI setup
		_mainWindow = new MainWindow(_model);
		//View is allowed one method call to notify observers. After that, it's up to the model.
		try {
			_model.ready();
		} catch (UpdateException e) {
			System.err.println("Error: View has exceeded notification permissions. Model must be responsible for updates.");
			e.printStackTrace();
		}
	}
	
	public void saveSuccessful(boolean b) {
		_mainWindow.saveSuccessful(b);
	}
	
	public void eventSelected(String eventName) {
		_mainWindow.eventSelected(eventName);
	}
	
	/** sizes and places a JComponent so that this does not have to be done repeatedly **/
	public static JComponent formatJComponent(JComponent component, int width, int height, int x, int y) {
		component.setSize(width, height);
		component.setLocation(x, y);
		return component;
	}	
	
	/** returns a string of spaces with a length equal to the input integer **/
	public static String spacePadding(int num) {
		String s = "";
		for (int i = 0; i < num; i++) {
			s = s + " ";
		}
		return s;
	}
	
	public static Font sansSerif(int size) {
		return new Font("Sans Serif", Font.PLAIN, size);
	}

}
