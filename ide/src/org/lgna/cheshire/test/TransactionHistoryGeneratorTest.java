package org.lgna.cheshire.test;

/**
 * @author Kyle J. Harms
 */
@Deprecated
public class TransactionHistoryGeneratorTest {

	private static final String PROJECT_FILENAME = "project.lgp";
	private static final String REUSE_FILENAME = "reuse.xml";

	private String testName;
	private java.io.File testPath;
	private java.io.File projectFile;
	private java.io.File reuseFile;

	private org.lgna.project.ast.AbstractNode reuseMethod;
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

		// We need to "convert" lgp to a3p... this hack will suffice
		org.lgna.project.Version VERSION_INDEPENDENT = null;
		org.lgna.project.migration.MigrationManager.addVersionIndependentMigration( new org.lgna.project.migration.TextMigration( 
				VERSION_INDEPENDENT, 
				VERSION_INDEPENDENT, 
				"edu.wustl.cse.lookingglass.ast.ThisInstanceExpression", 
				"org.lgna.project.ast.ThisExpression" ) );

		try {
			this.reuseMethod = loadReuseLgp( this.reuseFile );
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}

	private org.lgna.project.ast.AbstractNode loadReuseLgp( java.io.File file ) throws java.io.IOException {
		org.lgna.project.Version BAD_BAD_BAD_madeUpVersion = new org.lgna.project.Version( "3.1" );
		java.io.FileInputStream fis = new java.io.FileInputStream( file );
		org.w3c.dom.Document xmlDocument = org.lgna.project.io.IoUtilities.readXML( fis, BAD_BAD_BAD_madeUpVersion  );
		try {
			return org.lgna.project.ast.AbstractNode.decode( xmlDocument, BAD_BAD_BAD_madeUpVersion.toString() );
		} catch (org.lgna.project.VersionNotSupportedException e ) {
			e.printStackTrace();
			return null;
		}
	}

	public void generate( org.lgna.project.Project project ) {
		org.lgna.project.ast.NamedUserType programType = project.getProgramType();
		org.lgna.project.ast.NamedUserType sceneType = (org.lgna.project.ast.NamedUserType)programType.fields.get( 0 ).getValueType();
		assert sceneType.isAssignableTo( org.lgna.story.Scene.class ) : sceneType;
		org.lgna.project.ast.UserMethod owner = sceneType.getDeclaredMethod( "myFirstMethod" );

		org.lgna.project.ast.UserMethod methodToGenerate = (org.lgna.project.ast.UserMethod)this.reuseMethod;
		org.lgna.project.ast.MethodInvocation methodInvocation = new org.lgna.project.ast.MethodInvocation( new org.lgna.project.ast.ThisExpression(), methodToGenerate );
		org.lgna.cheshire.ast.TransactionHistoryGenerator transactionHistoryGenerator = new org.lgna.cheshire.ast.TransactionHistoryGenerator();
		this.reuseTransactionHistory = transactionHistoryGenerator.generate( owner, methodInvocation );
	}

	public org.lgna.croquet.history.TransactionHistory getReuseTransactionHistory() {
		return this.reuseTransactionHistory;
	}

	public java.io.File getProjectFile() {
		return this.projectFile;
	}

	public org.lgna.project.Project getProject() {
		try {
			return org.lgna.project.io.IoUtilities.readProject( this.projectFile );
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return null;
	}

	public org.lgna.project.ast.AbstractNode getReuseAst() {
		return this.reuseMethod;
	}

	public void showTransactionHistory() {
		// Show the transaction history panel.

		org.alice.ide.croquet.models.ui.debug.components.TransactionHistoryPanel transactionHistoryPanel = new org.alice.ide.croquet.models.ui.debug.components.TransactionHistoryPanel();
		transactionHistoryPanel.setTransactionHistory( this.reuseTransactionHistory );

		javax.swing.JFrame frame = new javax.swing.JFrame();
		frame.setTitle( "Reuse Transaction History" );
		frame.getContentPane().add( transactionHistoryPanel.getAwtComponent() );
		frame.setSize( 300, 800 );
		frame.setDefaultCloseOperation( javax.swing.WindowConstants.EXIT_ON_CLOSE );
		frame.setVisible(true);

		// Also print out the ast
		//this.reuseMethod.crawl(new org.lgna.cheshire.test.PrintAstVisitor( this.reuseMethod ), false);
	}

	public static TransactionHistoryGeneratorTest getSpinCrazyGenerator() {
		return new TransactionHistoryGeneratorTest( "Spin Crazy" );
	}

	public static TransactionHistoryGeneratorTest getBattleCrazyGenerator() {
		return new TransactionHistoryGeneratorTest( "Battle Crazy" );
	}

	public static void main(String[] args) throws Exception {
		TransactionHistoryGeneratorTest test;
		org.alice.stageide.StageIDE ide = new org.alice.stageide.StageIDE();
		ide.initialize( args );

		// Spin Crazy
		test = getSpinCrazyGenerator();
		test.generate( test.getProject() );
		test.showTransactionHistory();

		// Battle Crazy
		test = getBattleCrazyGenerator();
		test.generate( test.getProject() );
		test.showTransactionHistory();
	}
}
