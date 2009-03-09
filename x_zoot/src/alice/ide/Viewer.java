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
package alice.ide;

/**
 * @author Dennis Cosgrove
 */
public abstract class Viewer< E > extends Component {
	private E model;

	public E getModel() {
		return this.model;
	}
	public void setModel( E model ) {
		this.model = model;
	}
	
	public void fieldSelectionChanging( alice.ide.event.FieldSelectionEvent e ) {
	}
	public void fieldSelectionChanged( alice.ide.event.FieldSelectionEvent e ) {
	}

	public void localeChanging( alice.ide.event.LocaleEvent e ) {
	}
	public void localeChanged( alice.ide.event.LocaleEvent e ) {
	}


	public void focusedCodeChanging( alice.ide.event.FocusedCodeChangeEvent e ) {
	}
	public void focusedCodeChanged( alice.ide.event.FocusedCodeChangeEvent e ) {
	}


	public void projectOpening( alice.ide.event.ProjectOpenEvent e ) {
	}
	public void projectOpened( alice.ide.event.ProjectOpenEvent e ) {
	}


	public void transientSelectionChanging( alice.ide.event.TransientSelectionEvent e ) {
	}
	public void transientSelectionChanged( alice.ide.event.TransientSelectionEvent e ) {
	}
}
