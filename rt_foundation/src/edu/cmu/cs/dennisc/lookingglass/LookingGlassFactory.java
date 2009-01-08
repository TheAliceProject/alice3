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

package edu.cmu.cs.dennisc.lookingglass;

/**
 * @author Dennis Cosgrove
 */
public interface LookingGlassFactory {
	public void acquireRenderingLock();
	public void releaseRenderingLock();

	public LightweightOnscreenLookingGlass createLightweightOnscreenLookingGlass();
	public HeavyweightOnscreenLookingGlass createHeavyweightOnscreenLookingGlass();
	public OffscreenLookingGlass createOffscreenLookingGlass( LookingGlass lookingGlassToShareContextWith );
	
	public void addAutomaticDisplayListener( edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener automaticDisplayListener );
	public void removeAutomaticDisplayListener( edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener automaticDisplayListener );
	public Iterable< edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener > accessAutomaticDisplayListeners();

	//todo:
	public Iterable< LightweightOnscreenLookingGlass > accessLightweightOnscreenLookingGlasses();
	public Iterable< HeavyweightOnscreenLookingGlass > accessHeavyweightOnscreenLookingGlasses();
	public Iterable< OffscreenLookingGlass > accessOffscreenLookingGlasses();

	public int getAutomaticDisplayCount();
	public void incrementAutomaticDisplayCount();
	public void decrementAutomaticDisplayCount();
	
	public void invokeLater( Runnable runnable );
	public void invokeAndWait( Runnable runnable ) throws InterruptedException, java.lang.reflect.InvocationTargetException;
	public void invokeAndWait_ThrowRuntimeExceptionsIfNecessary( Runnable runnable );
}
