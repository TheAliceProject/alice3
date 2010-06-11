package org.alice.ide.croquet;

public class PopupMenuButton extends edu.cmu.cs.dennisc.croquet.AbstractButton<javax.swing.AbstractButton, edu.cmu.cs.dennisc.croquet./*AbstractPopupMenu*/Operation> {
	private edu.cmu.cs.dennisc.croquet.Component< ? > component;
	public PopupMenuButton( edu.cmu.cs.dennisc.croquet./*AbstractPopupMenu*/Operation model, edu.cmu.cs.dennisc.croquet.Component< ? > component ) {
		super( model );
		this.component = component;
	}
	@Override
	protected javax.swing.AbstractButton createAwtComponent() {
		javax.swing.AbstractButton rv = new javax.swing.AbstractButton() {
			@Override
			public java.awt.Dimension getMaximumSize() {
				return this.getPreferredSize();
			}
		};
		rv.setLayout(  new java.awt.BorderLayout() );
		rv.add( this.component.getAwtComponent(), java.awt.BorderLayout.CENTER );
		return rv;
	}
}
