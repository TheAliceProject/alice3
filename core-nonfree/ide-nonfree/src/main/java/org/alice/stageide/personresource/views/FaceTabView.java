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
package org.alice.stageide.personresource.views;

import edu.cmu.cs.dennisc.javax.swing.icons.ColorIcon;
import org.alice.stageide.personresource.FaceTabComposite;
import org.alice.stageide.personresource.views.renderers.FaceListCellRenderer;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.ImmutableDataSingleSelectListState;
import org.lgna.croquet.views.List;
import org.lgna.croquet.views.MigPanel;
import org.lgna.croquet.views.ScrollPane;
import org.lgna.croquet.views.ToggleButton;
import org.lgna.story.resources.sims2.BaseEyeColor;
import org.lgna.story.resources.sims2.BaseFace;

import javax.swing.BorderFactory;
import java.awt.Color;

/**
 * @author Dennis Cosgrove
 */
public class FaceTabView extends MigPanel {
	public FaceTabView( FaceTabComposite composite ) {
		super( composite, "insets 2, fillx", "[right][left, grow, shrink]", "" );
		Color backgroundColor = IngredientsView.BACKGROUND_COLOR;
		this.setBackgroundColor( backgroundColor );
		this.setBorder( BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );

		List<BaseFace> faceList = new HorizontalWrapList<BaseFace>( composite.getBaseFaceState(), -1, FaceListCellRenderer.getInstance() );
		faceList.setBackgroundColor( IngredientsView.BACKGROUND_COLOR );
		this.addComponent( composite.getBaseFaceState().getSidekickLabel().createLabel(), "top" );
		ScrollPane faceScrollPane = new ScrollPane( faceList );
		faceScrollPane.setHorizontalScrollbarPolicy( ScrollPane.HorizontalScrollbarPolicy.NEVER );
		faceScrollPane.setBothScrollBarIncrements( 66, 66 );
		this.addComponent( faceScrollPane, "wrap, grow, shrink" );

		this.addComponent( composite.getBaseEyeColorState().getSidekickLabel().createLabel() );
		final boolean IS_LIST_DESIRED = false;
		if( IS_LIST_DESIRED ) {
			this.addComponent( new HorizontalWrapList( composite.getBaseEyeColorState(), 1 ), "wrap, shrink" );
		} else {
			ImmutableDataSingleSelectListState<BaseEyeColor> eyeColorState = composite.getBaseEyeColorState();
			BaseEyeColor[] baseEyeColors = BaseEyeColor.values();
			String constraint = "split " + baseEyeColors.length;
			for( BaseEyeColor baseEyeColor : baseEyeColors ) {
				Color awtColor = baseEyeColor.getColor();
				BooleanState itemSelectedState = eyeColorState.getItemSelectedState( baseEyeColor );
				itemSelectedState.initializeIfNecessary();
				itemSelectedState.setTextForBothTrueAndFalse( "" );
				itemSelectedState.setIconForBothTrueAndFalse( new ColorIcon( awtColor ) );
				ToggleButton toggleButton = itemSelectedState.createToggleButton();
				toggleButton.tightenUpMargin( IngredientsView.COLOR_BUTTON_MARGIN );
				this.addComponent( toggleButton, constraint );
				constraint = "";
			}
		}
	}
}
