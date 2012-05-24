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

import java.io.File;
import java.io.IOException;

import org.alice.media.components.UploadView;
import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.ListSelectionState;
import org.lgna.croquet.StringState;
import org.lgna.croquet.StringValue;
import org.lgna.croquet.WizardPageComposite;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.croquet.history.Transaction;
import org.lgna.croquet.triggers.Trigger;
import org.lgna.project.Project;

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
public class UploadComposite extends WizardPageComposite<UploadView> {

	private final YouTubeUploader uploader = new YouTubeUploader();
	private final ExportToYouTubeWizardDialogComposite owner;
	private final StringState idState = this.createStringState( "", this.createKey( "id" ) );
	private final StringValue username = this.createStringValue( this.createKey( "username" ) );
	private final StringState passwordState = this.createStringState( "", this.createKey( "password" ) );
	private final StringValue passwordLabelValue = this.createStringValue( this.createKey( "passwordLabel" ) );
	private final StringValue titleLabelValue = this.createStringValue( this.createKey( "titleLabel" ) );
	private final StringState titleState = this.createStringState( "Alice Video", this.createKey( "title" ) );
	private final BooleanState isPrivateState = this.createBooleanState( true, this.createKey( "isPrivate" ) );
	private final StringValue categoryValue = this.createStringValue( this.createKey( "category" ) );
	private final ListSelectionState<VideoCategory> videoCategoryState = this.createListSelectionState( VideoCategory.class, VideoCategory.SCIENCE_AND_TECHNOLOGY, this.createKey( "videoCategory" ) );
	private final StringValue descriptionValue = this.createStringValue( this.createKey( "descriptionValue" ) );
	private final StringState descriptionState = this.createStringState( "", this.createKey( "description" ) );
	private final StringValue tagLabel = this.createStringValue( this.createKey( "tagLabel" ) );
	private final StringState tagState = this.createStringState( "", this.createKey( "tag" ) );
	private Status status;
	private final ActionOperation loginOperation = this.createActionOperation( new Action() {
		public org.lgna.croquet.edits.Edit perform( Transaction transaction, Trigger trigger ) {
//			try {
//				uploader.logIn( idState.getValue(), passwordState.getValue() );
//			} catch( AuthenticationException e ) {
//				e.printStackTrace();
//			}
			return null;
		}
	}, this.createKey( "login" ) );
	private final ActionOperation uploadOperation = this.createActionOperation( new Action() {
		public org.lgna.croquet.edits.Edit perform( Transaction transaction, Trigger trigger ) {
			VideoEntry entry = new VideoEntry();
			MediaFileSource source = new MediaFileSource( owner.getFile(), "video/quicktime" );
			entry.setMediaSource( source );
			YouTubeMediaGroup mediaGroup = new YouTubeMediaGroup();

			MediaTitle title = new MediaTitle();
			title.setPlainTextContent( titleState.getValue().trim() );
			mediaGroup.setTitle( title );//title

			MediaDescription mediaDescription = new MediaDescription();
			mediaDescription.setPlainTextContent( descriptionState.getValue().trim() );
			mediaGroup.setDescription( mediaDescription );//description

			MediaKeywords keywords = new MediaKeywords();
			String[] arr = tagState.getValue().split( "," );
			for( String s : arr ) {
				keywords.addKeyword( s.trim() );
			}
			mediaGroup.setKeywords( keywords );//tags

			mediaGroup.addCategory( new MediaCategory( YouTubeNamespace.CATEGORY_SCHEME, videoCategoryState.getValue().toString() ) );//category

			mediaGroup.setPrivate( isPrivateState.getValue() );//isPrivate
			try {
				uploader.uploadVideo( entry );
			} catch( IOException e ) {
				e.printStackTrace();
			}
			return null;
		}
	}, this.createKey( "upload" ) );

	public UploadComposite( ExportToYouTubeWizardDialogComposite owner ) {
		super( java.util.UUID.fromString( "5c7ee7ee-1c0e-4a92-ac4e-bca554a0d6bc" ) );
		this.owner = owner;
	}
	public StringState getIdState() {
		return this.idState;
	}
	public StringValue getUsername() {
		return this.username;
	}
	public StringState getPasswordState() {
		return this.passwordState;
	}
	public StringValue getPasswordLabelValue() {
		return this.passwordLabelValue;
	}
	public ActionOperation getLoginOperation() {
		return this.loginOperation;
	}
	public StringValue getTitleLabelValue() {
		return this.titleLabelValue;
	}
	public StringState getTitleState() {
		return this.titleState;
	}
	public BooleanState getIsPrivateState() {
		return this.isPrivateState;
	}
	public StringValue getCategoryValue() {
		return this.categoryValue;
	}
	public ListSelectionState<VideoCategory> getVideoCategoryState() {
		return this.videoCategoryState;
	}
	public StringValue getDescriptionValue() {
		return this.descriptionValue;
	}
	public StringState getDescriptionState() {
		return this.descriptionState;
	}
	public StringValue getTagLabel() {
		return this.tagLabel;
	}
	public StringState getTagState() {
		return this.tagState;
	}
	public ActionOperation getUploadOperation() {
		return this.uploadOperation;
	}

	@Override
	protected UploadView createView() {
		return new UploadView( this );
	}
	public Project getProject() {
		return owner.getProject();
	}
	public File getFile() {
		return owner.getFile();
	}
	@Override
	public void handlePreActivation() {
		super.handlePreActivation();
		System.out.println( "preactivation" );
		getView().setMovie( owner.getFile() );
		//		player.init();
	}
	@Override
	public org.lgna.croquet.GatedComposite.Status getPageStatus( CompletionStep<?> step ) {
		return this.status;
	}
}
