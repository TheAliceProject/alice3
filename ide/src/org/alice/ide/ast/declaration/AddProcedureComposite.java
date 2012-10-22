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
package org.alice.ide.ast.declaration;

/**
 * @author Dennis Cosgrove
 */
public final class AddProcedureComposite extends AddMethodComposite {
	private static edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap<org.lgna.project.ast.UserType<?>, AddProcedureComposite> map = edu.cmu.cs.dennisc.java.util.Collections.newInitializingIfAbsentHashMap();

	public static AddProcedureComposite getInstance( org.lgna.project.ast.UserType<?> declaringType ) {
		return map.getInitializingIfAbsent( declaringType, new edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap.Initializer<org.lgna.project.ast.UserType<?>, AddProcedureComposite>() {
			public AddProcedureComposite initialize( org.lgna.project.ast.UserType<?> declaringType ) {
				return new AddProcedureComposite( declaringType );
			}
		} );
	}

	private AddProcedureComposite( org.lgna.project.ast.UserType<?> declaringType ) {
		super( java.util.UUID.fromString( "1e7af2e9-2ce0-4c7e-9ddd-9af001601660" ), new Details()
				.valueComponentType( ApplicabilityStatus.APPLICABLE_BUT_NOT_DISPLAYED, org.lgna.project.ast.JavaType.VOID_TYPE )
				.name( ApplicabilityStatus.EDITABLE )
				, declaringType );
	}

	@Override
	protected org.alice.ide.ast.declaration.views.AddProcedureView createView() {
		return new org.alice.ide.ast.declaration.views.AddProcedureView( this );
	}

	@Override
	public void pushGeneratedContexts( org.lgna.croquet.edits.Edit<?> ownerEdit ) {
		super.pushGeneratedContexts( ownerEdit );
		assert ownerEdit instanceof org.alice.ide.croquet.edits.ast.DeclareMethodEdit : ownerEdit;
		org.alice.ide.croquet.edits.ast.DeclareMethodEdit declareMethodEdit = (org.alice.ide.croquet.edits.ast.DeclareMethodEdit)ownerEdit;

		org.lgna.project.ast.UserType<?> declaringType = declareMethodEdit.getDeclaringType();
		org.lgna.project.ast.NamedUserType namedUserType = (org.lgna.project.ast.NamedUserType)declaringType;
		org.lgna.project.ast.UserMethod method = declareMethodEdit.getMethod();

		if( org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().getValue() ) {
			org.alice.ide.declarationseditor.TypeState.getInstance().pushGeneratedValue( namedUserType );
			org.alice.ide.declarationseditor.DeclarationTabState.getInstance().pushGeneratedValue( org.alice.ide.declarationseditor.TypeComposite.getInstance( namedUserType ) );
		} else {
			org.lgna.project.ast.NamedUserType sceneType = org.alice.ide.IDE.getActiveInstance().getSceneType();
			org.alice.ide.instancefactory.InstanceFactory instanceFactory;
			if( sceneType == declaringType ) {
				instanceFactory = org.alice.ide.instancefactory.ThisInstanceFactory.getInstance();
			} else {
				instanceFactory = null;
				for( org.lgna.project.ast.UserField field : sceneType.fields ) {
					if( declaringType.isAssignableFrom( field.getValueType() ) ) {
						instanceFactory = org.alice.ide.instancefactory.ThisFieldAccessFactory.getInstance( field );
					}
				}
			}
			assert instanceFactory != null : method;
			org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().pushGeneratedValue( instanceFactory );
			org.alice.ide.members.ProcedureFunctionControlFlowTabState.getInstance().pushGeneratedValue( org.alice.ide.members.ProcedureTemplateComposite.getInstance() );
		}
	}

	@Override
	public void popGeneratedContexts( org.lgna.croquet.edits.Edit<?> ownerEdit ) {
		if( org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().getValue() ) {
			org.alice.ide.declarationseditor.DeclarationTabState.getInstance().popGeneratedValue();
			org.alice.ide.declarationseditor.TypeState.getInstance().popGeneratedValue();
		} else {
			org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().popGeneratedValue();
			org.alice.ide.members.ProcedureFunctionControlFlowTabState.getInstance().popGeneratedValue();
		}
		super.popGeneratedContexts( ownerEdit );
	}

	@Override
	public void addGeneratedSubTransactions( org.lgna.croquet.history.TransactionHistory subTransactionHistory, org.lgna.croquet.edits.Edit<?> ownerEdit ) throws org.lgna.croquet.UnsupportedGenerationException {
		assert ownerEdit instanceof org.alice.ide.croquet.edits.ast.DeclareMethodEdit : ownerEdit;
		org.alice.ide.croquet.edits.ast.DeclareMethodEdit declareMethodEdit = (org.alice.ide.croquet.edits.ast.DeclareMethodEdit)ownerEdit;
		org.lgna.project.ast.UserMethod method = declareMethodEdit.getMethod();

		this.getNameState().addGeneratedStateChangeTransaction( subTransactionHistory, "", method.getName() );

		super.addGeneratedSubTransactions( subTransactionHistory, ownerEdit );
	}

	@Override
	public void addGeneratedPostTransactions( org.lgna.croquet.history.TransactionHistory ownerTransactionHistory, org.lgna.croquet.edits.Edit<?> edit ) throws org.lgna.croquet.UnsupportedGenerationException {
		super.addGeneratedPostTransactions( ownerTransactionHistory, edit );
		assert edit instanceof org.alice.ide.croquet.edits.ast.DeclareMethodEdit : edit;
		org.alice.ide.croquet.edits.ast.DeclareMethodEdit declareMethodEdit = (org.alice.ide.croquet.edits.ast.DeclareMethodEdit)edit;
		org.lgna.project.ast.UserMethod method = declareMethodEdit.getMethod();
		org.lgna.cheshire.ast.BlockStatementGenerator.generateAndAddToTransactionHistory( ownerTransactionHistory, method.body.getValue() );
	}

	@Override
	protected org.alice.ide.croquet.resolvers.NodeStaticGetInstanceKeyedResolver<AddProcedureComposite> createResolver() {
		return new org.alice.ide.croquet.resolvers.NodeStaticGetInstanceKeyedResolver<AddProcedureComposite>( this, org.lgna.project.ast.UserType.class, this.getDeclaringType() );
	}
}
