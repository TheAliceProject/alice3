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
	@Override
	protected void run() {
	}
}
