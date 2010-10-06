/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.alice.media;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentListener;

import com.google.gdata.data.media.mediarss.MediaCategory;
import com.google.gdata.data.media.mediarss.MediaCredit;
import com.google.gdata.data.media.mediarss.MediaDescription;
import com.google.gdata.data.media.mediarss.MediaKeywords;
import com.google.gdata.data.media.mediarss.MediaRating;
import com.google.gdata.data.media.mediarss.MediaTitle;
import com.google.gdata.data.MediaContent;
import com.google.gdata.data.youtube.YouTubeMediaGroup;
import com.google.gdata.data.youtube.YouTubeNamespace;
import com.google.gdata.data.youtube.YtAspectRatio;

/**
 * @author David Culyba
 */
public class YouTubeMediaGroupEditorPanel extends JPanel {

	private YouTubeMediaGroup mediaGroup;
	
	private JTextField title;
	private JTextArea description;
	private JTextArea tags;
	private JCheckBox isPrivate;
	private JComboBox categories;
	
	private static final int TEXT_WIDTH = 24;
	private static final int TEXT_HEIGHT = 4;
	
	private static List<String> categoryStrings = null;
	private static List<String> termStrings = null;

	private static final String CATEGORY_URL = "http://gdata.youtube.com/schemas/2007/categories.cat";
	private static final String TERM_STRING = "term='";
	private static final String DEPRECATED_STRING = "deprecated";
	private static final String LABEL_STRING = "label='";
	private static final String TERM_PATTERN = "term='[^']*'";
	private static final String LABEL_PATTERN = "label='[^']*'";
	private static final String[] DEFAULT_TAGS = {"alice", "alice3"};
	private static final String DEFAULT_CATEGORY = "tech";
	
	public YouTubeMediaGroupEditorPanel()
	{
		super();
		this.setOpaque( false );
		this.title = new JTextField(TEXT_WIDTH);
		//this.title.setOpaque( false );
		
		this.description = new JTextArea(TEXT_HEIGHT, TEXT_WIDTH);
		//this.description.setOpaque( false );
		this.description.setBorder( BorderFactory.createEtchedBorder());
		
		this.tags = new JTextArea(TEXT_HEIGHT, TEXT_WIDTH);
		//this.tags.setOpaque( false );
		this.tags.setBorder( BorderFactory.createEtchedBorder() );
		
		this.categories = new JComboBox();
		//this.categories.setOpaque( false );
		
		this.isPrivate = new JCheckBox("Is Private");
		this.isPrivate.setOpaque( false );
		this.isPrivate.setSelected( true );
		
		this.setLayout( new GridBagLayout() );
		int gridY = 0;
		this.add( wrapInTitleBorder( this.title, "Title:" ), 
				new GridBagConstraints( 
				0, //gridX
				gridY++, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.NORTHWEST, //anchor 
				GridBagConstraints.HORIZONTAL, //fill
				new Insets( 2, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		this.add( wrapInTitleBorder( this.description, "Description:" ), 
				new GridBagConstraints( 
				0, //gridX
				gridY++, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.NORTHWEST, //anchor 
				GridBagConstraints.HORIZONTAL, //fill
				new Insets( 2, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		this.add( wrapInTitleBorder( this.tags, "Tags:" ), 
				new GridBagConstraints( 
				0, //gridX
				gridY++, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.NORTHWEST, //anchor 
				GridBagConstraints.HORIZONTAL, //fill
				new Insets( 2, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		this.add( wrapInTitleBorder( this.categories, "Category:" ), 
				new GridBagConstraints( 
				0, //gridX
				gridY++, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.NORTHWEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 2, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		this.add( this.isPrivate, 
				new GridBagConstraints( 
				0, //gridX
				gridY++, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.NORTHWEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 2, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
	}
	
	public void setMediaGroup(YouTubeMediaGroup ytMediaGroup)
	{
		this.mediaGroup = ytMediaGroup;
		setValuesBasedOnMediaGroup(this.mediaGroup);
	}
	
	private void setValuesBasedOnMediaGroup(YouTubeMediaGroup ytMediaGroup)
	{
		if (ytMediaGroup != null && ytMediaGroup.getTitle() != null)
		{
			this.title.setText( ytMediaGroup.getTitle().getPlainTextContent() );
		}
		
		if (ytMediaGroup != null && ytMediaGroup.getDescription() != null)
		{
			this.description.setText( ytMediaGroup.getDescription().getPlainTextContent() );
		}
		
		List<String> keywords;
		if (ytMediaGroup != null && ytMediaGroup.getKeywords() != null)
		{
			keywords = ytMediaGroup.getKeywords().getKeywords();
			for (String defaultKey : DEFAULT_TAGS)
			{
				boolean hasKey = false;
				for (String s : keywords)
				{
					if (s.equalsIgnoreCase( defaultKey))
					{
						hasKey = true;
						break;
					}
				}
				if (!hasKey)
				{
					keywords.add( defaultKey );
				}
			}
		}
		else
		{
			keywords = new LinkedList<String>();
			for (String s : DEFAULT_TAGS)
			{
				keywords.add( s );
			}
		}
		StringBuilder sb = new StringBuilder();
		for (String s : keywords)
		{
			sb.append( s+" " );
		}
		this.tags.setText( sb.toString() );
		
		String categoryString = DEFAULT_CATEGORY;
		
		if (ytMediaGroup != null && ytMediaGroup.getYouTubeCategory() != null)
		{
			MediaCategory category = ytMediaGroup.getYouTubeCategory();
			categoryString = category.getContent();
			if (categoryString == null || categoryString.equals(""))
			{
				categoryString = DEFAULT_CATEGORY;
			}
		}
		if (termStrings != null)
		{
			for (int i=0; i<termStrings.size(); i++)
			{
				if (termStrings.get( i ).equalsIgnoreCase( categoryString ))
				{
					this.categories.setSelectedIndex( i );
					break;
				}
			}
		}
		if (ytMediaGroup != null)
		{
			this.isPrivate.setSelected( ytMediaGroup.isPrivate() );
		}
		
	}
	
	public boolean initialize()
	{
		boolean success = false;
		if (categoryStrings == null || termStrings == null)
		{
			success = initializeCategories();
		}
		if (success)
		{
			this.categories.removeAllItems();
			for (String s : categoryStrings)
			{
				this.categories.addItem( s );
			}
			return true;
		}
		return false;
	}
	
	private static boolean initializeCategories()
	{
		try
		{
			URL categoryURL = new URL(CATEGORY_URL);
			InputStream is = categoryURL.openStream();
			StringBuilder sb = new StringBuilder();
			int readValue;
			while ((readValue = is.read()) != -1)
			{
				char charVal = (char)readValue;
				sb.append( charVal );
			}
			String categoryData = sb.toString();
			List<String> labels = new LinkedList<String>();
			List<String> terms = new LinkedList<String>();
			int TERM_LENGTH = TERM_STRING.length();
			int LABEL_LENGTH = LABEL_STRING.length();
			int DEPRECATED_LENGTH = DEPRECATED_STRING.length();
			int categoryLength = categoryData.length();
			String searchTerm = TERM_STRING;
			int searchLength = TERM_LENGTH;
			for (int i=0; i<categoryLength-DEPRECATED_LENGTH;)
			{
				if (categoryData.subSequence( i, i+searchLength).equals( searchTerm ))
				{
					int endIndex = categoryData.indexOf( "'", i+searchLength);
					String foundString = categoryData.substring( i+searchLength, endIndex );
					foundString = foundString.replace( "&amp;", "&" );
					if (searchTerm == TERM_STRING)
					{
						searchTerm = LABEL_STRING;
						searchLength = LABEL_LENGTH;
						terms.add( foundString );
					}
					else
					{
						searchTerm = TERM_STRING;
						searchLength = TERM_LENGTH;
						labels.add( foundString );
					}
					i = endIndex;
				}
				else if (categoryData.subSequence( i, i+DEPRECATED_LENGTH).equals( DEPRECATED_STRING ))
				{
					if (terms.size() == labels.size())
					{
						terms.remove( terms.size() - 1 );
						labels.remove( labels.size() - 1 );
					}
					else
					{
						System.err.println("CRAZY!");
					}
					i+=DEPRECATED_LENGTH;
				}
				else
				{
					i++;
				}
			}
			categoryStrings = labels;
			termStrings = terms;
			return true;
		}
		catch (IOException e)
		{
			categoryStrings = null;
			termStrings = null;
		}
		return false;
	}
	
	public void enableUI(boolean enable)
	{
		this.title.setEnabled(enable);
		this.description.setEnabled(enable);
		this.tags.setEnabled(enable);
		this.isPrivate.setEnabled(enable);
		this.categories.setEnabled(enable);
	}
	
	private JComponent wrapInTitleBorder(JComponent c, String title)
	{
		JLabel titleLabel = new JLabel(title);
		titleLabel.setOpaque( false );
		JPanel holderPanel = new JPanel();
		holderPanel.setOpaque( false );
		holderPanel.setLayout(new BorderLayout());
		holderPanel.add(titleLabel, BorderLayout.NORTH);
		holderPanel.add( c, BorderLayout.CENTER );
		return holderPanel;
	}
	
	public void addDocumentListener(DocumentListener listener)
	{
		this.title.getDocument().addDocumentListener( listener );
		this.description.getDocument().addDocumentListener( listener );
	}
	
	public void removeDocumentListener(DocumentListener listener)
	{
		this.title.getDocument().removeDocumentListener( listener );
		this.description.getDocument().removeDocumentListener( listener );
	}
	
	public void updateMediaGroup()
	{
		if (this.mediaGroup == null)
		{
			return;
		}
		this.mediaGroup.setTitle(new MediaTitle());
		this.mediaGroup.getTitle().setPlainTextContent( this.title.getText().trim() );
		this.mediaGroup.setDescription(new MediaDescription());
		this.mediaGroup.getDescription().setPlainTextContent(this.description.getText().trim());
		
		String[] tagStrings = this.tags.getText().split( " " );
		this.mediaGroup.setKeywords(new MediaKeywords());
		for (String tag : tagStrings)
		{
			this.mediaGroup.getKeywords().addKeyword( tag );
		}
		String categoryString = termStrings.get( this.categories.getSelectedIndex() );
		List<MediaCategory> categories = this.mediaGroup.getCategories();
		boolean foundCategory = false;
		for (MediaCategory c : categories)
		{
			if (c.getScheme().equals( YouTubeNamespace.CATEGORY_SCHEME ))
			{
				c.setContent( categoryString );
				foundCategory = true;
				break;
			}
		}
		if (!foundCategory)
		{
			this.mediaGroup.addCategory(new MediaCategory(YouTubeNamespace.CATEGORY_SCHEME, categoryString));
		}
//		this.mediaGroup.setYouTubeCategory( termStrings.get( this.categories.getSelectedIndex() ) );
		this.mediaGroup.setPrivate( this.isPrivate.isSelected() );
	}
	
	public boolean isTitleValid()
	{
		String titleString = this.title.getText().trim();
		return (titleString.length() > 0);
	}
	
	public boolean isDescriptionValid()
	{
		String descriptionString = this.description.getText().trim();
		return (descriptionString.length() > 0);
	}
	
	public YouTubeMediaGroup getUpdatedMediaGroup()
	{
		updateMediaGroup();
		return this.mediaGroup;
	}
	
}
