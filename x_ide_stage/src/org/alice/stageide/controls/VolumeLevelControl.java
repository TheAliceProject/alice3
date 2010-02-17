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
package org.alice.stageide.controls;

/**
 * @author Dennis Cosgrove
 */
public class VolumeLevelControl extends javax.swing.JSlider {
	public VolumeLevelControl() {
		this.setValue( 100 );
		this.setMaximum( 200 );

		java.util.Dictionary<Integer, javax.swing.JComponent> labels = new java.util.Hashtable<Integer, javax.swing.JComponent>();
		labels.put( 0, edu.cmu.cs.dennisc.zoot.ZLabel.acquire( "Silent (0.0)" ) );
		labels.put( 100, edu.cmu.cs.dennisc.zoot.ZLabel.acquire( "Normal (1.0)" ) );
		labels.put( 200, edu.cmu.cs.dennisc.zoot.ZLabel.acquire( "Louder (2.0)" ) );
		this.setLabelTable( labels );
		this.setPaintLabels( true );
		this.setOrientation( javax.swing.SwingConstants.VERTICAL );

		this.setSnapToTicks( true );
		this.setMinorTickSpacing( 10 );
		this.setMajorTickSpacing( 100 );
		this.setPaintTicks( true );
	}
	
	public double getVolumeLevel() {
		return this.getValue() / 100.0;
	}
	public void setVolumeLevel( double volumeLevel ) {
		this.setValue( (int)(volumeLevel*100+0.5) );
	}
}
