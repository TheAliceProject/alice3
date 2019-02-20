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

import edu.cmu.cs.dennisc.java.util.Lists;
import org.alice.ide.croquet.models.gallerybrowser.GalleryDragModel;
import org.alice.stageide.gallerybrowser.shapes.AxesDragModel;
import org.alice.stageide.gallerybrowser.shapes.BillboardDragModel;
import org.alice.stageide.gallerybrowser.shapes.BoxDragModel;
import org.alice.stageide.gallerybrowser.shapes.ConeDragModel;
import org.alice.stageide.gallerybrowser.shapes.CylinderDragModel;
import org.alice.stageide.gallerybrowser.shapes.DiscDragModel;
import org.alice.stageide.gallerybrowser.shapes.GroundDragModel;
import org.alice.stageide.gallerybrowser.shapes.ShapeDragModel;
import org.alice.stageide.gallerybrowser.shapes.SphereDragModel;
import org.alice.stageide.gallerybrowser.shapes.TextModelDragModel;
import org.alice.stageide.gallerybrowser.shapes.TorusDragModel;
import org.alice.stageide.gallerybrowser.views.ShapesTabView;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class ShapesTab extends GalleryTab {
	// @formatter:off
	private final List<ShapeDragModel> dragModels = Collections.unmodifiableList(
					Lists.newArrayList(
						DiscDragModel.getInstance()
						, ConeDragModel.getInstance()
						, CylinderDragModel.getInstance()
						, SphereDragModel.getInstance()
						, TorusDragModel.getInstance()
						, BoxDragModel.getInstance()
						, TextModelDragModel.getInstance()
						, BillboardDragModel.getInstance()
						, AxesDragModel.getInstance()
						, GroundDragModel.getInstance()
					) );
	// @formatter:on
	public ShapesTab() {
		super( UUID.fromString( "1e616f0e-4c57-460c-a4a7-919addbfc9d8" ) );
	}

	@Override
	protected ShapesTabView createView() {
		return new ShapesTabView( this );
	}

	public List<ShapeDragModel> getDragModels() {
		return this.dragModels;
	}

	public GalleryDragModel getDragModelForCls( Class<?> cls ) {
		String simpleName = cls.getSimpleName();
		if( simpleName.length() > 1 ) {
			if( ( simpleName.charAt( 0 ) == 'S' ) && Character.isUpperCase( simpleName.charAt( 1 ) ) ) {
				String desiredSimpleName = simpleName.substring( 1 ) + "DragModel";
				for( ShapeDragModel dragModel : this.dragModels ) {
					if( desiredSimpleName.contentEquals( dragModel.getClass().getSimpleName() ) ) {
						return dragModel;
					}
				}
			}
		}
		return null;
	}
}
