/*
 *
 * Modified by SQLeo Visual Query Builder :: java database frontend with join definitions
 * Copyright (C) 2012 anudeepgade@users.sourceforge.net
 * 
 * SQLeonardo :: java database frontend
 * Copyright (C) 2004 nickyb@users.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */

package com.sqleo.environment.mdi;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.sqleo.common.gui.AbstractDialogConfirm;
import com.sqleo.common.gui.Toolbar;
import com.sqleo.common.jdbc.ConnectionAssistant;
import com.sqleo.common.jdbc.ConnectionHandler;
import com.sqleo.common.util.I18n;
import com.sqleo.common.util.LocaleAdapter;
import com.sqleo.environment.Application;
import com.sqleo.environment.Preferences;
import com.sqleo.querybuilder.QueryBuilder;


public class DialogPreferences extends AbstractDialogConfirm {
	private JCheckBox optQbAutoJoin;
	private JCheckBox optQbAutoAlias;
	private JCheckBox optQbUseQuote;
	private JCheckBox optQbUseSchema;
	private JCheckBox optQbLoadAtOnce;
	private JCheckBox optQbSelectAll;

	private JLabel jLabelLanguage = new JLabel();
	private JTextArea jLabelAutoCommitInfo = new JTextArea();
	private JComboBox jComboBoxLanguage = new JComboBox();
	private JCheckBox jCheckBoxTrace = new JCheckBox();
	//private JTextField jTextFieldMaxRowFetchSize = new JTextField();

	private JTextField jTextFieldMaxColSize = new JTextField();
	private JCheckBox jCheckBoxAutoCommit = new JCheckBox();

	public DialogPreferences() {
		super(Application.window, Application.PROGRAM + ".preferences", 350,
				INITIAL_HEIGHT);

		JPanel pnlQB = new JPanel(new GridLayout(0, 1));
		pnlQB.setBorder(new EmptyBorder(10, 5, 90, 5));

		pnlQB.add(optQbAutoJoin = new JCheckBox(I18n.getString(
				"application.preferences.autoJoin", "auto join"), Preferences
				.getBoolean("querybuilder.auto-join", true)));
		pnlQB.add(optQbAutoAlias = new JCheckBox(I18n.getString(
				"application.preferences.autoAlias", "auto alias"), Preferences
				.getBoolean("querybuilder.auto-alias", true)));
		pnlQB.add(new JSeparator());
		pnlQB.add(optQbUseQuote = new JCheckBox(I18n.getString(
				"application.preferences.alwaysQuoteIdentifiers",
				"always quote identifiers"), Preferences.getBoolean(
				"querybuilder.use-quote", true)));
		pnlQB.add(optQbUseSchema = new JCheckBox(I18n.getString(
				"application.preferences.useSchemaName",
				"use schema name in syntax definition"), Preferences
				.getBoolean("querybuilder.use-schema", true)));
		pnlQB.add(new JSeparator());
		pnlQB.add(optQbLoadAtOnce = new JCheckBox(I18n.getString(
				"application.preferences.loadObjectsAtOnce",
				"load table objects list at once"), Preferences.getBoolean(
				"querybuilder.load-objects-at-once", true)));
		pnlQB.add(optQbSelectAll = new JCheckBox(I18n.getString(
				"application.preferences.selectAllColumns",
				"aout select all colmuns"), Preferences.getBoolean(
				"querybuilder.select-all-columns", true)));

		jLabelLanguage.setText(I18n.getString(
				"application.preferences.language", "language"));
		jLabelAutoCommitInfo.setText(I18n.getString(
				"application.preferences.autoCommitInfo",
				I18n.getString(
						"application.preferences.autoCommitWarning",
						"Warning:Commit in a window affects all openend windows for a connection when autocommit disabled")		
		));
		jLabelAutoCommitInfo.setForeground(Color.RED);
		jLabelAutoCommitInfo.setFont(new Font(jLabelAutoCommitInfo.getFont().getName(),Font.BOLD,jLabelAutoCommitInfo.getFont().getSize()));
		jLabelAutoCommitInfo.setLineWrap(true);
		jLabelAutoCommitInfo.setWrapStyleWord(true);
		
		// Add all the available languages...
		java.util.List list = I18n.getListOfAvailLanguages();
		list.add(0, Locale.getDefault());

		if (!Locale.getDefault().equals(Locale.ENGLISH)) {
			list.add(1, Locale.ENGLISH);
		}

		int selectedItem = 0;
		for (int i = 0; i < list.size(); ++i) {
			java.util.Locale loc = (java.util.Locale) list.get(i);
			jComboBoxLanguage.addItem(new LocaleAdapter(loc));

			if (I18n.getCurrentLocale().toString().equals(loc.toString())) {
				selectedItem = i;
			}
			// else
			// {
			// System.out.println(I18n.getCurrentLocale().toString() + " != " +
			// loc.toString());
			// }
		}
		jComboBoxLanguage.setSelectedIndex(selectedItem);

		jCheckBoxTrace.setText(I18n.getString("application.preferences.trace",
				"trace log (need restart)"));
		jCheckBoxTrace.setSelected(Preferences.getBoolean("application.trace",
				true));
		jCheckBoxAutoCommit.setText(I18n.getString(
				"application.preferences.autoCommit", "auto commit"));
		jCheckBoxAutoCommit.setSelected(Preferences.getBoolean(
				"application.autoCommit", true));
		jCheckBoxAutoCommit.addItemListener(new ItemListener() {

		    public void itemStateChanged(ItemEvent e) {
		        if(e.getStateChange() == ItemEvent.SELECTED || e.getStateChange() == ItemEvent.DESELECTED){
		        	boolean isAutoCommitON = e.getStateChange() == ItemEvent.SELECTED;
		        	//update all connections autoCommit flags
		        	for(String keych : (Set<String>)ConnectionAssistant.getHandlers() ){
		        			try {
		        				ConnectionHandler ch = ConnectionAssistant.getHandler(keych);
				        		if(ch!=null){
				        			ch.get().setAutoCommit(isAutoCommitON);
				        		}
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
		        	}
		        	// toggle commit-rollback buttons on toolbars of commandEditor and contentpane windows.
		        	ClientCommandEditor cce = (ClientCommandEditor) Application.window
					.getClient(ClientCommandEditor.DEFAULT_TITLE);
		        	if(cce!=null){
		        		Toolbar ceToolbar = cce.getSubToolbar();
		        		ceToolbar.getActionMap().get("action-commit").setEnabled(!isAutoCommitON);
		        		ceToolbar.getActionMap().get("action-rollback").setEnabled(!isAutoCommitON);
		        	}	
		        	
		        	MDIClient[] contentClients = Application.window.getClientsOfConnection(ClientContent.DEFAULT_TITLE,null);
		        	for(int i=0; i<contentClients.length; i++){
			        	ClientContent cct = (ClientContent)contentClients[i];
			        	Toolbar cctToolbar = cct.getSubToolbar();
			        	cctToolbar.getActionMap().get("action-commit").setEnabled(!isAutoCommitON);
			        	cctToolbar.getActionMap().get("action-rollback").setEnabled(!isAutoCommitON);
		        	}
		        	MDIClient[] previewClients = Application.window.getClientsOfConnection(ClientContent.PREVIEW_TITLE,null);
		        	for(int i=0; i<previewClients.length; i++){
			        	ClientContent pct = (ClientContent)previewClients[i];
			        	Toolbar pctToolbar = pct.getSubToolbar();
			        	pctToolbar.getActionMap().get("action-commit").setEnabled(!isAutoCommitON);
			        	pctToolbar.getActionMap().get("action-rollback").setEnabled(!isAutoCommitON);
		        	}
		        	
		        	
		        }
		    }
		});

		
		

		JPanel pnlGeneral = new JPanel(new GridLayout(0, 1));
		pnlGeneral.setBorder(new EmptyBorder(10, 5, 5, 5));

		pnlGeneral.add(jLabelLanguage);
		pnlGeneral.add(jComboBoxLanguage);
		pnlGeneral.add(jCheckBoxTrace);
		//TODO add max rows fetch size
		//pnlGeneral.add(new JSeparator());
//		pnlGeneral.add(new JLabel(I18n.getString(
//				"application.preferences.maxrowfetchsize", "Maximum rows fetch size:")));
//		jTextFieldMaxRowFetchSize.setText(String.valueOf(Preferences.getInteger(
//				"application.maxrowfetchsize", 20)));
//		pnlGeneral.add(jTextFieldMaxRowFetchSize);
		pnlGeneral.add(jLabelAutoCommitInfo);
		pnlGeneral.add(jCheckBoxAutoCommit);

		JPanel pnlCommand = new JPanel(new GridLayout(0, 1));
		pnlCommand.setBorder(new EmptyBorder(10, 5, 210, 5));
		pnlCommand.add(new JLabel(I18n.getString(
				"application.preferences.maxcolsize", "Maximum column size:")));
		jTextFieldMaxColSize.setText(String.valueOf(Preferences.getInteger(
				"editor.maxcolsize", 50)));
		pnlCommand.add(jTextFieldMaxColSize);

		JTabbedPane options = new JTabbedPane();
		options.addTab("general", pnlGeneral);
		options.addTab("command editor", pnlCommand);
		options.addTab("query builder", pnlQB);

		getContentPane().add(options);
	}

	@Override
	protected void onOpen() {
		Object[] items = Application.window.menubar.getMenu(
				MDIMenubar.IDX_WINDOW).getMenuComponents();
		for (int i = 0; i < items.length; i++) {
			if (items[i] instanceof JMenuItem) {
				String txt = ((JMenuItem) items[i]).getText();
				if (txt.indexOf(" - QUERY : ") != -1) {
					optQbAutoAlias.setEnabled(false);
					optQbAutoJoin.setEnabled(false);
					optQbUseQuote.setEnabled(false);
					optQbUseSchema.setEnabled(false);
					optQbLoadAtOnce.setEnabled(false);
					optQbSelectAll.setEnabled(false);

					break;
				}
			}
		}
	}

	@Override
	protected boolean onConfirm() {
		QueryBuilder.autoJoin = optQbAutoJoin.isSelected();
		QueryBuilder.autoAlias = optQbAutoAlias.isSelected();
		QueryBuilder.useAlwaysQuote = optQbUseQuote.isSelected();
		QueryBuilder.loadObjectsAtOnce = optQbLoadAtOnce.isSelected();
		QueryBuilder.selectAllColumns = optQbSelectAll.isSelected();

		Preferences.set("querybuilder.auto-join",
				new Boolean(optQbAutoJoin.isSelected()));
		Preferences.set("querybuilder.auto-alias",
				new Boolean(optQbAutoAlias.isSelected()));
		Preferences.set("querybuilder.use-quote",
				new Boolean(optQbUseQuote.isSelected()));
		Preferences.set("querybuilder.use-schema",
				new Boolean(optQbUseSchema.isSelected()));
		Preferences.set("querybuilder.load-objects-at-once", new Boolean(
				optQbLoadAtOnce.isSelected()));
		Preferences.set("querybuilder.select-all-columns", new Boolean(
				optQbSelectAll.isSelected()));

		if (jComboBoxLanguage.getSelectedIndex() >= 0) {
			LocaleAdapter la = (LocaleAdapter) jComboBoxLanguage
					.getSelectedItem();
			I18n.setCurrentLocale(la.getLocale());
			Preferences.set("app.locale", la.getLocale().toString());
		}

		Preferences.set("application.trace",
				new Boolean(jCheckBoxTrace.isSelected()));
		Preferences.set("application.autoCommit", new Boolean(
				jCheckBoxAutoCommit.isSelected()));
		Preferences.set("editor.maxcolsize",
				new Integer(jTextFieldMaxColSize.getText()));
//		Preferences.set("application.maxrowfetchsize",
//				new Integer(jTextFieldMaxRowFetchSize.getText()));

		return true;
	}
}