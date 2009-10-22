package edu.cmu.cs.dennisc.croquet.util;

public abstract class LocaleListCellRenderer extends edu.cmu.cs.dennisc.croquet.swing.ListCellRenderer<java.util.Locale> {
	protected abstract String getText( java.util.Locale value );
	@Override
	protected final javax.swing.JLabel getListCellRendererComponent(javax.swing.JLabel rv, javax.swing.JList list, java.util.Locale value, int index, boolean isSelected, boolean cellHasFocus) {
		if( value != null ) {
			rv.setText( this.getText( value ) );
		}
		return rv;
	}

}
