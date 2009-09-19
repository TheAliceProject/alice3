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
package zoot;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractBooleanStateOperation extends AbstractOperation implements BooleanStateOperation {
	private boolean prevValue;
	private boolean nextValue;
	private javax.swing.ButtonModel buttonModel = new javax.swing.JToggleButton.ToggleButtonModel();
	private javax.swing.Action actionForConfiguringSwingComponents = new javax.swing.AbstractAction() {
		public void actionPerformed( java.awt.event.ActionEvent e ) {
		}
	};
	public AbstractBooleanStateOperation( Boolean initialState ) {
		this.buttonModel.setSelected( initialState );
		this.buttonModel.addItemListener( new java.awt.event.ItemListener() {
			public void itemStateChanged( java.awt.event.ItemEvent e ) {
				boolean prev;
				boolean next;
				if( e.getStateChange() == java.awt.event.ItemEvent.SELECTED ) {
					prev = false;
					next = true;
				} else {
					prev = true;
					next = false;
				}
				ZManager.performIfAppropriate( AbstractBooleanStateOperation.this, e, ZManager.CANCEL_IS_FUTILE, prev, next );
			}
		} );
	}
	public Boolean getState() {
		return this.buttonModel.isSelected();
	}
	public javax.swing.ButtonModel getButtonModel() {
		return this.buttonModel;
	}
	public javax.swing.Action getActionForConfiguringSwing() {
		return this.actionForConfiguringSwingComponents;
	}
	protected void putValue( String key, Object value ) {
		this.actionForConfiguringSwingComponents.putValue( key, value );
	}

	public final void performStateChange(zoot.BooleanStateContext booleanStateContext) {
		this.prevValue = booleanStateContext.getPreviousValue();
		this.nextValue = booleanStateContext.getNextValue();
		booleanStateContext.commitAndInvokeRedoIfAppropriate();
	}
	protected abstract void handleStateChange( boolean value );
	@Override
	public final boolean canRedo() {
		return true;
	}
	@Override
	public final boolean canUndo() {
		return true;
	}
	@Override
	public final void redo() throws javax.swing.undo.CannotRedoException {
		this.buttonModel.setSelected( this.nextValue );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: replace w/ listeners" );
		this.handleStateChange( this.nextValue );
	}
	@Override
	public final void undo() throws javax.swing.undo.CannotUndoException {
		this.buttonModel.setSelected( this.prevValue );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: replace w/ listeners" );
		this.handleStateChange( this.prevValue );
	}
}
