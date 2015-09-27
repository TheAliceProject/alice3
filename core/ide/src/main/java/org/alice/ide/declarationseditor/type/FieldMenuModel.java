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
package org.alice.ide.declarationseditor.type;

/**
 * @author Dennis Cosgrove
 */
public final class FieldMenuModel extends MemberMenuModel<org.lgna.project.ast.UserField> {
	private static edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap<org.lgna.project.ast.UserField, FieldMenuModel> map = edu.cmu.cs.dennisc.java.util.Maps.newInitializingIfAbsentHashMap();

	public static FieldMenuModel getInstance( org.lgna.project.ast.UserField field ) {
		return map.getInitializingIfAbsent( field, new edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap.Initializer<org.lgna.project.ast.UserField, FieldMenuModel>() {
			@Override
			public FieldMenuModel initialize( org.lgna.project.ast.UserField key ) {
				java.util.List<org.lgna.croquet.StandardMenuItemPrepModel> prepModels = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
				prepModels.add( org.alice.ide.ast.rename.RenameFieldComposite.getInstance( key ).getLaunchOperation().getMenuItemPrepModel() );
				prepModels.add( org.alice.ide.croquet.models.ast.DeleteFieldOperation.getInstance( key ).getMenuItemPrepModel() );

				if( key.getValueType().isAssignableTo( org.lgna.story.SMarker.class ) ) {
					prepModels.add( org.alice.stageide.sceneeditor.side.MarkerColorIdCascade.getInstance( key ).getMenuModel() );
				}

				return new FieldMenuModel( key, prepModels );
			}
		} );
	}

	private FieldMenuModel( org.lgna.project.ast.UserField field, java.util.List<org.lgna.croquet.StandardMenuItemPrepModel> prepModels ) {
		super( java.util.UUID.fromString( "cb192f6c-0f06-462d-9e4c-baa19ca49ce7" ), field, prepModels );
	}
}
