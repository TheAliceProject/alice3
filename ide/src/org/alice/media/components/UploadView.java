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
package org.alice.media.components;

import org.alice.media.MoviePlayer;
import org.alice.media.UploadComposite;
import org.lgna.croquet.components.BorderPanel;
import org.lgna.croquet.components.CheckBox;
import org.lgna.croquet.components.GridPanel;
import org.lgna.croquet.components.Label;
import org.lgna.croquet.components.PasswordField;
import org.lgna.croquet.components.SwingAdapter;
import org.lgna.croquet.components.TextField;

/**
 * @author Matt May
 */
public class UploadView extends BorderPanel {
	public UploadView( UploadComposite composite ) {
		super( composite );
		this.addComponent( new UserNameAndPasswordComponent( composite ), Constraint.PAGE_START );
//		MoviePlayerComposite moviePlayerComposite = new MoviePlayerComposite( composite.getProject() );
		GridPanel gridPane = GridPanel.createGridPane( 1, 2 );
		SwingAdapter swingAdapter = new SwingAdapter( new MoviePlayer() );
		gridPane.addComponent( swingAdapter );
		gridPane.addComponent( new VideoInfoComponent( composite ) );
		this.addComponent( gridPane, Constraint.CENTER );
		this.addComponent( composite.getUploadOperation().createButton(), Constraint.PAGE_END );
	}

	private class UserNameAndPasswordComponent extends BorderPanel {
		public UserNameAndPasswordComponent( UploadComposite composite ) {
			GridPanel bottom = GridPanel.createGridPane( 1, 3 );
			bottom.addComponent( new Label() );
			bottom.addComponent( composite.getLoginOperation().createButton() );
			bottom.addComponent( new Label() );
			this.addComponent( bottom, Constraint.PAGE_END );
			TextField userName = composite.getIdState().createTextField();
			PasswordField password = composite.getPasswordState().createPasswordField();
			GridPanel top = GridPanel.createGridPane( 2, 2 );
			top.addComponent( composite.getUsername().createImmutableTextArea() );
			top.addComponent( userName );
			top.addComponent( composite.getPasswordLabelValue().createImmutableTextArea() );
			top.addComponent( password );
			this.addComponent( top, Constraint.CENTER );
		}
	}

	private class VideoInfoComponent extends BorderPanel {
		public VideoInfoComponent( UploadComposite composite ) {
			GridPanel titlePanel = GridPanel.createGridPane( 2, 1 );
			titlePanel.addComponent( composite.getTitleLabelValue().createImmutableTextArea() );
			TextField titleField = composite.getTitleState().createTextField();
			titlePanel.addComponent( titleField );
			this.addComponent( titlePanel, Constraint.PAGE_START );
			GridPanel detailPanel = GridPanel.createGridPane( 3, 1 );
			detailPanel.addComponent( composite.getCategoryValue().createImmutableTextArea() );
			detailPanel.addComponent( composite.getVideoCategoryState().createComboBox() );
			CheckBox isPrivateBox = composite.getIsPrivateState().createCheckBox();
			detailPanel.addComponent( isPrivateBox );
			GridPanel middle = GridPanel.createGridPane( 2, 1 );
			BorderPanel topBorder = new BorderPanel();
			topBorder.addComponent( composite.getDescriptionValue().createImmutableTextArea(), Constraint.PAGE_START );
			topBorder.addComponent( composite.getDescriptionState().createTextArea(), Constraint.CENTER );
			BorderPanel bottomBorder = new BorderPanel();
			bottomBorder.addComponent( composite.getTagLabel().createImmutableTextArea(), Constraint.PAGE_START );
			bottomBorder.addComponent( composite.getTagState().createTextArea(), Constraint.CENTER );
			middle.addComponent( topBorder );
			middle.addComponent( bottomBorder );
			this.addComponent( middle, Constraint.CENTER );
			this.addComponent( detailPanel, Constraint.PAGE_END );
		}
	}
}
