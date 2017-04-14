package client.controller;

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
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JWindow;

import client.model.MainModel;
import client.view.MainView;

@SuppressWarnings("serial")
public class EditPane extends JLayeredPane {

	private static final int MENU_HEIGHT = 30;

	private static final int WIDTH = 462;
	private static final int HEIGHT = 287;
	
	public static final int TRIANGLE_HALF = 10;
	public static final int TRIANGLE_HEIGHT = 18;

	private static final int LABEL_HEIGHT = 12;
	private static final int SUBLABEL_PADDING = 3;

	private static final int FIELD_WIDTH = 180;
	private static final int FIELD_HEIGHT = 40;
	private static final int PADDING = 5;

	private static final int BUTTON_WIDTH = 110;
	private static final int BUTTON_HEIGHT = 45;

	int _offset;
	ArrayList<JComponent> _fields;
	MainModel _model;
	
	//basic info fields
	GhostTextField _nameField;
	ArrayList<JComboBox> _basicDatePicker;
	GhostTextField _gatheredByField;
	GhostTextField _emailField;
	GhostTextField _phoneField;
	JTextArea _additionalInfoField;
	
	//follow up fields

	public EditPane(int pointerOffset, int x, int y, MainModel model) {

		setOpaque(false);

		_model = model;
		_fields = new ArrayList<JComponent>();
		_offset = pointerOffset;

		setLayout(null);
		setSize(WIDTH, HEIGHT);
		setLocation(x, y);

		JPanel basicPanel = new JPanel();
		basicPanel.setLayout(null);
		basicPanel.setSize(getWidth() - 20, getHeight() - 3 - MENU_HEIGHT);
		basicPanel.setLocation(TRIANGLE_HEIGHT, 2 + MENU_HEIGHT);
		add(basicPanel);
		
		JPanel followUpPanel = new JPanel();

		ActionListener radioButtonListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("basic")) {
					basicPanel.setVisible(true);
					followUpPanel.setVisible(false);
				} else if (e.getActionCommand().equals("followup")) {
					basicPanel.setVisible(false);
					followUpPanel.setVisible(true);
				}
			}
		};

		JRadioButton basicInfo = new JRadioButton("Basic Info");
		basicInfo.setActionCommand("basic");
		basicInfo.addActionListener(radioButtonListener);
		add(MainView.formatJComponent(basicInfo, 100, 30, TRIANGLE_HEIGHT + PADDING, PADDING));

		JRadioButton followUp = new JRadioButton("Follow Up");
		followUp.setActionCommand("followup");
		followUp.addActionListener(radioButtonListener);
		add(MainView.formatJComponent(followUp, 100, 30, TRIANGLE_HEIGHT + PADDING * 2 + 100, PADDING));

		ButtonGroup group = new ButtonGroup();
		group.add(basicInfo);
		group.add(followUp);
		basicInfo.setSelected(true);


		//SET UP BASIC INFO PANEL
		_basicDatePicker = new ArrayList<JComboBox>();
		JLabel row1 = new JLabel(" Name:" + MainView.spacePadding(47) + "Date Gathered:");
		basicPanel.add(MainView.formatJComponent(row1, WIDTH, LABEL_HEIGHT, PADDING, PADDING));

		_nameField = new GhostTextField("Name...");
		basicPanel.add(MainView.formatJComponent(_nameField, FIELD_WIDTH, FIELD_HEIGHT,
				PADDING, PADDING + LABEL_HEIGHT + SUBLABEL_PADDING));
		_fields.add(_nameField);

		setUpDatePicker(PADDING * 2 + FIELD_WIDTH, PADDING, basicPanel, _basicDatePicker);

		JLabel row2 = new JLabel(" Email Address:" + MainView.spacePadding(34) + "Phone Number:");
		basicPanel.add(MainView.formatJComponent(row2, WIDTH, LABEL_HEIGHT, PADDING, 
				PADDING * 2 + LABEL_HEIGHT + SUBLABEL_PADDING + FIELD_HEIGHT));

		_emailField = new GhostTextField("Email...");
		basicPanel.add(MainView.formatJComponent(_emailField, FIELD_WIDTH, FIELD_HEIGHT, PADDING, 
				PADDING * 2 + LABEL_HEIGHT * 2 + SUBLABEL_PADDING * 2 + FIELD_HEIGHT));
		_fields.add(_emailField);

		_phoneField = new GhostTextField("Phone...");
		basicPanel.add(MainView.formatJComponent(_phoneField, FIELD_WIDTH, FIELD_HEIGHT,
				PADDING * 2 + FIELD_WIDTH,
				PADDING * 2 + LABEL_HEIGHT * 2 + SUBLABEL_PADDING * 2 + FIELD_HEIGHT));
		_fields.add(_phoneField);

		JLabel row3 = new JLabel(" Additional Info:" + MainView.spacePadding(33) + "Info Gathered By:");
		basicPanel.add(MainView.formatJComponent(row3, WIDTH, LABEL_HEIGHT, PADDING,  
				PADDING * 3 + LABEL_HEIGHT * 2 + SUBLABEL_PADDING * 2 + FIELD_HEIGHT * 2));

		int textAreaY = PADDING * 3 + LABEL_HEIGHT * 3 + SUBLABEL_PADDING * 3 + FIELD_HEIGHT * 2;
		_additionalInfoField = new JTextArea();
		_additionalInfoField.setFont(new Font("Sans Serif", Font.PLAIN, 10));
		_additionalInfoField.setWrapStyleWord(true);
		_additionalInfoField.setLineWrap(true);
		JScrollPane additionalInfo = new JScrollPane(MainView.formatJComponent(_additionalInfoField, FIELD_WIDTH, 1000, 0, 0));
		basicPanel.add(MainView.formatJComponent(additionalInfo, FIELD_WIDTH, 
				HEIGHT - textAreaY - PADDING - MENU_HEIGHT, PADDING,
				textAreaY));
		_fields.add(_additionalInfoField);

		_gatheredByField = new GhostTextField("Gathered By...");
		basicPanel.add(MainView.formatJComponent(_gatheredByField, FIELD_WIDTH, FIELD_HEIGHT,
				PADDING * 2 + FIELD_WIDTH,
				PADDING * 3 + LABEL_HEIGHT * 3 + SUBLABEL_PADDING * 3 + FIELD_HEIGHT * 2));
		_fields.add(_gatheredByField);

		JButton editButton = new JButton("Edit");
		basicPanel.add(MainView.formatJComponent(editButton, BUTTON_WIDTH, BUTTON_HEIGHT, 
				PADDING * 4 + FIELD_WIDTH, 
				PADDING * 5 + LABEL_HEIGHT * 3 + SUBLABEL_PADDING * 3 + FIELD_HEIGHT * 3));
		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fieldsAreEnabled(true);
			}
		});

		JButton closeButton = new JButton("Close");
		basicPanel.add(MainView.formatJComponent(closeButton, BUTTON_WIDTH, BUTTON_HEIGHT, 
				PADDING * 4 + FIELD_WIDTH + BUTTON_WIDTH + 10, 
				PADDING * 5 + LABEL_HEIGHT * 3 + SUBLABEL_PADDING * 3 + FIELD_HEIGHT * 3));
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_model.saveProfile(null);
			}
		});
		fieldsAreEnabled(false);
	}

	public void fieldsAreEnabled(boolean b) {
		for (JComponent c : _fields) {
			c.setEnabled(b);
		}
	}

	public void setUpDatePicker(int x, int y, JPanel contentPane, ArrayList<JComboBox> picker) {
		y += 10;
		String[] months = {"January", "February", "March", "April", "May", "June", "July",
				"August", "September", "October", "November", "December"};
		JComboBox monthPicker = new JComboBox<String>(months);
		contentPane.add(MainView.formatJComponent(monthPicker, 100, FIELD_HEIGHT, x, y));
		_fields.add(monthPicker);
		picker.add(monthPicker);

		String[] dates = new String[31];
		for (Integer i = 1; i < 32; i++) {
			dates[i - 1] = i.toString();
		}
		JComboBox<String> datePicker = new JComboBox<String>(dates);
		contentPane.add(MainView.formatJComponent(datePicker, 70, FIELD_HEIGHT, x + monthPicker.getWidth() - 5, y));
		_fields.add(datePicker);
		picker.add(datePicker);

		String[] years = {"2015", "2016"};
		JComboBox yearPicker = new JComboBox<String>(years);
		contentPane.add(MainView.formatJComponent(yearPicker, 90, FIELD_HEIGHT, 
				x + datePicker.getWidth() + monthPicker.getWidth() - 10, y));
		_fields.add(yearPicker);
		picker.add(yearPicker);
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

		offset += TRIANGLE_HALF;
		if (offset < TRIANGLE_HALF) offset = TRIANGLE_HALF;
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
