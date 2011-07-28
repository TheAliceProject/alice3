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

package org.alice.ide.croquet.models.declaration;

/**
 * @author Dennis Cosgrove
 */
public abstract class DeclarationInputDialogOperation< T extends edu.cmu.cs.dennisc.alice.ast.AbstractDeclaration > extends org.lgna.croquet.InputDialogOperation< T > {
	public static class DeclaringTypeState extends org.lgna.croquet.DefaultItemState< edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice > {
		public DeclaringTypeState() {
			super( org.lgna.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString( "7b2413e0-a945-49d1-800b-4fba4f0bc741" ), org.alice.ide.croquet.codecs.NodeCodec.getInstance( edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice.class ) );
		}
	}
	public static class ValueComponentTypeState extends org.lgna.croquet.DefaultItemState< edu.cmu.cs.dennisc.alice.ast.AbstractType > {
		public ValueComponentTypeState() {
			super( org.lgna.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString( "7b2413e0-a945-49d1-800b-4fba4f0bc741" ), org.alice.ide.croquet.codecs.NodeCodec.getInstance( edu.cmu.cs.dennisc.alice.ast.AbstractType.class ) );
		}
	}
	public static class IsArrayState extends org.lgna.croquet.BooleanState {
		public IsArrayState( boolean defaultValue ) {
			super( org.lgna.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString( "0d05e96f-4eee-4b50-8065-c6a1aff9a573" ), defaultValue );
		}
	}
	public static class NameState extends org.lgna.croquet.StringState {
		public NameState() {
			super( org.lgna.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString( "cc8de0c1-261f-431e-a62c-60346a8fedff" ), "" );
		}
	}
	public static class InitializerState extends org.lgna.croquet.DefaultItemState< edu.cmu.cs.dennisc.alice.ast.Expression > {
		public InitializerState() {
			super( org.lgna.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString( "4edd354e-da3c-400d-9c55-66da924c09a7" ), org.alice.ide.croquet.codecs.NodeCodec.getInstance( edu.cmu.cs.dennisc.alice.ast.Expression.class ) );
		}
	}
	private final DeclaringTypeState declaringTypeState = new DeclaringTypeState();
	private final NameState nameState = new NameState();
	private final ValueComponentTypeState valueComponentTypeState = new ValueComponentTypeState();
	private final IsArrayState isArrayState = new IsArrayState( false );
	private final InitializerState initializerState = new InitializerState();
	public DeclarationInputDialogOperation( java.util.UUID id ) {
		super( org.alice.ide.IDE.PROJECT_GROUP, id );
	}

	public DeclaringTypeState getDeclaringTypeState() {
		return this.declaringTypeState;
	}
	public ValueComponentTypeState getComponentValueTypeState() {
		return this.valueComponentTypeState;
	}
	public org.lgna.croquet.StringState getNameState() {
		return this.nameState;
	}
	public InitializerState getInitializerState() {
		return this.initializerState;
	}
	
	private edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice< ? > getDeclaringType() {
		return this.getDeclaringTypeState().getValue();
	}
	private edu.cmu.cs.dennisc.alice.ast.AbstractType< ?,?,? > getValueType() {
		edu.cmu.cs.dennisc.alice.ast.AbstractType< ?,?,? > componentType = this.valueComponentTypeState.getValue();
		if( componentType != null ) {
			if( this.isArrayState.getValue() ) {
				return componentType.getArrayType();
			} else {
				return componentType;
			}
		} else {
			return null;
		}
	}
	private String getDeclarationName() {
		return this.nameState.getValue();
	}
	private edu.cmu.cs.dennisc.alice.ast.Expression getInitializer() {
		return this.initializerState.getValue();
	}
	
	protected abstract org.lgna.croquet.edits.Edit< ? > createEdit( org.lgna.croquet.history.InputDialogOperationStep step, edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice< ? > declaringType, edu.cmu.cs.dennisc.alice.ast.AbstractType< ?,?,? > valueType, String declarationName, edu.cmu.cs.dennisc.alice.ast.Expression initializer );
	@Override
	protected org.lgna.croquet.components.JComponent< ? > prologue( org.lgna.croquet.history.InputDialogOperationStep step ) {
		return null;
	}
	@Override
	protected final void epilogue( org.lgna.croquet.history.InputDialogOperationStep step, boolean isCommit ) {
		if( isCommit ) {
			org.lgna.croquet.edits.Edit< ? > edit = this.createEdit( step, this.getDeclaringType(), this.getValueType(), this.getDeclarationName(), this.getInitializer() );
			if( edit != null ) {
				step.commitAndInvokeDo( edit );
			} else {
				step.cancel();
			}
		} else {
			step.cancel();
		}
	}
}
