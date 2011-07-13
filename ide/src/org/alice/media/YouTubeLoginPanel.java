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
package org.alice.media;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jdesktop.swingworker.SwingWorker;

import com.google.gdata.util.AuthenticationException;

/**
 * @author David Culyba
 */
public class YouTubeLoginPanel extends JPanel implements ActionListener, DocumentListener{
	private static final Color ERROR_COLOR = Color.RED;
	private static final Color HAPPY_COLOR = new Color(0.0f, .6f, 0.0f);
	private static final Color NEUTRAL_COLOR = Color.GRAY;
	
	private static final String SUCCESS_MESSAGE = "Logged in as: ";
	
	private static final String LOG_IN = "LOG_IN";
	private static final String LOGGED_IN = "LOGGED_IN";
	
	private JPanel loginPanel;
	private JTextField userNameField;
	private JPasswordField passwordField;
	private JButton loginButton;
	private JLabel loginStatus;
	
	private YouTubeUploader youTubeUploader;
	
	private JPanel loggedInPanel;
	private JButton logoutButton;
	private JLabel loggedInStatus;
	
	private List<YouTubeListener> listeners = new LinkedList<YouTubeListener>();
	
	private boolean isLoggedIn = false;
	
	private CardLayout cardLayout;
	
	public YouTubeLoginPanel(YouTubeUploader youTubeUploader)
	{
		this.youTubeUploader = youTubeUploader;
		
		this.loginPanel = new JPanel();
		this.loginPanel.setLayout( new GridBagLayout() );
		
		this.userNameField = new JTextField(24);
        this.userNameField.setText("culyba@gmail.com");
		this.passwordField = new JPasswordField(24);
		this.loginButton = new JButton("Login");
		this.loginStatus = new JLabel("Not logged in.");
		this.loginStatus.setForeground( NEUTRAL_COLOR );
		
		int gridY = 0;
		this.loginPanel.add( new JLabel("Username: "), 
				new GridBagConstraints( 
				0, //gridX
				gridY, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.NORTHEAST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 2, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		this.loginPanel.add( this.userNameField, 
				new GridBagConstraints( 
				1, //gridX
				gridY++, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.NORTHWEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 2, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		this.loginPanel.add( new JLabel("Password: "), 
				new GridBagConstraints( 
				0, //gridX
				gridY, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.NORTHEAST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 2, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		this.loginPanel.add( this.passwordField, 
				new GridBagConstraints( 
				1, //gridX
				gridY++, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.NORTHWEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 2, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		this.loginPanel.add( this.loginButton, 
				new GridBagConstraints( 
				0, //gridX
				gridY++, //gridY
				2, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.NORTH, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 2, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		this.loginPanel.add( this.loginStatus, 
				new GridBagConstraints( 
				0, //gridX
				gridY++, //gridY
				2, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.CENTER, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 8, 16, 8, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		
		this.loggedInPanel = new JPanel();
		this.loggedInPanel.setLayout( new GridBagLayout() );
		
		this.logoutButton = new JButton("Change user");
		this.loggedInStatus = new JLabel(SUCCESS_MESSAGE);
		this.loggedInStatus.setFont( this.loggedInStatus.getFont().deriveFont( 16f ) );
		this.loggedInStatus.setForeground( HAPPY_COLOR );
		
		gridY = 0;
		this.loggedInPanel.add( this.loggedInStatus, 
				new GridBagConstraints( 
				0, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.CENTER, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 2, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		this.loggedInPanel.add( this.logoutButton, 
				new GridBagConstraints( 
				0, //gridX
				1, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.SOUTH, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 2, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		this.cardLayout = new CardLayout();
		this.setLayout( this.cardLayout );
		this.add( this.loginPanel, LOG_IN);
		this.add( this.loggedInPanel, LOGGED_IN);
		this.cardLayout.show( this, LOG_IN );
		
		this.userNameField.addActionListener( this );
		this.passwordField.addActionListener( this );
		this.loginButton.addActionListener( this );
		this.logoutButton.addActionListener( this );
		this.userNameField.getDocument().addDocumentListener( this );
		this.passwordField.getDocument().addDocumentListener( this );
		
		Dimension panelSize = new Dimension(400, 130);
		this.setPreferredSize( panelSize );
		this.setMinimumSize( panelSize );
		//this.setMaximumSize( panelSize );
	}
	
	public void enableUI(boolean enable)
	{
		this.userNameField.setEnabled(enable);
		this.passwordField.setEnabled(enable);
		this.loginButton.setEnabled(enable);
		this.logoutButton.setEnabled( enable );
	}
	
	public void removeYouTubeListener(YouTubeListener listener)
	{
		this.listeners.remove( listener );
	}
	
	public void addYouTubeListener(YouTubeListener listener)
	{
		if (!this.listeners.contains( listener ))
		{
			this.listeners.add(listener);
		}
	}
	
	public boolean isLoggedIn()
	{
		return this.isLoggedIn;
	}
	
	private void setLoggedInUIState(boolean loggedIn)
	{
		if (loggedIn)
		{
			this.cardLayout.show( this, LOGGED_IN );
		}
		else
		{
			this.cardLayout.show( this, LOG_IN );
		}
		this.revalidate();
	}
	
	public void actionPerformed( ActionEvent e ) {
		if (e.getSource() == this.logoutButton)
		{
			this.setLoggedInUIState( false );
		}
		else if (e.getSource() == this.userNameField ||
			e.getSource() == this.passwordField ||
			e.getSource() == this.loginButton)
		{
			this.userNameField.setEnabled( false );
			this.passwordField.setEnabled( false );
			this.loginButton.setEnabled(false);
			this.loginStatus.setForeground( NEUTRAL_COLOR );
			this.loginStatus.setText( "Logging in..." );
			SwingWorker< Boolean, Void > worker = new SwingWorker< Boolean, Void >(){
				@Override
				public Boolean doInBackground()
				{
					for (YouTubeListener l : YouTubeLoginPanel.this.listeners)
					{
						l.youTubeEventTriggered( new YouTubeEvent(YouTubeEvent.EventType.LOGIN_STARTED) );
					}
					try
					{
						YouTubeLoginPanel.this.youTubeUploader.logIn( YouTubeLoginPanel.this.userNameField.getText(), new String(YouTubeLoginPanel.this.passwordField.getPassword()) );
						String loginMessage = SUCCESS_MESSAGE + YouTubeLoginPanel.this.userNameField.getText();
						YouTubeLoginPanel.this.loginStatus.setText( loginMessage );
						YouTubeLoginPanel.this.loginStatus.setForeground( HAPPY_COLOR );
						YouTubeLoginPanel.this.loggedInStatus.setText( loginMessage );
						YouTubeLoginPanel.this.isLoggedIn = true;
						YouTubeLoginPanel.this.setLoggedInUIState( true );
						for (YouTubeListener l : YouTubeLoginPanel.this.listeners)
						{
							l.youTubeEventTriggered( new YouTubeEvent(YouTubeEvent.EventType.LOGIN_SUCCESS, loginMessage) );
						}
						
						return Boolean.TRUE;
					}
					catch (AuthenticationException e)
					{
                        e.printStackTrace();
                        System.out.println(e.getAuthHeader());
                        System.out.println(e.getExtendedHelp());
                        System.out.println(e.getInternalReason());
                        System.out.println(e.getMessage());
						YouTubeLoginPanel.this.loginStatus.setText( e.getMessage() );
						YouTubeLoginPanel.this.loginStatus.setForeground( ERROR_COLOR );
						YouTubeLoginPanel.this.isLoggedIn = false;
						YouTubeLoginPanel.this.setLoggedInUIState( false );
						for (YouTubeListener l : YouTubeLoginPanel.this.listeners)
						{
							l.youTubeEventTriggered( new YouTubeEvent(YouTubeEvent.EventType.LOGIN_FAILED, e.getMessage()) );
						}
						return Boolean.FALSE;
					}
				}
				
				@Override
				public void done()
				{
					YouTubeLoginPanel.this.userNameField.setEnabled( true );
					YouTubeLoginPanel.this.passwordField.setEnabled( true );
					YouTubeLoginPanel.this.loginButton.setEnabled( true );
				}
			};
			worker.execute();
		}
	}

	public void changedUpdate( DocumentEvent e ) {
		// TODO Auto-generated method stub
		
	}

	public void insertUpdate( DocumentEvent e ) {
		// TODO Auto-generated method stub
		
	}

	public void removeUpdate( DocumentEvent e ) {
		// TODO Auto-generated method stub
		
	}

}
