/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.lgna.story.implementation;

/**
 * @author Dennis Cosgrove
 */
public class ProgramControlPanel extends javax.swing.JPanel {
	public ProgramControlPanel( final ProgramImp programImp ) {
		final javax.swing.ButtonModel buttonModel = new javax.swing.JToggleButton.ToggleButtonModel();
		buttonModel.setSelected( true );
		buttonModel.addChangeListener( new javax.swing.event.ChangeListener() {
			@Override
			public void stateChanged( javax.swing.event.ChangeEvent e ) {
				programImp.getAnimator().setSpeedFactor( buttonModel.isSelected() ? 1.0 : 0.0 );
			}
		} );

		javax.swing.JButton playPauseButton = new javax.swing.JButton();
		playPauseButton.setModel( buttonModel );
		playPauseButton.setIcon( new javax.swing.Icon() {
			@Override
			public int getIconWidth() {
				return 12;
			}

			@Override
			public int getIconHeight() {
				return 12;
			}

			@Override
			public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
				javax.swing.AbstractButton toggleButton = (javax.swing.AbstractButton)c;
				javax.swing.ButtonModel buttonModel = toggleButton.getModel();
				if( buttonModel.isSelected() ) {
					g.fillRect( x + 1, y + 1, 3, 10 );
					g.fillRect( x + 7, y + 1, 3, 10 );
				} else {
					edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.fillTriangle( g, edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.Heading.EAST, x, y, 12, 12 );
				}
			}
		} );

		final int PAD_X = 12;
		final int PAD_Y = 8;
		playPauseButton.setBorder( javax.swing.BorderFactory.createEmptyBorder( PAD_Y, PAD_X, PAD_Y, PAD_X ) );

		edu.cmu.cs.dennisc.java.awt.font.FontUtilities.setFontToDerivedFont( label, edu.cmu.cs.dennisc.java.awt.font.TextFamily.MONOSPACED );

		boundedRangeModel.setMinimum( 1 );
		boundedRangeModel.setMaximum( 8 );
		boundedRangeModel.addChangeListener( new javax.swing.event.ChangeListener() {
			@Override
			public void stateChanged( javax.swing.event.ChangeEvent e ) {
				ProgramControlPanel.this.updateLabel();
				programImp.handleSpeedChange( boundedRangeModel.getValue() );
			}
		} );
		this.updateLabel();

		javax.swing.JSlider slider = new javax.swing.JSlider( boundedRangeModel );
		slider.addMouseListener( new java.awt.event.MouseListener() {
			@Override
			public void mousePressed( java.awt.event.MouseEvent e ) {
			}

			@Override
			public void mouseReleased( java.awt.event.MouseEvent e ) {
				if( edu.cmu.cs.dennisc.java.awt.event.InputEventUtilities.isQuoteControlUnquoteDown( e ) ) {
					//pass
				} else {
					ProgramControlPanel.this.boundedRangeModel.setValue( 1 );
				}
			}

			@Override
			public void mouseClicked( java.awt.event.MouseEvent e ) {
			}

			@Override
			public void mouseEntered( java.awt.event.MouseEvent e ) {
			}

			@Override
			public void mouseExited( java.awt.event.MouseEvent e ) {
			}
		} );

		final int LARGE_PAD = 8;
		final int SMALL_PAD = 2;
		this.setLayout( new java.awt.GridBagLayout() );

		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		gbc.weightx = 0.0;
		this.add( playPauseButton, gbc );
		gbc.insets.left = LARGE_PAD;
		this.add( this.label, gbc );
		gbc.weightx = 1.0;
		gbc.insets.left = SMALL_PAD;
		this.add( slider, gbc );
		gbc.weightx = 0.0;
		gbc.insets.left = LARGE_PAD;
		javax.swing.Action restartAction = programImp.getRestartAction();
		if( restartAction != null ) {
			this.add( new javax.swing.JButton( restartAction ), gbc );
		}
		javax.swing.Action toggleFullScreenAction = programImp.getToggleFullScreenAction();
		if( toggleFullScreenAction != null ) {
			gbc.insets.left = 0;//SMALL_PAD;
			this.add( new javax.swing.JToggleButton( toggleFullScreenAction ), gbc );
		}

		for( java.awt.Component awtComponent : this.getComponents() ) {
			awtComponent.setFocusable( false );
		}
	}

	private final void updateLabel() {
		StringBuilder sb = new StringBuilder();
		sb.append( "speed: " );
		sb.append( this.boundedRangeModel.getValue() );
		sb.append( "x" );
		this.label.setText( sb.toString() );
	}

	private final javax.swing.JLabel label = new javax.swing.JLabel();
	private final javax.swing.BoundedRangeModel boundedRangeModel = new javax.swing.DefaultBoundedRangeModel();
}
