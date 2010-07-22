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

package edu.cmu.cs.dennisc.inputpane;

/**
 * @author Dennis Cosgrove
 */
public abstract class InputPane<E> extends javax.swing.JPanel {
	private java.util.List< edu.cmu.cs.dennisc.pattern.Validator > m_validators = new java.util.LinkedList< edu.cmu.cs.dennisc.pattern.Validator >();
	private javax.swing.JButton m_okButton;
	
	private javax.swing.JDialog m_dialog;

	public InputPane() {
	}
	public InputPane( edu.cmu.cs.dennisc.pattern.Validator... validators ) {
		for( edu.cmu.cs.dennisc.pattern.Validator validator : validators ) {
			m_validators.add( validator );
		}
	}
	
	public void updateSizeIfNecessary() {
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				for( java.awt.Component component : InputPane.this.getComponents() ) {
					component.invalidate();
				}
				InputPane.this.revalidate();
				if( m_dialog != null ) {
					java.awt.Dimension actualSize = m_dialog.getSize();
					java.awt.Dimension preferredSize = m_dialog.getPreferredSize();
					if( ( actualSize.width < preferredSize.width ) || ( actualSize.height < preferredSize.height ) ) {
						m_dialog.setSize( Math.max( actualSize.width, preferredSize.width ), Math.max( actualSize.height, preferredSize.height ) );
					}
				}
			}
		} );
	}

	public void addOKButtonValidator( edu.cmu.cs.dennisc.pattern.Validator validator ) {
		synchronized( m_validators ) {
			m_validators.add( validator );
		}
	}
	public void removeOKButtonValidator( edu.cmu.cs.dennisc.pattern.Validator validator ) {
		synchronized( m_validators ) {
			m_validators.remove( validator );
		}
	}
	public Iterable< edu.cmu.cs.dennisc.pattern.Validator > getOKButtonValidators() {
		synchronized( m_validators ) {
			return m_validators;
		}
	}
	public boolean isOKButtonValid() {
		synchronized( m_validators ) {
			for( edu.cmu.cs.dennisc.pattern.Validator validator : m_validators ) {
				if( validator.isValid() ) {
					//pass
				} else {
					return false;
				}
			}
			return true;
		}
	}

	public void updateOKButton() {
		if( m_okButton != null ) {
			m_okButton.setEnabled( isOKButtonValid() );
		}
	}

	public void fireOKButtonIfPossible() {
		if( m_okButton != null ) {
			if( m_okButton.isEnabled() ) {
				m_okButton.doClick();
			}
		}
	}

	public void setOKButton( javax.swing.JButton okButton ) {
		m_okButton = okButton;
		updateOKButton();
	}

	private boolean m_isOK = false;

	protected void setOK( boolean isOK ) {
		m_isOK = isOK;
	}
	
	protected abstract E getActualInputValue();

	public final E getInputValue() {
		if( m_isOK ) {
			return getActualInputValue();
		} else {
			return null;
		}
	}
	
//	private static java.awt.Frame defaultOwnerFrame;
//	public static java.awt.Frame getDefaultOwnerFrame() {
//		return InputPane.defaultOwnerFrame;
//	}
//	public static void setDefaultOwnerFrame( java.awt.Frame defaultOwnerFrame ) {
//		if( InputPane.defaultOwnerFrame != null ) {
//		} else {
//			InputPane.defaultOwnerFrame = new javax.swing.JFrame();
//			//InputPane.defaultOwnerFrame.show();
//		}
//		InputPane.defaultOwnerFrame = defaultOwnerFrame;
//	}

	public E showInJDialog( java.awt.Component ownerComponent, String title, boolean isModal ) {
		final javax.swing.JDialog dialog;
		java.awt.Component root = javax.swing.SwingUtilities.getRoot( ownerComponent );
		if( root instanceof java.awt.Frame ) {
			dialog = new javax.swing.JDialog( (java.awt.Frame)root );
		} else if( root instanceof java.awt.Dialog ) {
			dialog = new javax.swing.JDialog( (java.awt.Dialog)root );
		} else {
			dialog = new javax.swing.JDialog();
//			java.awt.Frame owner = getDefaultOwnerFrame();
//			if( owner != null ) {
//				dialog = new javax.swing.JDialog( owner, title, isModal );
//			} else {
//			}
		}
//		java.awt.Frame ownerFrame = SwingUtilities.getRootFrame( ownerComponent );
//		if( ownerFrame != null ) {
//			dialog = new javax.swing.JDialog( ownerFrame, title, isModal );
//		} else {
//			dialog = new javax.swing.JDialog( SwingUtilities.getRootDialog( ownerComponent ), title, isModal );
//		}
		dialog.setTitle( title );
		dialog.setModal( isModal );

		javax.swing.JButton okButton = new javax.swing.JButton( "OK" );
		okButton.addActionListener( new java.awt.event.ActionListener() {
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				InputPane.this.setOK( true );
				dialog.setVisible( false );
			}
		} );
		javax.swing.JButton cancelButton = new javax.swing.JButton( "Cancel" );
		cancelButton.addActionListener( new java.awt.event.ActionListener() {
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				InputPane.this.setOK( false );
				dialog.setVisible( false );
			}
		} );

		javax.swing.JPanel panel = new javax.swing.JPanel();
		panel.add( okButton );
		panel.add( cancelButton );

		dialog.getContentPane().add( this, java.awt.BorderLayout.CENTER );
		dialog.getContentPane().add( panel, java.awt.BorderLayout.SOUTH );

		this.setOKButton( okButton );
		this.m_isOK = false;

		this.revalidate();
		dialog.pack();

		dialog.pack();
		//todo:
		java.awt.Dimension size = dialog.getSize();
		size.width = Math.max( size.width, 480 );
		//size.height = Math.max( size.height, 360 );
		size.width += 8;
		size.height += 8;
		dialog.setSize( size );

		assert m_dialog == null;
		m_dialog = dialog;
		try {
			m_dialog.setVisible( true );
			E rv = getInputValue();
			return rv;
		} finally {
			this.setOKButton( null );
			m_dialog = null;
			//todo?
			//this.m_isOK = false;
		}
	}
	public E showInJDialog( java.awt.Component ownerComponent, String title ) {
		return showInJDialog( ownerComponent, title, true );
	}
	public E showInJDialog( java.awt.Component ownerComponent ) {
		return showInJDialog( ownerComponent, null );
	}
}
