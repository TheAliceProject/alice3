#print "-->", __name__

import java
import javax

import ecc

class CodeRootPane( ecc.dennisc.alice.ide.IDEListenerPane, javax.swing.event.ChangeListener ):
	def __init__( self ):
		ecc.dennisc.alice.ide.IDEListenerPane.__init__( self )
		self._codeTabbedPane = self._createCodeTabbedPane()
		self._codeTabbedPane.addChangeListener( self )
		self.setLayout( java.awt.BorderLayout() )
		self.add( self._codeTabbedPane, java.awt.BorderLayout.CENTER )
		#self.add( southPane, java.awt.BorderLayout.SOUTH )

	
	def stateChanged(self, e):
		codeEditor = self._codeTabbedPane.getSelectedCodeEditor()
		if codeEditor:
			code = codeEditor.getCode()
		else:
			code = None
		self.getIDE().setFocusedCode( code )

	def getCodeEditorInFocus( self ):
		return self._codeTabbedPane.getSelectedCodeEditor()
	
	def _createCodeTabbedPane( self ):
		from CodeTabbedPane import CodeTabbedPane
		return CodeTabbedPane()
	
	def focusedCodeChanged( self, e ):
		code = e.getNextValue()
		#javax.swing.SwingUtilities.invokeLater( ecc.dennisc.lang.ApplyRunnable(self._codeTabbedPane._edit, ( code, )) )
		self._codeTabbedPane._edit( code )

	def projectOpening( self, e ):
		self._codeTabbedPane.clearTabs()
		

#print "<--", __name__
