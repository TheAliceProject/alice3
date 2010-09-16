package autotutorial;

class WizardOfHastings {
	public static void castPart( edu.cmu.cs.dennisc.croquet.Retargeter retargeter, edu.cmu.cs.dennisc.alice.Project originalProject, String originalFieldName, edu.cmu.cs.dennisc.alice.Project replacementProject, String replacementFieldName ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractType orginalSceneType = originalProject.getProgramType().getDeclaredFields().get( 0 ).getValueType();		
		edu.cmu.cs.dennisc.alice.ast.AbstractField originalField = orginalSceneType.getDeclaredField( originalFieldName );
		edu.cmu.cs.dennisc.alice.ast.AbstractType replacementSceneType = replacementProject.getProgramType().getDeclaredFields().get( 0 ).getValueType();
		edu.cmu.cs.dennisc.alice.ast.AbstractField replacementField = replacementSceneType.getDeclaredField( replacementFieldName );
		
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "original:", originalField.getName(), originalField.getValueType().getName(), originalField.getValueType().getUUID() );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "replacement:", replacementField.getName(), replacementField.getValueType().getName(), replacementField.getValueType().getUUID() );
		retargeter.addKeyValuePair( originalField, replacementField );
		retargeter.addKeyValuePair( originalField.getValueType(), replacementField.getValueType() );
	}
}

enum BareBonesUserInformation implements edu.cmu.cs.dennisc.croquet.UserInformation {
	INSTANCE;
	public java.util.Locale getLocale() {
		return java.util.Locale.getDefault();
	}
}

public class AutomaticTutorialIde extends org.alice.stageide.StageIDE {
	private static boolean IS_ENCODING;
	private static boolean IS_WIZARD_OF_OZ_HASTINGS_DESIRED;
	private static final String UI_HISTORY_PATH = "/autoTutorial1.bin";
	private static final String POST_PROJECT_PATH = "/post.a3p";
	
	private boolean isPostProjectLive = false;
	private edu.cmu.cs.dennisc.alice.Project postProject;
	private edu.cmu.cs.dennisc.croquet.RootContext postContext;
	
	@Override
	protected StringBuffer updateTitle( StringBuffer rv ) {
		rv.append( "AnonymizedForPeerReview" );
		return rv;
	}
	
	@Override
	public void loadProjectFrom( java.net.URI uri ) {
		super.loadProjectFrom( uri );
		org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().setValue( IS_ENCODING );
		//org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().setValue( true );
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
					if( key instanceof edu.cmu.cs.dennisc.alice.ast.Node && value instanceof edu.cmu.cs.dennisc.alice.ast.Node ) {
						mapIdToReplacementNode.put( ((edu.cmu.cs.dennisc.alice.ast.Node)key).getUUID(), (edu.cmu.cs.dennisc.alice.ast.Node)value );
					} else {
						edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: IGNORING addKeyValuePair", key, value );
					}
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

			final boolean IS_INFORMATION_GROUP_INCLUDED = false;
			edu.cmu.cs.dennisc.croquet.guide.GroupFilter.SINGLETON.addGroup( edu.cmu.cs.dennisc.alice.Project.GROUP, edu.cmu.cs.dennisc.croquet.guide.GroupFilter.SuccessfulCompletionPolicy.ONLY_COMMITS );
			edu.cmu.cs.dennisc.croquet.guide.GroupFilter.SINGLETON.addGroup( edu.cmu.cs.dennisc.croquet.Application.UI_STATE_GROUP, edu.cmu.cs.dennisc.croquet.guide.GroupFilter.SuccessfulCompletionPolicy.ONLY_COMMITS );
			edu.cmu.cs.dennisc.croquet.guide.GroupFilter.SINGLETON.addGroup( org.alice.ide.IDE.RUN_GROUP, edu.cmu.cs.dennisc.croquet.guide.GroupFilter.SuccessfulCompletionPolicy.BOTH_COMMITS_AND_FINISHES );
			if( IS_INFORMATION_GROUP_INCLUDED ) {
				edu.cmu.cs.dennisc.croquet.guide.GroupFilter.SINGLETON.addGroup( edu.cmu.cs.dennisc.croquet.Application.INFORMATION_GROUP, edu.cmu.cs.dennisc.croquet.guide.GroupFilter.SuccessfulCompletionPolicy.BOTH_COMMITS_AND_FINISHES );
			}
			edu.cmu.cs.dennisc.croquet.guide.Filter[] filters = {
					//edu.cmu.cs.dennisc.croquet.tutorial.SuccessfullyCompletedFilter.SINGLETON,
					edu.cmu.cs.dennisc.croquet.guide.MenuSelectionEventFilter.SINGLETON,
					edu.cmu.cs.dennisc.croquet.guide.GroupFilter.SINGLETON,
			};
			for( edu.cmu.cs.dennisc.croquet.guide.Filter filter : filters ) {
				this.postContext = filter.filter( this.postContext );
			}
			this.isPostProjectLive = false;
			edu.cmu.cs.dennisc.codec.CodecUtilities.isDebugDesired = false;
			
			org.alice.ide.croquet.models.ui.debug.IsInteractionTreeShowingState isInteractionTreeShowingState = new org.alice.ide.croquet.models.ui.debug.IsInteractionTreeShowingState( this.postContext );
			isInteractionTreeShowingState.setValue( true );

			
			edu.cmu.cs.dennisc.alice.Project replacementProject = this.getProject();
			
			AstDecodingRetargeter astDecodingRetargeter = new AstDecodingRetargeter();
			astDecodingRetargeter.addAllToReplacementMap( replacementProject );

			if( IS_WIZARD_OF_OZ_HASTINGS_DESIRED ) {
				WizardOfHastings.castPart( astDecodingRetargeter, this.postProject, "guppy", replacementProject, "car" );
			}
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
		this.preservePreferences();
//		super.handleQuit( e );
		if( IS_ENCODING ) {
			edu.cmu.cs.dennisc.croquet.ModelContext< ? > rootContext = edu.cmu.cs.dennisc.croquet.ContextManager.getRootContext();
			
			System.err.println( "todo: remove filtering" );
			edu.cmu.cs.dennisc.croquet.guide.Filter[] filters = {
					edu.cmu.cs.dennisc.croquet.guide.MenuSelectionEventFilter.SINGLETON,
			};
			for( edu.cmu.cs.dennisc.croquet.guide.Filter filter : filters ) {
				rootContext = filter.filter( rootContext );
			}

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
			private java.util.Map< Object, Object > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
			public void addKeyValuePair( Object key, Object value ) {
				this.map.put( key, value );
				if( key instanceof edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody ) {
					System.err.println( "TODO: recursive retarget" );
					this.addKeyValuePair( ((edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody)key).body.getValue(), ((edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody)value).body.getValue() );
				}
			}
			public <N> N retarget(N original) {
				N rv = (N)map.get( original );
				if( rv != null ) {
					//pass
				} else {
					rv = original;
				}
				return rv;
			}
		};

		//edu.cmu.cs.dennisc.tutorial.ScrollingRequiredRenderer scrollingRequiredRenderer = null;
		edu.cmu.cs.dennisc.tutorial.ScrollingRequiredRenderer scrollingRequiredRenderer = edu.cmu.cs.dennisc.tutorial.DefaultScrollingRequiredRenderer.INSTANCE;
		final edu.cmu.cs.dennisc.croquet.guide.ConstructionGuide tutorial = new edu.cmu.cs.dennisc.croquet.guide.ConstructionGuide( 
				BareBonesUserInformation.INSTANCE,
				
				
//				edu.cmu.cs.dennisc.tutorial.MenuPolicy.ABOVE_STENCIL_WITH_FEEDBACK,
//				edu.cmu.cs.dennisc.tutorial.MenuPolicy.ABOVE_STENCIL_WITHOUT_FEEDBACK,
				edu.cmu.cs.dennisc.croquet.guide.MenuPolicy.BELOW_STENCIL,
				
				
				edu.cmu.cs.dennisc.croquet.guide.StepAccessPolicy.ALLOW_ACCESS_UP_TO_AND_INCLUDING_FURTHEST_COMPLETED_STEP,
				//edu.cmu.cs.dennisc.croquet.guide.StepAccessPolicy.ALLOW_ACCESS_TO_ALL_STEPS,
				
				scrollingRequiredRenderer,
				new edu.cmu.cs.dennisc.croquet.Group[] { edu.cmu.cs.dennisc.alice.Project.GROUP, org.alice.ide.IDE.UI_STATE_GROUP }
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
		if( IS_ENCODING ) {
			IS_WIZARD_OF_OZ_HASTINGS_DESIRED = false;
		} else {
			IS_WIZARD_OF_OZ_HASTINGS_DESIRED = Boolean.parseBoolean( args[ 6 ] );
		}
		org.alice.ide.memberseditor.MembersEditor.IS_FOLDER_TABBED_PANE_DESIRED = IS_ENCODING;
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
