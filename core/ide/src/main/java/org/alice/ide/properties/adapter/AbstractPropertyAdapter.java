/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/

package org.alice.ide.properties.adapter;

import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException;
import org.alice.ide.croquet.models.StandardExpressionState;
import org.alice.stageide.StageIDE;
import org.alice.stageide.sceneeditor.StorytellingSceneEditor;
import org.lgna.project.ast.Expression;
import org.lgna.project.virtualmachine.VirtualMachine;

public abstract class AbstractPropertyAdapter<P, O> {
	public static interface ValueChangeObserver<P> {
		public void valueChanged( P newValue );
	}

	protected O instance;
	protected String repr;
	protected String reprKey;
	protected P lastSetValue;
	private boolean isExpressionSet = false;
	protected StandardExpressionState expressionState;

	public static String getLocalizedString( String key ) {
		ResourceBundle resourceBundle = ResourceBundle.getBundle( AbstractPropertyAdapter.class.getPackage().getName() + ".propertyNames" );
		if( key != null ) {
			try {
				key = resourceBundle.getString( key );
			} catch( MissingResourceException e ) {
				e.printStackTrace();
			}
		}
		return key;
	}

	protected String getCurrentValueLabelString() {
		String val = getLocalizedString( "currentValue" );
		return " (" + val + ")";
	}

	protected List<ValueChangeObserver<P>> valueChangeObservers = new LinkedList<ValueChangeObserver<P>>();

	public AbstractPropertyAdapter( String repr, O instance, StandardExpressionState expressionState ) {
		this.repr = repr;
		this.reprKey = this.repr.replaceAll( "\\s+", "" ); // emove all the spaces from the repr to make a valid key for localization
		this.expressionState = expressionState;
		this.setInstance( instance );
		this.initializeExpressionState();
	}

	public String getLocalizedRepr() {
		String val = getLocalizedString( this.reprKey );
		return val;
	}

	public String getRepr() {
		return this.repr;
	}

	public String getUndoRedoDescription() {
		return getRepr();
	}

	public void setInstance( O instance ) {
		if( this.instance != null ) {
			this.stopListening();
		}
		this.instance = instance;
		this.startListening();
	}

	public O getInstance() {
		return this.instance;
	}

	public void setValue( P newValue ) {
		this.lastSetValue = newValue;
	}

	public P getLastSetValue() {
		return this.lastSetValue;
	}

	public abstract P getValue();

	public abstract Class<P> getPropertyType();

	public abstract P getValueCopyIfMutable();

	public void addValueChangeObserver( ValueChangeObserver<P> observer ) {
		synchronized( this.valueChangeObservers ) {
			if( !this.valueChangeObservers.contains( observer ) ) {
				this.valueChangeObservers.add( observer );
			}
		}
	}

	public void addAndInvokeValueChangeObserver( ValueChangeObserver<P> observer ) {
		this.addValueChangeObserver( observer );
		observer.valueChanged( this.getValue() );
	}

	public void removeValueChangeObserver( ValueChangeObserver<P> observer ) {
		synchronized( this.valueChangeObservers ) {
			this.valueChangeObservers.remove( observer );
		}
	}

	public void setExpressionState( StandardExpressionState expressionState ) {
		this.expressionState = expressionState;
		this.setExpressionValue( this.getValue() );
	}

	public void clearListeners() {
		synchronized( this.valueChangeObservers ) {
			this.valueChangeObservers.clear();
		}
	}

	public StandardExpressionState getExpressionState() {
		return this.expressionState;
	}

	public void startListening() {
		startPropertyListening();
	}

	public void stopListening() {
		stopPropertyListening();
	}

	protected void startPropertyListening() {
	}

	protected void stopPropertyListening() {
	}

	protected void initializeExpressionState() {
		this.setExpressionValue( this.getValue() );
	}

	protected void setExpressionValue( P value ) {
		if( this.expressionState != null ) {
			try {
				Expression expressionValue = StageIDE.getActiveInstance().getApiConfigurationManager().getExpressionCreator().createExpression( this.getValue() );
				this.expressionState.setValueTransactionlessly( expressionValue );

			} catch( CannotCreateExpressionException e ) {
				this.expressionState = null;
			}
		}
	}

	protected void intermediateSetValue( Object value ) {
		this.setValue( (P)value );
	}

	protected Object evaluateExpression( Expression expression ) {
		VirtualMachine vm = StorytellingSceneEditor.getInstance().getVirtualMachine();
		Object[] values = vm.ENTRY_POINT_evaluate( null, new Expression[] { expression } );
		assert values.length == 1;
		return values[ 0 ];
	}

	protected void notifyValueObservers( P newValue ) {
		if( !isExpressionSet ) {
			this.setExpressionValue( newValue );
		}
		synchronized( this.valueChangeObservers ) {
			for( ValueChangeObserver<P> observer : this.valueChangeObservers ) {
				observer.valueChanged( newValue );
			}
		}
	}

}
