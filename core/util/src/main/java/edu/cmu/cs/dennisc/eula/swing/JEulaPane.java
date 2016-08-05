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
package edu.cmu.cs.dennisc.eula.swing;

/**
 * @author Dennis Cosgrove
 */
public class JEulaPane extends javax.swing.JPanel {
	public JEulaPane( String eulaText ) {
		javax.swing.JLabel headerLabel = new javax.swing.JLabel( "<html>Please read the following license agreement carefully.</html>" );

		javax.swing.JTextArea textArea = new javax.swing.JTextArea();
		textArea.setText( eulaText );
		textArea.setEditable( false );
		textArea.setLineWrap( true );
		textArea.setWrapStyleWord( true );

		javax.swing.JCheckBox reject = new javax.swing.JCheckBox();
		this.accept.setText( "I accept the terms in the License Agreement" );
		reject.setText( "I do not accept the terms in the License Agreement" );

		javax.swing.ButtonGroup group = new javax.swing.ButtonGroup();
		group.add( this.accept );
		group.add( reject );
		reject.setSelected( true );

		final javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane( textArea );
		scrollPane.setPreferredSize( new java.awt.Dimension( 480, 320 ) );

		headerLabel.setAlignmentX( java.awt.Component.LEFT_ALIGNMENT );
		scrollPane.setAlignmentX( java.awt.Component.LEFT_ALIGNMENT );
		this.accept.setAlignmentX( java.awt.Component.LEFT_ALIGNMENT );
		reject.setAlignmentX( java.awt.Component.LEFT_ALIGNMENT );

		this.accept.addChangeListener( this.changeListener );

		javax.swing.JPanel mainPanel = new javax.swing.JPanel();
		mainPanel.setLayout( new javax.swing.BoxLayout( mainPanel, javax.swing.BoxLayout.PAGE_AXIS ) );
		mainPanel.add( scrollPane );
		mainPanel.add( javax.swing.Box.createVerticalStrut( 4 ) );
		mainPanel.add( this.accept );
		mainPanel.add( reject );
		mainPanel.add( javax.swing.Box.createVerticalStrut( 8 ) );

		String okButtonText = javax.swing.UIManager.getString( "OptionPane.okButtonText" );
		String cancelButtonText = javax.swing.UIManager.getString( "OptionPane.cancelButtonText" );
		if( ( okButtonText != null ) && ( okButtonText.length() > 0 ) ) {
			//pass
		} else {
			okButtonText = "OK";
		}
		if( ( cancelButtonText != null ) && ( cancelButtonText.length() > 0 ) ) {
			//pass
		} else {
			cancelButtonText = "Cancel";
		}

		this.okButton = new javax.swing.JButton( new javax.swing.AbstractAction( okButtonText ) {
			@Override
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				handleOkOrCancel( true );
			}
		} );
		javax.swing.JButton cancelButton = new javax.swing.JButton( new javax.swing.AbstractAction( cancelButtonText ) {
			@Override
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				handleOkOrCancel( false );
			}
		} );

		this.okButton.setEnabled( false );

		javax.swing.JPanel controlPanel = new javax.swing.JPanel();
		controlPanel.setLayout( new java.awt.FlowLayout( java.awt.FlowLayout.TRAILING ) );
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isWindows() ) {
			controlPanel.add( okButton );
			controlPanel.add( cancelButton );
		} else {
			controlPanel.add( cancelButton );
			controlPanel.add( okButton );
		}
		//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( controlPanel.getComponentOrientation().isLeftToRight() );

		this.setLayout( new java.awt.BorderLayout() );
		this.add( headerLabel, java.awt.BorderLayout.PAGE_START );
		this.add( mainPanel, java.awt.BorderLayout.CENTER );
		this.add( controlPanel, java.awt.BorderLayout.PAGE_END );

		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				scrollPane.getVerticalScrollBar().setValue( 0 );
			}
		} );
	}

	@Override
	public void addNotify() {
		super.addNotify();
		this.getRootPane().setDefaultButton( this.okButton );
	}

	private void handleOkOrCancel( boolean isCommitted ) {
		this.isCommitted = isCommitted;
		javax.swing.SwingUtilities.getRoot( this ).setVisible( false );
	}

	public boolean isAccepted() {
		return this.isCommitted && this.accept.isSelected();
	}

	private final javax.swing.JButton okButton;
	private final javax.swing.event.ChangeListener changeListener = new javax.swing.event.ChangeListener() {
		@Override
		public void stateChanged( javax.swing.event.ChangeEvent e ) {
			okButton.setEnabled( accept.isSelected() );
		}
	};

	private final javax.swing.JCheckBox accept = new javax.swing.JCheckBox();
	private boolean isCommitted;

	public static void main( String[] args ) {
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				//				java.util.Locale locale = java.util.Locale.SIMPLIFIED_CHINESE;
				//				java.util.Locale.setDefault( locale );
				//				//javax.swing.JComponent.setDefaultLocale( locale );
				//				//javax.swing.JOptionPane.showConfirmDialog( null, "hello", "title", javax.swing.JOptionPane.OK_CANCEL_OPTION );
				edu.cmu.cs.dennisc.javax.swing.UIManagerUtilities.setLookAndFeel( "Nimbus" );
				JEulaPane eulaPane = new JEulaPane( "eulaText" );
				javax.swing.JDialog dialog = new edu.cmu.cs.dennisc.javax.swing.JDialogBuilder().isModal( true ).title( "title" ).build();
				dialog.getContentPane().add( eulaPane, java.awt.BorderLayout.CENTER );
				dialog.pack();
				dialog.setVisible( true );
				edu.cmu.cs.dennisc.java.util.logging.Logger.outln( eulaPane.isAccepted() );
				System.exit( 0 );
			}
		} );
	}
}
