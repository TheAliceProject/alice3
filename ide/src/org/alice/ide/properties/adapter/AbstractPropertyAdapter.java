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

public abstract class AbstractPropertyAdapter<P, O> implements PropertyAdapter<P, O> 
{
	
	protected O instance;
	protected String repr;
	protected P lastSetValue;
	
	protected String getCurrentValueLabelString()
	{
		return  " (current value)";
	}
	
	protected List<ValueChangeObserver<P>> valueChangeObservers = new LinkedList<ValueChangeObserver<P>>();
	
	public AbstractPropertyAdapter(String repr)
	{
		this(repr, null);
	}
	
	public AbstractPropertyAdapter(String repr, O instance)
	{
		this.repr = repr;
		this.setInstance(instance);
	}
	
	public String getRepr()
	{
		return this.repr;
	}
	
	public abstract SetValueOperation<P> getSetValueOperation(P value);

	public abstract String getUndoRedoDescription(java.util.Locale locale);
	
	public void setInstance(O instance)
	{
		this.stopListening();
		this.instance = instance;
		this.startListening();
	}
	
	public O getInstance()
	{
		return this.instance;
	}
	
	public void setValue(P newValue)
	{
		this.lastSetValue = newValue;
//		this.notifyValueObservers(newValue);
	}
	
	public P getLastSetValue()
	{
		return this.lastSetValue;
	}
	
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
	
	protected void notifyValueObservers(P newValue)
	{
		for (ValueChangeObserver<P> observer : this.valueChangeObservers)
		{
			observer.valueChanged(newValue);
		}
	}

	public abstract org.lgna.croquet.Model getEditModel();
	
	public org.lgna.croquet.components.ViewController< ?,? > createEditViewController()
    {
		org.lgna.croquet.Model model = this.getEditModel();
		if( model instanceof org.lgna.croquet.PopupPrepModel ) {
			org.lgna.croquet.PopupPrepModel popupPrepModel = (org.lgna.croquet.PopupPrepModel)model;
	        return popupPrepModel.createPopupButton();
		} else if( model instanceof org.lgna.croquet.Operation< ? > ) {
			org.lgna.croquet.Operation< ? > operation = (org.lgna.croquet.Operation< ? >)model;
	        return operation.createButton();
		} else if (model == null){
			return null;
		}
		else {
			throw new RuntimeException( "todo" );
		}
    }
	
	protected abstract void startListening();
	
	protected abstract void stopListening();
	
	
}
