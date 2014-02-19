package test;

import org.lgna.project.code.CodeConverter;
import org.lgna.project.code.PathCodePair;

public class TestCodeConverter {
	public static void main( String[] args ) throws Exception {
		org.lgna.project.Project project = org.lgna.project.io.IoUtilities.readProject( args[ 0 ] );
		CodeConverter codeConverter = new CodeConverter( new org.lgna.project.ast.JavaCodeGenerator.Builder().isLambdaSupported( true ) );
		Iterable<PathCodePair> pathCodePairs = codeConverter.convert( project );
		for( PathCodePair pathCodePair : pathCodePairs ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( pathCodePair.getPath() );
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( pathCodePair.getCode() );
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( org.lgna.project.code.CodeFormatter.format( pathCodePair.getCode() ) );
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln();
		}
	}
}
