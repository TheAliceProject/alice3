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
package org.alice.ide.declarationpanes;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractDeclarationPane<T> extends org.alice.ide.preview.PanelWithPreview {
	private edu.cmu.cs.dennisc.croquet.StringState declarationNameState = new edu.cmu.cs.dennisc.croquet.StringState(
			edu.cmu.cs.dennisc.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString( "c63e8377-84e0-48b0-a77e-137879e398c1" ), ""
	);
	private org.alice.ide.initializer.BogusNode bogusNode;
	private edu.cmu.cs.dennisc.croquet.BooleanState isReassignableStateOperation;
	private TypePane typePane;
	private edu.cmu.cs.dennisc.croquet.TextField declarationNameTextField;
	private org.alice.ide.initializer.InitializerPane initializerPane;

	private org.alice.ide.name.validators.NodeNameValidator nodeNameValidator;
	public AbstractDeclarationPane( org.alice.ide.name.validators.NodeNameValidator nodeNameValidator, edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> initialType, edu.cmu.cs.dennisc.alice.ast.Expression initialExpression ) {
		this.nodeNameValidator = nodeNameValidator;
		this.bogusNode = new org.alice.ide.initializer.BogusNode( initialType, false );
		if( initialExpression != null ) {
			if( initialExpression instanceof edu.cmu.cs.dennisc.alice.ast.ArrayInstanceCreation ) {
				edu.cmu.cs.dennisc.alice.ast.ArrayInstanceCreation arrayInstanceCreation = (edu.cmu.cs.dennisc.alice.ast.ArrayInstanceCreation)initialExpression;
				this.bogusNode.arrayExpressions.setValue( arrayInstanceCreation.expressions.getValue() );
			} else {
				this.bogusNode.componentExpression.setValue( initialExpression );
			}
		}
	}
	public AbstractDeclarationPane( org.alice.ide.name.validators.NodeNameValidator nodeNameValidator ) {
		this( nodeNameValidator, null, null );
	}
	public void setNodeNameValidator(org.alice.ide.name.validators.NodeNameValidator nodeNameValidator) {
		this.nodeNameValidator = nodeNameValidator;
	}
	

	@Override
	public String getExplanationIfOkButtonShouldBeDisabled() {
		String[] explanations = new String[ 2 ];
		if( this.getValueType() != null ) {
			//pass
		} else {
			explanations[ 0 ] = "Must select a type";
		}
		explanations[ 1 ] = this.nodeNameValidator.getExplanationIfOkButtonShouldBeDisabled( this.getDeclarationName() );
		
		
		//
		//todo: add initializer
		//
		
		
		StringBuilder sb = new StringBuilder();

		for( String explanation : explanations ) {
			if( explanation != null ) {
				if( sb.length() > 0 ) {
					sb.append( " AND " );
				}
				sb.append( explanation );
			}
		}
		if( sb.length() > 0 ) {
			return sb.toString();
		} else {
			return null;
		}
	}
	protected boolean isReassignable() {
		if( this.isReassignableStateOperation != null ) {
			return this.isReassignableStateOperation.getValue();
		} else {
			throw new RuntimeException( "override" );
		}
	}
	//todo: reduce to protected
	public edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> getValueType() {
		if( this.typePane != null ) {
			return this.typePane.getValueType();
		} else {
			throw new RuntimeException( "override" );
		}
	}
	//todo: reduce to protected
	public final String getDeclarationName() {
		return this.declarationNameState.getValue();
	}
	protected edu.cmu.cs.dennisc.alice.ast.Expression getInitializer() {
		if( this.initializerPane != null ) {
			return this.initializerPane.getInitializer();
		} else {
			throw new RuntimeException( "override" );
		}
	}

	protected abstract boolean isIsReassignableStateDesired();
	protected boolean isIsReassignableComponentEnabled() {
		return true;
	}
	protected boolean getIsReassignableInitialState() {
		return true;
	}
	protected final edu.cmu.cs.dennisc.croquet.Component< ? > createIsReassignableComponent() {
		edu.cmu.cs.dennisc.croquet.JComponent< ? > rv;
		if( isIsReassignableStateDesired() ) {
			this.isReassignableStateOperation = new edu.cmu.cs.dennisc.croquet.BooleanState( edu.cmu.cs.dennisc.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString( "c95e177e-8fea-4916-a401-1b865594b135" ), this.getIsReassignableInitialState() );
			 //"(is constant)", "(is variable)"
			this.isReassignableStateOperation.setTextForTrueAndTextForFalse( "yes", "no" );
			this.isReassignableStateOperation.setEnabled( this.isIsReassignableComponentEnabled() );
			if( org.alice.ide.croquet.models.ui.preferences.IsExposingReassignableStatusState.getInstance().getValue() ) {
				rv = this.isReassignableStateOperation.createCheckBox();
				rv.setBackgroundColor( null );
			} else {
				rv = null;
			}
		} else {
			rv = null;
		}
		return rv;
	}
	


	protected boolean isValueTypeComponentEnabled() {
		return true;
	}
	protected abstract boolean isEditableValueTypeComponentDesired();
	protected edu.cmu.cs.dennisc.croquet.Component< ? > createValueTypeComponent() {
		if( this.isEditableValueTypeComponentDesired() ) {
			boolean isEnabled = this.isValueTypeComponentEnabled();
			this.typePane = new TypePane( bogusNode.componentType, bogusNode.isArray, isEnabled, isEnabled );
		} else {
			this.typePane = null;
		}
		return this.typePane;
	}



	protected final edu.cmu.cs.dennisc.croquet.Component< ? >[] createIsReassignableRow() {
		edu.cmu.cs.dennisc.croquet.Component< ? > component = this.createIsReassignableComponent();
		if( component != null ) {
			return edu.cmu.cs.dennisc.croquet.SpringUtilities.createRow( edu.cmu.cs.dennisc.croquet.SpringUtilities.createTrailingLabel( "is re-assignable:" ), component );
		} else {
			return null;
		}
	}

	protected String getValueTypeText() {
		return "value type:";
	}

	protected final edu.cmu.cs.dennisc.croquet.Component< ? >[] createValueTypeRow() {
		edu.cmu.cs.dennisc.croquet.Component< ? > component = this.createValueTypeComponent();
		if( component != null ) {
			return edu.cmu.cs.dennisc.croquet.SpringUtilities.createRow( edu.cmu.cs.dennisc.croquet.SpringUtilities.createTrailingLabel( this.getValueTypeText() ), component );
		} else {
			return null;
		}
	}

	protected final edu.cmu.cs.dennisc.croquet.Component< ? >[] createNameRow() {
		this.declarationNameTextField = this.declarationNameState.createTextField();
		//this.declarationNameTextField.setFontSize( 24.0f );
		this.declarationNameTextField.scaleFont( 1.5f );
		return edu.cmu.cs.dennisc.croquet.SpringUtilities.createRow( 
				edu.cmu.cs.dennisc.croquet.SpringUtilities.createTrailingLabel( "name:" ), 
				this.declarationNameTextField 
		);
	}
	
	protected abstract boolean isEditableInitializerComponentDesired();
	protected edu.cmu.cs.dennisc.croquet.Component< ? > createInitializerComponent() {
		if( this.isEditableInitializerComponentDesired() ) {
			this.initializerPane = new org.alice.ide.initializer.InitializerPane( bogusNode );
		} else {
			this.initializerPane = null;
		}
		return this.initializerPane;
	}
	protected final edu.cmu.cs.dennisc.croquet.Component< ? >[] createInitializerRow() {
		edu.cmu.cs.dennisc.croquet.Component< ? > component = this.createInitializerComponent();
		if( component != null ) {
			return edu.cmu.cs.dennisc.croquet.SpringUtilities.createRow( edu.cmu.cs.dennisc.croquet.SpringUtilities.createTrailingTopLabel( "initializer:" ), component );
		} else {
			return null;
		}
	}
	protected edu.cmu.cs.dennisc.croquet.Component< ? >[] createWarningRow() {
		return null;
	}

	private static java.util.List< edu.cmu.cs.dennisc.croquet.Component< ? >[] > addIfNecessary( java.util.List< edu.cmu.cs.dennisc.croquet.Component< ? >[] > rv, edu.cmu.cs.dennisc.croquet.Component< ? >... row ) {
		if( row != null ) {
			for( edu.cmu.cs.dennisc.croquet.Component<?> component : row ) {
				assert component != null;
			}
			rv.add( row );
		}
		return rv;
	}
	protected java.util.List< edu.cmu.cs.dennisc.croquet.Component< ? >[] > updateComponentRows( java.util.List< edu.cmu.cs.dennisc.croquet.Component< ? >[] > rv ) {
		edu.cmu.cs.dennisc.croquet.Component< ? >[] isReassignableRow = createIsReassignableRow();
		edu.cmu.cs.dennisc.croquet.Component< ? >[] valueTypeRow = createValueTypeRow();
		edu.cmu.cs.dennisc.croquet.Component< ? >[] nameRow = createNameRow();
		edu.cmu.cs.dennisc.croquet.Component< ? >[] initializerRow = createInitializerRow();
		edu.cmu.cs.dennisc.croquet.Component< ? >[] warningRow = createWarningRow();
		addIfNecessary( rv, isReassignableRow );
		addIfNecessary( rv, valueTypeRow );
		addIfNecessary( rv, nameRow );
		addIfNecessary( rv, initializerRow );
		if( warningRow != null ) {
			addIfNecessary( rv, edu.cmu.cs.dennisc.croquet.SpringUtilities.createRow( edu.cmu.cs.dennisc.croquet.BoxUtilities.createVerticalSliver( 16 ), null ) );
			//addIfNecessary( rv, edu.cmu.cs.dennisc.croquet.BoxUtilities.createVerticalSliver( 16 ), null );
			addIfNecessary( rv, warningRow );
		}
		return rv;
	}
	@Override
	protected edu.cmu.cs.dennisc.croquet.Component< ? > createMainComponent() {
		edu.cmu.cs.dennisc.croquet.RowsSpringPanel rv = new edu.cmu.cs.dennisc.croquet.RowsSpringPanel() {
			@Override
			protected java.util.List<edu.cmu.cs.dennisc.croquet.Component<?>[]> updateComponentRows(java.util.List<edu.cmu.cs.dennisc.croquet.Component<?>[]> rv) {
				return AbstractDeclarationPane.this.updateComponentRows( rv );
			}
		};
		return rv;
	}
	
	protected String getDefaultNameText() {
		return "";
	}
	
	@Override
	protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		super.handleAddedTo( parent );
		this.declarationNameState.setValue( this.getDefaultNameText() );
		this.declarationNameTextField.selectAll();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				AbstractDeclarationPane.this.declarationNameTextField.requestFocus();
			}
		} );
	}
	protected String getExplanationIfOkButtonShouldBeDisabled(String name) {
		if( this.nodeNameValidator != null ) {
			return this.nodeNameValidator.getExplanationIfOkButtonShouldBeDisabled( this.declarationNameState.getValue() );
		} else {
			return null;
		}
	}
	protected boolean isValueTypeValid() {
		if( this.typePane != null ) {
			return this.typePane.getValueType() != null;
		} else {
			return true;
		}
	}
	public abstract T getActualInputValue();
}
