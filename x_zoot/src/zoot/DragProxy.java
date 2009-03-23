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
class DragProxy extends Proxy {
	public DragProxy( java.awt.Component subject ) {
		super( subject );
	}
	private final int DROP_SHADOW_SIZE = 6;
	@Override
	public int getProxyWidth() {
		return super.getProxyWidth() + DROP_SHADOW_SIZE;
	}
	@Override
	public int getProxyHeight() {
		return super.getProxyHeight() + DROP_SHADOW_SIZE;
	}
	@Override
	protected float getAlpha() {
		if( this.isOverDropAcceptor() ) {
			return 0.5f;
		} else {
			return 1.0f;
		}
	}
	
	
	@Override
	protected void paintProxy( java.awt.Graphics2D g2 ) {
		java.awt.Paint prevPaint = g2.getPaint();
		g2.setPaint( new java.awt.Color( 0,0,0,64 ) );
		//todo?
		g2.translate( DROP_SHADOW_SIZE, DROP_SHADOW_SIZE );
		fillBounds( g2 );
		g2.translate( -DROP_SHADOW_SIZE, -DROP_SHADOW_SIZE );
		g2.setPaint( prevPaint );
		this.getSubject().print( g2 );

		
//		if( isOverDragAccepter ) {
//			//pass
//		} else {
//			g2.setPaint( new java.awt.Color( 127, 127, 127, 127 ) );
//			this.createBoundsShape().fill( g2 );
//		}
		if( isCopyDesired ) {
			g2.setPaint( org.alice.ide.lookandfeel.PaintUtilities.getCopyTexturePaint() );
			fillBounds( g2 );
		}
	}
}
