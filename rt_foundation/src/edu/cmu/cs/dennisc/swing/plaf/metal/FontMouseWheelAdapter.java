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
package edu.cmu.cs.dennisc.swing.plaf.metal;

/**
 * @author Dennis Cosgrove
 */
public class FontMouseWheelAdapter implements java.awt.event.MouseWheelListener {
	private float scaleFactor = 1.0f;
	private AdjustableFontSizeOceanTheme adjustableFontSizeOceanTheme;
	private boolean isLookAndFeelUpdated = false;
	public FontMouseWheelAdapter() {
		this( new AdjustableFontSizeOceanTheme() );
	}
	public FontMouseWheelAdapter( AdjustableFontSizeOceanTheme adjustableFontSizeOceanTheme ) {
		this.adjustableFontSizeOceanTheme = adjustableFontSizeOceanTheme;
	}
	public float getScaleFactor() {
		return this.scaleFactor;
	}
	public void setScaleFactor( float scaleFactor ) {
		this.scaleFactor = scaleFactor;
	}
	public void updateLookAndFeel() {
		javax.swing.plaf.metal.MetalLookAndFeel.setCurrentTheme( this.adjustableFontSizeOceanTheme );
		try {
			javax.swing.UIManager.setLookAndFeel( "javax.swing.plaf.metal.MetalLookAndFeel" );
		} catch( Exception e ) {
			throw new RuntimeException( e );
		}
		this.isLookAndFeelUpdated = true;
	}
	public void mouseWheelMoved( final java.awt.event.MouseWheelEvent e ) {
		if( edu.cmu.cs.dennisc.swing.SwingUtilities.isQuoteControlUnquoteDown( e ) ) {
			this.adjustableFontSizeOceanTheme.adjustSizeDelta( e.getWheelRotation() * scaleFactor );
			if( this.isLookAndFeelUpdated ) {
				//pass
			} else {
				this.updateLookAndFeel();
			}
			javax.swing.SwingUtilities.updateComponentTreeUI( javax.swing.SwingUtilities.getRoot( e.getComponent() ) );
		}
	}
}
