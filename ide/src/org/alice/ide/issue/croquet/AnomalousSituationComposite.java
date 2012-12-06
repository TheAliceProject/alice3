/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.alice.ide.issue.croquet;

/**
 * @author Dennis Cosgrove
 */
public final class AnomalousSituationComposite extends org.alice.ide.croquet.models.help.AbstractIssueComposite<org.alice.ide.issue.croquet.views.AnomalousSitutationView> {
	public static AnomalousSituationComposite createInstance( String message, String description ) {
		try {
			throw new RuntimeException( message );
		} catch( RuntimeException re ) {
			return new AnomalousSituationComposite( re, description );
		}
	}

	private final Throwable throwable;
	private final java.awt.image.BufferedImage applicationContentPanelImage;

	private final org.lgna.croquet.BooleanState areProjectAndImageAttachmentsDesired = this.createBooleanState( this.createKey( "areProjectAndImageAttachmentsDesired" ), true );
	private final org.lgna.croquet.Operation showApplicationContentPanelImageOperation = new ShowImageComposite( java.util.UUID.fromString( "f8455aee-e131-417c-b539-b8a9ad7bdc73" ) ) {
		@Override
		public java.awt.Image getImage() {
			return applicationContentPanelImage;
		}
	}.getOperation();

	private final String description;

	private AnomalousSituationComposite( Throwable throwable, String description ) {
		super( java.util.UUID.fromString( "f6516c45-2ed6-4d7b-a12d-97726f655bab" ), true );
		this.throwable = throwable;
		this.description = description;

		org.lgna.croquet.Application app = org.lgna.croquet.Application.getActiveInstance();
		org.lgna.croquet.components.Frame frame = app.getFrame();
		org.lgna.croquet.components.Panel contentPanel = frame.getContentPanel();

		this.applicationContentPanelImage = new java.awt.image.BufferedImage( contentPanel.getWidth(), contentPanel.getHeight(), java.awt.image.BufferedImage.TYPE_INT_RGB );
		java.awt.Graphics g = applicationContentPanelImage.getGraphics();
		g.setColor( contentPanel.getBackgroundColor() );
		g.fillRect( 0, 0, this.applicationContentPanelImage.getWidth(), this.applicationContentPanelImage.getHeight() );
		contentPanel.getAwtComponent().printAll( g );
		g.dispose();
	}

	@Override
	public Throwable getThrowable() {
		return this.throwable;
	}

	@Override
	protected boolean isPublic() {
		return false;
	}

	@Override
	protected edu.cmu.cs.dennisc.jira.JIRAReport.Type getReportType() {
		return edu.cmu.cs.dennisc.jira.JIRAReport.Type.BUG;
	}

	@Override
	protected String getSummaryText() {
		return this.throwable.getMessage();
	}

	@Override
	protected String getDescriptionText() {
		return this.description;
	}

	public org.lgna.croquet.BooleanState getAreProjectAndImageAttachmentsDesired() {
		return this.areProjectAndImageAttachmentsDesired;
	}

	@Override
	protected boolean isProjectAttachmentDesired() {
		return this.areProjectAndImageAttachmentsDesired.getValue();
	}

	public org.lgna.croquet.Operation getShowApplicationContentPanelImageOperation() {
		return this.showApplicationContentPanelImageOperation;
	}

	@Override
	protected org.alice.ide.issue.croquet.views.AnomalousSitutationView createView() {
		return new org.alice.ide.issue.croquet.views.AnomalousSitutationView( this );
	}

	@Override
	protected boolean isClearedToSubmitBug() {
		return true;
	}

	@Override
	protected void addAttachments( edu.cmu.cs.dennisc.jira.JIRAReport report ) {
		super.addAttachments( report );
		if( this.areProjectAndImageAttachmentsDesired.getValue() ) {
			report.addAttachment( new org.alice.ide.issue.ImageAttachment( this.applicationContentPanelImage, "snapshot" ) );
		}
	}

	public static void main( String[] args ) throws Exception {
		org.lgna.croquet.simple.SimpleApplication app = new org.lgna.croquet.simple.SimpleApplication();
		app.initialize( args );
		app.getFrame().getContentPanel().addCenterComponent( new org.lgna.croquet.components.Label( "hello" ) );
		app.getFrame().pack();
		app.getFrame().setVisible( true );
		Thread.sleep( 1000 );
		final AnomalousSituationComposite composite = AnomalousSituationComposite.createInstance( "A popup menu has been requested for a statement without a parent.", "description" );
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				composite.getOperation().fire();
				//System.exit( 0 );
			}
		} );
	}
}
