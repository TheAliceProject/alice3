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

package edu.cmu.cs.dennisc.cheshire;

/**
 * @author Dennis Cosgrove
 */
public enum GroupFilter implements Filter {
	SINGLETON;
	public enum SuccessfulCompletionPolicy {
		ONLY_COMMITS() {
			@Override
			public boolean isAcceptable( edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent successfulCompletionEvent ) {
				return successfulCompletionEvent instanceof edu.cmu.cs.dennisc.croquet.CommitEvent;
			}
		},
		BOTH_COMMITS_AND_FINISHES() {
			@Override
			public boolean isAcceptable( edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent successfulCompletionEvent ) {
				return true;
			}
		};
		public abstract boolean isAcceptable( edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent successfulCompletionEvent );
	}

//	private java.util.List< edu.cmu.cs.dennisc.pattern.Tuple2< edu.cmu.cs.dennisc.croquet.Group, SuccessfulCompletionPolicy > > list = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
//	public void addGroup( edu.cmu.cs.dennisc.croquet.Group group, SuccessfulCompletionPolicy successfulCompletionPolicy ) {
//		list.add( edu.cmu.cs.dennisc.pattern.Tuple2.createInstance( group, successfulCompletionPolicy ) );
//	}
	private java.util.Map< edu.cmu.cs.dennisc.croquet.Group, SuccessfulCompletionPolicy > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public void addGroup( edu.cmu.cs.dennisc.croquet.Group group, SuccessfulCompletionPolicy successfulCompletionPolicy ) {
		map.put( group, successfulCompletionPolicy );
	}
	public < M extends edu.cmu.cs.dennisc.croquet.AbstractModelContext< ? > > M filter( M rv ) {
		java.util.ListIterator< edu.cmu.cs.dennisc.croquet.HistoryNode< ? > > childListIterator = rv.getChildListIterator();
		while( childListIterator.hasNext() ) {
			edu.cmu.cs.dennisc.croquet.HistoryNode< ? > node = childListIterator.next();
			boolean isToBeRemoved = true;
			if( node instanceof edu.cmu.cs.dennisc.croquet.AbstractModelContext< ? > ) {
				edu.cmu.cs.dennisc.croquet.AbstractModelContext< ? > childContext = (edu.cmu.cs.dennisc.croquet.AbstractModelContext< ? >)node;
				edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent successfulCompletionEvent = childContext.getSuccessfulCompletionEvent();
				if( successfulCompletionEvent != null ) {
					edu.cmu.cs.dennisc.croquet.AbstractModelContext< ? > parentContext = successfulCompletionEvent.getParent();
					edu.cmu.cs.dennisc.croquet.Model model = parentContext.getModel();
					edu.cmu.cs.dennisc.croquet.Group group = model.getGroup();
					SuccessfulCompletionPolicy successfulCompletionPolicy = map.get( group );
					if( successfulCompletionPolicy != null ) {
						if( successfulCompletionPolicy.isAcceptable( successfulCompletionEvent ) ) {
							isToBeRemoved = false;
						}
					}
				}
			}
			if( isToBeRemoved ) {
				childListIterator.remove();
			}
		}
		return rv;
	}
}
