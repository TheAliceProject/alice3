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
package org.alice.ide.issue;

class BooleanRadio extends javax.swing.JPanel {
	private javax.swing.JRadioButton trueButton;
	private javax.swing.JRadioButton falseButton;
	public BooleanRadio( String trueText, String falseText, Boolean selection, int axis ) {
		this.setLayout( new javax.swing.BoxLayout( this, axis ) );
		javax.swing.ButtonGroup group = new javax.swing.ButtonGroup();
		this.trueButton = new javax.swing.JRadioButton( trueText );
		this.falseButton = new javax.swing.JRadioButton( falseText );
		group.add( this.trueButton );
		group.add( this.falseButton );
		this.add( this.trueButton );
		this.add( this.falseButton );
		if( selection != null ) {
			if( selection ) {
				this.trueButton.setSelected( true );
			} else {
				this.falseButton.setSelected( true );
			}
		}
	}
	public Boolean getValue() {
		if( this.trueButton.isSelected() ) {
			return true;
		} else if( this.falseButton.isSelected() ) {
			return false;
		} else {
			return null;
		}
	}
	public void setValue( boolean b ) {
		if( b ) {
			this.trueButton.setSelected( true );
		} else {
			this.falseButton.setSelected( true );
		}
	}
}

class AttachProjectRadioButtons extends BooleanRadio {
	public AttachProjectRadioButtons() {
		super( "Yes, attach my current project as it is relevant.", "No, do not attach my project.", null, javax.swing.BoxLayout.PAGE_AXIS );
	}
}

class VisibilityRadioButtons extends BooleanRadio {
	public VisibilityRadioButtons() {
		super( "Public", "Private (Only Visible to the Alice Team)", true, javax.swing.BoxLayout.LINE_AXIS );
	}
}

/**
 * @author Dennis Cosgrove
 */
public class PostIssuePane extends edu.cmu.cs.dennisc.toolkit.issue.AbstractPostIssuePane {
	private javax.swing.JLabel labelAttachment = createLabelForMultiLine( "attachment:" );
	private AttachProjectRadioButtons radioAttachment = new AttachProjectRadioButtons();
	private java.awt.Component[] rowAttachment = edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( labelAttachment, radioAttachment );
	
	private javax.swing.JLabel labelVisibility = createLabelForSingleLine( "visibility:" );
	private VisibilityRadioButtons radioVisibility = new VisibilityRadioButtons();
	private java.awt.Component[] rowVisibility = edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( labelVisibility, radioVisibility );

	public PostIssuePane( edu.cmu.cs.dennisc.jira.JIRAReport.Type issueType ) {
		HeaderPane headerPane = new HeaderPane();
		this.add( headerPane, java.awt.BorderLayout.NORTH );
		this.setIssueType( issueType );
	}
	@Override
	protected edu.cmu.cs.dennisc.issue.ReportSubmissionConfiguration getReportSubmissionConfiguration() {
		return new ReportSubmissionConfiguration() {
			@Override
			public edu.cmu.cs.dennisc.jira.rpc.Authenticator getJIRAViaRPCAuthenticator() {
				final edu.cmu.cs.dennisc.login.AccountInformation accountInformation = edu.cmu.cs.dennisc.login.AccountManager.get( edu.cmu.cs.dennisc.toolkit.login.LogInStatusPane.BUGS_ALICE_ORG_KEY );
				if( accountInformation != null ) {
					return new edu.cmu.cs.dennisc.jira.rpc.Authenticator() {
						public Object login( redstone.xmlrpc.XmlRpcClient client ) throws redstone.xmlrpc.XmlRpcException, redstone.xmlrpc.XmlRpcFault {
							return edu.cmu.cs.dennisc.jira.rpc.RPCUtilities.logIn( client, accountInformation.getID(), accountInformation.getPassword() );
						}
					};
				} else {
					return super.getJIRAViaRPCAuthenticator();
				}
			}
			@Override
			public edu.cmu.cs.dennisc.jira.soap.Authenticator getJIRAViaSOAPAuthenticator() {
				final edu.cmu.cs.dennisc.login.AccountInformation accountInformation = edu.cmu.cs.dennisc.login.AccountManager.get( edu.cmu.cs.dennisc.toolkit.login.LogInStatusPane.BUGS_ALICE_ORG_KEY );
				if( accountInformation != null ) {
					return new edu.cmu.cs.dennisc.jira.soap.Authenticator() {
						public String login( com.atlassian.jira.rpc.soap.client.JiraSoapService service ) throws java.rmi.RemoteException {
							return service.login( accountInformation.getID(), accountInformation.getPassword() );
						}
					};
				} else {
					return super.getJIRAViaSOAPAuthenticator();
				}
			}
		};
	}
	@Override
	protected java.util.ArrayList< java.awt.Component[] > addRows( java.util.ArrayList< java.awt.Component[] > rows ) {
		rows.add( this.rowVisibility );
		rows = super.addRows( rows );
		rows.add( this.rowAttachment );
		return rows;
	}
	@Override
	protected String getJIRAProjectKey() {
		if( this.radioVisibility.getValue() == Boolean.TRUE ) {
			return "AIII";
		} else {
			return "AIIIP";
		}
	}
	

	@Override
	protected String[] getAffectsVersions() {
		return new String[] { edu.cmu.cs.dennisc.alice.Version.getCurrentVersionText() };
	}

	@Override
	protected boolean isClearedToSubmit() {
		if( super.isClearedToSubmit() ) {
			if( this.radioAttachment.getValue() != null ) {
				return true;
			} else {
				int result = javax.swing.JOptionPane.showConfirmDialog( this, "Is your current project relevant to this issue and are you willing to attach your project with this report?", "Submit project?", javax.swing.JOptionPane.YES_NO_CANCEL_OPTION );
				switch( result ) {
				case javax.swing.JOptionPane.YES_OPTION:
					this.radioAttachment.setValue( true );
					return true;
				case javax.swing.JOptionPane.NO_OPTION:
					this.radioAttachment.setValue( false );
					return true;
				}
				return false;
			}
		} else {
			return false;
		}
	}
	@Override
	protected edu.cmu.cs.dennisc.issue.AbstractReport addAttachments( edu.cmu.cs.dennisc.issue.AbstractReport rv ) {
		rv = super.addAttachments( rv );
		if( this.radioAttachment.getValue() == Boolean.TRUE ) {
			rv.addAttachment( new CurrentProjectAttachment() );
		}
		return rv;
	}
	public static void main( String[] args ) {
		PostIssuePane pane = new PostIssuePane( edu.cmu.cs.dennisc.jira.JIRAReport.Type.BUG );
		javax.swing.JFrame window = edu.cmu.cs.dennisc.swing.JFrameUtilities.createPackedJFrame( pane, "Report Issue", javax.swing.WindowConstants.DISPOSE_ON_CLOSE );
		window.getRootPane().setDefaultButton( pane.getSubmitButton() );
		window.setVisible( true );
	}

}
