package client.controller;

import java.awt.Graphics;

public abstract class PaintFunction {

	private boolean _used;
	
	public PaintFunction() {
		_used = false;
	}
	
	public void use() {
		_used = true;
		perform();
	}
	
	public abstract void perform();
	
	public boolean isUsed() {
		return _used;
	}
}
