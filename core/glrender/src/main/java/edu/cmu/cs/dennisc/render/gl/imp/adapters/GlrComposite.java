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

package edu.cmu.cs.dennisc.render.gl.imp.adapters;

import edu.cmu.cs.dennisc.render.gl.imp.PickContext;
import edu.cmu.cs.dennisc.render.gl.imp.PickParameters;
import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;

/**
 * @author Dennis Cosgrove
 */
public abstract class GlrComposite<T extends edu.cmu.cs.dennisc.scenegraph.Composite> extends GlrComponent<T> {
	/*package-private*/static void handleComponentAdded( edu.cmu.cs.dennisc.scenegraph.event.ComponentAddedEvent e ) {
		GlrComposite<?> glrComposite = AdapterFactory.getAdapterFor( e.getTypedSource() );
		GlrComponent<?> glrChild = AdapterFactory.getAdapterFor( e.getChild() );
		glrComposite.handleComponentAdded( glrChild );
	}

	/*package-private*/static void handleComponentRemoved( edu.cmu.cs.dennisc.scenegraph.event.ComponentRemovedEvent e ) {
		GlrComposite<?> glrComposite = AdapterFactory.getAdapterFor( e.getTypedSource() );
		GlrComponent<?> glrChild = AdapterFactory.getAdapterFor( e.getChild() );
		glrComposite.handleComponentRemoved( glrChild );
	}

	@Override
	public void accept( edu.cmu.cs.dennisc.pattern.Visitor visitor ) {
		super.accept( visitor );
		synchronized( this.glrChildren ) {
			for( GlrComponent<?> glrChild : this.glrChildren ) {
				glrChild.accept( visitor );
			}
		}
	}

	private void handleComponentAdded( GlrComponent<?> childAdapter ) {
		synchronized( this.glrChildren ) {
			this.glrChildren.add( childAdapter );
		}
	}

	private void handleComponentRemoved( GlrComponent<?> childAdapter ) {
		synchronized( this.glrChildren ) {
			this.glrChildren.remove( childAdapter );
		}
	}

	@Override
	public void initialize( T sgE ) {
		super.initialize( sgE );
		Iterable<edu.cmu.cs.dennisc.scenegraph.Component> sgChildren = this.owner.getComponents();
		synchronized( sgChildren ) {
			for( edu.cmu.cs.dennisc.scenegraph.Component sgChild : sgChildren ) {
				GlrComponent<?> glrComponent = AdapterFactory.getAdapterFor( sgChild );
				this.handleComponentAdded( glrComponent );
			}
		}
	}

	public void setupAffectors( RenderContext rc ) {
		synchronized( this.glrChildren ) {
			for( GlrComponent<?> glrChild : this.glrChildren ) {
				if( glrChild instanceof GlrComposite<?> ) {
					GlrComposite<?> glrComposite = (GlrComposite<?>)glrChild;
					glrComposite.setupAffectors( rc );
				} else if( glrChild instanceof GlrAffector<?> ) {
					GlrAffector<?> glrAffector = (GlrAffector<?>)glrChild;
					glrAffector.setupAffectors( rc );
				}
			}
		}
	}

	public void renderGhost( RenderContext rc, GlrGhost root ) {
		synchronized( this.glrChildren ) {
			for( GlrComponent<?> glrChild : this.glrChildren ) {
				if( glrChild instanceof GlrComposite<?> ) {
					GlrComposite<?> glrComposite = (GlrComposite<?>)glrChild;
					glrComposite.renderGhost( rc, root );
				} else if( glrChild instanceof GlrRenderContributor ) {
					GlrRenderContributor glrRenderContributor = (GlrRenderContributor)glrChild;
					glrRenderContributor.renderGhost( rc, root );
				}
			}
		}
	}

	public void renderOpaque( RenderContext rc ) {
		synchronized( this.glrChildren ) {
			for( GlrComponent<?> glrChild : this.glrChildren ) {
				if( glrChild instanceof GlrComposite<?> ) {
					GlrComposite<?> glrComposite = (GlrComposite<?>)glrChild;
					glrComposite.renderOpaque( rc );
				} else if( glrChild instanceof GlrRenderContributor ) {
					GlrRenderContributor glrRenderContributor = (GlrRenderContributor)glrChild;
					glrRenderContributor.renderOpaque( rc );
				}
			}
		}
	}

	public void pick( PickContext pc, PickParameters pickParameters ) {
		synchronized( this.glrChildren ) {
			for( GlrComponent<?> glrChild : this.glrChildren ) {
				if( glrChild instanceof GlrComposite<?> ) {
					GlrComposite<?> glrComposite = (GlrComposite<?>)glrChild;
					glrComposite.pick( pc, pickParameters );
				} else if( glrChild instanceof GlrRenderContributor ) {
					GlrRenderContributor glrRenderContributor = (GlrRenderContributor)glrChild;
					glrRenderContributor.pick( pc, pickParameters );
				}
			}
		}
	}

	public Iterable<GlrComponent<?>> accessChildren() {
		return this.glrChildren;
	}

	private final java.util.List<GlrComponent<?>> glrChildren = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
}
