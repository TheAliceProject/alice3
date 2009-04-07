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
package org.alice.ide.common;

/**
 * @author Dennis Cosgrove
 */
public abstract class NodeLikeSubstance extends org.alice.ide.AbstractDragComponent {
	protected static final int KNURL_WIDTH = 8;
	public NodeLikeSubstance() {
		this.setLayout( new javax.swing.BoxLayout( this, javax.swing.BoxLayout.LINE_AXIS ) );
	}

	protected edu.cmu.cs.dennisc.awt.BevelState getBevelState() {
		if( this.isActive() ) {
			if( this.isPressed() ) {
				return edu.cmu.cs.dennisc.awt.BevelState.SUNKEN;
			} else {
				return edu.cmu.cs.dennisc.awt.BevelState.RAISED;
			}
		} else {
			return edu.cmu.cs.dennisc.awt.BevelState.FLUSH;
		}
	}

	protected abstract int getDockInsetLeft();
	protected abstract int getInternalInsetLeft();
	@Override
	protected final int getInsetLeft() {
		int rv = 0;
		rv += getDockInsetLeft();
		if( this.isKnurlDesired() ) {
			rv += KNURL_WIDTH;
		}
		rv += getInternalInsetLeft();
		return rv;
	}
	
	protected boolean isKnurlDesired() {
		//todo: check for dragOperation
		return true;
	}
//	protected boolean isCullingContainsDesired() {
//		return isKnurlDesired() == false;
//	}
	
//	@Override
//	public boolean contains( int x, int y ) {
//		if( isCullingContainsDesired() ) {
//			return false;
//		} else {
//			return super.contains( x, y );
//		}
//	}

	@Override
	protected void paintEpilogue( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		if( isKnurlDesired() ) {
			g2.setColor( java.awt.Color.BLACK );
			edu.cmu.cs.dennisc.awt.KnurlUtilities.paintKnurl5( g2, x + getDockInsetLeft(), y + 2, KNURL_WIDTH, height - 2 );
		}
	}
}
