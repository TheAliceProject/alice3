package org.lgna.cheshire.ast;

/**
 * @author Kyle J. Harms
 */
public class TransactionHistoryGenerator {
//	public org.lgna.croquet.history.TransactionHistory generate( org.lgna.project.ast.AbstractNode ast ) {
//		
//		org.lgna.project.ast.UserMethod method = (org.lgna.project.ast.UserMethod)ast;
//
//		org.lgna.croquet.history.TransactionHistory history = new org.lgna.croquet.history.TransactionHistory();
//		
//		// <kjh/> method invocations should be handled more generically
//		
//		org.lgna.project.ast.UserType<?> declaringType = method.getDeclaringType();
//		org.alice.ide.croquet.models.declaration.ProcedureDeclarationOperation.getInstance( declaringType ).generateAndAddToTransactionHistory( history, method );
//		
//
//		// TODO: Create a run operation, at the end of the tutorial
//		// <kjh/> this should probably be done live, during the tutorial... not poorly inserted here.
//		//transactionHistory.addTransaction( createOpenAndClosePlainDialogOperationTransaction( transactionHistory, org.alice.stageide.croquet.models.run.RunOperation.getInstance() ) );
//		return history;
//	}
	public org.lgna.croquet.history.TransactionHistory generate( org.lgna.project.ast.UserType<?> declaringType, org.lgna.project.ast.MethodInvocation methodInvocation ) {
		org.lgna.croquet.history.TransactionHistory history = new org.lgna.croquet.history.TransactionHistory();

		//we add the new method to the same class as the method we add the invocation to
		org.lgna.project.ast.UserMethod method = (org.lgna.project.ast.UserMethod)methodInvocation.method.getValue();
		org.alice.ide.croquet.models.declaration.ProcedureDeclarationOperation.getInstance( declaringType ).generateAndAddToTransactionHistory( history, declaringType, method );

		//todo: add methodInvocation to owner

		return history;
	}
}
