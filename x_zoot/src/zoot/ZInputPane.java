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

package zoot;

/**
 * @author Dennis Cosgrove
 */
public abstract class ZInputPane<E> extends javax.swing.JPanel {
	private java.util.List< InputValidator > m_validators = new java.util.LinkedList< InputValidator >();
	private ZButton m_okButton;
	
	private javax.swing.JDialog m_dialog;

	public ZInputPane() {
	}
	public ZInputPane( InputValidator... validators ) {
		for( InputValidator validator : validators ) {
			m_validators.add( validator );
		}
	}
	
	public void updateSizeIfNecessary() {
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				for( java.awt.Component component : ZInputPane.this.getComponents() ) {
					component.invalidate();
				}
				ZInputPane.this.revalidate();
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

	public void addOKButtonValidator( InputValidator validator ) {
		synchronized( m_validators ) {
			m_validators.add( validator );
		}
	}
	public void removeOKButtonValidator( InputValidator validator ) {
		synchronized( m_validators ) {
			m_validators.remove( validator );
		}
	}
	public Iterable< InputValidator > getOKButtonValidators() {
		synchronized( m_validators ) {
			return m_validators;
		}
	}
	public boolean isOKButtonValid() {
		synchronized( m_validators ) {
			for( InputValidator validator : m_validators ) {
				if( validator.isInputValid() ) {
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

	public void setOKButton( ZButton okButton ) {
		m_okButton = okButton;
		updateOKButton();
	}

	private boolean m_isOK = false;

	protected void setOK( boolean isOK ) {
		m_isOK = isOK;
	}
	
	protected abstract E getActualInputValue();

	private final E getInputValue() {
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

		class OKOperation extends AbstractActionOperation {
			public OKOperation() {
				this.putValue( javax.swing.Action.NAME, "OK" );
			}
			public void perform( zoot.ActionContext actionContext ) {
				ZInputPane.this.setOK( true );
				dialog.setVisible( false );
			}
		}
		class CancelOperation extends AbstractActionOperation {
			public CancelOperation() {
				this.putValue( javax.swing.Action.NAME, "Cancel" );
			}
			public void perform( zoot.ActionContext actionContext ) {
				ZInputPane.this.setOK( false );
				dialog.setVisible( false );
			}
		}
		ZButton okButton = new ZButton( new OKOperation() );

		javax.swing.JPanel panel = new javax.swing.JPanel();
		panel.setBackground( this.getBackground() );
		panel.add( okButton );

		if( this.isCancelDesired() ) {
			ZButton cancelButton = new ZButton( new CancelOperation() );
			panel.add( cancelButton );
		} else {
			dialog.setDefaultCloseOperation( javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE );
		}

		dialog.getRootPane().setDefaultButton( okButton );
		dialog.getContentPane().add( this, java.awt.BorderLayout.CENTER );
		dialog.getContentPane().add( panel, java.awt.BorderLayout.SOUTH );

		this.setOKButton( okButton );
		this.m_isOK = false;

		this.revalidate();
		dialog.pack();
		
		edu.cmu.cs.dennisc.awt.WindowUtilties.setLocationOnScreenToCenteredWithin( dialog, root );

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
	protected boolean isCancelDesired() {
		return true;
	}
	protected boolean isModalDefault() {
		return true;
	}
	public E showInJDialog( java.awt.Component ownerComponent, String title ) {
		return showInJDialog( ownerComponent, title, this.isModalDefault() );
	}
	protected String getTitleDefault() {
		return null;
	}
	public E showInJDialog( java.awt.Component ownerComponent ) {
		return showInJDialog( ownerComponent, this.getTitleDefault() );
	}
}
