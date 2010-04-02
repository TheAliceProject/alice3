import java
import javax

import ecc

#from ecc.dennisc.tools.Actions import *
#from ecc.dennisc.tools.Menu import MenuBar, Menu, MenuSeparator
#from ecc.dennisc.tools.ToolWithDefaultMenus import ToolWithDefaultMenus 

FONT = java.awt.Font( "Monospaced", java.awt.Font.BOLD, 18 )
NEW_TEMPLATE = '#enter code you want executed to get things going here\n\n\n""" ***scratchpad space***\n#enter code you want to execute on the fly here\n\n\n"""'

class RunEntireFileAction( ecc.dennisc.tool.Action ):
	def __init__( self, actionHandler ):
		ecc.dennisc.tool.Action.__init__( self, actionHandler )
		self.putValue( javax.swing.Action.NAME, "Run entire file" )
		self.putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK|java.awt.event.InputEvent.SHIFT_MASK ) )
		#self.putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_F5, java.awt.event.InputEvent.SHIFT_MASK ) )

class RunSelectionAction( ecc.dennisc.tool.Action ):
	def __init__( self, actionHandler ):
		ecc.dennisc.tool.Action.__init__( self, actionHandler )
		self.putValue( javax.swing.Action.NAME, "Run selection" )
		self.putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK ) )
		#self.putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_F5, 0 ) )

class ExecThread( java.lang.Thread ):
	def __init__( self, text, filename, kind, globals, locals ):
		java.lang.Thread.__init__( self )
		self._text = text
		self._filename = filename
		self._kind = kind
		self._globals = globals
		self._locals = locals
	def run( self ):
		c = compile( self._text, self._filename, self._kind ) 
		exec c in self._globals, self._locals

class DoLine( javax.swing.JPanel ):
	def __init__( self ):
		javax.swing.JPanel.__init__( self )
		self._comboBox = javax.swing.JComboBox()
		#self._button = javax.swing.JButton( "exec" )
		self._comboBox.setFont( FONT )
		#self._button.setFont( FONT )
		
		self._comboBox.setEditable( True )
		
		self.setLayout( java.awt.BorderLayout() )
		self.add( self._comboBox, java.awt.BorderLayout.CENTER )
		#self.add( self._button, java.awt.BorderLayout.EAST )
		

class SetSelectionRunnable( java.lang.Runnable ):
	def __init__( self, textComponent, selStart, selEnd ):
		self._textComponent = textComponent
		self._selStart = selStart
		self._selEnd = selEnd
	def run( self ):
		self._textComponent.setSelectionStart( self._selStart )
		self._textComponent.setSelectionEnd( self._selEnd )

class DelayedRunnableThread( java.lang.Thread ):
	def __init__( self, msec, runnable ):
		self._msec = msec
		self._runnable = runnable
	def run( self ):
		self.sleep( self._msec )
		self._runnable.run()
		#javax.swing.SwingUtilities.invokeLater( self._runnable )

class MouseAdapter( java.awt.event.MouseAdapter ):
	def mouseClicked( self, e ):
		if e.getClickCount()==2:
			print "TODO: figure out why swing won't let me customize word boundaries.  Best guess so far: they hate me."

class CodeEditor( javax.swing.JTextArea ):
	def __init__( self ):
		javax.swing.JTextArea.__init__( self )
		self.addMouseListener( MouseAdapter() )
		self.setFont( FONT )
		self.setTabSize( 4 )

	def _getSelectedText( self ):
		#document = self.getDocument()
		#s = java.lang.String( document.getText( 0, document.getLength() ) )
		_selStart = self.getSelectionStart()
		_selEnd = self.getSelectionEnd()
		s = java.lang.String( self.getText() )
		selStart = s.lastIndexOf( '\n', _selStart - 1 ) + 1
		selEnd = s.indexOf( '\n', _selEnd )
		self.setSelectionStart( selStart )
		self.setSelectionEnd( selEnd )
		DelayedRunnableThread( 100, SetSelectionRunnable( self, _selStart, _selEnd ) ).start()
		return s.substring( selStart, selEnd )
		

class Root( javax.swing.JPanel, java.awt.event.ActionListener ):
	def __init__( self ):
		javax.swing.JPanel.__init__( self )
		
		import __main__
		self._globals = __main__.__dict__
		self._locals = self._globals

		self._doLine = DoLine()
		self._codeEditor = CodeEditor()

		self._doLine._comboBox.addActionListener( self )
		
		self.setLayout( java.awt.BorderLayout() )
		self.add( self._doLine, java.awt.BorderLayout.NORTH )
		self.add( javax.swing.JScrollPane( self._codeEditor ), java.awt.BorderLayout.CENTER )

	def actionPerformed( self, e ):
		if e.getActionCommand() == "comboBoxEdited":
			text = self._doLine._comboBox.getModel().getSelectedItem()
			self._handleExec( text, "<<<do line>>>", "single" )
		else:
			pass
			#print "skipping ", e
		
	def _handleExec( self, s, file, kind ):
		ExecThread( s, file, kind, self._globals, self._locals ).start()
	

class Haru( ecc.dennisc.tool.ToolWithDefaultMenus, javax.swing.event.DocumentListener ):
	def __init__( self, size, path=None, isRunEntireFileDesired=False ):
		ecc.dennisc.tool.ToolWithDefaultMenus.__init__( self, Root(), size, path )

		self._pane._codeEditor.getDocument().addDocumentListener( self )

		if isRunEntireFileDesired:
			self.handleRunEntireFile()

		self._runEntireFileAction = RunEntireFileAction( self )
		self._runSelectionAction = RunSelectionAction( self )

		#menuBar.add( ecc.dennisc.tool.Menu( "Edit", java.awt.event.KeyEvent.VK_E, self._undoAction, self._redoAction, MenuSeparator, self._cutAction, self._copyAction, self._pasteAction ) )
		self._menuBar.add( ecc.dennisc.tool.Menu( "Run", java.awt.event.KeyEvent.VK_R, self._runEntireFileAction, self._runSelectionAction ) )
		#self._menuBar.add( ecc.dennisc.tool.Menu( "Help", java.awt.event.KeyEvent.VK_H, self._helpAction ) )

	def changedUpdate( self, e ):
		self._setDirty( True )
	def insertUpdate( self, e ):
		self._setDirty( True )
	def removeUpdate( self, e ):
		self._setDirty( True )

	def _new( self ):
		self._pane._codeEditor.setText( NEW_TEMPLATE )
		self._setFile( None )
		return True
	def _open( self, file ):
		reader = java.io.FileReader( file )
		try:
			self._pane._codeEditor.read( reader, None )
			self._setFile( file )
		finally:
			reader.close()
	def _save( self, file ):
		writer = java.io.FileWriter( file )
		try:
			self._pane._codeEditor.write( writer )
			self._setFile( file )
		finally:
			writer.close()
		return True
	def _exit( self ):
		import sys
		sys.exit()

	def handleRunEntireFile( self ):
		self._pane._handleExec( self._pane._codeEditor.getText(), self._getFilePath(), "exec" )

	def handleRunSelection( self ):
		text = self._pane._codeEditor._getSelectedText()
		if "\n" in text:
			kind = "exec"
		else:
			kind = "single"
		self._pane._handleExec( text, "selection within " +  self._getFilePath(), kind )

	def handleActionPerformed( self, action, e ):
		if action == self._runEntireFileAction:
			self.handleRunEntireFile()
		elif action == self._runSelectionAction:
			self.handleRunSelection()
		else:
			ToolWithDefaultMenus.handleActionPerformed( self, action, e )

if __name__ == "__main__":
	import sys
	if len( sys.argv ) > 1:
		path = sys.argv[ 1 ]
		if len( sys.argv ) > 2:
			isRunEntireFileDesired = sys.argv[ 2 ]=="isRunEntireFileDesired=True"
		else:
			isRunEntireFileDesired = False
	else:
		path = None
		isRunEntireFileDesired = False
	haru = Haru( ( 1600, 1200 ), path, isRunEntireFileDesired )
	haru.setVisible( True )

