package autotutorial.ecard.ribbon;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import edu.cmu.cs.dennisc.croquet.LineAxisPanel;
import edu.cmu.cs.dennisc.croquet.PageAxisPanel;

public class PictureEffectsRibbonModel extends edu.cmu.cs.dennisc.croquet.PredeterminedTab {
	private static class SingletonHolder {
		private static PictureEffectsRibbonModel instance = new PictureEffectsRibbonModel();
	}
	public static PictureEffectsRibbonModel getInstance() {
		return SingletonHolder.instance;
	}
	private edu.cmu.cs.dennisc.croquet.Operation< ? >[] operations = new edu.cmu.cs.dennisc.croquet.Operation< ? >[] {
			autotutorial.ecard.menu.DropShadowModel.getInstance(),
			null,
			autotutorial.ecard.menu.RoundedCornersModel.getInstance(),
			autotutorial.ecard.ThoughtBubbleEffectModel.getInstance(),
			autotutorial.ecard.menu.EllipseModel.getInstance(),
			null,
			autotutorial.ecard.menu.RotateForwardModel.getInstance(),
			autotutorial.ecard.menu.RotateBackwardModel.getInstance()
	};
	private PictureEffectsRibbonModel() {
		super( java.util.UUID.fromString( "e0b095cb-1cba-443d-b320-c0fc4f7c3312" ) );
	}
	@Override
	protected edu.cmu.cs.dennisc.croquet.JComponent< ? > createMainComponent() {
		edu.cmu.cs.dennisc.croquet.LineAxisPanel rv = new edu.cmu.cs.dennisc.croquet.LineAxisPanel();

		edu.cmu.cs.dennisc.croquet.Button button;
		javax.swing.JButton jButton;

		LineAxisPanel rotatePanel = new LineAxisPanel();
		rotatePanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		rv.addComponent(rotatePanel);

		button = autotutorial.ecard.menu.RotateBackwardModel.getInstance().createButton();
		jButton = button.getAwtComponent();
		jButton.setHorizontalTextPosition(SwingConstants.CENTER);
		jButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		jButton.setIcon(new ImageIcon(getClass().getResource("/autotutorial/ecard/resources/ribbon/rotate-counter-clockwise.png") ));
		rotatePanel.addComponent(button);

		button = autotutorial.ecard.menu.RotateForwardModel.getInstance().createButton();
		jButton = button.getAwtComponent();
		jButton.setHorizontalTextPosition(SwingConstants.CENTER);
		jButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		jButton.setIcon(new ImageIcon(getClass().getResource("/autotutorial/ecard/resources/ribbon/rotate-clockwise.png") ));
		rotatePanel.addComponent(button);

		rv.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalSliver( 6 ) );
		PageAxisPanel effectsPanel = new PageAxisPanel();
		effectsPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		rv.addComponent(effectsPanel);

		effectsPanel.addComponent(autotutorial.ecard.menu.EllipseModel.getInstance().createButton());
		effectsPanel.addComponent(autotutorial.ecard.menu.RoundedCornersModel.getInstance().createButton());
		effectsPanel.addComponent(autotutorial.ecard.ThoughtBubbleEffectModel.getInstance().createButton());
		
		rv.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalSliver( 6 ) );
		LineAxisPanel shadowPanel = new LineAxisPanel();
		shadowPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		rv.addComponent(shadowPanel);

		button = autotutorial.ecard.menu.DropShadowModel.getInstance().createButton();
		jButton = button.getAwtComponent();
		jButton.setHorizontalTextPosition(SwingConstants.CENTER);
		jButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		jButton.setIcon(new ImageIcon(getClass().getResource("/autotutorial/ecard/resources/ribbon/drop-shadow.png") ));
		shadowPanel.addComponent(button);

		return rv;
	}
	@Override
	public boolean contains( edu.cmu.cs.dennisc.croquet.Model model ) {
		for( edu.cmu.cs.dennisc.croquet.Operation< ? > operation : this.operations ) {
			if( operation == model ) {
				return true;
			}
		}
		return false;
		//		return edu.cmu.cs.dennisc.java.util.Collections.newHashSet( this.operations ).contains( model );
	}
}