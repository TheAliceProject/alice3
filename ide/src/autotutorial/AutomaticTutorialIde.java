package autotutorial;

public class AutomaticTutorialIde extends org.alice.stageide.StageIDE {
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
		//super.handleQuit( e );
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
	}
	private void createAndShowTutorial() {
		//final org.alice.ide.tutorial.IdeTutorial tutorial = new org.alice.ide.tutorial.IdeTutorial( this, 0 );
		
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

		
		final edu.cmu.cs.dennisc.tutorial.AutomaticTutorial tutorial = new edu.cmu.cs.dennisc.tutorial.AutomaticTutorial( 
//				edu.cmu.cs.dennisc.tutorial.MenuPolicy.ABOVE_STENCIL_WITH_FEEDBACK,
//				edu.cmu.cs.dennisc.tutorial.MenuPolicy.ABOVE_STENCIL_WITHOUT_FEEDBACK,
				edu.cmu.cs.dennisc.tutorial.MenuPolicy.BELOW_STENCIL,
				new edu.cmu.cs.dennisc.croquet.Group[] { edu.cmu.cs.dennisc.alice.Project.GROUP, org.alice.ide.IDE.UI_STATE_GROUP },
				new edu.cmu.cs.dennisc.croquet.Group[] { org.alice.ide.IDE.RUN_GROUP }
		);
		tutorial.addSteps( this.postContext );
		
		AstLiveRetargeter astLiveRetargeter = new AstLiveRetargeter();
		tutorial.setRetargeter( astLiveRetargeter );

		tutorial.setVisible( true );
		this.getFrame().setVisible( true );
		
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				tutorial.setSelectedIndex( 0 );
			}
		} );

	}
	
	public static void main( String[] args ) throws Exception {
		IS_ENCODING = Boolean.parseBoolean( args[ 5 ] );
		final AutomaticTutorialIde ide = org.alice.ide.LaunchUtilities.launchAndWait( AutomaticTutorialIde.class, null, args, false );
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
