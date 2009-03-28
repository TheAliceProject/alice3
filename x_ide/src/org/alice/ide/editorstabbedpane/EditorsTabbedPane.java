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
package org.alice.ide.editorstabbedpane;

/**
 * @author Dennis Cosgrove
 */
public class EditorsTabbedPane extends zoot.ZTabbedPane implements org.alice.ide.event.IDEListener {
	public EditorsTabbedPane() {
		this.setTabCloseOperation( new org.alice.ide.operations.AbstractActionOperation() {
			public void perform( zoot.ActionContext actionContext ) {
				EditorsTabbedPane.this.remove( EditorsTabbedPane.this.getSelectedIndex() );
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: handle tab close" );
			}
		} );
		this.addChangeListener( new javax.swing.event.ChangeListener() {
			public void stateChanged( javax.swing.event.ChangeEvent e ) {
				org.alice.ide.codeeditor.CodeEditor codeEditor = getCodeEditorFor( EditorsTabbedPane.this.getSelectedComponent() );
				edu.cmu.cs.dennisc.alice.ast.AbstractCode nextFocusedCode;
				if( codeEditor != null ) {
					nextFocusedCode = codeEditor.getCode();
				} else {
					nextFocusedCode = null;
				}
				org.alice.ide.IDE.getSingleton().setFocusedCode( nextFocusedCode );
			}
		} );
	}

	private static org.alice.ide.codeeditor.CodeEditor getCodeEditorFor( java.awt.Component component ) {
		if( component instanceof org.alice.ide.codeeditor.CodeEditor ) {
			org.alice.ide.codeeditor.CodeEditor codeEditor = (org.alice.ide.codeeditor.CodeEditor)component;
			return codeEditor;
		} else {
			return null;
		}
	}
	private void edit( final edu.cmu.cs.dennisc.alice.ast.AbstractCode code ) {
		assert code != null;
		for( java.awt.Component component : this.getComponents() ) {
			org.alice.ide.codeeditor.CodeEditor codeEditor = getCodeEditorFor( component );
			if( codeEditor != null ) {
				if( codeEditor.getCode() == code ) {
					this.setSelectedComponent( component );
					return;
				}
			}
		}
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				org.alice.ide.codeeditor.CodeEditor codeEditor = new org.alice.ide.codeeditor.CodeEditor( code );
				EditorsTabbedPane.this.addTab( code.getName(), codeEditor );
				EditorsTabbedPane.this.setSelectedComponent( codeEditor );
			}
		} );
	}
//	@Override
//	public boolean isCloseButtonDesiredAt( int index ) {
//		return index > 0;
//	}

	public void fieldSelectionChanging( org.alice.ide.event.FieldSelectionEvent e ) {
	}
	public void fieldSelectionChanged( org.alice.ide.event.FieldSelectionEvent e ) {
	}

	public void localeChanging( org.alice.ide.event.LocaleEvent e ) {
	}
	public void localeChanged( org.alice.ide.event.LocaleEvent e ) {
	}

	public void focusedCodeChanging( org.alice.ide.event.FocusedCodeChangeEvent e ) {
	}
	public void focusedCodeChanged( org.alice.ide.event.FocusedCodeChangeEvent e ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractCode code = e.getNextValue();
		if( code != null ) {
			this.edit( e.getNextValue() );
		}
	}

	public void projectOpening( org.alice.ide.event.ProjectOpenEvent e ) {
		this.removeAll();
	}
	public void projectOpened( org.alice.ide.event.ProjectOpenEvent e ) {
	}

	public void transientSelectionChanging( org.alice.ide.event.TransientSelectionEvent e ) {
	}
	public void transientSelectionChanged( org.alice.ide.event.TransientSelectionEvent e ) {
	}
}
