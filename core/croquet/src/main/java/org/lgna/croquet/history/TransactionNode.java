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
package org.lgna.croquet.history;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.pattern.Criterion;
import org.lgna.croquet.DropSite;
import org.lgna.croquet.Model;
import org.lgna.croquet.history.event.Event;
import org.lgna.croquet.history.event.Listener;
import org.lgna.croquet.triggers.DropTrigger;
import org.lgna.croquet.triggers.Trigger;

import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public abstract class TransactionNode<P extends TransactionNode<?>> {
	private final List<Listener> listeners = Lists.newCopyOnWriteArrayList();
	private P owner;

	TransactionNode( P owner ) {
		this.setOwner( owner );
	}

	public P getOwner() {
		return this.owner;
	}

	void setOwner( P owner ) {
		this.owner = owner;
	}


	public void addListener( Listener listener ) {
		this.listeners.add( listener );
	}

	public void removeListener( Listener listener ) {
		this.listeners.remove( listener );
	}

	public boolean isListening( Listener listener ) {
		return this.listeners.contains( listener );
	}

	void fireChanging( Event<?> e ) {
		if( this.owner != null ) {
			this.owner.fireChanging( e );
		}
		for( Listener listener : this.listeners ) {
			listener.changing( e );
		}
	}

	protected void fireChanged( Event<?> e ) {
		if( this.owner != null ) {
			this.owner.fireChanged( e );
		}
		for( Listener listener : this.listeners ) {
			listener.changed( e );
		}
	}
}
