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
package org.alice.stageide.sceneeditor.side.views;

import edu.cmu.cs.dennisc.java.awt.font.TextWeight;
import org.alice.ide.IDE;
import org.alice.ide.ProjectDocumentFrame;
import org.alice.ide.Theme;
import org.alice.ide.ThemeUtilities;
import org.alice.ide.croquet.components.InstanceFactoryPopupButton;
import org.alice.ide.preferences.IsToolBarShowing;
import org.alice.interact.handle.HandleStyle;
import org.alice.stageide.oneshot.DynamicOneShotMenuModel;
import org.alice.stageide.sceneeditor.side.SideComposite;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.ToolPaletteCoreComposite;
import org.lgna.croquet.views.AbstractRadioButtons;
import org.lgna.croquet.views.BooleanStateButton;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.DefaultRadioButtons;
import org.lgna.croquet.views.FlowPanel;
import org.lgna.croquet.views.HorizontalTextPosition;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.LineAxisPanel;
import org.lgna.croquet.views.MigPanel;
import org.lgna.croquet.views.PushButton;
import org.lgna.croquet.views.ScrollPane;
import org.lgna.croquet.views.ToolPaletteTitle;
import org.lgna.croquet.views.ToolPaletteView;
import org.lgna.croquet.views.VerticalTextPosition;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import java.awt.Color;

/**
 * @author Dennis Cosgrove
 */
public class SideView extends BorderPanel {
	private static Border createSeparatorBorder( int top, int bottom, Color color ) {
		return BorderFactory.createMatteBorder( top, 0, bottom, 0, color );
	}

	public SideView( SideComposite composite ) {
		super( composite );

		final Theme theme = ThemeUtilities.getActiveTheme();
		final Color color = theme.getPrimaryBackgroundColor();

		if( IsToolBarShowing.getValue() ) {
			//pass
		} else {
			ProjectDocumentFrame projectDocumentFrame = IDE.getActiveInstance().getDocumentFrame();
			FlowPanel undoRedoPanel = new FlowPanel(
					FlowPanel.Alignment.CENTER,
					projectDocumentFrame.getUndoOperation().createButton(),
					projectDocumentFrame.getRedoOperation().createButton()
					);

			undoRedoPanel.setBorder( createSeparatorBorder( 0, 1, theme.getSecondaryBackgroundColor() ) );
			this.addPageStartComponent( undoRedoPanel );
		}

		MigPanel migPanel = new MigPanel( null, "fill, insets 0, aligny top", "", "" );

		AbstractRadioButtons<HandleStyle> radioButtons = new DefaultRadioButtons<HandleStyle>( composite.getHandleStyleState(), false ) {
			@Override
			protected BooleanStateButton<?> createButtonForItemSelectedState( HandleStyle item, BooleanState itemSelectedState ) {
				PushButton b = itemSelectedState.createPushButton();
				b.setVerticalTextPosition( VerticalTextPosition.BOTTOM );
				b.setHorizontalTextPosition( HorizontalTextPosition.CENTER );
				b.setSelectedColor( theme.getSelectedColor() );
				b.setBackgroundColor( color );
				return b;
			}
		};
		migPanel.addComponent( new LineAxisPanel(
				composite.getHandleStyleState().getSidekickLabel().createLabel( 1.2f ),
				radioButtons
				), "wrap" );

		ToolPaletteView toolPaletteView = composite.getSnapDetailsToolPaletteCoreComposite().getOuterComposite().getView();
		ToolPaletteTitle title = toolPaletteView.getTitle();
		title.setRenderingStyle( ToolPaletteTitle.RenderingStyle.LIGHT_UP_ICON_ONLY );

		migPanel.addComponent( new FlowPanel(
				composite.getIsSnapEnabledState().createCheckBox(),
				title ), "wrap, gapleft 4" );
		migPanel.addComponent( toolPaletteView, "wrap" );

		//this.addComponent( org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().getSidekickLabel().createLabel( 1.4f, edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD ), "align right" );
		migPanel.addComponent( new InstanceFactoryPopupButton( IDE.getActiveInstance().getDocumentFrame().getInstanceFactoryState() ), "wrap" );

		//todo
		//migPanel.addComponent( composite.getAreJointsShowingState().createCheckBox(), "wrap" );

		migPanel.addComponent( DynamicOneShotMenuModel.getInstance().getPopupPrepModel().createPopupButton(), "wrap" );

		ToolPaletteCoreComposite<?>[] toolPaletteCoreComposites = {
				composite.getObjectPropertiesTab(),
				composite.getObjectMarkersTab(),
				composite.getCameraMarkersTab()
		};

		title.setBackgroundColor( color );
		for( ToolPaletteCoreComposite<?> toolPaletteCoreComposite : toolPaletteCoreComposites ) {
			ToolPaletteTitle toolPaletteTitle = toolPaletteCoreComposite.getOuterComposite().getView().getTitle();
			toolPaletteTitle.setBackgroundColor( color );
			toolPaletteTitle.scaleFont( 1.4f );
			toolPaletteTitle.changeFont( TextWeight.BOLD );
			migPanel.addComponent( toolPaletteCoreComposite.getOuterComposite().getView(), "wrap, growx" );
		}
		migPanel.addComponent( new Label(), "wrap, grow, push" );

		ScrollPane scrollPane = new ScrollPane( migPanel );
		this.addCenterComponent( scrollPane );

		migPanel.setBackgroundColor( color );
		scrollPane.setBackgroundColor( color );
		this.setBackgroundColor( color );

		this.setMaximumPreferredWidth( 400 );
	}
}
