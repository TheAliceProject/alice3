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

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public class JointId {
	private static final Map<Class<? extends JointedModelResource>, Map<JointId, List<JointId>>> externalChildrenMap = Maps.newHashMap();

	private static void addExternalChild( JointId parent, JointId child ) {
		Class<? extends JointedModelResource> childClass = child.getContainingClass();
		Map<JointId, List<JointId>> childClassMap = null;
		if( externalChildrenMap.containsKey( childClass ) ) {
			childClassMap = externalChildrenMap.get( childClass );
		} else {
			childClassMap = Maps.newHashMap();
			externalChildrenMap.put( childClass, childClassMap );
		}
		List<JointId> externalChildList = null;
		if( childClassMap.containsKey( parent ) ) {
			externalChildList = childClassMap.get( parent );
		} else {
			externalChildList = Lists.newLinkedList();
			childClassMap.put( parent, externalChildList );
		}
		externalChildList.add( child );
	}

	private static List<JointId> getChildList( Class<? extends JointedModelResource> forClass, JointId forJoint ) {
		if( ( forClass == null ) || ( forJoint == null ) ) {
			return null;
		}
		if( externalChildrenMap.containsKey( forClass ) ) {
			Map<JointId, List<JointId>> classMap = externalChildrenMap.get( forClass );
			if( classMap.containsKey( forJoint ) ) {
				return classMap.get( forJoint );
			}
		}
		return null;
	}

	private static class ExternalChildrenIterator implements Iterator<JointId> {
		private final JointId forJoint;
		private Class<? extends JointedModelResource> currentClass;
		private Iterator<JointId> currentIterator;
		private final JointedModelResource forResource;

		public ExternalChildrenIterator( Class<? extends JointedModelResource> forClass, JointedModelResource forResource, JointId forJoint ) {
			this.forJoint = forJoint;
			this.currentClass = forClass;
			this.forResource = forResource;
			this.currentIterator = this.forJoint.getDeclaredChildren( this.forResource ).iterator();
			if( !this.currentIterator.hasNext() ) {
				this.currentIterator = getIteratorForExternalChildren();
			} else {
				this.forJoint.getDeclaredChildren( this.forResource );
			}
		}

		private Iterator<JointId> getIteratorForExternalChildren() {
			Iterator<JointId> externalIterator = null;
			while( ( currentClass != null ) && ( externalIterator == null ) ) {
				List<JointId> jointList = JointId.getChildList( currentClass, forJoint );
				if( jointList != null ) {
					externalIterator = jointList.iterator();
				}

				Class<?>[] interfaces = currentClass.getInterfaces();
				Class<?> superClass = null;
				if( interfaces.length > 0 ) {
					superClass = interfaces[ 0 ];
				} else {
					superClass = currentClass.getSuperclass();
				}
				if( JointedModelResource.class.isAssignableFrom( superClass ) ) {
					currentClass = (Class<? extends JointedModelResource>)superClass;
				} else {
					currentClass = null;
				}
			}
			return externalIterator;
		}

		@Override
		public boolean hasNext() {
			if( currentIterator != null ) {
				return currentIterator.hasNext();
			}
			return false;
		}

		@Override
		public JointId next() {
			if( currentIterator != null ) {
				JointId next = currentIterator.next();
				if( !currentIterator.hasNext() ) {
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

	private static class ExternalChildrenIterable implements Iterable<JointId> {
		private final Class<? extends JointedModelResource> forClass;
		private final JointId forJoint;
		private final JointedModelResource forResource;

		public ExternalChildrenIterable( Class<? extends JointedModelResource> forClass, JointedModelResource forResource, JointId forJoint ) {
			this.forClass = forClass;
			this.forJoint = forJoint;
			this.forResource = forResource;
		}

		@Override
		public Iterator<JointId> iterator() {
			return new ExternalChildrenIterator( this.forClass, this.forResource, this.forJoint );
		}

	}

	private final JointId parent;
	private final Class<? extends JointedModelResource> containingClass;
	private final JointedModelResource resourceReference;
	private final List<JointId> children = Lists.newLinkedList();
	private final Map<JointedModelResource, List<JointId>> childrenMap = Maps.newHashMap();
	private Field fld;

	public JointId( JointId parent, Class<? extends JointedModelResource> containingClass, JointedModelResource resourceReference ) {
		this.parent = parent;
		this.containingClass = containingClass;
		this.resourceReference = resourceReference;
		//Initialize the childrenMap such that there's an empty list to be gotten
		this.childrenMap.put( resourceReference, new ArrayList<JointId>() );
		if( this.parent != null ) {
			if( this.parent.getContainingClass() == this.getContainingClass() ) {
				this.parent.addChild( this.resourceReference, this );
			} else {
				JointId.addExternalChild( parent, this );
			}
		}
	}

	public JointId( JointId parent, Class<? extends JointedModelResource> containingClass ) {
		this( parent, containingClass, null );
	}

	public JointId getParent() {
		return this.parent;
	}

	private void addChild( JointedModelResource resource, JointId child ) {
		List<JointId> childList = null;
		if( this.childrenMap.containsKey( resource ) ) {
			childList = this.childrenMap.get( resource );
		} else if( this.childrenMap.containsKey( null ) ) {
			//Joints that are in the "null" resource child map are the base joints for this resource and are part of all resources in this group
			List<JointId> baseChildList = this.childrenMap.get( null );
			childList = Lists.newArrayList( baseChildList );
			this.childrenMap.put( resource, childList );
		} else {
			childList = Lists.newArrayList();
			this.childrenMap.put( resource, childList );
		}
		childList.add( child );
	}

	protected Class<? extends JointedModelResource> getContainingClass() {
		return this.containingClass;
	}

	protected JointedModelResource getResourceReference() {
		return this.resourceReference;
	}

	public Field getPublicStaticFinalFld() {
		if( this.fld != null ) {
			//pass
		} else {
			for( Field fld : this.containingClass.getFields() ) {
				int modidiers = fld.getModifiers();
				if( Modifier.isPublic( modidiers ) ) {
					if( Modifier.isStatic( modidiers ) ) {
						if( Modifier.isFinal( modidiers ) ) {
							try {
								Object o = fld.get( null );
								if( o == this ) {
									this.fld = fld;
									break;
								}
							} catch( IllegalAccessException iae ) {
								Logger.throwable( iae, fld );
							}
						}
					}
				}
			}
		}
		return this.fld;
	}

	public Iterable<JointId> getDeclaredChildren( JointedModelResource forResource ) {
		if( !this.childrenMap.containsKey( forResource ) ) {
			return this.childrenMap.get( null );
		}
		return this.childrenMap.get( forResource );
	}

	public Iterable<JointId> getChildren( JointedModelResource resource ) {
		return new ExternalChildrenIterable( resource.getClass(), resource, this );
	}

	public Iterable<JointId> getChildren( Class<? extends JointedModelResource> resourceClass ) {
		return new ExternalChildrenIterable( resourceClass, null, this );
	}

	@Override
	public String toString() {
		Field fld = this.getPublicStaticFinalFld();
		if( fld != null ) {
			return fld.getName();
		} else {
			return super.toString();
		}
	}
}
