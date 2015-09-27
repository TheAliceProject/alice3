/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.ide.custom;

import org.lgna.project.ast.Expression;

/**
 * @author Dennis Cosgrove
 */
public class ArrayCustomExpressionCreatorComposite extends CustomExpressionCreatorComposite<org.alice.ide.custom.components.ArrayCustomExpressionCreatorView> {
	private static java.util.Map<org.lgna.project.ast.AbstractType<?, ?, ?>, ArrayCustomExpressionCreatorComposite> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	public static ArrayCustomExpressionCreatorComposite getInstance( org.lgna.project.ast.AbstractType<?, ?, ?> arrayType ) {
		synchronized( map ) {
			ArrayCustomExpressionCreatorComposite rv = map.get( arrayType );
			if( rv != null ) {
				//pass
			} else {
				rv = new ArrayCustomExpressionCreatorComposite( arrayType );
				map.put( arrayType, rv );
			}
			return rv;
		}
	}

	private final org.lgna.croquet.PlainStringValue arrayTypeLabel = this.createStringValue( "arrayTypeLabel" );
	private final org.lgna.project.ast.AbstractType<?, ?, ?> arrayType;

	private final org.lgna.croquet.data.MutableListData<org.lgna.project.ast.Expression> data = new org.lgna.croquet.data.MutableListData<Expression>( org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.Expression.class ) );

	private final org.lgna.croquet.Cascade<org.lgna.project.ast.Expression> addItemCascade = this.createCascadeWithInternalBlank( "addItemCascade", org.lgna.project.ast.Expression.class, new CascadeCustomizer<org.lgna.project.ast.Expression>() {
		@Override
		public void appendBlankChildren( java.util.List<org.lgna.croquet.CascadeBlankChild> rv, org.lgna.croquet.imp.cascade.BlankNode<org.lgna.project.ast.Expression> blankNode ) {
			org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
			ide.getExpressionCascadeManager().appendItems( rv, blankNode, arrayType.getComponentType(), null );
		}

		@Override
		public org.lgna.croquet.edits.Edit createEdit( org.lgna.croquet.history.CompletionStep completionStep, org.lgna.project.ast.Expression[] values ) {
			assert values.length == 1;
			data.internalAddItem( values[ 0 ] );
			getView().updatePreview();
			return null;
		}
	} );

	private ArrayCustomExpressionCreatorComposite( org.lgna.project.ast.AbstractType<?, ?, ?> arrayType ) {
		super( java.util.UUID.fromString( "187d56c4-cc05-4157-a5fc-55943ca5b099" ) );
		assert arrayType.isArray() : arrayType;
		this.arrayType = arrayType;
	}

	public org.lgna.project.ast.AbstractType<?, ?, ?> getArrayType() {
		return this.arrayType;
	}

	public org.lgna.croquet.PlainStringValue getArrayTypeLabel() {
		return this.arrayTypeLabel;
	}

	public org.lgna.croquet.data.MutableListData<org.lgna.project.ast.Expression> getData() {
		return this.data;
	}

	public org.lgna.croquet.Cascade<org.lgna.project.ast.Expression> getAddItemCascade() {
		return this.addItemCascade;
	}

	@Override
	protected org.alice.ide.custom.components.ArrayCustomExpressionCreatorView createView() {
		return new org.alice.ide.custom.components.ArrayCustomExpressionCreatorView( this );
	}

	@Override
	protected org.lgna.project.ast.Expression createValue() {
		return org.lgna.project.ast.AstUtilities.createArrayInstanceCreation( this.arrayType, this.data.toArray() );
	}

	@Override
	protected Status getStatusPreRejectorCheck( org.lgna.croquet.history.CompletionStep<?> step ) {
		return IS_GOOD_TO_GO_STATUS;
	}

	@Override
	protected void initializeToPreviousExpression( org.lgna.project.ast.Expression expression ) {
		java.util.List<org.lgna.project.ast.Expression> items = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		if( expression instanceof org.lgna.project.ast.ArrayInstanceCreation ) {
			org.lgna.project.ast.ArrayInstanceCreation arrayInstanceCreation = (org.lgna.project.ast.ArrayInstanceCreation)expression;
			if( this.arrayType.isAssignableFrom( arrayInstanceCreation.getType() ) ) {
				for( org.lgna.project.ast.Expression itemExpression : arrayInstanceCreation.expressions ) {
					items.add( org.alice.ide.IDE.getActiveInstance().createCopy( itemExpression ) );
				}
			}
		}
		this.data.internalSetAllItems( items );
	}

	@Override
	protected java.awt.Dimension calculateWindowSize( org.lgna.croquet.views.AbstractWindow<?> window ) {
		return new java.awt.Dimension( 400, 500 );
	}

	public static void main( String[] args ) throws Exception {
		edu.cmu.cs.dennisc.javax.swing.UIManagerUtilities.setLookAndFeel( "Nimbus" );
		//new org.alice.stageide.StageIDE();
		try {
			org.lgna.croquet.triggers.Trigger trigger = null;
			ArrayCustomExpressionCreatorComposite.getInstance( org.lgna.project.ast.JavaType.getInstance( String[].class ) ).getValueCreator().fire( trigger );
		} catch( org.lgna.croquet.CancelException ce ) {
			//pass
		}
		System.exit( 0 );
	}
}
