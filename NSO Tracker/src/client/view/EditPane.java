package client.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JWindow;

import client.controller.GhostTextField;
import client.model.MainModel;

@SuppressWarnings("serial")
public class EditPane extends JLayeredPane {

	private static final int WIDTH = 513;
	private static final int HEIGHT = 287;
	private static final int TRIANGLE_HALF = 10;
	private static final int TRIANGLE_HEIGHT = 15;

	public static final int BASIC_INFO = 1;
	public static final int FOLLOW_UP = 2;

	private static final int LABEL_HEIGHT = 12;
	private static final int SUBLABEL_PADDING = 3;

	private static final int FIELD_WIDTH = 220;
	private static final int FIELD_HEIGHT = 40;
	private static final int PADDING = 10;
	
	private static final int BUTTON_WIDTH = 110;
	private static final int BUTTON_HEIGHT = 45;

	MainModel _model;
	int _offset;
	ArrayList<JComponent> _fields;

	public EditPane(int pointerOffset, int x, int y, int content, MainModel model, int indexInList) {

		_model = model;
		setOpaque(false);
		
		_fields = new ArrayList<JComponent>();
		_offset = pointerOffset;

		setLayout(null);
		setSize(WIDTH, HEIGHT);
		setLocation(x, y);

		if (content == BASIC_INFO) {
			//set up the swing components of the window
			JLabel row1 = new JLabel(" Name:" + MainView.spacePadding(47) + "Date Gathered:");
			add(MainView.formatJComponent(row1, WIDTH, LABEL_HEIGHT, TRIANGLE_HEIGHT + PADDING, PADDING));

			GhostTextField nameField = new GhostTextField("Name...");
			add(MainView.formatJComponent(nameField, FIELD_WIDTH, FIELD_HEIGHT,
					TRIANGLE_HEIGHT + PADDING, PADDING + LABEL_HEIGHT + SUBLABEL_PADDING));
			_fields.add(nameField);
			
			setUpDatePicker(TRIANGLE_HEIGHT + PADDING * 2 + FIELD_WIDTH, PADDING, this);
			
			JLabel row2 = new JLabel(" Email Address:" + MainView.spacePadding(34) + "Phone Number:");
			add(MainView.formatJComponent(row2, WIDTH, LABEL_HEIGHT, TRIANGLE_HEIGHT + PADDING, 
					PADDING * 2 + LABEL_HEIGHT + SUBLABEL_PADDING + FIELD_HEIGHT));
			
			GhostTextField emailField = new GhostTextField("Email...");
			add(MainView.formatJComponent(emailField, FIELD_WIDTH, FIELD_HEIGHT, 
					TRIANGLE_HEIGHT + PADDING, PADDING * 2 + LABEL_HEIGHT * 2 + SUBLABEL_PADDING * 2 + FIELD_HEIGHT));
			_fields.add(emailField);
			
			GhostTextField phoneField = new GhostTextField("Phone...");
			add(MainView.formatJComponent(phoneField, FIELD_WIDTH, FIELD_HEIGHT,
					TRIANGLE_HEIGHT + PADDING * 2 + FIELD_WIDTH,
					PADDING * 2 + LABEL_HEIGHT * 2 + SUBLABEL_PADDING * 2 + FIELD_HEIGHT));
			_fields.add(phoneField);
			
			JLabel row3 = new JLabel(" Additional Info:" + MainView.spacePadding(33) + "Info Gathered By:");
			add(MainView.formatJComponent(row3, WIDTH, LABEL_HEIGHT,
					TRIANGLE_HEIGHT + PADDING,  PADDING * 3 + LABEL_HEIGHT * 2 + SUBLABEL_PADDING * 2 + FIELD_HEIGHT * 2));
			
			JTextArea textArea = new JTextArea();
			textArea.setFont(new Font("Sans Serif", Font.PLAIN, 10));
			textArea.setWrapStyleWord(true);
			textArea.setLineWrap(true);
			JScrollPane additionalInfo = new JScrollPane(MainView.formatJComponent(textArea, FIELD_WIDTH, 1000, 0, 0));
			add(MainView.formatJComponent(additionalInfo, FIELD_WIDTH, FIELD_HEIGHT * 3,
					TRIANGLE_HEIGHT + PADDING,
					PADDING * 3 + LABEL_HEIGHT * 3 + SUBLABEL_PADDING * 3 + FIELD_HEIGHT * 2));
			_fields.add(textArea);
			
			GhostTextField gatheredBy = new GhostTextField("Gathered By...");
			add(MainView.formatJComponent(gatheredBy, FIELD_WIDTH, FIELD_HEIGHT,
					TRIANGLE_HEIGHT + PADDING * 2 + FIELD_WIDTH,
					PADDING * 3 + LABEL_HEIGHT * 3 + SUBLABEL_PADDING * 3 + FIELD_HEIGHT * 2));
			_fields.add(gatheredBy);
			
			JButton editButton = new JButton("Edit");
			add(MainView.formatJComponent(editButton, BUTTON_WIDTH, BUTTON_HEIGHT, 
					TRIANGLE_HEIGHT + PADDING * 2 + FIELD_WIDTH, 
					PADDING * 5 + LABEL_HEIGHT * 3 + SUBLABEL_PADDING * 3 + FIELD_HEIGHT * 3));
			editButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					fieldsAreEnabled(true);
				}
			});
			
			JButton closeButton = new JButton("Close");
			add(MainView.formatJComponent(closeButton, BUTTON_WIDTH, BUTTON_HEIGHT, 
					TRIANGLE_HEIGHT + PADDING * 2 + FIELD_WIDTH + BUTTON_WIDTH + 10, 
					PADDING * 5 + LABEL_HEIGHT * 3 + SUBLABEL_PADDING * 3 + FIELD_HEIGHT * 3));
			closeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					_model.saveBasicInfo(fieldContents(null));
				}
			});
			
			//now put in the info from the model
			fieldContents(_model.getCurrentEventPackets().get(indexInList).getBasicInfo());
			
		} else if (content == FOLLOW_UP) {
			//follow up window code
		}
		
		
		fieldsAreEnabled(false);
	}
	
	public void fieldsAreEnabled(boolean b) {
		for (JComponent c : _fields) {
			c.setEnabled(b);
		}
	}
	
	public String[] fieldContents(String[] info) {
		String[] contents = new String[_fields.size()];
		int index = 0;
		
		if (info != null) {
			if (info.length != contents.length) {
				System.err.println("Error: Invalid field contents input!");
				return null;
			}
		}
		
		for (JComponent c : _fields) {
			String className = c.getClass().getName();
			if (className.equals("client.controller.GhostTextField")) {
				GhostTextField field = (GhostTextField) c;
				if (info != null) field.setContents(info[index]);
				contents[index] = field.getContents();
			}
			
			if (className.equals("javax.swing.JTextArea")) {
				JTextArea field = (JTextArea) c;
				if (info != null) field.setText(info[index]);
				contents[index] = field.getText();
			}
			
			if (className.equals("javax.swing.JComboBox")) {
				JComboBox box = (JComboBox) c;
				Integer i = box.getSelectedIndex();
				if (info != null) box.setSelectedIndex(Integer.parseInt(info[index]));
				contents[index] = i.toString();
			}
			index++;
		}
		return contents;
	}
	
	public void setUpDatePicker(int x, int y, EditPane contentPane) {
		y += 10;
		String[] months = {"January", "February", "March", "April", "May", "June", "July",
				"August", "September", "October", "November", "December"};
		JComboBox monthPicker = new JComboBox<String>(months);
		contentPane.add(MainView.formatJComponent(monthPicker, 100, FIELD_HEIGHT, x, y));
		_fields.add(monthPicker);

		String[] dates = new String[31];
		for (Integer i = 1; i < 32; i++) {
			dates[i - 1] = i.toString();
		}
		JComboBox<String> datePicker = new JComboBox<String>(dates);
		contentPane.add(MainView.formatJComponent(datePicker, 70, FIELD_HEIGHT, x + monthPicker.getWidth() - 5, y));
		_fields.add(datePicker);

		String[] years = {"2015", "2016"};
		JComboBox yearPicker = new JComboBox<String>(years);
		contentPane.add(MainView.formatJComponent(yearPicker, 90, FIELD_HEIGHT, 
				x + datePicker.getWidth() + monthPicker.getWidth() - 10, y));
		_fields.add(yearPicker);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		RenderingHints hints = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHints(hints);
		g2d.setColor(getBackground());
		g2d.fill(getWindowShape(_offset));

		AffineTransform t = new AffineTransform();
		t.translate(0, 0);
		t.scale(0.999f, 0.9999f);
		Shape s = t.createTransformedShape(getWindowShape(_offset));

		g2d.setColor(Color.black);
		g2d.draw(s);
		g2d.dispose();
	}

	public GeneralPath getWindowShape(int offset) {

		if (offset < TRIANGLE_HALF) offset = 10;
		if (offset > HEIGHT - TRIANGLE_HALF) offset = HEIGHT - TRIANGLE_HALF;

		int x2Points[] = {TRIANGLE_HEIGHT, WIDTH, WIDTH, TRIANGLE_HEIGHT, TRIANGLE_HEIGHT, 0, TRIANGLE_HEIGHT, TRIANGLE_HEIGHT};
		int y2Points[] = {0, 0, HEIGHT, HEIGHT, offset + TRIANGLE_HALF, offset, offset - TRIANGLE_HALF, 0};
		GeneralPath polyline =  new GeneralPath(GeneralPath.WIND_EVEN_ODD, x2Points.length);

		polyline.moveTo(x2Points[0], y2Points[0]);
		for (int index = 1; index < x2Points.length; index++) {
			polyline.lineTo(x2Points[index], y2Points[index]);
		}

		return polyline;
	}
}
