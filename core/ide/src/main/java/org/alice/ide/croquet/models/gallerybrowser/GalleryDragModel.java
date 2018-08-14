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

package org.alice.ide.croquet.models.gallerybrowser;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import org.alice.stageide.StageIDE;
import org.alice.stageide.sceneeditor.StorytellingSceneEditor;
import org.lgna.croquet.DragModel;
import org.lgna.croquet.DropReceptor;
import org.lgna.croquet.Model;
import org.lgna.croquet.history.DragStep;
import org.lgna.croquet.icon.IconFactory;

import java.awt.Dimension;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class GalleryDragModel extends DragModel {
	private static final Dimension DEFAULT_LARGE_ICON_SIZE = new Dimension( 160, 120 );

	protected static Dimension getDefaultLargeIconSize() {
		return DEFAULT_LARGE_ICON_SIZE;
	}

	public GalleryDragModel( UUID migrationId ) {
		super( migrationId );
	}

	public final boolean isClickAndClackAppropriate() {
		//todo
		return false;
	}

	public abstract String getText();

	public abstract IconFactory getIconFactory();

	public Dimension getIconSize() {
		return DEFAULT_LARGE_ICON_SIZE;
	}

	public abstract Model getLeftButtonClickModel();

	@Override
	public List<? extends DropReceptor> createListOfPotentialDropReceptors() {
		StageIDE ide = StageIDE.getActiveInstance();
		if( ide != null ) {
			StorytellingSceneEditor sceneEditor = ide.getSceneEditor();
			return Lists.newArrayList( sceneEditor.getDropReceptor() );
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public void handleDragStarted( DragStep step ) {
	}

	@Override
	public void handleDragEnteredDropReceptor( DragStep step ) {
	}

	@Override
	public void handleDragExitedDropReceptor( DragStep step ) {
	}

	@Override
	public void handleDragStopped( DragStep step ) {
	}

	public abstract AxisAlignedBox getBoundingBox();

	public abstract boolean placeOnGround();

	public abstract boolean isInstanceCreator();

	public boolean isUserDefinedModel() {
		return false;
	}
}
