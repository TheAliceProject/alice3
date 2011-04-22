package autotutorial.ecard.ribbon;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

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
		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
		gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		gbc.weightx = 0.0;
		edu.cmu.cs.dennisc.croquet.GridBagPanel rv = new edu.cmu.cs.dennisc.croquet.GridBagPanel();
		rv.setBorder( BorderFactory.createEmptyBorder( 4,2,4,2 ) );
		rv.setBackgroundColor(java.awt.SystemColor.control);

		edu.cmu.cs.dennisc.croquet.Button button;
		javax.swing.JButton jButton;

//		LineAxisPanel rotatePanel = new LineAxisPanel();
//		rotatePanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
//		rv.addComponent(rotatePanel, gbc );
//		rotatePanel.setBorder( null );

		button = autotutorial.ecard.menu.RotateBackwardModel.getInstance().createButton();
		jButton = button.getAwtComponent();
//		jButton.setHorizontalTextPosition(SwingConstants.CENTER);
//		jButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		jButton.setIcon(new ImageIcon(getClass().getResource("/autotutorial/ecard/resources/ribbon/rotate-counter-clockwise.png") ));
		rv.addComponent( button, gbc );
//		rotatePanel.addComponent(button);

		button = autotutorial.ecard.menu.RotateForwardModel.getInstance().createButton();
		jButton = button.getAwtComponent();
//		jButton.setHorizontalTextPosition(SwingConstants.CENTER);
//		jButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		jButton.setIcon(new ImageIcon(getClass().getResource("/autotutorial/ecard/resources/ribbon/rotate-clockwise.png") ));
		rv.addComponent( button, gbc );
//		rotatePanel.addComponent(button);


		rv.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalSliver( 6 ), gbc  );
		edu.cmu.cs.dennisc.croquet.GridPanel effectsPanel = edu.cmu.cs.dennisc.croquet.GridPanel.createSingleColumnGridPane();
//		effectsPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		rv.addComponent(effectsPanel, gbc );

		button = autotutorial.ecard.menu.EllipseModel.getInstance().createButton();
		button.setHorizontalAlignment( edu.cmu.cs.dennisc.croquet.HorizontalAlignment.LEADING );
		effectsPanel.addComponent(button);

		button = autotutorial.ecard.menu.RoundedCornersModel.getInstance().createButton();
		button.setHorizontalAlignment( edu.cmu.cs.dennisc.croquet.HorizontalAlignment.LEADING );
		effectsPanel.addComponent(button);

		button = autotutorial.ecard.ThoughtBubbleEffectModel.getInstance().createButton();
		button.setHorizontalAlignment( edu.cmu.cs.dennisc.croquet.HorizontalAlignment.LEADING );
		effectsPanel.addComponent(button);
		effectsPanel.setBorder( null );
		
		rv.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalSliver( 6 ), gbc  );
//		LineAxisPanel shadowPanel = new LineAxisPanel();
//		shadowPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
//		rv.addComponent(shadowPanel, gbc );
//		shadowPanel.setBorder( null );

		button = autotutorial.ecard.menu.DropShadowModel.getInstance().createButton();
		jButton = button.getAwtComponent();
//		jButton.setHorizontalTextPosition(SwingConstants.CENTER);
//		jButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		jButton.setIcon(new ImageIcon(getClass().getResource("/autotutorial/ecard/resources/ribbon/drop-shadow.png") ));
//		shadowPanel.addComponent(button);
		rv.addComponent( button, gbc );

//		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		rv.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalGlue(), gbc  );
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