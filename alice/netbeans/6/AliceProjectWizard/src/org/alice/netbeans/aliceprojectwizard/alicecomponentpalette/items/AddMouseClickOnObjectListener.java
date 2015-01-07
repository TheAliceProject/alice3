package org.alice.netbeans.aliceprojectwizard.alicecomponentpalette.items;

public class AddMouseClickOnObjectListener extends AbstractActiveEditorDrop {

	@Override
	protected String[] getImports() {
		return new String[]{
					"org.lgna.story.event.MouseClickOnObjectListener",
					"org.lgna.story.event.MouseClickOnObjectEvent"
				};
	}
}
