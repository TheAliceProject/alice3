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
public abstract class FieldDeclarationInputDialogBoxOperation extends DeclarationInputDialogOperation< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > {
	protected static class StatementCollector {
		private java.util.List< edu.cmu.cs.dennisc.alice.ast.Statement > doStatements = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		private java.util.List< edu.cmu.cs.dennisc.alice.ast.Statement > undoStatements = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		public void addDoStatement( edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
			this.doStatements.add( statement );
		}
		public edu.cmu.cs.dennisc.alice.ast.Statement[] getDoStatements() {
			return edu.cmu.cs.dennisc.java.util.CollectionUtilities.createArray( this.doStatements, edu.cmu.cs.dennisc.alice.ast.Statement.class );
		}
		public void addUndoStatement( edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
			this.undoStatements.add( statement );
		}
		public edu.cmu.cs.dennisc.alice.ast.Statement[] getUndoStatements() {
			return edu.cmu.cs.dennisc.java.util.CollectionUtilities.createArray( this.undoStatements, edu.cmu.cs.dennisc.alice.ast.Statement.class );
		}
	}

	public FieldDeclarationInputDialogBoxOperation( java.util.UUID id ) {
		super( id );
	}
	protected abstract StatementCollector addStatements( StatementCollector rv );
	@Override
	protected org.lgna.croquet.edits.Edit< ? > createEdit( org.lgna.croquet.history.InputDialogOperationStep step, edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice< ? > declaringType, edu.cmu.cs.dennisc.alice.ast.AbstractType< ?, ?, ? > valueType, java.lang.String declarationName, edu.cmu.cs.dennisc.alice.ast.Expression initializer ) {
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field = new edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice();
		field.valueType.setValue( valueType );
		field.name.setValue( declarationName );
		field.initializer.setValue( initializer );

		StatementCollector statementCollector = new StatementCollector();
		this.addStatements( statementCollector );
		return new org.alice.ide.operations.ast.DeclareFieldEdit( step, this.getDeclaringTypeState().getValue(), field, statementCollector.getDoStatements(), statementCollector.getUndoStatements() );
	}
}
