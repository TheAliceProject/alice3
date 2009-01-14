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
package sceneeditor;

import java.awt.Point;

/**
 * @author David Culyba
 */
public class CameraTranslateManipulator extends DragManipulator {

	@Override
	protected void endManipulator( Point current ) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void startManipulator( Point current ) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateManipulator( Point current, int xDeltaSincePrevious, int yDeltaSincePrevious  ) {
		
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 transform = this.manipulatedTransformable.localTransformation.getValue();
		transform.applyTranslation( xDeltaSincePrevious*.05d, yDeltaSincePrevious*-.05d, 0.0d);
		this.manipulatedTransformable.setLocalTransformation( transform );
	}

}
