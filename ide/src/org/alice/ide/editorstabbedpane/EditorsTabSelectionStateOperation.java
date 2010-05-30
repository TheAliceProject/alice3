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

import org.alice.ide.codeeditor.CodeEditor;

class Cycle< E > {
	private java.util.LinkedList< E > list = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	private int index = -1;
	
	public static <E> Cycle<E> newInstance() {
		return new Cycle<E>();
	}
	
	public void clear() {
		this.list.clear();
		this.index = -1;
	}
	
	public void add( E e ) {
		int prevIndex = this.list.indexOf( e );
		if( prevIndex != -1 ) {
			this.list.remove( prevIndex );
			if( prevIndex < this.index ) {
				this.index --;
			}
		}
		this.index ++;
		assert this.index <= this.size();
		this.list.add( this.index, e );
	}
	
	public E setToPrevious() {
		final int N = this.list.size();
		assert N > 0;
		this.index --;
		this.index += N;
		this.index %= N;
		return this.list.get( this.index );
	}
	
	public int size() {
		return this.list.size();
	}
}

//class DeclarationsUIResource extends edu.cmu.cs.dennisc.javax.swing.components.JLineAxisPane implements javax.swing.plaf.UIResource {
//	public DeclarationsUIResource() {
//		this.add( new DeclarationsDropDown().getJComponent() );
//		this.add( new org.alice.ide.common.ThisPane().getJComponent() );
//		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 2, 2, 0 ) );
//	}
//}
//
//class EditorsTabbedPaneUI extends edu.cmu.cs.dennisc.javax.swing.plaf.TabbedPaneUI {
//	private EditorsTabbedPane editorsTabbedPane;
//	private DeclarationsUIResource declarationsUIResource;
//	private BackUIResource backUIResource;
//	private java.awt.event.ComponentListener componentAdapter = new java.awt.event.ComponentListener() {
//		public void componentShown( java.awt.event.ComponentEvent e ) {
//		}
//		public void componentHidden( java.awt.event.ComponentEvent e ) {
//		}
//		public void componentResized( java.awt.event.ComponentEvent e ) {
//			EditorsTabbedPaneUI.this.handleResized( e );
//		}
//		public void componentMoved( java.awt.event.ComponentEvent e ) {
//		}
//	};
//	public EditorsTabbedPaneUI( EditorsTabbedPane editorsTabbedPane ) {
//		super( editorsTabbedPane.getContentAreaColor() );
//		this.editorsTabbedPane = editorsTabbedPane;
//		if( org.alice.ide.IDE.getSingleton().isEmphasizingClasses() ) {
//			this.declarationsUIResource = new DeclarationsUIResource();
//		}
//		this.backUIResource = new BackUIResource( editorsTabbedPane );
//	}
//
//	@Override
//	protected boolean isCloseButtonDesiredAt( int index ) {
//		return this.editorsTabbedPane.isCloseButtonDesiredAt( index );
//	}
//	@Override
//	protected void closeTab( int index, java.awt.event.MouseEvent e ) {
//		this.editorsTabbedPane.closeTab( index, e );
//	}
//
//	@Override
//	protected void installComponents() {
//		super.installComponents();
//		if( this.declarationsUIResource != null ) {
//			this.tabPane.add( this.declarationsUIResource );
//		}
//		this.tabPane.add( this.backUIResource );
//	}
//	@Override
//	protected void uninstallComponents() {
//		if( this.declarationsUIResource != null ) {
//			this.tabPane.remove( this.declarationsUIResource );
//		}
//		this.tabPane.remove( this.backUIResource );
//		super.uninstallComponents();
//	}
//	@Override
//	protected void installListeners() {
//		super.installListeners();
//		if( this.declarationsUIResource != null ) {
//			this.declarationsUIResource.addComponentListener( this.componentAdapter );
//		}
//		this.backUIResource.addComponentListener( this.componentAdapter );
//	}
//	@Override
//	protected void uninstallListeners() {
//		if( this.declarationsUIResource != null ) {
//			this.declarationsUIResource.removeComponentListener( this.componentAdapter );
//		}
//		this.backUIResource.removeComponentListener( this.componentAdapter );
//		super.uninstallListeners();
//	}
//	private java.awt.Dimension prevDeclarationsSize = null;
//	private java.awt.Dimension prevBackSize = null;
//	private void handleResized( java.awt.event.ComponentEvent e ) {
//		//todo:
//		boolean isRevalidateAndRepaintRequired = false;
//		if( this.declarationsUIResource != null ) {
//			java.awt.Dimension size = this.declarationsUIResource.getSize();
//			if( size.equals( prevDeclarationsSize ) ) {
//				//pass
//			} else {
//				isRevalidateAndRepaintRequired = true;
//				this.prevDeclarationsSize = size;
//			}
//		}
//		if( this.backUIResource != null ) {
//			java.awt.Dimension size = this.backUIResource.getSize();
//			if( size.equals( prevBackSize ) ) {
//				//pass
//			} else {
//				isRevalidateAndRepaintRequired = true;
//				this.prevBackSize = size;
//			}
//		}
//		if( isRevalidateAndRepaintRequired ) {
//			if( this.tabPane != null ) {
//				this.tabPane.revalidate();
//				this.tabPane.repaint();
//			}
//		}
//	}
//	@Override
//	protected java.awt.Insets getTabAreaInsets( int tabPlacement ) {
//		java.awt.Insets rv = super.getTabAreaInsets( tabPlacement );
//		if( this.declarationsUIResource != null ) {
//			rv.left += this.declarationsUIResource.getWidth();
//			rv.left += 16;
//		}
//		if( this.backUIResource.isButtonVisible() ) {
//			rv.right += this.backUIResource.getWidth();
//			rv.right += 16;
//		}
//		return rv;
//	}
//	@Override
//	protected int calculateMaxTabHeight( int tabPlacement ) {
//		int rv = super.calculateMaxTabHeight( tabPlacement );
//		if( this.declarationsUIResource != null ) {
//			rv = Math.max( rv, this.declarationsUIResource.getHeight() );
//		}
//		if( this.backUIResource.isButtonVisible() ) {
//			rv = Math.max( rv, this.backUIResource.getHeight() );
//		}
//		return rv;
//	}
//	
////	@Override
////	protected int calculateTabAreaHeight( int tabPlacement, int horizRunCount, int maxTabHeight ) {
////		int rv = super.calculateTabAreaHeight( tabPlacement, horizRunCount, maxTabHeight );
////		org.alice.ide.declarationseditor.DeclarationsUIResource declarationsUIResource = org.alice.ide.IDE.getSingleton().getDeclarationsUIResource();
////		rv = Math.max( rv, declarationsUIResource.getHeight() );
////		return rv;
////	}	
//	@Override
//	protected java.awt.LayoutManager createLayoutManager() {
//		class LayoutManager extends TabbedPaneLayout {
//			@Override
//			public void layoutContainer( java.awt.Container parent ) {
//				super.layoutContainer( parent );
//				if( declarationsUIResource != null ) {
//					declarationsUIResource.setSize( declarationsUIResource.getPreferredSize() );
//					declarationsUIResource.setLocation( 4, 0 );
//				}
//				if( backUIResource.isButtonVisible() ) {
//					backUIResource.setSize( backUIResource.getPreferredSize() );
//					backUIResource.setLocation( parent.getWidth()-backUIResource.getWidth()-4, 0 );
//				}
//			}
//		}
//		return new LayoutManager();
//	}
//}
/**
 * @author Dennis Cosgrove
 */
public class EditorsTabSelectionStateOperation extends edu.cmu.cs.dennisc.croquet.ItemSelectionOperation<edu.cmu.cs.dennisc.alice.ast.AbstractCode> {
	class EditPreviousCodeOperation extends org.alice.ide.operations.ActionOperation {
		public EditPreviousCodeOperation() {
			super( org.alice.app.ProjectApplication.IDE_GROUP, java.util.UUID.fromString( "71ff1171-9e5e-443f-a7aa-cb4012f05fec" ) );
			this.setName( "previous" );
		}
		@Override
		protected final void perform( edu.cmu.cs.dennisc.croquet.Context context, java.util.EventObject e, edu.cmu.cs.dennisc.croquet.Component<?> component ) {
			EditorsTabSelectionStateOperation.this.editPreviousCode();
		}
	}
	private EditPreviousCodeOperation editPreviousCodeOperation;
	@Override
	protected void encodeValue(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, edu.cmu.cs.dennisc.alice.ast.AbstractCode value) {
		throw new RuntimeException( "todo" );
	}

	@Override
	protected edu.cmu.cs.dennisc.alice.ast.AbstractCode decodeValue(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
		throw new RuntimeException( "todo" );
	}
	
//	private SelectionObserver selectionObserver = new SelectionObserver() {
//		public void selected(edu.cmu.cs.dennisc.croquet.TabStateOperation next) {
//			EditorsTabSelectionStateOperation.this.updateFocusedCode();
//		}
//	};
	public EditorsTabSelectionStateOperation() {
		super( org.alice.ide.IDE.IDE_GROUP, java.util.UUID.fromString( "846ef10d-b22b-44a7-8fdd-a6b5d459948d" ) );
//		this.addSelectionObserver( this.selectionObserver );
		org.alice.ide.IDE.getSingleton().addProjectObserver( this.projectObserver );
//		org.alice.ide.IDE.getSingleton().addCodeInFocusObserver( this.codeInFocusObserver );
	}
	private class EditorTabCreator implements TabCreator< edu.cmu.cs.dennisc.alice.ast.AbstractCode > {
		public java.util.UUID getId( edu.cmu.cs.dennisc.alice.ast.AbstractCode code ) {
			return code.getUUID();
		}
		public edu.cmu.cs.dennisc.croquet.ScrollPane createScrollPane( edu.cmu.cs.dennisc.alice.ast.AbstractCode code ) {
			edu.cmu.cs.dennisc.croquet.ScrollPane rv = new edu.cmu.cs.dennisc.croquet.ScrollPane();
			rv.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
			return rv;
		}
		public edu.cmu.cs.dennisc.croquet.JComponent< ? > createMainComponent( edu.cmu.cs.dennisc.alice.ast.AbstractCode code ) {
			return new CodeEditor( code );
		}
		public edu.cmu.cs.dennisc.croquet.JComponent< ? > createInnerTitleComponent( edu.cmu.cs.dennisc.alice.ast.AbstractCode code ) {
			return new edu.cmu.cs.dennisc.croquet.Label( code.getName() );
		}
		public boolean isCloseAffordanceDesired() {
			return true;
		}
	};
	private edu.cmu.cs.dennisc.croquet.FolderTabbedPane singleton;
	private EditorTabCreator editorTabCreator = new EditorTabCreator();
	public edu.cmu.cs.dennisc.croquet.FolderTabbedPane createEditorsFolderTabbedPane() {
		assert this.singleton == null;
		this.singleton = this.createFolderTabbedPane( this.editorTabCreator );
		return this.singleton;
	}
	public EditPreviousCodeOperation getEditPreviousCodeOperation() {
		if( this.editPreviousCodeOperation != null ) {
			//pass
		} else {
			this.editPreviousCodeOperation = new EditPreviousCodeOperation();
		}
		return this.editPreviousCodeOperation;
	}
	
//	private void updateFocusedCode() {
//		edu.cmu.cs.dennisc.croquet.Component< ? > component = this.getCurrentTabStateOperation().getSingletonView();
//		if( component instanceof org.alice.ide.codeeditor.CodeEditor ) {
//			org.alice.ide.codeeditor.CodeEditor codeEditor = (org.alice.ide.codeeditor.CodeEditor)component;
//			edu.cmu.cs.dennisc.alice.ast.AbstractCode nextFocusedCode;
//			if( codeEditor != null ) {
//				nextFocusedCode = codeEditor.getCode();
//			} else {
//				nextFocusedCode = null;
//			}
//			org.alice.ide.IDE.getSingleton().setFocusedCode( nextFocusedCode );
//		}
//	}

	private Cycle< edu.cmu.cs.dennisc.alice.ast.AbstractCode > editedCodes = Cycle.newInstance();
	private void editPreviousCode() {
		this.edit( this.editedCodes.setToPrevious(), true );
		
	}
	private void updateBackOperationsEnabled() {
		boolean isVisibleAndEnabled = this.editedCodes.size() > 1;
		this.getEditPreviousCodeOperation().setEnabled( isVisibleAndEnabled );
		this.getEditPreviousCodeOperation().setVisible( isVisibleAndEnabled );
	}
	
//	private static org.alice.ide.codeeditor.CodeEditor getCodeEditorFor( edu.cmu.cs.dennisc.croquet.Component< ? > component ) {
//		if( component instanceof org.alice.ide.codeeditor.CodeEditor ) {
//			org.alice.ide.codeeditor.CodeEditor codeEditor = (org.alice.ide.codeeditor.CodeEditor)component;
//			return codeEditor;
//		} else {
//			return null;
//		}
//	}
	@Deprecated
	public void edit( final edu.cmu.cs.dennisc.alice.ast.AbstractCode code, boolean isOriginatedByPreviousCodeOperation ) {
		if( this.containsItem( code ) ) {
			//pass
		} else {
			this.addItem( code );
		}
		this.setValue( code );
//		for( edu.cmu.cs.dennisc.croquet.TabStateOperation tabIsSelectedOperation : this.getTabStateOperations() ) {
//			edu.cmu.cs.dennisc.croquet.Component< ? > component = tabIsSelectedOperation.getSingletonView();
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( component.getClass() );
//			if( component instanceof org.alice.ide.codeeditor.CodeEditor ) {
//				org.alice.ide.codeeditor.CodeEditor codeEditor = (org.alice.ide.codeeditor.CodeEditor)component;
//				if( codeEditor.getCode() == code ) {
//					//tabIsSelectedOperation.setState( true );
//					//this.setCurrentTabStateOperation( tabIsSelectedOperation );
//					return;
//				}
//			}
//		}
//		class CodeTabIsSelectedOperation extends edu.cmu.cs.dennisc.croquet.TabStateOperation<edu.cmu.cs.dennisc.alice.ast.AbstractCode> {
//			public CodeTabIsSelectedOperation( edu.cmu.cs.dennisc.alice.ast.AbstractCode code ) {
//				super( org.alice.ide.IDE.IDE_GROUP,  java.util.UUID.fromString( "83fc2f34-a05f-48fd-941f-4e2ba08f45af" ), code );
//			}
//			@Override
//			protected String getTextFor( edu.cmu.cs.dennisc.alice.ast.AbstractCode code ) {
//				if( code != null ) {
//					if( code instanceof edu.cmu.cs.dennisc.alice.ast.AbstractConstructor ) {
//						return "constructor";
//					} else {
//						return code.getName();
//					}
//				} else {
//					return super.getTextFor( code );
//				}
//			}
//			@Override
//			protected boolean isCloseAffordanceDesired() {
//				return true;
//			}
////			@Override
////			public boolean isCloseButtonDesiredAt( int index ) {
////				return index > 0 || org.alice.ide.IDE.getSingleton().isEmphasizingClasses();
////			}
//			@Override
//			protected edu.cmu.cs.dennisc.croquet.JComponent<?> createSingletonView() {
//				final org.alice.ide.codeeditor.CodeEditor codeEditor = new org.alice.ide.codeeditor.CodeEditor( code );
//				if( code instanceof edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice ) {
//					edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)code;
//					
//					//todo: remove this property listener when tab is closed
//					
//					edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: listen to code name change" );
////					method.name.addPropertyListener( new edu.cmu.cs.dennisc.property.event.PropertyListener() {
////						public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
////						}
////						public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
////							CodeTabIsSelectedOperation.this.setTrueText( code.getName() );
////						}
////					} );
//				}
//				return codeEditor;
//			}
//		}
//		
//		CodeTabIsSelectedOperation codeTabIsSelectedOperation = new CodeTabIsSelectedOperation( code );
//		this.addTabStateOperation( codeTabIsSelectedOperation );
//		
//		this.setValue( code );
//		//codeTabIsSelectedOperation.setState( true );
//
//		if( isOriginatedByPreviousCodeOperation ) {
//			//pass
//		} else {
//			this.editedCodes.add( code );
//		}
//		this.updateBackOperationsEnabled();
	}

//	private org.alice.ide.IDE.CodeInFocusObserver codeInFocusObserver = new org.alice.ide.IDE.CodeInFocusObserver() {
//		public void focusedCodeChanging( edu.cmu.cs.dennisc.alice.ast.AbstractCode previousCode, edu.cmu.cs.dennisc.alice.ast.AbstractCode nextCode ) {
//		}
//		public void focusedCodeChanged( edu.cmu.cs.dennisc.alice.ast.AbstractCode previousCode, edu.cmu.cs.dennisc.alice.ast.AbstractCode nextCode ) {
//			if( nextCode != null ) {
//				EditorsTabSelectionStateOperation.this.edit( nextCode, false );
//			}
//		}
//	};

	private org.alice.app.ProjectApplication.ProjectObserver projectObserver = new org.alice.app.ProjectApplication.ProjectObserver() {
		public void projectOpening( edu.cmu.cs.dennisc.alice.Project previousProject, edu.cmu.cs.dennisc.alice.Project nextProject ) {
			EditorsTabSelectionStateOperation.this.clear();
			EditorsTabSelectionStateOperation.this.editedCodes.clear();
			EditorsTabSelectionStateOperation.this.updateBackOperationsEnabled();
		}
		public void projectOpened( edu.cmu.cs.dennisc.alice.Project previousProject, edu.cmu.cs.dennisc.alice.Project nextProject ) {
		}
	};
	public CodeEditor getCodeEditorInFocus() {
		return null;
	}

//	public void setEmphasizingClasses( boolean isEmphasizingClasses ) {
//		this.getSingletonSingleSelectionPane().getAwtComponent().updateUI();
//	}
//	public void setOmittingThisFieldAccesses( boolean isOmittingThisFieldAccesses ) {
//		for( edu.cmu.cs.dennisc.croquet.TabStateOperation tabIsSelectedOperation : this.getTabStateOperations() ) {
//			edu.cmu.cs.dennisc.croquet.Component< ? > component = tabIsSelectedOperation.getSingletonView();
//			if( component instanceof org.alice.ide.codeeditor.CodeEditor ) {
//				org.alice.ide.codeeditor.CodeEditor codeEditor = (org.alice.ide.codeeditor.CodeEditor)component;
//				codeEditor.refresh();
//			}
//		}
//	}
	
//	@Override
//	protected void adding() {
//		super.adding();
//		org.alice.ide.IDE.getSingleton().addCodeInFocusObserver( this.codeInFocusObserver );
//	}
//	@Override
//	protected void removed() {
//		org.alice.ide.IDE.getSingleton().removeCodeInFocusObserver( this.codeInFocusObserver );
//		super.removed();
//	}
}
