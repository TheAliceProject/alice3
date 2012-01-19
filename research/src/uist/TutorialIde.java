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

	private static boolean IS_STENCILS;
	
	private static String ROOT_PATH;
	private static final String TRANSACTION_HISTORY_PATH = "/transactionHistory.bin";
	private static final String AST_MIMIC_PATH = "/astMimic1.bin";
	private static final String POST_PROJECT_PATH = "/post.a3p";
	
	private static org.lgna.project.Project originalProject;
	private static org.lgna.croquet.history.TransactionHistory originalTransactionHistory;
	
	private boolean isOriginalProjectLive = false;
	@Override
	public void loadProjectFrom( java.net.URI uri ) {
		super.loadProjectFrom( uri );
		org.alice.ide.croquet.models.ui.debug.IsTransactionHistoryShowingState.getInstance().setValueTransactionlessly( true );
		if( IS_ENCODING ) {
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					org.lgna.croquet.history.TransactionManager.getRootTransactionHistory().EPIC_HACK_clear();
				}
			} );
		}
	}

	private org.lgna.project.Project getOriginalProject() {
		return originalProject;
	}
	private org.lgna.project.Project getReplacementProject() {
		return super.getProject();
	}

	@Override
	public org.lgna.project.Project getProject() {
		if( this.isOriginalProjectLive ) {
			return this.getOriginalProject();
		} else {
			return this.getReplacementProject();
		}
	}

	private static class AstDecodingRetargeter implements org.lgna.croquet.Retargeter {
		private java.util.Map< java.util.UUID, org.lgna.project.ast.Node > mapIdToReplacementNode = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
		public void addAllToReplacementMap( org.lgna.project.Project project ) {
			org.lgna.project.ast.NamedUserType programType = project.getProgramType();
			edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< org.lgna.project.ast.AbstractNode > crawler = edu.cmu.cs.dennisc.pattern.IsInstanceCrawler.createInstance( org.lgna.project.ast.AbstractNode.class );
			programType.crawl( crawler, true );
			for( org.lgna.project.ast.AbstractNode node : crawler.getList() ) {
				//if( node.isAppropriatelyIdenitifiedById() ) {
					this.addKeyValuePair( node, node );
				//}
				//mapIdToReplacementNode.put( node.getId(), node );
			}
		}
		public void addKeyValuePair( Object key, Object value ) {
			if( key instanceof org.lgna.project.ast.Node && value instanceof org.lgna.project.ast.Node ) {
				org.lgna.project.ast.Node keyNode = ((org.lgna.project.ast.Node)key);
				org.lgna.project.ast.Node valueNode = ((org.lgna.project.ast.Node)value);
				this.mapIdToReplacementNode.put( keyNode.getId(), valueNode );
			} else {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: IGNORING addKeyValuePair", key, value );
			}
		}
		public <N> N retarget(N value) {
			if( value != null ) {
				N rv;
				if( value instanceof org.lgna.project.ast.Node ) {
					org.lgna.project.ast.Node originalNode = (org.lgna.project.ast.Node)value;
					org.lgna.project.ast.Node retargetedNode = mapIdToReplacementNode.get( originalNode.getId() );
					if( retargetedNode != null ) {
						rv = (N)retargetedNode;
					} else {
						rv = value;
					}
				} else if( value instanceof org.alice.ide.declarationseditor.DeclarationComposite ) {
					rv = (N)org.alice.ide.declarationseditor.DeclarationComposite.getInstance( retarget( ((org.alice.ide.declarationseditor.DeclarationComposite)value).getDeclaration() ) );
				} else {
					rv = value;
				}
				assert rv != null : value;
				return rv;
			} else {
				return null;
			}
		}
	};

	private static org.lgna.project.ast.BlockStatement getMyFirstMethodBody( org.lgna.project.Project project ) {
		org.lgna.project.ast.NamedUserType sceneType = (org.lgna.project.ast.NamedUserType)project.getProgramType().fields.get( 0 ).getValueType();
		org.lgna.project.ast.UserMethod myFirstMethod = sceneType.getDeclaredMethod( "myFirstMethod" );
		return myFirstMethod.body.getValue();
	}
	
	private void createAndShowTutorial() {
		if( IS_BASED_ON_INTERACTION_AST ) {
			uist.ast.TransactionHistoryGenerator transactionHistoryGenerator = new uist.ast.TransactionHistoryGenerator( getMyFirstMethodBody( this.getOriginalProject() ), getMyFirstMethodBody( this.getReplacementProject() ), 0 );
			org.lgna.croquet.UserInformation userInformation = null;
			originalTransactionHistory = transactionHistoryGenerator.generate( userInformation );
			//encode and decode
			this.isOriginalProjectLive = true;
			edu.cmu.cs.dennisc.codec.CodecUtilities.encodeBinary( originalTransactionHistory, ROOT_PATH+AST_MIMIC_PATH );
			originalTransactionHistory = edu.cmu.cs.dennisc.codec.CodecUtilities.decodeBinary( ROOT_PATH+AST_MIMIC_PATH, org.lgna.croquet.history.TransactionHistory.class );
			this.isOriginalProjectLive = false;

		} else {
			edu.cmu.cs.dennisc.codec.CodecUtilities.isDebugDesired = true;
			try {
				this.isOriginalProjectLive = true;
//				edu.cmu.cs.dennisc.codec.CodecUtilities.decodeBinary( ROOT_PATH+CONTEXT_PATH, edu.cmu.cs.dennisc.croquet.RootContext.class );
				originalTransactionHistory = edu.cmu.cs.dennisc.codec.CodecUtilities.decodeBinary( ROOT_PATH+TRANSACTION_HISTORY_PATH, org.lgna.croquet.history.TransactionHistory.class );
				this.isOriginalProjectLive = false;
			} finally {
				edu.cmu.cs.dennisc.codec.CodecUtilities.isDebugDesired = false;
			}
		}

		//final org.alice.ide.tutorial.IdeTutorial tutorial = new org.alice.ide.tutorial.IdeTutorial( this, 0 );
		org.lgna.cheshire.Filterer filterer = new uist.filterers.TutorialFilterer();

		org.lgna.project.Project replacementProject = this.getReplacementProject();
		AstDecodingRetargeter astDecodingRetargeter = new AstDecodingRetargeter();
		astDecodingRetargeter.addAllToReplacementMap( replacementProject );

		if( IS_WIZARD_OF_OZ_HASTINGS_DESIRED ) {
			WizardOfHastings.castPart( astDecodingRetargeter, this.getOriginalProject(), "puffy", replacementProject, "car" );
			//WizardOfHastings.castType( astDecodingRetargeter, this.getOriginalProject(), "MyClownFish", replacementProject, "MyPirateShip" );
		}
		originalTransactionHistory.retarget( astDecodingRetargeter );

		Recoverer recoverer = new Recoverer();
		final org.lgna.cheshire.Presentation presentation;
		if( IS_STENCILS ) {
			presentation = new org.lgna.cheshire.stencil.StencilsPresentation( 
					UserInformation.INSTANCE, 
					
					//edu.cmu.cs.dennisc.croquet.guide.StepAccessPolicy.ALLOW_ACCESS_UP_TO_AND_INCLUDING_FURTHEST_COMPLETED_STEP,
					org.lgna.cheshire.ChapterAccessPolicy.ALLOW_ACCESS_TO_ALL_CHAPTERS,

					originalTransactionHistory, 
					MigrationManager.INSTANCE, 
					filterer,
					recoverer,
					
					new org.lgna.croquet.Group[] { org.alice.ide.IDE.PROJECT_GROUP, org.alice.ide.IDE.UI_STATE_GROUP },
					
					org.lgna.stencil.DefaultScrollingRequiredRenderer.INSTANCE,

//					org.lgna.stencil.MenuPolicy.ABOVE_STENCIL_WITH_FEEDBACK
					org.lgna.stencil.MenuPolicy.ABOVE_STENCIL_WITHOUT_FEEDBACK
//					org.lgna.stencil.MenuPolicy.BELOW_STENCIL
			);
		} else {
			presentation = new org.lgna.cheshire.docwizardsesque.DocWizardsesquePresentation( UserInformation.INSTANCE, originalTransactionHistory, MigrationManager.INSTANCE, uist.filterers.FinishFilterer.INSTANCE, recoverer, new org.lgna.croquet.Group[] { org.alice.ide.IDE.PROJECT_GROUP, org.alice.ide.IDE.UI_STATE_GROUP } );
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
				//edu.cmu.cs.dennisc.java.lang.ThreadUtilities.sleep( 500 );
				presentation.setSelectedIndex( 0 );
			}
		} );

	}
	
	@Override
	protected void handleQuit( org.lgna.croquet.triggers.Trigger trigger ) {
		this.preservePreferences();
//		super.handleQuit( e );
		if( IS_ENCODING ) {
//			edu.cmu.cs.dennisc.cheshire.Filter[] filters = {
//					edu.cmu.cs.dennisc.cheshire.MenuSelectionEventFilter.SINGLETON,
//			};
//			for( edu.cmu.cs.dennisc.cheshire.Filter filter : filters ) {
//				rootContext = filter.filter( rootContext );
//			}

			edu.cmu.cs.dennisc.codec.CodecUtilities.isDebugDesired = true;
			try {
				edu.cmu.cs.dennisc.codec.CodecUtilities.encodeBinary( org.lgna.croquet.history.TransactionManager.getRootTransactionHistory(), ROOT_PATH+TRANSACTION_HISTORY_PATH );
				
//				edu.cmu.cs.dennisc.croquet.RootContext rootContext = edu.cmu.cs.dennisc.croquet.ContextManager.getRootContext();
//				edu.cmu.cs.dennisc.cheshire.Filter[] filters = {
//						edu.cmu.cs.dennisc.cheshire.MenuSelectionEventFilter.SINGLETON,
//				};
//				for( edu.cmu.cs.dennisc.cheshire.Filter filter : filters ) {
//					rootContext = filter.filter( rootContext );
//				}
//				
//				edu.cmu.cs.dennisc.codec.CodecUtilities.encodeBinary( edu.cmu.cs.dennisc.croquet.ContextManager.getRootContext(), ROOT_PATH+CONTEXT_PATH );
			} finally {
				edu.cmu.cs.dennisc.codec.CodecUtilities.isDebugDesired = false;
			}

			try {
				org.lgna.project.io.IoUtilities.writeProject( ROOT_PATH+POST_PROJECT_PATH, this.getProject() );
			} catch( java.io.IOException ioe ) {
				throw new RuntimeException( ioe );
			}
		}
		System.exit( 0 );
	}

	@Override
	protected StringBuffer updateTitle( StringBuffer rv ) {
		rv.append( "" );
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
		} else {
			org.alice.ide.croquet.models.ui.debug.IsTransactionHistoryShowingState.IS_SIDE_DOCKING_DESIRED = true;
			IS_WIZARD_OF_OZ_HASTINGS_DESIRED = Boolean.parseBoolean( args[ 7 ] );
			IS_MONKEY_WRENCH_DESIRED = Boolean.parseBoolean( args[ 8 ] );
			IS_STENCILS  = Boolean.parseBoolean( args[ 9 ] );
			IS_BASED_ON_INTERACTION_AST = Boolean.parseBoolean( args[ 10 ] );
		}
		//org.alice.stageide.sceneeditor.StorytellingSceneEditor.IS_RUN_BUTTON_DESIRED = IS_MONKEY_WRENCH_DESIRED == false;
		org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().setValue( IS_ENCODING || IS_MONKEY_WRENCH_DESIRED == false );
		//org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().setValue( true );

		org.alice.ide.croquet.models.ui.preferences.IsAlwaysShowingBlocksState.getInstance().setValue( IS_ENCODING || IS_MONKEY_WRENCH_DESIRED == false );
		//org.alice.ide.croquet.models.ui.preferences.IsAlwaysShowingBlocksState.getInstance().setValue( false );
		//org.alice.ide.croquet.models.ui.preferences.IsAlwaysShowingBlocksState.getInstance().setValue( true );

		
		try {
			if (IS_ENCODING) {
				originalProject = null;
			} else {
				originalProject = org.lgna.project.io.IoUtilities.readProject( new java.io.File( ROOT_PATH+POST_PROJECT_PATH ) );
			}
		} catch( java.io.IOException ioe ) {			
			throw new AssertionError();
		} catch( org.lgna.project.VersionNotSupportedException vnse )  {
			throw new AssertionError();
		}

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
