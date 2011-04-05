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
	private edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver< org.alice.ide.editorstabbedpane.CodeComposite > codeSelectionObserver = new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver< org.alice.ide.editorstabbedpane.CodeComposite >() {
		public void changed(org.alice.ide.editorstabbedpane.CodeComposite nextValue) {
			SelectedFieldExpressionPane.this.handleCodeChanged( nextValue != null ? nextValue.getCode() : null );
		}
	};
	private edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver< edu.cmu.cs.dennisc.alice.ast.Accessible > accessibleSelectionObserver = new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver< edu.cmu.cs.dennisc.alice.ast.Accessible >() {
		public void changed(edu.cmu.cs.dennisc.alice.ast.Accessible nextValue) {
			SelectedFieldExpressionPane.this.handleAccessibleChanged( nextValue );
		}
	};
	private edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField > partSelectionObserver = new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField >() {
		public void changed(edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField nextValue) {
			SelectedFieldExpressionPane.this.handlePartChanged( nextValue );
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
	
	private edu.cmu.cs.dennisc.alice.ast.Accessible accessible = null;
	private edu.cmu.cs.dennisc.croquet.Label label = new edu.cmu.cs.dennisc.croquet.Label();
	public SelectedFieldExpressionPane( org.alice.ide.ast.SelectedFieldExpression selectedFieldExpression ) {
		this.addComponent( this.label );
		this.setEnabledBackgroundPaint( getIDE().getTheme().getColorFor( edu.cmu.cs.dennisc.alice.ast.FieldAccess.class ) );
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> getExpressionType() {
		edu.cmu.cs.dennisc.alice.ast.Accessible accessible = org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance().getSelectedItem();
		if( accessible != null ) {
			return accessible.getValueType();
		} else {
			return null;
		}
	}
	private void handleAccessibleChanged( edu.cmu.cs.dennisc.alice.ast.Accessible accessible ) {
		if( this.accessible != null ) {
			edu.cmu.cs.dennisc.property.StringProperty nameProperty = this.accessible.getNamePropertyIfItExists();
			if( nameProperty != null ) {
				nameProperty.removePropertyListener( this.namePropertyAdapter );
			}
		}
		this.accessible = accessible;
		if( this.accessible != null ) {
			edu.cmu.cs.dennisc.property.StringProperty nameProperty = this.accessible.getNamePropertyIfItExists();
			if( nameProperty != null ) {
				nameProperty.addPropertyListener( this.namePropertyAdapter );
			}
		}
		this.updateLabel();
	}
	private void handlePartChanged( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField field ) {
		this.updateLabel();
	}
	private void handleCodeChanged( edu.cmu.cs.dennisc.alice.ast.AbstractCode code ) {
		this.updateLabel();
	}
	
	private void updateLabel() {
		StringBuilder sb = new StringBuilder();
		sb.append( "<html>" );
		sb.append( getIDE().getInstanceTextForAccessible( this.accessible ) );
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField field = org.alice.ide.croquet.models.members.PartSelectionState.getInstance().getSelectedItem();
		if( field != null ) {
			//sb.append( ".getPart(" );
			sb.append( "'s " );
			sb.append( "<em>" );
			sb.append( field.getName() );
			sb.append( "</em>" );
		}
		sb.append( "</html>" );
		this.label.setText( sb.toString() );
	}
	@Override
	protected boolean isExpressionTypeFeedbackDesired() {
		return true;
	}
	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		org.alice.ide.croquet.models.ui.preferences.IsIncludingThisForFieldAccessesState.getInstance().addAndInvokeValueObserver( this.valueObserver );
		org.alice.ide.editorstabbedpane.EditorsTabSelectionState.getInstance().addAndInvokeValueObserver( this.codeSelectionObserver );
		org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance().addAndInvokeValueObserver( this.accessibleSelectionObserver );
		org.alice.ide.croquet.models.members.PartSelectionState.getInstance().addValueObserver( this.partSelectionObserver );
	}
	@Override
	protected void handleUndisplayable() {
		org.alice.ide.croquet.models.members.PartSelectionState.getInstance().removeValueObserver( this.partSelectionObserver );
		org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance().removeValueObserver( this.accessibleSelectionObserver );
		org.alice.ide.editorstabbedpane.EditorsTabSelectionState.getInstance().removeValueObserver( this.codeSelectionObserver );
		org.alice.ide.croquet.models.ui.preferences.IsIncludingThisForFieldAccessesState.getInstance().removeValueObserver( this.valueObserver );
		super.handleUndisplayable();
	}
}
