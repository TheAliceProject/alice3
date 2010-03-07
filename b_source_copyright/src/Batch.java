public abstract class Batch {
	protected abstract void handle( java.io.File inFile, java.io.File outFile );
	protected String getInRoot( String args[] ) {
		return args[ 0 ];
	}
	protected String getOutRoot( String args[] ) {
		return args[ 1 ];
	}
	protected String getSub( String args[] ) {
		return args[ 2 ];
	}
	protected String getInExtension( String args[] ) {
		return args[ 3 ];
	}
	protected String getOutExtension( String args[] ) {
		return args[ 4 ];
	}
	protected boolean isSkipExistingOutFilesDesirable( String args[] ) {
		return args[ 5 ].equalsIgnoreCase( "true" );
	}
	protected void process( String args[] ) {
		String inRoot = getInRoot( args ) + getSub( args );
		String outRoot = getOutRoot( args ) + getSub( args );
		String inExt = getInExtension( args );
		String outExt = getOutExtension( args );
		boolean isSkipExistingOutFilesDesirable = isSkipExistingOutFilesDesirable( args );
		
		System.out.print( "FileUtilities.listDescendants... " );
		java.io.File[] inFiles = edu.cmu.cs.dennisc.io.FileUtilities.listDescendants( inRoot, inExt );
		System.out.println( "Done.  ( " + inFiles.length + " files )" );

		java.util.Set< java.io.File > unhandledFiles = new java.util.HashSet< java.io.File >();
		//Runtime.getRuntime().gc();
		//long freeMemory0 = Runtime.getRuntime().freeMemory();
		for( java.io.File inFile : inFiles ) {

			String inPath = inFile.getAbsolutePath();
			String outPath = outRoot + inPath.substring( inRoot.length(), inPath.length() - inExt.length() ) + outExt;

			java.io.File outFile = new java.io.File( outPath );

			if( isSkipExistingOutFilesDesirable && outFile.exists() ) {
				System.out.println( "SKIPPING: " + outFile + " exists." );
			} else {
				if( inFile.exists() ) {
					//todo:
					if( inFile.length() < 100L * 1024L * 1024L ) {
						try {
							handle( inFile, outFile );
						} catch( RuntimeException re ) {
							re.printStackTrace();
							unhandledFiles.add( inFile );
						}
					} else {
						unhandledFiles.add( inFile );
					}
				}
			}
			//Runtime.getRuntime().gc();
			//long freeMemoryI = Runtime.getRuntime().freeMemory();
			//System.err.println( freeMemoryI + " " + (freeMemoryI-freeMemory0) );
		}
		System.out.flush();
		for( java.io.File unhandledFile : unhandledFiles ) {
			System.err.println( "UNHANDLED FILE: " + unhandledFile );
		}
	}
}
