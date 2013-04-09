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

package org.alice.stageide.personresource.views;

/**
 * @author Dennis Cosgrove
 */
public class IngredientsView extends org.lgna.croquet.components.MigPanel {
	public static final java.awt.Color BACKGROUND_COLOR = new java.awt.Color( 173, 167, 208 );
	public static final java.awt.Color SELECTED_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( java.awt.Color.YELLOW, 1.0, 0.3, 1.0 );
	public static final java.awt.Color UNSELECTED_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( BACKGROUND_COLOR, 1.0, 0.9, 0.8 );

	private final org.lgna.croquet.components.Label isLifeStageLockedLabel = new org.lgna.croquet.components.Label();
	private final HorizontalWrapList<org.lgna.story.resources.sims2.LifeStage> lifeStageList;

	private static final javax.swing.Icon LOCKED_ICON = edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( org.alice.stageide.personresource.IngredientsComposite.class.getResource( "images/locked.png" ) );

	public IngredientsView( final org.alice.stageide.personresource.IngredientsComposite composite ) {
		super( composite, "insets 0, fill", "[][align right][grow 0][][]" );

		this.addComponent( this.isLifeStageLockedLabel );
		this.addComponent( composite.getLifeStageState().getSidekickLabel().createLabel(), "" );

		this.lifeStageList = new HorizontalWrapList( composite.getLifeStageState(), 1 );
		this.addComponent( this.lifeStageList, "push, span 2" );
		this.addComponent( composite.getRandomize().createButton(), "wrap" );

		this.addComponent( composite.getGenderState().getSidekickLabel().createLabel(), "skip" );
		this.addComponent( new HorizontalWrapList( composite.getGenderState(), 1 ), "span 2, wrap" );

		this.addComponent( composite.getBaseSkinToneState().getSidekickLabel().createLabel(), "skip" );
		this.addComponent( new HorizontalWrapList( composite.getBaseSkinToneState(), 1 ), "span 2, wrap" );

		final org.alice.stageide.personresource.SkinColorState skinColorState = composite.getSkinColorState();
		this.addComponent( composite.getSkinColorState().getSidekickLabel().createLabel(), "align right, skip 1" );
		this.addComponent( new ColorView( composite.getSkinColorState() ) );

		final int SIZE = 16;
		class SkinColorIcon implements javax.swing.Icon {
			public int getIconWidth() {
				return SIZE;
			}

			public int getIconHeight() {
				return SIZE;
			}

			public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
				java.awt.Color color = skinColorState.getSwingModel().getValue();
				g.setColor( color );
				g.fillRect( x, y, SIZE, SIZE );
			}
		}

		final org.lgna.croquet.components.Button button = composite.getCustomSkinColorOperation().createButton();
		button.setClobberIcon( new SkinColorIcon() );

		javax.swing.event.ChangeListener changeListener = new javax.swing.event.ChangeListener() {
			public void stateChanged( javax.swing.event.ChangeEvent e ) {
				button.repaint();
			}
		};

		skinColorState.getSwingModel().addChangeListener( changeListener );

		this.addComponent( button, "push, wrap" );

		org.lgna.croquet.components.FolderTabbedPane tabbedPane = composite.getBodyHeadTabState().createFolderTabbedPane();
		tabbedPane.setBackgroundColor( BACKGROUND_COLOR );
		this.addComponent( tabbedPane, "span 5, grow" );
		this.setBackgroundColor( BACKGROUND_COLOR );
	}

	@Override
	public void handleCompositePreActivation() {
		org.alice.stageide.personresource.IngredientsComposite composite = (org.alice.stageide.personresource.IngredientsComposite)this.getComposite();
		this.isLifeStageLockedLabel.setIcon( composite.getLifeStageState().isEnabled() ? null : LOCKED_ICON );
		super.handleCompositePreActivation();
	}
}
