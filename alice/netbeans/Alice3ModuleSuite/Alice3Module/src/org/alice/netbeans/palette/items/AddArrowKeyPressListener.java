package org.alice.netbeans.palette.items;

public class AddArrowKeyPressListener extends AbstractActiveEditorDrop {
	@Override
	protected String[] getImports() {
		return new String[] {
			//"org.lgna.story.event.ArrowKeyPressListener",
			"org.lgna.story.event.ArrowKeyEvent"
		};
	}
}
