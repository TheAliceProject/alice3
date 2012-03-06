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
		private final Frame owner;
		private final java.util.Map< UserLocal, Object > mapLocalToValue = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newConcurrentHashMap();

		public AbstractFrame( Frame owner ) {
			this.owner = owner;
		}
		public Frame getOwner() {
			return this.owner;
		}
		private boolean contains( UserLocal local ) {
			return this.mapLocalToValue.containsKey( local );
		}
		public void push( UserLocal local, Object value ) {
			this.mapLocalToValue.put( local, value );
		}
		public Object get( UserLocal local ) {
			if( contains( local ) ) {
				return this.mapLocalToValue.get( local );
			} else {
				if( this.owner != null ) {
					return this.owner.get( local );
				} else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( local );
					return null;
				}
			}
		}
		public void set( UserLocal local, Object value ) {
			if( this.contains( local ) ) {
				this.mapLocalToValue.put( local, value );
			} else {
				this.owner.set( local, value );
			}
		}
		public void pop( UserLocal local ) {
			assert contains( local );
			synchronized( this.mapLocalToValue ) {
				this.mapLocalToValue.remove( local );
			}
		}
		public abstract UserInstance getThis();
		public abstract Object lookup( AbstractParameter parameter );
		protected abstract void appendRepr( StringBuilder sb, boolean isFormatted );
		public void appendFormatted( StringBuilder sb ) {
			this.appendRepr( sb, true );
		}
		@Override
		public final String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append( this.getClass().getSimpleName() );
			sb.append( "[" );
			this.appendRepr( sb, false );
			sb.append( "]" );
			return sb.toString();
		}
	}
	
	protected static class BogusFrame extends AbstractFrame {
		private final UserInstance instance;
		public BogusFrame( Frame owner, UserInstance instance ) {
			super( owner );
			this.instance = instance;
		}
		@Override
		public UserInstance getThis() {
			return this.instance;
		}
		@Override
		protected void appendRepr( StringBuilder sb, boolean isFormatted ) {
			sb.append( "bogus" );
		}
		@Override
		public Object lookup( org.lgna.project.ast.AbstractParameter parameter ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( parameter );
			return null;
		}
	}

	protected static abstract class InvocationFrame extends AbstractFrame {
		private final java.util.Map< AbstractParameter, Object > mapParameterToValue;
		public InvocationFrame( Frame owner, java.util.Map< AbstractParameter, Object > mapParameterToValue ) {
			super( owner );
			this.mapParameterToValue = mapParameterToValue;
		}
		@Override
		public Object lookup( AbstractParameter parameter ) {
			if( this.mapParameterToValue.containsKey( parameter ) ) {
				return this.mapParameterToValue.get( parameter );
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( parameter );
				return null;
			}
		}
	}
	
	protected static class ConstructorInvocationFrame extends InvocationFrame {
		private final NamedUserType type;
		private UserInstance instance;
		public ConstructorInvocationFrame( Frame owner, NamedUserType type, java.util.Map< AbstractParameter, Object > mapParameterToValue ) {
			super( owner, mapParameterToValue );
			this.type = type;
		}
		@Override
		public org.lgna.project.virtualmachine.UserInstance getThis() {
			return this.instance;
		}
		public void setThis( UserInstance instance ) {
			this.instance = instance;
		}
		@Override
		protected void appendRepr( StringBuilder sb, boolean isFormatted ) {
			sb.append( this.type.getName() );
		}
	}

	protected static abstract class AbstractMethodInvocationFrame extends InvocationFrame {
		private final UserInstance instance;
		public AbstractMethodInvocationFrame( Frame owner, java.util.Map< AbstractParameter, Object > mapParameterToValue, UserInstance instance ) {
			super( owner, mapParameterToValue );
			this.instance = instance;
		}
		@Override
		public UserInstance getThis() {
			return this.instance;
		}
	}
	protected static class MethodInvocationFrame extends AbstractMethodInvocationFrame {
		private final UserMethod method;
		public MethodInvocationFrame( Frame owner, java.util.Map< AbstractParameter, Object > mapParameterToValue, UserInstance instance, UserMethod method ) {
			super( owner, mapParameterToValue, instance );
			this.method = method;
		}
		@Override
		protected void appendRepr( StringBuilder sb, boolean isFormatted ) {
			sb.append( "<strong>" );
			sb.append( this.method.getName() );
			sb.append( "</strong>" );
			sb.append( " <i>instance:</i>" );
			sb.append( this.getThis() );
		}
	}
	protected static class LambdaInvocationFrame extends AbstractMethodInvocationFrame {
		private final UserLambda lambda;
		public LambdaInvocationFrame( Frame owner, java.util.Map< AbstractParameter, Object > mapParameterToValue, UserInstance instance, UserLambda lambda ) {
			super( owner, mapParameterToValue, instance );
			this.lambda = lambda;
		}
		@Override
		protected void appendRepr( StringBuilder sb, boolean isFormatted ) {
			sb.append( "lambda" );
		}
	}

	
	protected static class ThreadFrame extends AbstractFrame {
		public ThreadFrame( Frame owner ) {
			super( owner );
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
		protected void appendRepr( StringBuilder sb, boolean isFormatted ) {
			sb.append( "thread" );
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
	
	@Override
	public LgnaStackTraceElement[] getStackTrace( Thread thread ) {
		Frame frame = this.getFrameForThread( thread );
		if( frame != null ) {
			java.util.Stack< LgnaStackTraceElement > stack = edu.cmu.cs.dennisc.java.util.Collections.newStack();
			do {
				stack.push( frame );
				frame = frame.getOwner();
			} while( frame != null );
			return edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( stack, LgnaStackTraceElement.class );
		} else {
			return new LgnaStackTraceElement[] {};
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
	protected void pushBogusFrame( org.lgna.project.virtualmachine.UserInstance instance ) {
		Frame owner = getCurrentFrame();
		this.pushFrame( new BogusFrame( owner, instance ) );
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
	protected void pushMethodFrame( UserInstance instance, UserMethod method, java.util.Map< AbstractParameter, Object > map ) {
		Frame owner = getCurrentFrame();
		this.pushFrame( new MethodInvocationFrame( owner, map, instance, method ) );
	}
	@Override
	protected void pushLambdaFrame( org.lgna.project.virtualmachine.UserInstance instance, org.lgna.project.ast.UserLambda lambda, java.util.Map< org.lgna.project.ast.AbstractParameter, java.lang.Object > map ) {
		Frame owner = getCurrentFrame();
		this.pushFrame( new LambdaInvocationFrame( owner, map, instance, lambda ) );
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
