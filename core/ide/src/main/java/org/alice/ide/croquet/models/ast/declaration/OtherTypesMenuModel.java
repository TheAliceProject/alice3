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
package org.alice.ide.croquet.models.ast.declaration;

import org.alice.ide.IDE;
import org.alice.ide.ProjectStack;
import org.alice.stageide.ast.StoryApiSpecificAstUtilities;
import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.CascadeLineSeparator;
import org.lgna.croquet.CascadeMenuModel;
import org.lgna.croquet.imp.cascade.BlankNode;
import org.lgna.project.Project;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.NamedUserType;

import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class OtherTypesMenuModel extends CascadeMenuModel<AbstractType> {
	private static final boolean IS_SCENE_TYPE_DESIRED = false;

	private static class SingletonHolder {
		private static OtherTypesMenuModel instance = new OtherTypesMenuModel();
	}

	public static OtherTypesMenuModel getInstance() {
		return SingletonHolder.instance;
	}

	private OtherTypesMenuModel() {
		super( UUID.fromString( "909d8fb3-f1a0-4f21-9bbf-a871ea04d1a0" ) );
	}

	private NamedUserType getSceneType() {
		Project project = ProjectStack.peekProject();
		if( project != null ) {
			return StoryApiSpecificAstUtilities.getSceneTypeFromProject( project );
		} else {
			return null;
		}
	}

	public boolean isEmpty() {
		if( IDE.getActiveInstance().getApiConfigurationManager().getSecondarySelectableJavaTypes().size() > 0 ) {
			return false;
		} else {
			if( IS_SCENE_TYPE_DESIRED ) {
				return this.getSceneType() == null;
			} else {
				return true;
			}
		}
	}

	@Override
	protected void updateBlankChildren( List<CascadeBlankChild> blankChildren, BlankNode<AbstractType> blankNode ) {
		if( IS_SCENE_TYPE_DESIRED ) {
			NamedUserType sceneType = this.getSceneType();
			if( sceneType != null ) {
				blankChildren.add( TypeFillIn.getInstance( sceneType ) );
				blankChildren.add( CascadeLineSeparator.getInstance() );
			}
		}

		List<JavaType> otherTypes = IDE.getActiveInstance().getApiConfigurationManager().getSecondarySelectableJavaTypes();
		for( JavaType otherType : otherTypes ) {
			if( otherType != null ) {
				blankChildren.add( TypeFillIn.getInstance( otherType ) );
			} else {
				blankChildren.add( CascadeLineSeparator.getInstance() );
			}
		}
	}
}
