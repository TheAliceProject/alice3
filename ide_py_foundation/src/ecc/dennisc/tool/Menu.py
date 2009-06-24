import java
import javax

class MenuBar( javax.swing.JMenuBar ):
	def __init__( self ):
		javax.swing.JMenuBar.__init__( self )

class Menu( javax.swing.JMenu ):
	def __init__( self , text, mnemonic, *args ):
		javax.swing.JMenu.__init__( self, text )
		self.setMnemonic( mnemonic )
		for action in args:
			if action:
				self.add( action )
			else:
				self.addSeparator()
		self.getPopupMenu().setLightWeightPopupEnabled( False )

MenuSeparator = None
