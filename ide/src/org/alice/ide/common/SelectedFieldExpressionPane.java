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
	private org.lgna.croquet.ListSelectionState.ValueObserver< org.alice.ide.croquet.models.typeeditor.DeclarationComposite > declarationSelectionObserver = new org.lgna.croquet.ListSelectionState.ValueObserver< org.alice.ide.croquet.models.typeeditor.DeclarationComposite >() {
		public void changing( org.lgna.croquet.State< org.alice.ide.croquet.models.typeeditor.DeclarationComposite > state, org.alice.ide.croquet.models.typeeditor.DeclarationComposite prevValue, org.alice.ide.croquet.models.typeeditor.DeclarationComposite nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.alice.ide.croquet.models.typeeditor.DeclarationComposite > state, org.alice.ide.croquet.models.typeeditor.DeclarationComposite prevValue, org.alice.ide.croquet.models.typeeditor.DeclarationComposite nextValue, boolean isAdjusting ) {
			SelectedFieldExpressionPane.this.handleDeclarationChanged( nextValue != null ? nextValue.getDeclaration() : null );
		}
	};
	private org.lgna.croquet.State.ValueObserver<org.alice.ide.instancefactory.InstanceFactory> instanceFactorySelectionObserver = new org.lgna.croquet.State.ValueObserver<org.alice.ide.instancefactory.InstanceFactory>() {
		public void changing( org.lgna.croquet.State< org.alice.ide.instancefactory.InstanceFactory > state, org.alice.ide.instancefactory.InstanceFactory prevValue, org.alice.ide.instancefactory.InstanceFactory nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.alice.ide.instancefactory.InstanceFactory > state, org.alice.ide.instancefactory.InstanceFactory prevValue, org.alice.ide.instancefactory.InstanceFactory nextValue, boolean isAdjusting ) {
			SelectedFieldExpressionPane.this.handleInstanceFactoryChanged( nextValue );
		}
	};
	private edu.cmu.cs.dennisc.property.event.PropertyListener namePropertyAdapter = new edu.cmu.cs.dennisc.property.event.PropertyListener() {
		public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
		}
		public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			SelectedFieldExpressionPane.this.updateLabel();
		}
	};
	private org.lgna.croquet.State.ValueObserver< Boolean > valueObserver = new org.lgna.croquet.State.ValueObserver< Boolean >() {
		public void changing( org.lgna.croquet.State< java.lang.Boolean > state, java.lang.Boolean prevValue, java.lang.Boolean nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< java.lang.Boolean > state, java.lang.Boolean prevValue, java.lang.Boolean nextValue, boolean isAdjusting ) {
			SelectedFieldExpressionPane.this.updateLabel();
		}
	};
	
	private org.alice.ide.instancefactory.InstanceFactory instanceFactory = null;
	private org.lgna.croquet.components.Label label = new org.lgna.croquet.components.Label();
	public SelectedFieldExpressionPane( org.alice.ide.ast.SelectedFieldExpression selectedFieldExpression ) {
		super( null );
		this.addComponent( this.label );
	}
	@Override
	public org.lgna.project.ast.AbstractType<?,?,?> getExpressionType() {
		org.alice.ide.instancefactory.InstanceFactory instanceFactory = org.alice.ide.instancefactory.InstanceFactoryState.getInstance().getValue();
		if( instanceFactory != null ) {
			return instanceFactory.getValueType();
		} else {
			return null;
		}
	}
	private void handleInstanceFactoryChanged( org.alice.ide.instancefactory.InstanceFactory instanceFactory ) {
		if( this.instanceFactory != null ) {
			edu.cmu.cs.dennisc.property.StringProperty nameProperty = this.instanceFactory.getNamePropertyIfItExists();
			if( nameProperty != null ) {
				nameProperty.removePropertyListener( this.namePropertyAdapter );
			}
		}
		this.instanceFactory = instanceFactory;
		if( this.instanceFactory != null ) {
			edu.cmu.cs.dennisc.property.StringProperty nameProperty = this.instanceFactory.getNamePropertyIfItExists();
			if( nameProperty != null ) {
				nameProperty.addPropertyListener( this.namePropertyAdapter );
			}
		}
		this.updateLabel();
	}
	private void handleDeclarationChanged( org.lgna.project.ast.AbstractDeclaration declaration ) {
		this.updateLabel();
	}
	
	private void updateLabel() {
		this.setBackgroundColor( org.alice.ide.IDE.getActiveInstance().getTheme().getColorFor( this.instanceFactory != null ? org.lgna.project.ast.FieldAccess.class : org.lgna.project.ast.NullLiteral.class ) );
		this.label.setText( this.instanceFactory != null ? this.instanceFactory.getRepr() : "<unset>" );
		this.label.revalidateAndRepaint();
	}
	@Override
	protected boolean isExpressionTypeFeedbackDesired() {
		return true;
	}
	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		org.alice.ide.croquet.models.ui.preferences.IsIncludingThisForFieldAccessesState.getInstance().addAndInvokeValueObserver( this.valueObserver );
		org.alice.ide.croquet.models.typeeditor.DeclarationTabState.getInstance().addAndInvokeValueObserver( this.declarationSelectionObserver );
		org.alice.ide.instancefactory.InstanceFactoryState.getInstance().addAndInvokeValueObserver( this.instanceFactorySelectionObserver );
	}
	@Override
	protected void handleUndisplayable() {
		org.alice.ide.instancefactory.InstanceFactoryState.getInstance().removeValueObserver( this.instanceFactorySelectionObserver );
		org.alice.ide.croquet.models.typeeditor.DeclarationTabState.getInstance().removeValueObserver( this.declarationSelectionObserver );
		org.alice.ide.croquet.models.ui.preferences.IsIncludingThisForFieldAccessesState.getInstance().removeValueObserver( this.valueObserver );
		super.handleUndisplayable();
	}
}
