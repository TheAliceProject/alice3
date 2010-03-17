package edu.cmu.cs.dennisc.croquet;

public class KFrame extends KRoot {
	private javax.swing.JFrame jFrame = new javax.swing.JFrame();
	@Override
	protected java.awt.Dialog getAWTDialog() {
		return null;
	}
	@Override
	protected java.awt.Frame getAWTFrame() {
		return this.jFrame;
	}
	@Override
	protected java.awt.Window getAWTWindow() {
		return this.jFrame;
	}
	@Override
	protected java.awt.Container getContentPane() {
		return this.jFrame.getContentPane();
	}
}
