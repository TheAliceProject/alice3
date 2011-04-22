/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package uist;

/**
 * @author Dennis Cosgrove
 */
public class TutorialIde extends org.alice.stageide.StageIDE {
	/*package-private*/ static boolean IS_MONKEY_WRENCH_DESIRED;

	private static boolean IS_ENCODING;
	private static boolean IS_WIZARD_OF_OZ_HASTINGS_DESIRED;
	private static boolean IS_BASED_ON_INTERACTION_AST;
	private static boolean IS_OPTIMIZED_FOR_BUG_REPRO;

	private static boolean IS_STENCILS;
	
	private static String ROOT_PATH;
	private static final String TRANSACTION_HISTORY_PATH = "/transactionHistory.bin";
	//note: we encode and decode the root context temporarily while models (ListSelectionState (Edit) ) still rely on context (for things like getCodec())
	private static final String CONTEXT_PATH = "/context.bin";
	private static final String AST_MIMIC_PATH = "/astMimic1.bin";
	private static final String POST_PROJECT_PATH = "/post.a3p";
	
	private boolean isOriginalProjectLive = false;
	private edu.cmu.cs.dennisc.alice.Project originalProject;
	private org.lgna.croquet.steps.TransactionHistory originalTransactionHistory;
	
	@Override
	public void loadProjectFrom( java.net.URI uri ) {
		super.loadProjectFrom( uri );
		if( IS_ENCODING ) {
			org.alice.ide.croquet.models.ui.debug.IsTransactionHistoryShowingState.getInstance().setValue( true );
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					org.lgna.croquet.steps.TransactionManager.getRootTransactionHistory().EPIC_HACK_clear();
					edu.cmu.cs.dennisc.croquet.ContextManager.getRootContext().EPIC_HACK_clear();
				}
			} );
		}
	}

	private edu.cmu.cs.dennisc.alice.Project getOriginalProject() {
		return this.originalProject;
	}
	private edu.cmu.cs.dennisc.alice.Project getReplacementProject() {
		return super.getProject();
	}

	@Override
	public edu.cmu.cs.dennisc.alice.Project getProject() {
		if( this.isOriginalProjectLive ) {
			return this.getOriginalProject();
		} else {
			return this.getReplacementProject();
		}
	}
	

	private void retarget() {
		//note: we leverage the fact that the uuids are identical for much of the initial states of the two projects
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
				} else if( value instanceof org.alice.ide.editorstabbedpane.CodeComposite ) {
					return (N)org.alice.ide.editorstabbedpane.CodeComposite.getInstance( retarget( ((org.alice.ide.editorstabbedpane.CodeComposite)value).getCode() ) );
				} else {
					return value;
				}
			}
		};

		edu.cmu.cs.dennisc.alice.Project replacementProject = this.getReplacementProject();
		AstDecodingRetargeter astDecodingRetargeter = new AstDecodingRetargeter();
		astDecodingRetargeter.addAllToReplacementMap( replacementProject );

		if( IS_WIZARD_OF_OZ_HASTINGS_DESIRED ) {
			WizardOfHastings.castPart( astDecodingRetargeter, this.getOriginalProject(), "guppy", replacementProject, "car" );
		}
		this.originalTransactionHistory.retarget( astDecodingRetargeter );
	}
	private static edu.cmu.cs.dennisc.alice.ast.BlockStatement getRunBody( edu.cmu.cs.dennisc.alice.Project project ) {
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice sceneType = (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)project.getProgramType().fields.get( 0 ).getValueType();
		edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice runMethod = sceneType.getDeclaredMethod( "run" );
		return runMethod.body.getValue();
	}
	private void createAndShowTutorial() {
		//final org.alice.ide.tutorial.IdeTutorial tutorial = new org.alice.ide.tutorial.IdeTutorial( this, 0 );
		this.originalProject = edu.cmu.cs.dennisc.alice.project.ProjectUtilities.readProject( ROOT_PATH+POST_PROJECT_PATH );

		if( IS_BASED_ON_INTERACTION_AST ) {
			uist.ast.TransactionHistoryGenerator transactionHistoryGenerator = new uist.ast.TransactionHistoryGenerator( getRunBody( this.getOriginalProject() ), getRunBody( this.getReplacementProject() ), 0 );
			this.originalTransactionHistory = transactionHistoryGenerator.generate();
			//encode and decode
			this.isOriginalProjectLive = true;
			edu.cmu.cs.dennisc.codec.CodecUtilities.encodeBinary( this.originalTransactionHistory, ROOT_PATH+AST_MIMIC_PATH );
			this.originalTransactionHistory = edu.cmu.cs.dennisc.codec.CodecUtilities.decodeBinary( ROOT_PATH+AST_MIMIC_PATH, org.lgna.croquet.steps.TransactionHistory.class );
			this.isOriginalProjectLive = false;

		} else {
			edu.cmu.cs.dennisc.codec.CodecUtilities.isDebugDesired = true;
			this.isOriginalProjectLive = true;
			edu.cmu.cs.dennisc.codec.CodecUtilities.decodeBinary( ROOT_PATH+CONTEXT_PATH, edu.cmu.cs.dennisc.croquet.RootContext.class );
			this.originalTransactionHistory = edu.cmu.cs.dennisc.codec.CodecUtilities.decodeBinary( ROOT_PATH+TRANSACTION_HISTORY_PATH, org.lgna.croquet.steps.TransactionHistory.class );
			this.isOriginalProjectLive = false;
			edu.cmu.cs.dennisc.codec.CodecUtilities.isDebugDesired = false;
		}

		org.lgna.cheshire.Filterer filterer;
		if( IS_OPTIMIZED_FOR_BUG_REPRO ) {
			//do not filter
			filterer = uist.filterers.SentinelFilterer.INSTANCE;
		} else {
			filterer = new uist.filterers.TutorialFilterer();
		}
		this.retarget();
		Recoverer recoverer = new Recoverer();
		final org.lgna.cheshire.Presentation presentation;
		if( IS_STENCILS ) {
			presentation = new org.lgna.cheshire.stencil.StencilsPresentation( 
					UserInformation.INSTANCE, 
					
					//edu.cmu.cs.dennisc.croquet.guide.StepAccessPolicy.ALLOW_ACCESS_UP_TO_AND_INCLUDING_FURTHEST_COMPLETED_STEP,
					org.lgna.cheshire.ChapterAccessPolicy.ALLOW_ACCESS_TO_ALL_CHAPTERS,

					this.originalTransactionHistory, 
					MigrationManager.INSTANCE, 
					filterer,
					recoverer,
					
					new edu.cmu.cs.dennisc.croquet.Group[] { edu.cmu.cs.dennisc.alice.Project.GROUP, org.alice.ide.IDE.UI_STATE_GROUP },
					
					org.lgna.stencil.MenuPolicy.ABOVE_STENCIL_WITH_FEEDBACK,
//					org.lgna.stencil.MenuPolicy.ABOVE_STENCIL_WITHOUT_FEEDBACK,
//					org.lgna.stencil.MenuPolicy.BELOW_STENCIL,
					
					org.lgna.stencil.DefaultScrollingRequiredRenderer.INSTANCE,
					IS_OPTIMIZED_FOR_BUG_REPRO 
			);
		} else {
			presentation = new org.lgna.cheshire.docwizardsesque.DocWizardsesquePresentation( UserInformation.INSTANCE, this.originalTransactionHistory, MigrationManager.INSTANCE, uist.filterers.SentinelFilterer.INSTANCE, recoverer, new edu.cmu.cs.dennisc.croquet.Group[] { edu.cmu.cs.dennisc.alice.Project.GROUP, org.alice.ide.IDE.UI_STATE_GROUP } );
		}
		AstLiveRetargeter astLiveRetargeter = new AstLiveRetargeter();
		presentation.setRetargeter( astLiveRetargeter );

		presentation.setVisible( true );
		this.getFrame().setVisible( true );
		
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				//org.alice.ide.croquet.models.ui.debug.IsInteractionTreeShowingState.getInstance().setValue( true );
				org.alice.ide.croquet.models.ui.debug.IsTransactionHistoryShowingState isInteractionTreeShowingState = org.alice.ide.croquet.models.ui.debug.IsTransactionHistoryShowingState.createInstance( originalTransactionHistory );
				isInteractionTreeShowingState.setValue( true );
				if( IS_OPTIMIZED_FOR_BUG_REPRO ) {
					presentation.setSelectedIndex( -1 );
				} else {
					presentation.setSelectedIndex( 0 );
				}
			}
		} );

	}
	
	@Override
	protected void handleQuit( java.util.EventObject e ) {
		this.preservePreferences();
//		super.handleQuit( e );
		if( IS_ENCODING ) {
//			System.err.println( "todo: remove filtering" );
//			edu.cmu.cs.dennisc.cheshire.Filter[] filters = {
//					edu.cmu.cs.dennisc.cheshire.MenuSelectionEventFilter.SINGLETON,
//			};
//			for( edu.cmu.cs.dennisc.cheshire.Filter filter : filters ) {
//				rootContext = filter.filter( rootContext );
//			}

			edu.cmu.cs.dennisc.codec.CodecUtilities.isDebugDesired = true;
			
			edu.cmu.cs.dennisc.codec.CodecUtilities.encodeBinary( org.lgna.croquet.steps.TransactionManager.getRootTransactionHistory(), ROOT_PATH+TRANSACTION_HISTORY_PATH );
			
			edu.cmu.cs.dennisc.croquet.RootContext rootContext = edu.cmu.cs.dennisc.croquet.ContextManager.getRootContext();
			System.err.println( "todo: remove filtering" );
			edu.cmu.cs.dennisc.cheshire.Filter[] filters = {
					edu.cmu.cs.dennisc.cheshire.MenuSelectionEventFilter.SINGLETON,
			};
			for( edu.cmu.cs.dennisc.cheshire.Filter filter : filters ) {
				rootContext = filter.filter( rootContext );
			}
			
			edu.cmu.cs.dennisc.codec.CodecUtilities.encodeBinary( edu.cmu.cs.dennisc.croquet.ContextManager.getRootContext(), ROOT_PATH+CONTEXT_PATH );
			edu.cmu.cs.dennisc.codec.CodecUtilities.isDebugDesired = false;

			try {
				edu.cmu.cs.dennisc.alice.project.ProjectUtilities.writeProject( ROOT_PATH+POST_PROJECT_PATH, this.getProject() );
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
		ROOT_PATH = args[ 6 ];
		if( IS_ENCODING ) {
			org.alice.ide.croquet.models.ui.debug.IsTransactionHistoryShowingState.IS_SIDE_DOCKING_DESIRED = true;
			IS_WIZARD_OF_OZ_HASTINGS_DESIRED = false;
			IS_MONKEY_WRENCH_DESIRED = false;
			IS_STENCILS = false;
			IS_BASED_ON_INTERACTION_AST = false;
			IS_OPTIMIZED_FOR_BUG_REPRO = false;
		} else {
			IS_WIZARD_OF_OZ_HASTINGS_DESIRED = Boolean.parseBoolean( args[ 7 ] );
			IS_MONKEY_WRENCH_DESIRED = Boolean.parseBoolean( args[ 8 ] );
			IS_STENCILS  = Boolean.parseBoolean( args[ 9 ] );
			IS_BASED_ON_INTERACTION_AST = Boolean.parseBoolean( args[ 10 ] );
			IS_OPTIMIZED_FOR_BUG_REPRO = Boolean.parseBoolean( args[ 11 ] );
		}
		org.alice.stageide.sceneeditor.MoveAndTurnSceneEditor.IS_RUN_BUTTON_DESIRED = IS_MONKEY_WRENCH_DESIRED == false;
		org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().setValue( IS_ENCODING || IS_MONKEY_WRENCH_DESIRED == false );
		//org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().setValue( true );

		org.alice.ide.croquet.models.ui.preferences.IsAlwaysShowingBlocksState.getInstance().setValue( IS_ENCODING || IS_MONKEY_WRENCH_DESIRED == false );
		//org.alice.ide.croquet.models.ui.preferences.IsAlwaysShowingBlocksState.getInstance().setValue( false );
		//org.alice.ide.croquet.models.ui.preferences.IsAlwaysShowingBlocksState.getInstance().setValue( true );
		final TutorialIde ide = org.alice.ide.LaunchUtilities.launchAndWait( TutorialIde.class, null, args, false );
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
