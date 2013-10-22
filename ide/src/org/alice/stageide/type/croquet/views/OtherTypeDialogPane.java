/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.alice.stageide.type.croquet.views;

/**
 * @author Dennis Cosgrove
 */
public class OtherTypeDialogPane extends org.lgna.croquet.components.MigPanel {
	private final org.lgna.croquet.components.Tree<org.alice.stageide.type.croquet.TypeNode> treeView;
	private final org.lgna.croquet.views.HtmlView htmlView = new org.lgna.croquet.views.HtmlView();

	//todo: move to composite
	private final org.lgna.croquet.event.ValueListener<org.alice.stageide.type.croquet.TypeNode> valueListener = new org.lgna.croquet.event.ValueListener<org.alice.stageide.type.croquet.TypeNode>() {
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.alice.stageide.type.croquet.TypeNode> e ) {
			org.alice.stageide.type.croquet.TypeNode nextValue = e.getNextValue();
			handleTypeChange( nextValue != null ? nextValue.getType() : null );
		}
	};

	public OtherTypeDialogPane( org.alice.stageide.type.croquet.OtherTypeDialog composite ) {
		super( composite, "fill", "[grow 0, shrink0][grow 0, shrink0][grow, shrink]", "[grow 0, shrink 0][grow, shrink]" );

		org.lgna.croquet.ListSelectionState<org.alice.stageide.type.croquet.SelectionStyle> selectionStyleState = composite.getSelectionStyleState();

		this.treeView = composite.getTreeState().createTree();
		this.treeView.setCellRenderer( new org.alice.stageide.type.croquet.views.renderers.TypeCellRenderer() );
		org.lgna.croquet.components.ScrollPane scrollPane = new org.lgna.croquet.components.ScrollPane( this.treeView ) {
			@Override
			protected edu.cmu.cs.dennisc.javax.swing.components.JScrollPaneCoveringLinuxPaintBug createJScrollPane() {
				return new edu.cmu.cs.dennisc.javax.swing.components.VerticalScrollBarPaintOmittingWhenAppropriateJScrollPane();
			}
		};
		scrollPane.setVerticalScrollbarPolicy( org.lgna.croquet.components.ScrollPane.VerticalScrollbarPolicy.ALWAYS );
		this.addComponent( selectionStyleState.getItemSelectedState( org.alice.stageide.type.croquet.SelectionStyle.DIRECT ).createToggleButton() );
		this.addComponent( selectionStyleState.getItemSelectedState( org.alice.stageide.type.croquet.SelectionStyle.COMMON_ANCESTOR ).createToggleButton() );
		this.addComponent( new org.lgna.croquet.components.ScrollPane( this.htmlView ), "spany 2, grow, wrap" );
		this.addComponent( scrollPane, "grow" );

		org.lgna.croquet.views.MultipleSelectionListView<org.lgna.project.ast.UserField> listView = composite.getSceneFieldsState().createMultipleSelectionListView();
		listView.getAwtComponent().setEnabled( false );
		listView.getAwtComponent().setCellRenderer( new org.alice.stageide.type.croquet.views.renderers.FieldCellRenderer() );
		this.addComponent( listView, "grow" );
	}

	private static boolean isInclusionDesired( org.lgna.project.ast.AbstractMember member ) {
		if( member instanceof org.lgna.project.ast.AbstractMethod ) {
			org.lgna.project.ast.AbstractMethod method = (org.lgna.project.ast.AbstractMethod)member;
			if( method.isStatic() ) {
				return false;
			}
		} else if( member instanceof org.lgna.project.ast.AbstractField ) {
			org.lgna.project.ast.AbstractField field = (org.lgna.project.ast.AbstractField)member;
			if( field.isStatic() ) {
				return false;
			}
		}
		if( member.isPublicAccess() || member.isUserAuthored() ) {
			org.lgna.project.annotations.Visibility visibility = member.getVisibility();
			return ( visibility == null ) || visibility.equals( org.lgna.project.annotations.Visibility.PRIME_TIME );
		} else {
			return false;
		}
	}

	private static void appendMembers( StringBuilder sb, org.lgna.project.ast.AbstractType<?, ?, ?> type, boolean isSelected ) {
		if( isSelected ) {
			sb.append( "<h2>" );
		} else {
			sb.append( "<h2>" );
		}
		sb.append( "class " );
		sb.append( type.getName() );
		if( isSelected ) {
			sb.append( "</h2>" );
		} else {
			sb.append( " <em>(inherit)</em></h2>" );
		}

		java.util.ArrayList<? extends org.lgna.project.ast.AbstractMethod> methods = type.getDeclaredMethods();

		boolean isFirst = true;
		for( org.lgna.project.ast.AbstractMethod method : methods ) {
			if( isInclusionDesired( method ) ) {
				if( method.isProcedure() ) {
					if( isFirst ) {
						sb.append( "<em>procedures</em>" );
						sb.append( "<ul>" );
						isFirst = false;
					}
					sb.append( "<li>" );
					sb.append( method.getName() );
					sb.append( "</li>" );
				}
			}
		}
		if( isFirst ) {
			//pass
		} else {
			sb.append( "</ul>" );
		}
		isFirst = true;
		for( org.lgna.project.ast.AbstractMethod method : methods ) {
			if( isInclusionDesired( method ) ) {
				if( method.isFunction() ) {
					if( isFirst ) {
						sb.append( "<em>functions</em>" );
						sb.append( "<ul>" );
						isFirst = false;
					}
					sb.append( "<li>" );
					sb.append( method.getName() );
					sb.append( "</li>" );
				}
			}
		}
		if( isFirst ) {
			//pass
		} else {
			sb.append( "</ul>" );
		}

		isFirst = true;
		for( org.lgna.project.ast.AbstractField field : type.getDeclaredFields() ) {
			if( isInclusionDesired( field ) ) {
				if( isFirst ) {
					sb.append( "<em>properties</em>" );
					sb.append( "<ul>" );
					isFirst = false;
				}
				sb.append( "<li>" );
				sb.append( field.getName() );
				sb.append( "</li>" );
			}
		}
		if( isFirst ) {
			//pass
		} else {
			sb.append( "</ul>" );
		}

		if( type.isFollowToSuperClassDesired() ) {
			appendMembers( sb, type.getSuperType(), false );
		}
	}

	private void handleTypeChange( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "<html>" );
		if( type != null ) {
			appendMembers( sb, type, true );
		}
		sb.append( "</html>" );
		htmlView.setText( sb.toString() );
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				htmlView.getAwtComponent().scrollRectToVisible( new java.awt.Rectangle( 0, 0, 1, 1 ) );
			}
		} );
		org.alice.stageide.type.croquet.OtherTypeDialog composite = this.getComposite();

		org.lgna.croquet.MultipleSelectionState<org.lgna.project.ast.UserField> sceneFieldsState = composite.getSceneFieldsState();

		org.lgna.croquet.data.ListData<org.lgna.project.ast.UserField> data = sceneFieldsState.getData();

		org.lgna.croquet.MultipleSelectionState.SwingModel<org.lgna.project.ast.UserField> swingModel = sceneFieldsState.getSwingModel();
		swingModel.getListSelectionModel().clearSelection();
		if( type != null ) {
			synchronized( data ) {
				final int N = data.getItemCount();
				for( int i = 0; i < N; i++ ) {
					org.lgna.project.ast.UserField item = data.getItemAt( i );
					if( type.isAssignableFrom( item.getValueType() ) ) {
						swingModel.getListSelectionModel().addSelectionInterval( i, i );
					}
				}
			}
		}
	}

	@Override
	public org.alice.stageide.type.croquet.OtherTypeDialog getComposite() {
		return (org.alice.stageide.type.croquet.OtherTypeDialog)super.getComposite();
	}

	@Override
	public void handleCompositePreActivation() {
		org.alice.stageide.type.croquet.OtherTypeDialog composite = this.getComposite();
		composite.getTreeState().addNewSchoolValueListener( this.valueListener );
		this.treeView.expandAllRows();
		super.handleCompositePreActivation();
	}

	@Override
	public void handleCompositePostDeactivation() {
		org.alice.stageide.type.croquet.OtherTypeDialog composite = this.getComposite();
		composite.getTreeState().removeNewSchoolValueListener( this.valueListener );
		super.handleCompositePostDeactivation();
	}
}
