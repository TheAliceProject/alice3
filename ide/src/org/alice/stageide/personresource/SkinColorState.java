/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.alice.stageide.personresource;

import static org.lgna.story.resources.sims2.BaseSkinTone.DARK;
import static org.lgna.story.resources.sims2.BaseSkinTone.DARKER;
import static org.lgna.story.resources.sims2.BaseSkinTone.LIGHT;
import static org.lgna.story.resources.sims2.BaseSkinTone.LIGHTER;

import org.lgna.croquet.color.ColorState;

/**
 * @author Dennis Cosgrove
 */
public final class SkinColorState extends org.lgna.croquet.color.ColorState {
	private final MelaninChooserTabComposite skinToneChooserTabComposite = new MelaninChooserTabComposite();

	private static final java.awt.Color[] MELANIN_SHADES = {
			edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( DARKER.getColor(), 1.0, 1.0, 0.9 ),
			DARKER.getColor(),
			edu.cmu.cs.dennisc.java.awt.ColorUtilities.interpolate( DARKER.getColor(), DARK.getColor(), 0.5f ),
			DARK.getColor(),
			edu.cmu.cs.dennisc.java.awt.ColorUtilities.interpolate( DARK.getColor(), LIGHT.getColor(), 0.5f ),
			LIGHT.getColor(),
			edu.cmu.cs.dennisc.java.awt.ColorUtilities.interpolate( LIGHT.getColor(), LIGHTER.getColor(), 0.5f ),
			LIGHTER.getColor(),
			edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( LIGHTER.getColor(), 1.0, 1.0, 1.1 )
	};

	public SkinColorState() {
		super( org.lgna.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString( "646ae40a-547a-4f52-962f-57a7dc4d970f" ), org.lgna.story.resources.sims2.BaseSkinTone.getRandom().getColor() );
		this.getChooserDialogCoreComposite().addSubComposite( this.skinToneChooserTabComposite );
	}

	public java.awt.Color[] getMelaninShades() {
		return MELANIN_SHADES;
	}

	public static void main( String[] args ) throws Exception {
		javax.swing.UIManager.LookAndFeelInfo lookAndFeelInfo = edu.cmu.cs.dennisc.javax.swing.plaf.PlafUtilities.getInstalledLookAndFeelInfoNamed( "Nimbus" );
		if( lookAndFeelInfo != null ) {
			javax.swing.UIManager.setLookAndFeel( lookAndFeelInfo.getClassName() );
		}

		final org.lgna.croquet.simple.SimpleApplication app = new org.lgna.croquet.simple.SimpleApplication();
		final ColorState colorState = new SkinColorState();

		//org.alice.stageide.personresource.views.MelaninSlider colorView = new org.alice.stageide.personresource.views.MelaninSlider( colorState );
		org.lgna.croquet.components.Button button = colorState.getChooserDialogCoreComposite().getOperation().createButton();

		final int SIZE = 16;
		class ColorIcon implements javax.swing.Icon {
			public int getIconWidth() {
				return SIZE;
			}

			public int getIconHeight() {
				return SIZE;
			}

			public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
				java.awt.Color color = colorState.getSwingModel().getValue();
				//				float[] hsbs = new float[ 3 ];
				//				java.awt.Color.RGBtoHSB( color.getRed(), color.getGreen(), color.getBlue(), hsbs );
				//				edu.cmu.cs.dennisc.java.util.logging.Logger.outln( hsbs[ 0 ] );
				g.setColor( color );
				g.fillRect( x, y, SIZE, SIZE );
			}
		}
		final org.lgna.croquet.components.Label label = new org.lgna.croquet.components.Label( new ColorIcon() );

		javax.swing.event.ChangeListener changeListener = new javax.swing.event.ChangeListener() {
			public void stateChanged( javax.swing.event.ChangeEvent e ) {
				label.repaint();
			}
		};

		colorState.getSwingModel().addChangeListener( changeListener );

		org.lgna.croquet.components.LineAxisPanel lineAxisPanel = new org.lgna.croquet.components.LineAxisPanel();

		for( java.awt.Color melaninShade : MELANIN_SHADES ) {
			org.lgna.croquet.BooleanState itemSelectedState = colorState.getItemSelectedState( melaninShade );
			itemSelectedState.initializeIfNecessary();
			itemSelectedState.setTextForBothTrueAndFalse( "" );
			itemSelectedState.setIconForBothTrueAndFalse( new org.alice.ide.swing.icons.ColorIcon( melaninShade ) );
			org.lgna.croquet.components.ToggleButton toggleButton = itemSelectedState.createToggleButton();
			lineAxisPanel.addComponent( toggleButton );
		}
		lineAxisPanel.addComponent( button );

		app.getFrame().getContentPane().addLineStartComponent( label );
		app.getFrame().getContentPane().addCenterComponent( lineAxisPanel );

		app.getFrame().setSize( 600, 64 );
		app.getFrame().setVisible( true );
	}
}
