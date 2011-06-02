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
public class TutorialECard extends uist.ecard.ECardApplication {
	private static boolean IS_ENCODING;
	private static String ROOT_PATH;
	private static final String TRANSACTION_HISTORY_SUB_PATH = "/transactionHistory.bin";
	private org.lgna.croquet.steps.TransactionHistory originalTransactionHistory;

	private void createAndShowTutorial() {
		edu.cmu.cs.dennisc.codec.CodecUtilities.isDebugDesired = true;
		this.originalTransactionHistory = edu.cmu.cs.dennisc.codec.CodecUtilities.decodeBinary( ROOT_PATH + TRANSACTION_HISTORY_SUB_PATH, org.lgna.croquet.steps.TransactionHistory.class );
		edu.cmu.cs.dennisc.codec.CodecUtilities.isDebugDesired = false;

		org.lgna.cheshire.Filterer filterer = new uist.filterers.TutorialFilterer();
		org.lgna.cheshire.Recoverer recoverer = new org.lgna.cheshire.Recoverer() {
			public org.lgna.croquet.steps.Transaction createTransactionToGetCloserToTheRightStateWhenNoViewControllerCanBeFound( org.lgna.croquet.steps.Transaction transaction ) {
				return null;
			}
		};
		
		final boolean IS_OPTIMIZED_FOR_BUG_REPRO = false;
		final org.lgna.cheshire.stencil.StencilsPresentation presentation = new org.lgna.cheshire.stencil.StencilsPresentation( 
				UserInformation.INSTANCE, 
				
				//edu.cmu.cs.dennisc.croquet.guide.StepAccessPolicy.ALLOW_ACCESS_UP_TO_AND_INCLUDING_FURTHEST_COMPLETED_STEP,
				org.lgna.cheshire.ChapterAccessPolicy.ALLOW_ACCESS_TO_ALL_CHAPTERS,

				this.originalTransactionHistory, 
				MigrationManager.INSTANCE, 
				filterer,
				recoverer,
				
				new org.lgna.croquet.Group[] { edu.cmu.cs.dennisc.alice.Project.GROUP, org.alice.ide.IDE.UI_STATE_GROUP },
				
				org.lgna.stencil.DefaultScrollingRequiredRenderer.INSTANCE,

//				org.lgna.stencil.MenuPolicy.ABOVE_STENCIL_WITH_FEEDBACK
				org.lgna.stencil.MenuPolicy.ABOVE_STENCIL_WITHOUT_FEEDBACK
//				org.lgna.stencil.MenuPolicy.BELOW_STENCIL
		);
		//final org.lgna.cheshire.docwizardsesque.Presentation presentation = new org.lgna.cheshire.docwizardsesque.Presentation( UserInformation.INSTANCE, this.originalTransactionHistory, uist.filterers.NoOpFilterer.INSTANCE, recoverer, new edu.cmu.cs.dennisc.croquet.Group[] { edu.cmu.cs.dennisc.alice.Project.GROUP, org.alice.ide.IDE.UI_STATE_GROUP } );
		
		AstLiveRetargeter astLiveRetargeter = new AstLiveRetargeter();
		presentation.setRetargeter( astLiveRetargeter );

		presentation.setVisible( true );
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				edu.cmu.cs.dennisc.java.lang.ThreadUtilities.sleep( 1000 );
				TutorialECard.this.getFrame().setVisible( true );
			}
		} );
		
		
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
	protected void handleQuit( org.lgna.croquet.Trigger trigger ) {
//		super.handleQuit( e );
		if( IS_ENCODING ) {
			edu.cmu.cs.dennisc.codec.CodecUtilities.isDebugDesired = true;
			
			edu.cmu.cs.dennisc.codec.CodecUtilities.encodeBinary( org.lgna.croquet.steps.TransactionManager.getRootTransactionHistory(), ROOT_PATH + TRANSACTION_HISTORY_SUB_PATH );
			
//			System.err.println( "todo: remove filtering" );
//			edu.cmu.cs.dennisc.cheshire.Filter[] filters = {
//					edu.cmu.cs.dennisc.cheshire.MenuSelectionEventFilter.SINGLETON,
//			};
//			for( edu.cmu.cs.dennisc.cheshire.Filter filter : filters ) {
//				rootContext = filter.filter( rootContext );
//			}
//			
			edu.cmu.cs.dennisc.codec.CodecUtilities.isDebugDesired = false;
		}
		System.exit( 0 );
	}

	public static void main( final String[] args ) throws Exception {
		ROOT_PATH = args[ 5 ];
		IS_ENCODING = "2003".equals( args[ 4 ] );
		if( IS_ENCODING ) {
			org.alice.ide.croquet.models.ui.debug.IsTransactionHistoryShowingState.IS_SIDE_DOCKING_DESIRED = true;
			org.alice.ide.croquet.models.ui.debug.IsTransactionHistoryShowingState.getInstance().setValue( true );
		}
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final TutorialECard app = new TutorialECard();
				app.initialize( args );
				if( IS_ENCODING ) {
					app.getFrame().setVisible(true);
					edu.cmu.cs.dennisc.java.lang.ThreadUtilities.sleep( 500 );
					org.lgna.croquet.steps.TransactionManager.getRootTransactionHistory().EPIC_HACK_clear();
				} else {
					app.createAndShowTutorial();
				}
			}
		} );
	}
}
