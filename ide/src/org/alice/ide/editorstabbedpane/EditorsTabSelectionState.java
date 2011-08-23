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

import org.alice.ide.croquet.models.ast.TypeRootMenuModel;

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
//		if( org.alice.ide.IDE.getActiveInstance().isEmphasizingClasses() ) {
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
////		org.alice.ide.declarationseditor.DeclarationsUIResource declarationsUIResource = org.alice.ide.IDE.getActiveInstance().getDeclarationsUIResource();
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
public class EditorsTabSelectionState extends org.lgna.croquet.TabSelectionState<CodeComposite> {
	private static class SingletonHolder {
		private static EditorsTabSelectionState instance = new EditorsTabSelectionState();
	}
	public static EditorsTabSelectionState getInstance() {
		return SingletonHolder.instance;
	}
	private EditorsTabSelectionState() {
		super( org.alice.ide.IDE.UI_STATE_GROUP, java.util.UUID.fromString( "846ef10d-b22b-44a7-8fdd-a6b5d459948d" ), CodeCompositeCodec.SINGLETON );
//		this.addSelectionObserver( this.selectionObserver );
		org.alice.ide.IDE.getActiveInstance().addProjectObserver( this.projectObserver );
//		org.alice.ide.IDE.getActiveInstance().addCodeInFocusObserver( this.codeInFocusObserver );
	}
	private static final boolean IS_RUN_BUTTON_DESIRED = false;
	private class EditorTabCreator implements TabCreator< CodeComposite > {
		public java.util.UUID getId( CodeComposite item ) {
			return item.getCode().getUUID();
		}
		public org.lgna.croquet.components.ScrollPane createScrollPane( CodeComposite item ) {
			return null;
		}
		public org.lgna.croquet.components.JComponent< ? > createMainComponent( CodeComposite item ) {
			return org.alice.ide.codeeditor.CodeEditor.getInstance( item.getCode() );
		}
		
		private boolean isEntryPoint( edu.cmu.cs.dennisc.alice.ast.AbstractCode code ) {
			if( "run".equals( code.getName() ) ) { 
				return code.getDeclaringType() == org.alice.ide.IDE.getActiveInstance().getSceneType();
			}
			return false;
		}
		public void customizeTitleComponent( org.lgna.croquet.BooleanState booleanState, final org.lgna.croquet.components.AbstractButton< ?, org.lgna.croquet.BooleanState > button, CodeComposite item ) {
			booleanState.setTextForBothTrueAndFalse( getMenuText( item ) );
			button.getAwtComponent().setIcon( getMenuSmallIcon( item ) );
			button.scaleFont( 1.5f );
			
			edu.cmu.cs.dennisc.alice.ast.AbstractCode code = item.getCode();
			if( code instanceof edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice ) {
				final edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice methodDeclaredInAlice = (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)code;
				methodDeclaredInAlice.name.addPropertyListener( new edu.cmu.cs.dennisc.property.event.PropertyListener() {
					public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
					}
					public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
						button.getAwtComponent().setText( (String)e.getValue() );
					}
				} );
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: name listener" );
			}
			
			if( IS_RUN_BUTTON_DESIRED ) {
				if( isEntryPoint(code) ) {
					org.lgna.croquet.Operation<?> runOperation = org.alice.ide.IDE.getActiveInstance().getRunOperation();
					org.lgna.croquet.components.Button runButton = runOperation.createButton();
					edu.cmu.cs.dennisc.javax.swing.SpringUtilities.add( button.getAwtComponent(), runButton.getAwtComponent(), edu.cmu.cs.dennisc.javax.swing.SpringUtilities.Horizontal.EAST, -1, edu.cmu.cs.dennisc.javax.swing.SpringUtilities.Vertical.NORTH, 1 );
					runButton.getAwtComponent().setText( null );
					runButton.getAwtComponent().setToolTipText( runOperation.getName() );
				}
			}
		}
//		public org.lgna.croquet.JComponent< ? > createInnerTitleComponent( CodeComposite item ) {
//			org.lgna.croquet.Label rv = new org.lgna.croquet.Label( code.getName() );
//			rv.scaleFont( 1.5f );
//			return rv;
//		}
		public boolean isCloseable(CodeComposite item) {
			return isEntryPoint(item.getCode()) == false;
		}
	};
	private org.lgna.croquet.components.FolderTabbedPane<CodeComposite> singleton;
//	private DeclarationsDropDown declarationsDropDown = new DeclarationsDropDown();

	@Override
	protected String getMenuText(CodeComposite codeComposite) {
		edu.cmu.cs.dennisc.alice.ast.AbstractCode code = codeComposite.getCode();
		if( code != null ) {
			return code.getName();
		} else {
			return null;
		}
	}
	
	@Override
	protected javax.swing.Icon getMenuSmallIcon(CodeComposite codeComposite) {
		edu.cmu.cs.dennisc.alice.ast.AbstractCode code = codeComposite.getCode();
		if( code != null ) {
			return org.alice.stageide.gallerybrowser.ResourceManager.getSmallIconForType( code.getDeclaringType() );
		} else {
			return null; 
		}
	}
	
	@Override
	public org.lgna.croquet.components.TrackableShape getTrackableShapeFor(CodeComposite codeComposite) {
		final org.lgna.croquet.components.TrackableShape rv = super.getTrackableShapeFor(codeComposite);
		if( IS_RUN_BUTTON_DESIRED ) {
			if( rv instanceof org.lgna.croquet.components.JComponent<?> ) {
				org.lgna.croquet.components.JComponent<?> component = (org.lgna.croquet.components.JComponent<?>)rv;
				if( "run".equals( codeComposite.getCode().getName() ) ) {
					org.lgna.croquet.components.Button button = null;
					for( org.lgna.croquet.components.Component<?> child : component.getComponents() ) {
						if( child instanceof org.lgna.croquet.components.Button ) {
							button = (org.lgna.croquet.components.Button)child;
							break;
						}
					}
					if( button != null ) {
						final org.lgna.croquet.components.Button accessibleButton = button;
						return new org.lgna.croquet.components.TrackableShape() {
							public void addComponentListener(java.awt.event.ComponentListener listener) {
								rv.addComponentListener(listener);
							}
							public void addHierarchyBoundsListener(java.awt.event.HierarchyBoundsListener listener) {
								rv.addHierarchyBoundsListener(listener);
							}
							public void removeComponentListener(java.awt.event.ComponentListener listener) {
								rv.removeComponentListener(listener);
							}
							public void removeHierarchyBoundsListener(java.awt.event.HierarchyBoundsListener listener) {
								rv.addHierarchyBoundsListener(listener);
							}
							public org.lgna.croquet.components.ScrollPane getScrollPaneAncestor() {
								return rv.getScrollPaneAncestor();
							}
							public java.awt.Shape getShape(org.lgna.croquet.components.ScreenElement asSeenBy, java.awt.Insets insets) {
								java.awt.Rectangle rvRect = (java.awt.Rectangle)rv.getShape(asSeenBy, insets);
								java.awt.Rectangle buttonRect = (java.awt.Rectangle)accessibleButton.getShape(asSeenBy, null);
								rvRect.width = buttonRect.x-rvRect.x;
								if( insets != null ) {
									rvRect.width += insets.right;
								}
								return rvRect;
							}
							public java.awt.Shape getVisibleShape(org.lgna.croquet.components.ScreenElement asSeenBy, java.awt.Insets insets) {
								java.awt.Rectangle rvRect = (java.awt.Rectangle)rv.getVisibleShape(asSeenBy, insets);
								java.awt.Rectangle buttonRect = (java.awt.Rectangle)accessibleButton.getShape(asSeenBy, null);
								rvRect.width = buttonRect.x-rvRect.x;
								if( insets != null ) {
									rvRect.width += insets.right;
								}
								return rvRect;
							}
							public boolean isInView() {
								return rv.isInView();
							}
						};
					}
				}
			}
		}
		return rv;
	}
	private static final int UI_ROUND = 3;
	private static class AttentionGrabbingBorder implements javax.swing.border.Border {
		private java.awt.Insets insets;
		private java.awt.Stroke[] strokes = {
				new java.awt.BasicStroke( UI_ROUND + 10.0f ),
				new java.awt.BasicStroke( UI_ROUND + 8.0f ),
				new java.awt.BasicStroke( UI_ROUND + 6.0f ),
				new java.awt.BasicStroke( UI_ROUND + 4.0f ),
				new java.awt.BasicStroke( UI_ROUND + 2.0f ),
		};
		private final int alpha = 23;
		private java.awt.Paint[] paints = {
				new java.awt.Color( 255, 255, 0, alpha ),
				new java.awt.Color( 255, 255, 0, alpha ),
				new java.awt.Color( 255, 255, 0, alpha ),
				new java.awt.Color( 255, 255, 0, alpha ),
				new java.awt.Color( 0, 0, 0, 91 ),
		};
		private javax.swing.JButton button;
		public AttentionGrabbingBorder( javax.swing.JButton button ) {
			this.button = button;
			insets = new java.awt.Insets(5,5,5,5);
		}
		public java.awt.Insets getBorderInsets(java.awt.Component c) {
			return this.insets;
		}
		public boolean isBorderOpaque() {
			return false;
		}
		public void paintBorder(java.awt.Component c, java.awt.Graphics g, int x, int y, int width, int height) {
			if( this.button.hasFocus() ) {
				//pass
			} else {
				int xShape = x+this.insets.left+UI_ROUND;
				int yShape = y+this.insets.top+UI_ROUND;
				int widthShape = width-this.insets.left-this.insets.right-UI_ROUND-UI_ROUND; 
				int heightShape = height-this.insets.top-this.insets.bottom-UI_ROUND-UI_ROUND; 
				java.awt.Shape shape = new java.awt.geom.RoundRectangle2D.Float( xShape, yShape, widthShape, heightShape, UI_ROUND, UI_ROUND );
				
				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
				java.awt.Stroke prevStroke = g2.getStroke();
				try {
					final int N = this.strokes.length;
					for( int i=0; i<N; i++ ) {
						g2.setStroke( this.strokes[ i ] );
						g2.setPaint( this.paints[ i ] );
						g2.draw( shape );
					}
				} finally {
					g2.setStroke( prevStroke );
				}
			}
		}
	};
	private static class DropDownPanel extends org.lgna.croquet.components.BorderPanel {
		private javax.swing.ButtonModel buttonModel;
		public DropDownPanel() {
			org.lgna.croquet.components.PopupButton button = TypeRootMenuModel.getInstance().getPopupPrepModel().createPopupButton();
			button.getAwtComponent().setFocusable( false );
			this.buttonModel = button.getAwtComponent().getModel();
			button.setHorizontalTextPosition( org.lgna.croquet.components.HorizontalTextPosition.LEADING );
			this.addComponent( button, Constraint.CENTER );
			this.setBorder( new AttentionGrabbingBorder( button.getAwtComponent() ) );
			//button.setBorder( new AttentionGrabbingBorder( button.getAwtComponent() ) );
		}
		private org.lgna.croquet.ListSelectionState.ValueObserver<CodeComposite> selectionObserver = new org.lgna.croquet.ListSelectionState.ValueObserver<CodeComposite>() {
			public void changing( org.lgna.croquet.State< org.alice.ide.editorstabbedpane.CodeComposite > state, org.alice.ide.editorstabbedpane.CodeComposite prevValue, org.alice.ide.editorstabbedpane.CodeComposite nextValue, boolean isAdjusting ) {
			}
			public void changed( org.lgna.croquet.State< org.alice.ide.editorstabbedpane.CodeComposite > state, org.alice.ide.editorstabbedpane.CodeComposite prevValue, org.alice.ide.editorstabbedpane.CodeComposite nextValue, boolean isAdjusting ) {
				DropDownPanel.this.updateOperation( nextValue );
			}
		};
		private org.lgna.croquet.State.ValueObserver<Boolean> isEmphasizingClassesObserver = new org.lgna.croquet.State.ValueObserver<Boolean>() {
			public void changing( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			}
			public void changed( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
				DropDownPanel.this.setVisible( nextValue );
			}
		};
		@Override
		protected void handleDisplayable() {
			super.handleDisplayable();
			org.alice.ide.editorstabbedpane.EditorsTabSelectionState.getInstance().addAndInvokeValueObserver( this.selectionObserver );
			org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().addAndInvokeValueObserver( this.isEmphasizingClassesObserver );
		}
		@Override
		protected void handleUndisplayable() {
			org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().removeValueObserver( this.isEmphasizingClassesObserver );
			org.alice.ide.editorstabbedpane.EditorsTabSelectionState.getInstance().removeValueObserver( this.selectionObserver );
			super.handleUndisplayable();
		}
		private void updateOperation( CodeComposite item ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type;
			if( item != null ) {
				type = item.getCode().getDeclaringType();
			} else {
				type = null;
			}
			if( type != null ) {
				final boolean IS_ICON_DESIRED = true;
				if( IS_ICON_DESIRED ) {
					TypeRootMenuModel.getInstance().getPopupPrepModel().getAction().putValue( javax.swing.Action.NAME, "class:" );
					TypeRootMenuModel.getInstance().getPopupPrepModel().getAction().putValue( javax.swing.Action.SMALL_ICON, new org.alice.ide.common.TypeDropDownIcon( type, this.buttonModel ) );
				} else {
					TypeRootMenuModel.getInstance().getPopupPrepModel().getAction().putValue( javax.swing.Action.NAME, "class: " + type.getName() );
				}
			}
			this.revalidateAndRepaint();
		}
	}
	private EditorTabCreator editorTabCreator = new EditorTabCreator();
	private DropDownPanel dropDownPanel = new DropDownPanel();
	private org.alice.ide.clipboard.Clipboard clipboard = org.alice.ide.clipboard.Clipboard.getInstance();
	public org.lgna.croquet.components.FolderTabbedPane<CodeComposite> createEditorsFolderTabbedPane() {
		assert this.singleton == null;
		this.clipboard.setBorder( javax.swing.BorderFactory.createEmptyBorder( 2,2,2,2 ) );
		this.singleton = this.createFolderTabbedPane( this.editorTabCreator );
		this.singleton.setHeaderLeadingComponent( this.dropDownPanel );
		this.singleton.setHeaderTrailingComponent( this.clipboard );
		return this.singleton;
	}
//	private void updateFocusedCode() {
//		org.lgna.croquet.Component< ? > component = this.getCurrentTabStateOperation().getSingletonView();
//		if( component instanceof org.alice.ide.codeeditor.CodeEditor ) {
//			org.alice.ide.codeeditor.CodeEditor codeEditor = (org.alice.ide.codeeditor.CodeEditor)component;
//			edu.cmu.cs.dennisc.alice.ast.AbstractCode nextFocusedCode;
//			if( codeEditor != null ) {
//				nextFocusedCode = codeEditor.getCode();
//			} else {
//				nextFocusedCode = null;
//			}
//			org.alice.ide.IDE.getActiveInstance().setFocusedCode( nextFocusedCode );
//		}
//	}

	private Cycle< edu.cmu.cs.dennisc.alice.ast.AbstractCode > editedCodes = Cycle.newInstance();
	/*package-private*/ void editPreviousCode() {
		this.edit( this.editedCodes.setToPrevious(), true );
		
	}
	private void updateBackOperationsEnabled() {
		boolean isVisibleAndEnabled = this.editedCodes.size() > 1;
		EditPreviousCodeOperation.getInstance().setEnabled( isVisibleAndEnabled );
		//this.getEditPreviousCodeOperation().setVisible( isVisibleAndEnabled );
	}
	
//	private static org.alice.ide.codeeditor.CodeEditor getCodeEditorFor( org.lgna.croquet.Component< ? > component ) {
//		if( component instanceof org.alice.ide.codeeditor.CodeEditor ) {
//			org.alice.ide.codeeditor.CodeEditor codeEditor = (org.alice.ide.codeeditor.CodeEditor)component;
//			return codeEditor;
//		} else {
//			return null;
//		}
//	}
//	private class TypeListener  {
//		private edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice<?> type;
//		public TypeListener( edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice<?> type ) {
//			this.type = type;
//		}
//	}
	private void removeDeadCode() {
		for( CodeComposite item : this.toArray() ) {
			if( item.getCode().getDeclaringType() != null ) {
				//pass
			} else {
				this.removeItem( item );
			}
		}
	}
	private edu.cmu.cs.dennisc.property.event.ListPropertyListener<edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice> typeMembersListener = new edu.cmu.cs.dennisc.property.event.ListPropertyListener<edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice>() {
		public void adding(edu.cmu.cs.dennisc.property.event.AddListPropertyEvent<edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice> e) {
		}
		public void added(edu.cmu.cs.dennisc.property.event.AddListPropertyEvent<edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice> e) {
		}
		public void clearing(edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent<edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice> e) {
		}
		public void cleared(edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent<edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice> e) {
			EditorsTabSelectionState.this.removeDeadCode();
		}
		public void removing(edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent<edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice> e) {
		}
		public void removed(edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent<edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice> e) {
			EditorsTabSelectionState.this.removeDeadCode();
		}
		public void setting(edu.cmu.cs.dennisc.property.event.SetListPropertyEvent<edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice> e) {
		}
		public void set(edu.cmu.cs.dennisc.property.event.SetListPropertyEvent<edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice> e) {
			EditorsTabSelectionState.this.removeDeadCode();
		}
	};
	private java.util.Set<edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice<?>> typeSet = edu.cmu.cs.dennisc.java.util.Collections.newHashSet();
	private void startListeningTo( edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice<?> type ) {
	}

	@Override
	protected void handleItemAdded( CodeComposite item ) {
		super.handleItemAdded( item );
		edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> declaringType = item.getCode().getDeclaringType();
		if (declaringType instanceof edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice<?>) {
			edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice<?> typeInAlice = (edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice<?>) declaringType;
			if( this.typeSet.contains( typeInAlice ) ) {
				//pass
			} else {
				this.typeSet.add( typeInAlice );
				typeInAlice.methods.addListPropertyListener( this.typeMembersListener );
			}
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "investigate: declaringType==null" );
		}
	}
	
	//todo: handleItemRemoved
	@Override
	protected void handleItemRemoved( org.alice.ide.editorstabbedpane.CodeComposite item ) {
		super.handleItemRemoved( item );
		this.setSelectedIndex( this.getItemCount()-1 );
	}
	
	@Override
	public void clear() {
		super.clear();
		for( edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice<?> type : this.typeSet ) {
			type.methods.removeListPropertyListener( this.typeMembersListener );
		}
		this.typeSet.clear();
	}
	@Deprecated
	public void edit( final edu.cmu.cs.dennisc.alice.ast.AbstractCode code, boolean isOriginatedByPreviousCodeOperation ) {
		if( code != null ) {
			CodeComposite item = CodeComposite.getInstance( code );
			if( this.containsItem( item ) ) {
				//pass
			} else {
				this.addItem( item );
				edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> declaringType = code.getDeclaringType();
				if( declaringType != null ) {
					if (declaringType instanceof edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice<?>) {
						edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice<?> typeInAlice = (edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice<?>) declaringType;
						this.startListeningTo( typeInAlice );
					}
				} else {
					edu.cmu.cs.dennisc.print.PrintUtilities.println( "investigate: declaringType==null" );
				}
			}
			this.setSelectedItem( item );
//			for( org.lgna.croquet.TabStateOperation tabIsSelectedOperation : this.getTabStateOperations() ) {
//				org.lgna.croquet.Component< ? > component = tabIsSelectedOperation.getSingletonView();
//				edu.cmu.cs.dennisc.print.PrintUtilities.println( component.getClass() );
//				if( component instanceof org.alice.ide.codeeditor.CodeEditor ) {
//					org.alice.ide.codeeditor.CodeEditor codeEditor = (org.alice.ide.codeeditor.CodeEditor)component;
//					if( codeEditor.getCode() == code ) {
//						//tabIsSelectedOperation.setState( true );
//						//this.setCurrentTabStateOperation( tabIsSelectedOperation );
//						return;
//					}
//				}
//			}
//			class CodeTabIsSelectedOperation extends org.lgna.croquet.TabStateOperation<edu.cmu.cs.dennisc.alice.ast.AbstractCode> {
//				public CodeTabIsSelectedOperation( edu.cmu.cs.dennisc.alice.ast.AbstractCode code ) {
//					super( org.alice.ide.IDE.IDE_GROUP,  java.util.UUID.fromString( "83fc2f34-a05f-48fd-941f-4e2ba08f45af" ), code );
//				}
//				@Override
//				protected String getTextFor( edu.cmu.cs.dennisc.alice.ast.AbstractCode code ) {
//					if( code != null ) {
//						if( code instanceof edu.cmu.cs.dennisc.alice.ast.AbstractConstructor ) {
//							return "constructor";
//						} else {
//							return code.getName();
//						}
//					} else {
//						return super.getTextFor( code );
//					}
//				}
//				@Override
//				protected boolean isCloseAffordanceDesired() {
//					return true;
//				}
////				@Override
////				public boolean isCloseButtonDesiredAt( int index ) {
////					return index > 0 || org.alice.ide.IDE.getActiveInstance().isEmphasizingClasses();
////				}
//				@Override
//				protected org.lgna.croquet.JComponent<?> createSingletonView() {
//					final org.alice.ide.codeeditor.CodeEditor codeEditor = new org.alice.ide.codeeditor.CodeEditor( code );
//					if( code instanceof edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice ) {
//						edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)code;
//						
//						//todo: remove this property listener when tab is closed
//						
//						edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: listen to code name change" );
////						method.name.addPropertyListener( new edu.cmu.cs.dennisc.property.event.PropertyListener() {
////							public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
////							}
////							public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
////								CodeTabIsSelectedOperation.this.setTrueText( code.getName() );
////							}
////						} );
//					}
//					return codeEditor;
//				}
//			}
//			
//			CodeTabIsSelectedOperation codeTabIsSelectedOperation = new CodeTabIsSelectedOperation( code );
//			this.addTabStateOperation( codeTabIsSelectedOperation );
//			
//			this.setValue( code );
//			//codeTabIsSelectedOperation.setState( true );
	//
//			if( isOriginatedByPreviousCodeOperation ) {
//				//pass
//			} else {
//				this.editedCodes.add( code );
//			}
//			this.updateBackOperationsEnabled();
		}
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

	private org.alice.ide.ProjectApplication.ProjectObserver projectObserver = new org.alice.ide.ProjectApplication.ProjectObserver() {
		public void projectOpening( edu.cmu.cs.dennisc.alice.Project previousProject, edu.cmu.cs.dennisc.alice.Project nextProject ) {
			EditorsTabSelectionState.this.clear();
			EditorsTabSelectionState.this.editedCodes.clear();
			EditorsTabSelectionState.this.updateBackOperationsEnabled();
		}
		public void projectOpened( edu.cmu.cs.dennisc.alice.Project previousProject, edu.cmu.cs.dennisc.alice.Project nextProject ) {
		}
	};
	public org.alice.ide.codeeditor.CodeEditor getCodeEditorInFocus() {
		CodeComposite item = this.getSelectedItem();
		if( item != null ) {
			return (org.alice.ide.codeeditor.CodeEditor)this.getMainComponentFor( item );
		} else {
			return null;
		}
	}

//	public void setEmphasizingClasses( boolean isEmphasizingClasses ) {
//		this.getSingletonSingleSelectionPane().getAwtComponent().updateUI();
//	}
//	public void setOmittingThisFieldAccesses( boolean isOmittingThisFieldAccesses ) {
//		for( org.lgna.croquet.TabStateOperation tabIsSelectedOperation : this.getTabStateOperations() ) {
//			org.lgna.croquet.Component< ? > component = tabIsSelectedOperation.getSingletonView();
//			if( component instanceof org.alice.ide.codeeditor.CodeEditor ) {
//				org.alice.ide.codeeditor.CodeEditor codeEditor = (org.alice.ide.codeeditor.CodeEditor)component;
//				codeEditor.refresh();
//			}
//		}
//	}
	
//	@Override
//	protected void adding() {
//		super.adding();
//		org.alice.ide.IDE.getActiveInstance().addCodeInFocusObserver( this.codeInFocusObserver );
//	}
//	@Override
//	protected void removed() {
//		org.alice.ide.IDE.getActiveInstance().removeCodeInFocusObserver( this.codeInFocusObserver );
//		super.removed();
//	}
}
