package org.lgna.cheshire.test;

import org.lgna.croquet.BooleanState;

public class IsShowingHackTutorialState extends BooleanState {

	public IsShowingHackTutorialState() {
		super( org.lgna.croquet.Application.DOCUMENT_UI_GROUP, java.util.UUID.fromString( "e944ac7c-6184-426e-aaed-1f65c2bbf87b" ), false );

		this.addValueListener( new ValueListener<Boolean>() {
			public void changing( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			}
			public void changed( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
				final boolean value = nextValue;
				javax.swing.SwingUtilities.invokeLater( new Runnable() {
					public void run() {
						if ( value ) {
							final org.lgna.cheshire.test.TransactionHistoryGeneratorTest test = org.lgna.cheshire.test.TransactionHistoryGeneratorTest.getColorCrazyGenerator();
							org.alice.ide.IDE.getActiveInstance().loadProjectFrom( test.getProjectFile() );
							test.generate( org.alice.ide.IDE.getActiveInstance().getProject() );
							org.alice.ide.IDE.getActiveInstance().getSimplePresentation().initializePresentation(org.lgna.cheshire.simple.ChapterAccessPolicy.ALLOW_ACCESS_TO_ALL_CHAPTERS, test.getReuseTransactionHistory(), null, null, new org.lgna.cheshire.simple.Recoverer(), new org.lgna.croquet.Group[] { org.alice.ide.IDE.PROJECT_GROUP, org.alice.ide.IDE.DOCUMENT_UI_GROUP } );
							test.showTransactionHistory();
							org.alice.ide.croquet.models.ui.debug.ActiveTransactionHistoryComposite.getInstance().getBooleanState().setValue( true );
							org.alice.ide.IDE.getActiveInstance().getSimplePresentation().showStencilsPresentation();
						} else {
							org.alice.ide.IDE.getActiveInstance().getSimplePresentation().showStencilsPresentation();
						}
					}
				});
			}
		});
	}
}
