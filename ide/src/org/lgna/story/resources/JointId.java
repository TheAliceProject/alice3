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

package org.lgna.story.resources;

import java.util.Iterator;

import javax.mail.MethodNotSupportedException;

import org.lgna.story.resources.BipedResource.BipedJointId;

/**
 * @author Dennis Cosgrove
 */
public abstract class JointId {
	
	
	private final JointId parent;
	private final java.util.List< JointId > children = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	
	private static final java.util.Map< Class, java.util.Map<JointId, java.util.List<JointId>> > externalChildrenMap = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	
	private static void addExternalChild(JointId parent, JointId child)
	{
		Class childClass = child.getClass();
		java.util.Map<JointId, java.util.List<JointId>> childClassMap = null;
		if (externalChildrenMap.containsKey(childClass))
		{
			childClassMap = externalChildrenMap.get(childClass);
		}
		else
		{
			childClassMap = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
			externalChildrenMap.put(childClass, childClassMap);
		}
		java.util.List<JointId> externalChildList = null;
		if (childClassMap.containsKey(parent))
		{
			externalChildList = childClassMap.get(parent);
		}
		else
		{
			externalChildList = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			childClassMap.put(parent, externalChildList);
		}
		externalChildList.add(child);
	}
	
	private static java.util.List< JointId > getChildList( Class forClass, JointId forJoint )
	{
		if (forClass == null || forJoint == null)
		{
			return null;
		}
		if (externalChildrenMap.containsKey(forClass))
		{
			java.util.Map<JointId, java.util.List<JointId>> classMap = externalChildrenMap.get(forClass);
			if (classMap.containsKey(forJoint))
			{
				return classMap.get(forJoint);
			}
		}
		return null;
	}
	
	protected JointId(JointId parent){
		this.parent = parent;
		if( this.parent != null ) {
			if( this.parent.getClass() == this.getClass() ) {
				this.parent.children.add( this );
			} else {
				JointId.addExternalChild( parent, this );
			}
		}
	}
	
	protected JointId getParent()
	{
		return this.parent;
	}
	
	protected Iterable< JointId > getDeclaredChildren()
	{
		return this.children;
	}
	
	private static class ExternalChildrenIterator implements java.util.Iterator<JointId>
	{
		private final JointId forJoint;
		private Class currentClass;
		private Iterator<JointId> currentIterator;
		
		public ExternalChildrenIterator(Class forClass, JointId forJoint)
		{
			this.forJoint = forJoint;
			this.currentClass = forClass;
			this.currentIterator = this.forJoint.getDeclaredChildren().iterator();
		}

		public boolean hasNext() {
			if (currentIterator != null)
			{
				return currentIterator.hasNext();
			}
			return false;
		}
		
		public JointId next() {
			if (currentIterator != null)
			{
				JointId next = currentIterator.next();
				if (!currentIterator.hasNext())
				{
					currentIterator = null;
					while (currentClass != null)
					{
						currentClass = currentClass.getSuperclass();
						java.util.List<JointId> jointList = JointId.getChildList(currentClass, forJoint);
						if (jointList != null)
						{
							currentIterator = jointList.iterator();
							break;
						}
					}
				}
				return next;
			}
			return null;
		}

		public void remove() {
			//Not implemented
		}
	}
	
	private static class ExternalChildrenIterable implements java.lang.Iterable<JointId>
	{
		private final Class forClass;
		private final JointId forJoint;
		
		public ExternalChildrenIterable(Class forClass, JointId forJoint)
		{
			this.forClass = forClass;
			this.forJoint = forJoint;
		}
		
		public Iterator<JointId> iterator() {
			return new ExternalChildrenIterator(this.forClass, this.forJoint);
		}
		
	}
	
	public static Iterable< JointId > getChildren( Class forClass, JointId forJoint )
	{
		return new ExternalChildrenIterable(forClass, forJoint);
	}
	
}
