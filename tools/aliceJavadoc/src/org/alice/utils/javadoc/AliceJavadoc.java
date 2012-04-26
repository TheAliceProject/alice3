package org.alice.utils.javadoc;

import java.io.File;

public class AliceJavadoc {

	public static void rebuildDocs(String baseDir) {
		String srcIn = baseDir + "\\ide\\src";
		String docsOut = baseDir + "C:\\aliceDocs";
		
		String[] javadocargs = {"-d", docsOut, "-sourcepath", srcIn, "-encoding", "UTF-8", "-docencoding", "UTF-8", "-subpackages", "org" };
		com.sun.tools.javadoc.Main.execute(javadocargs);
	}
	
	public static void main( String[] args ) throws Exception {
		
		File baseDir = new File(System.getProperty( "user.dir" ));
		File baseAliceDir = baseDir;
		if (baseAliceDir.exists() && baseAliceDir.getParentFile() != null) {
			baseAliceDir = baseAliceDir.getParentFile();
			if (baseAliceDir.exists() && baseAliceDir.getParent() != null) {
				baseAliceDir = baseAliceDir.getParentFile();
				if (!baseAliceDir.exists()) {
					baseAliceDir = null;
				}
			}
			else {
				baseAliceDir = null;
			}
		}
		else {
			baseAliceDir = null;
		}
		
		if (baseAliceDir == null) {
			throw new Exception("Can't find Alice dir.");
		}
		
		AliceJavadoc.rebuildDocs(baseAliceDir.getAbsolutePath());
		
	}
}
