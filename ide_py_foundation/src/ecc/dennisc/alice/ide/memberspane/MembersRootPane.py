#print "-->", __name__

import java
import javax
import edu

import ecc

class MembersRootPane( ecc.dennisc.alice.ide.IDEListenerPane ):
	def __init__( self ):
		ecc.dennisc.alice.ide.IDEListenerPane.__init__( self )
		self._tabbedPane = self._createTabbedPane()
		self.setLayout( java.awt.GridLayout( 1, 1 ) )
		self.add( self._tabbedPane )
	def _createTabbedPane(self):
		from MembersTabbedPane import MembersTabbedPane
		return MembersTabbedPane()
	def _update(self):
		self._tabbedPane._update()
	def fieldSelectionChanged( self, e ):
		self._update()
	def focusedCodChanged( self, e ):
		self._update()

#print "<--", __name__
