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

class EditorsTabbedPaneUI extends zoot.plaf.TabbedPaneUI {
	private DeclarationsUIResource declarationsUIResource;
	private java.awt.event.ComponentListener componentAdapter = new java.awt.event.ComponentListener() {
		public void componentShown( java.awt.event.ComponentEvent e ) {
		}
		public void componentHidden( java.awt.event.ComponentEvent e ) {
		}
		public void componentResized( java.awt.event.ComponentEvent e ) {
			EditorsTabbedPaneUI.this.handleResized( e );
		}
		public void componentMoved( java.awt.event.ComponentEvent e ) {
		}
	};
	public EditorsTabbedPaneUI( EditorsTabbedPane editorsTabbedPane ) {
		super( editorsTabbedPane );
		if( org.alice.ide.IDE.getSingleton().isEmphasizingClasses() ) {
			this.declarationsUIResource = new DeclarationsUIResource();
		}
	}
	@Override
	protected void installComponents() {
		super.installComponents();
		if( this.declarationsUIResource != null ) {
			this.tabPane.add( this.declarationsUIResource );
		}
	}
	@Override
	protected void uninstallComponents() {
		if( this.declarationsUIResource != null ) {
			this.tabPane.remove( this.declarationsUIResource );
		}
		super.uninstallComponents();
	}
	@Override
	protected void installListeners() {
		super.installListeners();
		if( this.declarationsUIResource != null ) {
			this.declarationsUIResource.addComponentListener( this.componentAdapter );
		}
	}
	@Override
	protected void uninstallListeners() {
		if( this.declarationsUIResource != null ) {
			this.declarationsUIResource.removeComponentListener( this.componentAdapter );
		}
		super.uninstallListeners();
	}
	private java.awt.Dimension prevSize = null;
	private void handleResized( java.awt.event.ComponentEvent e ) {
		if( this.declarationsUIResource != null ) {
			java.awt.Dimension size = this.declarationsUIResource.getSize();
			if( size.equals( prevSize ) ) {
				//pass
			} else {
//				edu.cmu.cs.dennisc.print.PrintUtilities.println( e );
				this.prevSize = size;
				if( this.tabPane != null ) {
					this.tabPane.revalidate();
					this.tabPane.repaint();
				}
			}
		}
	}
	@Override
	protected java.awt.Insets getTabAreaInsets( int tabPlacement ) {
		java.awt.Insets rv = super.getTabAreaInsets( tabPlacement );
		if( this.declarationsUIResource != null ) {
			rv.left += this.declarationsUIResource.getWidth();
			rv.left += 16;
		}
		return rv;
	}
	@Override
	protected int calculateMaxTabHeight( int tabPlacement ) {
		int rv = super.calculateMaxTabHeight( tabPlacement );
		if( this.declarationsUIResource != null ) {
			rv = Math.max( rv, this.declarationsUIResource.getHeight() );
		}
		return rv;
	}
	
//	@Override
//	protected int calculateTabAreaHeight( int tabPlacement, int horizRunCount, int maxTabHeight ) {
//		int rv = super.calculateTabAreaHeight( tabPlacement, horizRunCount, maxTabHeight );
//		org.alice.ide.declarationseditor.DeclarationsUIResource declarationsUIResource = org.alice.ide.IDE.getSingleton().getDeclarationsUIResource();
//		rv = Math.max( rv, declarationsUIResource.getHeight() );
//		return rv;
//	}	
	@Override
	protected java.awt.LayoutManager createLayoutManager() {
		if( this.declarationsUIResource != null ) {
			class LayoutManager extends TabbedPaneLayout {
				@Override
				public void layoutContainer( java.awt.Container parent ) {
					super.layoutContainer( parent );
					declarationsUIResource.setLocation( 0, 0 );
					declarationsUIResource.setSize( declarationsUIResource.getPreferredSize() );
				}
			}
			return new LayoutManager();
		} else {
			return super.createLayoutManager();
		}
	}
}
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
		
//		this.addVetoableChangeListener( new java.beans.VetoableChangeListener() {
//			public void vetoableChange( java.beans.PropertyChangeEvent e ) throws java.beans.PropertyVetoException {
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
		
		java.awt.Font f = this.getFont();
		this.setFont( f.deriveFont( f.getSize2D() * 1.25f ) );
	}
	@Override
	protected zoot.plaf.TabbedPaneUI createTabbedPaneUI() {
		return new EditorsTabbedPaneUI( this );
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

//		javax.swing.SwingUtilities.invokeLater( new Runnable() {
//			public void run() {
				final org.alice.ide.codeeditor.CodeEditor codeEditor = new org.alice.ide.codeeditor.CodeEditor( code );
				if( code instanceof edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice ) {
					edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)code;
					
					//todo: remove this property listener when tab is closed
					
					method.name.addPropertyListener( new edu.cmu.cs.dennisc.property.event.PropertyListener() {
						public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
						}
						public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
							int index = EditorsTabbedPane.this.indexOfComponent( codeEditor );
							if( index >= 0 ) {
								EditorsTabbedPane.this.setTitleAt( index, code.getName() );
							}
						}
					} );
				}
				EditorsTabbedPane.this.addTab( code.getName(), codeEditor );
				EditorsTabbedPane.this.setSelectedComponent( codeEditor );
//			}
//		} );
	}
	@Override
	public boolean isCloseButtonDesiredAt( int index ) {
		return index > 0 || org.alice.ide.IDE.getSingleton().isEmphasizingClasses();
	}

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
	public void setEmphasizingClasses( boolean isEmphasizingClasses ) {
		this.updateUI();
	}
}
