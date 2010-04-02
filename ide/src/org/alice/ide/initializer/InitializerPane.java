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
public class InitializerPane extends edu.cmu.cs.dennisc.croquet.swing.CardPane {
	private static final String ITEM_KEY = "ITEM_KEY";
	private static final String ARRAY_KEY = "ARRAY_KEY";
	private BogusNode bogusNode;
	private ItemInitializerPane itemInitializerPane;
	private ArrayInitializerPane arrayInitializerPane;
	public InitializerPane( BogusNode bogusNode ) {
		this.bogusNode = bogusNode;
		this.bogusNode.componentType.addPropertyListener( new edu.cmu.cs.dennisc.property.event.PropertyListener() {
			public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			}
			public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractType type = InitializerPane.this.bogusNode.componentType.getValue();
				if( type != null ) {
					arrayInitializerPane.handleTypeChange( type.getArrayType() );
				}
			}
		} );
		this.bogusNode.isArray.addPropertyListener( new edu.cmu.cs.dennisc.property.event.PropertyListener() {
			public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			}
			public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
				handleIsArrayChange( InitializerPane.this.bogusNode.isArray.getValue() );
			}
		} );
		this.itemInitializerPane = new ItemInitializerPane( this.bogusNode.componentExpression );
		this.arrayInitializerPane = new ArrayInitializerPane( this.bogusNode.arrayExpressions );
		this.add( this.itemInitializerPane, ITEM_KEY );
		this.add( this.arrayInitializerPane, ARRAY_KEY );
		String key;
		if( this.bogusNode.isArray.getValue() ) {
			key = ARRAY_KEY;
		} else {
			key = ITEM_KEY;
		}
		this.show( key );
	}
//	private java.awt.Component getCurrentCard() {
//		if( this.itemInitializerPane.isVisible() ) {
//			return this.itemInitializerPane;
//		} else {
//			return this.arrayInitializerPane;
//		}
//	}
	private void handleIsArrayChange( boolean isArray ) {
		String key;
		if( isArray ) {
			key = ARRAY_KEY;
		} else {
			key = ITEM_KEY;
		}
		this.show( key );
	}

//	public void handleTypeChange( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
//		if( type != null ) {
//			this.handleIsArrayChange( type.isArray() );
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: handleTypeChange" );
//			//this.getCurrentCard().handleTypeChange( type );
//		}
//	}
	public edu.cmu.cs.dennisc.alice.ast.Expression getInitializer() {
		if( this.itemInitializerPane.isVisible() ) {
			return this.bogusNode.componentExpression.getValue();
		} else {
			java.util.List< edu.cmu.cs.dennisc.alice.ast.Expression > expressions = this.bogusNode.arrayExpressions.getValue();
			return org.alice.ide.ast.NodeUtilities.createArrayInstanceCreation( this.bogusNode.getType(), expressions );
		}
	}
	@Override
	public java.awt.Dimension getPreferredSize() {
		if( this.itemInitializerPane.isVisible() ) {
			return this.itemInitializerPane.getPreferredSize();
		} else {
			return this.arrayInitializerPane.getPreferredSize();
		}
	}
	@Override
	public java.awt.Dimension getMaximumSize() {
		return this.getPreferredSize();
	}
}
