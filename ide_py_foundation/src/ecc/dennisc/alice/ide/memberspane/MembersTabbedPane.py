#print "-->", __name__

import java
import javax
import edu

from edu.cmu.cs.dennisc import alice

from MembersTab import ProceduresTab, FunctionsTab, FieldsTab

class MembersTabbedPane( edu.cmu.cs.dennisc.swing.TabbedPane, alice.ide.event.IDEListener ):
	def __init__( self ):
		edu.cmu.cs.dennisc.swing.TabbedPane.__init__( self, True )
		self._tabs = [ ProceduresTab(), FunctionsTab(), FieldsTab() ]
#		isSelectionDesired=True
		for tab in self._tabs:
			self.addTab( tab )
#			if isSelectionDesired:
#				self.selectTab( tab )
#			isSelectionDesired = False
		self.selectTab( self._tabs[ 0 ] )

	def _update(self):
		for tab in self._tabs:
			tab._update()
		self.repaint()

#print "<--", __name__
