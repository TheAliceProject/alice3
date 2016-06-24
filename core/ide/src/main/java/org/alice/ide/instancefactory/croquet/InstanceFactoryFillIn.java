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

package org.alice.ide.instancefactory.croquet;

/**
 * @author Dennis Cosgrove
 */
public class InstanceFactoryFillIn extends org.lgna.croquet.ImmutableCascadeFillIn<org.alice.ide.instancefactory.InstanceFactory, Void> {
	private class PropertyAdapter implements edu.cmu.cs.dennisc.property.event.PropertyListener {
		@Override
		public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
		}

		@Override
		public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			InstanceFactoryFillIn.this.markDirty();
		}
	}

	private final org.lgna.croquet.event.ValueListener<org.lgna.project.ast.NamedUserType> typeListener = new org.lgna.croquet.event.ValueListener<org.lgna.project.ast.NamedUserType>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.lgna.project.ast.NamedUserType> e ) {
			InstanceFactoryFillIn.this.markDirty();
		}
	};
	private static java.util.Map<org.alice.ide.instancefactory.InstanceFactory, InstanceFactoryFillIn> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	public static InstanceFactoryFillIn getInstance( org.alice.ide.instancefactory.InstanceFactory value ) {
		synchronized( map ) {
			InstanceFactoryFillIn rv = map.get( value );
			if( rv != null ) {
				//pass
			} else {
				rv = new InstanceFactoryFillIn( value );
				map.put( value, rv );
			}
			return rv;
		}
	}

	private final org.alice.ide.instancefactory.InstanceFactory value;
	private final PropertyAdapter propertyAdapter;

	private InstanceFactoryFillIn( org.alice.ide.instancefactory.InstanceFactory value ) {
		super( java.util.UUID.fromString( "2fce347e-f10e-4eec-8ac4-291225a5da4f" ) );
		this.value = value;

		if( this.value == org.alice.ide.instancefactory.ThisInstanceFactory.getInstance() ) {
			org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getTypeMetaState().addValueListener( this.typeListener );
		}

		edu.cmu.cs.dennisc.property.InstanceProperty<?>[] mutablePropertiesOfInterest = this.value.getMutablePropertiesOfInterest();
		if( ( mutablePropertiesOfInterest != null ) && ( mutablePropertiesOfInterest.length > 0 ) ) {
			this.propertyAdapter = new PropertyAdapter();
		} else {
			this.propertyAdapter = null;
		}
		if( ( mutablePropertiesOfInterest != null ) && ( this.propertyAdapter != null ) ) {
			for( edu.cmu.cs.dennisc.property.InstanceProperty<?> property : mutablePropertiesOfInterest ) {
				property.addPropertyListener( this.propertyAdapter );
			}
		}
	}

	@Override
	protected final javax.swing.JComponent createMenuItemIconProxy( org.lgna.croquet.imp.cascade.ItemNode<? super org.alice.ide.instancefactory.InstanceFactory, Void> step ) {
		org.lgna.project.ast.Expression expression = this.value.createTransientExpression();
		javax.swing.JComponent expressionPane = org.alice.ide.x.PreviewAstI18nFactory.getInstance().createExpressionPane( expression ).getAwtComponent();

		java.awt.Dimension iconSize;
		if( ( this.value instanceof org.alice.ide.instancefactory.ThisMethodInvocationFactory ) || ( this.value instanceof org.alice.ide.instancefactory.ThisFieldAccessMethodInvocationFactory ) ) {
			iconSize = org.alice.ide.Theme.DEFAULT_SMALLER_ICON_SIZE;
		} else {
			iconSize = org.alice.ide.Theme.DEFAULT_SMALL_ICON_SIZE;
		}

		javax.swing.Icon icon = this.value.getIconFactory().getIcon( iconSize );
		javax.swing.JLabel label = new javax.swing.JLabel( icon );

		expressionPane.setAlignmentY( 0.5f );
		label.setAlignmentY( 0.5f );

		javax.swing.JPanel rv = new javax.swing.JPanel();
		//		rv.setLayout( new java.awt.BorderLayout() );
		//		rv.add( label, java.awt.BorderLayout.LINE_START );
		//		rv.add( expressionPane, java.awt.BorderLayout.CENTER );
		rv.setLayout( new javax.swing.BoxLayout( rv, javax.swing.BoxLayout.LINE_AXIS ) );
		rv.add( label );
		rv.add( expressionPane );
		rv.setOpaque( false );
		return rv;

	}

	@Override
	public final org.alice.ide.instancefactory.InstanceFactory getTransientValue( org.lgna.croquet.imp.cascade.ItemNode<? super org.alice.ide.instancefactory.InstanceFactory, Void> node ) {
		return this.value;
	}

	@Override
	public org.alice.ide.instancefactory.InstanceFactory createValue( org.lgna.croquet.imp.cascade.ItemNode<? super org.alice.ide.instancefactory.InstanceFactory, Void> node, org.lgna.croquet.history.TransactionHistory transactionHistory ) {
		return this.value;
	}

	@Override
	protected void appendRepr( StringBuilder sb ) {
		super.appendRepr( sb );
		sb.append( "value=" );
		sb.append( this.value );
	}
}
