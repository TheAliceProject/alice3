package org.lgna.cheshire.test;

/**
 * @author Kyle J. Harms
 */
@Deprecated
public class TransactionHistoryGeneratorTest {

	private static final String PROJECT_FILENAME = "original.lgp";
	private static final String REMIXED_FILENAME = "remixed.lgp";
	private static final String REUSE_FILENAME = "reuse.xml";

	private String testName;
	private java.io.File testPath;
	private java.io.File projectFile;
	private java.io.File reuseFile;

	private org.lgna.project.Project project;
	private org.lgna.project.ast.AbstractNode reuseMethod;
	private org.lgna.cheshire.ast.TransactionHistoryGenerator transactionHistoryGenerator;
	private org.lgna.croquet.history.TransactionHistory reuseTransactionHistory;

	public TransactionHistoryGeneratorTest( String testName ) {
		this.testName = testName;
		try {
			this.testPath = new java.io.File(this.getClass().getResource( this.testName ).toURI());
		} catch (java.net.URISyntaxException e) {
			e.printStackTrace();
		}
		this.projectFile = new java.io.File( this.testPath, PROJECT_FILENAME );
		this.reuseFile = new java.io.File( this.testPath, REUSE_FILENAME );

		org.lgna.project.Version VERSION_INDEPENDENT = null;
		// We need to "convert" lgp to a3p... this hack will suffice
		org.lgna.project.migration.MigrationManager.addVersionIndependentMigration( new org.lgna.project.migration.TextMigration( 
				VERSION_INDEPENDENT, 
				VERSION_INDEPENDENT, 
				"edu.wustl.cse.lookingglass.ast.ThisInstanceExpression", 
				"org.lgna.project.ast.ThisExpression" ) );

		try {
			this.project = org.lgna.project.io.IoUtilities.readProject( this.projectFile );
		} catch (java.io.IOException e) {
			e.printStackTrace();
		} catch (org.lgna.project.VersionNotSupportedException e) {
			e.printStackTrace();
		}

		try {
			this.reuseMethod = loadReuseLgp( this.reuseFile );
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}

		this.transactionHistoryGenerator = new org.lgna.cheshire.ast.TransactionHistoryGenerator( this.reuseMethod );
		this.reuseTransactionHistory = this.transactionHistoryGenerator.generate();
	}

	private org.lgna.project.ast.AbstractNode loadReuseLgp( java.io.File file ) throws java.io.IOException {
		java.io.FileInputStream fis = new java.io.FileInputStream( file );
		org.w3c.dom.Document xmlDocument = org.lgna.project.io.IoUtilities.readXML( fis, /* BAD BAD BAD */ new org.lgna.project.Version( "3.1" ) );
		try {
			return org.lgna.project.ast.AbstractNode.decode( xmlDocument, /* BAD BAD BAD */ "3.1" );
		} catch (org.lgna.project.VersionNotSupportedException e ) {
			e.printStackTrace();
			return null;
		}
	}

	public void showTransactionHistory() {
		// Show the transaction history panel.
		
		org.alice.ide.croquet.models.ui.debug.components.TransactionHistoryPanel transactionHistoryPanel = new org.alice.ide.croquet.models.ui.debug.components.TransactionHistoryPanel( this.reuseTransactionHistory );

		javax.swing.JFrame frame = new javax.swing.JFrame();
		frame.setTitle( "Reuse Transaction History" );
		frame.getContentPane().add( transactionHistoryPanel.getAwtComponent() );
		frame.setSize( 300, 800 );
		frame.setDefaultCloseOperation( javax.swing.WindowConstants.EXIT_ON_CLOSE );
		frame.setVisible(true);
		
		// Also print out the ast
		org.lgna.cheshire.test.PrintAstVisitor printAstVisitor = new org.lgna.cheshire.test.PrintAstVisitor( this.reuseMethod );
		this.reuseMethod.crawl(printAstVisitor, false);
	}

	public static void main(String[] args) {
		org.alice.stageide.StageIDE ide = new org.alice.stageide.StageIDE();
		ide.initialize( args );
		
		TransactionHistoryGeneratorTest test = new TransactionHistoryGeneratorTest( "Spin Crazy" );
		test.showTransactionHistory();
	}
}
