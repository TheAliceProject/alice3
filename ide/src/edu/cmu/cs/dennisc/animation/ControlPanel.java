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
package edu.cmu.cs.dennisc.animation;

/**
 * @author Dennis Cosgrove
 */
public class ControlPanel extends javax.swing.JPanel {
	private static final String PAUSE_TEXT = "pause";
	private static final String RESUME_TEXT = "resume";

	private javax.swing.JButton pauseResume = new javax.swing.JButton( "pause" );
	private javax.swing.JLabel feedback = new javax.swing.JLabel( "speed: 1x" );
	private javax.swing.JSlider speed = new javax.swing.JSlider() {
		@Override
		public java.awt.Dimension getMaximumSize() {
			java.awt.Dimension rv = super.getMaximumSize();
			edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMaximumWidth( rv, 400 );
			return rv;
		}
	};
	private javax.swing.JButton restart = new javax.swing.JButton( "restart" );

	public ControlPanel( final Program program ) {

		this.speed.setMinimum( 1 );
		this.speed.setValue( 1 );
		this.speed.setMaximum( 10 );

		this.restart.setEnabled( program.isRestartSupported() );

		this.pauseResume.addActionListener( new java.awt.event.ActionListener() {
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				boolean isPaused = ControlPanel.this.isPaused();
				if( isPaused ) {
					ControlPanel.this.pauseResume.setText( PAUSE_TEXT );
					program.handleSpeedChange( ControlPanel.this.speed.getValue() );
				} else {
					ControlPanel.this.pauseResume.setText( RESUME_TEXT );
					program.handleSpeedChange( 0 );
				}
				ControlPanel.this.speed.setEnabled( isPaused );
				ControlPanel.this.feedback.setEnabled( isPaused );
				updateFeedback();
			}
		} );
		this.restart.addActionListener( new java.awt.event.ActionListener() {
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				program.restart( e );
			}
		} );

		this.speed.addChangeListener( new javax.swing.event.ChangeListener() {
			public void stateChanged( javax.swing.event.ChangeEvent e ) {
				ControlPanel.this.updateFeedback();
				program.handleSpeedChange( ControlPanel.this.speed.getValue() );
			}
		} );
		this.speed.addMouseListener( new java.awt.event.MouseListener() {
			public void mousePressed( java.awt.event.MouseEvent e ) {
			}
			public void mouseReleased( java.awt.event.MouseEvent e ) {
				if( edu.cmu.cs.dennisc.javax.swing.SwingUtilities.isQuoteControlUnquoteDown( e ) ) {
					//pass
				} else {
					ControlPanel.this.speed.setValue( 1 );
				}
			}
			public void mouseClicked( java.awt.event.MouseEvent e ) {
			}
			public void mouseEntered( java.awt.event.MouseEvent e ) {
			}
			public void mouseExited( java.awt.event.MouseEvent e ) {
			}
		} );

		javax.swing.Box box = javax.swing.Box.createHorizontalBox();
		box.add( this.speed );
		setLayout( new java.awt.GridBagLayout() );
		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		add( this.pauseResume, gbc );
		add( this.feedback, gbc );
		gbc.weightx = 1.0;
		add( box, gbc );
		gbc.weightx = 0.0;
		add( this.restart, gbc );
	}

	private boolean isPaused() {
		return this.pauseResume.getText().equals( RESUME_TEXT );
	}

	private void updateFeedback() {
		if( isPaused() ) {
			this.feedback.setText( "speed: 0x" );
		} else {
			this.feedback.setText( "speed: " + this.speed.getValue() + "x" );
		}
	}
}
