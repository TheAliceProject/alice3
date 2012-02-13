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

package org.lgna.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractModel extends AbstractElement implements Model {
	private final java.util.List< ContextFactory<?> > contextFactories = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	
	//TODO: contemplate passing context factories on construction
	public AbstractModel( java.util.UUID id/*, ContextFactory<?>... contextFactories*/ ) {
		super( id );
	}

	public Iterable< ContextFactory<?> > getContextFactories() {
		return this.contextFactories;
	}
	public void addContextFactory( ContextFactory<?> contextFactory ) {
		this.contextFactories.add( contextFactory );
	}
	public void removeContextFactory( ContextFactory<?> contextFactory ) {
		this.contextFactories.remove( contextFactory );
	}

	public final Model getResolved() {
		return this;
	}
	
	public abstract org.lgna.croquet.history.Step<?> fire( org.lgna.croquet.triggers.Trigger trigger );
	
//	private boolean isEnabled = true;
//	public boolean isEnabled() {
//		return this.isEnabled;
//	}
//	public void setEnabled( boolean isEnabled ) {
//		if( this.isEnabled != isEnabled ) {
//			this.isEnabled = isEnabled;
//			synchronized( this.components ) {
//				for( org.lgna.croquet.components.JComponent<?> component : this.components ) {
//					component.getAwtComponent().setEnabled( this.isEnabled );
//				}
//			}
//		}
//	}
	
	
//	public abstract boolean isEnabled();
//	public abstract void setEnabled( boolean isEnabled );
	public boolean isEnabled() {
		throw new RuntimeException( "todo" );
	}
	public void setEnabled( boolean isEnabled ) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( this );
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
			for( org.lgna.croquet.components.JComponent<?> component : ComponentManager.getComponents( this ) ) {
				component.setToolTipText( this.toolTipText );
			}
		}
	}

	protected abstract StringBuilder updateTutorialStepText( StringBuilder rv, org.lgna.croquet.history.Step< ? > step, org.lgna.croquet.edits.Edit< ? > edit, UserInformation userInformation );
//	protected StringBuilder updateTutorialStepText( StringBuilder rv, org.lgna.croquet.history.Step< ? > step, Edit< ? > edit, UserInformation userInformation ) {
//		rv.append( "TODO: override updateTutorialStepText " );
//		rv.append( this );
//		return rv;
//	}
	public final String getTutorialNoteText( org.lgna.croquet.history.Step< ? > step, String triggerText, org.lgna.croquet.edits.Edit< ? > edit, UserInformation userInformation ) {
		StringBuilder sb = new StringBuilder();
		sb.append( triggerText );
		this.updateTutorialStepText( sb, step, edit, userInformation );
		return sb.toString();
	}
}
