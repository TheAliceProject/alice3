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


class ToolTipUI extends javax.swing.plaf.basic.BasicToolTipUI {
	private static ToolTipUI singleton = new ToolTipUI();
	public static javax.swing.plaf.ComponentUI createUI( javax.swing.JComponent c ) {
		return ToolTipUI.singleton;
	}
	@Override
	public java.awt.Dimension getPreferredSize( javax.swing.JComponent c ) {
		ZToolTip toolTip = (ZToolTip)c;
		java.awt.Component subject = toolTip.getSubject();
		if( subject != null ) {
			edu.cmu.cs.dennisc.swing.SwingUtilities.invalidateTree( subject );
			edu.cmu.cs.dennisc.swing.SwingUtilities.doLayoutTree( subject );
			return subject.getPreferredSize();
		} else {
			return super.getPreferredSize( c );
		}
	}
//	@Override
//	public void paint( java.awt.Graphics g, javax.swing.JComponent c ) {
//		ZToolTip toolTip = (ZToolTip)c;
//		java.awt.Component subject = toolTip.getSubject();
//		if( subject != null ) {
//			subject.print( g );
//		} else {
//			super.paint( g, c );
//		}
//	}
}

/**
 * @author Dennis Cosgrove
 */
public class ZToolTip extends javax.swing.JToolTip {
	private java.awt.Component subject;
	
	public ZToolTip( java.awt.Component subject ) {
		this.setLayout( new java.awt.GridLayout( 1, 1 ) );
		this.setSubject( subject );
		this.setOpaque( false );
	}
	public java.awt.Component getSubject() {
		return this.subject;
	}
	public void setSubject( java.awt.Component subject ) {
		if( this.subject != null ) {
			this.remove( this.subject );
		}
		this.subject = subject;
		if( this.subject != null ) {
			this.add( this.subject );
		}
	}
	@Override
	public void updateUI() {
		setUI( ToolTipUI.createUI( this ) );
	}
}
