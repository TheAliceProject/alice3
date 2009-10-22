package edu.cmu.cs.dennisc.croquet.util;

public class LocaleDisplayNameListCellRenderer extends LocaleListCellRenderer {
	@Override
	protected java.lang.String getText(java.util.Locale value) {
		return value.getDisplayName();
	}
}
