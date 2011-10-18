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
package org.alice.ide.choosers;

/**
 * @author Dennis Cosgrove
 */
public class ArrayChooser extends AbstractRowsPaneChooser< org.lgna.project.ast.ArrayInstanceCreation > {
	private org.alice.ide.initializer.BogusNode bogusNode = new org.alice.ide.initializer.BogusNode( null, false );
	private org.alice.ide.declarationpanes.TypePane typePane;
	private org.alice.ide.initializer.ArrayInitializerPane arrayInitializerPane;
	private static final String[] LABEL_TEXTS = { "type:", "value:" };
	private org.lgna.croquet.components.Component< ? >[] components;
	
	public ArrayChooser(org.lgna.project.ast.AbstractType<?, ?, ?> arrayComponentType) {
		bogusNode.isArray.setValue( true );
		org.lgna.project.ast.ArrayInstanceCreation arrayInstanceCreation = edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( this.getPreviousExpression(), org.lgna.project.ast.ArrayInstanceCreation.class );
		boolean isComponentTypeEnabled = arrayInstanceCreation != null;
		boolean isArrayEnabled = false;
		this.typePane = new org.alice.ide.declarationpanes.TypePane( bogusNode.componentType, bogusNode.isArray, isComponentTypeEnabled, isArrayEnabled );
		if( arrayInstanceCreation != null ) {
			//typePane.setAndLockType( arrayInstanceCreation.arrayType.getValue() );
			org.lgna.project.ast.AbstractType<?,?,?> type = arrayInstanceCreation.arrayType.getValue().getComponentType();
			bogusNode.componentType.setValue( type );
			for( org.lgna.project.ast.Expression expression : arrayInstanceCreation.expressions ) {
				bogusNode.arrayExpressions.add( expression );
			}
		} else {
			bogusNode.componentType.setValue(arrayComponentType);
		}

		this.arrayInitializerPane = new org.alice.ide.initializer.ArrayInitializerPane( bogusNode.componentType, bogusNode.arrayExpressions );
		
		if (arrayComponentType.isAssignableFrom(Object.class)) {
			this.components = new org.lgna.croquet.components.Component< ? >[] { this.typePane, this.arrayInitializerPane };
		} else {
			org.alice.ide.formatter.Formatter formatter = org.alice.ide.croquet.models.ui.formatter.FormatterSelectionState.getInstance().getSelectedItem();
			String typeName = formatter.getTextForType( arrayComponentType ) + " [ ]";
			org.lgna.croquet.components.Label typeLabel = new org.lgna.croquet.components.Label( typeName );
			
			this.components = new org.lgna.croquet.components.Component< ? >[] { typeLabel, this.arrayInitializerPane };
		}
		
//		bogusNode.componentType.addPropertyListener( new edu.cmu.cs.dennisc.property.event.PropertyListener() {
//			public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
//			}
//			public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
//				ArrayChooser.this.arrayInitializerPane.handleTypeChange( bogusNode.getType() );
//			}
//		} );
//		bogusNode.arrayExpressions.addListPropertyListener( new edu.cmu.cs.dennisc.property.event.SimplifiedListPropertyAdapter< org.lgna.project.ast.Expression >() {
//			@Override
//			protected void changing( edu.cmu.cs.dennisc.property.event.ListPropertyEvent< org.lgna.project.ast.Expression > e ) {
//			}
//			@Override
//			protected void changed( edu.cmu.cs.dennisc.property.event.ListPropertyEvent< org.lgna.project.ast.Expression > e ) {
//			}
//		} );
	}
	
	//todo
	@Override
	protected org.lgna.croquet.components.Component< ? > createLabel( String text ) {
		if( LABEL_TEXTS[ 1 ].equals( text ) ) {
			return org.lgna.croquet.components.SpringUtilities.createTrailingTopLabel( text );
		} else {
			return super.createLabel( text );
		}
	}
	
	public org.lgna.project.ast.AbstractType<?,?,?> EPIC_HACK_getArrayComponentType() {
		if( this.typePane != null ) {
			org.lgna.project.ast.AbstractType<?,?,?> arrayType = this.typePane.getValueType();
			if( arrayType != null ) {
				return arrayType.getComponentType();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	@Override
	public String getExplanationIfOkButtonShouldBeDisabled() {
		if( this.typePane.getValueType() != null ) {
			return null;
		} else {
			return "type is not set";
		}
	}
	
	@Override
	public String[] getLabelTexts() {
		return LABEL_TEXTS;
	}
	@Override
	public org.lgna.croquet.components.Component< ? >[] getRowComponents() {
		return this.components;
	}
	@Override
	public org.lgna.project.ast.ArrayInstanceCreation getValue() {
		java.util.List< org.lgna.project.ast.Expression > expressions = this.bogusNode.arrayExpressions.getValue();
		return org.lgna.project.ast.AstUtilities.createArrayInstanceCreation( this.bogusNode.getType(), expressions );
	}
}
