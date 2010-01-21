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
package org.alice.apis.moveandturn.graphic.animation;

/**
 * @author Dennis Cosgrove
 */
public class BubbleAnimation extends OpenUpdateCloseOverlayGraphicAnimation {
	private edu.cmu.cs.dennisc.scenegraph.graphics.Bubble m_bubble;
	public BubbleAnimation( org.alice.apis.moveandturn.Composite composite, double openingDuration, double updatingDuration, double closingDuration, edu.cmu.cs.dennisc.scenegraph.graphics.Bubble bubble ) {
		super( composite, openingDuration, updatingDuration, closingDuration );
		m_bubble = bubble;
	}
	@Override
	protected edu.cmu.cs.dennisc.scenegraph.Graphic getSGGraphic() {
		return m_bubble;
	}
	@Override
	protected void updateStateAndPortion( State state, double portion) {
		if( state == State.OPENNING ) {
			m_bubble.portion.setValue( portion );
		} else if( state == State.UPDATING ) {
			m_bubble.portion.setValue( 1.0 );
		} else {
			//state == State.CLOSING;
			m_bubble.portion.setValue( 1.0-portion );
		}
	}
}
