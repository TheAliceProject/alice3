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

package org.alice.ide.properties.adapter;

import java.util.LinkedList;
import java.util.List;

import org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException;
import org.alice.ide.croquet.models.StandardExpressionState;

public abstract class AbstractPropertyAdapter<P, O>
{
	public static interface ValueChangeObserver<P>
	{
		public void valueChanged(P newValue);
	}
	
	protected O instance;
	protected String repr;
	protected P lastSetValue;
	private boolean isExpressionSet = false;
	protected StandardExpressionState expressionState;

	private org.lgna.croquet.State.ValueObserver< org.lgna.project.ast.Expression > valueObserver = new org.lgna.croquet.State.ValueObserver< org.lgna.project.ast.Expression >() {
		public void changing( org.lgna.croquet.State< org.lgna.project.ast.Expression > state, org.lgna.project.ast.Expression prevValue, org.lgna.project.ast.Expression nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.lgna.project.ast.Expression > state, org.lgna.project.ast.Expression prevValue, org.lgna.project.ast.Expression nextValue, boolean isAdjusting ) {
			AbstractPropertyAdapter.this.onExpressionStateUpdate();
		}
	};
	
	
	protected String getCurrentValueLabelString()
	{
		return  " (current value)";
	}
	
	protected List<ValueChangeObserver<P>> valueChangeObservers = new LinkedList<ValueChangeObserver<P>>();
	
	public AbstractPropertyAdapter(String repr, O instance, StandardExpressionState expressionState)
	{
		this.repr = repr;
		this.expressionState = expressionState;
		this.setInstance(instance);
	}
	
	public String getRepr()
	{
		return this.repr;
	}

	public abstract String getUndoRedoDescription(java.util.Locale locale);
	
	public void setInstance(O instance)
	{
		this.stopListening();
		this.instance = instance;
		this.setExpressionValue(this.getValue());
		this.startListening();
	}
	
	public O getInstance()
	{
		return this.instance;
	}
	
	public void setValue(P newValue)
	{
		this.lastSetValue = newValue;
	}
	
	public P getLastSetValue()
	{
		return this.lastSetValue;
	}
	
	public abstract P getValue();
	public abstract Class<P> getPropertyType();
	public abstract P getValueCopy();
	
	public void addValueChangeObserver(ValueChangeObserver<P> observer)
	{
		if (!this.valueChangeObservers.contains(observer))
		{
			this.valueChangeObservers.add(observer);
		}
	}
	
	public void addAndInvokeValueChangeObserver(ValueChangeObserver<P> observer)
	{
		this.addValueChangeObserver(observer);
		observer.valueChanged(this.getValue());
	}
	
	public void removeValueChangeObserver(ValueChangeObserver<P> observer)
	{
		this.valueChangeObservers.remove(observer);
	}
	
	public void setExpressionState( StandardExpressionState expressionState )
	{
		this.expressionState = expressionState;
		this.setExpressionValue(this.getValue());
	}
	
	public StandardExpressionState getExpressionState()
	{
		return this.expressionState;
	}
	
	protected void startListening() {
		startPropertyListening();
		startExpressionListening();
	}
	
	protected void stopListening() {
		stopPropertyListening();
		stopExpressionListening();
	}
	
	protected void startExpressionListening() {
		if (this.expressionState != null)
		{
			this.expressionState.addValueObserver( this.valueObserver );
		}
	}
	
	protected void stopExpressionListening() {
		if (this.expressionState != null)
		{
			this.expressionState.removeValueObserver( this.valueObserver );
		}
	}
	
	protected void startPropertyListening() {
	}
	
	protected void stopPropertyListening() {
	}
	
	private void setExpressionValue(P value)
	{
		if (this.expressionState != null)
		{
			try
			{
				org.lgna.project.ast.Expression expressionValue = org.alice.stageide.StageIDE.getActiveInstance().getApiConfigurationManager().getExpressionCreator().createExpression(this.getValue());
				this.expressionState.setValue(expressionValue);
			}
			catch (CannotCreateExpressionException e)
			{
				this.expressionState = null;
			}
		}
	}
	
	private void onExpressionStateUpdate()
	{
		org.lgna.project.ast.Expression expression = expressionState.getValue();
		if( expression != null ) {
			org.lgna.project.virtualmachine.VirtualMachine vm = org.alice.stageide.StageIDE.getActiveInstance().getVirtualMachineForSceneEditor();
			
			Object[] values = vm.ENTRY_POINT_evaluate( null, new org.lgna.project.ast.Expression[] { expression } );
			assert values.length == 1;
			isExpressionSet = true;
			this.setValue((P)values[ 0 ]);
			isExpressionSet = false;
		}
	}
	
	protected void notifyValueObservers(P newValue)
	{
		if (!isExpressionSet)
		{
			this.setExpressionValue(newValue);
		}
		for (ValueChangeObserver<P> observer : this.valueChangeObservers)
		{
			observer.valueChanged(newValue);
		}
	}

	public org.lgna.croquet.Model getEditModel()
	{
		return null;
	}
	
	public org.lgna.croquet.components.ViewController< ?,? > createEditViewController()
    {
		return null;
    }
	
	
}
