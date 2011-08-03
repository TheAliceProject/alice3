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
package org.alice.ide.operations.ast;

import org.lgna.project.ast.UserField;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractEditFieldOperation extends org.alice.ide.croquet.models.InputDialogWithPreviewOperation<org.lgna.project.ast.UserField> {
	private UserField field;

	private org.lgna.project.ast.FieldModifierFinalVolatileOrNeither prevFinalVolatileOrNeither;
	private org.lgna.project.ast.AbstractType<?,?,?> prevValueType;
	private String prevName;
	private org.lgna.project.ast.Expression prevInitializer;
	
	public AbstractEditFieldOperation( org.lgna.croquet.Group group, java.util.UUID individualId, String name, UserField field ) {
		super( group, individualId );
		this.setName( name );
		this.field = field;
		
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: AbstractEditFieldOperation" );
	}
	protected UserField getField() {
		return this.field;
	}
	protected org.alice.ide.declarationpanes.EditFieldPane prologue( org.lgna.croquet.history.InputDialogOperationStep step, java.util.Set< UserField > referencedFields, java.util.Set< UserField > reassignedFields ) {
		if( this.field != null ) {
			this.prevFinalVolatileOrNeither = field.finalVolatileOrNeither.getValue();
			this.prevValueType = field.valueType.getValue();
			this.prevName = this.field.getName();
			this.prevInitializer = field.initializer.getValue();
			return new org.alice.ide.declarationpanes.EditFieldPane( field, referencedFields.contains( field ), reassignedFields.contains( field ) );
		} else {
			return null;
		}
	}
	@Override
	protected void epilogue( org.lgna.croquet.history.InputDialogOperationStep step, boolean isOk ) {
		if( isOk ) {
			org.alice.ide.declarationpanes.EditFieldPane editFieldPane = step.getMainPanel();
			UserField tempField = editFieldPane.getInputValue();
			if( tempField != null ) {
				final org.lgna.project.ast.FieldModifierFinalVolatileOrNeither nextFinalVolatileOrNeither = tempField.finalVolatileOrNeither.getValue();
				final org.lgna.project.ast.AbstractType<?,?,?> nextValueType = tempField.valueType.getValue();
				final String nextName = tempField.getName();
				final org.lgna.project.ast.Expression nextInitializer = tempField.initializer.getValue();
				step.commitAndInvokeDo( new org.alice.ide.ToDoEdit( step ) {
					@Override
					protected final void doOrRedoInternal( boolean isDo ) {
						field.finalVolatileOrNeither.setValue( nextFinalVolatileOrNeither );
						field.valueType.setValue( nextValueType );
						field.name.setValue( nextName );
						field.initializer.setValue( nextInitializer );
					}
					@Override
					protected final void undoInternal() {
						field.finalVolatileOrNeither.setValue( prevFinalVolatileOrNeither );
						field.valueType.setValue( prevValueType );
						field.name.setValue( prevName );
						field.initializer.setValue( prevInitializer );
					}
					@Override
					protected StringBuilder updatePresentation( StringBuilder rv, java.util.Locale locale ) {
						rv.append( "edit: " );
						org.lgna.project.ast.NodeUtilities.safeAppendRepr( rv, field, locale );
						return rv;
					}
				} );
			} else {
				step.cancel();
			}
		} else {
			step.cancel();
		}
	}
}
