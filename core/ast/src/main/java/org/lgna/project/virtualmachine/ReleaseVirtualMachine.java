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

import org.lgna.project.ast.AbstractParameter;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.UserLambda;
import org.lgna.project.ast.UserLocal;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.ast.UserParameter;

/**
 * @author Dennis Cosgrove
 */
public class ReleaseVirtualMachine extends VirtualMachine {
	protected static abstract class AbstractFrame implements Frame {
		private final Frame owner;
		private final java.util.Map<UserLocal, Object> mapLocalToValue = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

		//note: concurrent hashmaps cannot contain null
		//private final java.util.Map<UserLocal, Object> mapLocalToValue = edu.cmu.cs.dennisc.java.util.Queues.newConcurrentHashMap();

		public AbstractFrame( Frame owner ) {
			this.owner = owner;
		}

		@Override
		public Frame getOwner() {
			return this.owner;
		}

		private boolean contains( UserLocal local ) {
			return this.mapLocalToValue.containsKey( local );
		}

		@Override
		public boolean isValidLocal( org.lgna.project.ast.UserLocal local ) {
			if( this.contains( local ) ) {
				return true;
			} else {
				if( this.owner != null ) {
					return this.owner.isValidLocal( local );
				} else {
					return false;
				}
			}
		}

		@Override
		public boolean isValidParameter( org.lgna.project.ast.UserParameter parameter ) {
			if( this.owner != null ) {
				return this.owner.isValidParameter( parameter );
			} else {
				return false;
			}
		}

		@Override
		public void push( UserLocal local, Object value ) {
			this.mapLocalToValue.put( local, value );
		}

		@Override
		public Object get( UserLocal local ) {
			if( this.contains( local ) ) {
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

		@Override
		public void set( UserLocal local, Object value ) {
			if( this.contains( local ) ) {
				this.mapLocalToValue.put( local, value );
			} else {
				this.owner.set( local, value );
			}
		}

		@Override
		public void pop( UserLocal local ) {
			assert this.contains( local );
			synchronized( this.mapLocalToValue ) {
				this.mapLocalToValue.remove( local );
			}
		}

		@Override
		public abstract UserInstance getThis();

		@Override
		public abstract Object lookup( UserParameter parameter );

		protected abstract void appendRepr( StringBuilder sb, boolean isFormatted );

		@Override
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
		public Object lookup( UserParameter parameter ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( parameter );
			return null;
		}
	}

	protected static abstract class InvocationFrame extends AbstractFrame {
		private final java.util.Map<AbstractParameter, Object> mapParameterToValue;

		public InvocationFrame( Frame owner, java.util.Map<AbstractParameter, Object> mapParameterToValue ) {
			super( owner );
			this.mapParameterToValue = mapParameterToValue;
		}

		@Override
		public boolean isValidParameter( org.lgna.project.ast.UserParameter parameter ) {
			return this.mapParameterToValue.containsKey( parameter );
		}

		@Override
		public Object lookup( UserParameter parameter ) {
			if( this.mapParameterToValue.containsKey( parameter ) ) {
				return this.mapParameterToValue.get( parameter );
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( parameter );
				return null;
			}
		}

		protected void appendArgumentsRepr( StringBuilder sb, boolean isFormatted ) {
			if( this.mapParameterToValue.size() > 0 ) {
				if( isFormatted ) {
					sb.append( "<i>" );
				}
				sb.append( "arguments: " );
				if( isFormatted ) {
					sb.append( "</i> " );
				}
				for( AbstractParameter parameter : this.mapParameterToValue.keySet() ) {
					Object value = this.mapParameterToValue.get( parameter );
					sb.append( parameter.getName() );
					sb.append( "=" );
					sb.append( value );
				}
			}
		}
	}

	protected static class ConstructorInvocationFrame extends InvocationFrame {
		private final NamedUserType type;
		private UserInstance instance;

		public ConstructorInvocationFrame( Frame owner, NamedUserType type, java.util.Map<AbstractParameter, Object> mapParameterToValue ) {
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
			this.appendArgumentsRepr( sb, isFormatted );
		}
	}

	protected static abstract class AbstractMethodInvocationFrame extends InvocationFrame {
		private final UserInstance instance;

		public AbstractMethodInvocationFrame( Frame owner, java.util.Map<AbstractParameter, Object> mapParameterToValue, UserInstance instance ) {
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

		public MethodInvocationFrame( Frame owner, java.util.Map<AbstractParameter, Object> mapParameterToValue, UserInstance instance, UserMethod method ) {
			super( owner, mapParameterToValue, instance );
			this.method = method;
		}

		@Override
		protected void appendRepr( StringBuilder sb, boolean isFormatted ) {
			if( isFormatted ) {
				sb.append( "<strong>" );
			}
			sb.append( this.method.getName() );
			if( isFormatted ) {
				sb.append( "</strong>" );
			}
			sb.append( " " );
			if( isFormatted ) {
				sb.append( "<i>" );
			}
			sb.append( "instance:" );
			if( isFormatted ) {
				sb.append( "</i> " );
			}
			sb.append( this.getThis() );
			sb.append( " " );
			this.appendArgumentsRepr( sb, isFormatted );
		}
	}

	protected static class LambdaInvocationFrame extends AbstractMethodInvocationFrame {
		private final UserLambda lambda;
		private final org.lgna.project.ast.AbstractMethod singleAbstractMethod;

		public LambdaInvocationFrame( Frame owner, java.util.Map<AbstractParameter, Object> mapParameterToValue, UserInstance instance, UserLambda lambda, org.lgna.project.ast.AbstractMethod singleAbstractMethod ) {
			super( owner, mapParameterToValue, instance );
			this.lambda = lambda;
			this.singleAbstractMethod = singleAbstractMethod;
		}

		@Override
		protected void appendRepr( StringBuilder sb, boolean isFormatted ) {
			if( isFormatted ) {
				sb.append( "<strong>" );
			}
			sb.append( this.singleAbstractMethod != null ? this.singleAbstractMethod.getName() : null );
			if( isFormatted ) {
				sb.append( "</strong> " );
			}
			//todo
			//this.appendArgumentsRepr( sb, isFormatted );
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
		public Object lookup( UserParameter parameter ) {
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

	private final java.util.Map<Thread, Frame> mapThreadToFrame = edu.cmu.cs.dennisc.java.util.Maps.newConcurrentHashMap();

	private Frame getCurrentFrame() {
		Frame rv = this.mapThreadToFrame.get( Thread.currentThread() );
		//assert rv != null;
		return rv;
	}

	private void setCurrentFrame( Frame currentFrame ) {
		if( currentFrame != null ) {
			this.mapThreadToFrame.put( Thread.currentThread(), currentFrame );
		} else {
			this.mapThreadToFrame.remove( Thread.currentThread() );
		}
	}

	@Override
	public LgnaStackTraceElement[] getStackTrace( Thread thread ) {
		Frame frame = this.getFrameForThread( thread );
		if( frame != null ) {
			//a bit of double negative logic
			//push onto stack from top of runtime stack to get the order we want
			java.util.Stack<LgnaStackTraceElement> stack = edu.cmu.cs.dennisc.java.util.Stacks.newVectorStack();
			do {
				stack.push( frame );
				frame = frame.getOwner();
			} while( frame != null );
			return edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( stack, LgnaStackTraceElement.class );
		} else {
			return new LgnaStackTraceElement[] {};
		}
	}

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
	protected void pushConstructorFrame( org.lgna.project.ast.NamedUserType type, java.util.Map<AbstractParameter, Object> map ) {
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
	protected void pushMethodFrame( UserInstance instance, UserMethod method, java.util.Map<AbstractParameter, Object> map ) {
		Frame owner = getCurrentFrame();
		this.pushFrame( new MethodInvocationFrame( owner, map, instance, method ) );
	}

	@Override
	protected void pushLambdaFrame( org.lgna.project.virtualmachine.UserInstance instance, org.lgna.project.ast.UserLambda lambda, org.lgna.project.ast.AbstractMethod singleAbstractMethod, java.util.Map<org.lgna.project.ast.AbstractParameter, java.lang.Object> map ) {
		Frame owner = getCurrentFrame();
		this.pushFrame( new LambdaInvocationFrame( owner, map, instance, lambda, singleAbstractMethod ) );
	}

	@Override
	protected void pushLocal( org.lgna.project.ast.UserLocal local, Object value ) {
		getCurrentFrame().push( local, value );
	}

	@Override
	protected void setLocal( org.lgna.project.ast.UserLocal local, Object value ) {
		Frame frame = this.getCurrentFrame();
		if( frame.isValidLocal( local ) ) {
			frame.set( local, value );
		} else {
			throw new LgnaVmIllegalLocalAssignmentException( this, local );
		}
	}

	@Override
	protected Object getLocal( org.lgna.project.ast.UserLocal local ) {
		Frame frame = this.getCurrentFrame();
		if( frame.isValidLocal( local ) ) {
			return frame.get( local );
		} else {
			throw new LgnaVmIllegalLocalAccessException( this, local );
		}
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
	protected Object lookup( UserParameter parameter ) {
		Frame frame = this.getCurrentFrame();
		if( frame.isValidParameter( parameter ) ) {
			return frame.lookup( parameter );
		} else {
			throw new LgnaVmIllegalParameterAccessException( this, parameter );
		}
	}

	@Override
	protected Frame getFrameForThread( Thread thread ) {
		Frame rv;
		if( thread != null ) {
			rv = this.mapThreadToFrame.get( thread );
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
