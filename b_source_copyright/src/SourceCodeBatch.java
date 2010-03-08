import edu.cmu.cs.dennisc.batch.Batch;

public class SourceCodeBatch extends Batch {
	private static java.util.Set< String > specialCases = edu.cmu.cs.dennisc.java.util.Collections.newHashSet();

	static {
		specialCases.add( "ImagesToMOVEncoder.java" );
	}

	private String allRightsReservedCopyright;
	private String[] oldCopyrights;
	private String newCopyright;

	public SourceCodeBatch( String allRightsReservedCopyright, String[] oldCopyrights, String newCopyright ) {
		this.allRightsReservedCopyright = allRightsReservedCopyright;
		this.oldCopyrights = oldCopyrights;
		this.newCopyright = newCopyright;
	}

	@Override
	protected void handle( java.io.File inFile, java.io.File outFile ) {
		String s = edu.cmu.cs.dennisc.io.TextFileUtilities.read( inFile );
		if( s.startsWith( allRightsReservedCopyright ) ) {
			throw new AssertionError( outFile );
		} else {
			if( s.startsWith( newCopyright ) ) {
				//pass
			} else {
				for( String oldCopyright : oldCopyrights ) {
					if( s.startsWith( oldCopyright ) ) {
						edu.cmu.cs.dennisc.print.PrintUtilities.println( inFile, outFile );
						s = s.substring( oldCopyright.length() );
						break;
					} else {
						//pass
					}
				}
				String name = inFile.getName();
				if( name.equals( "License.java" ) ) {
					//pass
				} else {
					if( specialCases.contains( name ) ) {
						//pass
					} else {
						if( s.toLowerCase().contains( "copyright" ) ) {
							System.err.println( s );
							throw new AssertionError( outFile );
						}
					}
				}
				s = newCopyright + s;
				edu.cmu.cs.dennisc.io.TextFileUtilities.write( outFile, s );
			}
		}
	}

	@Override
	protected boolean isSkipExistingOutFilesDesirable() {
		return true;
	}

	public static void main( String[] args ) {
		final String ALL_RIGHTS_RESERVED_COPYRIGHT = edu.cmu.cs.dennisc.io.TextFileUtilities.read( SourceCodeBatch.class.getResourceAsStream( "COPYRIGHT_ALL_RIGHTS_RESERVED.txt" ) );
		final String[] OLD_COPYRIGHTS = { 
				edu.cmu.cs.dennisc.io.TextFileUtilities.read( SourceCodeBatch.class.getResourceAsStream( "COPYRIGHT_A.txt" ) ), 
				edu.cmu.cs.dennisc.io.TextFileUtilities.read( SourceCodeBatch.class.getResourceAsStream( "COPYRIGHT_B.txt" ) ), 
				edu.cmu.cs.dennisc.io.TextFileUtilities.read( SourceCodeBatch.class.getResourceAsStream( "COPYRIGHT_C.txt" ) ), 
				edu.cmu.cs.dennisc.io.TextFileUtilities.read( SourceCodeBatch.class.getResourceAsStream( "COPYRIGHT_C_DOUBLE_STAR.txt" ) ),
				edu.cmu.cs.dennisc.io.TextFileUtilities.read( SourceCodeBatch.class.getResourceAsStream( "COPYRIGHT_C_DOUBLE_STAR_MISSING_LINE.txt" ) ) };
		final String NEW_COPYRIGHT = edu.cmu.cs.dennisc.io.TextFileUtilities.read( SourceCodeBatch.class.getResourceAsStream( "COPYRIGHT_D.txt" ) );
		SourceCodeBatch sourceCodeBatch = new SourceCodeBatch( ALL_RIGHTS_RESERVED_COPYRIGHT, OLD_COPYRIGHTS, NEW_COPYRIGHT );
		final String USER_HOME = System.getProperty( "user.home" );
		final String IN_ROOT = USER_HOME + "/My Documents/My Workspaces/Alice3";
		final String OUT_ROOT = USER_HOME + "/My Documents/Alice3_SourceCode_PostBatch";
		final String EXTENSION = "java";
		String[] subs = { 
				"rt_foundation", 
				"rt_issue", 
				"x_ide", 
				"x_ide_stage", 
				"x_youtube",
				
				"rt_moveandturn",
				"rt_moveandturn_generated",
				"rt_storytelling",
				"rt_storytelling_deprecated",
				"rt_storytelling_generated_depended_upon",
				"rt_storytelling_generated_post",
		};
		for( String sub : subs ) {
			sub =  "/" + sub + "/src";
			sourceCodeBatch.process( IN_ROOT+sub, OUT_ROOT+sub, EXTENSION, EXTENSION );
		}

		for( String specialCase : specialCases ) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "SPECIAL CASE:", specialCase );
		}
	}
}
