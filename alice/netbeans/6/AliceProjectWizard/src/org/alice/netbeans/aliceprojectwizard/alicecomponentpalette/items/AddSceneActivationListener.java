package org.alice.netbeans.aliceprojectwizard.alicecomponentpalette.items;

/**
 * @author Dennis Cosgrove
 */
public class AddSceneActivationListener extends AbstractActiveEditorDrop {

	@Override
	protected String[] getImports() {
		return new String[]{
					"org.lgna.story.event.SceneActivationListener",
					"org.lgna.story.event.SceneActivationEvent"
				};
	}
}
