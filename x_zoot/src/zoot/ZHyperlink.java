/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package zoot;

/**
 * @author Dennis Cosgrove
 */
public class ZHyperlink extends javax.swing.AbstractButton {
	private static java.util.Map< String, javax.swing.text.View > map = new java.util.HashMap< String, javax.swing.text.View >();
	private static javax.swing.text.View createHTMLView( ZHyperlink hyperlink, java.awt.Color color ) {
		String name = (String)hyperlink.getAction().getValue( javax.swing.Action.NAME );
		String text = "<html><font color=\"#" + Integer.toHexString( color.getRGB() & 0xFFFFFF ) + "\"><u><b>" + name + "</b></u></font></html>";
		javax.swing.text.View rv = map.get( text );
		if( rv != null ) {
			//pass
		} else {
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( text );
			rv = javax.swing.plaf.basic.BasicHTML.createHTMLView( hyperlink, text );
			map.put( text, rv );
		}
		return rv;
	}

	private ActionOperation actionOperation;
	public ZHyperlink( ActionOperation actionOperation ) {
		this.actionOperation = actionOperation;
		this.setModel( new javax.swing.DefaultButtonModel() );
		javax.swing.Action action = this.actionOperation.getActionForConfiguringSwing();
		this.setAction( action );
		this.setToolTipText( (String)action.getValue( javax.swing.Action.LONG_DESCRIPTION ) );
	}
	protected ActionOperation getActionOperation() {
		return this.actionOperation;
	}
	
	class MouseAdapter implements java.awt.event.MouseListener {
		public void mouseEntered( java.awt.event.MouseEvent e ) {
			model.setArmed( true );
			repaint();
		}
		public void mouseExited( java.awt.event.MouseEvent e ) {
			model.setArmed( false );
			repaint();
		}
		public void mousePressed( java.awt.event.MouseEvent e ) {
			model.setPressed( true );
			repaint();
		}
		public void mouseReleased( java.awt.event.MouseEvent e ) {
			model.setPressed( false );
			repaint();
		}
		public void mouseClicked( java.awt.event.MouseEvent e ) {
		}
	}
	private MouseAdapter mouseAdapter = new MouseAdapter();
	
	@Override
	public void addNotify() {
		super.addNotify();
		this.addMouseListener( this.mouseAdapter );
	}
	@Override
	public void removeNotify() {
		this.removeMouseListener( this.mouseAdapter );
		super.removeNotify();
	}
	
	private java.awt.Color activeColor = java.awt.Color.BLUE;
	private java.awt.Color pressedColor = java.awt.Color.BLUE.darker();
	public java.awt.Color getActiveColor() {
		return this.activeColor;
	}
	public void setActiveColor( java.awt.Color activeColor ) {
		this.activeColor = activeColor;
		this.repaint();
	}
	public java.awt.Color getPressedColor() {
		return this.pressedColor;
	}
	public void setPressedColor( java.awt.Color pressedColor ) {
		this.pressedColor = pressedColor;
		this.repaint();
	}
	
	@Override
	public java.awt.Dimension getPreferredSize() {
		javax.swing.text.View view = createHTMLView( this, java.awt.Color.BLACK );;
		float width = view.getPreferredSpan( javax.swing.text.View.X_AXIS );
		float height = view.getPreferredSpan( javax.swing.text.View.Y_AXIS );
		return new java.awt.Dimension( (int)width+1, (int)height+1 );
	}
	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		super.paintComponent( g );
		java.awt.Color color;
		if( model.isArmed() ) {
			if( model.isPressed() ) {
				color = this.getPressedColor();
			} else {
				color = this.getActiveColor();
			}
		} else {
			color = this.getForeground();
		}
		javax.swing.text.View view = createHTMLView( this, color );;
		view.paint( g, g.getClipBounds() );
	}	
}
