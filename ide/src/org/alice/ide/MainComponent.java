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

package org.alice.ide;

/**
 * @author Dennis Cosgrove
 */
public abstract class MainComponent extends org.lgna.croquet.components.BorderPanel {
	private int rootDividerLocation = 340;
	private int leftDividerLocation = 240;

	private final org.lgna.croquet.components.VerticalSplitPane left = new org.lgna.croquet.components.VerticalSplitPane();
	private final org.lgna.croquet.components.BorderPanel right = new org.lgna.croquet.components.BorderPanel();
	private final org.lgna.croquet.components.HorizontalSplitPane root = new org.lgna.croquet.components.HorizontalSplitPane( left, right );

	private final org.lgna.croquet.components.JComponent< ? > galleryBrowser;
	private final org.alice.ide.memberseditor.MembersEditor membersEditor;
	private final org.alice.ide.ubiquitouspane.UbiquitousPane ubiquitousPane;
	private final org.alice.ide.typehierarchyview.TypeHierarchyView typeHierarchyView;
	private final org.alice.ide.contextview.ContextView contextView;

	private final org.alice.ide.typeeditor.TypeEditor typeEditor = new org.alice.ide.typeeditor.TypeEditor();

	public MainComponent() {
		this.galleryBrowser = this.createClassGalleryBrowser(this.getClassGalleryRoot());
		this.membersEditor = this.createClassMembersEditor();
		this.ubiquitousPane = this.createUbiquitousPane();
		this.typeHierarchyView = new org.alice.ide.typehierarchyview.TypeHierarchyView();
		this.contextView = new org.alice.ide.contextview.ContextView( this.typeHierarchyView, this.membersEditor );

		final int MINIMUM_SIZE = 24;
		this.right.getAwtComponent().setMinimumSize( new java.awt.Dimension( MINIMUM_SIZE, MINIMUM_SIZE ) );
		this.left.getAwtComponent().setMinimumSize( new java.awt.Dimension( MINIMUM_SIZE, MINIMUM_SIZE ) );

		this.right.addComponent( this.typeEditor, org.lgna.croquet.components.BorderPanel.Constraint.CENTER );
		org.alice.ide.croquet.models.ui.IsSceneEditorExpandedState.getInstance().addAndInvokeValueObserver( new org.lgna.croquet.State.ValueObserver< Boolean >() {
			public void changing( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			}
			public void changed( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
				setSceneEditorExpanded( nextValue );
			}
		} );
		org.alice.ide.croquet.models.typeeditor.DeclarationTabState.getInstance().addAndInvokeValueObserver( new org.lgna.croquet.ListSelectionState.ValueObserver< org.alice.ide.croquet.models.typeeditor.DeclarationComposite >() {
			public void changing( org.lgna.croquet.State< org.alice.ide.croquet.models.typeeditor.DeclarationComposite > state, org.alice.ide.croquet.models.typeeditor.DeclarationComposite prevValue, org.alice.ide.croquet.models.typeeditor.DeclarationComposite nextValue, boolean isAdjusting ) {
			}
			public void changed( org.lgna.croquet.State< org.alice.ide.croquet.models.typeeditor.DeclarationComposite > state, org.alice.ide.croquet.models.typeeditor.DeclarationComposite prevValue, org.alice.ide.croquet.models.typeeditor.DeclarationComposite nextValue, boolean isAdjusting ) {
				refreshAccessibles();
			}
		} );
		
		this.addComponent( this.root, Constraint.CENTER );
	}
	
	public abstract edu.cmu.cs.dennisc.javax.swing.models.TreeNode<org.lgna.project.ast.JavaType> getClassGalleryRoot();
	protected abstract org.lgna.croquet.components.JComponent< ? > createClassGalleryBrowser( edu.cmu.cs.dennisc.javax.swing.models.TreeNode<org.lgna.project.ast.JavaType> root );
	protected org.alice.ide.memberseditor.MembersEditor createClassMembersEditor() {
		return new org.alice.ide.memberseditor.MembersEditor();
	}
	protected org.alice.ide.ubiquitouspane.UbiquitousPane createUbiquitousPane() {
		return new org.alice.ide.ubiquitouspane.UbiquitousPane();
	}

	public org.alice.ide.typeeditor.TypeEditor getTypeEditor() {
		return this.typeEditor;
	}
	public org.alice.ide.ubiquitouspane.UbiquitousPane getUbiquitousPane() {
		return this.ubiquitousPane;
	}
	public org.alice.ide.memberseditor.MembersEditor getMembersEditor() {
		return this.membersEditor;
	}
	public org.lgna.croquet.components.JComponent< ? > getGalleryBrowser() {
		return this.galleryBrowser;
	}
	public abstract org.alice.ide.sceneeditor.AbstractSceneEditor getSceneEditor();

	private java.util.Stack< ReasonToDisableSomeAmountOfRendering > reasonToDisableSomeAmountOfRenderingStack = edu.cmu.cs.dennisc.java.util.Collections.newStack();

	public void disableRendering( ReasonToDisableSomeAmountOfRendering reasonToDisableSomeAmountOfRendering ) {
		this.reasonToDisableSomeAmountOfRenderingStack.push( reasonToDisableSomeAmountOfRendering );
		if( reasonToDisableSomeAmountOfRendering == ReasonToDisableSomeAmountOfRendering.RUN_PROGRAM ) {
			//pass
		} else {
			this.root.setIgnoreRepaint( true );
			this.left.setIgnoreRepaint( true );
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( "ignore repaint: true" );
		}
		this.getSceneEditor().disableRendering( reasonToDisableSomeAmountOfRendering );
	}
	public void enableRendering() {
		if( reasonToDisableSomeAmountOfRenderingStack.size() > 0 ) {
			ReasonToDisableSomeAmountOfRendering reasonToDisableSomeAmountOfRendering = reasonToDisableSomeAmountOfRenderingStack.pop();
			if( reasonToDisableSomeAmountOfRendering == ReasonToDisableSomeAmountOfRendering.RUN_PROGRAM ) {
				//pass
			} else {
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "ignore repaint: false" );
				this.root.setIgnoreRepaint( false );
				this.left.setIgnoreRepaint( false );
			}
			this.getSceneEditor().enableRendering( reasonToDisableSomeAmountOfRendering );
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: investigate extra enableRendering" );
		}
	}

	//todo remove
	/*package-private*/ boolean isRespondingToRefreshAccessibles = true;
	public void refreshAccessibles() {
		this.typeHierarchyView.refresh();
//		if( isRespondingToRefreshAccessibles ) {
//			//edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: reduce visibility of refreshAccessibles" );
//
//			org.lgna.project.ast.AbstractCode code = this.getFocusedCode();
//			org.lgna.project.ast.Accessible accessible = org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance().getSelectedItem();
//
//			java.util.List< org.lgna.project.ast.Accessible > accessibles = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
//			if( this.rootField != null ) {
//				accessibles.add( this.rootField );
//				for( org.lgna.project.ast.AbstractField field : this.getRootTypeDeclaredInAlice().fields ) {
//					if( this.isAccessibleDesired( field ) ) {
//						accessibles.add( field );
//					}
//				}
//			}
//
//			int indexOfLastField = accessibles.size() - 1;
//			if( code instanceof org.lgna.project.ast.CodeDeclaredInAlice ) {
//				org.lgna.project.ast.CodeDeclaredInAlice codeDeclaredInAlice = (org.lgna.project.ast.CodeDeclaredInAlice)code;
//				for( org.lgna.project.ast.ParameterDeclaredInAlice parameter : codeDeclaredInAlice.getParamtersProperty() ) {
//					if( this.isAccessibleDesired( parameter ) ) {
//						accessibles.add( parameter );
//					}
//				}
//				for( org.lgna.project.ast.VariableDeclaredInAlice variable : IDE.getVariables( code ) ) {
//					if( this.isAccessibleDesired( variable ) ) {
//						accessibles.add( variable );
//					}
//				}
//				for( org.lgna.project.ast.ConstantDeclaredInAlice constant : IDE.getConstants( code ) ) {
//					if( this.isAccessibleDesired( constant ) ) {
//						accessibles.add( constant );
//					}
//				}
//			}
//
//			int selectedIndex;
//			if( accessible != null ) {
//				selectedIndex = accessibles.indexOf( accessible );
//			} else {
//				selectedIndex = -1;
//			}
//			if( selectedIndex == -1 ) {
//				if( code != null ) {
//					accessible = this.mapCodeToAccessible.get( code );
//					selectedIndex = accessibles.indexOf( accessible );
//				}
//			}
//			if( selectedIndex == -1 ) {
//				selectedIndex = indexOfLastField;
//			}
//			org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance().setListData( selectedIndex, accessibles );
//		}
	}
	private void setSceneEditorExpanded( boolean isSceneEditorExpanded ) {
		if( isSceneEditorExpanded ) {
			if( this.root.getAwtComponent().isValid() ) {
				this.rootDividerLocation = this.root.getDividerLocation();
			}
			if( this.left.getAwtComponent().isValid() ) {
				this.leftDividerLocation = this.left.getDividerLocation();
			}
			this.left.setResizeWeight( 1.0 );
			this.root.setLeftComponent( this.left );
			this.left.setTopComponent( this.getSceneEditor() );
			this.left.setBottomComponent( this.galleryBrowser );
			//this.root.setRightComponent( null );
			this.right.setVisible( false );
			this.root.setDividerSize( 0 );
			this.left.setDividerLocation( this.getHeight() - 256 );
		} else {
			this.left.setResizeWeight( 0.0 );
			this.root.setLeftComponent( this.left );
			this.right.setVisible( true );
			//this.root.setRightComponent( this.right );
			this.root.setDividerLocation( this.rootDividerLocation );
			this.left.setTopComponent( this.getSceneEditor() );
			this.left.setBottomComponent( this.contextView );
			this.left.setDividerLocation( this.leftDividerLocation );
			//			if( this.right.getComponentCount() == 0 ) {
			//				this.right.add( this.ubiquitousPane, java.awt.BorderLayout.SOUTH );
			//				this.right.add( this.editorsTabbedPane, java.awt.BorderLayout.CENTER );
			//				this.right.add( this.declarationsUIResource, java.awt.BorderLayout.NORTH );
			//			}
			this.root.setDividerSize( this.left.getDividerSize() );
		}
	}	
}
