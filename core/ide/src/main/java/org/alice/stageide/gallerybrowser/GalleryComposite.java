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

package org.alice.stageide.gallerybrowser;

import org.alice.ide.croquet.models.gallerybrowser.GalleryDragModel;
import org.alice.ide.croquet.models.ui.preferences.IsIncludingImportAndExportType;
import org.alice.ide.icons.Icons;
import org.alice.stageide.gallerybrowser.search.croquet.SearchTab;
import org.alice.stageide.gallerybrowser.search.croquet.views.SearchTabView;
import org.alice.stageide.gallerybrowser.views.GalleryView;
import org.alice.stageide.icons.TorusIcon;
import org.alice.stageide.modelresource.TreeUtilities;
import org.lgna.croquet.ImmutableDataTabState;
import org.lgna.croquet.SimpleComposite;

import java.awt.Dimension;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class GalleryComposite extends SimpleComposite<GalleryView> {
	private final TreeOwningGalleryTab resourceBasedTab =
		new TreeOwningGalleryTab( TreeUtilities.getClassTreeState(), "ResourceBasedTab");
	private final TreeOwningGalleryTab themeBasedTab =
		new TreeOwningGalleryTab( TreeUtilities.getThemeTreeState(), "ThemeBasedTab" );
	private final TreeOwningGalleryTab groupBasedTab =
		new TreeOwningGalleryTab( TreeUtilities.getGroupTreeState(), "GroupBasedTab" );
	private final ShapesTab shapesTab = new ShapesTab();
	private final SearchTab searchTab = new SearchTab();
	private final ImportTab importTab = new ImportTab();
	private final TreeOwningGalleryTab myGalleryTab =
		new TreeOwningGalleryTab( TreeUtilities.getUserTreeState(), "CustomGalleryTab" );
	private final ImmutableDataTabState<GalleryTab> tabState = this.createImmutableTabState( "tabState", 0, GalleryTab.class,
			IsIncludingImportAndExportType.getValue()
				? new GalleryTab[] { this.resourceBasedTab, this.themeBasedTab, this.groupBasedTab, this.searchTab, this.shapesTab, this.importTab, this.myGalleryTab }
				: new GalleryTab[] { this.resourceBasedTab, this.themeBasedTab, this.groupBasedTab, this.searchTab, this.shapesTab, this.myGalleryTab }
			);

	public GalleryComposite() {
		super( UUID.fromString( "c3dd549e-6622-4641-913b-27b08dc4dba5" ) );
	}

	@Override
	protected void localize() {
		super.localize();
		this.tabState.setItemIconForBothTrueAndFalse( this.shapesTab, new TorusIcon( new Dimension( 24, 24 ) ) );
		this.tabState.setItemIconForBothTrueAndFalse( this.resourceBasedTab, Icons.EMPTY_HEIGHT_ICON_SMALL );
		this.tabState.setItemIconForBothTrueAndFalse( this.themeBasedTab, Icons.EMPTY_HEIGHT_ICON_SMALL );
		this.tabState.setItemIconForBothTrueAndFalse( this.groupBasedTab, Icons.EMPTY_HEIGHT_ICON_SMALL );
		this.tabState.setItemIconForBothTrueAndFalse( this.searchTab, SearchTabView.SEARCH_ICON );
		this.tabState.setItemIconForBothTrueAndFalse( this.importTab, Icons.FOLDER_ICON_SMALL );
		this.tabState.setItemIconForBothTrueAndFalse( this.myGalleryTab, Icons.FOLDER_ICON_SMALL );
	}

	public ImmutableDataTabState<GalleryTab> getTabState() {
		return this.tabState;
	}

	@Override
	protected GalleryView createView() {
		return new GalleryView( this );
	}

	public GalleryDragModel getDragModelForCls( Class<?> cls ) {
		return this.shapesTab.getDragModelForCls( cls );
	}

	public void modelUpdated() {
		resourceBasedTab.modelUpdated();
		searchTab.modelUpdated();
		myGalleryTab.modelUpdated();
	}
}
