package org.lgna.cheshire.test;

/**
 * @author Kyle J. Harms
 */
@Deprecated
public class TransactionHistoryGeneratorTest {

	private static final String PROJECT_FILENAME = "project.";
	private static final String LGP_REUSE_FILENAME = "reuse.xml";
	private static final String A3P_REUSE_FILENAME = "complete.a3p";

	private String testName;
	private String reuseType;
	private String fieldName;
	private java.io.File testPath;
	private java.io.File projectFile;
	private java.io.File reuseFile;

	private org.lgna.project.ast.AbstractNode reuseMethod;
	private org.lgna.croquet.history.TransactionHistory reuseTransactionHistory;

	public TransactionHistoryGeneratorTest( String testName, String reuseType ) {
		this( testName, reuseType, null, null );
	}

	public TransactionHistoryGeneratorTest( String testName, String reuseType, String fieldName, String methodName ) {
		this.testName = testName;
		this.reuseType = reuseType;
		this.fieldName = fieldName;

		try {
			this.testPath = new java.io.File(this.getClass().getResource( this.testName ).toURI());
		} catch (java.net.URISyntaxException e) {
			e.printStackTrace();
		}

		this.projectFile = new java.io.File( this.testPath, PROJECT_FILENAME + this.reuseType );

		// We need to "convert" lgp to a3p... this hack will suffice
		org.lgna.project.Version VERSION_INDEPENDENT = null;
		org.lgna.project.migration.MigrationManager.addVersionIndependentMigration( new org.lgna.project.migration.TextMigration( 
				VERSION_INDEPENDENT, 
				VERSION_INDEPENDENT, 
				"edu.wustl.cse.lookingglass.ast.ThisInstanceExpression", 
				"org.lgna.project.ast.ThisExpression" ) );

		try {
			if ( this.reuseType.equals("lgp") ) {
				this.reuseFile = new java.io.File( this.testPath, LGP_REUSE_FILENAME );
				this.reuseMethod = loadReuseLgp( this.reuseFile );
			} else if ( this.reuseType.equals("a3p") ) {
				this.reuseFile = new java.io.File( this.testPath, A3P_REUSE_FILENAME );
				this.reuseMethod = loadReuseA3p( this.reuseFile, fieldName, methodName );
			} else {
				throw new RuntimeException( "um. i dunno" );
			}
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

	private org.lgna.project.ast.AbstractNode loadReuseA3p( java.io.File file, String fieldName, String methodName ) throws java.io.IOException {
		org.lgna.project.Project project;
		org.lgna.project.ast.AbstractMethod method = null;
		try {
			project = org.lgna.project.io.IoUtilities.readProject( file );

			org.lgna.project.ast.NamedUserType sceneType = (org.lgna.project.ast.NamedUserType)project.getProgramType().fields.get( 0 ).getValueType();
			org.lgna.project.ast.AbstractField alienField = sceneType.findField( fieldName );

			method = alienField.getValueType().findMethod( methodName );
		} catch (org.lgna.project.VersionNotSupportedException e) {
			e.printStackTrace();
		}
		return method;
	}


	private static final boolean TEMPORARY_HACK_isStoringDesired = false;
	public static final java.io.File TEMPORARY_HACK_lastGeneratedTransactionHistoryFile = new java.io.File( edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory(), "lastGeneratedTransactionHistory.bin" );
	public static final java.io.File TEMPORARY_HACK_lastGeneratedProjectFile = new java.io.File( edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory(), "lastGeneratedProject.a3p" );
	
	public void generate( org.lgna.project.Project project ) {
		org.lgna.project.ast.NamedUserType programType = project.getProgramType();
		org.lgna.project.ast.NamedUserType sceneType = (org.lgna.project.ast.NamedUserType)programType.fields.get( 0 ).getValueType();
		assert sceneType.isAssignableTo( org.lgna.story.Scene.class ) : sceneType;

		org.lgna.project.ast.UserType<?> type;
		org.lgna.project.ast.UserField field;
		if ( this.fieldName != null ) {
			field = sceneType.getDeclaredField( this.fieldName );
			type = (org.lgna.project.ast.UserType<?>)field.getValueType();
		} else {
			type = sceneType;
			field = null;
		}

		org.lgna.project.ast.UserMethod myFirstMethod = sceneType.getDeclaredMethod( "myFirstMethod" );

		org.lgna.project.ast.UserMethod methodToGenerate = (org.lgna.project.ast.UserMethod)this.reuseMethod;
		org.lgna.project.ast.MethodInvocation methodInvocation = new org.lgna.project.ast.MethodInvocation( new org.lgna.project.ast.ThisExpression(), methodToGenerate );
		org.lgna.cheshire.ast.TransactionHistoryGenerator transactionHistoryGenerator = new org.lgna.cheshire.ast.TransactionHistoryGenerator();
		this.reuseTransactionHistory = transactionHistoryGenerator.generate( type, methodInvocation, myFirstMethod, field );

		if( TEMPORARY_HACK_isStoringDesired ) {
			try {
				org.lgna.project.io.IoUtilities.writeProject( TEMPORARY_HACK_lastGeneratedProjectFile, project );
				edu.cmu.cs.dennisc.codec.CodecUtilities.encodeBinary( this.reuseTransactionHistory, TEMPORARY_HACK_lastGeneratedTransactionHistoryFile );
			} catch( Throwable t ) {
				t.printStackTrace();
			}
		}
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
		org.alice.ide.croquet.models.ui.debug.components.TransactionHistoryPanel transactionHistoryPanel = new org.alice.ide.croquet.models.ui.debug.components.TransactionHistoryPanel();
		transactionHistoryPanel.setTransactionHistory( this.reuseTransactionHistory );

		javax.swing.JFrame frame = new javax.swing.JFrame();
		frame.setTitle( "Reuse Transaction History" );
		frame.getContentPane().add( transactionHistoryPanel.getAwtComponent() );
		frame.setSize( 300, 800 );
		frame.setDefaultCloseOperation( javax.swing.WindowConstants.EXIT_ON_CLOSE );
		frame.setVisible(true);
	}

	public static TransactionHistoryGeneratorTest getSpinCrazyGenerator() {
		return new TransactionHistoryGeneratorTest( "Spin Crazy", "lgp" );
	}

	public static TransactionHistoryGeneratorTest getBattleCrazyGenerator() {
		return new TransactionHistoryGeneratorTest( "Battle Crazy", "lgp" );
	}

	public static TransactionHistoryGeneratorTest getColorCrazyGenerator() {
		return new TransactionHistoryGeneratorTest( "Color Crazy", "a3p", "alien", "color_crazy" );
	}

	public static void main(String[] args) throws Exception {
		TransactionHistoryGeneratorTest test;
		org.alice.stageide.StageIDE ide = new org.alice.stageide.StageIDE();
		ide.initialize( args );

		//		// Spin Crazy
		//		test = getSpinCrazyGenerator();
		//		test.generate( test.getProject() );
		//		test.showTransactionHistory();
		//
		//		// Battle Crazy
		//		test = getBattleCrazyGenerator();
		//		test.generate( test.getProject() );
		//		test.showTransactionHistory();

		// Color Crazy
		test = getColorCrazyGenerator();
		test.generate( test.getProject() );
		test.showTransactionHistory();
	}
}
