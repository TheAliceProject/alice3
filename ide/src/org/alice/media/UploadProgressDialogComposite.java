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
package org.alice.media;

import java.io.IOException;

import org.alice.ide.browser.MutableBrowserOperation;
import org.alice.media.components.UploadProgressDialogView;
import org.lgna.croquet.PlainDialogOperationComposite;
import org.lgna.croquet.PlainStringValue;

import com.google.gdata.data.media.MediaFileSource;
import com.google.gdata.data.media.mediarss.MediaCategory;
import com.google.gdata.data.media.mediarss.MediaDescription;
import com.google.gdata.data.media.mediarss.MediaKeywords;
import com.google.gdata.data.media.mediarss.MediaTitle;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.YouTubeMediaGroup;
import com.google.gdata.data.youtube.YouTubeNamespace;

/**
 * @author Matt May
 */
public class UploadProgressDialogComposite extends PlainDialogOperationComposite<UploadProgressDialogView> implements YouTubeListener {

	private UploadComposite parentComposite;
	private PlainStringValue pliableValue = createStringValue( createKey( "NotAvailable" ) );
	private PlainStringValue uploadingState = createStringValue( createKey( "uploading" ) );
	private PlainStringValue successfulUploadState = createStringValue( createKey( "successfulUpload" ) );
	private PlainStringValue unsuccessfulUploadState = createStringValue( createKey( "unsuccessfulUpload" ) );
	private MutableBrowserOperation videoLinkOperation = new MutableBrowserOperation( java.util.UUID.fromString( "7de64bab-9f75-48b0-b52e-ad7d4ba8d22a" ) );

	public UploadProgressDialogComposite( UploadComposite parentComposite ) {
		super( java.util.UUID.fromString( "d8820578-420e-4fe4-9623-a3e4d073a190" ), null );
		this.parentComposite = parentComposite;
	}

	public PlainStringValue getPliablePlainStringValue() {
		return pliableValue;
	}

	public PlainStringValue getUploadingState() {
		return this.uploadingState;
	}

	public PlainStringValue getSuccessfulUploadState() {
		return this.successfulUploadState;
	}

	public PlainStringValue getUnsuccessfulUploadState() {
		return this.unsuccessfulUploadState;
	}

	public MutableBrowserOperation getVideoLinkOperation() {
		return this.videoLinkOperation;
	}

	@Override
	protected UploadProgressDialogView createView() {
		return new UploadProgressDialogView( this );
	}

	public void setEnabled( boolean isEnabled ) {
		this.getOperation().setEnabled( isEnabled );
	}

	public void youTubeEventTriggered( YouTubeEvent event ) {
		getView().youtubeEventTriggered( event );
	}

	@Override
	public void handlePreActivation() {
		super.handlePreActivation();
		VideoEntry entry = new VideoEntry();

		MediaFileSource source = new MediaFileSource( parentComposite.getOwner().getFile(), "video/quicktime" );
		entry.setMediaSource( source );
		YouTubeMediaGroup mediaGroup = entry.getOrCreateMediaGroup();

		MediaTitle title = new MediaTitle();
		title.setPlainTextContent( parentComposite.getTitleState().getValue().trim() );
		mediaGroup.setTitle( title );//title

		MediaDescription mediaDescription = new MediaDescription();
		mediaDescription.setPlainTextContent( parentComposite.getDescriptionState().getValue().trim() );
		mediaGroup.setDescription( mediaDescription );//description]

		MediaKeywords keywords = new MediaKeywords();
		String[] arr = parentComposite.getTagsState().getValue().split( "," );
		for( String s : arr ) {
			keywords.addKeyword( s.trim() );
		}
		mediaGroup.setKeywords( keywords );//tags

		String category = parentComposite.getVideoCategoryState().getValue().toString().split( "\\s" )[ 0 ].trim();
		mediaGroup.addCategory( new MediaCategory( YouTubeNamespace.CATEGORY_SCHEME, category ) );//category
		mediaGroup.setPrivate( parentComposite.getIsPrivateState().getValue() );//isPrivate
		try {
			parentComposite.getUploader().uploadVideo( entry );
		} catch( IOException e ) {
			e.printStackTrace();
		}
	}

}
