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
package org.alice.media.components;

import org.alice.ide.browser.MutableBrowserOperation;
import org.alice.media.UploadProgressDialogComposite;
import org.alice.media.YouTubeEvent;
import org.alice.media.YouTubeEvent.EventType;
import org.lgna.croquet.components.BorderPanel;
import org.lgna.croquet.components.ImmutableTextArea;
import org.lgna.croquet.components.Label;

import com.google.gdata.data.youtube.VideoEntry;

import edu.cmu.cs.dennisc.math.GoldenRatio;

/**
 * @author Matt May
 */
public class UploadProgressDialogView extends BorderPanel {

	private YouTubeEvent youTubeEvent;
	private ImmutableTextArea createImmutableTextArea;

	public UploadProgressDialogView( UploadProgressDialogComposite uploadProgressDialogComposite ) {
		super( uploadProgressDialogComposite );

		createImmutableTextArea = uploadProgressDialogComposite.getPliablePlainStringValue().createImmutableTextArea();
		this.addCenterComponent( createImmutableTextArea );
		this.addComponent( new Label(), Constraint.PAGE_END );

		this.setMinimumPreferredHeight( 175 );
		this.setMinimumPreferredWidth( GoldenRatio.getLongerSideLength( 175 ) );
	}

	public void youtubeEventTriggered( YouTubeEvent event ) {
		this.youTubeEvent = event;
		refreshLater();
	}

	@Override
	protected void internalRefresh() {
		super.internalRefresh();
		UploadProgressDialogComposite uploadProgressDialogComposite = (UploadProgressDialogComposite)this.getComposite();
		if( this.youTubeEvent != null ) {
			EventType eventType = this.youTubeEvent.getType();
			if( eventType == EventType.UPLOAD_FAILED ) {
				uploadProgressDialogComposite.getPliablePlainStringValue().setText( uploadProgressDialogComposite.getUnsuccessfulUploadState().getText() );
				System.out.println( youTubeEvent.getMoreInfo() );
				//				Label failLabel = new Label( youTubeEvent.getMoreInfo().toString() );
				//				this.addComponent( failLabel, Constraint.PAGE_END );
			} else if( eventType == EventType.UPLOAD_SUCCESS ) {
				if( youTubeEvent.getMoreInfo() instanceof VideoEntry ) {
					VideoEntry entry = ( (VideoEntry)youTubeEvent.getMoreInfo() );
					uploadProgressDialogComposite.getPliablePlainStringValue().setText( uploadProgressDialogComposite.getSuccessfulUploadState().getText() );
					MutableBrowserOperation videoLinkOperation = uploadProgressDialogComposite.getVideoLinkOperation();
					videoLinkOperation.setUrl( entry.getHtmlLink().getHref() );
					this.addComponent( uploadProgressDialogComposite.getVideoLinkOperation().createHyperlink(), Constraint.PAGE_END );
				}
			}
		} else {
			System.out.println( "HELLO WORLD (mmay) " + uploadProgressDialogComposite.getUploadingState().getText() );
			uploadProgressDialogComposite.getPliablePlainStringValue().setText( uploadProgressDialogComposite.getUploadingState().getText() );
		}
	}

	@Override
	public void handleCompositePreActivation() {
		super.handleCompositePreActivation();
		this.youTubeEvent = null;
		refreshLater();
	}
}
