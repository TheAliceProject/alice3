/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.cmu.cs.dennisc.tutorial;

/**
 * @author Dennis Cosgrove
 */
public abstract class Step {
	private java.util.UUID id = java.util.UUID.randomUUID();
	private Tutorial tutorial;
	public Step( Tutorial tutorial ) {
		this.tutorial = tutorial;
	}
	public java.util.UUID getId() {
		return this.id;
	}
	public Tutorial getTutorial() {
		return this.tutorial;
	}
	
	public abstract edu.cmu.cs.dennisc.croquet.Model getModelWaitingOn();
	
	public boolean isAutoAdvanceDesired() {
		return true;
	}
	private java.util.List< Feature > features = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	protected void addFeature( Feature feature ) {
		this.features.add( feature );
	}
	public Iterable< Feature > getFeatures() {
		return this.features;
	}
	public abstract edu.cmu.cs.dennisc.croquet.JComponent< ? > getCard();
	public abstract edu.cmu.cs.dennisc.croquet.JComponent< ? > getNote();

	private static int getXForWestLayout( java.awt.Rectangle noteBounds, java.awt.Rectangle featureComponentBounds ) {
		int x = featureComponentBounds.x;
		x -= 200;
		x -= noteBounds.width;
		return x;
	}
	private static int getXForEastLayout( java.awt.Rectangle noteBounds, java.awt.Rectangle featureComponentBounds ) {
		int x = featureComponentBounds.x;
		x += featureComponentBounds.width;
		x += 200;
		return x;
	}

	private static int getYForNorthLayout( java.awt.Rectangle noteBounds, java.awt.Rectangle featureComponentBounds ) {
		int y = featureComponentBounds.y;
		y -= 200;
		y -= noteBounds.height;
		return y;
	}
	private static int getYForSouthLayout( java.awt.Rectangle noteBounds, java.awt.Rectangle featureComponentBounds ) {
		int y = featureComponentBounds.y;
		y += featureComponentBounds.height;
		y += 200;
		return y;
	}
	
	protected void layoutNote( edu.cmu.cs.dennisc.croquet.Component< ? > note ) {
		edu.cmu.cs.dennisc.croquet.Component< ? > card = this.getCard();
		assert card == note.getParent();
		
		javax.swing.JFrame jFrame = edu.cmu.cs.dennisc.croquet.Application.getSingleton().getFrame().getAwtWindow();
		javax.swing.JLayeredPane layeredPane = jFrame.getLayeredPane();
		java.awt.Rectangle cardBounds = javax.swing.SwingUtilities.getLocalBounds( layeredPane );
		assert cardBounds.width > 0 && cardBounds.height > 0;
		
		java.awt.Rectangle noteBounds = note.getLocalBounds();
		int x = 20;
		int y = 20;
		if( this.features.size() > 0 ) {
			Feature feature = this.features.get( 0 );
			Feature.Connection actualConnection = null;
			edu.cmu.cs.dennisc.croquet.Component<?> featureComponent = feature.getComponent();
			if( featureComponent != null ) {
				java.awt.Rectangle featureComponentBounds = featureComponent.getBounds( note.getParent() );
				Feature.ConnectionPreference connectionPreference = feature.getConnectionPreference();
				if( connectionPreference == Feature.ConnectionPreference.EAST_WEST ) {
					x = getXForWestLayout( noteBounds, featureComponentBounds );
					if( x >= 32 ) {
						actualConnection = Feature.Connection.WEST;
					} else {
						x = getXForEastLayout(noteBounds, featureComponentBounds);
						if( x <= ( cardBounds.width - noteBounds.width - 32 ) ) {
							actualConnection = Feature.Connection.EAST;
						}
					}
				}
				if( actualConnection != null ) {
					int yFeatureComponentCenter = featureComponentBounds.y + featureComponentBounds.height/2;
					int yCardCenter = ( cardBounds.y + cardBounds.height ) / 2;
					y = yFeatureComponentCenter;
					if( yFeatureComponentCenter < yCardCenter ) {
						y += 200;
					} else {
						y -= noteBounds.height;
						y -= 200;
					}
				} else {
					y = getYForNorthLayout( noteBounds, featureComponentBounds );
					if( y >= 32 ) {
						actualConnection = Feature.Connection.NORTH;
					} else {
						y = getYForSouthLayout( noteBounds, featureComponentBounds);
						if( y <= ( cardBounds.height - noteBounds.height - 32 ) ) {
							actualConnection = Feature.Connection.SOUTH;
						} else {
							actualConnection = Feature.Connection.SOUTH;
							y = 200;
						}
					}
					int xFeatureComponentCenter = featureComponentBounds.x + featureComponentBounds.width/2;
					int xCardCenter = ( cardBounds.x + cardBounds.width ) / 2;
					x = xFeatureComponentCenter;
					if( xFeatureComponentCenter < xCardCenter ) {
						x += 200;
					} else {
						x -= noteBounds.width;
						x -= 200;
					}
				}
			}
			feature.setActualConnection( actualConnection );
		} else {
			x = (cardBounds.width-noteBounds.width)/2;
			y = 64;
		}
		note.getAwtComponent().setLocation( x, y );
	}
}
