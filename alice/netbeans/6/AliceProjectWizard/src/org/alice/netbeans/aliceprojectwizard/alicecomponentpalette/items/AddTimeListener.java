package org.alice.netbeans.aliceprojectwizard.alicecomponentpalette.items;

/**
 * @author Dennis Cosgrove
 */
public class AddTimeListener extends AbstractActiveEditorDrop {

	@Override
	protected String[] getImports() {
		return new String[]{
					"org.lgna.story.event.TimeListener",
					"org.lgna.story.event.TimeEvent"
				};
	}
}
