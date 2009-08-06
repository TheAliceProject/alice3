/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.stageide.sceneeditor;

/**
 * @author Dennis Cosgrove
 */
public class Program extends org.alice.apis.moveandturn.Program {
	private MoveAndTurnSceneEditor moveAndTurnSceneEditor;
	public Program( MoveAndTurnSceneEditor moveAndTurnSceneEditor ) {
		this.moveAndTurnSceneEditor = moveAndTurnSceneEditor;
	}
	@Override
	protected boolean isLightweightOnscreenLookingGlassDesired() {
		return true;
	}
	@Override
	protected java.awt.Component createSpeedMultiplierControlPanel() {
		return null;
	}
	@Override
	protected void initialize() {
		edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass = getOnscreenLookingGlass();
		if( onscreenLookingGlass instanceof edu.cmu.cs.dennisc.lookingglass.LightweightOnscreenLookingGlass ) {
			edu.cmu.cs.dennisc.lookingglass.LightweightOnscreenLookingGlass lightweightOnscreenLookingGlass = (edu.cmu.cs.dennisc.lookingglass.LightweightOnscreenLookingGlass)onscreenLookingGlass;
			this.moveAndTurnSceneEditor.initializeLightweightOnscreenLookingGlass( lightweightOnscreenLookingGlass );
		}
	}
//	@Override
//	protected void preRun() {
//		super.preRun();
//		this.getOnscreenLookingGlass().addLookingGlassListener( new edu.cmu.cs.dennisc.lookingglass.event.LookingGlassListener() {
//			public void cleared( edu.cmu.cs.dennisc.lookingglass.event.LookingGlassRenderEvent e ) {
//			}
//			public void displayChanged( edu.cmu.cs.dennisc.lookingglass.event.LookingGlassDisplayChangeEvent e ) {
//			}
//			public void initialized( edu.cmu.cs.dennisc.lookingglass.event.LookingGlassInitializeEvent e ) {
//			}
//			public void rendered( edu.cmu.cs.dennisc.lookingglass.event.LookingGlassRenderEvent e ) {
//				edu.cmu.cs.dennisc.lookingglass.opengl.Graphics2D g2 = (edu.cmu.cs.dennisc.lookingglass.opengl.Graphics2D)e.getGraphics2D();
//				javax.media.opengl.GL gl = g2.getGL();
//				
//				final int N = 64;
////				int[] textures = new int[ N ];
////				byte[] residences = new byte[ N ];
////				for( int i=0; i<N; i++ ) {
////					textures[ i ] = i;
////					residences[ i ] = 0xF;
////				}
////				boolean result = gl.glAreTexturesResident( N, java.nio.IntBuffer.wrap( textures ), java.nio.ByteBuffer.wrap( residences ) );
//				
//				StringBuffer sb = new StringBuffer( N );
//				for( int i=0; i<N; i++ ) {
//					if( gl.glIsTexture( i ) ) {
//						sb.append( '*' );
//						gl.glBindTexture( javax.media.opengl.GL.GL_TEXTURE_2D, i );
//						if( edu.cmu.cs.dennisc.lookingglass.opengl.GetUtilities.getTexParameterInteger( gl, javax.media.opengl.GL.GL_TEXTURE_2D, javax.media.opengl.GL.GL_TEXTURE_RESIDENT ) != 0 ) {
//							sb.append( '1' );
//						} else {
//							sb.append( '0' );
//						}
//					} else {
//						sb.append( '_' );
//						sb.append( '0' );
//					}
//				}
//				edu.cmu.cs.dennisc.print.PrintUtilities.println( sb.toString() );
////				edu.cmu.cs.dennisc.print.PrintUtilities.println( "binding:", edu.cmu.cs.dennisc.lookingglass.opengl.GetUtilities.getInteger( gl, javax.media.opengl.GL.GL_TEXTURE_BINDING_2D ) );
////				edu.cmu.cs.dennisc.print.PrintUtilities.println( "isResident:", edu.cmu.cs.dennisc.lookingglass.opengl.GetUtilities.getTexParameterInteger( gl, javax.media.opengl.GL.GL_TEXTURE_2D, javax.media.opengl.GL.GL_TEXTURE_RESIDENT ) );
//			}
//			public void resized( edu.cmu.cs.dennisc.lookingglass.event.LookingGlassResizeEvent e ) {
//			}
//		} );
//	}
	
	@Override
	protected void run() {
	}
}
