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
	private static int getMnemonicKey( javax.swing.Action action ) {
		Object rv = action.getValue( javax.swing.Action.MNEMONIC_KEY );
		return rv != null ? Integer.class.cast( rv ) : 0;
	}

	protected static void safeSetNameAndMnemonic( javax.swing.Action action, String nextName, int nextMnemonicKey ) {
		int index;
		if( nextMnemonicKey != 0 ) {
			char mnemonicChar = (char)nextMnemonicKey;

			int upperCaseIndex = nextName.indexOf( Character.toUpperCase( mnemonicChar ) );
			int lowerCaseIndex = nextName.indexOf( Character.toLowerCase( mnemonicChar ) );

			if( upperCaseIndex == -1 ) {
				index = lowerCaseIndex;
			} else if( lowerCaseIndex == -1 ) {
				index = upperCaseIndex;
			} else {
				if( lowerCaseIndex < upperCaseIndex ) {
					index = lowerCaseIndex;
				} else {
					index = upperCaseIndex;
				}
			}
		} else {
			index = -1;
		}

		int prevMnemonicKey = getMnemonicKey( action );
		if( prevMnemonicKey != 0 ) {
			action.putValue( javax.swing.Action.MNEMONIC_KEY, 0 );
		}
		action.putValue( javax.swing.Action.NAME, nextName );
		if( index != -1 ) {
			action.putValue( javax.swing.Action.MNEMONIC_KEY, nextMnemonicKey );
		}
	}

	private final java.util.List<ContextFactory<?>> contextFactories = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();

	//TODO: contemplate passing context factories on construction
	public AbstractModel( java.util.UUID id/* , ContextFactory<?>... contextFactories */) {
		super( id );
	}

	@Override
	public Iterable<ContextFactory<?>> getContextFactories() {
		return this.contextFactories;
	}

	public void addContextFactory( ContextFactory<?> contextFactory ) {
		assert contextFactory != null : this;
		this.contextFactories.add( contextFactory );
	}

	public void removeContextFactory( ContextFactory<?> contextFactory ) {
		assert contextFactory != null : this;
		this.contextFactories.remove( contextFactory );
	}

	@Override
	public abstract org.lgna.croquet.history.Step<?> fire( org.lgna.croquet.triggers.Trigger trigger );

	private boolean isEnabled = true;

	@Override
	public boolean isEnabled() {
		return this.isEnabled;
	}

	@Override
	public void setEnabled( boolean isEnabled ) {
		if( this.isEnabled != isEnabled ) {
			this.isEnabled = isEnabled;
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "todo: override setEnabled", this, isEnabled );
			for( org.lgna.croquet.views.SwingComponentView<?> component : org.lgna.croquet.views.ComponentManager.getComponents( this ) ) {
				component.getAwtComponent().setEnabled( this.isEnabled );
			}
		}
	}
}
