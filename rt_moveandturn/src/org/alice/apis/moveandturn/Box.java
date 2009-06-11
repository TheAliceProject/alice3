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
package org.alice.apis.moveandturn;

/**
 * @author Dennis Cosgrove
 */
public class Box extends Model {
	private edu.cmu.cs.dennisc.scenegraph.Box m_sgBox = new edu.cmu.cs.dennisc.scenegraph.Box();
	@Override
	protected void createSGGeometryIfNecessary() {
	}
	@Override
	protected edu.cmu.cs.dennisc.scenegraph.Geometry getSGGeometry() {
		return m_sgBox;
	}

	public abstract class Distance {
		protected double m_value;
		public Distance( Double value ) {
			m_value = value;
		}
	}
	public abstract class DistanceRightOrLeft extends Distance {
		public DistanceRightOrLeft( Double value ) {
			super( value );
		}
		public Double getAsDistanceRight() {
			return m_value;
		}
		public Double getAsDistanceLeft() {
			return -m_value;
		}
	}
	public class DistanceRight extends DistanceRightOrLeft {
		public DistanceRight( Double distance ) {
			super( distance );
		}
	}
	public class DistanceLeft extends DistanceRightOrLeft {
		public DistanceLeft( Double distance ) {
			super( -distance );
		}
	}

	public abstract class DistanceUpOrDown extends Distance {
		public DistanceUpOrDown( Double value ) {
			super( value );
		}
		public Double getAsDistanceUp() {
			return m_value;
		}
		public Double getAsDistanceDown() {
			return -m_value;
		}
	}
	public class DistanceUp extends DistanceUpOrDown {
		public DistanceUp( Double distance ) {
			super( distance );
		}
	}
	public class DistanceDown extends DistanceUpOrDown {
		public DistanceDown( Double distance ) {
			super( -distance );
		}
	}
	
	public abstract class DistanceBackwardOrForward extends Distance {
		public DistanceBackwardOrForward( Double value ) {
			super( value );
		}
		public Double getAsDistanceBackward() {
			return m_value;
		}
		public Double getAsDistanceForward() {
			return -m_value;
		}
	}
	public class DistanceBackward extends DistanceBackwardOrForward {
		public DistanceBackward( Double distance ) {
			super( distance );
		}
	}
	public class DistanceForward extends DistanceBackwardOrForward {
		public DistanceForward( Double distance ) {
			super( -distance );
		}
	}

	public DistanceRightOrLeft getLeft() {
		return new DistanceRight( m_sgBox.xMinimum.getValue() );
	}
	public void setLeft( DistanceRightOrLeft value, Number duration, Style style ) {
		perform( new edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation( adjustDurationIfNecessary( duration ), style, getLeft().getAsDistanceRight(), value.getAsDistanceRight() ) {
			@Override
			protected void updateValue( Double d ) {
				m_sgBox.xMinimum.setValue( d );
			}
		} );
	}
	public void setLeft( DistanceRightOrLeft value, Number duration ) {
		setLeft( value, duration, DEFAULT_STYLE );
	}
	public void setLeft( DistanceRightOrLeft value ) {
		setLeft( value, DEFAULT_DURATION );
	}

	public DistanceRightOrLeft getRight() {
		return new DistanceRight( m_sgBox.xMaximum.getValue() );
	}
	public void setRight( DistanceRightOrLeft value, Number duration, Style style ) {
		perform( new edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation( adjustDurationIfNecessary( duration ), style, getRight().getAsDistanceRight(), value.getAsDistanceRight() ) {
			@Override
			protected void updateValue( Double d ) {
				m_sgBox.xMaximum.setValue( d );
			}
		} );
	}
	public void setRight( DistanceRightOrLeft value, Number duration ) {
		setRight( value, duration, DEFAULT_STYLE );
	}
	public void setRight( DistanceRightOrLeft value ) {
		setRight( value, DEFAULT_DURATION );
	}

	public DistanceUpOrDown getBottom() {
		return new DistanceUp( m_sgBox.yMinimum.getValue() );
	}
	public void setBottom( DistanceUpOrDown value, Number duration, Style style ) {
		perform( new edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation( adjustDurationIfNecessary( duration ), style, getBottom().getAsDistanceUp(), value.getAsDistanceUp() ) {
			@Override
			protected void updateValue( Double d ) {
				m_sgBox.yMinimum.setValue( d );
			}
		} );
	}
	public void setBottom( DistanceUpOrDown value, Number duration ) {
		setBottom( value, duration, DEFAULT_STYLE );
	}
	public void setBottom( DistanceUpOrDown value ) {
		setBottom( value, DEFAULT_DURATION );
	}

	public DistanceUpOrDown getTop() {
		return new DistanceUp( m_sgBox.yMaximum.getValue() );
	}
	public void setTop( DistanceUpOrDown value, Number duration, Style style ) {
		perform( new edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation( adjustDurationIfNecessary( duration ), style, getTop().getAsDistanceUp(), value.getAsDistanceUp() ) {
			@Override
			protected void updateValue( Double d ) {
				m_sgBox.yMaximum.setValue( d );
			}
		} );
	}
	public void setTop( DistanceUpOrDown value, Number duration ) {
		setTop( value, duration, DEFAULT_STYLE );
	}
	public void setTop( DistanceUpOrDown value ) {
		setTop( value, DEFAULT_DURATION );
	}

	public DistanceBackwardOrForward getFront() {
		return new DistanceBackward( m_sgBox.zMinimum.getValue() );
	}
	public void setFront( DistanceBackwardOrForward value, Number duration, Style style ) {
		perform( new edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation( adjustDurationIfNecessary( duration ), style, getFront().getAsDistanceBackward(), value.getAsDistanceBackward() ) {
			@Override
			protected void updateValue( Double d ) {
				m_sgBox.zMinimum.setValue( d );
			}
		} );
	}
	public void setFront( DistanceBackwardOrForward value, Number duration ) {
		setFront( value, duration, DEFAULT_STYLE );
	}
	public void setFront( DistanceBackwardOrForward value ) {
		setFront( value, DEFAULT_DURATION );
	}

	public DistanceBackwardOrForward getBack() {
		return new DistanceBackward( m_sgBox.zMaximum.getValue() );
	}
	public void setBack( DistanceBackwardOrForward value, Number duration, Style style ) {
		perform( new edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation( adjustDurationIfNecessary( duration ), style, getBack().getAsDistanceBackward(), value.getAsDistanceBackward() ) {
			@Override
			protected void updateValue( Double d ) {
				m_sgBox.zMaximum.setValue( d );
			}
		} );
	}
	public void setBack( DistanceBackwardOrForward value, Number duration ) {
		setBack( value, duration, DEFAULT_STYLE );
	}
	public void setBack( DistanceBackwardOrForward value ) {
		setBack( value, DEFAULT_DURATION );
	}
}
