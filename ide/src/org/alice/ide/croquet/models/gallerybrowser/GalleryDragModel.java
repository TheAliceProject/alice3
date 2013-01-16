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

package org.alice.ide.croquet.models.gallerybrowser;

/**
 * @author Dennis Cosgrove
 */
public abstract class GalleryDragModel extends org.lgna.croquet.DragModel {
	private static final java.awt.Color INSTANCE_CREATION_BASE_COLOR = new java.awt.Color( 0xf7e4b6 );
	private static final java.awt.Color OTHER_BASE_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 191 );

	public GalleryDragModel( java.util.UUID migrationId ) {
		super( migrationId );
	}

	public abstract String getText();

	public abstract org.lgna.croquet.icon.IconFactory getIconFactory();

	public abstract org.lgna.croquet.Model getLeftButtonClickModel();

	@Override
	public java.util.List<? extends org.lgna.croquet.DropReceptor> createListOfPotentialDropReceptors() {
		org.alice.stageide.StageIDE ide = org.alice.stageide.StageIDE.getActiveInstance();
		if( ide != null ) {
			org.alice.stageide.sceneeditor.StorytellingSceneEditor sceneEditor = ide.getSceneEditor();
			return edu.cmu.cs.dennisc.java.util.Collections.newArrayList( sceneEditor.getDropReceptor() );
		} else {
			return java.util.Collections.emptyList();
		}
	}

	@Override
	public void handleDragStarted( org.lgna.croquet.history.DragStep step ) {
	}

	@Override
	public void handleDragEnteredDropReceptor( org.lgna.croquet.history.DragStep step ) {
	}

	@Override
	public void handleDragExitedDropReceptor( org.lgna.croquet.history.DragStep step ) {
	}

	@Override
	public void handleDragStopped( org.lgna.croquet.history.DragStep step ) {
	}

	public abstract edu.cmu.cs.dennisc.math.AxisAlignedBox getBoundingBox();

	protected abstract boolean isInstanceCreator();

	public final java.awt.Color getBaseColor() {
		if( this.isInstanceCreator() ) {
			return INSTANCE_CREATION_BASE_COLOR;
		} else {
			return OTHER_BASE_COLOR;
		}
	}
}
