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
package org.alice.stageide.gallerybrowser.uri;

import org.alice.ide.croquet.models.gallerybrowser.GalleryDragModel;
import org.alice.stageide.perspectives.scenesetup.SetupScenePerspectiveComposite;
import org.lgna.croquet.Model;
import org.lgna.croquet.history.UserActivity;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class ThingClsUriIteratingOperation extends ResourceKeyUriIteratingOperation {
	private static class SingletonHolder {
		private static ThingClsUriIteratingOperation instance = new ThingClsUriIteratingOperation();
	}

	/*package-private*/static ThingClsUriIteratingOperation getInstance() {
		return SingletonHolder.instance;
	}

	private ThingClsUriIteratingOperation() {
		super( UUID.fromString( "b3dcac55-4522-4014-b658-93ac8d516c1a" ) );
	}

	@Override
	protected int getStepCount() {
		return 2;
	}

	@Override
	protected Model getNext( List<UserActivity> subSteps, Iterator<Model> iteratingData ) {
		if( this.thingCls != null ) {
			switch( subSteps.size() ) {
			case 0:
				SetupScenePerspectiveComposite composite = SetupScenePerspectiveComposite.getInstance();
				GalleryDragModel dragModel = composite.getDragModelForCls( this.thingCls );
				if( dragModel != null ) {
					return dragModel.getLeftButtonClickModel();
				} else {
					return null;
				}
			case 1:
				return this.getMergeTypeOperation();
			default:
				return null;
			}
		} else {
			return null;
		}
	}
}
