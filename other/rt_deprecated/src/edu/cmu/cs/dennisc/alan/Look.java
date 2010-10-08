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

package edu.cmu.cs.dennisc.alan;

import edu.cmu.cs.dennisc.awt.BevelState;

/**
 * @author Dennis Cosgrove
 */
public final class Look {
	//todo: allow people to set this value
	private static final int BASE_FONT_SIZE_MINIMUM = 8;
	private static int s_baseFontSize = 12;
	private static java.awt.Font s_baseFont = null;
	private static java.awt.Font s_mutedFont = null;

	private Look() {
	}
	
	public static java.awt.Paint getNounPaint( float x, float y, float width, float height, BevelState bevelState ) {
		if( bevelState == BevelState.RAISED ) {
			return new java.awt.GradientPaint( x, y, new java.awt.Color( 192, 192, 255 ), x, y+height, new java.awt.Color( 128, 128, 160 ) );
		} else if( bevelState == BevelState.SUNKEN ){
			return new java.awt.GradientPaint( x, y, new java.awt.Color( 128, 128, 160 ), x, y+height, new java.awt.Color( 96, 96, 128 ) );
		} else {
			//todo
			return new java.awt.GradientPaint( x, y, new java.awt.Color( 192, 192, 255 ), x, y+height, new java.awt.Color( 128, 128, 160 ) );
		}
	}

	public static int getBaseFontSize() {
		return s_baseFontSize;
	}
	public static void setBaseFontSize( int baseFontSize ) {
		if( s_baseFontSize != baseFontSize ) {
			s_baseFontSize = baseFontSize;
			s_baseFont = null;
			s_mutedFont = null;
			//fireBaseFontSizeListeners();
		}
	}
	
	public static void incBaseFontSize( int baseFontSizeDelta ) {
		int baseFontSize = Math.max( s_baseFontSize + baseFontSizeDelta, BASE_FONT_SIZE_MINIMUM );
		setBaseFontSize( baseFontSize );
	}
	
	public static java.awt.Font getBaseFont() {
		if( s_baseFont != null ) {
			//pass
		} else {
			s_baseFont = new java.awt.Font( null, java.awt.Font.BOLD, s_baseFontSize );
		}
		return s_baseFont;
	}
	public static java.awt.Font getMutedFont() {
		if( s_mutedFont != null ) {
			//pass
		} else {
			s_mutedFont = new java.awt.Font( null, java.awt.Font.ITALIC, s_baseFontSize*5/6 );
		}
		return s_mutedFont;
	}
	
	
	public final static java.awt.Paint TEXT_PAINT = java.awt.Color.BLACK;

//	public static void fill3D( java.awt.Graphics2D g2, java.awt.Paint paint, boolean isRaised, java.awt.geom.Area area ) {
//		float dx;
//		float dy;
//		if( isRaised ) {
//			dx = -1.0f;
//			dy = -1.0f;
//		} else {
//			dx = +1.0f;
//			dy = +1.0f;
//		}
//		g2.translate( -dx, -dy );
//		g2.setPaint( java.awt.Color.BLACK );
//		g2.fill( area );
//		g2.translate( dx+dx, dy+dy );
//		g2.setPaint( java.awt.Color.WHITE );
//		g2.fill( area );
//		g2.translate( -dx, -dy );
//		g2.setPaint( paint );
//		g2.fill( area );
//	}

	public static float getSocketWidth() {
		return Look.getBaseFont().getSize() * 0.5f;
	}
	public static float getSocketHeight() {
		return getSocketWidth() * 1.5f;
	}
	
	//todo
	public static float getKnurlWidth() {
		return 8;
	}

	public static java.awt.Shape constructShapeFor( Class<?> cls, float x0, float y0, float width, float height ) {
		assert cls != null;
		java.awt.Shape shape;
		float x1 = x0 + width;
		float y1 = y0 + height;
		if( String.class.isAssignableFrom( cls ) ) {

			float yDelta = height * 0.2f;
			float yA = y0 + yDelta;
			float yB = y1 - yDelta;
			
			float xDelta = width * 0.2f;
			float xA = x0 + xDelta;
			float xB = x1 - xDelta;

			
			java.awt.geom.GeneralPath generalPath = new java.awt.geom.GeneralPath();
			generalPath.moveTo( x1, y0 );
			generalPath.curveTo( x0, y0, xB, yB, x0, yB );
			generalPath.lineTo( x0, y1 );
			generalPath.curveTo( x1, y1, xA, yA, x1, yA );
			//todo?
//			generalPath.closePath();
			shape = generalPath;
		} else if( Number.class.isAssignableFrom( cls ) ) {
			//todo: Integer
//			if( Integer.class.isAssignableFrom( cls ) ) {
//			} else {
//			}
			float yDelta = height * 0.333f;
			float yA = y0 + yDelta;
			float yB = y1 - yDelta;
			java.awt.geom.GeneralPath generalPath = new java.awt.geom.GeneralPath();
			generalPath.moveTo( x1, y0 );
			generalPath.lineTo( x0, y0 );
			generalPath.lineTo( x0, yA );
			generalPath.lineTo( x1, yA );
			generalPath.lineTo( x1, yB );
			generalPath.lineTo( x0, yB );
			generalPath.lineTo( x0, y1 );
			generalPath.lineTo( x1, y1 );
			//todo?
//			generalPath.closePath();
			shape = generalPath;
		} else if( Boolean.class.isAssignableFrom( cls ) ) {
			float xA = ( x0 + x1 ) * 0.5f;
			float yA = ( y0 + y1 ) * 0.5f;
			java.awt.geom.GeneralPath generalPath = new java.awt.geom.GeneralPath();
			generalPath.moveTo( x1, y0 );
			generalPath.lineTo( x0, y0 );
			generalPath.quadTo( xA, yA, x0, y1 );
			generalPath.lineTo( x1, y1 );
			//todo?
//			generalPath.closePath();
			shape = generalPath;
		} else {
			shape = new java.awt.geom.Rectangle2D.Float( x0, y0, width, height );
		}
		return shape;
	}
	public static java.awt.geom.Area constructAreaFor( Class<?> cls, float x, float y, float width, float height ) {
		return new java.awt.geom.Area( constructShapeFor( cls, x, y, width, height ) );
	}
//	// todo: name
//	public enum State {
//		FILL, TOP_LEFT, BOTTOM_RIGHT
//	}
//	
//	public static java.awt.Shape constructShapeFor( Class<?> cls, float x0, float y0, float width, float height, State state ) {
//		assert cls != null;
//		java.awt.Shape shape;
//		float x1 = x0 + width;
//		float y1 = y0 + height;
//		if( String.class.isAssignableFrom( cls ) ) {
//
//			float yDelta = height * 0.2f;
//			float yA = y0 + yDelta;
//			float yB = y1 - yDelta;
//			
//			float xDelta = width * 0.2f;
//			float xA = x0 + xDelta;
//			float xB = x1 - xDelta;
//
//			
//			java.awt.geom.GeneralPath generalPath = new java.awt.geom.GeneralPath();
//			if( state == State.FILL ) {
//				generalPath.moveTo( x1, y0 );
//				generalPath.curveTo( x0, y0, xB, yB, x0, yB );
//				generalPath.lineTo( x0, y1 );
//				generalPath.curveTo( x1, y1, xA, yA, x1, yA );
//			} else if( state == State.TOP_LEFT ) {
//				generalPath.moveTo( x1, y0 );
//				generalPath.curveTo( x0, y0, xB, yB, x0, yB );
//				generalPath.lineTo( x0, y1 );
//			} else if( state == State.BOTTOM_RIGHT ) {
//				generalPath.moveTo( x0, y1 );
//				generalPath.curveTo( x1, y1, xA, yA, x1, yA );
//				//generalPath.lineTo( x1, y0 );
//			} else {
//				assert false;
//			}
//			shape = generalPath;
//		} else if( Double.class.isAssignableFrom( cls ) ) {
//			float yDelta = height * 0.333f;
//			float yA = y0 + yDelta;
//			float yB = y1 - yDelta;
//			java.awt.geom.GeneralPath generalPath = new java.awt.geom.GeneralPath();
//			if( state == State.FILL ) {
//				generalPath.moveTo( x1, y0 );
//				generalPath.lineTo( x0, y0 );
//				generalPath.lineTo( x0, yA );
//				generalPath.lineTo( x1, yA );
//				generalPath.lineTo( x1, yB );
//				generalPath.lineTo( x0, yB );
//				generalPath.lineTo( x0, y1 );
//				generalPath.lineTo( x1, y1 );
//			} else if( state == State.TOP_LEFT ) {
//				generalPath.moveTo( x1, y0 );
//				generalPath.lineTo( x0, y0 );
//				generalPath.lineTo( x0, yA );
//
//				generalPath.moveTo( x1, yA );
//				generalPath.lineTo( x1, yB );
//				generalPath.lineTo( x0, yB );
//				generalPath.lineTo( x0, y1 );
//			} else if( state == State.BOTTOM_RIGHT ) {
//				generalPath.moveTo( x0, yA );
//				generalPath.lineTo( x1, yA );
//
//				generalPath.moveTo( x0, y1 );
//				generalPath.lineTo( x1, y1 );
//				//generalPath.lineTo( x1, y0 );
//			} else {
//				assert false;
//			}
//			shape = generalPath;
//		} else if( Boolean.class.isAssignableFrom( cls ) ) {
//			float xA = ( x0 + x1 ) * 0.5f;
//			float yA = ( y0 + y1 ) * 0.5f;
//			java.awt.geom.GeneralPath generalPath = new java.awt.geom.GeneralPath();
//			if( state == State.FILL ) {
//				generalPath.moveTo( x1, y0 );
//				generalPath.lineTo( x0, y0 );
//				generalPath.quadTo( xA, yA, x0, y1 );
//				generalPath.lineTo( x1, y1 );
//			} else if( state == State.TOP_LEFT ) {
//				generalPath.moveTo( x1, y0 );
//				generalPath.lineTo( x0, y0 );
//				generalPath.quadTo( xA, yA, x0, y1 );
//			} else if( state == State.BOTTOM_RIGHT ) {
//				generalPath.moveTo( x0, y1 );
//				generalPath.lineTo( x1, y1 );
//				//generalPath.lineTo( x1, y0 );
//			} else {
//				assert false;
//			}
//			shape = generalPath;
//		} else if( org.alice.apis.storytelling.Transformable.class.isAssignableFrom( cls ) ) {
//			shape = new java.awt.geom.CubicCurve2D.Float( x1, y0, x0, y0, x0, y1, x1, y1 );
//		} else {
//			shape = new java.awt.geom.Rectangle2D.Float( x0, y0, width, height );
//		}
//		return shape;
//	}
//	public static java.awt.geom.Area constructAreaFor( Class<?> cls, float x, float y, float width, float height ) {
//		return new java.awt.geom.Area( constructShapeFor( cls, x, y, width, height, State.FILL ) );
//	}
}
