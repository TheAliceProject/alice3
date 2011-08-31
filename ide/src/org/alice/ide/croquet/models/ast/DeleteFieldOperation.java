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
public class DeleteFieldOperation extends DeleteMemberOperation< org.lgna.project.ast.UserField > {
	private static java.util.Map< org.lgna.project.ast.UserField, DeleteFieldOperation > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public static synchronized DeleteFieldOperation getInstance( org.lgna.project.ast.UserField field ) {
		return getInstance( field, field.getDeclaringType() );
	}
	public static synchronized DeleteFieldOperation getInstance( org.lgna.project.ast.UserField field, org.lgna.project.ast.UserType< ? > declaringType ) {
		DeleteFieldOperation rv = map.get( field );
		if( rv != null ) {
			//pass
		} else {
			rv = new DeleteFieldOperation( field, declaringType );
			map.put( field, rv );
		}
		return rv;
	}

	//todo
	//note: instance not preserved and restored
	//in the case where it is undone across sessions, it will not know what to pass to the scene editor
	private transient Object instance = null;
	private DeleteFieldOperation( org.lgna.project.ast.UserField field, org.lgna.project.ast.UserType< ? > declaringType ) {
		super( java.util.UUID.fromString( "29e5416c-c0c4-4b6d-9146-5461d5c73c42" ), field, declaringType );
	}
	@Override
	protected java.lang.Class< org.lgna.project.ast.UserField > getNodeParameterType() {
		return org.lgna.project.ast.UserField.class;
	}
	@Override
	protected org.lgna.project.ast.NodeListProperty< org.lgna.project.ast.UserField > getNodeListProperty( org.lgna.project.ast.UserType< ? > declaringType ) {
		return declaringType.fields;
	}
	@Override
	protected boolean isClearToDelete( org.lgna.project.ast.UserField field ) {
		java.util.List< org.lgna.project.ast.FieldAccess > references = org.alice.ide.IDE.getActiveInstance().getFieldAccesses( field );
		final int N = references.size();
		if( N > 0 ) {
			StringBuffer sb = new StringBuffer();
			sb.append( "Unable to delete property named \"" );
			sb.append( field.name.getValue() );
			sb.append( "\" because it has " );
			if( N == 1 ) {
				sb.append( "an access refrence" );
			} else {
				sb.append( N );
				sb.append( " access refrences" );
			}
			sb.append( " to it.\nYou must remove " );
			if( N == 1 ) {
				sb.append( "this reference" );
			} else {
				sb.append( "these references" );
			}
			sb.append( " if you want to delete \"" );
			sb.append( field.name.getValue() );
			sb.append( "\" ." );
			org.lgna.croquet.Application.getActiveInstance().showMessageDialog( sb.toString() );
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public void doOrRedoInternal( boolean isDo ) {
		this.instance = org.alice.ide.IDE.getActiveInstance().getSceneEditor().getInstanceInJavaForUndo( this.getMember() );
		super.doOrRedoInternal( isDo );
	}
	@Override
	public void undoInternal() {
		if( this.instance != null ) {
			org.alice.ide.IDE.getActiveInstance().getSceneEditor().putInstanceForInitializingPendingField( this.getMember(), this.instance );
		}
		super.undoInternal();
	}
}
