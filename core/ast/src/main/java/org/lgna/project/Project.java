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
package org.lgna.project;

/**
 * @author Dennis Cosgrove
 */
public class Project {

	private final org.lgna.project.ast.NamedUserType programType;
	private final java.util.Set<org.lgna.common.Resource> resources = edu.cmu.cs.dennisc.java.util.Sets.newCopyOnWriteArraySet();
	private final java.util.Map/* < org.lgna.project.properties.PropertyKey< T >, T > */propertyMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private final java.util.Set<org.lgna.project.ast.NamedUserType> namedUserTypes = edu.cmu.cs.dennisc.java.util.Sets.newCopyOnWriteArraySet();

	private final java.util.List<org.lgna.project.event.ResourceListener> resourceListeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();

	private final Object lock = new Object();

	public Project( org.lgna.project.ast.NamedUserType programType, java.util.Set<org.lgna.project.ast.NamedUserType> namedUserTypes, java.util.Set<org.lgna.common.Resource> resources ) {
		this( programType );
		this.namedUserTypes.addAll( namedUserTypes );
		this.resources.addAll( resources );
	}

	public Project( org.lgna.project.ast.NamedUserType programType ) {
		this.programType = programType;
	}

	public Object getLock() {
		return this.lock;
	}

	public org.lgna.project.ast.NamedUserType getProgramType() {
		return this.programType;
	}

	public void addResourceListener( org.lgna.project.event.ResourceListener resourceListener ) {
		this.resourceListeners.add( resourceListener );
	}

	public void removeResourceListener( org.lgna.project.event.ResourceListener resourceListener ) {
		this.resourceListeners.remove( resourceListener );
	}

	public void addResource( org.lgna.common.Resource resource ) {
		if( this.resources.contains( resource ) ) {
			//todo
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( "already contains resource:", resource );
		} else {
			this.resources.add( resource );
			if( this.resourceListeners.size() > 0 ) {
				org.lgna.project.event.ResourceEvent e = new org.lgna.project.event.ResourceEvent( this, resource );
				for( org.lgna.project.event.ResourceListener resourceListener : this.resourceListeners ) {
					resourceListener.resourceAdded( e );
				}
			}
		}
	}

	public void removeResource( org.lgna.common.Resource resource ) {
		this.resources.remove( resource );
		if( this.resourceListeners.size() > 0 ) {
			org.lgna.project.event.ResourceEvent e = new org.lgna.project.event.ResourceEvent( this, resource );
			for( org.lgna.project.event.ResourceListener resourceListener : this.resourceListeners ) {
				resourceListener.resourceRemoved( e );
			}
		}
	}

	public java.util.Set<org.lgna.common.Resource> getResources() {
		return this.resources;
	}

	public java.util.Set<org.lgna.project.properties.PropertyKey<Object>> getPropertyKeys() {
		return this.propertyMap.keySet();
	}

	public <T> boolean containsValueFor( org.lgna.project.properties.PropertyKey<T> key ) {
		return this.propertyMap.containsKey( key );
	}

	public <T> T getValueFor( org.lgna.project.properties.PropertyKey<T> key ) {
		return (T)this.propertyMap.get( key );
	}

	public <T> void putValueFor( org.lgna.project.properties.PropertyKey<T> key, T value ) {
		this.propertyMap.put( key, value );
	}

	public <T> void removeValueFor( org.lgna.project.properties.PropertyKey<T> key ) {
		this.propertyMap.remove( key );
	}

	public void addNamedUserType( org.lgna.project.ast.NamedUserType namedUserType ) {
		if( this.namedUserTypes.contains( namedUserType ) ) {
			//todo
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( "already contains named user type:", namedUserType );
		} else {
			this.namedUserTypes.add( namedUserType );
		}
	}

	public void removeNamedUserType( org.lgna.project.ast.NamedUserType namedUserType ) {
		this.namedUserTypes.remove( namedUserType );
	}

	public java.util.Set<org.lgna.project.ast.NamedUserType> getNamedUserTypes() {
		synchronized( this.getLock() ) {
			this.namedUserTypes.addAll( org.lgna.project.ast.AstUtilities.getNamedUserTypes( this.programType ) );
			return this.namedUserTypes;
		}
	}
}
