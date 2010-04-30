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
public abstract class AbstractDeclarationPane<T> extends org.alice.ide.preview.PreviewInputPane< T > {
	class IsReassignableStateOperation extends org.alice.ide.operations.AbstractBooleanStateOperation {
		public IsReassignableStateOperation( boolean initialValue ) {
			super( edu.cmu.cs.dennisc.alice.Project.GROUP_UUID, java.util.UUID.fromString( "c95e177e-8fea-4916-a401-1b865594b135" ), initialValue, "(is constant)", "(is variable)" );
		}
		@Override
		protected void handleStateChange(boolean value) {
			AbstractDeclarationPane.this.handleIsReassignableChange( value );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: undo/redo support for", this );
		}
	}
	class DeclarationNameTextField extends edu.cmu.cs.dennisc.javax.swing.components.JSuggestiveTextField {
		public DeclarationNameTextField() {
			super( "", "" );
			this.setFont( this.getFont().deriveFont( 18.0f ) );
			this.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
				public void changedUpdate( javax.swing.event.DocumentEvent e ) {
					DeclarationNameTextField.this.handleUpdate( e );
				}
				public void insertUpdate( javax.swing.event.DocumentEvent e ) {
					DeclarationNameTextField.this.handleUpdate( e );
				}
				public void removeUpdate( javax.swing.event.DocumentEvent e ) {
					DeclarationNameTextField.this.handleUpdate( e );
				}
			} );
		}
		@Override
		public java.awt.Dimension getPreferredSize() {
			return edu.cmu.cs.dennisc.java.awt.DimensionUtilties.constrainToMinimumWidth( super.getPreferredSize(), 240 );
		}
		@Override
		public java.awt.Dimension getMaximumSize() {
			return this.getPreferredSize();
		}
		private void handleUpdate( javax.swing.event.DocumentEvent e ) {
			AbstractDeclarationPane.this.handleDeclarationNameUpdate( e );
		}
	}

	private org.alice.ide.initializer.BogusNode bogusNode;
	private IsReassignableStateOperation isReassignableStateOperation;
	private TypePane typePane;
	private DeclarationNameTextField declarationNameTextField = new DeclarationNameTextField();
	private org.alice.ide.initializer.InitializerPane initializerPane;

	private org.alice.ide.name.validators.NodeNameValidator nodeNameValidator;
	public AbstractDeclarationPane( org.alice.ide.name.validators.NodeNameValidator nodeNameValidator, edu.cmu.cs.dennisc.alice.ast.AbstractType initialType, edu.cmu.cs.dennisc.alice.ast.Expression initialExpression ) {
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
		bogusNode.componentType.addPropertyListener( new edu.cmu.cs.dennisc.property.event.PropertyListener() {
			public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			}
			public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
				AbstractDeclarationPane.this.handleChange();
			}
		} );
		bogusNode.isArray.addPropertyListener( new edu.cmu.cs.dennisc.property.event.PropertyListener() {
			public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			}
			public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
				AbstractDeclarationPane.this.handleChange();
			}
		} );
		bogusNode.componentExpression.addPropertyListener( new edu.cmu.cs.dennisc.property.event.PropertyListener() {
			public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			}
			public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
				AbstractDeclarationPane.this.handleChange();
			}
		} );
		bogusNode.arrayExpressions.addListPropertyListener( new edu.cmu.cs.dennisc.property.event.SimplifiedListPropertyAdapter< edu.cmu.cs.dennisc.alice.ast.Expression >() {
			@Override
			protected void changing( edu.cmu.cs.dennisc.property.event.ListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.Expression > e ) {
			}
			@Override
			protected void changed( edu.cmu.cs.dennisc.property.event.ListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.Expression > e ) {
				AbstractDeclarationPane.this.handleChange();
			}
		} );
	}
	public AbstractDeclarationPane( org.alice.ide.name.validators.NodeNameValidator nodeNameValidator ) {
		this( nodeNameValidator, null, null );
	}
	private void handleChange() {
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				AbstractDeclarationPane.this.updateOKButton();
			}
		} );
	}
	
//	@Override
//	public java.awt.Dimension getPreferredSize() {
//		return edu.cmu.cs.dennisc.java.awt.DimensionUtilties.constrainToMinimumWidth( super.getPreferredSize(), 320 );
//	}

	protected boolean isReassignable() {
		if( this.isReassignableStateOperation != null ) {
			return this.isReassignableStateOperation.getState();
		} else {
			throw new RuntimeException( "override" );
		}
	}
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType getValueType() {
		if( this.typePane != null ) {
			return this.typePane.getValueType();
		} else {
			throw new RuntimeException( "override" );
		}
	}
	protected final String getDeclarationName() {
		return this.declarationNameTextField.getText();
	}
	protected edu.cmu.cs.dennisc.alice.ast.Expression getInitializer() {
		if( this.initializerPane != null ) {
			return this.initializerPane.getInitializer();
		} else {
			throw new RuntimeException( "override" );
		}
	}

	protected abstract boolean isIsReassignableComponentDesired();
	protected boolean isIsReassignableComponentEnabled() {
		return true;
	}
	protected boolean getIsReassignableInitialState() {
		return true;
	}
	protected final edu.cmu.cs.dennisc.croquet.KComponent< ? > createIsReassignableComponent() {
		edu.cmu.cs.dennisc.croquet.KComponent< ? > rv;
		if( isIsReassignableComponentDesired() ) {
			this.isReassignableStateOperation = new IsReassignableStateOperation( this.getIsReassignableInitialState() );
			this.isReassignableStateOperation.setTrueText( "yes" );
			this.isReassignableStateOperation.setFalseText( "no" );
			this.isReassignableStateOperation.setEnabled( this.isIsReassignableComponentEnabled() );
			rv = this.getIDE().createCheckBox( this.isReassignableStateOperation );
			rv.setOpaque( false );
		} else {
			rv = null;
		}
		return rv;
	}
	


	protected boolean isValueTypeComponentEnabled() {
		return true;
	}
	protected abstract boolean isEditableValueTypeComponentDesired();
	protected edu.cmu.cs.dennisc.croquet.KComponent< ? > createValueTypeComponent() {
		if( this.isEditableValueTypeComponentDesired() ) {
			boolean isEnabled = this.isValueTypeComponentEnabled();
			this.typePane = new TypePane( bogusNode.componentType, bogusNode.isArray, isEnabled, isEnabled );
		} else {
			this.typePane = null;
		}
		return this.typePane;
	}



	protected final edu.cmu.cs.dennisc.croquet.KComponent< ? >[] createIsReassignableRow() {
		edu.cmu.cs.dennisc.croquet.KComponent< ? > component = this.createIsReassignableComponent();
		if( component != null ) {
			return edu.cmu.cs.dennisc.croquet.SpringUtilities.createRow( edu.cmu.cs.dennisc.croquet.SpringUtilities.createTrailingLabel( "is re-assignable:" ), component );
		} else {
			return null;
		}
	}

	protected String getValueTypeText() {
		return "value type:";
	}

	protected final edu.cmu.cs.dennisc.croquet.KComponent< ? >[] createValueTypeRow() {
		edu.cmu.cs.dennisc.croquet.KComponent< ? > component = this.createValueTypeComponent();
		if( component != null ) {
			return edu.cmu.cs.dennisc.croquet.SpringUtilities.createRow( edu.cmu.cs.dennisc.croquet.SpringUtilities.createTrailingLabel( this.getValueTypeText() ), component );
		} else {
			return null;
		}
	}

	protected final edu.cmu.cs.dennisc.croquet.KComponent< ? >[] createNameRow() {
		return edu.cmu.cs.dennisc.croquet.SpringUtilities.createRow( edu.cmu.cs.dennisc.croquet.SpringUtilities.createTrailingLabel( "name:" ), new edu.cmu.cs.dennisc.croquet.KSwingAdapter( this.declarationNameTextField ) );
	}
	
	protected abstract boolean isEditableInitializerComponentDesired();
	protected edu.cmu.cs.dennisc.croquet.KComponent< ? > createInitializerComponent() {
		if( this.isEditableInitializerComponentDesired() ) {
			this.initializerPane = new org.alice.ide.initializer.InitializerPane( bogusNode );
		} else {
			this.initializerPane = null;
		}
		return this.initializerPane;
	}
	protected final edu.cmu.cs.dennisc.croquet.KComponent< ? >[] createInitializerRow() {
		edu.cmu.cs.dennisc.croquet.KComponent< ? > component = this.createInitializerComponent();
		if( component != null ) {
			return edu.cmu.cs.dennisc.croquet.SpringUtilities.createRow( edu.cmu.cs.dennisc.croquet.SpringUtilities.createTrailingLabel( "initializer:" ), component );
		} else {
			return null;
		}
	}
	protected edu.cmu.cs.dennisc.croquet.KComponent< ? >[] createWarningRow() {
		return null;
	}

	@Override
	protected java.util.List< edu.cmu.cs.dennisc.croquet.KComponent< ? >[] > updateInternalComponentRows( java.util.List< edu.cmu.cs.dennisc.croquet.KComponent< ? >[] > rv ) {
		edu.cmu.cs.dennisc.croquet.KComponent< ? >[] isReassignableRow = createIsReassignableRow();
		edu.cmu.cs.dennisc.croquet.KComponent< ? >[] valueTypeRow = createValueTypeRow();
		edu.cmu.cs.dennisc.croquet.KComponent< ? >[] nameRow = createNameRow();
		edu.cmu.cs.dennisc.croquet.KComponent< ? >[] initializerRow = createInitializerRow();
		edu.cmu.cs.dennisc.croquet.KComponent< ? >[] warningRow = createWarningRow();
		if( isReassignableRow != null ) {
			rv.add( isReassignableRow );
		}
		if( valueTypeRow != null ) {
			rv.add( valueTypeRow );
		}
		if( nameRow != null ) {
			rv.add( nameRow );
		}
		if( initializerRow != null ) {
			rv.add( initializerRow );
		}
		if( warningRow != null ) {
			rv.add( new edu.cmu.cs.dennisc.croquet.KComponent< ? >[] { edu.cmu.cs.dennisc.croquet.BoxUtilities.createVerticalStrut( 16 ), null } );
			rv.add( warningRow );
		}
		return rv;
	}
	
	protected String getDefaultNameText() {
		return "";
	}
	
	@Override
	protected void adding() {
		super.adding();
		this.declarationNameTextField.setText( this.getDefaultNameText() );
		this.declarationNameTextField.selectAll();
		this.declarationNameTextField.requestFocus();
	}

	protected void handleIsReassignableChange( boolean value ) {
		this.updateOKButton();
	}
	protected void handleDeclarationNameUpdate( javax.swing.event.DocumentEvent e ) {
		this.updateOKButton();
	}
	protected boolean isDeclarationNameValidAndAvailable() {
		return this.nodeNameValidator.isNameValidAndAvailable( this.declarationNameTextField.getText() );
	}
	protected boolean isValueTypeValid() {
		if( this.typePane != null ) {
			return this.typePane.getValueType() != null;
		} else {
			return true;
		}
	}
	@Override
	public boolean isOKButtonValid() {
		return super.isOKButtonValid() && this.isDeclarationNameValidAndAvailable() && this.isValueTypeValid();
	}
}
