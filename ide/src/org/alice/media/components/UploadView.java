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

import java.io.File;

import org.alice.media.UploadComposite;
import org.lgna.croquet.components.CheckBox;
import org.lgna.croquet.components.ComboBox;
import org.lgna.croquet.components.TextArea;
import org.lgna.croquet.components.TextField;

/**
 * @author Matt May
 */
public class UploadView extends org.lgna.croquet.components.BorderPanel {

	//private final MoviePlayerComposite moviePlayerComposite;

	private final org.lgna.croquet.components.Panel youtubeDetailsPanel;

	public UploadView( UploadComposite composite ) {
		super( composite, 24, 0 );

		org.lgna.croquet.components.MigPanel loginPanel = new org.lgna.croquet.components.MigPanel( null, "fill, inset 0", "", "[]0[]4[]0[]4[]" );
		loginPanel.addComponent( composite.getIdState().getSidekickLabel().createImmutableTextField(), "wrap" );

		loginPanel.addComponent( composite.getIdState().createTextField(), "wrap, growx" );

		loginPanel.addComponent( composite.getPasswordState().getSidekickLabel().createImmutableTextField(), "wrap" );

		loginPanel.addComponent( composite.getPasswordState().createPasswordField(), "wrap, growx" );

		loginPanel.addComponent( composite.getLoginComposite().getOperation().createButton(), "wrap" );

		org.lgna.croquet.components.GridBagPanel panel = new org.lgna.croquet.components.GridBagPanel();

		final int TOP_INSET = 2;
		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;

		gbc.insets.top = TOP_INSET;
		panel.addComponent( composite.getTitleState().getSidekickLabel().createImmutableTextField(), gbc );
		gbc.insets.top = 0;

		TextField titleField = composite.getTitleState().createTextField();
		panel.addComponent( titleField, gbc );

		gbc.insets.top = TOP_INSET;
		panel.addComponent( composite.getDescriptionState().getSidekickLabel().createImmutableTextField(), gbc );
		gbc.insets.top = 0;

		gbc.weighty = 1.0;
		TextArea description = composite.getDescriptionState().createTextArea();
		description.getAwtComponent().setRows( 4 );
		//		description.getAwtComponent().setLineWrap( true );
		description.setMaximumSizeClampedToPreferredSize( false );

		org.lgna.croquet.components.ScrollPane descriptionScrollPane = new org.lgna.croquet.components.ScrollPane( description );
		panel.addComponent( descriptionScrollPane, gbc );
		gbc.weighty = 0.0;

		gbc.insets.top = TOP_INSET;
		panel.addComponent( composite.getTagsState().getSidekickLabel().createImmutableTextField(), gbc );
		gbc.insets.top = 0;

		gbc.weighty = 1.0;
		TextArea tags = composite.getTagsState().createTextArea();
		//		tags.getAwtComponent().setLineWrap( true );
		tags.getAwtComponent().setRows( 2 );
		org.lgna.croquet.components.ScrollPane tagsScrollPane = new org.lgna.croquet.components.ScrollPane( tags );
		panel.addComponent( tagsScrollPane, gbc );

		gbc.weighty = 0.0;

		gbc.insets.top = TOP_INSET;
		panel.addComponent( composite.getVideoCategoryState().getSidekickLabel().createImmutableTextField(), gbc );
		gbc.insets.top = 0;

		ComboBox<String> categories = composite.getVideoCategoryState().getPrepModel().createComboBox();
		panel.addComponent( categories, gbc );

		gbc.insets.top = TOP_INSET;
		CheckBox isPrivateBox = composite.getIsPrivateState().createCheckBox();
		panel.addComponent( isPrivateBox, gbc );
		gbc.insets.top = 0;

		//panel.addComponent( composite.getUploadOperation().getOperation().createButton(), gbc );

		this.youtubeDetailsPanel = panel;

		this.addCenterComponent( new org.lgna.croquet.components.BorderPanel.Builder().pageStart( loginPanel ).center( this.youtubeDetailsPanel ).build() );

		this.addLineStartComponent( new org.lgna.croquet.components.PageAxisPanel(
				new org.lgna.croquet.components.Label( "Preview:" ),
				new PreviewVideoView(),
				org.lgna.croquet.components.BoxUtilities.createVerticalSliver( 24 ),
				composite.getExportToFileOperation().createButton() ) );
		//		this.moviePlayerComposite = new MoviePlayerComposite( composite.getFile() );
		//
		//		org.lgna.croquet.components.PageAxisPanel sidePanel = new org.lgna.croquet.components.PageAxisPanel();
		//		sidePanel.addComponent( panel );
		//		sidePanel.addComponent( composite.getSaveToFileOperation().createButton() );
		//		//		this.addComponent( new PreserveAspectRatioPanel( panel, new Dimension( 16, 9 ) ), "wrap, aligny top" );
		//		this.addLineEndComponent( sidePanel );

		//this.disableable = (java.util.List)edu.cmu.cs.dennisc.java.util.Collections.newLinkedList( titleField, categories, description, tags );
	}

	public org.lgna.croquet.components.Panel getYoutubeDetailsPanel() {
		return this.youtubeDetailsPanel;
	}

	public void setMovie( File file ) {
		//moviePlayerComposite.setMovie( file );
	}
}
