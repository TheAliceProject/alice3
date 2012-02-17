package uist.ast;

import edu.cmu.cs.dennisc.pattern.Crawlable;

public class AstTransactionGeneratorVisitor implements edu.cmu.cs.dennisc.pattern.Crawler {

	protected org.lgna.croquet.history.TransactionHistory transactionHistory;

	public AstTransactionGeneratorVisitor( org.lgna.croquet.history.TransactionHistory transactionHistory ) {
		this.transactionHistory = transactionHistory;
	}

	public void visit(org.lgna.project.ast.AssertStatement assertStatement) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.errln(assertStatement);
	}

	public void visit(org.lgna.project.ast.BlockStatement blockStatement) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.errln(blockStatement, blockStatement.statements);
	}

	public void visit(org.lgna.project.ast.Comment comment) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.errln(comment);
	}

	public void visit(org.lgna.project.ast.ConditionalStatement conditionalStatement) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.errln(conditionalStatement);
	}

	public void visit(org.lgna.project.ast.ConstructorBlockStatement constructorBlockStatement) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.errln(constructorBlockStatement);
	}

	public void visit(org.lgna.project.ast.CountLoop countLoop) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.errln(countLoop);

		org.lgna.project.ast.BlockStatement blockStatement = (org.lgna.project.ast.BlockStatement)countLoop.getParent();
		int index = blockStatement.statements.indexOf( countLoop );
		org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair = new org.alice.ide.ast.draganddrop.BlockStatementIndexPair(blockStatement, index);
		org.alice.ide.croquet.models.ast.cascade.statement.CountLoopInsertCascade countLoopInsertCascade = org.alice.ide.croquet.models.ast.cascade.statement.CountLoopInsertCascade.getInstance(blockStatementIndexPair);

		org.lgna.croquet.history.Transaction transaction = org.lgna.croquet.history.TransactionManager.createSimulatedTransactionForCascade( this.transactionHistory, countLoopInsertCascade );
		org.lgna.croquet.edits.Edit edit = new org.alice.ide.croquet.edits.ast.InsertStatementEdit(transaction.getCompletionStep(), blockStatementIndexPair, countLoop);

		transaction.getCompletionStep().setEdit( edit );
		this.transactionHistory.addTransaction(transaction);
	}

	public void visit(org.lgna.project.ast.DoInOrder doInOrder) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.errln(doInOrder);
	}

	public void visit(org.lgna.project.ast.DoTogether doTogether) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.errln(doTogether);
	}

	public void visit(org.lgna.project.ast.EachInArrayTogether eachInArrayTogether) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.errln(eachInArrayTogether);
	}

	public void visit(org.lgna.project.ast.EachInIterableTogether eachInIterableTogether) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.errln(eachInIterableTogether);
	}

	public void visit(org.lgna.project.ast.ExpressionStatement expressionStatement) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.errln(expressionStatement);
	}

	public void visit(org.lgna.project.ast.ForEachInArrayLoop forEachInArrayLoop) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.errln(forEachInArrayLoop);
	}

	public void visit(org.lgna.project.ast.ForEachInIterableLoop forEachInIterableLoop) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.errln(forEachInIterableLoop);
	}

	public void visit(org.lgna.project.ast.LocalDeclarationStatement localDeclarationStatement) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.errln(localDeclarationStatement);
	}

	public void visit(org.lgna.project.ast.ReturnStatement returnStatement) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.errln(returnStatement);
	}

	public void visit(org.lgna.project.ast.SuperConstructorInvocationStatement superConstructorInvocationStatement) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.errln(superConstructorInvocationStatement);
	}

	public void visit(org.lgna.project.ast.ThisConstructorInvocationStatement thisConstructorInvocationStatement) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.errln(thisConstructorInvocationStatement);
	}

	public void visit(org.lgna.project.ast.WhileLoop whileLoop) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.errln(whileLoop);
	}
	
	public void visit(org.lgna.project.ast.MethodInvocation methodInvocation) {
		
	}

	//	public void visit(Statement statement) {
	//		edu.cmu.cs.dennisc.java.util.logging.Logger.errln(statement);
	//	}

	//	public void visit(org.lgna.project.ast.AbstractNode abstractNode) {
	//		edu.cmu.cs.dennisc.java.util.logging.Logger.errln("  UH OH!: ", abstractNode );
	//	}

	public void visit(Crawlable crawlable) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.errln("  NO NO!: ", crawlable );
	}
}
