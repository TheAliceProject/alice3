package autotutorial;

public class AutoTutorialIde extends org.alice.stageide.StageIDE {
	private static boolean IS_ENCODING;
	private static final String UI_HISTORY_PATH = "/autoTutorial1.bin";
	private static final String POST_PROJECT_PATH = "/post.a3p";
	
	private boolean isPostProjectLive = false;
	private edu.cmu.cs.dennisc.alice.Project postProject;
	private edu.cmu.cs.dennisc.croquet.RootContext postContext;
	@Override
	public void loadProjectFrom( java.net.URI uri ) {
		super.loadProjectFrom( uri );
		if( IS_ENCODING ) {
			edu.cmu.cs.dennisc.croquet.ModelContext< ? > rootContext = edu.cmu.cs.dennisc.croquet.ContextManager.getRootContext();
			rootContext.EPIC_HACK_clear();
		} else {
			
			edu.cmu.cs.dennisc.alice.project.ProjectUtilities.addAllToReplacementMap( this.getProject() );
			
			this.postProject = edu.cmu.cs.dennisc.alice.project.ProjectUtilities.readProject( POST_PROJECT_PATH );
			
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "    this.project:", this.getProject().getProgramType().getUUID(), this.getProject().hashCode() );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "this.postProject:", this.postProject.getProgramType().getUUID(), this.postProject.hashCode() );
			edu.cmu.cs.dennisc.codec.CodecUtilities.isDebugDesired = true;
			this.isPostProjectLive = true;
			postContext = edu.cmu.cs.dennisc.codec.CodecUtilities.decodeBinary( UI_HISTORY_PATH, edu.cmu.cs.dennisc.croquet.RootContext.class );
			this.isPostProjectLive = false;
			edu.cmu.cs.dennisc.codec.CodecUtilities.isDebugDesired = false;
		}
	}
	
	@Override
	public edu.cmu.cs.dennisc.alice.Project getProject() {
		if( this.isPostProjectLive ) {
			return this.postProject;
		} else {
			return super.getProject();
		}
	}
	@Override
	protected void handleQuit( java.util.EventObject e ) {
		if( IS_ENCODING ) {
			edu.cmu.cs.dennisc.croquet.ModelContext< ? > rootContext = edu.cmu.cs.dennisc.croquet.ContextManager.getRootContext();
			edu.cmu.cs.dennisc.codec.CodecUtilities.isDebugDesired = true;
			edu.cmu.cs.dennisc.codec.CodecUtilities.encodeBinary( rootContext, UI_HISTORY_PATH );
			edu.cmu.cs.dennisc.codec.CodecUtilities.isDebugDesired = false;

			try {
				edu.cmu.cs.dennisc.alice.project.ProjectUtilities.writeProject( POST_PROJECT_PATH, this.getProject() );
			} catch( java.io.IOException ioe ) {
				throw new RuntimeException( ioe );
			}
		}
		System.exit( 0 );
		//super.handleQuit( e );
	}
	private void createAndShowTutorial() {
		final org.alice.ide.tutorial.IdeTutorial tutorial = new org.alice.ide.tutorial.IdeTutorial( this, 0 );
		
		tutorial.addMessageStep( "start", "start of tutorial" );
		for( edu.cmu.cs.dennisc.croquet.HistoryNode node : this.postContext.getChildren() ) {
			if( node instanceof edu.cmu.cs.dennisc.croquet.InputDialogOperationContext< ? > ) {
				edu.cmu.cs.dennisc.croquet.InputDialogOperationContext< ? > inputDialogOperationContext = (edu.cmu.cs.dennisc.croquet.InputDialogOperationContext< ? >)node;
				edu.cmu.cs.dennisc.croquet.InputDialogOperation< ? > inputDialogOperation = inputDialogOperationContext.getModel();
				assert inputDialogOperation != null;
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "inputDialogOperation:", inputDialogOperation );
				tutorial.addInputDialogOpenAndCommitStep( "title", "openText", "commitText", inputDialogOperation, tutorial.createToDoCompletorValidator() );
			} else if( node instanceof edu.cmu.cs.dennisc.croquet.BooleanStateContext ) {
				edu.cmu.cs.dennisc.croquet.BooleanStateContext booleanStateContext = (edu.cmu.cs.dennisc.croquet.BooleanStateContext)node;
				edu.cmu.cs.dennisc.croquet.BooleanStateEdit edit = booleanStateContext.getEdit();
				if( edit != null ) {
					edu.cmu.cs.dennisc.croquet.BooleanState booleanState = booleanStateContext.getModel();
					//booleanState = org.alice.ide.croquet.models.ui.IsSceneEditorExpandedState.getInstance();
					if( booleanState instanceof org.alice.ide.croquet.models.ui.debug.IsInteractionTreeShowingState ) {
						//pass
					} else {
						tutorial.addBooleanStateStep( booleanState.getClass().getSimpleName(), booleanState.getClass().getName(), booleanState, edit.getNextValue() );
					}
				}
			}
		}
		tutorial.addMessageStep( "end", "end of tutorial" );
		tutorial.setVisible( true );
		this.getFrame().setVisible( true );
	}
	
	public static void main( String[] args ) throws Exception {
		IS_ENCODING = Boolean.parseBoolean( args[ 5 ] );
		final AutoTutorialIde ide = org.alice.ide.LaunchUtilities.launchAndWait( AutoTutorialIde.class, null, args, false );
		if( IS_ENCODING ) {
			ide.getFrame().setVisible( true );
		} else {
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					ide.createAndShowTutorial();
				} 
			} );
		}
	}
}
