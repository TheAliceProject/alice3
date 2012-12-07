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

package org.alice.ide.croquet.components.gallerybrowser;

/**
 * @author Dennis Cosgrove
 */
public class GalleryDragComponent extends org.alice.ide.croquet.components.KnurlDragComponent<org.alice.ide.croquet.models.gallerybrowser.GalleryDragModel> {
	private static final java.awt.Dimension DEFAULT_LARGE_ICON_SIZE = new java.awt.Dimension( 160, 120 );

	private static final java.awt.Color BASE_COLOR = new java.awt.Color( 0xf7e4b6 );
	private static final java.awt.Color HIGHLIGHT_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( BASE_COLOR, 1.0, 1.0, 1.4 );
	private static final java.awt.Color SHADOW_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( BASE_COLOR, 1.0, 1.0, 0.8 );

	private static final java.awt.Color ACTIVE_HIGHLIGHT_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( BASE_COLOR, 1.0, 1.0, 2.0 );
	private static final java.awt.Color ACTIVE_SHADOW_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( BASE_COLOR, 1.0, 1.0, 0.9 );

	public GalleryDragComponent( org.alice.ide.croquet.models.gallerybrowser.GalleryDragModel model ) {
		super( model );
		this.setLeftButtonClickModel( model.getLeftButtonClickModel() );
		org.lgna.croquet.components.Label label = new org.lgna.croquet.components.Label();
		label.setText( model.getText() );
		org.lgna.croquet.icon.IconFactory iconFactory = model.getIconFactory();
		label.setIcon( iconFactory != null ? iconFactory.getIcon( DEFAULT_LARGE_ICON_SIZE ) : null );
		label.setVerticalTextPosition( org.lgna.croquet.components.VerticalTextPosition.BOTTOM );
		label.setHorizontalTextPosition( org.lgna.croquet.components.HorizontalTextPosition.CENTER );
		this.internalAddComponent( label );
		this.setBackgroundColor( BASE_COLOR );
		this.setMaximumSizeClampedToPreferredSize( true );
		this.setAlignmentY( java.awt.Component.TOP_ALIGNMENT );

		if( model instanceof org.alice.stageide.modelresource.ResourceNode ) {
			org.alice.stageide.modelresource.ResourceNode resourceNode = (org.alice.stageide.modelresource.ResourceNode)model;
			org.alice.stageide.modelresource.ResourceKey resourceKey = resourceNode.getResourceKey();
			if( resourceKey != null ) {
				this.setToolTipText( "credit: " + resourceKey.getDisplayText() );
			}
		}
	}

	@Override
	protected int getInsetTop() {
		return 4;
	}

	@Override
	protected int getInsetRight() {
		return 4;
	}

	@Override
	protected int getInsetBottom() {
		return 4;
	}

	@Override
	protected int getDockInsetLeft() {
		return 0;
	}

	@Override
	protected int getInternalInsetLeft() {
		return 4;
	}

	@Override
	protected java.awt.geom.RoundRectangle2D.Float createShape( int x, int y, int width, int height ) {
		return new java.awt.geom.RoundRectangle2D.Float( x, y, width - 1, height - 1, 8, 8 );
	}

	@Override
	protected void fillBounds( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		g2.fill( this.createShape( x, y, width, height ) );
	}

	@Override
	protected void paintPrologue( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		java.awt.geom.RoundRectangle2D.Float shape = this.createShape( x, y, width, height );
		if( this.getAwtComponent().getModel().isPressed() ) {
			g2.setPaint( BASE_COLOR );
			g2.fill( shape );
		} else {
			int y1 = y + height;
			int yCenter = y + ( height / 2 );
			int yA = y + ( height / 3 );
			int yB = y1 - ( height / 3 );

			java.awt.Color highlightColor = this.isActive() ? ACTIVE_HIGHLIGHT_COLOR : HIGHLIGHT_COLOR;
			java.awt.Color shadowColor = this.isActive() ? ACTIVE_SHADOW_COLOR : SHADOW_COLOR;

			java.awt.GradientPaint paintTop = new java.awt.GradientPaint( x, y, highlightColor, x, yA, shadowColor );
			java.awt.GradientPaint paintBottom = new java.awt.GradientPaint( x, yB, shadowColor, x, y1, highlightColor );

			java.awt.Paint prevPaint = g2.getPaint();
			java.awt.Shape prevClip = g2.getClip();

			try {
				java.awt.geom.Area topArea = edu.cmu.cs.dennisc.java.awt.geom.AreaUtilities.createIntersection( prevClip, new java.awt.Rectangle( x, y, width, yCenter - y ) );
				g2.setClip( topArea );
				g2.setPaint( paintTop );
				g2.fill( shape );

				java.awt.geom.Area bottomArea = edu.cmu.cs.dennisc.java.awt.geom.AreaUtilities.createIntersection( prevClip, new java.awt.Rectangle( x, yCenter, width, y1 - yCenter ) );
				g2.setClip( bottomArea );
				g2.setPaint( paintBottom );
				g2.fill( shape );
			} finally {
				g2.setClip( prevClip );
				g2.setPaint( prevPaint );
			}
		}
	}
}
