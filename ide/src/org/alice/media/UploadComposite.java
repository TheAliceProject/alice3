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

import org.alice.media.components.UploadView;
import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.Application;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.Composite;
import org.lgna.croquet.ListSelectionState;
import org.lgna.croquet.StringState;
import org.lgna.croquet.components.BorderPanel.Constraint;
import org.lgna.croquet.history.Transaction;
import org.lgna.croquet.simple.SimpleApplication;
import org.lgna.croquet.triggers.Trigger;

/**
 * @author Matt May
 */
public class UploadComposite extends Composite<UploadView>{
	private static class SingletonHolder {
		private static UploadComposite instance = new UploadComposite();
	}
	public static UploadComposite getInstance() {
		return SingletonHolder.instance;
	}
	
	private final StringState idState = this.createStringState( "", this.createKey( "id" ) );
	private final StringState passwordState = this.createStringState( "", this.createKey( "password" ) );
	private final StringState titleState = this.createStringState( "Alice Video", this.createKey( "title" ) );
	private final BooleanState isPrivateState = this.createBooleanState( true, this.createKey( "isPrivate" ) );
	private final ListSelectionState<VideoCategory> videoCategoryState = this.createListSelectionState( VideoCategory.class, VideoCategory.SCIENCE_AND_TECHNOLOGY, this.createKey( "videoCategory" ) );
	private final StringState descriptionState = this.createStringState( "", this.createKey( "description" ) );
	private final StringState tagState = this.createStringState( "", this.createKey( "tag" ) );
	private final ActionOperation loginOperation = this.createActionOperation( new Action() {
		public void perform( Transaction transaction, Trigger trigger ) {
		}
	}, this.createKey( "login" ) );
	private final ActionOperation uploadOperation = this.createActionOperation( new Action() {
		public void perform( Transaction transaction, Trigger trigger ) {
		}
	}, this.createKey( "upload" ) );
	private UploadComposite() {
		super( java.util.UUID.fromString( "5c7ee7ee-1c0e-4a92-ac4e-bca554a0d6bc" ) );
	}
	public StringState getIdState() {
		return this.idState;
	}
	public StringState getPasswordState() {
		return this.passwordState;
	}
	public ActionOperation getLoginOperation() {
		return this.loginOperation;
	}
	public StringState getTitleState() {
		return this.titleState;
	}
	public BooleanState getIsPrivateState() {
		return this.isPrivateState;
	}
	public ListSelectionState<VideoCategory> getVideoCategoryState() {
		return this.videoCategoryState;
	}
	public StringState getDescriptionState() {
		return this.descriptionState;
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
	
	public static void main( String[] args ) {
		Application application = new SimpleApplication();
		application.initialize( args );
		application.getFrame().setSize( 640, 480 );
		
		application.getFrame().getContentPanel().addComponent( UploadComposite.getInstance().getView(), Constraint.CENTER );
		application.getFrame().setVisible( true );
		
	}
}
