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
			
			class AstDecodingRetargeter implements edu.cmu.cs.dennisc.croquet.Retargeter {
				private java.util.Map< java.util.UUID, edu.cmu.cs.dennisc.alice.ast.Node > mapIdToReplacementNode = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
				public void addAllToReplacementMap( edu.cmu.cs.dennisc.alice.Project project ) {
					edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice programType = project.getProgramType();
					edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.Node > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.Node >( edu.cmu.cs.dennisc.alice.ast.Node.class );
					programType.crawl( crawler, true );
					for( edu.cmu.cs.dennisc.alice.ast.Node node : crawler.getList() ) {
						mapIdToReplacementNode.put( node.getUUID(), node );
					}
				}
				public void addKeyValuePair( Object key, Object value ) {
					assert false;
				}
				public <N> N retarget(N value) {
					if( value instanceof edu.cmu.cs.dennisc.alice.ast.Node ) {
						edu.cmu.cs.dennisc.alice.ast.Node originalNode = (edu.cmu.cs.dennisc.alice.ast.Node)value;
						edu.cmu.cs.dennisc.alice.ast.Node retargetedNode = mapIdToReplacementNode.get( originalNode.getUUID() );
						if( retargetedNode != null ) {
							return (N)retargetedNode;
						} else {
							return value;
						}
					} else {
						return value;
					}
				}
			};
			
			class AstLiveRetargeter implements edu.cmu.cs.dennisc.croquet.Retargeter {
				private java.util.Map< edu.cmu.cs.dennisc.alice.ast.Node, edu.cmu.cs.dennisc.alice.ast.Node > mapOriginalNodeToReplacementNode = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
				public void addKeyValuePair( Object key, Object value ) {
					if( key instanceof edu.cmu.cs.dennisc.alice.ast.Node && value instanceof edu.cmu.cs.dennisc.alice.ast.Node ) {
						this.mapOriginalNodeToReplacementNode.put( (edu.cmu.cs.dennisc.alice.ast.Node)key, (edu.cmu.cs.dennisc.alice.ast.Node)value );
					}
				}
				public <N> N retarget(N value) {
					if( value instanceof edu.cmu.cs.dennisc.alice.ast.Node ) {
						edu.cmu.cs.dennisc.alice.ast.Node originalNode = (edu.cmu.cs.dennisc.alice.ast.Node)value;
						edu.cmu.cs.dennisc.alice.ast.Node retargetedNode = mapOriginalNodeToReplacementNode.get( originalNode );
						if( retargetedNode != null ) {
							return (N)retargetedNode;
						} else {
							return value;
						}
					} else {
						return value;
					}
				}
			};

			this.postProject = edu.cmu.cs.dennisc.alice.project.ProjectUtilities.readProject( POST_PROJECT_PATH );
			edu.cmu.cs.dennisc.codec.CodecUtilities.isDebugDesired = true;
			this.isPostProjectLive = true;
			this.postContext = edu.cmu.cs.dennisc.codec.CodecUtilities.decodeBinary( UI_HISTORY_PATH, edu.cmu.cs.dennisc.croquet.RootContext.class );
			this.isPostProjectLive = false;
			edu.cmu.cs.dennisc.codec.CodecUtilities.isDebugDesired = false;
			
			AstDecodingRetargeter astDecodingRetargeter = new AstDecodingRetargeter();
			astDecodingRetargeter.addAllToReplacementMap( this.getProject() );
			this.postContext.retarget( astDecodingRetargeter );
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
