package org.lgna.cheshire.test;

import org.lgna.croquet.BooleanState;

public class IsShowingHackTutorialState extends BooleanState {

	public IsShowingHackTutorialState() {
		super( org.lgna.croquet.Application.DOCUMENT_UI_GROUP, java.util.UUID.fromString( "e944ac7c-6184-426e-aaed-1f65c2bbf87b" ), false );

		this.addValueListener( new ValueListener<Boolean>() {
			public void changing( org.lgna.croquet.State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			}

			public void changed( org.lgna.croquet.State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
				final boolean value = nextValue;
				javax.swing.SwingUtilities.invokeLater( new Runnable() {
					public void run() {
						if( value ) {
							final org.lgna.cheshire.test.TransactionHistoryGeneratorTest test = org.lgna.cheshire.test.TransactionHistoryGeneratorTest.getColorCrazyGenerator();
							org.alice.ide.IDE.getActiveInstance().loadProjectFrom( test.getProjectFile() );
							test.generate( org.alice.ide.IDE.getActiveInstance().getProject() );

							org.lgna.cheshire.simple.stencil.SimplePresentation presentation = org.alice.ide.IDE.getActiveInstance().getSimplePresentation();

							presentation.initializePresentation( org.lgna.cheshire.simple.ChapterAccessPolicy.ALLOW_ACCESS_TO_ALL_CHAPTERS, test.getReuseTransactionHistory(), null, null, new org.lgna.cheshire.simple.Recoverer(), new org.lgna.croquet.Group[] { org.alice.ide.IDE.PROJECT_GROUP, org.alice.ide.IDE.DOCUMENT_UI_GROUP } );

							org.lgna.project.Project originalProject = test.getReuseProject();
							org.lgna.project.Project replacementProject = org.alice.ide.IDE.getActiveInstance().getProject();

							org.lgna.croquet.Retargeter retargeter = presentation.getRetargeter();

							java.util.Set<org.lgna.project.ast.NamedUserType> originalTypes = originalProject.getNamedUserTypes();
							java.util.Set<org.lgna.project.ast.NamedUserType> replacementTypes = replacementProject.getNamedUserTypes();

							for( org.lgna.project.ast.NamedUserType originalType : originalTypes ) {
								for( org.lgna.project.ast.NamedUserType replacementType : replacementTypes ) {
									if( originalType.getName().equals( replacementType.getName() ) ) {
										retargeter.addKeyValuePair( originalType, replacementType );
									}
								}
							}

							test.showTransactionHistory();
							org.alice.ide.croquet.models.ui.debug.ActiveTransactionHistoryComposite.getInstance().getBooleanState().setValueTransactionlessly( true );
							org.alice.ide.IDE.getActiveInstance().getSimplePresentation().showStencilsPresentation();
						} else {
							org.alice.ide.IDE.getActiveInstance().getSimplePresentation().hideStencilsPresentation();
						}
					}
				} );
			}
		} );

		this.initializeIfNecessary();
	}

	@Override
	protected void localize() {
		super.localize();
		//note: do not want to waste time localizing
		this.setTextForBothTrueAndFalse( "Show Hack Tutorial" );
		this.getSwingModel().getAction().putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_F3, 0 ) );
	}
}
