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

///**
// * @author Dennis Cosgrove
// */
//class MenuItemUI extends javax.swing.plaf.basic.BasicMenuItemUI {
//	private static MenuItemUI singleton = new MenuItemUI();
//
//	public static javax.swing.plaf.ComponentUI createUI( javax.swing.JComponent c ) {
//		return MenuItemUI.singleton;
//	}
//	@Override
//	public java.awt.Dimension getPreferredSize( javax.swing.JComponent c ) {
//		java.awt.Dimension rv;
//		ZMenuItem menuItem = (ZMenuItem)c;
//		java.awt.Component subject = menuItem.getSubject();
//		if( subject != null ) {
//			edu.cmu.cs.dennisc.swing.SwingUtilities.doLayout( subject );
//			rv = subject.getPreferredSize();
//		} else {
//			rv = super.getPreferredSize( c );
//		}
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "getPreferredSize", rv, subject );
//		return rv;
//	}
////	@Override
////	public void paint( java.awt.Graphics g, javax.swing.JComponent c ) {
////		super.paint( g, c );
////		c.revalidate();
////		c.repaint();
////	}
////	@Override
////	public void paint( java.awt.Graphics g, javax.swing.JComponent c ) {
////		super.paint( g, c );
////		ZMenuItem menuItem = (ZMenuItem)c;
////		menuItem.getSubject().paint( g );
////		//c.getSubc.getComponent
////	}
//}

/**
 * @author Dennis Cosgrove
 */
public class ZMenuItem extends javax.swing.JMenuItem {
	private ActionOperation actionOperation;
//	private java.awt.Component subject;

	public ZMenuItem( ActionOperation actionOperation ) {
		setActionOperation( actionOperation );
		//this.setLayout( new javax.swing.BoxLayout( this, javax.swing.BoxLayout.LINE_AXIS ) );
		//this.setAlignmentX( 0.0f );
		//this.setOpaque( false );
//		this.addMouseListener( new java.awt.event.MouseListener() {
//			public void mouseEntered( java.awt.event.MouseEvent e ) {
//				ZMenuItem.this.setSelected( true );
////				ZMenuItem.this.repaint();
//			}
//			public void mouseExited( java.awt.event.MouseEvent e ) {
//				ZMenuItem.this.setSelected( false );
////				ZMenuItem.this.repaint();
//			}
//			public void mousePressed( java.awt.event.MouseEvent e ) {
////				java.awt.event.ActionEvent actionEvent = new java.awt.event.ActionEvent( ZMenuItem.this, java.awt.event.ActionEvent.ACTION_PERFORMED, ZMenuItem.this.getActionCommand() );
////				ZMenuItem.this.fireActionPerformed( actionEvent );
//				//ZManager.performIfAppropriate( ZMenuItem.this.getActionOperation(), e, ZManager.CANCEL_IS_WORTHWHILE );
//			}
//			public void mouseReleased( java.awt.event.MouseEvent e ) {
//			}
//			public void mouseClicked( java.awt.event.MouseEvent e ) {
//			}
//		} );
	}
	protected ActionOperation getActionOperation() {
		return this.actionOperation;
	}
	public void setActionOperation( ActionOperation actionOperation ) {
		if( this.actionOperation != null ) {
			this.setAction( null );
			//todo?
			this.setModel( model );
		}
		this.actionOperation = actionOperation;
		if( this.actionOperation != null ) {
			this.setAction( this.actionOperation.getActionForConfiguringSwing() );
			this.setModel( this.actionOperation.getButtonModel() );
		}
	}

//	@Override
//	public java.awt.Component getComponent() {
//		return this.getSubject();
//	}
//	public java.awt.Component getSubject() {
//		return this.subject;
//	}
//	public void setSubject( java.awt.Component subject ) {
//		if( this.subject != null ) {
//			this.remove( this.subject );
//			//this.removeAll();
//		}
//		this.subject = subject;
//		if( this.subject != null ) {
//			this.add( this.subject );
//			//this.add( javax.swing.Box.createHorizontalGlue() );
//		}
//		this.revalidate();
//		this.repaint();
//	}
//	@Override
//	public void updateUI() {
//		setUI( MenuItemUI.createUI( this ) );
//	}
//	@Override
//	protected void paintComponent( java.awt.Graphics g ) {
//		java.awt.Color color;
//		if( isSelected() ) {
//			color = new java.awt.Color( 191, 191, 255 );
//		} else {
//			color = this.getBackground();
//		}
//		g.setColor( color );
//		g.fillRect( 0, 0, this.getWidth(), this.getHeight() );
//	}
}
