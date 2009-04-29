package org.alice.ide.declarationseditor;

public class DeclarationsUIResource extends swing.Pane implements javax.swing.plaf.UIResource {
	public DeclarationsUIResource() {
		this.add( new DeclarationsDropDown() );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 2, 0, 2, 0 ) );
	}
}
