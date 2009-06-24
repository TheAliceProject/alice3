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
			edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMaximumWidth( rv, 400 );
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
				if( edu.cmu.cs.dennisc.swing.SwingUtilities.isQuoteControlUnquoteDown( e ) ) {
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
