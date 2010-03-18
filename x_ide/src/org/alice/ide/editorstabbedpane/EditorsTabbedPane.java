/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.alice.ide.editorstabbedpane;

class EditorsTabbedPaneUI extends edu.cmu.cs.dennisc.zoot.plaf.TabbedPaneUI {
	private DeclarationsUIResource declarationsUIResource;
	private BackUIResource backUIResource;
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
		this.backUIResource = new BackUIResource( editorsTabbedPane );
	}
	@Override
	protected void installComponents() {
		super.installComponents();
		if( this.declarationsUIResource != null ) {
			this.tabPane.add( this.declarationsUIResource );
		}
		this.tabPane.add( this.backUIResource );
	}
	@Override
	protected void uninstallComponents() {
		if( this.declarationsUIResource != null ) {
			this.tabPane.remove( this.declarationsUIResource );
		}
		this.tabPane.remove( this.backUIResource );
		super.uninstallComponents();
	}
	@Override
	protected void installListeners() {
		super.installListeners();
		if( this.declarationsUIResource != null ) {
			this.declarationsUIResource.addComponentListener( this.componentAdapter );
		}
		this.backUIResource.addComponentListener( this.componentAdapter );
	}
	@Override
	protected void uninstallListeners() {
		if( this.declarationsUIResource != null ) {
			this.declarationsUIResource.removeComponentListener( this.componentAdapter );
		}
		this.backUIResource.removeComponentListener( this.componentAdapter );
		super.uninstallListeners();
	}
	private java.awt.Dimension prevDeclarationsSize = null;
	private void handleResized( java.awt.event.ComponentEvent e ) {
		boolean isRevalidateAndRepaintRequired = false;
		if( this.declarationsUIResource != null ) {
			java.awt.Dimension size = this.declarationsUIResource.getSize();
			if( size.equals( prevDeclarationsSize ) ) {
				//pass
			} else {
//				edu.cmu.cs.dennisc.print.PrintUtilities.println( e );
				this.prevDeclarationsSize = size;
			}
		}
		if( isRevalidateAndRepaintRequired ) {
			if( this.tabPane != null ) {
				this.tabPane.revalidate();
				this.tabPane.repaint();
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
		rv.right += this.backUIResource.getWidth();
		rv.right += 16;
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
					declarationsUIResource.setLocation( 4, 0 );
					declarationsUIResource.setSize( declarationsUIResource.getPreferredSize() );
					backUIResource.setSize( backUIResource.getPreferredSize() );
					backUIResource.setLocation( parent.getWidth()-backUIResource.getWidth()-4, 0 );
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
public class EditorsTabbedPane extends edu.cmu.cs.dennisc.zoot.ZTabbedPane implements org.alice.ide.event.IDEListener {
	class EditPreviousCodeOperation extends org.alice.ide.operations.AbstractActionOperation {
		public EditPreviousCodeOperation() {
			super( org.alice.ide.IDE.INTERFACE_GROUP );
			this.putValue( javax.swing.Action.NAME, "previous" );
		}
		public void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
			edits.peek();
		}
	}
	private EditPreviousCodeOperation editPreviousCodeOperation;

	public EditorsTabbedPane() {
		this.setTabCloseOperation( new org.alice.ide.operations.InconsequentialActionOperation() {
			@Override
			protected void performInternal(edu.cmu.cs.dennisc.zoot.ActionContext actionContext) {
				EditorsTabbedPane.this.remove( EditorsTabbedPane.this.getSelectedIndex() );
				EditorsTabbedPane.this.updateFocusedCode();
			}
		} );
		
//		this.addVetoableChangeListener( new java.beans.VetoableChangeListener() {
//			public void vetoableChange( java.beans.PropertyChangeEvent e ) throws java.beans.PropertyVetoException {
		this.addChangeListener( new javax.swing.event.ChangeListener() {
			public void stateChanged( javax.swing.event.ChangeEvent e ) {
				EditorsTabbedPane.this.updateFocusedCode();
			}
		} );
		java.awt.Font f = this.getFont();
		this.setFont( f.deriveFont( f.getSize2D() * 1.25f ) );
	}
	public EditPreviousCodeOperation getEditPreviousCodeOperation() {
		if( this.editPreviousCodeOperation != null ) {
			//pass
		} else {
			this.editPreviousCodeOperation = new EditPreviousCodeOperation();
		}
		return this.editPreviousCodeOperation;
	}
	@Override
	protected edu.cmu.cs.dennisc.zoot.plaf.TabbedPaneUI createTabbedPaneUI() {
		return new EditorsTabbedPaneUI( this );
	}
	
	private void updateFocusedCode() {
		org.alice.ide.codeeditor.CodeEditor codeEditor = getCodeEditorFor( this.getSelectedComponent() );
		edu.cmu.cs.dennisc.alice.ast.AbstractCode nextFocusedCode;
		if( codeEditor != null ) {
			nextFocusedCode = codeEditor.getCode();
		} else {
			nextFocusedCode = null;
		}
		org.alice.ide.IDE.getSingleton().setFocusedCode( nextFocusedCode );
	}

	private java.util.LinkedList< edu.cmu.cs.dennisc.alice.ast.AbstractCode > edits = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	
	private void updateBackOperationsEnabled() {
		boolean isEnabled = this.edits.size() > 1;
		this.editPreviousCodeOperation.setEnabled( isEnabled );
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
		this.edits.remove( code );
		this.edits.add( code );
		this.updateBackOperationsEnabled();
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
		this.edits.clear();
		this.updateBackOperationsEnabled();
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
	public void setOmittingThisFieldAccesses( boolean isOmittingThisFieldAccesses ) {
		for( java.awt.Component component : this.getComponents() ) {
			org.alice.ide.codeeditor.CodeEditor codeEditor = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( component, org.alice.ide.codeeditor.CodeEditor.class );
			if( codeEditor != null ) {
				codeEditor.refresh();
			}
		}
	}
	
}
