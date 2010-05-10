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
public abstract class Operation {
	private java.util.UUID groupUUID;
	private java.util.UUID inividualUUID;
	public Operation( java.util.UUID groupUUID, java.util.UUID inividualUUID ) {
		this.groupUUID = groupUUID;
		this.inividualUUID = inividualUUID;
	}
	public java.util.UUID getGroupUUID() {
		return this.groupUUID;
	}
	public java.util.UUID getIndividualUUID() {
		return this.inividualUUID;
	}

//	public CompositeContext getCurrentCompositeContext() {	
//		Application application = Application.getSingleton();
//		return application.getCurrentCompositeContext();
//	}
//	protected java.awt.Component getSourceComponent( Context< ? > context ) {
//		if( context != null ) {
//			java.util.EventObject e = context.getEvent();
//			return edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( e.getSource(), java.awt.Component.class );
//		} else {
//			return null;
//		}
//	}


	private boolean isVisible = true;
	public boolean isVisible() {
		return this.isVisible;
	}
	public void setVisible( boolean isVisible ) {
		if( this.isVisible != isVisible ) {
			this.isVisible = isVisible;
			synchronized( this.components ) {
				for( Component<?> component : this.components ) {
					component.setVisible( this.isVisible );
				}
			}
		}
	}
	private boolean isEnabled = true;
	public boolean isEnabled() {
		return this.isEnabled;
	}
	public void setEnabled( boolean isEnabled ) {
		if( this.isEnabled != isEnabled ) {
			this.isEnabled = isEnabled;
			synchronized( this.components ) {
				for( Component<?> component : this.components ) {
					component.setEnabled( this.isEnabled );
				}
			}
		}
	}

	private String toolTipText = null;
	public String getToolTipText() {
		return this.toolTipText;
	}
	public void setToolTipText( String toolTipText ) {
		if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( this.toolTipText, toolTipText ) ) {
			//pass
		} else {
			this.toolTipText = toolTipText;
			synchronized( this.components ) {
				for( Component<?> component : this.components ) {
					component.setToolTipText( this.toolTipText );
				}
			}
		}
	}

	private java.util.List< Component<?> > components = new java.util.LinkedList< Component<?> >();
	
	protected void addComponent( Component<?> component ) {
		synchronized( this.components ) {
			this.components.add( component );
		}
		component.setEnabled( this.isEnabled );
		component.setToolTipText( this.toolTipText );
	}
	protected void removeComponent( Component<?> component ) {
		synchronized( this.components ) {
			this.components.remove( component );
		}
	}
	
//	protected abstract void perform( C context );
//	protected abstract C createContext( CompositeContext parentContext, java.util.EventObject e, CancelEffectiveness cancelEffectiveness );
//	protected final void performAsChildInCurrentContext( java.util.EventObject e, CancelEffectiveness cancelEffectiveness ) {
//		CompositeContext parentContext = Application.getSingleton().getCurrentCompositeContext();
//		C context = this.createContext( parentContext, e, cancelEffectiveness );
//		parentContext.performAsChild( this, context );
//	}
}
