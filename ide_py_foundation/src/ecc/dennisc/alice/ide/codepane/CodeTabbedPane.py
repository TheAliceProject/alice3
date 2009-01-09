#print "-->", __name__

import java
import javax
import edu

from edu.cmu.cs.dennisc import alice

import ecc

class CodeTabbedPane( edu.cmu.cs.dennisc.swing.TabbedPane ):
	def __init__( self ):
		edu.cmu.cs.dennisc.swing.TabbedPane.__init__( self, False )
		self._blockStatementViews = None
		self._potentialReceptors = None
		self._mapCodeToTab = {}
		
#		#timerThread = TimerThread( self._updateBlockStatementViews )
#		#timerThread.start()
#		
#		#edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().addAutomaticDisplayListener( DisplayListener( self._updateBlockStatementViews ) )
#		DELAY_IN_MSEC = 25
#		self._timer = javax.swing.Timer( DELAY_IN_MSEC, ecc.dennisc.swing.event.ActionAdapter( self._handleTimer ) )
#		self._timer.start()

	def addScrollableTab( self, title, pane, isSelectionDesired ):
		color = pane.getBackground()

		scrollPane = javax.swing.JScrollPane( pane )
		scrollPane.setBackground( color )
		scrollPane.setBorder( None )
		
		scrollPane.getVerticalScrollBar().setUnitIncrement( 12 )
		
		tab = edu.cmu.cs.dennisc.swing.Tab( title, scrollPane )
		self.addTab( tab )
		if isSelectionDesired:
			self._doNotSetIDE = True
			self.selectTab( tab )
			del self._doNotSetIDE
		return tab
	
	def getPaneForTab(self, tab):
		return tab.getTabPane().getViewport().getView()


	def _edit( self, code ):
		if self._mapCodeToTab.has_key( code ):
			tab = self._mapCodeToTab[ code ]
			self.selectTab( tab )
		else:
			from CodePane import CodePane
			codePane = CodePane( code )
			titlePane = alice.ide.editors.code.CodeTitlePane( code )
			tab = self.addScrollableTab( titlePane, codePane, isSelectionDesired=True )
			tab.setClosable( self.getTabCount() > 1 )
			tab.setBackground( codePane.getBackground() )
			self._mapCodeToTab[ code ] = tab
		self.revalidate()

	def tabSelected(self, e):
		edu.cmu.cs.dennisc.swing.TabbedPane.tabSelected( self, e )
		if self.__dict__.has_key( "_doNotSetIDE" ):
			pass
		else:
			codePane = self.getPaneForTab( e.getSource() )
			code = codePane.getCode()
			self._doNotSetIDE = True
			alice.ide.IDE.getSingleton().setFocusedCode( code )
			del self._doNotSetIDE
		
	def tabClosed( self, e ):
		edu.cmu.cs.dennisc.swing.TabbedPane.tabClosed( self, e )
		codePane = self.getPaneForTab( e.getSource() )
		code = codePane.getCode()
		del self._mapCodeToTab[ code ]

	#todo: investogate if necessary
	def clearTabs( self ):
		edu.cmu.cs.dennisc.swing.TabbedPane.clearTabs( self )
		self._mapCodeToTab.clear()

#print "<--", __name__
