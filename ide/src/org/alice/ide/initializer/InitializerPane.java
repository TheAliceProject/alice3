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
public class InitializerPane extends edu.cmu.cs.dennisc.croquet.CardPanel {
	private Key itemKey;
	private Key arrayKey;
	private BogusNode bogusNode;
	private ItemInitializerPane itemInitializerPane;
	private ArrayInitializerPane arrayInitializerPane;

	public InitializerPane(BogusNode bogusNode) {
		this.bogusNode = bogusNode;
//		this.bogusNode.componentType.addPropertyListener(new edu.cmu.cs.dennisc.property.event.PropertyListener() {
//			public void propertyChanging(edu.cmu.cs.dennisc.property.event.PropertyEvent e) {
//			}
//
//			public void propertyChanged(edu.cmu.cs.dennisc.property.event.PropertyEvent e) {
//				edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type = InitializerPane.this.bogusNode.componentType.getValue();
//				if (type != null) {
//					arrayInitializerPane.handleTypeChange(type.getArrayType());
//				}
//			}
//		});
		this.bogusNode.isArray.addPropertyListener(new edu.cmu.cs.dennisc.property.event.PropertyListener() {
			public void propertyChanging(edu.cmu.cs.dennisc.property.event.PropertyEvent e) {
			}

			public void propertyChanged(edu.cmu.cs.dennisc.property.event.PropertyEvent e) {
				handleIsArrayChange(InitializerPane.this.bogusNode.isArray.getValue());
			}
		});

		this.itemInitializerPane = new ItemInitializerPane(this.bogusNode.componentExpression);
		this.arrayInitializerPane = new ArrayInitializerPane(bogusNode.componentType, this.bogusNode.arrayExpressions);

		this.itemKey = this.createKey( this.itemInitializerPane, java.util.UUID.fromString("21574d0d-fb16-46df-8124-a0fdef77a4eb") );
		this.arrayKey = this.createKey( this.arrayInitializerPane, java.util.UUID.fromString("c6d6e1d9-93f3-45d7-956a-61a8d6914fb3") );
		this.addComponent(this.itemKey);
		this.addComponent(this.arrayKey);

		this.handleIsArrayChange(this.bogusNode.isArray.getValue());
	}

	// private java.awt.Component getCurrentCard() {
	// if( this.itemInitializerPane.isVisible() ) {
	// return this.itemInitializerPane;
	// } else {
	// return this.arrayInitializerPane;
	// }
	// }
	private void handleIsArrayChange(boolean isArray) {
		Key key;
		if (isArray) {
			key = this.arrayKey;
		} else {
			key = this.itemKey;
		}
		this.show(key);

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				edu.cmu.cs.dennisc.croquet.AbstractWindow<?> root = InitializerPane.this.getRoot();
				if( root != null ) {
					root.pack();
				}
			}
		} );
	}

	// public void handleTypeChange( edu.cmu.cs.dennisc.alice.ast.AbstractType
	// type ) {
	// if( type != null ) {
	// this.handleIsArrayChange( type.isArray() );
	// edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: handleTypeChange"
	// );
	// //this.getCurrentCard().handleTypeChange( type );
	// }
	// }
	public edu.cmu.cs.dennisc.alice.ast.Expression getInitializer() {
		if (this.itemInitializerPane.isVisible()) {
			return this.bogusNode.componentExpression.getValue();
		} else {
			java.util.List<edu.cmu.cs.dennisc.alice.ast.Expression> expressions = this.bogusNode.arrayExpressions.getValue();
			return org.alice.ide.ast.NodeUtilities.createArrayInstanceCreation(this.bogusNode.getType(), expressions);
		}
	}

	@Override
	protected javax.swing.JPanel createJPanel() {
		return new DefaultJPanel() {
			@Override
			public java.awt.Dimension getPreferredSize() {
				if (InitializerPane.this.itemInitializerPane.isVisible()) {
					return InitializerPane.this.itemInitializerPane.getAwtComponent().getPreferredSize();
				} else {
					return InitializerPane.this.arrayInitializerPane.getAwtComponent().getPreferredSize();
				}
			}

			@Override
			public java.awt.Dimension getMaximumSize() {
				return super.getPreferredSize();
			}
		};
	}
}
