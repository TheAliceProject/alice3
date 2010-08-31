package org.alice.stageide;

public class AutoTutorialIde extends org.alice.stageide.StageIDE {
	private static final String PATH = "/autoTutorial1.bin";

	@Override
	protected void handleQuit( java.util.EventObject e ) {
		
		
		edu.cmu.cs.dennisc.croquet.ModelContext< ? > rootContext = edu.cmu.cs.dennisc.croquet.ContextManager.getRootContext();
		edu.cmu.cs.dennisc.codec.CodecUtilities.encodeBinary( rootContext, PATH );
		super.handleQuit( e );
	}
	
	public static void main( String[] args ) {
		final boolean IS_ENCODING = false;
		if( IS_ENCODING ) {
			org.alice.ide.LaunchUtilities.launch( AutoTutorialIde.class, null, args );
		} else {
			edu.cmu.cs.dennisc.croquet.ModelContext<?> modelContext = edu.cmu.cs.dennisc.codec.CodecUtilities.decodeBinary( PATH, edu.cmu.cs.dennisc.croquet.ModelContext.class );
		}
	}
}
