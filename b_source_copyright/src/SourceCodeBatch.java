public class SourceCodeBatch extends Batch {
	private static final String[] OLD_COPYRIGHTS = { 
        edu.cmu.cs.dennisc.io.TextFileUtilities.read( SourceCodeBatch.class.getResourceAsStream( "COPYRIGHT_A.txt" ) ),
        edu.cmu.cs.dennisc.io.TextFileUtilities.read( SourceCodeBatch.class.getResourceAsStream( "COPYRIGHT_B.txt" ) ),
        edu.cmu.cs.dennisc.io.TextFileUtilities.read( SourceCodeBatch.class.getResourceAsStream( "COPYRIGHT_C.txt" ) ),
        edu.cmu.cs.dennisc.io.TextFileUtilities.read( SourceCodeBatch.class.getResourceAsStream( "COPYRIGHT_C_DOUBLE_STAR.txt" ) ),
        edu.cmu.cs.dennisc.io.TextFileUtilities.read( SourceCodeBatch.class.getResourceAsStream( "COPYRIGHT_C_DOUBLE_STAR_MISSING_LINE.txt" ) )
	};
	private static final String NEW_COPYRIGHT = edu.cmu.cs.dennisc.io.TextFileUtilities.read( SourceCodeBatch.class.getResourceAsStream( "COPYRIGHT_D.txt" ) );
	private static final String COPYRIGHT_ALL_RIGHTS_RESERVED = edu.cmu.cs.dennisc.io.TextFileUtilities.read( SourceCodeBatch.class.getResourceAsStream( "COPYRIGHT_ALL_RIGHTS_RESERVED.txt" ) );
	@Override
	protected void handle( java.io.File inFile, java.io.File outFile ) {
		String s = edu.cmu.cs.dennisc.io.TextFileUtilities.read( inFile );
		if( s.startsWith( COPYRIGHT_ALL_RIGHTS_RESERVED ) ) {
			throw new AssertionError( outFile );
		} else {
			if( s.startsWith( NEW_COPYRIGHT ) ) {
				//pass
			} else {
				for( String oldCopyright : OLD_COPYRIGHTS ) {
					if( s.startsWith( oldCopyright ) ) {
						edu.cmu.cs.dennisc.print.PrintUtilities.println( inFile, outFile );
						s = s.substring( oldCopyright.length() );
						break;
					} else {
						//pass
					}
				}
				if( inFile.getName().equals( "License.java" ) ) {
					//pass
				} else {
					if( s.toLowerCase().contains( "copyright" ) ) {
						System.err.println( s );
						throw new AssertionError( outFile );
					}
				}
				s = NEW_COPYRIGHT + s;
				edu.cmu.cs.dennisc.io.TextFileUtilities.write( outFile, s );
			}
		}
	}

	private static final String USER_HOME = System.getProperty( "user.home" ); 
	@Override
	protected String getInRoot( String args[] ) {
		return USER_HOME + "/My Documents/My Workspaces/Alice3";
	}
	@Override
	protected String getOutRoot( String args[] ) {
		return USER_HOME + "/My Documents/Alice3_SourceCode_PostBatch";
	}
	@Override
	protected String getSub( String args[] ) {
		return "/rt_foundation/src";
	}
	@Override
	protected String getInExtension( String args[] ) {
		return "java";
	}
	@Override
	protected String getOutExtension( String args[] ) {
		return "java";
	}
	@Override
	protected boolean isSkipExistingOutFilesDesirable( String args[] ) {
		return true;
	}
	
	public static void main( String[] args ) {
		SourceCodeBatch sourceCodeBatch = new SourceCodeBatch();
		sourceCodeBatch.process( args );
	}
}
