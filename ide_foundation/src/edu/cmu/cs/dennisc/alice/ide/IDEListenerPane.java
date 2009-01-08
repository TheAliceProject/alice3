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
package edu.cmu.cs.dennisc.alice.ide;

// note: perhaps this class should be abstract, however i'd like the compiler to tell me when it is missing a method from the listener 
/**
 * @author Dennis Cosgrove
 */
public /*abstract*/ class IDEListenerPane extends javax.swing.JPanel implements edu.cmu.cs.dennisc.alice.ide.event.IDEListener {
	public IDEListenerPane() {
		getIDE().addIDEListener( IDEListenerPane.this );
	}
	
	protected IDE getIDE() {
		return IDE.getSingleton();
	}
	
	public void fieldSelectionChanging( edu.cmu.cs.dennisc.alice.ide.event.FieldSelectionEvent e ) {
	}
	public void fieldSelectionChanged( edu.cmu.cs.dennisc.alice.ide.event.FieldSelectionEvent e ) {
	}

	public void localeChanging( edu.cmu.cs.dennisc.alice.ide.event.LocaleEvent e ) {
	}
	public void localeChanged( edu.cmu.cs.dennisc.alice.ide.event.LocaleEvent e ) {
	}


	public void focusedCodeChanging( edu.cmu.cs.dennisc.alice.ide.event.FocusedCodeChangeEvent e ) {
	}
	public void focusedCodeChanged( edu.cmu.cs.dennisc.alice.ide.event.FocusedCodeChangeEvent e ) {
	}


	public void projectOpening( edu.cmu.cs.dennisc.alice.ide.event.ProjectOpenEvent e ) {
	}
	public void projectOpened( edu.cmu.cs.dennisc.alice.ide.event.ProjectOpenEvent e ) {
	}


	public void transientSelectionChanging( edu.cmu.cs.dennisc.alice.ide.event.TransientSelectionEvent e ) {
	}
	public void transientSelectionChanged( edu.cmu.cs.dennisc.alice.ide.event.TransientSelectionEvent e ) {
	}

	
}
