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

import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.resolvers.RuntimeResolver;
/**
 * @author Dennis Cosgrove
 */
public abstract class Model extends Element implements RuntimeResolver< Model > {
	public Model( java.util.UUID id ) {
		super( id );
	}

	public final Model getResolved() {
		return this;
	}
	
	public abstract org.lgna.croquet.history.Step<?> fire( org.lgna.croquet.triggers.Trigger trigger );
	
	private boolean isEnabled = true;
	public boolean isEnabled() {
		return this.isEnabled;
	}
	public void setEnabled( boolean isEnabled ) {
		if( this.isEnabled != isEnabled ) {
			this.isEnabled = isEnabled;
			synchronized( this.components ) {
				for( org.lgna.croquet.components.JComponent<?> component : this.components ) {
					component.getAwtComponent().setEnabled( this.isEnabled );
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
				for( org.lgna.croquet.components.JComponent<?> component : this.components ) {
					component.setToolTipText( this.toolTipText );
				}
			}
		}
	}

	private java.util.List< org.lgna.croquet.components.JComponent<?> > components = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	public void addComponent( org.lgna.croquet.components.JComponent<?> component ) {
		if( this.components.size() == 0 ) {
			Manager.registerModel( this );
		}
		synchronized( this.components ) {
			this.components.add( component );
		}
		component.getAwtComponent().setEnabled( this.isEnabled );
		component.setToolTipText( this.toolTipText );
	}
	public void removeComponent( org.lgna.croquet.components.JComponent<?> component ) {
		synchronized( this.components ) {
			this.components.remove( component );
		}
		if( this.components.size() == 0 ) {
			Manager.unregisterModel( this );
		} else {
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( "removeComponent", this.components.size(), this );
		}
	}

	protected void repaintAllComponents() {
		synchronized( this.components ) {
			for( org.lgna.croquet.components.JComponent<?> component : this.components ) {
				component.repaint();
			}
		}
	}
	protected void revalidateAndRepaintAllComponents() {
		synchronized( this.components ) {
			for( org.lgna.croquet.components.JComponent<?> component : this.components ) {
				component.revalidateAndRepaint();
			}
		}
	}
	
	/*package-private*/ Iterable< org.lgna.croquet.components.JComponent<?> > getComponents() {
		return this.components;
	}
	
	private org.lgna.croquet.components.JComponent< ? > firstComponentHint;
	
	@Deprecated
	public <J extends org.lgna.croquet.components.JComponent< ? > > J getFirstComponent( Class<J> cls, boolean isVisibleAcceptable ) {
		if( this.firstComponentHint != null ) {
			return cls.cast( this.firstComponentHint );
		} else {
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "getFirstComponent:", this );
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "count:", this.components.size() );
			for( org.lgna.croquet.components.JComponent< ? > component : this.components ) {
				if( cls.isAssignableFrom( component.getClass() ) ) {
					if( component.getAwtComponent().isShowing() ) {
//						edu.cmu.cs.dennisc.print.PrintUtilities.println( "isShowing:", component.getAwtComponent().getClass() );
						return cls.cast( component );
					} else {
						//pass
					}
				}
			}
			if( isVisibleAcceptable ) {
				for( org.lgna.croquet.components.JComponent< ? > component : this.components ) {
					if( cls.isAssignableFrom( component.getClass() ) ) {
						if( component.getAwtComponent().isVisible() ) {
//							edu.cmu.cs.dennisc.print.PrintUtilities.println( "isShowing:", component.getAwtComponent().getClass() );
							return cls.cast( component );
						} else {
							//pass
						}
					}
				}
			}
		}
		return null;
	}
//	public JComponent getFirstNotNecessarilyShowingComponent() {
//		for( JComponent< ? > component : this.components ) {
//			if( component.getAwtComponent().isVisible() ) {
//				return component;
//			} else {
//				//pass
//			}
//		}
//		return null;
//	}
	@Deprecated
	public<J extends org.lgna.croquet.components.JComponent< ? > > J getFirstComponent( Class<J> cls ) {
		return getFirstComponent( cls, false );
	}
	@Deprecated
	public org.lgna.croquet.components.JComponent< ? > getFirstComponent( boolean isVisibleAcceptable ) {
		return getFirstComponent( org.lgna.croquet.components.JComponent.class, isVisibleAcceptable );
	}
	@Deprecated
	public org.lgna.croquet.components.JComponent< ? > getFirstComponent() {
		return getFirstComponent( false );
	}
	@Deprecated
	public void setFirstComponentHint( org.lgna.croquet.components.JComponent< ? > firstComponentHint ) {
//		Thread.dumpStack();
		assert this.components.contains( firstComponentHint );
		if( this.firstComponentHint != firstComponentHint ) {
//			if( this.firstComponentHint != null ) {
//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "setFirstComponentHint", this.firstComponentHint, "->", firstComponentHint );
//			}
			this.firstComponentHint = firstComponentHint;
		}
	}
	
	protected StringBuilder updateTutorialStepText( StringBuilder rv, org.lgna.croquet.history.Step< ? > step, Edit< ? > edit, UserInformation userInformation ) {
		rv.append( "text: " );
		rv.append( this );
		return rv;
	}

	public final String getTutorialNoteText( org.lgna.croquet.history.Step< ? > step, Edit< ? > edit, UserInformation userInformation ) {
		StringBuilder sb = new StringBuilder();
		this.updateTutorialStepText( sb, step, edit, userInformation );
		return sb.toString();
	}
}
