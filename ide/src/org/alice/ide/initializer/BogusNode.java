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
package org.alice.ide.initializer;

/**
 * @author Dennis Cosgrove
 */
public class BogusNode extends edu.cmu.cs.dennisc.alice.ast.AbstractNode {
	public edu.cmu.cs.dennisc.alice.ast.DeclarationProperty< edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> > componentType = new edu.cmu.cs.dennisc.alice.ast.DeclarationProperty< edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> >( this ) {
		@Override
		public void setValue(edu.cmu.cs.dennisc.property.PropertyOwner owner, edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> value) {
			assert value == null || value.isArray() == false;
			super.setValue( owner, value );
		}
	};
	public edu.cmu.cs.dennisc.property.BooleanProperty isArray = new edu.cmu.cs.dennisc.property.BooleanProperty( this, false );
	public edu.cmu.cs.dennisc.alice.ast.ExpressionProperty componentExpression = new edu.cmu.cs.dennisc.alice.ast.ExpressionProperty( this ) {
		@Override
		public edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> getExpressionType() {
			return BogusNode.this.componentType.getValue();
		}
	};
	public edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty arrayExpressions = new edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty( this );

	public BogusNode( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type, boolean isArrayIfTypeIsNull ) {
		this.componentType.addPropertyListener( new edu.cmu.cs.dennisc.property.event.PropertyListener() {
			public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			}
			public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
				BogusNode.this.handleChanged( e );
			}
		} );
		this.isArray.addPropertyListener( new edu.cmu.cs.dennisc.property.event.PropertyListener() {
			public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			}
			public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
				BogusNode.this.handleChanged( e );
			}
		} );
		edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> componentType;
		boolean isArray;
		if( type != null ) {
			if( type.isArray() ) {
				componentType = type.getComponentType();
				isArray = true;
			} else {
				componentType = type;
				isArray = false;
			}
		} else {
			componentType = null;
			isArray = isArrayIfTypeIsNull;
		}
		this.componentType.setValue( componentType );
		this.isArray.setValue( isArray );
	}
	private void handleChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type = this.componentType.getValue();
		if( type != null ) {
			this.componentExpression.setValue( ExpressionUtilities.getNextExpression( type, this.componentExpression.getValue() ) );
			final int N = this.arrayExpressions.size();
			for( int i=0; i<N; i++ ) {
				this.arrayExpressions.set( i, ExpressionUtilities.getNextExpression( type, this.arrayExpressions.get( i ) ) );
			}
		}
	}
	
	public edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> getType() {
		edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> componentType = this.componentType.getValue();
		if( componentType != null ) {
			if( this.isArray.getValue() ) {
				return componentType.getArrayType();
			} else {
				return componentType;
			}
		} else {
			return null;
		}
	}
}
