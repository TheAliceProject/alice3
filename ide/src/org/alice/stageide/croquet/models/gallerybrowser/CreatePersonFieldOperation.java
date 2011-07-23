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
package org.alice.stageide.croquet.models.gallerybrowser;

/**
 * @author Dennis Cosgrove
 */
class CreateFieldFromPersonPane extends org.alice.ide.declarationpanes.CreateLargelyPredeterminedFieldPane {
	private org.lookingglassandalice.storytelling.resources.sims2.Person person;
	public CreateFieldFromPersonPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType, org.lookingglassandalice.storytelling.resources.sims2.Person person ) {
		super( declaringType, person.getClass(), null );
		this.person = person;
	}
	public org.lookingglassandalice.storytelling.resources.sims2.Person getPerson() {
		return this.person;
	}
}

/**
 * @author Dennis Cosgrove
 */
public class CreatePersonFieldOperation extends AbstractGalleryDeclareFieldOperation {
	private static class SingletonHolder {
		private static CreatePersonFieldOperation instance = new CreatePersonFieldOperation();
	}
	public static CreatePersonFieldOperation getInstance() {
		return SingletonHolder.instance;
	}
	private CreatePersonFieldOperation() {
		super( java.util.UUID.fromString( "84f3a391-4a6c-4a10-82da-6b6231937949" ) );
	}
	@Override
	protected CreateFieldFromPersonPane prologue( org.lgna.croquet.history.InputDialogOperationStep step ) {
		CreatePersonOperation createPersonOperation = new CreatePersonOperation( org.lgna.croquet.Application.INHERIT_GROUP );
		createPersonOperation.fire();
		
		org.lookingglassandalice.storytelling.resources.sims2.Person person = createPersonOperation.getPerson();
		
		if( person != null ) {
			org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
			edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType = ide.getSceneType();
			return new CreateFieldFromPersonPane( declaringType, person );
		} else {
			return null;
		}
	}
	@Override
	protected org.alice.ide.operations.ast.AbstractDeclareFieldInputDialogOperation.Initializer fillInInitializer( org.alice.ide.operations.ast.AbstractDeclareFieldInputDialogOperation.Initializer rv, org.lgna.croquet.history.InputDialogOperationStep step ) {
		super.fillInInitializer( rv, step );
		return rv;
	}
	@Override
	protected edu.cmu.cs.dennisc.pattern.Tuple2< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice, org.lookingglassandalice.storytelling.resources.sims2.Person > createFieldAndInstance(org.lgna.croquet.history.InputDialogOperationStep step ) {
		CreateFieldFromPersonPane createFieldFromPersonPane = step.getMainPanel();
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field = createFieldFromPersonPane.getInputValue();
		if( field != null ) {
			//ide.getSceneEditor().handleFieldCreation( declaringType, field, person );
			return edu.cmu.cs.dennisc.pattern.Tuple2.createInstance( field, createFieldFromPersonPane.getPerson() );
		} else {
			return null;
		}
	}
}
