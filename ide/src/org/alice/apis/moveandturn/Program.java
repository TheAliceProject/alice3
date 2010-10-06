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

package org.alice.apis.moveandturn;

/**
 * @author Dennis Cosgrove
 */
public abstract class Program extends edu.cmu.cs.dennisc.animation.Program implements SceneOwner {
	private Scene m_scene = null;
//	private java.util.List<org.alice.apis.moveandturn.graphic.Graphic> m_liveGraphics = new java.util.LinkedList<org.alice.apis.moveandturn.graphic.Graphic>();
//	private java.util.List<org.alice.apis.moveandturn.graphic.Graphic> m_finishedGraphics = new java.util.LinkedList<org.alice.apis.moveandturn.graphic.Graphic>();
	private double m_simulationSpeedFactor = 1;
	private boolean m_isPreInitialized = false;

//	private edu.cmu.cs.dennisc.lookingglass.event.LookingGlassListener m_lookingGlassListener = new edu.cmu.cs.dennisc.lookingglass.event.LookingGlassAdapter() {
//		@Override
//		public void rendered( edu.cmu.cs.dennisc.lookingglass.event.LookingGlassRenderEvent e ) {
//			super.rendered( e );
//			edu.cmu.cs.dennisc.lookingglass.Graphics2D g2 = e.getGraphics2D();
//
//			edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass = e.getTypedSource();
//			if( lookingGlass.getCameraCount() > 0 ) {
//				edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = lookingGlass.getCameraAt( 0 );
//				assert sgCamera != null;
//				synchronized( m_liveGraphics ) {
//					for( org.alice.apis.moveandturn.graphic.Graphic graphic : m_liveGraphics ) {
//						graphic.paint( g2, lookingGlass, sgCamera );
//					}
//				}
//			}
//			synchronized( m_finishedGraphics ) {
//				for( org.alice.apis.moveandturn.graphic.Graphic graphic : m_finishedGraphics ) {
//					graphic.forgetIfNecessary( g2 );
//				}
//				m_finishedGraphics.clear();
//			}
//		}
//	};
	
	@Override
	protected void preInitialize() {
		super.preInitialize();
		m_simulationSpeedFactor = Double.POSITIVE_INFINITY;
//		getOnscreenLookingGlass().addLookingGlassListener( m_lookingGlassListener );
		m_isPreInitialized = true;
		if( m_scene != null ) {
			m_scene.setOwner( this );
		}
	}
	@Override
	protected void postInitialize( boolean success ) {
		super.postInitialize( success );
		//Model.reportAnyInstancesWithVehicleSetToNull();
		m_simulationSpeedFactor = 1;
	}

	public Double getSimulationSpeedFactor() {
		return m_simulationSpeedFactor;
	}
	public void setSimulationSpeedFactor( Number simulationSpeedFactor ) {
		m_simulationSpeedFactor = simulationSpeedFactor.doubleValue();
	}
	
//	private java.util.List< edu.cmu.cs.dennisc.media.Player > startedPlayers = new java.util.LinkedList< edu.cmu.cs.dennisc.media.Player >();
//	public void playToCompletion( edu.cmu.cs.dennisc.media.Player player ) {
//		if( this.isClosed() ) {
//			throw new edu.cmu.cs.dennisc.program.ProgramClosedException();
//		}
//		synchronized( this.startedPlayers ) {
//			this.startedPlayers.add( player );
//			player.playToCompletion();
//		}
//	}
//	@Override
//	protected void handleWindowClosed( java.awt.event.WindowEvent e ) {
//		super.handleWindowClosed( e );
//		synchronized( this.startedPlayers ) {
//			for( edu.cmu.cs.dennisc.media.Player player : this.startedPlayers ) {
//				player.stop();
//			}
//			this.startedPlayers.clear();
//		}
//	}
	
//	public void addGraphicToOverlay( org.alice.apis.moveandturn.graphic.Graphic graphic ) {
//		synchronized( m_liveGraphics ) {
//			m_liveGraphics.add( graphic );
//		}
//	}
//	public void removeGraphicFromOverlay( org.alice.apis.moveandturn.graphic.Graphic graphic ) {
//		synchronized( m_liveGraphics ) {
//			m_liveGraphics.remove( graphic );
//		}
//		synchronized( m_finishedGraphics ) {
//			m_finishedGraphics.add( graphic );
//		}
//	}

	//todo
	public Scene getScene() {
		return m_scene;
	}
	public void setScene( Scene scene ) {
		if( m_scene != scene ) {
			if( m_scene != null ) {
				m_scene.setOwner( null );
			}
			m_scene = scene;
			if( m_isPreInitialized ) {
				if( m_scene != null ) {
					m_scene.setOwner( this );
				}
			}
		}
	}
//	/*package-private*/ void addBubbleToOverlay( Bubble bubble ) {
//		org.alice.lookingglass.LookingGlass lookingGlass = getOnscreenLookingGlass();
//		Camera camera = (Camera)lookingGlass.getCameraAt( 0 ).getBonusDataFor( AliceObject.ALICE_OBJECT_KEY );
//		assert camera != null;
//		bubble.set( camera, lookingGlass );
//		synchronized( m_bubbles ) {
//			m_bubbles.add( bubble );
//		}
//	}
//	/*package proteced*/ void removeBubbleFromOverlay( Bubble bubble ) {
//		synchronized( m_bubbles ) {
//			m_bubbles.remove( bubble );
//		}
//	}
	
	@Override
	protected boolean isRestartSupported() {
		return false;
	}
}
