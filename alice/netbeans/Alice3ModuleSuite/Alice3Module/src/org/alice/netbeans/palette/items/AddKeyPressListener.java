package org.alice.netbeans.palette.items;

public class AddKeyPressListener extends AbstractActiveEditorDrop {
	@Override
	protected String[] getImports() {
		return new String[] {
			//"org.lgna.story.event.KeyPressListener",
			"org.lgna.story.event.KeyEvent"
		};
	}
}
