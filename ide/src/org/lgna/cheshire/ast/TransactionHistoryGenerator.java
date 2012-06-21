package org.lgna.cheshire.ast;

/**
 * @author Kyle J. Harms
 */
public class TransactionHistoryGenerator {

	public org.lgna.croquet.history.TransactionHistory generate( org.lgna.project.ast.UserType<?> declaringType, org.lgna.project.ast.MethodInvocation methodInvocation ) {
		org.lgna.croquet.history.TransactionHistory history = new org.lgna.croquet.history.TransactionHistory();
		
		
		// Call run
		org.lgna.croquet.history.Transaction transaction = org.lgna.croquet.history.Transaction.createAndAddToHistory( history );

		// Maybe for the close?
//		org.lgna.croquet.history.TransactionHistory subTransactionHistory = new org.lgna.croquet.history.TransactionHistory();
//		org.lgna.croquet.history.Transaction commitTransaction = org.lgna.croquet.history.Transaction.createAndAddToHistory( subTransactionHistory );
		org.lgna.croquet.history.CompletionStep<?> commitStep = org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( transaction, org.alice.stageide.croquet.models.run.RunOperation.getInstance(), org.lgna.croquet.triggers.ActionEventTrigger.createGeneratorInstance(), null );
		commitStep.finish();
		
		

		//we add the new method to the same class as the method we add the invocation to
		org.lgna.project.ast.UserMethod method = (org.lgna.project.ast.UserMethod)methodInvocation.method.getValue();
		org.alice.ide.ast.declaration.ProcedureDeclarationComposite.getInstance( declaringType ).generateAndAddToTransactionHistory( history, declaringType, method );

		//todo: add methodInvocation
		//todo: run operation?

		
		
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
