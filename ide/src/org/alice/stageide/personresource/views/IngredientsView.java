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

package org.alice.stageide.personresource.views;

/**
 * @author Dennis Cosgrove
 */
public class IngredientsView extends org.lgna.croquet.components.MigPanel {
	public static final java.awt.Color BACKGROUND_COLOR = new java.awt.Color( 173, 167, 208 );
	public static final java.awt.Color SELECTED_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( java.awt.Color.YELLOW, 1.0, 0.3, 1.0 );
	public static final java.awt.Color UNSELECTED_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( BACKGROUND_COLOR, 1.0, 0.9, 0.8 );

	public IngredientsView( final org.alice.stageide.personresource.IngredientsComposite composite ) {
		super( composite, "insets 0, fill" );

		java.awt.Color backgroundColor = BACKGROUND_COLOR;
		org.lgna.croquet.components.PushButton isLifeStageLockedButton = composite.getIsLifeStageLockedState().createPushButton();
		isLifeStageLockedButton.setBackgroundColor( backgroundColor );
		isLifeStageLockedButton.setSelectedColor( backgroundColor );
		//isLifeStageLockedButton.tightenUpMargin();
		this.addComponent( isLifeStageLockedButton );
		this.addComponent( composite.getLifeStageState().getSidekickLabel().createLabel(), "align right" );
		this.addComponent( new HorizontalWrapList( composite.getLifeStageState(), 1 ), "push" );
		this.addComponent( composite.getRandomize().createButton(), "wrap" );

		this.addComponent( composite.getGenderState().getSidekickLabel().createLabel(), "align right, skip" );
		this.addComponent( new HorizontalWrapList( composite.getGenderState(), 1 ), "wrap" );

		this.addComponent( composite.getBaseSkinToneState().getSidekickLabel().createLabel(), "align right, skip" );
		this.addComponent( new HorizontalWrapList( composite.getBaseSkinToneState(), 1 ), "wrap" );

		org.lgna.croquet.components.FolderTabbedPane tabbedPane = composite.getBodyHeadTabState().createFolderTabbedPane();
		tabbedPane.setBackgroundColor( backgroundColor );
		this.addComponent( tabbedPane, "span 4, grow" );
		//
		//		org.lgna.croquet.components.BorderPanel centerPanel = new org.lgna.croquet.components.BorderPanel.Builder().pageStart( rowSpringPanel ).center( tabbedPane ).build();
		//
		//		this.addPageStartComponent( composite.getRandomize().createButton() );
		//		this.addCenterComponent( centerPanel );
		this.setBackgroundColor( backgroundColor );
	}
}
