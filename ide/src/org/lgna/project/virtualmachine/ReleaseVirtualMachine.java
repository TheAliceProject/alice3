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
package org.lgna.project.virtualmachine;

import org.lgna.project.ast.*;

/**
 * @author Dennis Cosgrove
 */
public class ReleaseVirtualMachine extends VirtualMachine {
	protected static abstract class AbstractFrame implements Frame {
		private Frame m_owner = null;
		private java.util.Map< UserLocal, Object > m_mapLocalToValue = new java.util.concurrent.ConcurrentHashMap< UserLocal, Object >();

		public AbstractFrame( Frame owner ) {
			m_owner = owner;
		}
		public AbstractFrame( Frame owner, AbstractFrame other ) {
			this( owner );
			m_mapLocalToValue = new java.util.concurrent.ConcurrentHashMap< UserLocal, Object >( other.m_mapLocalToValue );
		}
		public Frame getOwner() {
			return m_owner;
		}
		private boolean contains( UserLocal local ) {
			synchronized( m_mapLocalToValue ) {
				return m_mapLocalToValue.containsKey( local );
			}
		}
		public void push( UserLocal local, Object value ) {
			synchronized( m_mapLocalToValue ) {
				m_mapLocalToValue.put( local, value );
			}
		}
		public Object get( UserLocal local ) {
			if( contains( local ) ) {
				synchronized( m_mapLocalToValue ) {
					return m_mapLocalToValue.get( local );
				}
			} else {
				if( m_owner != null ) {
					return m_owner.get( local );
				} else {
					throw new RuntimeException( "cannot find local: " + local.toString() );
				}
			}
		}
		public void set( UserLocal local, Object value ) {
			if( contains( local ) ) {
				synchronized( m_mapLocalToValue ) {
					m_mapLocalToValue.put( local, value );
				}
			} else {
				m_owner.set( local, value );
			}
		}
		public void pop( UserLocal local ) {
			assert contains( local );
			synchronized( m_mapLocalToValue ) {
				m_mapLocalToValue.remove( local );
			}
		}
		public abstract UserInstance getThis();
		public abstract Object lookup( AbstractParameter parameter );
		protected abstract StringBuilder appendRepr( StringBuilder rv );
		@Override
		public final String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append( this.getClass().getSimpleName() );
			sb.append( "[" );
			this.appendRepr( sb );
			sb.append( "]" );
			return sb.toString();
		}
	}

	protected static abstract class InvocationFrame extends AbstractFrame {
		protected UserInstance m_instance;
		private java.util.Map< AbstractParameter, Object > m_mapParameterToValue;
		public InvocationFrame( Frame owner, UserInstance instance, java.util.Map< AbstractParameter, Object > mapParameterToValue ) {
			super( owner );
			m_instance = instance;
			m_mapParameterToValue = mapParameterToValue;
		}
		public InvocationFrame( Frame owner, InvocationFrame other ) {
			super( owner, other );
			m_instance = other.m_instance;
			m_mapParameterToValue = new java.util.concurrent.ConcurrentHashMap< AbstractParameter, Object >( other.m_mapParameterToValue );
		}
		@Override
		public UserInstance getThis() {
			return m_instance;
		}
		@Override
		public Object lookup( AbstractParameter parameter ) {
			return m_mapParameterToValue.get( parameter );
		}
	}
	
	protected static class MethodInvocationFrame extends InvocationFrame {
		public MethodInvocationFrame( Frame owner, UserInstance instance, java.util.Map< AbstractParameter, Object > mapParameterToValue ) {
			super( owner, instance, mapParameterToValue );
		}
		public MethodInvocationFrame( Frame owner, MethodInvocationFrame other ) {
			super( owner, other );
		}
		@Override
		protected StringBuilder appendRepr( StringBuilder rv ) {
			return null;
		}
	}
	protected static class ConstructorInvocationFrame extends InvocationFrame {
		private final NamedUserType type;
		public ConstructorInvocationFrame( Frame owner, NamedUserType type, java.util.Map< AbstractParameter, Object > mapParameterToValue ) {
			super( owner, null, mapParameterToValue );
			this.type = type;
		}
		public ConstructorInvocationFrame( Frame owner, ConstructorInvocationFrame other ) {
			super( owner, other );
			this.type = other.type;
		}
		public void setThis( UserInstance instance ) {
			m_instance = instance;
		}
		@Override
		protected StringBuilder appendRepr( StringBuilder rv ) {
			rv.append( this.type.getName() );
			return rv;
		}
	}

	
	protected static class ThreadFrame extends AbstractFrame {
		public ThreadFrame( Frame owner ) {
			super( owner );
		}
		public ThreadFrame( Frame owner, ThreadFrame other ) {
			super( owner, other );
		}
		@Override
		public UserInstance getThis() {
			Frame owner = this.getOwner();
			assert owner != null;
			if( owner != null ) {
				return owner.getThis();
			} else {
				return null;
			}
		}
		@Override
		public Object lookup( AbstractParameter parameter ) {
			Frame owner = this.getOwner();
			assert owner != null;
			if( owner != null ) {
				return owner.lookup( parameter );
			} else {
				return null;
			}
		}
		@Override
		protected StringBuilder appendRepr( StringBuilder rv ) {
			return rv;
		}
	}

	private java.util.Map< Thread, Frame > m_mapThreadToFrame = new java.util.concurrent.ConcurrentHashMap< Thread, Frame >();

	private Frame getCurrentFrame() {
		Frame rv = m_mapThreadToFrame.get( Thread.currentThread() );
		//assert rv != null;
		return rv;
	}
	private void setCurrentFrame( Frame currentFrame ) {
		if( currentFrame != null ) {
			m_mapThreadToFrame.put( Thread.currentThread(), currentFrame );
		} else {
			m_mapThreadToFrame.remove( Thread.currentThread() );
		}
	}

//	private static Frame createCopyOfFrame( Frame frame ) {
//		Frame owner = frame.getOwner();
//		Frame ownerCopy;
//		if( owner != null ) {
//			ownerCopy = createCopyOfFrame( owner );
//		} else {
//			ownerCopy = null;
//		}
//		Frame rv;
//		if( frame instanceof MethodInvocationFrame ) {
//			rv = new MethodInvocationFrame( ownerCopy, (MethodInvocationFrame)frame );
//		} else if( frame instanceof ConstructorInvocationFrame ) {
//			rv = new ConstructorInvocationFrame( ownerCopy, (ConstructorInvocationFrame)frame );
//		} else if( frame instanceof ThreadFrame ) {
//			rv = new ThreadFrame( ownerCopy, (ThreadFrame)frame );
//		} else {
//			throw new AssertionError();
//		}
//		return rv;
//	}
//	@Override
//	protected Frame createCopyOfCurrentFrame() {
//		Frame frame = getCurrentFrame();
//		return createCopyOfFrame( frame );
//	}

	@Override
	protected UserInstance getThis() {
		Frame currentFrame = this.getCurrentFrame();
		return currentFrame != null ? currentFrame.getThis() : null;
	}

	private void pushFrame( Frame frame ) {
		this.setCurrentFrame( frame );
	}
	@Override
	protected void pushConstructorFrame( org.lgna.project.ast.NamedUserType type, java.util.Map< AbstractParameter, Object > map ) {
		Frame owner = getCurrentFrame();
		this.pushFrame( new ConstructorInvocationFrame( owner, type, map ) );
	}
	@Override
	protected void setConstructorFrameUserInstance( org.lgna.project.virtualmachine.UserInstance instance ) {
		Frame currentFrame = getCurrentFrame();
		assert currentFrame instanceof ConstructorInvocationFrame;
		ConstructorInvocationFrame constructorInvocationFrame = (ConstructorInvocationFrame)currentFrame;
		constructorInvocationFrame.setThis( instance );
	}
	@Override
	protected void pushMethodFrame( UserInstance instance, java.util.Map< AbstractParameter, Object > map ) {
		Frame owner = getCurrentFrame();
		this.pushFrame( new MethodInvocationFrame( owner, instance, map ) );
	}
	@Override
	protected void pushLocal( org.lgna.project.ast.UserLocal local, Object value ) {
		getCurrentFrame().push( local, value );
	}
	@Override
	protected void setLocal( org.lgna.project.ast.UserLocal local, Object value ) {
		getCurrentFrame().set( local, value );
	}
	@Override
	protected Object getLocal( org.lgna.project.ast.UserLocal local ) {
		return getCurrentFrame().get( local );
	}
	@Override
	protected void popLocal( org.lgna.project.ast.UserLocal local ) {
		getCurrentFrame().pop( local );
	}

	@Override
	protected void popFrame() {
		Frame currentFrame = this.getCurrentFrame();
		assert currentFrame != null;
		setCurrentFrame( currentFrame.getOwner() );
	}
	@Override
	protected Object lookup( AbstractParameter parameter ) {
		return getCurrentFrame().lookup( parameter );
	}
	@Override
	protected Frame getFrameForThread( Thread thread ) {
		Frame rv;
		if( thread != null ) {
			rv = m_mapThreadToFrame.get( thread );
		} else {
			rv = null;
		}
		return rv;
	}
	@Override
	protected void pushCurrentThread( Frame owner ) {
		this.pushFrame( new ThreadFrame( owner ) );
	}
	@Override
	protected void popCurrentThread() {
		this.popFrame();
	}
}
