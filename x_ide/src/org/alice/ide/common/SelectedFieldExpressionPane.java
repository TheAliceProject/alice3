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
package org.alice.ide.common;

import org.alice.ide.event.FieldSelectionEvent;
import org.alice.ide.event.FocusedCodeChangeEvent;
import org.alice.ide.event.LocaleEvent;
import org.alice.ide.event.ProjectOpenEvent;
import org.alice.ide.event.TransientSelectionEvent;

/**
 * @author Dennis Cosgrove
 */
public class SelectedFieldExpressionPane extends ExpressionLikeSubstance {
	private org.alice.ide.event.IDEListener ideAdapter = new org.alice.ide.event.IDEListener() {

		public void fieldSelectionChanging( FieldSelectionEvent e ) {
		}
		public void fieldSelectionChanged( FieldSelectionEvent e ) {
			SelectedFieldExpressionPane.this.handleFieldChanged( e.getNextValue() );
		}


		public void focusedCodeChanging( FocusedCodeChangeEvent e ) {
		}
		public void focusedCodeChanged( FocusedCodeChangeEvent e ) {
			SelectedFieldExpressionPane.this.handleCodeChanged( e.getNextValue() );
		}


		public void localeChanging( LocaleEvent e ) {
		}
		public void localeChanged( LocaleEvent e ) {
		}


		public void projectOpening( ProjectOpenEvent e ) {
		}
		public void projectOpened( ProjectOpenEvent e ) {
		}


		public void transientSelectionChanging( TransientSelectionEvent e ) {
		}
		public void transientSelectionChanged( TransientSelectionEvent e ) {
		}
		
	};
	
	private edu.cmu.cs.dennisc.property.event.PropertyListener namePropertyAdapter = new edu.cmu.cs.dennisc.property.event.PropertyListener() {
		public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
		}
		public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			SelectedFieldExpressionPane.this.updateLabel();
		}
	};
	
	private edu.cmu.cs.dennisc.alice.ast.AbstractField field = null;
	private javax.swing.JLabel label = edu.cmu.cs.dennisc.croquet.LabelUtilities.createLabel();
	public SelectedFieldExpressionPane( org.alice.ide.ast.SelectedFieldExpression selectedFieldExpression ) {
		this.add( this.label );
		this.setBackground( getIDE().getColorFor( edu.cmu.cs.dennisc.alice.ast.FieldAccess.class ) );
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.AbstractType getExpressionType() {
		edu.cmu.cs.dennisc.alice.ast.AbstractField field = getIDE().getFieldSelection();
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
	public void addNotify() {
		super.addNotify();
		getIDE().addIDEListener( this.ideAdapter );
		this.handleFieldChanged( getIDE().getFieldSelection() );
	}
	@Override
	public void removeNotify() {
		getIDE().removeIDEListener( this.ideAdapter );
		super.removeNotify();
	}
}
