/**
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
package org.alice.interact.handle;

/**
 * @author David Culyba
 */
public enum HandleRenderState {
		JUST_VISIBLE,
		VISIBLE_BUT_SIBLING_IS_ACTIVE,
		VISIBLE_AND_ACTIVE,
		NOT_VISIBLE,
		VISIBLE_AND_ROLLOVER;
		
		public static HandleRenderState getStateForHandle(ManipulationHandle handle)
		{
			boolean isSiblingActive = false;
			if (handle.getHandleManager() != null)
			{
				isSiblingActive = handle.getHandleManager().isASiblingActive( handle );
			}
			HandleState handleState = handle.getHandleStateCopy();
			if (handleState.isActive())
			{
				return HandleRenderState.VISIBLE_AND_ACTIVE;
			}
			else if (handleState.isRollover())
			{
				return HandleRenderState.VISIBLE_AND_ROLLOVER;
			}
			else if (handle.isAlwaysVisible())
			{
				return JUST_VISIBLE;
			}
			else if (handleState.isVisible())
			{
				if (isSiblingActive)
				{
					return VISIBLE_BUT_SIBLING_IS_ACTIVE;
				}
				else
				{
					return JUST_VISIBLE;
				}
			}
			else
			{
				return NOT_VISIBLE;
			}
		}
}
