package org.alice.ide.editorstabbedpane;

public class DeclarationsUIResource extends swing.LineAxisPane implements javax.swing.plaf.UIResource {
	public DeclarationsUIResource() {
		this.add( new DeclarationsDropDown() );
		this.add( new org.alice.ide.common.ThisPane() );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 2, 2, 0 ) );
	}
}
