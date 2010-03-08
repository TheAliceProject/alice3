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
package edu.cmu.cs.dennisc.alice.virtualmachine;

import edu.cmu.cs.dennisc.alice.ast.*;

/**
 * @author Dennis Cosgrove
 */
public class ReleaseVirtualMachine extends VirtualMachine {
	protected abstract class AbstractFrame {
		private AbstractFrame m_owner = null;
		private java.util.Map< LocalDeclaredInAlice, Object > m_mapLocalToValue = new java.util.concurrent.ConcurrentHashMap< LocalDeclaredInAlice, Object >();

		public AbstractFrame( AbstractFrame owner ) {
			m_owner = owner;
		}
		public AbstractFrame getOwner() {
			return m_owner;
		}
		private boolean contains( LocalDeclaredInAlice local ) {
			return m_mapLocalToValue.containsKey( local );
		}
		public void push( LocalDeclaredInAlice local, Object value ) {
			m_mapLocalToValue.put( local, value );
		}
		public Object get( LocalDeclaredInAlice local ) {
			if( contains( local ) ) {
				return m_mapLocalToValue.get( local );
			} else {
				if( m_owner != null ) {
					return m_owner.get( local );
				} else {
					throw new RuntimeException( "cannot find local: " + local.toString() );
				}
			}
		}
		public void set( LocalDeclaredInAlice local, Object value ) {
			if( contains( local ) ) {
				m_mapLocalToValue.put( local, value );
			} else {
				m_owner.set( local, value );
			}
		}
		public void pop( LocalDeclaredInAlice local ) {
			assert contains( local );
			m_mapLocalToValue.remove( local );
		}
		public abstract Object getThis();
		public abstract Object lookup( AbstractParameter parameter );
	}

	protected class InvocationFrame extends AbstractFrame {
		private InstanceInAlice m_instance;
		private java.util.Map< AbstractParameter, Object > m_mapParameterToValue;
		public InvocationFrame( AbstractFrame owner, InstanceInAlice instance, java.util.Map< AbstractParameter, Object > mapParameterToValue ) {
			super( owner );
			m_instance = instance;
			m_mapParameterToValue = mapParameterToValue;
		}
		@Override
		public Object getThis() {
			return m_instance;
		}
		@Override
		public Object lookup( AbstractParameter parameter ) {
			return m_mapParameterToValue.get( parameter );
		}
	}

	protected class ThreadFrame extends AbstractFrame {
		public ThreadFrame( AbstractFrame owner ) {
			super( owner );
		}
		@Override
		public Object getThis() {
			return getOwner().getThis();
		}
		@Override
		public Object lookup( AbstractParameter parameter ) {
			return getOwner().lookup( parameter );
		}
	}

	private java.util.Map< Thread, AbstractFrame > m_mapThreadToFrame = new java.util.concurrent.ConcurrentHashMap< Thread, AbstractFrame >();

	private AbstractFrame getCurrentFrame() {
		AbstractFrame rv = m_mapThreadToFrame.get( Thread.currentThread() );
		assert rv != null;
		return rv;
	}
	private void setCurrentFrame( AbstractFrame currentFrame ) {
		m_mapThreadToFrame.put( Thread.currentThread(), currentFrame );
	}

	@Override
	protected Object getThis() {
		return getCurrentFrame().getThis();
	}
	@Override
	protected void pushFrame( InstanceInAlice instance, java.util.Map< AbstractParameter, Object > map ) {
		AbstractFrame owner = getCurrentFrame();
		setCurrentFrame( new InvocationFrame( owner, instance, map ) );
	}
	@Override
	protected void pushLocal( edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice local, Object value ) {
		getCurrentFrame().push( local, value );
	}
	@Override
	protected void setLocal( edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice local, Object value ) {
		getCurrentFrame().set( local, value );
	}
	@Override
	protected Object getLocal( edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice local ) {
		return getCurrentFrame().get( local );
	}
	@Override
	protected void popLocal( edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice local ) {
		getCurrentFrame().pop( local );
	}

	@Override
	protected void popFrame() {
		AbstractFrame frame = this.getCurrentFrame();
		setCurrentFrame( frame.getOwner() );
	}
	@Override
	protected Object lookup( AbstractParameter parameter ) {
		return getCurrentFrame().lookup( parameter );
	}

	@Override
	protected void pushCurrentThread( Thread parentThread ) {
		AbstractFrame owner;
		if( parentThread != null ) {
			owner = m_mapThreadToFrame.get( parentThread );
		} else {
			owner = null;
		}
		setCurrentFrame( new ThreadFrame( owner ) );
	}
	@Override
	protected void popCurrentThread() {
		m_mapThreadToFrame.remove( Thread.currentThread() );
	}
}
