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
public abstract class DeclarationOperation< T extends edu.cmu.cs.dennisc.alice.ast.AbstractDeclaration > extends org.lgna.croquet.InputDialogOperation< T > {
	private final DeclaringTypeState declaringTypeState;
	private final ValueComponentTypeState valueComponentTypeState;
	private final IsArrayValueTypeState isArrayValueTypeState;
	private final NameState nameState;
	private final InitializerState initializerState;
	private final boolean isDeclaringTypeEditable;
	private final boolean isValueComponentTypeEditable;
	private final boolean isIsArrayValueTypeEditable;
	private final boolean isNameEditable;
	private final boolean isInitializerEditable;
	private String valueTypeLabelText; 

	public DeclarationOperation( 
			java.util.UUID id, 
			edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice<?> initialDeclaringType,
			boolean isDeclaringTypeEditable,
			edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> initialValueComponentType,
			boolean isValueComponentTypeEditable,
			boolean initialIsArrayValueType,
			boolean isIsArrayValueTypeEditable,
			String initialName,
			boolean isNameEditable,
			edu.cmu.cs.dennisc.alice.ast.Expression initialExpression,
			boolean isInitializerEditable
	) {
		super( org.alice.ide.IDE.PROJECT_GROUP, id );
		if( initialDeclaringType != null || isDeclaringTypeEditable ) {
			this.declaringTypeState = new DeclaringTypeState( this, initialDeclaringType );
		} else {
			this.declaringTypeState = null;
		}
		
		assert initialValueComponentType != null || isValueComponentTypeEditable;
		this.valueComponentTypeState = new ValueComponentTypeState( this, initialValueComponentType );
		this.isArrayValueTypeState = new IsArrayValueTypeState( this, initialIsArrayValueType );
		
		if( initialName != null || isNameEditable ) {
			this.nameState = new NameState( this, initialName );
		} else {
			this.nameState = null;
		}
		if( initialExpression != null || isInitializerEditable ) {
			this.initializerState = new InitializerState( this, initialExpression );
		} else {
			this.initializerState = null;
		}
		
		this.isDeclaringTypeEditable = isDeclaringTypeEditable;
		this.isValueComponentTypeEditable = isValueComponentTypeEditable;
		this.isIsArrayValueTypeEditable = isIsArrayValueTypeEditable;
		this.isNameEditable = isNameEditable;
		this.isInitializerEditable = isInitializerEditable;
	}
	@Override
	protected void localize() {
		super.localize();
		if( this.declaringTypeState != null ) {
			this.declaringTypeState.setLabelText( this.findLocalizedText( "declaringTypeLabel", DeclarationOperation.class ) );
		}
		if( this.valueComponentTypeState != null ) {
			this.valueTypeLabelText = this.findLocalizedText( "valueTypeLabel", DeclarationOperation.class );
		}
		if( this.nameState != null ) {
			this.nameState.setLabelText( this.findLocalizedText( "nameLabel", DeclarationOperation.class ) );
		}
		if( this.initializerState != null ) {
			this.initializerState.setLabelText( this.findLocalizedText( "initializerLabel", DeclarationOperation.class ) );
		}
	}

	public String getValueTypeLabelText() {
		return this.valueTypeLabelText;
	}
	
	public DeclaringTypeState getDeclaringTypeState() {
		return this.declaringTypeState;
	}
	public ValueComponentTypeState getComponentValueTypeState() {
		return this.valueComponentTypeState;
	}
	public IsArrayValueTypeState getIsArrayState() {
		return this.isArrayValueTypeState;
	}
	public NameState getNameState() {
		return this.nameState;
	}
	public InitializerState getInitializerState() {
		return this.initializerState;
	}
	
	public boolean isDeclaringTypeEditable() {
		return this.isDeclaringTypeEditable;
	}
	public boolean isValueComponentTypeEditable() {
		return this.isValueComponentTypeEditable;
	}
	public boolean isIsArrayEditable() {
		return this.isIsArrayValueTypeEditable;
	}
	public boolean isNameEditable() {
		return this.isNameEditable;
	}
	public boolean isInitializerEditable() {
		return this.isInitializerEditable;
	}
	
	protected edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice< ? > getDeclaringType() {
		if( this.declaringTypeState != null ) {
			return this.declaringTypeState.getValue();
		} else {
			return null;
		}
	}
	public edu.cmu.cs.dennisc.alice.ast.AbstractType< ?,?,? > getValueType() {
		edu.cmu.cs.dennisc.alice.ast.AbstractType< ?,?,? > componentType = this.valueComponentTypeState.getValue();
		if( componentType != null ) {
			if( this.isArrayValueTypeState.getValue() ) {
				return componentType.getArrayType();
			} else {
				return componentType;
			}
		} else {
			return null;
		}
	}
	private String getDeclarationName() {
		if( this.nameState != null ) {
			return this.nameState.getValue();
		} else {
			return null;
		}
	}
	private edu.cmu.cs.dennisc.alice.ast.Expression getInitializer() {
		if( this.initializerState != null ) {
			return this.initializerState.getValue();
		} else {
			return null;
		}
	}
	
	protected abstract org.lgna.croquet.edits.Edit< ? > createEdit( org.lgna.croquet.history.InputDialogOperationStep step, edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice< ? > declaringType, edu.cmu.cs.dennisc.alice.ast.AbstractType< ?,?,? > valueType, String declarationName, edu.cmu.cs.dennisc.alice.ast.Expression initializer );
	protected abstract org.alice.ide.croquet.components.declaration.DeclarationPanel< ? > createMainComponent( org.lgna.croquet.history.InputDialogOperationStep step );
	@Override
	protected org.lgna.croquet.components.JComponent< ? > prologue( org.lgna.croquet.history.InputDialogOperationStep step ) {
		return this.createMainComponent( step );
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
