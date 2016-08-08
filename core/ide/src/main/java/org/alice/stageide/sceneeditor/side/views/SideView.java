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

/**
 * @author Dennis Cosgrove
 */
public class SideView extends org.lgna.croquet.views.BorderPanel {
	private static javax.swing.border.Border createSeparatorBorder( int top, int bottom, java.awt.Color color ) {
		return javax.swing.BorderFactory.createMatteBorder( top, 0, bottom, 0, color );
	}

	public SideView( org.alice.stageide.sceneeditor.side.SideComposite composite ) {
		super( composite );

		final org.alice.ide.Theme theme = org.alice.ide.ThemeUtilities.getActiveTheme();
		final java.awt.Color color = theme.getPrimaryBackgroundColor();

		if( org.alice.ide.preferences.IsToolBarShowing.getValue() ) {
			//pass
		} else {
			org.alice.ide.ProjectDocumentFrame projectDocumentFrame = org.alice.ide.IDE.getActiveInstance().getDocumentFrame();
			org.lgna.croquet.views.FlowPanel undoRedoPanel = new org.lgna.croquet.views.FlowPanel(
					org.lgna.croquet.views.FlowPanel.Alignment.CENTER,
					projectDocumentFrame.getUndoOperation().createButton(),
					projectDocumentFrame.getRedoOperation().createButton()
					);

			undoRedoPanel.setBorder( createSeparatorBorder( 0, 1, theme.getSecondaryBackgroundColor() ) );
			this.addPageStartComponent( undoRedoPanel );
		}

		org.lgna.croquet.views.MigPanel migPanel = new org.lgna.croquet.views.MigPanel( null, "fill, insets 0, aligny top", "", "" );

		org.lgna.croquet.views.AbstractRadioButtons<org.alice.interact.handle.HandleStyle> radioButtons = new org.lgna.croquet.views.DefaultRadioButtons<org.alice.interact.handle.HandleStyle>( composite.getHandleStyleState(), false ) {
			@Override
			protected org.lgna.croquet.views.BooleanStateButton<?> createButtonForItemSelectedState( org.alice.interact.handle.HandleStyle item, org.lgna.croquet.BooleanState itemSelectedState ) {
				org.lgna.croquet.views.PushButton b = itemSelectedState.createPushButton();
				b.setVerticalTextPosition( org.lgna.croquet.views.VerticalTextPosition.BOTTOM );
				b.setHorizontalTextPosition( org.lgna.croquet.views.HorizontalTextPosition.CENTER );
				b.setSelectedColor( theme.getSelectedColor() );
				b.setBackgroundColor( color );
				return b;
			}
		};
		migPanel.addComponent( new org.lgna.croquet.views.LineAxisPanel(
				composite.getHandleStyleState().getSidekickLabel().createLabel( 1.2f ),
				radioButtons
				), "wrap" );

		org.lgna.croquet.views.ToolPaletteView toolPaletteView = composite.getSnapDetailsToolPaletteCoreComposite().getOuterComposite().getView();
		org.lgna.croquet.views.ToolPaletteTitle title = toolPaletteView.getTitle();
		title.setRenderingStyle( org.lgna.croquet.views.ToolPaletteTitle.RenderingStyle.LIGHT_UP_ICON_ONLY );

		migPanel.addComponent( new org.lgna.croquet.views.FlowPanel(
				composite.getIsSnapEnabledState().createCheckBox(),
				title ), "wrap, gapleft 4" );
		migPanel.addComponent( toolPaletteView, "wrap" );

		//this.addComponent( org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().getSidekickLabel().createLabel( 1.4f, edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD ), "align right" );
		migPanel.addComponent( new org.alice.ide.croquet.components.InstanceFactoryPopupButton( org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getInstanceFactoryState() ), "wrap" );

		//todo
		//migPanel.addComponent( composite.getAreJointsShowingState().createCheckBox(), "wrap" );

		migPanel.addComponent( org.alice.stageide.oneshot.DynamicOneShotMenuModel.getInstance().getPopupPrepModel().createPopupButton(), "wrap" );

		org.lgna.croquet.ToolPaletteCoreComposite<?>[] toolPaletteCoreComposites = {
				composite.getObjectPropertiesTab(),
				composite.getObjectMarkersTab(),
				composite.getCameraMarkersTab()
		};

		title.setBackgroundColor( color );
		for( org.lgna.croquet.ToolPaletteCoreComposite<?> toolPaletteCoreComposite : toolPaletteCoreComposites ) {
			org.lgna.croquet.views.ToolPaletteTitle toolPaletteTitle = toolPaletteCoreComposite.getOuterComposite().getView().getTitle();
			toolPaletteTitle.setBackgroundColor( color );
			toolPaletteTitle.scaleFont( 1.4f );
			toolPaletteTitle.changeFont( edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD );
			migPanel.addComponent( toolPaletteCoreComposite.getOuterComposite().getView(), "wrap, growx" );
		}
		migPanel.addComponent( new org.lgna.croquet.views.Label(), "wrap, grow, push" );

		org.lgna.croquet.views.ScrollPane scrollPane = new org.lgna.croquet.views.ScrollPane( migPanel );
		this.addCenterComponent( scrollPane );

		migPanel.setBackgroundColor( color );
		scrollPane.setBackgroundColor( color );
		this.setBackgroundColor( color );

		this.setMaximumPreferredWidth( 400 );
	}
}
