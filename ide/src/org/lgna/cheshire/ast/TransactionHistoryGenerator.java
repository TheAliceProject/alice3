package org.lgna.cheshire.ast;

/**
 * @author Kyle J. Harms
 */
public class TransactionHistoryGenerator {

	public org.lgna.croquet.history.TransactionHistory generate( org.lgna.project.ast.UserType<?> declaringType, org.lgna.project.ast.MethodInvocation methodInvocation, org.lgna.project.ast.UserMethod destinationMethod, org.lgna.project.ast.UserField field ) {
		org.lgna.croquet.history.TransactionHistory history = new org.lgna.croquet.history.TransactionHistory();
		
		//we add the new method to the same class as the method we add the invocation to
		org.lgna.project.ast.UserMethod method = (org.lgna.project.ast.UserMethod)methodInvocation.method.getValue();
//		org.alice.ide.ast.declaration.ProcedureDeclarationComposite.getInstance( declaringType ).generateAndAddToTransactionHistory( history, declaringType, method );
		
		org.alice.ide.croquet.edits.ast.DeclareMethodEdit declareMethodEdit = new org.alice.ide.croquet.edits.ast.DeclareMethodEdit( null, declaringType, method );
		org.alice.ide.ast.declaration.ProcedureDeclarationComposite.getInstance( declaringType ).getOperation().addGeneratedTransaction( history, declareMethodEdit );
		

		//cheshire cat does not recover since the procedure invocation drag model is found (albeit with the wrong state).  curses.
		//so we manually place ide in correct configuration.
		org.alice.ide.declarationseditor.TypeState typeState = org.alice.ide.declarationseditor.TypeState.getInstance();
		typeState.addGeneratedStateChangeTransaction( history, null, (org.lgna.project.ast.NamedUserType)destinationMethod.getDeclaringType() );
		
//		org.lgna.croquet.history.Transaction typeSetTransaction = org.lgna.croquet.history.Transaction.createAndAddToHistory( history );
//		org.lgna.croquet.history.CompletionStep typeChangeStep = org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( typeSetTransaction, typeState, org.lgna.croquet.triggers.ChangeEventTrigger.createGeneratorInstance(), null );
//		typeChangeStep.setEdit( new org.lgna.croquet.edits.StateEdit( typeChangeStep, null, destinationMethod.getDeclaringType() ) );
		
		org.alice.ide.declarationseditor.DeclarationTabState declarationTabState = org.alice.ide.declarationseditor.DeclarationTabState.getInstance();
		declarationTabState.addGeneratedStateChangeTransaction( history, null, org.alice.ide.declarationseditor.DeclarationComposite.getInstance( destinationMethod ) );
//		org.lgna.croquet.history.Transaction methodSetTransaction = org.lgna.croquet.history.Transaction.createAndAddToHistory( history );
//		org.lgna.croquet.history.CompletionStep methodChangeStep = org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( methodSetTransaction, declarationTabState, org.lgna.croquet.triggers.ChangeEventTrigger.createGeneratorInstance(), null );
//		methodChangeStep.setEdit( new org.lgna.croquet.edits.StateEdit( methodChangeStep, null, org.alice.ide.declarationseditor.DeclarationComposite.getInstance( destinationMethod ) ) );
		
		org.alice.ide.instancefactory.InstanceFactory instanceFactory;
		org.lgna.project.ast.Expression instanceExpression;
		if( field != null ) {
			instanceFactory = org.alice.ide.instancefactory.ThisFieldAccessFactory.getInstance( field );
			instanceExpression = new org.lgna.project.ast.FieldAccess(
					new org.lgna.project.ast.ThisExpression(),
					field
			);
		} else {
			instanceFactory = org.alice.ide.instancefactory.ThisInstanceFactory.getInstance();
			instanceExpression = new org.lgna.project.ast.ThisExpression();
		}
		org.alice.ide.declarationseditor.TypeState.getInstance().pushGeneratedValue( (org.lgna.project.ast.NamedUserType)destinationMethod.getDeclaringType() );
		org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().pushGeneratedValue( instanceFactory );
		org.alice.ide.members.ProcedureFunctionPropertyTabState.getInstance().pushGeneratedValue( org.alice.ide.members.ProcedureTemplateComposite.getInstance() );
		try {
			org.lgna.project.ast.Expression[] argumentExpressions = {};
			org.lgna.project.ast.ExpressionStatement invocationStatement = org.lgna.project.ast.AstUtilities.createMethodInvocationStatement( instanceExpression, method, argumentExpressions );
			int index = destinationMethod.body.getValue().statements.size();
			destinationMethod.body.getValue().statements.add( invocationStatement );
			org.alice.ide.ast.draganddrop.statement.ProcedureInvocationTemplateDragModel.getInstance( method ).generateAndAddStepsToTransaction( history, invocationStatement, argumentExpressions );
			destinationMethod.body.getValue().statements.remove( index );
		} finally {
			org.alice.ide.members.ProcedureFunctionPropertyTabState.getInstance().popGeneratedValue();
			org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().popGeneratedValue();
			org.alice.ide.declarationseditor.TypeState.getInstance().popGeneratedValue();
		}
		
		
		org.alice.stageide.croquet.models.run.RunOperation runOperation = org.alice.stageide.croquet.models.run.RunOperation.getInstance();
		// Call run
		org.lgna.croquet.history.Transaction transaction = org.lgna.croquet.history.Transaction.createAndAddToHistory( history );

		org.lgna.croquet.history.TransactionHistory subTransactionHistory = new org.lgna.croquet.history.TransactionHistory();
		org.lgna.croquet.history.Transaction closeTransaction = org.lgna.croquet.history.Transaction.createAndAddToHistory( subTransactionHistory );
		org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( closeTransaction, runOperation.getCloseOperation(), org.lgna.croquet.triggers.MouseEventTrigger.createGeneratorInstance(), null );
		org.lgna.croquet.history.CompletionStep<?> commitStep = org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( transaction, runOperation, org.lgna.croquet.triggers.ActionEventTrigger.createGeneratorInstance(), subTransactionHistory );
		commitStep.finish();

//		// Invoke the newly generated method
//		// <kjh/> todo: I hate myself for doing this... this should be more generic... but good enough for now
//		assert declaringType.isAssignableTo( org.lgna.story.Scene.class ) : declaringType;
//		org.lgna.project.ast.AbstractMethod callingMethod = declaringType.findMethod( "myFirstMethod" );
//		assert callingMethod != null : this;
//
//		// todo: so bad. these type may not even match!
//		org.alice.ide.instancefactory.InstanceFactory instanceFactory = org.alice.ide.instancefactory.InstanceFactoryUtilities.getInstanceFactoryForExpression( methodInvocation.expression.getValue() );
//		StatementGenerator statementGenerator = org.alice.ide.ast.draganddrop.statement.ProcedureInvocationTemplateDragModel.getInstance( callingMethod );
//		org.alice.ide.members.TemplateComposite<?> templateComposite = org.alice.ide.members.ProcedureTemplateComposite.getInstance();
//
//		org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().pushGeneratedValue( instanceFactory );
//
//		if( org.alice.ide.croquet.models.ui.preferences.IsAlwaysShowingBlocksState.getInstance().getValue() ) {
//			org.alice.ide.members.ProcedureFunctionPropertyTabState.getInstance().pushGeneratedValue( templateComposite );
//		} else {
//			org.alice.ide.members.ProcedureFunctionControlFlowTabState.getInstance().pushGeneratedValue( templateComposite );
//		}
//		
//		org.lgna.project.ast.ExpressionStatement expressionStatement = org.lgna.project.ast.AstUtilities.createMethodInvocationStatement(instanceFactory.createExpression(), callingMethod, null );
//		statementGenerator.generateAndAddStepsToTransaction( history, expressionStatement, new org.lgna.project.ast.Expression[ 0 ] );
//		
//		if( org.alice.ide.croquet.models.ui.preferences.IsAlwaysShowingBlocksState.getInstance().getValue() ) {
//			org.alice.ide.members.ProcedureFunctionPropertyTabState.getInstance().popGeneratedValue();
//		} else {
//			org.alice.ide.members.ProcedureFunctionControlFlowTabState.getInstance().popGeneratedValue();
//		}
//		org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().popGeneratedValue();


		return history;
	}
}
