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

package org.lgna.story.resources;

/**
 * @author Dennis Cosgrove
 */
public class JointId {

	private final JointId parent;
	private final Class<? extends JointedModelResource> containingClass;
	private final java.util.List<JointId> children = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();

	private static final java.util.Map<Class<? extends JointedModelResource>, java.util.Map<JointId, java.util.List<JointId>>> externalChildrenMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	private static void addExternalChild( JointId parent, JointId child )
	{
		Class<? extends JointedModelResource> childClass = child.getContainingClass();
		java.util.Map<JointId, java.util.List<JointId>> childClassMap = null;
		if( externalChildrenMap.containsKey( childClass ) )
		{
			childClassMap = externalChildrenMap.get( childClass );
		}
		else
		{
			childClassMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
			externalChildrenMap.put( childClass, childClassMap );
		}
		java.util.List<JointId> externalChildList = null;
		if( childClassMap.containsKey( parent ) )
		{
			externalChildList = childClassMap.get( parent );
		}
		else
		{
			externalChildList = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
			childClassMap.put( parent, externalChildList );
		}
		externalChildList.add( child );
	}

	private static java.util.List<JointId> getChildList( Class<? extends JointedModelResource> forClass, JointId forJoint )
	{
		if( ( forClass == null ) || ( forJoint == null ) )
		{
			return null;
		}
		if( externalChildrenMap.containsKey( forClass ) )
		{
			java.util.Map<JointId, java.util.List<JointId>> classMap = externalChildrenMap.get( forClass );
			if( classMap.containsKey( forJoint ) )
			{
				return classMap.get( forJoint );
			}
		}
		return null;
	}

	public JointId( JointId parent, Class<? extends JointedModelResource> containingClass ) {
		this.parent = parent;
		this.containingClass = containingClass;
		if( this.parent != null ) {
			if( this.parent.getContainingClass() == this.getContainingClass() ) {
				this.parent.children.add( this );
			} else {
				JointId.addExternalChild( parent, this );
			}
		}
	}

	public JointId getParent()
	{
		return this.parent;
	}

	protected Class<? extends JointedModelResource> getContainingClass()
	{
		return this.containingClass;
	}

	public Iterable<JointId> getDeclaredChildren()
	{
		return this.children;
	}

	private static class ExternalChildrenIterator implements java.util.Iterator<JointId>
	{
		private final JointId forJoint;
		private Class<? extends JointedModelResource> currentClass;
		private java.util.Iterator<JointId> currentIterator;

		public ExternalChildrenIterator( Class<? extends JointedModelResource> forClass, JointId forJoint )
		{
			this.forJoint = forJoint;
			this.currentClass = forClass;
			this.currentIterator = this.forJoint.getDeclaredChildren().iterator();
			if( !this.currentIterator.hasNext() )
			{
				this.currentIterator = getIteratorForExternalChildren();
			}
		}

		private java.util.Iterator<JointId> getIteratorForExternalChildren()
		{
			java.util.Iterator<JointId> externalIterator = null;
			while( ( currentClass != null ) && ( externalIterator == null ) )
			{
				java.util.List<JointId> jointList = JointId.getChildList( currentClass, forJoint );
				if( jointList != null )
				{
					externalIterator = jointList.iterator();
				}

				Class<?>[] interfaces = currentClass.getInterfaces();
				Class<?> superClass = null;
				if( interfaces.length > 0 ) {
					superClass = interfaces[ 0 ];
				}
				else {
					superClass = currentClass.getSuperclass();
				}
				if( JointedModelResource.class.isAssignableFrom( superClass ) ) {
					currentClass = (Class<? extends JointedModelResource>)superClass;
				}
				else {
					currentClass = null;
				}
			}
			return externalIterator;
		}

		@Override
		public boolean hasNext() {
			if( currentIterator != null )
			{
				return currentIterator.hasNext();
			}
			return false;
		}

		@Override
		public JointId next() {
			if( currentIterator != null )
			{
				JointId next = currentIterator.next();
				if( !currentIterator.hasNext() )
				{
					currentIterator = getIteratorForExternalChildren();
				}

				return next;
			}
			return null;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException( "remove" );
		}
	}

	private static class ExternalChildrenIterable implements java.lang.Iterable<JointId>
	{
		private final Class<? extends JointedModelResource> forClass;
		private final JointId forJoint;

		public ExternalChildrenIterable( Class<? extends JointedModelResource> forClass, JointId forJoint )
		{
			this.forClass = forClass;
			this.forJoint = forJoint;
		}

		@Override
		public java.util.Iterator<JointId> iterator() {
			return new ExternalChildrenIterator( this.forClass, this.forJoint );
		}

	}

	public Iterable<JointId> getChildren( JointedModelResource resource ) {
		return new ExternalChildrenIterable( resource.getClass(), this );
	}

	public Iterable<JointId> getChildren( Class<? extends JointedModelResource> resourceClass ) {
		return new ExternalChildrenIterable( resourceClass, this );
	}

	@Override
	public String toString() {
		for( java.lang.reflect.Field fld : this.containingClass.getFields() ) {
			int modidiers = fld.getModifiers();
			if( java.lang.reflect.Modifier.isPublic( modidiers ) ) {
				if( java.lang.reflect.Modifier.isStatic( modidiers ) ) {
					if( java.lang.reflect.Modifier.isFinal( modidiers ) ) {
						try {
							Object o = fld.get( null );
							if( o == this ) {
								return fld.getName();
							}
						} catch( IllegalAccessException iae ) {
							//pass
						}
					}
				}
			}
		}
		return super.toString();
	}
}
