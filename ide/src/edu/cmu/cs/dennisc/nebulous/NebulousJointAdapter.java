package edu.cmu.cs.dennisc.nebulous;

import java.nio.DoubleBuffer;

import edu.cmu.cs.dennisc.lookingglass.opengl.AbstractTransformableAdapter;
import edu.cmu.cs.dennisc.lookingglass.opengl.ConformanceTestResults;
import edu.cmu.cs.dennisc.lookingglass.opengl.GhostAdapter;
import edu.cmu.cs.dennisc.lookingglass.opengl.PickContext;
import edu.cmu.cs.dennisc.lookingglass.opengl.PickParameters;
import edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext;

public class NebulousJointAdapter extends AbstractTransformableAdapter< edu.cmu.cs.dennisc.nebulous.NebulousJoint > {
	private double[] m_localTransformation = new double[ 16 ];
	private java.nio.DoubleBuffer m_localTransformationBuffer = java.nio.DoubleBuffer.wrap( m_localTransformation );
	private edu.cmu.cs.dennisc.math.AffineMatrix4x4 m_localTransformationMatrix = new edu.cmu.cs.dennisc.math.AffineMatrix4x4();
	
	private void updateLocalTransformation() {
		m_element.getLocalTransformation(m_localTransformationMatrix);
		this.m_localTransformationMatrix.getAsColumnMajorArray16( m_localTransformation );
	}
	@Override
	public double[] accessLocalTransformation() {
		this.updateLocalTransformation();
		return m_localTransformation;
	}

	@Override
	public DoubleBuffer accessLocalTransformationAsBuffer() {
		this.updateLocalTransformation();
		return this.m_localTransformationBuffer;
	}
	@Override
	public void renderOpaque( RenderContext rc ) {
		//todo
	}
	@Override
	public void renderGhost( RenderContext rc, GhostAdapter root ) {
		//todo
	}
	@Override
	public void pick( PickContext pc, PickParameters pickParameters, ConformanceTestResults conformanceTestResults ) {
		//todo
	}
}
