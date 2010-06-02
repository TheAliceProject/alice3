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
package edu.cmu.cs.dennisc.croquet;

/**
 * @author Dennis Cosgrove
 */
public /*final*/ class BooleanState extends Model {
	public static interface ValueObserver {
		public void changing( boolean nextValue );
		public void changed( boolean nextValue );
	};
	
	private java.util.List< ValueObserver > valueObservers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	public void addValueObserver( ValueObserver valueObserver ) {
		this.valueObservers.add( valueObserver );
	}
	public void addAndInvokeValueObserver(ValueObserver valueObserver) {
		this.addValueObserver(valueObserver);
		valueObserver.changed(this.getState());
	}
	public void removeValueObserver( ValueObserver valueObserver ) {
		this.valueObservers.remove( valueObserver );
	}
	
	private javax.swing.ButtonModel createButtonModel() {
		return new javax.swing.JToggleButton.ToggleButtonModel();
	}
	private javax.swing.ButtonModel buttonModel = this.createButtonModel();
	private javax.swing.Action action = new javax.swing.AbstractAction() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
		}
	};
	private java.awt.event.ItemListener itemListener = new java.awt.event.ItemListener() {
		public void itemStateChanged(java.awt.event.ItemEvent e) {
			Application application = Application.getSingleton();
			ModelContext parentContext = application.getCurrentContext();
			ModelContext childContext = parentContext.createChildContext();
			childContext.addChild( new BooleanStateEvent( childContext, BooleanState.this, e ) );
			childContext.commitAndInvokeDo( new BooleanStateEdit( childContext, e, BooleanState.this ) );
		}
	};

	private boolean state;
	private String trueText = null;
	private String falseText = null;

	public BooleanState(Group group, java.util.UUID individualUUID, boolean initialState, String trueText, String falseText) {
		super(group, individualUUID);
		this.state = initialState;
		this.buttonModel.setSelected(initialState);
		this.buttonModel.addItemListener(this.itemListener);
		this.setTrueText(trueText);
		this.setFalseText(falseText);
	}

	public BooleanState(Group group, java.util.UUID individualUUID, boolean initialState, String trueAndFalseText) {
		this(group, individualUUID, initialState, trueAndFalseText, trueAndFalseText);
	}
	public BooleanState(Group group, java.util.UUID individualUUID, boolean initialState ) {
		this(group, individualUUID, initialState, null, null );
	}

	/*package-private*/ javax.swing.ButtonModel getButtonModel() {
		return this.buttonModel;
	}
	public Boolean getState() {
		return this.buttonModel.isSelected();
	}
	/*package-private*/ void internalSetValue(boolean nextValue) {
		if( nextValue != this.state ) {
			this.buttonModel.removeItemListener(itemListener);

			for( ValueObserver valueObserver : this.valueObservers ) {
				valueObserver.changing( nextValue );
			}
			this.buttonModel.setSelected(nextValue);
			this.state = nextValue;
			for( ValueObserver valueObserver : this.valueObservers ) {
				valueObserver.changed( nextValue );
			}

			this.buttonModel.addItemListener(itemListener);
			this.updateName();
		}
	}
	@Deprecated
	public void setState( boolean state ) {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: BooleanStateOperation setValue" );
		this.internalSetValue( state );
	}

	public String getTrueText() {
		return this.trueText;
	}
	public void setTrueText(String trueText) {
		this.trueText = trueText;
		this.updateName();
	}

	public String getFalseText() {
		return this.falseText;
	}

	public void setFalseText(String falseText) {
		this.falseText = falseText;
		this.updateName();
	}


	private void updateName() {
		String name;
		if (this.getState()) {
			name = this.trueText;
		} else {
			name = this.falseText;
		}
		this.action.putValue(javax.swing.Action.NAME, name);
	}
	
	public < B extends AbstractButton<?,?> > B register( final B rv ) {
		Application.getSingleton().register( this );
		rv.setModel( this.buttonModel );
		rv.setAction( this.action );
		rv.addContainmentObserver( new Component.ContainmentObserver() {
			public void addedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
				BooleanState.this.addComponent( rv );
			}
			public void removedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
				BooleanState.this.removeComponent( rv );
			}
		} );
		return rv;
	}
	public RadioButton createRadioButton() {
		return register(new RadioButton() );
	}
	public CheckBox createCheckBox() {
		return register(new CheckBox() );
	}
	public CheckBoxMenuItem createCheckBoxMenuItem() {
		return register(new CheckBoxMenuItem() );
	}
}
