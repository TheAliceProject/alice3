package org.lgna.cheshire.ast;

/**
 * @author Kyle J. Harms
 */
public class TransactionHistoryGenerator {
	private final org.lgna.project.ast.AbstractNode ast;
	private final org.lgna.croquet.history.TransactionHistory transactionHistory;
	private final TransactionGeneratorVisitor transactionGeneratorVisitor;

	public TransactionHistoryGenerator( org.lgna.project.ast.AbstractNode ast, org.lgna.croquet.history.TransactionHistory transactionHistory ) {
		this.ast = ast;
		this.transactionHistory = transactionHistory;
		this.transactionGeneratorVisitor = new TransactionGeneratorVisitor( this.transactionHistory );
	}

	public TransactionHistoryGenerator( org.lgna.project.ast.AbstractNode ast ) {
		this( ast, new org.lgna.croquet.history.TransactionHistory() );
	}

	public org.lgna.croquet.history.TransactionHistory generate() {
		this.ast.crawl( transactionGeneratorVisitor, true );

		// TODO: Create a run operation, at the end of the tutorial
		// <kjh/> this should probably be done live, during the tutorial... not poorly inserted here.
		//transactionHistory.addTransaction( createOpenAndClosePlainDialogOperationTransaction( transactionHistory, org.alice.stageide.croquet.models.run.RunOperation.getInstance() ) );
		return this.transactionHistory;
	}
}
