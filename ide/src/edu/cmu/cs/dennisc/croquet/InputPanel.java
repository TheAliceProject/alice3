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

package edu.cmu.cs.dennisc.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class InputPanel< T > extends Panel {
	public static interface Validator {
		public boolean isInputValid();
	}
	private java.util.List< Validator > validators = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private javax.swing.JDialog dialog;
	private boolean isOK = false;

	public void addValidator( Validator validator ) {
		this.validators.add( validator );
	}
	public void removeValidator( Validator validator ) {
		this.validators.remove( validator );
	}

	private javax.swing.JButton okButton;
	public boolean isOKButtonValid() {
		for( Validator validator : validators ) {
			if( validator.isInputValid() ) {
				//pass
			} else {
				return false;
			}
		}
		return true;
	}
	public void updateOKButton() {
		if( this.okButton != null ) {
			this.okButton.setEnabled( isOKButtonValid() );
		}
	}
//	@Deprecated
//	public void fireOKButtonIfPossible() {
//		if( this.okButton != null ) {
//			if( this.okButton.isEnabled() ) {
//				this.okButton.doClick();
//			}
//		}
//	}

	protected boolean isDisposeDesired( java.awt.event.WindowEvent e ) {
		return true;
	}
	protected boolean isCancelDesired() {
		return true;
	}

	public void setOKButton( javax.swing.JButton okButton ) {
		this.okButton = okButton;
		updateOKButton();
	}
	protected void setOK( boolean isOK ) {
		this.isOK = isOK;
	}
	protected abstract T getActualInputValue();
	private final T getInputValue() {
		if( this.isOK ) {
			return getActualInputValue();
		} else {
			return null;
		}
	}
	
	public T showInJDialog( AbstractButton< ? > button, String title ) {
		final javax.swing.JDialog dialog;
		
		
		java.awt.Component root;
		if( button != null ) {
			root = javax.swing.SwingUtilities.getRoot( button.getJComponent() );
		} else {
			root = null;
		}
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
		boolean isModal = true;
		dialog.setTitle( title );
		dialog.setModal( isModal );
		class OKAction extends javax.swing.AbstractAction {
			public OKAction() {
				this.putValue( javax.swing.Action.NAME, "OK" );
			}
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				InputPanel.this.setOK( true );
				dialog.setVisible( false );
			}
		}
		class CancelAction extends javax.swing.AbstractAction {
			public CancelAction() {
				this.putValue( javax.swing.Action.NAME, "Cancel" );
			}
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				InputPanel.this.setOK( false );
				dialog.setVisible( false );
			}
		}

		
		javax.swing.JButton okButton = new javax.swing.JButton( new OKAction() );
		javax.swing.JButton cancelButton = new javax.swing.JButton( new CancelAction() );
		javax.swing.JPanel panel = new javax.swing.JPanel();
		panel.setBackground( this.getJComponent().getBackground() );
		panel.add( okButton );
		
		if( this.isCancelDesired() ) {
			panel.add( cancelButton );
		} else {
			dialog.setDefaultCloseOperation( javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE );
			dialog.addWindowListener( new java.awt.event.WindowListener() {
				public void windowActivated( java.awt.event.WindowEvent e ) {
				}
				public void windowDeactivated( java.awt.event.WindowEvent e ) {
				}
				public void windowIconified( java.awt.event.WindowEvent e ) {
				}
				public void windowDeiconified( java.awt.event.WindowEvent e ) {
				}
				public void windowOpened( java.awt.event.WindowEvent e ) {
				}
				public void windowClosing( java.awt.event.WindowEvent e ) {
					if( InputPanel.this.isDisposeDesired( e ) ) {
						e.getWindow().dispose();
					}
				}
				public void windowClosed( java.awt.event.WindowEvent e ) {
				}
			} );
		}

		dialog.getRootPane().setDefaultButton( okButton );
		dialog.getContentPane().add( this.getJComponent(), java.awt.BorderLayout.CENTER );
		dialog.getContentPane().add( panel, java.awt.BorderLayout.SOUTH );

		this.setOKButton( okButton );
		this.isOK = false;

		this.getJComponent().revalidate();
		dialog.pack();
		
		edu.cmu.cs.dennisc.java.awt.WindowUtilties.setLocationOnScreenToCenteredWithin( dialog, root );

		assert this.dialog == null;
		this.dialog = dialog;
		try {
			this.dialog.setVisible( true );
			T rv = getInputValue();
			return rv;
		} finally {
			this.setOKButton( null );
			this.dialog = null;
			//todo?
			//this.m_isOK = false;
		}
	}
	
	@Deprecated
	public T showInJDialog( AbstractButton< ? > button ) {
		return this.showInJDialog( button, null );
	}
	@Deprecated
	public T showInJDialog() {
		return this.showInJDialog( null );
	}
	@Deprecated
	protected void addComponent( Component<?> component, Object constraints ) {
		assert component != null;
		this.getJComponent().add( component.getJComponent(), constraints );
	}
	public void removeComponent( Component< ? > component ) {
		this.internalRemoveComponent( component );
	}
}
