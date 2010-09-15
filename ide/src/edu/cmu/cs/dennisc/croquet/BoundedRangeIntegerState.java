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
public final class BoundedRangeIntegerState extends State<Integer> {
	public static interface ValueObserver {
		//public void changing( int nextValue );
		public void changed( int nextValue );
	};

	private int previousValue;
	private javax.swing.BoundedRangeModel boundedRangeModel = new javax.swing.DefaultBoundedRangeModel();
	private javax.swing.event.ChangeListener changeListener = new javax.swing.event.ChangeListener() {
		private boolean previousValueIsAdjusting = false;
		public void stateChanged(javax.swing.event.ChangeEvent e) {
			BoundedRangeIntegerStateContext boundedRangeIntegerStateContext;
			if( this.previousValueIsAdjusting ) {
				boundedRangeIntegerStateContext = (BoundedRangeIntegerStateContext)ContextManager.getCurrentContext();
			} else {
				boundedRangeIntegerStateContext = ContextManager.createAndPushBoundedRangeIntegerStateContext( BoundedRangeIntegerState.this );
			}
			this.previousValueIsAdjusting = boundedRangeModel.getValueIsAdjusting();
			boundedRangeIntegerStateContext.handleStateChanged( e );
			BoundedRangeIntegerState.this.fireValueChanged( e );
			
			if( this.previousValueIsAdjusting ) {
				//pass
			} else {
				int nextValue = boundedRangeModel.getValue();
				boundedRangeIntegerStateContext.commitAndInvokeDo( new BoundedRangeIntegerStateEdit( e, BoundedRangeIntegerState.this.previousValue, nextValue, false ) );
				BoundedRangeIntegerState.this.previousValue = nextValue;
				ModelContext< ? > popContext = ContextManager.popContext();
				assert popContext == boundedRangeIntegerStateContext;
			}
		}
	};
	public BoundedRangeIntegerState( Group group, java.util.UUID id, int minimum, int value, int maximum ) {
		super( group, id );
		this.boundedRangeModel.setMinimum( minimum );
		this.boundedRangeModel.setMaximum( maximum );
		this.boundedRangeModel.setValue( value );
		this.previousValue = this.boundedRangeModel.getValue();
		this.boundedRangeModel.addChangeListener( this.changeListener );
		this.localize();
	}

	@Override
	protected boolean isOwnerOfEdit() {
		return true;
	}

	@Override
	protected void localize() {
	}

	private java.util.List< ValueObserver > valueObservers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	public void addValueObserver( ValueObserver valueObserver ) {
		this.valueObservers.add( valueObserver );
	}
	public void removeValueObserver( ValueObserver valueObserver ) {
		this.valueObservers.remove( valueObserver );
	}
	
	private void fireValueChanged( javax.swing.event.ChangeEvent e ) {
		for( ValueObserver valueObserver : this.valueObservers ) {
			valueObserver.changed( this.boundedRangeModel.getValue() );
		}
	}
	
//	public javax.swing.BoundedRangeModel getBoundedRangeModel() {
//		return this.boundedRangeModel;
//	}
	
	public int getMinimum() {
		return this.boundedRangeModel.getMinimum();
	}
	public int getMaximum() {
		return this.boundedRangeModel.getMaximum();
	}
	@Override
	public Integer getValue() {
		return this.boundedRangeModel.getValue();
	}
	public void setValue( Integer value ) {
		this.boundedRangeModel.setValue( value );
	}
	
	private Slider register( final Slider rv ) {
		rv.getAwtComponent().setModel( this.boundedRangeModel );
		rv.addContainmentObserver( new Component.ContainmentObserver() {
			public void addedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
				BoundedRangeIntegerState.this.addComponent( rv );
			}
			public void removedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
				BoundedRangeIntegerState.this.removeComponent( rv );
			}
		} );
		return rv;
	}
	public Slider createSlider() {
		return register( new Slider( this ) );
	}
	//public abstract void perform( BoundedRangeContext boundedRangeContext );
}
