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

import edu.cmu.cs.dennisc.java.awt.GraphicsUtilities;
import edu.cmu.cs.dennisc.java.awt.event.InputEventUtilities;
import edu.cmu.cs.dennisc.java.awt.font.FontUtilities;
import edu.cmu.cs.dennisc.java.awt.font.TextFamily;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.ButtonModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author Dennis Cosgrove
 */
public class ProgramControlPanel extends JPanel {
	public ProgramControlPanel( final ProgramImp programImp ) {
		final ButtonModel buttonModel = new JToggleButton.ToggleButtonModel();
		buttonModel.setSelected( true );
		buttonModel.addChangeListener( new ChangeListener() {
			@Override
			public void stateChanged( ChangeEvent e ) {
				programImp.getAnimator().setSpeedFactor( buttonModel.isSelected() ? 1.0 : 0.0 );
			}
		} );

		JButton playPauseButton = new JButton();
		playPauseButton.setModel( buttonModel );
		playPauseButton.setIcon( new Icon() {
			@Override
			public int getIconWidth() {
				return 12;
			}

			@Override
			public int getIconHeight() {
				return 12;
			}

			@Override
			public void paintIcon( Component c, Graphics g, int x, int y ) {
				AbstractButton toggleButton = (AbstractButton)c;
				ButtonModel buttonModel = toggleButton.getModel();
				if( buttonModel.isSelected() ) {
					g.fillRect( x + 1, y + 1, 3, 10 );
					g.fillRect( x + 7, y + 1, 3, 10 );
				} else {
					GraphicsUtilities.fillTriangle( g, GraphicsUtilities.Heading.EAST, x, y, 12, 12 );
				}
			}
		} );

		final int PAD_X = 12;
		final int PAD_Y = 8;
		playPauseButton.setBorder( BorderFactory.createEmptyBorder( PAD_Y, PAD_X, PAD_Y, PAD_X ) );

		FontUtilities.setFontToDerivedFont( label, TextFamily.MONOSPACED );

		boundedRangeModel.setMinimum( 1 );
		boundedRangeModel.setMaximum( 8 );
		boundedRangeModel.addChangeListener( new ChangeListener() {
			@Override
			public void stateChanged( ChangeEvent e ) {
				ProgramControlPanel.this.updateLabel(programImp.getSpeedFormat());
				programImp.handleSpeedChange( boundedRangeModel.getValue() );
			}
		} );
		this.updateLabel(programImp.getSpeedFormat());

		JSlider slider = new JSlider( boundedRangeModel );
		slider.addMouseListener( new MouseListener() {
			@Override
			public void mousePressed( MouseEvent e ) {
			}

			@Override
			public void mouseReleased( MouseEvent e ) {
				if( InputEventUtilities.isQuoteControlUnquoteDown( e ) ) {
					//pass
				} else {
					ProgramControlPanel.this.boundedRangeModel.setValue( 1 );
				}
			}

			@Override
			public void mouseClicked( MouseEvent e ) {
			}

			@Override
			public void mouseEntered( MouseEvent e ) {
			}

			@Override
			public void mouseExited( MouseEvent e ) {
			}
		} );

		final int LARGE_PAD = 8;
		final int SMALL_PAD = 2;
		this.setLayout( new GridBagLayout() );

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.0;
		this.add( playPauseButton, gbc );
		gbc.insets.left = LARGE_PAD;
		this.add( this.label, gbc );
		gbc.weightx = 1.0;
		gbc.insets.left = SMALL_PAD;
		this.add( slider, gbc );
		gbc.weightx = 0.0;
		gbc.insets.left = LARGE_PAD;
		Action restartAction = programImp.getRestartAction();
		if( restartAction != null ) {
			this.add( new JButton( restartAction ), gbc );
		}
		Action toggleFullScreenAction = programImp.getToggleFullScreenAction();
		if( toggleFullScreenAction != null ) {
			gbc.insets.left = 0;//SMALL_PAD;
			this.add( new JToggleButton( toggleFullScreenAction ), gbc );
		}

		for( Component awtComponent : this.getComponents() ) {
			awtComponent.setFocusable( false );
		}
	}

	private void updateLabel(String speedFormat) {
		label.setText(String.format(speedFormat, boundedRangeModel.getValue()));
	}

	private final JLabel label = new JLabel();
	private final BoundedRangeModel boundedRangeModel = new DefaultBoundedRangeModel();
}
