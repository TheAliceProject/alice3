package org.alice.interact.operations;

import edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation;
import edu.cmu.cs.dennisc.math.ClippedZPlane;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;

public class PredeterminedSetOrthographicPicturePlaneActionOperation extends edu.cmu.cs.dennisc.zoot.AbstractActionOperation {
	private boolean isDoRequired;
	private edu.cmu.cs.dennisc.animation.Animator animator;
	private edu.cmu.cs.dennisc.scenegraph.OrthographicCamera sgCamera;
	private double previousPicturePlaneHeight;
	private double nextPicturePlaneHeight;
	
	private String editPresentationKey;
	public PredeterminedSetOrthographicPicturePlaneActionOperation( java.util.UUID groupUUID, boolean isDoRequired, edu.cmu.cs.dennisc.animation.Animator animator, edu.cmu.cs.dennisc.scenegraph.OrthographicCamera sgCamera, double previousPicturePlaneHeight, double nextPicturePlaneHeight, String editPresentationKey ) {
		super( groupUUID );
		this.isDoRequired = isDoRequired;
		this.animator = animator;
		this.sgCamera = sgCamera;

		this.previousPicturePlaneHeight = previousPicturePlaneHeight;
		this.nextPicturePlaneHeight = nextPicturePlaneHeight;
		
		this.editPresentationKey = editPresentationKey;
	}
	
	private void setHeightOnCamera(OrthographicCamera camera, double height)
	{
		ClippedZPlane picturePlane = PredeterminedSetOrthographicPicturePlaneActionOperation.this.sgCamera.picturePlane.getValue();
		picturePlane.setHeight(height);
		PredeterminedSetOrthographicPicturePlaneActionOperation.this.sgCamera.picturePlane.setValue(picturePlane);
	}
	
	private void setPicturePlaneHeight( final double height ) {
		if( this.animator != null ) {
			class ZoomAnimation extends DoubleAnimation {
				public ZoomAnimation() {
					super( 0.5, edu.cmu.cs.dennisc.animation.TraditionalStyle.BEGIN_AND_END_GENTLY, sgCamera.picturePlane.getValue().getHeight(), height);
				}
				@Override
				protected void updateValue( Double newHeight) {
					setHeightOnCamera(sgCamera, newHeight.doubleValue());
				}
			}
			this.animator.invokeLater( new ZoomAnimation(), null );
		} else 
		{
			setHeightOnCamera(sgCamera, height);
		}
		
	}
	public void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		actionContext.commitAndInvokeDo( new edu.cmu.cs.dennisc.zoot.AbstractEdit() {
			@Override
			public void doOrRedo( boolean isDo ) {
				if( isDo && ( isDoRequired == false ) ) {
					//pass
				} else {
					setPicturePlaneHeight( nextPicturePlaneHeight );
				}
			}
			@Override
			public void undo() {
				setPicturePlaneHeight( previousPicturePlaneHeight );
			}
			@Override
			protected StringBuffer updatePresentation(StringBuffer rv, java.util.Locale locale) {
				rv.append( editPresentationKey );
				return rv;
			}
		} );
	}
}