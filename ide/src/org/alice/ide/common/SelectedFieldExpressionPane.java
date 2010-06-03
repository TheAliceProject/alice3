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
package org.alice.ide.common;

/**
 * @author Dennis Cosgrove
 */
public class SelectedFieldExpressionPane extends ExpressionLikeSubstance {
	private edu.cmu.cs.dennisc.croquet.ItemSelectionState.ValueObserver< edu.cmu.cs.dennisc.alice.ast.AbstractCode > codeSelectionObserver = new edu.cmu.cs.dennisc.croquet.ItemSelectionState.ValueObserver< edu.cmu.cs.dennisc.alice.ast.AbstractCode >() {
		public void changed(edu.cmu.cs.dennisc.alice.ast.AbstractCode nextValue) {
			SelectedFieldExpressionPane.this.handleCodeChanged( nextValue );
		}
	};
	private edu.cmu.cs.dennisc.croquet.ItemSelectionState.ValueObserver< edu.cmu.cs.dennisc.alice.ast.AbstractField > fieldSelectionObserver = new edu.cmu.cs.dennisc.croquet.ItemSelectionState.ValueObserver< edu.cmu.cs.dennisc.alice.ast.AbstractField >() {
		public void changed(edu.cmu.cs.dennisc.alice.ast.AbstractField nextValue) {
			SelectedFieldExpressionPane.this.handleFieldChanged( nextValue );
		}
	};
	private edu.cmu.cs.dennisc.property.event.PropertyListener namePropertyAdapter = new edu.cmu.cs.dennisc.property.event.PropertyListener() {
		public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
		}
		public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			SelectedFieldExpressionPane.this.updateLabel();
		}
	};
	private edu.cmu.cs.dennisc.croquet.BooleanState.ValueObserver valueObserver = new edu.cmu.cs.dennisc.croquet.BooleanState.ValueObserver() {
		public void changing(boolean nextValue) {
		}
		public void changed(boolean nextValue) {
			SelectedFieldExpressionPane.this.updateLabel();
		}
	};
	
	private edu.cmu.cs.dennisc.alice.ast.AbstractField field = null;
	private edu.cmu.cs.dennisc.croquet.Label label = new edu.cmu.cs.dennisc.croquet.Label();
	public SelectedFieldExpressionPane( org.alice.ide.ast.SelectedFieldExpression selectedFieldExpression ) {
		this.addComponent( this.label );
		this.setBackgroundColor( getIDE().getColorFor( edu.cmu.cs.dennisc.alice.ast.FieldAccess.class ) );
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.AbstractType getExpressionType() {
		edu.cmu.cs.dennisc.alice.ast.AbstractField field = getIDE().getFieldSelectionState().getValue();
		if( field != null ) {
			return field.getValueType();
		} else {
			return null;
		}
	}
	private void handleFieldChanged( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		if( this.field != null ) {
			edu.cmu.cs.dennisc.property.StringProperty nameProperty = this.field.getNamePropertyIfItExists();
			if( nameProperty != null ) {
				nameProperty.removePropertyListener( this.namePropertyAdapter );
			}
		}
		this.field = field;
		if( this.field != null ) {
			edu.cmu.cs.dennisc.property.StringProperty nameProperty = this.field.getNamePropertyIfItExists();
			if( nameProperty != null ) {
				nameProperty.addPropertyListener( this.namePropertyAdapter );
			}
		}
		this.updateLabel();
	}
	private void handleCodeChanged( edu.cmu.cs.dennisc.alice.ast.AbstractCode code ) {
		this.updateLabel();
	}
	
	private void updateLabel() {
		this.label.setText( getIDE().getInstanceTextForField( this.field, true ) );
	}
	@Override
	protected boolean isExpressionTypeFeedbackDesired() {
		return true;
	}
	@Override
	protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		super.handleAddedTo( parent );
		org.alice.ide.IDE.getSingleton().getIsOmissionOfThisForFieldAccessesDesiredState().addAndInvokeValueObserver( this.valueObserver );
		org.alice.ide.IDE.getSingleton().getEditorsTabSelectionState().addAndInvokeValueObserver( this.codeSelectionObserver );
		org.alice.ide.IDE.getSingleton().getFieldSelectionState().addAndInvokeValueObserver( this.fieldSelectionObserver );
	}
	@Override
	protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		org.alice.ide.IDE.getSingleton().getFieldSelectionState().removeValueObserver( this.fieldSelectionObserver );
		org.alice.ide.IDE.getSingleton().getEditorsTabSelectionState().removeValueObserver( this.codeSelectionObserver );
		org.alice.ide.IDE.getSingleton().getIsOmissionOfThisForFieldAccessesDesiredState().removeValueObserver( this.valueObserver );
		super.handleRemovedFrom( parent );
	}
}
