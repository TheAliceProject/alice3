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

enum AlgUserInformation implements edu.cmu.cs.dennisc.croquet.UserInformation {
	INSTANCE;
	public java.util.Locale getLocale() {
		return java.util.Locale.getDefault();
	}
	public edu.cmu.cs.dennisc.cheshire.Message createMessageIfUnfamiliarWithProgrammingConstruct( Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > statementCls ) {
		if( statementCls == edu.cmu.cs.dennisc.alice.ast.CountLoop.class ) {
			return new edu.cmu.cs.dennisc.cheshire.Message( "New Concept: Count Loop", "<strong>New Concept: Count Loop</strong><br>Count Loops are useful for perfoming the same action multiple times." );
		} else {
			return null;
		}
	}
}

interface GuidedInteractionGenerator {
	edu.cmu.cs.dennisc.croquet.RootContext generate( edu.cmu.cs.dennisc.croquet.UserInformation userInformation );
}

abstract class PriorInteractionHistoryBasedGuidedInteractionGenerator implements GuidedInteractionGenerator {
	private edu.cmu.cs.dennisc.croquet.RootContext originalRoot;
	public PriorInteractionHistoryBasedGuidedInteractionGenerator( edu.cmu.cs.dennisc.croquet.RootContext originalRoot ) {
		this.originalRoot = originalRoot;
	}
	protected abstract void filterAndAugment( edu.cmu.cs.dennisc.croquet.ModelContext< ? > originalRoot, edu.cmu.cs.dennisc.croquet.UserInformation userInformation );
	public edu.cmu.cs.dennisc.croquet.RootContext generate( edu.cmu.cs.dennisc.croquet.UserInformation userInformation ) {
		this.filterAndAugment( this.originalRoot, userInformation );
		return this.originalRoot;
	}
}

class AlgPriorInteractionHistoryBasedGuidedInteractionGenerator extends PriorInteractionHistoryBasedGuidedInteractionGenerator {
	public AlgPriorInteractionHistoryBasedGuidedInteractionGenerator( edu.cmu.cs.dennisc.croquet.RootContext originalRoot ) {
		super( originalRoot );
	}
	@Override
	protected void filterAndAugment( edu.cmu.cs.dennisc.croquet.ModelContext< ? > originalRoot, edu.cmu.cs.dennisc.croquet.UserInformation userInformation ) {
		java.util.ListIterator< edu.cmu.cs.dennisc.croquet.HistoryNode< ? > > listIterator = originalRoot.getChildListIterator();
		while( listIterator.hasNext() ) {
			edu.cmu.cs.dennisc.croquet.HistoryNode< ? > node = listIterator.next();
			if( node instanceof edu.cmu.cs.dennisc.croquet.ModelContext< ? > ) {
				edu.cmu.cs.dennisc.croquet.ModelContext< ? > modelContext = (edu.cmu.cs.dennisc.croquet.ModelContext< ? >)node;
				edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent successfulCompletionEvent = modelContext.getSuccessfulCompletionEvent();
				if( successfulCompletionEvent != null ) {
					edu.cmu.cs.dennisc.croquet.Edit< ? > edit = successfulCompletionEvent.getEdit();
					if( edit instanceof org.alice.ide.croquet.edits.ast.InsertStatementEdit ) {
						org.alice.ide.croquet.edits.ast.InsertStatementEdit insertStatementEdit = (org.alice.ide.croquet.edits.ast.InsertStatementEdit)edit;
						edu.cmu.cs.dennisc.alice.ast.Statement statement = insertStatementEdit.getStatement();
						if( userInformation instanceof AlgUserInformation ) {
							AlgUserInformation algUserInformation = (AlgUserInformation)userInformation;
							edu.cmu.cs.dennisc.cheshire.Message message = algUserInformation.createMessageIfUnfamiliarWithProgrammingConstruct( statement.getClass() );
							if( message != null ) {
								listIterator.previous();
								listIterator.add( message );
								listIterator.next();
							}
						}
					}
				}
			}
		}
		//originalRoot.addChild( 0, new edu.cmu.cs.dennisc.cheshire.Message( "title", "text" ) );
		originalRoot.addChild( new edu.cmu.cs.dennisc.cheshire.Message( "Finished", "<strong>Congratulations.</strong><br>You have completed the guided interaction." ) );
	}
}

class AlgTopDownASTGuidedInteractionGenerator implements GuidedInteractionGenerator {
	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice fieldType;
	public AlgTopDownASTGuidedInteractionGenerator( edu.cmu.cs.dennisc.alice.Project project, String sceneFieldName, String fieldName ) {
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice programType = project.getProgramType();
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice sceneType = (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)programType.getDeclaredField( sceneFieldName ).getValueType();
		for( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field : sceneType.fields ) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( field.getName() );
		}
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field = (edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice)sceneType.getDeclaredField( fieldName );
		assert field != null;
		this.fieldType = (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)field.valueType.getValue();
		assert this.fieldType != null;
	}
	
	private static edu.cmu.cs.dennisc.croquet.HistoryNode< ? > createDeclareProcedureContext( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice< ? > declaringType = method.getDeclaringType();
		org.alice.ide.croquet.models.ast.DeclareProcedureOperation operation = org.alice.ide.croquet.models.ast.DeclareProcedureOperation.getInstance( declaringType );
		edu.cmu.cs.dennisc.croquet.InputDialogOperationContext< ? > context = new edu.cmu.cs.dennisc.croquet.InputDialogOperationContext( operation, null, null );
		edu.cmu.cs.dennisc.croquet.Edit edit = new org.alice.ide.croquet.edits.ast.DeclareMethodEdit( declaringType, method );
		edit.setContext( context );
		edu.cmu.cs.dennisc.croquet.CommitEvent commitEvent = new edu.cmu.cs.dennisc.croquet.CommitEvent( edit );
		context.handleWindowOpened( null );
		context.handleWindowClosing( null );
		context.addChild( commitEvent );
		return context;
	}
	
	public edu.cmu.cs.dennisc.croquet.RootContext generate( edu.cmu.cs.dennisc.croquet.UserInformation userInformation ) {
//		edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = org.alice.ide.ast.NodeUtilities.createProcedure( "inflate" );
//		this.fieldType.methods.add( method );
		edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = this.fieldType.methods.get( 0 );
		assert method != null;
		edu.cmu.cs.dennisc.croquet.RootContext rv = new edu.cmu.cs.dennisc.croquet.RootContext();
		rv.addChild( new edu.cmu.cs.dennisc.cheshire.Message( "top down", "generated from project AST" ) );
		rv.addChild( createDeclareProcedureContext( method ) );
//		org.alice.ide.croquet.models.ui.IsSceneEditorExpandedState isSceneEditorExpandedState = org.alice.ide.croquet.models.ui.IsSceneEditorExpandedState.getInstance();
//		edu.cmu.cs.dennisc.croquet.BooleanStateContext context = new edu.cmu.cs.dennisc.croquet.BooleanStateContext( isSceneEditorExpandedState, null, null );
//		edu.cmu.cs.dennisc.croquet.Edit<?> edit = new org.alice.ide.croquet.edits.ast.DeclareMethodEdit( declaringType, method );
//		edu.cmu.cs.dennisc.croquet.CommitEvent commitEvent = new edu.cmu.cs.dennisc.croquet.CommitEvent( edit );
//		rv.addChild( context );
		return rv;
	}
}

class AlgConstructionGuide extends edu.cmu.cs.dennisc.cheshire.GuidedInteraction {
	private static boolean IS_MONKEY_WRENCH_DESIRED = false;
	public AlgConstructionGuide() {
		super( 
			AlgUserInformation.INSTANCE,
			
			
//			edu.cmu.cs.dennisc.tutorial.MenuPolicy.ABOVE_STENCIL_WITH_FEEDBACK,
//			edu.cmu.cs.dennisc.tutorial.MenuPolicy.ABOVE_STENCIL_WITHOUT_FEEDBACK,
			edu.cmu.cs.dennisc.cheshire.MenuPolicy.BELOW_STENCIL,
			
			
			//edu.cmu.cs.dennisc.croquet.guide.StepAccessPolicy.ALLOW_ACCESS_UP_TO_AND_INCLUDING_FURTHEST_COMPLETED_STEP,
			edu.cmu.cs.dennisc.cheshire.StepAccessPolicy.ALLOW_ACCESS_TO_ALL_STEPS,
			
			edu.cmu.cs.dennisc.tutorial.DefaultScrollingRequiredRenderer.INSTANCE,
			new edu.cmu.cs.dennisc.croquet.Group[] { edu.cmu.cs.dennisc.alice.Project.GROUP, org.alice.ide.IDE.UI_STATE_GROUP }
		);

		if( IS_MONKEY_WRENCH_DESIRED ) {
			final int N = org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance().getItemCount();
			org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance().setSelectedIndex( 0 );
			//org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance().setSelectedIndex( N-2 );
		}
		
	}
	@Override
	protected java.util.List< edu.cmu.cs.dennisc.cheshire.RetargetableNote > addNotesToGetIntoTheRightStateWhenNoViewControllerCanBeFound( 
			java.util.List< edu.cmu.cs.dennisc.cheshire.RetargetableNote > rv,
			edu.cmu.cs.dennisc.cheshire.ParentContextCriterion parentContextCriterion, 
			edu.cmu.cs.dennisc.croquet.ModelContext< ? > modelContext ) {
		
		if( IS_MONKEY_WRENCH_DESIRED ) {
			System.err.println( "addNotesToGetIntoTheRightStateWhenNoViewControllerCanBeFound: " + modelContext );
			org.alice.ide.croquet.models.ui.AccessibleListSelectionState accessibleListSelectionState = org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance();
			edu.cmu.cs.dennisc.croquet.ListSelectionStateContext context = edu.cmu.cs.dennisc.croquet.ContextManager.createContextFor( accessibleListSelectionState, accessibleListSelectionState.getItemAt( accessibleListSelectionState.getItemCount()-1 ) );
			edu.cmu.cs.dennisc.cheshire.ListSelectionStateStartNote listSelectionStateStartNote =  edu.cmu.cs.dennisc.cheshire.ListSelectionStateStartNote.createInstance( context, parentContextCriterion, context.getSuccessfulCompletionEvent() );
			rv.add( listSelectionStateStartNote );
			rv.add( edu.cmu.cs.dennisc.cheshire.ListSelectionStateFinishNote.createInstance( context, listSelectionStateStartNote.getAcceptedContextAt( 0 ), context.getSuccessfulCompletionEvent() ) );

		}
		
		if( IS_MONKEY_WRENCH_DESIRED ) {
			org.alice.ide.croquet.models.members.MembersTabSelectionState membersTabSelectionState = org.alice.ide.croquet.models.members.MembersTabSelectionState.getInstance();
			edu.cmu.cs.dennisc.croquet.ListSelectionStateContext context = edu.cmu.cs.dennisc.croquet.ContextManager.createContextFor( membersTabSelectionState, membersTabSelectionState.getItemAt( 1 ) );
			rv.add( edu.cmu.cs.dennisc.cheshire.ListSelectionStateSimpleNote.createInstance( context, parentContextCriterion, context.getSuccessfulCompletionEvent() ) );
		}
		return rv;
	}
}


public class AutomaticTutorialIde extends org.alice.stageide.StageIDE {
	private static boolean IS_ENCODING;
	private static boolean IS_WIZARD_OF_OZ_HASTINGS_DESIRED;
	private static boolean IS_BASED_ON_INTERACTION_AST;
	private static final String UI_HISTORY_PATH = "/autoTutorial1.bin";
	private static final String AST_MIMIC_PATH = "/astMimic1.bin";
	private static final String POST_PROJECT_PATH = "/post.a3p";
	
	private boolean isPostProjectLive = false;
	private edu.cmu.cs.dennisc.alice.Project originalProject;
	private edu.cmu.cs.dennisc.croquet.RootContext originalContext;
	
	@Override
	public void loadProjectFrom( java.net.URI uri ) {
		super.loadProjectFrom( uri );
		org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().setValue( IS_ENCODING );
		//org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().setValue( false );
		if( IS_ENCODING ) {
			edu.cmu.cs.dennisc.croquet.ModelContext< ? > rootContext = edu.cmu.cs.dennisc.croquet.ContextManager.getRootContext();
			rootContext.EPIC_HACK_clear();
		}
	}

	@Override
	public edu.cmu.cs.dennisc.alice.Project getProject() {
		if( this.isPostProjectLive ) {
			return this.originalProject;
		} else {
			return super.getProject();
		}
	}

	private void retarget() {
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

		edu.cmu.cs.dennisc.alice.Project replacementProject = this.getProject();
		AstDecodingRetargeter astDecodingRetargeter = new AstDecodingRetargeter();
		astDecodingRetargeter.addAllToReplacementMap( replacementProject );

		if( IS_WIZARD_OF_OZ_HASTINGS_DESIRED ) {
			WizardOfHastings.castPart( astDecodingRetargeter, this.originalProject, "guppy", replacementProject, "car" );
		}
		this.originalContext.retarget( astDecodingRetargeter );
	}
	private void createAndShowTutorial() {
		//final org.alice.ide.tutorial.IdeTutorial tutorial = new org.alice.ide.tutorial.IdeTutorial( this, 0 );
		this.originalProject = edu.cmu.cs.dennisc.alice.project.ProjectUtilities.readProject( POST_PROJECT_PATH );

		GuidedInteractionGenerator generator;
		if( IS_BASED_ON_INTERACTION_AST ) {
			//this.isPostProjectLive = true;
			generator = new AlgTopDownASTGuidedInteractionGenerator( this.originalProject, "scene", "guppy" );
			//this.isPostProjectLive = false;
		} else {
			edu.cmu.cs.dennisc.codec.CodecUtilities.isDebugDesired = true;
			this.isPostProjectLive = true;
			this.originalContext = edu.cmu.cs.dennisc.codec.CodecUtilities.decodeBinary( UI_HISTORY_PATH, edu.cmu.cs.dennisc.croquet.RootContext.class );

			final boolean IS_INFORMATION_GROUP_INCLUDED = false;
			edu.cmu.cs.dennisc.cheshire.GroupFilter.SINGLETON.addGroup( edu.cmu.cs.dennisc.alice.Project.GROUP, edu.cmu.cs.dennisc.cheshire.GroupFilter.SuccessfulCompletionPolicy.ONLY_COMMITS );
			edu.cmu.cs.dennisc.cheshire.GroupFilter.SINGLETON.addGroup( edu.cmu.cs.dennisc.croquet.Application.UI_STATE_GROUP, edu.cmu.cs.dennisc.cheshire.GroupFilter.SuccessfulCompletionPolicy.ONLY_COMMITS );
			edu.cmu.cs.dennisc.cheshire.GroupFilter.SINGLETON.addGroup( org.alice.ide.IDE.RUN_GROUP, edu.cmu.cs.dennisc.cheshire.GroupFilter.SuccessfulCompletionPolicy.BOTH_COMMITS_AND_FINISHES );
			if( IS_INFORMATION_GROUP_INCLUDED ) {
				edu.cmu.cs.dennisc.cheshire.GroupFilter.SINGLETON.addGroup( edu.cmu.cs.dennisc.croquet.Application.INFORMATION_GROUP, edu.cmu.cs.dennisc.cheshire.GroupFilter.SuccessfulCompletionPolicy.BOTH_COMMITS_AND_FINISHES );
			}
			edu.cmu.cs.dennisc.cheshire.Filter[] filters = {
					//edu.cmu.cs.dennisc.croquet.tutorial.SuccessfullyCompletedFilter.SINGLETON,
					edu.cmu.cs.dennisc.cheshire.MenuSelectionEventFilter.SINGLETON,
					edu.cmu.cs.dennisc.cheshire.DocumentEventFilter.SINGLETON,
					edu.cmu.cs.dennisc.cheshire.GroupFilter.SINGLETON,
			};
			for( edu.cmu.cs.dennisc.cheshire.Filter filter : filters ) {
				this.originalContext = filter.filter( this.originalContext );
			}
			this.isPostProjectLive = false;
			edu.cmu.cs.dennisc.codec.CodecUtilities.isDebugDesired = false;

			generator = new AlgPriorInteractionHistoryBasedGuidedInteractionGenerator( this.originalContext );
		}

		this.originalContext = generator.generate( AlgUserInformation.INSTANCE );

		if( IS_BASED_ON_INTERACTION_AST ) {
			this.isPostProjectLive = true;
			edu.cmu.cs.dennisc.codec.CodecUtilities.encodeBinary( this.originalContext, AST_MIMIC_PATH );
			this.originalContext = edu.cmu.cs.dennisc.codec.CodecUtilities.decodeBinary( AST_MIMIC_PATH, edu.cmu.cs.dennisc.croquet.RootContext.class );
			this.isPostProjectLive = false;
		}
		this.retarget();
		
		final AlgConstructionGuide tutorial = new AlgConstructionGuide();
		tutorial.setOriginalRoot( this.originalContext );
		
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
		AstLiveRetargeter astLiveRetargeter = new AstLiveRetargeter();
		tutorial.setRetargeter( astLiveRetargeter );

		tutorial.setVisible( true );
		this.getFrame().setVisible( true );
		
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				org.alice.ide.croquet.models.ui.debug.IsInteractionTreeShowingState isInteractionTreeShowingState = new org.alice.ide.croquet.models.ui.debug.IsInteractionTreeShowingState( originalContext );
				isInteractionTreeShowingState.setValue( true );
				tutorial.setSelectedIndex( 0 );
			}
		} );

	}
	
	@Override
	protected void handleQuit( java.util.EventObject e ) {
		this.preservePreferences();
//		super.handleQuit( e );
		if( IS_ENCODING ) {
			edu.cmu.cs.dennisc.croquet.ModelContext< ? > rootContext = edu.cmu.cs.dennisc.croquet.ContextManager.getRootContext();
			
			System.err.println( "todo: remove filtering" );
			edu.cmu.cs.dennisc.cheshire.Filter[] filters = {
					edu.cmu.cs.dennisc.cheshire.MenuSelectionEventFilter.SINGLETON,
			};
			for( edu.cmu.cs.dennisc.cheshire.Filter filter : filters ) {
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

	@Override
	protected StringBuffer updateTitle( StringBuffer rv ) {
		rv.append( "AnonymizedForPeerReview" );
		return rv;
	}

	public static void main( String[] args ) throws Exception {
		IS_ENCODING = Boolean.parseBoolean( args[ 5 ] );
		if( IS_ENCODING ) {
			IS_WIZARD_OF_OZ_HASTINGS_DESIRED = false;
			IS_BASED_ON_INTERACTION_AST = false;
		} else {
			IS_WIZARD_OF_OZ_HASTINGS_DESIRED = Boolean.parseBoolean( args[ 6 ] );
			IS_BASED_ON_INTERACTION_AST = Boolean.parseBoolean( args[ 7 ] );
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
