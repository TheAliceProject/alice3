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
package org.lgna.story.implementation.eventhandling;

import java.util.concurrent.ConcurrentHashMap;

import org.lgna.common.ComponentThread;
import org.lgna.story.MultipleEventPolicy;

/**
 * @author Matt May
 */
public abstract class AbstractBinaryEventHandler<L, E extends org.lgna.story.event.AbstractEvent> extends TransformationChangedHandler<L, E> {

	protected void fireEvent( final L listener, final E event, final Object one, final Object two ) {
		//		final Object o = object == null ? NULL_OBJECT : object;
		if( isFiringMap.get( listener ) == null ) {
			isFiringMap.put( listener, new ConcurrentHashMap<Object, Boolean>() );
		}
		if( isFiringMap.get( listener ).get( one ) == null ) {
			isFiringMap.get( listener ).put( one, false );
		}
		if( isFiringMap.get( listener ).get( two ) == null ) {
			isFiringMap.get( listener ).put( two, false );
		}
		if( shouldFire ) {
			ComponentThread thread = new org.lgna.common.ComponentThread( new Runnable() {
				@Override
				public void run() {
					fire( listener, event );
					if( policyMap.get( listener ).equals( MultipleEventPolicy.ENQUEUE ) ) {
						fireDequeue( listener );
					}
					isFiringMap.get( listener ).put( one, false );
					isFiringMap.get( listener ).put( two, false );
				}
			}, "eventThread" );
			if( isFiringMap.get( listener ).get( one ).equals( false ) && isFiringMap.get( listener ).get( two ).equals( false ) ) {
				isFiringMap.get( listener ).put( one, true );
				isFiringMap.get( listener ).put( two, true );
				thread.start();
				return;
			} else if( policyMap.get( listener ).equals( MultipleEventPolicy.COMBINE ) ) {
				thread.start();
			} else if( policyMap.get( listener ).equals( MultipleEventPolicy.ENQUEUE ) ) {
				enqueue( event );
			}
		}
	}

	@Override
	@Deprecated
	/**
	 * If you are calling this method you are doing something wrong (mmay) use fireEvent(L,E,Object,Object)
	 */
	protected void fireEvent( L listener, E event ) {
		super.fireEvent( listener, event );
	}
}
