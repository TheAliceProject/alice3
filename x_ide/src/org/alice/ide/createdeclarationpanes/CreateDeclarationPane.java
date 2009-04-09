/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.ide.createdeclarationpanes;

import org.alice.ide.initializer.InitializerPane;

/**
 * @author Dennis Cosgrove
 */
public abstract class CreateDeclarationPane<E> extends org.alice.ide.preview.PreviewInputPane< E > {
	class IsReassignableStateOperation extends org.alice.ide.operations.AbstractBooleanStateOperation {
		public IsReassignableStateOperation( boolean initialValue ) {
			super( initialValue );
			//this.putValue( javax.swing.Action.NAME, "is constant" );
		}
		public void performStateChange( zoot.BooleanStateContext booleanStateContext ) {
			CreateDeclarationPane.this.handleIsReassignableChange( booleanStateContext );
			booleanStateContext.commit();
		}
	}
	class DeclarationNameTextField extends zoot.ZSuggestiveTextField {
		public DeclarationNameTextField() {
			super( "", "<unset>" );
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
			return edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMinimumWidth( super.getPreferredSize(), 240 );
		}
		@Override
		public java.awt.Dimension getMaximumSize() {
			return this.getPreferredSize();
		}
		private void handleUpdate( javax.swing.event.DocumentEvent e ) {
			CreateDeclarationPane.this.handleDeclarationNameUpdate( e );
		}
	}

	private org.alice.ide.initializer.BogusNode bogusNode = new org.alice.ide.initializer.BogusNode( null, false );
	private zoot.ZCheckBox isReassignableCheckBox;
	private TypePane typePane;
	private DeclarationNameTextField declarationNameTextField = new DeclarationNameTextField();
	private InitializerPane initializerPane;

	
	public CreateDeclarationPane() {
		bogusNode.componentType.addPropertyListener( new edu.cmu.cs.dennisc.property.event.PropertyListener() {
			public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			}
			public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
				CreateDeclarationPane.this.handleChange();
			}
		} );
		bogusNode.isArray.addPropertyListener( new edu.cmu.cs.dennisc.property.event.PropertyListener() {
			public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			}
			public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
				CreateDeclarationPane.this.handleChange();
			}
		} );
		bogusNode.componentExpression.addPropertyListener( new edu.cmu.cs.dennisc.property.event.PropertyListener() {
			public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			}
			public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
				CreateDeclarationPane.this.handleChange();
			}
		} );
		bogusNode.arrayExpressions.addListPropertyListener( new edu.cmu.cs.dennisc.property.event.SimplifiedListPropertyAdapter< edu.cmu.cs.dennisc.alice.ast.Expression >() {
			@Override
			protected void changing( edu.cmu.cs.dennisc.property.event.ListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.Expression > e ) {
			}
			@Override
			protected void changed( edu.cmu.cs.dennisc.property.event.ListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.Expression > e ) {
				CreateDeclarationPane.this.handleChange();
			}
		} );
	}
	
	private void handleChange() {
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				CreateDeclarationPane.this.updateOKButton();
			}
		} );
	}
	
	@Override
	public java.awt.Dimension getPreferredSize() {
		return edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMinimumWidth( super.getPreferredSize(), 320 );
	}

	protected boolean isReassignable() {
		if( this.isReassignableCheckBox != null ) {
			return this.isReassignableCheckBox.isSelected();
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
	protected final java.awt.Component createIsReassignableComponent() {
		if( isIsReassignableComponentDesired() ) {
			this.isReassignableCheckBox = new zoot.ZCheckBox( new IsReassignableStateOperation( this.getIsReassignableInitialState() ) ) {
				@Override
				public String getText() {
					if( this.isSelected() ) {
						return "yes";
					} else {
						return "no";
					}
				}
			};
			this.isReassignableCheckBox.setEnabled( this.isIsReassignableComponentEnabled() );
			this.isReassignableCheckBox.setOpaque( false );
		} else {
			this.isReassignableCheckBox = null;
		}
		return this.isReassignableCheckBox;
	}
	


	protected abstract boolean isEditableValueTypeComponentDesired();
	protected java.awt.Component createValueTypeComponent() {
		if( this.isEditableValueTypeComponentDesired() ) {
			this.typePane = new TypePane( bogusNode.componentType, bogusNode.isArray, true );
		} else {
			this.typePane = null;
		}
		return this.typePane;
	}



	protected final java.awt.Component[] createIsReassignableRow() {
		java.awt.Component component = this.createIsReassignableComponent();
		if( component != null ) {
			return edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "is re-assignable:" ), component );
		} else {
			return null;
		}
	}

	protected String getValueTypeText() {
		return "value type:";
	}

	protected final java.awt.Component[] createValueTypeRow() {
		java.awt.Component component = this.createValueTypeComponent();
		if( component != null ) {
			return edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( this.getValueTypeText() ), component );
		} else {
			return null;
		}
	}

	protected final java.awt.Component[] createNameRow() {
		return edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "name:" ), this.declarationNameTextField );
	}
	
	protected abstract boolean isEditableInitializerComponentDesired();
	protected java.awt.Component createInitializerComponent() {
		if( this.isEditableInitializerComponentDesired() ) {
			this.initializerPane = new InitializerPane( bogusNode );
		} else {
			this.initializerPane = null;
		}
		return this.initializerPane;
	}
	protected final java.awt.Component[] createInitializerRow() {
		java.awt.Component component = this.createInitializerComponent();
		if( component != null ) {
			return edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "initializer:" ), component );
		} else {
			return null;
		}
	}

	@Override
	protected java.util.List< java.awt.Component[] > updateRows( java.util.List< java.awt.Component[] > rv ) {
		final java.awt.Component[] isReassignableRow = createIsReassignableRow();
		final java.awt.Component[] valueTypeRow = createValueTypeRow();
		final java.awt.Component[] nameRow = createNameRow();
		final java.awt.Component[] initializerRow = createInitializerRow();
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
		return rv;
	}
	@Override
	public void addNotify() {
		super.addNotify();
		this.declarationNameTextField.requestFocus();
	}

	protected void handleIsReassignableChange( zoot.BooleanStateContext booleanStateContext ) {
		this.updateOKButton();
	}
	protected void handleDeclarationNameUpdate( javax.swing.event.DocumentEvent e ) {
		this.updateOKButton();
	}
	protected boolean isDeclarationNameValid() {
		return this.declarationNameTextField.getText().length() > 0;
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
		return super.isOKButtonValid() && this.isDeclarationNameValid() && this.isValueTypeValid();
	}
}
