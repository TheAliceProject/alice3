/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.stageide.personeditor;

/**
 * @author Dennis Cosgrove
 */
abstract class AbstractList<E> extends zoot.ZList< E > {
	class ItemSelectionOperation extends org.alice.ide.operations.AbstractItemSelectionOperation<E> {
		public ItemSelectionOperation( javax.swing.ComboBoxModel comboBoxModel ) {
			super( comboBoxModel );
		}
		@Override
		protected void handleSelectionChange(E value) {
			AbstractList.this.handlePerformSelectionChange( value );
		}
		@Override
		public boolean isSignificant() {
			return true;
		}
	}

	public AbstractList( javax.swing.ComboBoxModel comboBoxModel ) {
		this.setItemSelectionOperation( new ItemSelectionOperation( comboBoxModel ) );
	}
	public void randomize() {
		final int N = this.getModel().getSize();
		int i;
		if( N > 0 ) {
			i = org.alice.random.RandomUtilities.nextIntegerFrom0ToNExclusive( N );
		} else {
			i = -1;
		}
		this.setSelectedIndex( i );
	}
	public E getSelectedTypedValue() {
		return (E)this.getSelectedValue();
	}
	protected abstract void handlePerformSelectionChange( E value );
}
