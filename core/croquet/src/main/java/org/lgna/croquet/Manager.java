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
package org.lgna.croquet;

import org.lgna.croquet.views.ComponentManager;

/**
 * @author Dennis Cosgrove
 */
public class Manager {
	private static java.util.Map<java.util.UUID, java.util.Set<Model>> mapIdToModels = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	private static java.util.Set<Model> lookupModels( java.util.UUID id ) {
		synchronized( mapIdToModels ) {
			return mapIdToModels.get( id );
		}
	}

	@Deprecated
	public static Model findFirstAppropriateModel( java.util.UUID id ) {
		java.util.Set<Model> models = lookupModels( id );
		for( Model model : models ) {
			java.util.Queue<org.lgna.croquet.views.SwingComponentView<?>> components = ComponentManager.getComponents( model );
			for( org.lgna.croquet.views.SwingComponentView<?> component : components ) {
				if( component.getAwtComponent().isShowing() ) {
					return model;
				}
			}
			for( org.lgna.croquet.views.SwingComponentView<?> component : components ) {
				if( component.getAwtComponent().isVisible() ) {
					return model;
				}
			}
		}
		return null;
	}

	private static java.util.List<Composite> composites = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();

	/* package-private */static void registerComposite( Composite composite ) {
		composites.add( composite );
	}

	/* package-private */static void unregisterComposite( Composite composite ) {
		composites.remove( composite );
	}

	public Iterable<Composite> getComposites() {
		return composites;
	}

	public static void registerModel( Model model ) {
		java.util.UUID id = model.getMigrationId();
		synchronized( mapIdToModels ) {
			java.util.Set<Model> set = mapIdToModels.get( id );
			if( set != null ) {
				//pass
			} else {
				set = edu.cmu.cs.dennisc.java.util.Sets.newHashSet();
				mapIdToModels.put( id, set );
			}
			set.add( model );
		}
	}

	public static void unregisterModel( Model model ) {
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "unregister:", model );
		java.util.UUID id = model.getMigrationId();
		synchronized( mapIdToModels ) {
			java.util.Set<Model> set = mapIdToModels.get( id );
			if( set != null ) {
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "pre set size:", set.size() );
				set.remove( model );
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "post set size:", set.size() );
				if( set.size() == 0 ) {
					mapIdToModels.remove( id );
				}
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "investigate set == null" );
			}
		}
	}

	public static <M extends Model> Iterable<M> getRegisteredModels( Class<M> cls ) {
		java.util.List<M> rv = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		for( java.util.Set<Model> set : mapIdToModels.values() ) {
			for( Model model : set ) {
				if( cls.isAssignableFrom( model.getClass() ) ) {
					rv.add( cls.cast( model ) );
				}
			}
		}
		return rv;
	}

	/* package-private */static void relocalizeAllElements() {
		synchronized( mapIdToModels ) {
			java.util.Collection<java.util.Set<Model>> sets = mapIdToModels.values();
			for( java.util.Set<Model> set : sets ) {
				for( Model model : set ) {
					model.relocalize();
					//					for( JComponent<?> component : model.getComponents() ) {
					//					}
				}
			}
		}
	}

	private static int isUndoOrRedoCount = 0;

	public static boolean isInTheMidstOfUndoOrRedo() {
		return isUndoOrRedoCount > 0;
	}

	public static void pushUndoOrRedo() {
		isUndoOrRedoCount++;
	}

	public static void popUndoOrRedo() {
		isUndoOrRedoCount--;
	}
}
