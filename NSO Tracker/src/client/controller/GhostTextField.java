package client.controller;

import java.awt.Color;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

/** Displays default text in a JTextField when the field is empty **/
@SuppressWarnings("serial")
public class GhostTextField extends JTextField {
	
	private GhostTextField _self;
	private ChangeListener _listener;
	
	private String _emptyText;
	private String _contents;
	
	public GhostTextField(String emptyText) {
		super();
		_self = this;
		_contents = "";
		_emptyText = emptyText;
		ghost(true);
		setText(_emptyText);
		
		this.addFocusListener(new FocusListener() {
			@Override public void focusGained(FocusEvent e) {
				if (_contents.length() == 0) {
					_self.setCaretPosition(0);
				}
			}
			@Override public void focusLost(FocusEvent e) { }
		});
		
		this.addCaretListener(new CaretListener() {
			@Override public void caretUpdate(CaretEvent e) {
				if (_contents.length() == 0 && _self.getCaretPosition() > 0) {
					_self.setCaretPosition(0);
				}
			}
		});
		
		AbstractDocument doc = (AbstractDocument) this.getDocument();
		doc.setDocumentFilter(new SearchFieldFilter());
	}
	
	private void ghost(boolean b) {
		if (b) {
			_self.setForeground(Color.gray);
		} else {
			_self.setForeground(Color.black);
		}
	}
	
	public void addChangeListener(ChangeListener c) {
		_listener = c;
	}
	
	private void performAction() {
		if (_listener != null) {
			_listener.textChanged();
		}
	}
	
	public void setContents(String s) {
		_contents = s;
		setText(s);
	}
	
	public String getContents() {
		return _contents;
	}
	
	public String getText() {
		return _contents;
	}
	
	private class SearchFieldFilter extends DocumentFilter {
		
		public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
			fb.remove(offset, length);
			if (fb.getDocument().getLength() == 0) {
				fb.insertString(0, _emptyText, null);
				ghost(true);
				_self.setCaretPosition(0);
				_contents = "";
				_self.ghost(true);
			} else {
				_contents = fb.getDocument().getText(0, fb.getDocument().getLength());
			}
			_self.performAction();
		}
		
		public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
			if (_contents.length() == 0) {
				if (text.length() == 0) {
					fb.remove(0, fb.getDocument().getLength());
					fb.insertString(0, _emptyText, null);
					_self.setCaretPosition(0);
					_self.ghost(true);
				} else {
					_contents = text;
					fb.remove(0, fb.getDocument().getLength());
					fb.replace(0, length, text, attrs);
					_self.ghost(false);
				}

			} else {
				fb.replace(offset, length, text, attrs);
				_contents = fb.getDocument().getText(0, fb.getDocument().getLength());
			}
			_self.performAction();
		}
		
		public void insertString(FilterBypass fb, int offset, String string,
				AttributeSet attr) throws BadLocationException {
			fb.insertString(offset, string, attr);
			if (fb.getDocument().getLength() == 0) {
				fb.insertString(0, _emptyText, null);
				_self.ghost(true);
			}
		}
	}
	
	public interface ChangeListener {
		public void textChanged();
	}
	
}
