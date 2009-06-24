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
public class AdjustableFontSizeOceanTheme extends javax.swing.plaf.metal.OceanTheme {
	private float sizeDelta = 0.0f;
	private float sizeMinimum = 0.0f;
	private javax.swing.plaf.FontUIResource createDeltaFontIfNecessary( javax.swing.plaf.FontUIResource fontUIResource ) {
		javax.swing.plaf.FontUIResource rv;
		if( this.sizeDelta != 0 ) {
			float nextSize = Math.max( this.sizeMinimum, fontUIResource.getSize() + this.sizeDelta );
			rv = new javax.swing.plaf.FontUIResource( fontUIResource.deriveFont( nextSize ) );
		} else {
			rv = fontUIResource;
		}
		return rv;
	}

	public float getSizeMinimum() {
		return this.sizeMinimum;
	}
	public void setSizeMinimum( float sizeMinimum ) {
		this.sizeMinimum = sizeMinimum;
	}

	public float getSizeDelta() {
		return this.sizeDelta;
	}
	public void setSizeDelta( float sizeDelta ) {
		if( this.sizeDelta != sizeDelta ) {
			this.sizeDelta = sizeDelta;
			this.controlTextFont = null;
			this.menuTextFont = null;
			this.subTextFont = null;
			this.systemTextFont = null;
			this.userTextFont = null;
			this.windowTitleTextFont = null;
		}
	}
	public void adjustSizeDelta( float sizeDeltaDelta ) {
		setSizeDelta( this.sizeDelta + sizeDeltaDelta );
	}
	

	private javax.swing.plaf.FontUIResource controlTextFont = null;
	private javax.swing.plaf.FontUIResource menuTextFont = null;
	private javax.swing.plaf.FontUIResource subTextFont = null;
	private javax.swing.plaf.FontUIResource systemTextFont = null;
	private javax.swing.plaf.FontUIResource userTextFont = null;
	private javax.swing.plaf.FontUIResource windowTitleTextFont = null;
	@Override
	public javax.swing.plaf.FontUIResource getControlTextFont() {
		if( this.controlTextFont != null ) {
			//pass
		} else {
			this.controlTextFont = this.createDeltaFontIfNecessary( super.getControlTextFont() );
		}
		return this.controlTextFont;
	}
	@Override
	public javax.swing.plaf.FontUIResource getMenuTextFont() {
		if( this.menuTextFont != null ) {
			//pass
		} else {
			this.menuTextFont = this.createDeltaFontIfNecessary( super.getMenuTextFont() );
		}
		return this.menuTextFont;
	}
	@Override
	public javax.swing.plaf.FontUIResource getSubTextFont() {
		if( this.subTextFont != null ) {
			//pass
		} else {
			this.subTextFont = this.createDeltaFontIfNecessary( super.getSubTextFont() );
		}
		return this.subTextFont;
	}
	@Override
	public javax.swing.plaf.FontUIResource getSystemTextFont() {
		if( this.systemTextFont != null ) {
			//pass
		} else {
			this.systemTextFont = this.createDeltaFontIfNecessary( super.getSystemTextFont() );
		}
		return this.systemTextFont;
	}
	@Override
	public javax.swing.plaf.FontUIResource getUserTextFont() {
		if( this.userTextFont != null ) {
			//pass
		} else {
			this.userTextFont = this.createDeltaFontIfNecessary( super.getUserTextFont() );
		}
		return this.userTextFont;
	}
	@Override
	public javax.swing.plaf.FontUIResource getWindowTitleFont() {
		if( this.windowTitleTextFont != null ) {
			//pass
		} else {
			this.windowTitleTextFont = this.createDeltaFontIfNecessary( super.getWindowTitleFont() );
		}
		return this.windowTitleTextFont;
	}
}
