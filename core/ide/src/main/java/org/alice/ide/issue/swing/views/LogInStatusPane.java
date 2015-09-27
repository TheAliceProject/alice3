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
package org.alice.ide.issue.swing.views;

class PasswordPane extends edu.cmu.cs.dennisc.javax.swing.components.JPageAxisPane {
	private static final String HIDDEN_KEY = "HIDDEN_KEY";
	private static final String EXPOSED_KEY = "EXPOSED_KEY";

	class PasswordCardPane extends edu.cmu.cs.dennisc.javax.swing.components.JCardPane {
		private javax.swing.JPasswordField hidden = new javax.swing.JPasswordField() {
			@Override
			public java.awt.Dimension getPreferredSize() {
				return edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMinimumWidth( super.getPreferredSize(), 256 );
			}
		};
		private javax.swing.JTextField exposed = new javax.swing.JTextField();
		private boolean isExposed = false;

		public PasswordCardPane() {
			this.add( hidden, HIDDEN_KEY );
			this.add( exposed, EXPOSED_KEY );
			this.exposed.setDocument( this.hidden.getDocument() );
		}

		public void toggle() {
			if( this.isExposed ) {
				this.show( HIDDEN_KEY );
				this.hidden.requestFocus();
			} else {
				this.show( EXPOSED_KEY );
				this.exposed.requestFocus();
			}
			this.isExposed = !this.isExposed;
		}
	}

	private PasswordCardPane passwordCardPane = new PasswordCardPane();
	private javax.swing.JCheckBox checkBox = new javax.swing.JCheckBox( "display password" );

	public PasswordPane() {
		checkBox.addActionListener( new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				passwordCardPane.toggle();
			}
		} );
		this.add( passwordCardPane );
		this.add( checkBox );
	}

	public String getPassword() {
		return passwordCardPane.exposed.getText();
	}
}

class LogInPane extends edu.cmu.cs.dennisc.javax.swing.components.JPageAxisPane {
	class TestLogInOperation extends org.alice.ide.operations.InconsequentialActionOperation {
		public TestLogInOperation() {
			super( java.util.UUID.fromString( "cf700b82-c80b-4fb4-8886-2d170503a253" ) );
		}

		@Override
		protected void localize() {
			super.localize();
			this.setName( "Log In" );
		}

		@Override
		protected void performInternal( org.lgna.croquet.history.CompletionStep<?> step ) {
			try {
				com.atlassian.jira.rpc.soap.client.JiraSoapServiceServiceLocator jiraSoapServiceLocator = new com.atlassian.jira.rpc.soap.client.JiraSoapServiceServiceLocator();
				com.atlassian.jira.rpc.soap.client.JiraSoapService service = jiraSoapServiceLocator.getJirasoapserviceV2( new java.net.URL( "http://bugs.alice.org:8080/rpc/soap/jirasoapservice-v2" ) );
				String username = textUsername.getText();
				try {
					String password = passwordPane.getPassword();
					String token = service.login( username, password );
					try {
						com.atlassian.jira.rpc.soap.client.RemoteUser remoteUser = service.getUser( token, username );
						edu.cmu.cs.dennisc.login.AccountManager.logIn( LogInStatusPane.BUGS_ALICE_ORG_KEY, username, password, remoteUser.getFullname() );
					} finally {
						service.logout( token );
					}
					javax.swing.SwingUtilities.getRoot( LogInPane.this ).setVisible( false );
				} catch( com.atlassian.jira.rpc.soap.client.RemoteAuthenticationException rae ) {
					javax.swing.JOptionPane.showMessageDialog( null, rae );
					//edu.cmu.cs.dennisc.account.AccountManager.logOut( BUGS_ALICE_ORG_KEY );
				}
			} catch( Exception e ) {
				throw new RuntimeException( e );
			}
		}
	}

	private javax.swing.JTextField textUsername = new javax.swing.JTextField();
	private PasswordPane passwordPane = new PasswordPane();
	//todo: remove. rely only on operations.
	private javax.swing.JButton logInButton = new TestLogInOperation().createButton().getAwtComponent();

	private java.awt.Component createLabel( String text ) {
		javax.swing.JLabel rv = edu.cmu.cs.dennisc.javax.swing.LabelUtilities.createLabel( text );
		rv.setVerticalAlignment( javax.swing.SwingConstants.TOP );
		rv.setHorizontalAlignment( javax.swing.SwingConstants.TRAILING );
		return rv;
	}

	public LogInPane() {
		edu.cmu.cs.dennisc.javax.swing.components.JRowsSpringPane rowsPane = new edu.cmu.cs.dennisc.javax.swing.components.JRowsSpringPane( 8, 4 ) {
			@Override
			protected java.util.List<java.awt.Component[]> addComponentRows( java.util.List<java.awt.Component[]> rv ) {
				rv.add( edu.cmu.cs.dennisc.javax.swing.SpringUtilities.createRow( createLabel( "Username:" ), textUsername ) );
				rv.add( edu.cmu.cs.dennisc.javax.swing.SpringUtilities.createRow( createLabel( "Password:" ), passwordPane ) );
				return rv;
			}
		};

		edu.cmu.cs.dennisc.javax.swing.components.JPane signUpPane = new edu.cmu.cs.dennisc.javax.swing.components.JPane();
		signUpPane.add( edu.cmu.cs.dennisc.javax.swing.LabelUtilities.createLabel( "Not a member?" ) );
		org.alice.ide.croquet.models.help.SignUpOperation signUpOperation = new org.alice.ide.croquet.models.help.SignUpOperation();
		signUpPane.add( signUpOperation.createHyperlink().getAwtComponent() );
		signUpPane.add( edu.cmu.cs.dennisc.javax.swing.LabelUtilities.createLabel( "for an account." ) );

		edu.cmu.cs.dennisc.javax.swing.components.JPane buttonPane = new edu.cmu.cs.dennisc.javax.swing.components.JPane();
		buttonPane.add( this.logInButton );

		signUpPane.setAlignmentX( javax.swing.JComponent.CENTER_ALIGNMENT );
		rowsPane.setAlignmentX( javax.swing.JComponent.CENTER_ALIGNMENT );
		buttonPane.setAlignmentX( javax.swing.JComponent.CENTER_ALIGNMENT );

		this.add( signUpPane );
		this.add( javax.swing.Box.createVerticalStrut( 32 ) );
		this.add( rowsPane );
		this.add( javax.swing.Box.createVerticalStrut( 6 ) );
		this.add( buttonPane );

		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 32, 8, 32 ) );
	}

	public javax.swing.JButton getLogInButton() {
		return this.logInButton;
	}
}

public class LogInStatusPane extends edu.cmu.cs.dennisc.javax.swing.components.JCardPane {
	public static final String BUGS_ALICE_ORG_KEY = "bugs.alice.org";

	class LogInOperation extends org.alice.ide.operations.InconsequentialActionOperation {
		public LogInOperation() {
			super( java.util.UUID.fromString( "f2d620ad-9b18-42e7-8b77-240e7a829b03" ) );
			this.setName( "Log In... (Optional)" );
		}

		@Override
		protected void performInternal( org.lgna.croquet.history.CompletionStep<?> step ) {
			LogInPane pane = new LogInPane();
			org.lgna.croquet.triggers.Trigger trigger = step.getTrigger();
			java.awt.Component owner = trigger.getViewController().getAwtComponent();
			javax.swing.JDialog dialog = edu.cmu.cs.dennisc.javax.swing.JDialogUtilities.createPackedJDialog( pane, owner, "Log In", true, javax.swing.WindowConstants.DISPOSE_ON_CLOSE );
			edu.cmu.cs.dennisc.java.awt.WindowUtilities.setLocationOnScreenToCenteredWithin( dialog, javax.swing.SwingUtilities.getRoot( owner ) );
			dialog.getRootPane().setDefaultButton( pane.getLogInButton() );
			dialog.setVisible( true );
			edu.cmu.cs.dennisc.login.AccountInformation accountInformation = edu.cmu.cs.dennisc.login.AccountManager.get( LogInStatusPane.BUGS_ALICE_ORG_KEY );
			if( accountInformation != null ) {
				LogInStatusPane.this.onPane.refresh();
				LogInStatusPane.this.show( ON_KEY );
			}
		}
	}

	class LogOutOperation extends org.alice.ide.operations.InconsequentialActionOperation {
		public LogOutOperation() {
			super( java.util.UUID.fromString( "73bf08cc-3666-463d-86da-3d483a4d8f2b" ) );
			this.setName( "Log Out" );
		}

		@Override
		protected void performInternal( org.lgna.croquet.history.CompletionStep<?> step ) {
			edu.cmu.cs.dennisc.login.AccountManager.logOut( LogInStatusPane.BUGS_ALICE_ORG_KEY );
			LogInStatusPane.this.show( OFF_KEY );
		}
	}

	private static final String OFF_KEY = "OFF_KEY";
	private static final String ON_KEY = "ON_KEY";
	private javax.swing.JButton logInButton = new LogInOperation().createButton().getAwtComponent();
	private javax.swing.JButton logOutButton = new LogOutOperation().createButton().getAwtComponent();

	class OffPane extends edu.cmu.cs.dennisc.javax.swing.components.JLineAxisPane {
		public OffPane() {
			this.add( javax.swing.Box.createHorizontalGlue() );
			this.add( logInButton );
		}
	}

	class OnPane extends edu.cmu.cs.dennisc.javax.swing.components.JLineAxisPane {
		private javax.swing.JLabel nameLabel = new javax.swing.JLabel( "Full Name" ) {
			@Override
			public java.awt.Dimension getPreferredSize() {
				return edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMinimumWidth( super.getPreferredSize(), 320 );
			}

			@Override
			public java.awt.Dimension getMaximumSize() {
				return this.getPreferredSize();
			}
		};

		public OnPane() {
			this.refresh();
			this.nameLabel.setHorizontalAlignment( javax.swing.SwingConstants.TRAILING );
			this.nameLabel.setForeground( java.awt.Color.WHITE );
			this.add( javax.swing.Box.createHorizontalGlue() );
			this.add( this.nameLabel );
			this.add( javax.swing.Box.createHorizontalStrut( 8 ) );
			this.add( logOutButton );
		}

		public void refresh() {
			edu.cmu.cs.dennisc.login.AccountInformation accountInformation = edu.cmu.cs.dennisc.login.AccountManager.get( LogInStatusPane.BUGS_ALICE_ORG_KEY );
			if( accountInformation != null ) {
				this.nameLabel.setText( accountInformation.getFullName() );
				this.revalidate();
			}
		}
	}

	private OffPane offPane = new OffPane();
	private OnPane onPane = new OnPane();

	public LogInStatusPane() {
		this.add( this.offPane, OFF_KEY );
		this.add( this.onPane, ON_KEY );
		edu.cmu.cs.dennisc.login.AccountInformation accountInformation = edu.cmu.cs.dennisc.login.AccountManager.get( LogInStatusPane.BUGS_ALICE_ORG_KEY );
		if( accountInformation != null ) {
			LogInStatusPane.this.show( ON_KEY );
		}
	}

	//	@Override
	//	public void show( String key ) {
	//		super.show( key );
	//		this.revalidate();
	//	}
	public static void main( String[] args ) {
		LogInStatusPane pane = new LogInStatusPane();
		//LogInPane pane = new LogInPane();
		//PasswordPane pane = new PasswordPane();
		javax.swing.JDialog dialog = edu.cmu.cs.dennisc.javax.swing.JDialogUtilities.createPackedJDialog( pane, null, "", true, javax.swing.WindowConstants.DISPOSE_ON_CLOSE );
		dialog.getContentPane().setBackground( java.awt.Color.DARK_GRAY );
		dialog.setLocation( 200, 200 );
		dialog.setVisible( true );
	}
}
