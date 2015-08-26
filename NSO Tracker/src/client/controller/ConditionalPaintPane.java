package client.controller;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

public class ConditionalPaintPane extends JScrollPane {
	
	PaintFunction _paintFunction;
	
	public void setPaintFunction(PaintFunction p) {
		_paintFunction = p;
	}
	
	@Override 
	public void paint(Graphics g) {
		super.paint(g);
	}
}
