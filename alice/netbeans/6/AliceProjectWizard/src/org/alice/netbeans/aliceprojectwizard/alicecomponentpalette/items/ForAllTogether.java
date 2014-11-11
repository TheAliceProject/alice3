package org.alice.netbeans.aliceprojectwizard.alicecomponentpalette.items;

public class ForAllTogether extends AbstractActiveEditorDrop {

	@Override
	protected String[] getImports() {
		return new String[]{
					"org.lgna.common.EachInTogetherRunnable",
					"static org.lgna.common.ThreadUtilities.eachInTogether"
				};
	}
}
