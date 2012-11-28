package org.lgna.cheshire.ast;

/**
 * @author Kyle J. Harms
 */
public class TransactionHistoryGenerator {
	public org.lgna.croquet.history.TransactionHistory generate( org.lgna.project.ast.UserType<?> declaringType, org.lgna.project.ast.MethodInvocation methodInvocation, org.lgna.project.ast.UserMethod destinationMethod, org.lgna.project.ast.UserField field ) throws org.lgna.croquet.UnsupportedGenerationException {
		org.lgna.croquet.history.TransactionHistory history = new org.lgna.croquet.history.TransactionHistory();

		//we add the new method to the same class as the method we add the invocation to
		org.lgna.project.ast.UserMethod method = (org.lgna.project.ast.UserMethod)methodInvocation.method.getValue();

		org.alice.ide.croquet.edits.ast.DeclareMethodEdit declareMethodEdit = new org.alice.ide.croquet.edits.ast.DeclareMethodEdit( null, declaringType, method );
		org.alice.ide.ast.declaration.AddProcedureComposite.getInstance( declaringType ).getOperation().addGeneratedTransaction( history, org.lgna.croquet.triggers.ActionEventTrigger.createGeneratorInstance(), declareMethodEdit );

		//cheshire cat does not recover since the procedure invocation drag model is found (albeit with the wrong state).  curses.
		//so we manually place ide in correct configuration.
		org.alice.ide.declarationseditor.TypeState.getInstance().addGeneratedStateChangeTransaction( history, null, (org.lgna.project.ast.NamedUserType)destinationMethod.getDeclaringType() );
		org.alice.ide.declarationseditor.DeclarationsEditorComposite.getInstance().getTabState().addGeneratedStateChangeTransaction( history, null, org.alice.ide.declarationseditor.DeclarationComposite.getInstance( destinationMethod ) );

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

		return history;
	}
}
