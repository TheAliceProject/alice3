/**
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
package org.alice.installer.error;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import edu.cmu.cs.dennisc.issue.AbstractReport;
import edu.cmu.cs.dennisc.issue.Attachment;
import edu.cmu.cs.dennisc.issue.IssueReportWorker;
import edu.cmu.cs.dennisc.issue.ProgressPane;
import edu.cmu.cs.dennisc.issue.ReportGenerator;
import edu.cmu.cs.dennisc.issue.ReportSubmissionConfiguration;
import edu.cmu.cs.dennisc.jira.JIRAReport;
import edu.cmu.cs.dennisc.mail.MailReport;

/**
 * @author David Culyba
 */
public class InstallerErrorDialog extends JDialog implements ActionListener, ReportGenerator{

	
	private class MailAuthenticator extends edu.cmu.cs.dennisc.mail.AbstractAuthenticator {
		@Override
		protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
			return new javax.mail.PasswordAuthentication( "anonymous-alice-user", "silkySmooth123" );
		}
		@Override
		public String getAnonymousFrom() {
			return "anonymous-alice-user@haru.pc.cc.cmu.edu";
		}
	}
	
	private class SOAPAuthenticator implements edu.cmu.cs.dennisc.jira.soap.Authenticator {
		public String login( com.atlassian.jira.rpc.soap.client.JiraSoapService service ) throws java.rmi.RemoteException {
			return service.login( "alice3_soap", "BW@U~1.455g45u" );
		}
	}
	
	private class RPCAuthenticator implements edu.cmu.cs.dennisc.jira.rpc.Authenticator {
		public Object login( redstone.xmlrpc.XmlRpcClient client ) throws redstone.xmlrpc.XmlRpcException, redstone.xmlrpc.XmlRpcFault {
			return client.invoke( "jira1.login", new Object[] { "alice3_rpc", "iNw6aFRhNia6SirM" } );
		}
	}
	
	private class InstallerReportSubmissionConfiguration implements edu.cmu.cs.dennisc.issue.ReportSubmissionConfiguration {
		public java.net.URL getJIRAViaRPCServer() throws java.net.MalformedURLException {
			return new java.net.URL( "http://bugs.alice.org:8080/rpc/xmlrpc" );
		}
		public java.net.URL getJIRAViaSOAPServer() throws java.net.MalformedURLException {
			return new java.net.URL( "http://bugs.alice.org:8080/rpc/soap/jirasoapservice-v2" );
		}
		public edu.cmu.cs.dennisc.jira.rpc.Authenticator getJIRAViaRPCAuthenticator() {
			return new RPCAuthenticator();
		}
		public edu.cmu.cs.dennisc.jira.soap.Authenticator getJIRAViaSOAPAuthenticator() {
			return new SOAPAuthenticator();
		}
		public String getMailServer() {
			return "haru.pc.cc.cmu.edu";
		}
		public edu.cmu.cs.dennisc.mail.AbstractAuthenticator getMailAuthenticator() {
			return new MailAuthenticator();
		}
		public String getMailRecipient() {
			return "alice.bugs.3.beta.xxxx@gmail.com";
		}
	}
	
	public enum SubmitChoice
	{
		SUBMIT,
		DO_NOT_SUBMIT,
	}
	
	protected JButton submitButton;
	protected JButton skipSubmitButton;
	
	protected SubmitChoice submitChoice = SubmitChoice.DO_NOT_SUBMIT;
	protected ProgressPane errorProgressPane;
	protected File logFile;
	protected String logText = null;
	
	public InstallerErrorDialog(Frame owner, File logFile)
	{
		super(owner, "Installer Error", true);
		
		this.errorProgressPane = new ProgressPane();
		this.logFile = logFile;
		
		Dimension dialogSize = new Dimension(400, 300);
		this.setPreferredSize( dialogSize );
		this.setMaximumSize( dialogSize );
		this.setMinimumSize( dialogSize );
		this.setResizable( true );
		this.setDefaultCloseOperation( HIDE_ON_CLOSE );
		
		ImageIcon queenIcon = new ImageIcon(InstallerErrorDialog.class.getResource( "meanQueen.png" ));
		JLabel queenLabel = new JLabel(queenIcon);
		JTextArea messageText = new JTextArea("A error has occurred during the installation of Alice. Would you like to help us track down the problem by reporting this error?");
		messageText.setEditable( false );
		messageText.setOpaque( false );
		messageText.setBorder( null );
		Dimension textSize = new Dimension(200, 100);
		messageText.setPreferredSize( textSize );
		messageText.setMaximumSize( textSize );
		messageText.setMinimumSize( textSize );
		messageText.setLineWrap( true );
		messageText.setWrapStyleWord( true );
		messageText.setFont( messageText.getFont().deriveFont( Font.BOLD, 14f ) );
		
		this.submitButton = new JButton("Report Error");
		this.submitButton.addActionListener( this );
		this.skipSubmitButton = new JButton("Cancel");
		this.skipSubmitButton.addActionListener( this );
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque( false );
		buttonPanel.setLayout( new FlowLayout(FlowLayout.CENTER, 40, 0) );
		buttonPanel.add( this.skipSubmitButton );
		buttonPanel.add( this.submitButton );
		
		this.getContentPane().setLayout( new GridBagLayout() );
		this.getContentPane().add( queenLabel, 
				new GridBagConstraints( 
				0, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				1.0, //weightY
				GridBagConstraints.CENTER, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 10, 10, 10, 10 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		this.getContentPane().add( messageText, 
				new GridBagConstraints( 
				1, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				1.0, //weightY
				GridBagConstraints.CENTER, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 10, 10, 10, 10 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		this.getContentPane().add( buttonPanel, 
				new GridBagConstraints( 
				0, //gridX
				1, //gridY
				2, //gridWidth
				1, //gridHeight
				1.0, //weightX
				1.0, //weightY
				GridBagConstraints.CENTER, //anchor 
				GridBagConstraints.HORIZONTAL, //fill
				new Insets( 0, 0, 0, 0 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		this.pack();
	}

	public boolean shouldSubmit()
	{
		return this.submitChoice == SubmitChoice.SUBMIT;
	}
	
	protected boolean submitBug()
	{
		ProgressPane progressPane = new ProgressPane();
		progressPane.initializeAndExecuteWorker( this, new InstallerReportSubmissionConfiguration() );

		javax.swing.JFrame frame = new javax.swing.JFrame();
		javax.swing.JDialog dialog = new javax.swing.JDialog( frame, "Uploading Bug Report", true );
		dialog.addWindowListener( new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing( java.awt.event.WindowEvent e ) {
				e.getComponent().setVisible( false );
			}
		} );
		dialog.getContentPane().add( progressPane );
		dialog.setDefaultCloseOperation( javax.swing.JFrame.DISPOSE_ON_CLOSE );
		dialog.pack();
		dialog.setVisible( true );


		//this.urlResult = progressPane.getURLResult();
		
		return progressPane.isSuccessful();
	}
	
	public void actionPerformed( ActionEvent e ) {
		if (e.getSource() == this.submitButton)
		{
			this.submitChoice = SubmitChoice.SUBMIT;
			submitBug();
		}
		else if (e.getSource() == this.skipSubmitButton)
		{
			this.submitChoice = SubmitChoice.DO_NOT_SUBMIT;
		}
		this.setVisible( false );
	}

	private class LogAttachment implements Attachment
	{
		public byte[] getBytes() {
			if (InstallerErrorDialog.this.logFile != null && InstallerErrorDialog.this.logFile.canRead())
			{
				byte[] buf = new byte[1024];
			    try
			    {
				    InputStream in = new FileInputStream(InstallerErrorDialog.this.logFile);
				    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
				    ZipOutputStream zipStream = new ZipOutputStream(byteStream);
				    zipStream.putNextEntry( new ZipEntry("LogFile.txt") );
				    int len;
		            while ((len = in.read(buf)) > 0) {
		            	zipStream.write(buf, 0, len);
		            } 
		            zipStream.closeEntry();
		            zipStream.close();
				    in.close();
				    byte[] byteArray = byteStream.toByteArray();
				    System.out.println("compressed from "+InstallerErrorDialog.this.logFile.length() + " to "+byteArray.length);
				    return byteArray;
			    }
			    catch (IOException e)
			    {
			    	return null;
			    }
			}
			return null;
		}

		public String getFileName() {
			if (InstallerErrorDialog.this.logFile != null)
			{
				return "LogFile.zip";
			}
			return null;
		}

		public String getMIMEType() {
			return "text/plain";
		}
		
	}
	
	protected String getJIRAProjectKey() {
		return "AIIIP";
	}

	protected edu.cmu.cs.dennisc.jira.JIRAReport.Type getJIRAType() {
		return edu.cmu.cs.dennisc.jira.JIRAReport.Type.BUG;
	}
	
//	protected String getLogString()
//	{
//		if (this.logText == null)
//		{
//			StringBuilder returnString = new StringBuilder();
//			if (this.logFile != null && this.logFile.canRead())
//			{
//				try
//				{
//					BufferedReader br = new BufferedReader( new FileReader(this.logFile) );
//					String readLine;
//					while ( (readLine = br.readLine()) != null )
//					{
//						returnString.append( readLine );
//					}
//				}
//				catch (IOException e)
//				{
//					e.printStackTrace();
//				}
//			}
//			this.logText = returnString.toString();
//		}
//		return this.logText;
//	}
	
	private String getEnvironmentString()
	{
		StringBuffer sb = new StringBuffer();
		String intersticial = "";
		java.util.List< String > props = new java.util.LinkedList< String >();
		props.add( "java.version" );
		props.add( "os.name" );
		props.add( "os.arch" );
		props.add( "os.version" );
		for( String propertyName : props ) {
			sb.append( intersticial );
			sb.append(propertyName );
			sb.append( ": " );
			sb.append( System.getProperty( propertyName ) );
			intersticial = "\n";
		}
		return sb.toString();
	}
	
	private edu.cmu.cs.dennisc.jira.JIRAReport generateIssue() {
		edu.cmu.cs.dennisc.jira.JIRAReport rv = new edu.cmu.cs.dennisc.jira.JIRAReport();
		rv.setProjectKey( this.getJIRAProjectKey() );
		rv.setType( this.getJIRAType() );
		rv.setSummary( "Installer Failure" );
		rv.setDescription( "See attachment" );
		rv.setEnvironment( getEnvironmentString() );
		rv.setSteps( "NONE" );
		rv.setException( "NONE" );
		return rv;
	}
	
	public JIRAReport generateIssueForRPC() {
//		edu.cmu.cs.dennisc.jira.JIRAReport rv = this.generateIssue();
//		return rv;
		return null;
	}
	
	private static final long MAX_JIRA_UPLOAD = 1500000;
	
	public JIRAReport generateIssueForSOAP() {
		if (this.logFile.length() > MAX_JIRA_UPLOAD)
		{
			return null;
		}
		edu.cmu.cs.dennisc.jira.JIRAReport rv = this.generateIssue();
		this.addAttachments( rv );
		return rv;
	}
	public MailReport generateIssueForSMTP() {
		edu.cmu.cs.dennisc.mail.MailReport rv = new edu.cmu.cs.dennisc.mail.MailReport();
		rv.setSubject( "Alice Installer Error" );
		rv.setBody( getEnvironmentString() );
		this.addAttachments( rv );
		return rv;
	}
	
	protected AbstractReport addAttachments( AbstractReport rv ) {
		rv.addAttachment( new LogAttachment() );
		return rv;
	}
	
	/**
	 * @param args
	 */
	public static void main( String[] args ) {
		String logFileName = "20090627145529.log"; //large log
		String userDirectoryPath = javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory().getParent();
		File file = new File( userDirectoryPath + "/.alice/log/"+logFileName);
		assert file.exists();
		InstallerErrorDialog errorDialog = new InstallerErrorDialog(null, file );
////		String logFileName = "20090616151938.log"; //small log
//		String logFileName = "20090616151432.log"; //medium log
////		String logFileName = "20090722172456.log"; //large log
//		InstallerErrorDialog errorDialog = new InstallerErrorDialog(null, new File("C:/Users/Administrator/.alice/log/"+logFileName));
		errorDialog.setVisible( true );
		System.out.println("Should submit? "+errorDialog.shouldSubmit());
		System.exit( 0 );
	}
}
