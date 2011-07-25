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

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractDeclareFieldInputDialogOperation extends org.alice.ide.croquet.models.InputDialogWithPreviewOperation<edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice> {
	protected static class EpilogueData {
		private edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice<?> declaringType;
		private edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field;
		private java.util.List< edu.cmu.cs.dennisc.alice.ast.Statement > statements = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		
		public boolean isValid() {
			return this.declaringType != null && this.field != null;
		}
		public edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice< ? > getDeclaringType() {
			return this.declaringType;
		}
		public void setDeclaringType( edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice< ? > declaringType ) {
			this.declaringType = declaringType;
		}
		public edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice getField() {
			return this.field;
		}
		public void setField( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field ) {
			this.field = field;
		}
		public void addStatement( edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
			this.statements.add( statement );
		}
		public edu.cmu.cs.dennisc.alice.ast.Statement[] getStatements() {
			return edu.cmu.cs.dennisc.java.util.CollectionUtilities.createArray( this.statements, edu.cmu.cs.dennisc.alice.ast.Statement.class );
		}
	}
	public AbstractDeclareFieldInputDialogOperation( java.util.UUID individualId ) {
		super( edu.cmu.cs.dennisc.alice.Project.GROUP, individualId );
	}
	
	protected abstract edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice< ? > getDeclaringType();
	protected org.alice.ide.operations.ast.AbstractDeclareFieldInputDialogOperation.EpilogueData fillInEpilogueData( org.alice.ide.operations.ast.AbstractDeclareFieldInputDialogOperation.EpilogueData rv, org.lgna.croquet.history.InputDialogOperationStep step ) {
		rv.setDeclaringType( this.getDeclaringType() );
		return rv;
	}
	protected org.alice.ide.IDE getIDE() {
		return org.alice.ide.IDE.getActiveInstance();
	}
	@Override
	protected final void epilogue(org.lgna.croquet.history.InputDialogOperationStep step, boolean isOk) {
		if( isOk ) {
			EpilogueData data = new EpilogueData();
			this.fillInEpilogueData( data, step );
			if( data.isValid() ) {
				step.commitAndInvokeDo( new DeclareFieldEdit( step, data.getDeclaringType(), data.getField(), data.getStatements() ) );
			} else {
				step.cancel();
			}
		} else {
			step.cancel();
		}
	}
}
