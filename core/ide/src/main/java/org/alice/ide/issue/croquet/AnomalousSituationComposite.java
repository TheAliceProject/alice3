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
package org.alice.ide.issue.croquet;

import edu.cmu.cs.dennisc.issue.IssueType;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.jira.JIRAReport;
import org.alice.ide.croquet.models.help.AbstractIssueComposite;
import org.alice.ide.issue.ImageAttachment;
import org.alice.ide.issue.croquet.views.AnomalousSitutationView;
import org.lgna.croquet.Application;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.DocumentFrame;
import org.lgna.croquet.Operation;
import org.lgna.croquet.simple.SimpleApplication;
import org.lgna.croquet.views.ContentPane;
import org.lgna.croquet.views.Frame;
import org.lgna.croquet.views.Label;

import javax.swing.SwingUtilities;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public final class AnomalousSituationComposite extends AbstractIssueComposite<AnomalousSitutationView> {
	public static AnomalousSituationComposite createInstance( String message, String description ) {
		try {
			throw new RuntimeException( message );
		} catch( RuntimeException re ) {
			return new AnomalousSituationComposite( re, description );
		}
	}

	private final Thread thread;
	private final Throwable throwable;
	private final BufferedImage applicationContentPanelImage;

	private final BooleanState areProjectAndImageAttachmentsDesired = this.createBooleanState( "areProjectAndImageAttachmentsDesired", true );
	private final Operation showApplicationContentPanelImageOperation = new ShowImageComposite( UUID.fromString( "f8455aee-e131-417c-b539-b8a9ad7bdc73" ) ) {
		@Override
		public Image getImage() {
			return applicationContentPanelImage;
		}
	}.getLaunchOperation();

	private final String description;

	private AnomalousSituationComposite( Throwable throwable, String description ) {
		super( UUID.fromString( "f6516c45-2ed6-4d7b-a12d-97726f655bab" ), IsModal.TRUE );
		this.thread = Thread.currentThread();
		this.throwable = throwable;
		this.description = description;

		Application app = Application.getActiveInstance();
		DocumentFrame documentFrame = app.getDocumentFrame();
		Frame frame = documentFrame.getFrame();
		ContentPane contentPane = frame.getContentPane();

		this.applicationContentPanelImage = new BufferedImage( contentPane.getWidth(), contentPane.getHeight(), BufferedImage.TYPE_INT_RGB );
		Graphics g = applicationContentPanelImage.getGraphics();
		g.setColor( contentPane.getBackgroundColor() );
		g.fillRect( 0, 0, this.applicationContentPanelImage.getWidth(), this.applicationContentPanelImage.getHeight() );
		contentPane.getAwtComponent().printAll( g );
		g.dispose();
		this.getImp().createAndRegisterNullKeyLaunchOperation();
	}

	@Override
	protected String getDefaultTitleText() {
		Operation launchOperation = this.getLaunchOperation();
		return launchOperation != null ? launchOperation.getImp().getName() : null;
	}

	public Operation getLaunchOperation() {
		return this.getImp().getLaunchOperation( null );
	}

	@Override
	protected Thread getThread() {
		return this.thread;
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
	protected IssueType getReportType() {
		return IssueType.BUG;
	}

	@Override
	protected String getSummaryText() {
		return this.throwable.getMessage();
	}

	@Override
	protected String getDescriptionText() {
		return this.description;
	}

	public BooleanState getAreProjectAndImageAttachmentsDesired() {
		return this.areProjectAndImageAttachmentsDesired;
	}

	@Override
	protected boolean isProjectAttachmentDesired() {
		return this.areProjectAndImageAttachmentsDesired.getValue();
	}

	public Operation getShowApplicationContentPanelImageOperation() {
		return this.showApplicationContentPanelImageOperation;
	}

	@Override
	protected AnomalousSitutationView createView() {
		return new AnomalousSitutationView( this );
	}

	@Override
	protected boolean isClearedToSubmitBug() {
		return true;
	}

	@Override
	protected void addAttachments( JIRAReport report ) {
		super.addAttachments( report );
		if( this.areProjectAndImageAttachmentsDesired.getValue() ) {
			try {
				ImageAttachment imageAttachment = new ImageAttachment( this.applicationContentPanelImage, "snapshot" );
				report.addAttachment( imageAttachment );
			} catch( IOException ioe ) {
				Logger.throwable( ioe, this.applicationContentPanelImage );
			}
		}
	}

	public static void main( String[] args ) throws Exception {
		SimpleApplication app = new SimpleApplication();
		app.initialize( args );
		DocumentFrame documentFrame = app.getDocumentFrame();
		Frame frame = documentFrame.getFrame();
		frame.getContentPane().addCenterComponent( new Label( "hello" ) );
		frame.pack();
		frame.setVisible( true );
		Thread.sleep( 1000 );
		final AnomalousSituationComposite composite = AnomalousSituationComposite.createInstance( "A popup menu has been requested for a statement without a parent.", "description" );
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				composite.getLaunchOperation().fire();
				//System.exit( 0 );
			}
		} );
	}
}
