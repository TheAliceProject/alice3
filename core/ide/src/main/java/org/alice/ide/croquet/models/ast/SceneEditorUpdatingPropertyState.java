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

package org.alice.ide.croquet.models.ast;

/**
 * @author Dennis Cosgrove
 */
public class SceneEditorUpdatingPropertyState extends org.alice.ide.ast.PropertyState {
	private static edu.cmu.cs.dennisc.map.MapToMap<org.lgna.project.ast.UserField, org.lgna.project.ast.JavaMethod, SceneEditorUpdatingPropertyState> mapToMap = edu.cmu.cs.dennisc.map.MapToMap.newInstance();

	public static synchronized SceneEditorUpdatingPropertyState getInstanceForSetter( org.lgna.project.ast.UserField field, org.lgna.project.ast.JavaMethod setter ) {
		return mapToMap.getInitializingIfAbsent( field, setter, new edu.cmu.cs.dennisc.map.MapToMap.Initializer<org.lgna.project.ast.UserField, org.lgna.project.ast.JavaMethod, SceneEditorUpdatingPropertyState>() {
			public SceneEditorUpdatingPropertyState initialize( org.lgna.project.ast.UserField field, org.lgna.project.ast.JavaMethod setter ) {
				return new SceneEditorUpdatingPropertyState( field, setter );
			}
		} );
	}

	public static synchronized SceneEditorUpdatingPropertyState getInstanceForGetter( org.lgna.project.ast.UserField field, org.lgna.project.ast.JavaMethod getter ) {
		return getInstanceForSetter( field, org.lgna.project.ast.AstUtilities.getSetterForGetter( getter ) );
	}

	private final org.lgna.project.ast.UserField field;

	private SceneEditorUpdatingPropertyState( org.lgna.project.ast.UserField field, org.lgna.project.ast.JavaMethod setter ) {
		super( org.lgna.croquet.Application.PROJECT_GROUP, java.util.UUID.fromString( "f38ed248-1d68-43eb-b2c0-09ac62bd748e" ), setter );
		this.field = field;
	}

	public org.lgna.project.ast.UserField getField() {
		return this.field;
	}

	@Override
	protected void handleTruthAndBeautyValueChange( org.lgna.project.ast.Expression nextValue ) {
		super.handleTruthAndBeautyValueChange( nextValue );
		if( nextValue instanceof org.lgna.project.ast.NullLiteral ) {
			//do nothing for null literals
		}
		else {
			org.lgna.project.ast.Expression e;
			if( this.field == null ) {
				e = org.lgna.project.ast.ThisExpression.createInstanceThatCanExistWithoutAnAncestorType( org.alice.stageide.sceneeditor.StorytellingSceneEditor.getInstance().getActiveSceneType() );
			}
			else {
				e = new org.lgna.project.ast.FieldAccess( new org.lgna.project.ast.ThisExpression(), this.field );
			}
			org.lgna.project.ast.AbstractParameter parameter = this.getSetter().getRequiredParameters().get( 0 );
			org.lgna.project.ast.SimpleArgument argument = new org.lgna.project.ast.SimpleArgument( parameter, nextValue );
			org.lgna.project.ast.MethodInvocation methodInvocation = new org.lgna.project.ast.MethodInvocation( e, this.getSetter(), argument );
			org.lgna.project.ast.Statement s = new org.lgna.project.ast.ExpressionStatement( methodInvocation );
			org.alice.stageide.sceneeditor.StorytellingSceneEditor.getInstance().executeStatements( s );
		}
	}
}
