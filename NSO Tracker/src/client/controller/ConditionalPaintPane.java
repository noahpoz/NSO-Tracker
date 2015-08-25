package client.controller;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

public class ConditionalPaintPane extends JScrollPane {
	
	PaintFunction _paintFunction;
	
	public void setPaintFunction(PaintFunction p) {
		_paintFunction = p;
	}
	
	@Override public void repaint() {
//		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
//		System.out.println("***** Repaint *****");
//		for (int i = 0; i < stackTraceElements.length; i++) {
//			System.out.println(stackTraceElements[i].getClassName() + " index: " + i);
//		}
//		System.out.println("*****");
		super.repaint();
	}
	
	@Override public void paint(Graphics g) {
//		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
//		System.out.println("***** Paint *****");
//		for (int i = 0; i < stackTraceElements.length; i++) {
//			System.out.println(stackTraceElements[i].getClassName() + " index: " + i);
//		}
//		System.out.println("*****");
		super.paint(g);
	}

}
