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
package edu.cmu.cs.dennisc.math;

/**
 * @author Dennis Cosgrove
 */
public class GoldenRatio {
//	private static int s_windowBorderWidth;
//	private static int s_windowBorderHeight;
//	static {
//		try {
//			java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
//			String propNames[] = (String[])toolkit.getDesktopProperty( "win.propNames" );
//			if( propNames.length > 0 ) {
//				Integer dragWidth = (Integer)toolkit.getDesktopProperty( "win.drag.width" );
//				Integer dragHeight = (Integer)toolkit.getDesktopProperty( "win.drag.height" );
//				Integer captionHeight = (Integer)toolkit.getDesktopProperty( "win.frame.captionHeight" );
//
//				s_windowBorderWidth = dragWidth.intValue();
//				s_windowBorderHeight = captionHeight.intValue() + dragHeight.intValue();
//			}
//		} catch( Throwable t ) {
//			t.printStackTrace();
//			s_windowBorderWidth = 0;
//			s_windowBorderHeight = 0;
//		}
//	}
	public static double PHI = 1.6180339887;
	public static int getShorterSideLength( int longerSideLength ) {
		return (int)(longerSideLength/PHI);
	}
	public static int getLongerSideLength( int shorterSideLength ) {
		return (int)(shorterSideLength*PHI);
	}
	
//	public static int getLongerWidthAccountingForWindowBorder( int shorterHeight ) {
//		return getLongerSideLength( shorterHeight + s_windowBorderHeight ) - s_windowBorderWidth;
//	}
//	public static int getShorterWidthAccountingForWindowBorder( int longerHeight ) {
//		return getShorterSideLength( longerHeight + s_windowBorderHeight ) - s_windowBorderWidth;
//	}
//
//	public static int getLongerHeightAccountingForWindowBorder( int shorterWidth ) {
//		return getLongerSideLength( shorterWidth + s_windowBorderWidth ) - s_windowBorderHeight;
//	}
//	public static int getShorterHeightAccountingForWindowBorder( int longerWidth ) {
//		return getShorterSideLength( longerWidth + s_windowBorderWidth ) - s_windowBorderHeight;
//	}
}
