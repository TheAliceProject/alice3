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
public class ProcedureDeclarationOperation extends MethodDeclarationOperation {
	private static java.util.Map< org.lgna.project.ast.UserType< ? >, ProcedureDeclarationOperation > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public static ProcedureDeclarationOperation getInstance( org.lgna.project.ast.UserType< ? > declarationType ) {
		synchronized( map ) {
			ProcedureDeclarationOperation rv = map.get( declarationType );
			if( rv != null ) {
				//pass
			} else {
				rv = new ProcedureDeclarationOperation( declarationType );
				map.put( declarationType, rv );
			}
			return rv;
		}
	}
	private ProcedureDeclarationOperation( org.lgna.project.ast.UserType< ? > declarationType ) {
		super( 
				java.util.UUID.fromString( "dbfbd9df-8a8d-457b-8b02-c358eb9e9124" ), 
				declarationType, true, 
				org.lgna.project.ast.JavaType.VOID_TYPE, false,
				false, false, 
				"", true 
		);
	}

	public void generateAndAddToTransactionHistory( org.lgna.croquet.history.TransactionHistory history, org.lgna.project.ast.UserMethod method ) {
		this.generateAndAddToTransactionHistory( history, method.getDeclaringType(), method );
	}

	public void generateAndAddToTransactionHistory( org.lgna.croquet.history.TransactionHistory history, org.lgna.project.ast.UserType<?> declaringType, org.lgna.project.ast.UserMethod method ) {
		org.lgna.project.ast.NamedUserType namedUserType = (org.lgna.project.ast.NamedUserType)declaringType;

		// Handle the contexts for this step
		if ( org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().getValue() ) {
			org.alice.ide.declarationseditor.TypeState.getInstance().pushGeneratedValue( namedUserType );
			org.alice.ide.declarationseditor.DeclarationTabState.getInstance().pushGeneratedValue( org.alice.ide.declarationseditor.TypeComposite.getInstance( namedUserType ) );
		} else {
			// <kjh/> interesting problem. this context accepts several criteria... which we don't support currently. :(
			org.lgna.project.ast.NamedUserType sceneType = org.alice.ide.IDE.getActiveInstance().getSceneType();
			org.alice.ide.instancefactory.InstanceFactory instanceFactory;
			if ( sceneType == declaringType ) {
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
			assert false;
		}

		try {
			org.lgna.croquet.history.Transaction transaction = org.lgna.croquet.history.Transaction.createAndAddToHistory( history );

			org.lgna.croquet.history.TransactionHistory subTransactionHistory = new org.lgna.croquet.history.TransactionHistory();
			org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( transaction, this, org.lgna.croquet.triggers.ActionEventTrigger.createGeneratorInstance(), subTransactionHistory );

			org.lgna.croquet.history.Transaction nameTransaction = org.lgna.croquet.history.Transaction.createAndAddToHistory( subTransactionHistory );
			org.lgna.croquet.history.StateChangeStep<String> nameChangeStep = org.lgna.croquet.history.StateChangeStep.createAndAddToTransaction( nameTransaction, this.getNameState(), org.lgna.croquet.triggers.DocumentEventTrigger.createGeneratorInstance() );
			nameChangeStep.setEdit( new org.lgna.croquet.edits.StateEdit<String>( nameChangeStep, "", method.getName() ) );

			org.lgna.croquet.history.Transaction commitTransaction = org.lgna.croquet.history.Transaction.createAndAddToHistory( subTransactionHistory );
			org.lgna.croquet.history.CompletionStep<?> commitStep = org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( commitTransaction, this.getCompleteOperation(), org.lgna.croquet.triggers.ActionEventTrigger.createGeneratorInstance(), null );
			commitStep.finish();

			org.lgna.croquet.history.CompletionStep completionStep = transaction.getCompletionStep();
			completionStep.setEdit( new org.alice.ide.croquet.edits.ast.DeclareMethodEdit( completionStep, declaringType, method ) );

			org.lgna.cheshire.ast.BlockStatementGenerator.generateAndAddToTransactionHistory( history, method.body.getValue() );
		} finally {
			if ( org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().getValue() ) {
				org.alice.ide.declarationseditor.DeclarationTabState.getInstance().popGeneratedValue();
				org.alice.ide.declarationseditor.TypeState.getInstance().popGeneratedValue();
			} else {
				org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().popGeneratedValue();
				org.alice.ide.members.ProcedureFunctionControlFlowTabState.getInstance().popGeneratedValue();				
			}
		}
	}

	@Override
	protected org.alice.ide.croquet.components.declaration.DeclarationPanel< ? > createMainComponent( org.lgna.croquet.history.CompletionStep<?> step ) {
		return new org.alice.ide.croquet.components.declaration.ProcedureDeclarationPanel( this );
	}

	@Override
	protected org.alice.ide.croquet.resolvers.NodeStaticGetInstanceKeyedResolver< ProcedureDeclarationOperation > createResolver() {
		return new org.alice.ide.croquet.resolvers.NodeStaticGetInstanceKeyedResolver< ProcedureDeclarationOperation >( this, org.lgna.project.ast.UserType.class, this.getInitialDeclaringType() );
	}
}
