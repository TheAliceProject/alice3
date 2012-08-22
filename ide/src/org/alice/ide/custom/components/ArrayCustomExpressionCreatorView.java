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
package org.alice.ide.custom.components;

/**
 * @author Dennis Cosgrove
 */
public class ArrayCustomExpressionCreatorView extends RowBasedCustomExpressionCreatorView {
	private static abstract class ItemAtIndexCascade<T> extends org.lgna.croquet.CascadeWithInternalBlank<T> {
		private final org.lgna.croquet.ListSelectionState<T> state;
		private/* final */int index = -1;

		public ItemAtIndexCascade( java.util.UUID migrationId, org.lgna.croquet.ListSelectionState<T> state ) {
			super( state.getGroup(), migrationId, state.getItemClass() );
			this.state = state;
		}

		public org.lgna.croquet.ListSelectionState<T> getState() {
			return this.state;
		}

		public int getIndex() {
			return this.index;
		}

		public void setIndex( int index ) {
			this.index = index;
		}

		@Override
		protected org.lgna.croquet.edits.Edit<? extends org.lgna.croquet.Cascade<T>> createEdit( org.lgna.croquet.history.CompletionStep<org.lgna.croquet.Cascade<T>> completionStep, T[] values ) {
			T[] items = this.state.toArray( this.getComponentType() );
			items[ this.index ] = values[ 0 ];
			int selectedIndex = this.state.getSelectedIndex();
			this.state.setListData( selectedIndex, items );
			return null;
		}
	}

	private static class ExpressionAtIndexCascade extends ItemAtIndexCascade<org.lgna.project.ast.Expression> {
		private final org.lgna.project.ast.AbstractType<?, ?, ?> componentType;

		public ExpressionAtIndexCascade( org.lgna.croquet.ListSelectionState<org.lgna.project.ast.Expression> state, org.lgna.project.ast.AbstractType<?, ?, ?> componentType ) {
			super( java.util.UUID.fromString( "bbdd16fe-0ea0-41ae-8e09-fde5ee075e06" ), state );
			this.componentType = componentType;
		}

		@Override
		protected java.util.List<org.lgna.croquet.CascadeBlankChild> updateBlankChildren( java.util.List<org.lgna.croquet.CascadeBlankChild> rv, org.lgna.croquet.cascade.BlankNode<org.lgna.project.ast.Expression> blankNode ) {
			org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
			ide.getExpressionCascadeManager().appendItems( rv, blankNode, this.componentType, null );
			return rv;
		}

	}

	private static class ExpressionDropDown extends org.lgna.croquet.components.DropDown<org.lgna.croquet.PopupPrepModel> {
		private class MainComponent extends org.lgna.croquet.components.BorderPanel {
			private final org.alice.ide.x.AstI18nFactory factory;

			public MainComponent( org.alice.ide.x.AstI18nFactory factory ) {
				this.factory = factory;
			}

			@Override
			protected void internalRefresh() {
				super.internalRefresh();
				this.forgetAndRemoveAllComponents();
				org.lgna.project.ast.Expression expression = cascade.getState().getItemAt( cascade.getIndex() );
				this.addLineStartComponent( factory.createExpressionPane( expression ) );
				this.addCenterComponent( new org.lgna.croquet.components.Label() );
				this.revalidateAndRepaint();
			}
		};

		private final ExpressionAtIndexCascade cascade;
		private final MainComponent mainComponent;

		public ExpressionDropDown( ExpressionAtIndexCascade cascade, org.alice.ide.x.AstI18nFactory factory ) {
			super( cascade.getRoot().getPopupPrepModel() );
			this.cascade = cascade;
			this.mainComponent = new MainComponent( factory );
			this.setMainComponent( this.mainComponent );
			this.setMaximumSizeClampedToPreferredSize( true );
		}

		public ExpressionAtIndexCascade getCascade() {
			return this.cascade;
		}

		@Override
		protected javax.swing.Action getAction() {
			return this.getModel().getAction();
		}

		public void refreshInternalLater() {
			this.mainComponent.refreshLater();
		}
	};

	private static class ExpressionList extends org.lgna.croquet.components.MutableList<org.lgna.project.ast.Expression, org.lgna.croquet.components.Label, ExpressionDropDown, org.lgna.croquet.components.JComponent<?>> {
		private final org.lgna.project.ast.AbstractType<?, ?, ?> componentType;

		public ExpressionList( org.lgna.croquet.ListSelectionState<org.lgna.project.ast.Expression> state, org.lgna.croquet.PopupPrepModel popupPrepModel, org.lgna.project.ast.AbstractType<?, ?, ?> componentType ) {
			super( state, popupPrepModel );
			this.componentType = componentType;
			this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
		}

		@Override
		protected org.lgna.croquet.components.Label createLeadingComponent() {
			org.lgna.croquet.components.Label rv = new org.lgna.croquet.components.Label( "leading", 1.4f, edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD );
			rv.setVerticalAlignment( org.lgna.croquet.components.VerticalAlignment.CENTER );
			rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0, 0, 0, 8 ) );
			return rv;
		}

		@Override
		protected ExpressionDropDown createMainComponent() {
			ExpressionAtIndexCascade cascade = new ExpressionAtIndexCascade( this.getModel(), this.componentType );
			return new ExpressionDropDown( cascade, org.alice.ide.x.DialogAstI18nFactory.getInstance() );
		}

		@Override
		protected org.lgna.croquet.components.JComponent<?> createTrailingComponent() {
			return null;
		}

		@Override
		protected void update( org.lgna.croquet.components.Label leadingComponent, ExpressionDropDown mainComponent, org.lgna.croquet.components.JComponent<?> trailingComponent, int index, org.lgna.project.ast.Expression item ) {
			leadingComponent.setText( "[" + index + "]" );
			//org.lgna.project.ast.Expression expression = this.getModel().getItemAt( index );
			mainComponent.getCascade().setIndex( index );
			mainComponent.refreshInternalLater();
		}

		@Override
		protected void updateSelection( org.lgna.croquet.components.Label leadingComponent, ExpressionDropDown mainComponent, org.lgna.croquet.components.JComponent<?> trailingComponent, boolean isSelected ) {
			java.awt.Color color = isSelected ? java.awt.Color.WHITE : java.awt.Color.BLACK;
			leadingComponent.setForegroundColor( color );
			mainComponent.setForegroundColor( color );
		}

		@Override
		protected void handleDisplayable() {
			super.handleDisplayable();
			this.registerKeyboardActions();
		}

		@Override
		protected void handleUndisplayable() {
			this.unregisterKeyboardActions();
			super.handleUndisplayable();
		}
	}

	public ArrayCustomExpressionCreatorView( org.alice.ide.custom.ArrayCustomExpressionCreatorComposite composite ) {
		super( composite );
	}

	@Override
	protected void appendRows( java.util.List<org.lgna.croquet.components.SpringRow> rows ) {
		org.alice.ide.custom.ArrayCustomExpressionCreatorComposite composite = (org.alice.ide.custom.ArrayCustomExpressionCreatorComposite)this.getComposite();
		rows.add( new org.lgna.croquet.components.LabeledSpringRow( composite.getArrayTypeLabel(), new org.lgna.croquet.components.Label( org.alice.ide.common.TypeIcon.getInstance( composite.getArrayType() ) ) ) );
		org.lgna.croquet.components.ScrollPane scrollPane = new org.lgna.croquet.components.ScrollPane( new ExpressionList( composite.getValueState(), composite.getAddItemCascade().getRoot().getPopupPrepModel(), composite.getArrayType().getComponentType() ) );
		scrollPane.setBorder( null );
		rows.add( new org.lgna.croquet.components.LabeledSpringRow( composite.getValueLabel(), scrollPane, org.lgna.croquet.components.VerticalAlignment.TOP ) );
	}

}
