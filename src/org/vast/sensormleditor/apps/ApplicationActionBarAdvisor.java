/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.sensormleditor.apps;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.vast.sensormleditor.actions.NewSensorMLEditorInstanceAction;


public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	// Actions - important to allocate these only in makeActions, and then use
	// them
	// in the fill methods. This ensures that the actions aren't recreated
	// when fillActionBars is called with FILL_PROXY.
	private IWorkbenchAction exitAction;
	private IWorkbenchAction aboutAction;
	private IWorkbenchAction helpAction;
	private IWorkbenchAction newAction;
	private IWorkbenchAction closeAction;
	private IWorkbenchAction closeAllAction;
	private IWorkbenchAction saveAction;
	private IWorkbenchAction saveAsAction;
	private IWorkbenchAction importAction;
	private IWorkbenchAction exportAction;
	private IWorkbenchAction newInstanceAction;
	private IWorkbenchAction deleteAction;
	private IWorkbenchAction refreshAction;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	protected void makeActions(final IWorkbenchWindow window) {
		// Creates the actions and registers them.
		// Registering is needed to ensure that key bindings work.
		// The corresponding commands keybindings are defined in the plugin.xml
		// file.
		// Registering also provides automatic disposal of the actions when
		// the window is closed.
		exitAction = ActionFactory.QUIT.create(window);
		register(exitAction);
		
		deleteAction = ActionFactory.DELETE.create(window);
		register(deleteAction);
		
		refreshAction = ActionFactory.REFRESH.create(window);
		register(refreshAction);
		
		aboutAction = ActionFactory.ABOUT.create(window);
		register(aboutAction);
		
		helpAction = ActionFactory.HELP_CONTENTS.create(window);
		register(helpAction);
		
		newAction = ActionFactory.NEW.create(window);
		newAction.setText("New");
		register(newAction);
		
		importAction = ActionFactory.IMPORT.create(window);
		register(importAction);
		
		exportAction = ActionFactory.EXPORT.create(window);
		register(exportAction);
		
		closeAction = ActionFactory.CLOSE.create(window);
		register(closeAction);
		
		closeAllAction = ActionFactory.CLOSE_ALL.create(window);
		register(closeAllAction);
		
		saveAction = ActionFactory.SAVE.create(window);
		register(saveAction);
		
		saveAsAction = ActionFactory.SAVE_AS.create(window);
		register(saveAsAction);
		
		newInstanceAction = new NewSensorMLEditorInstanceAction(window);
		ImageDescriptor im = AbstractUIPlugin.imageDescriptorFromPlugin(
				Application.PLUGIN_ID, "/icons/icon_edit.gif");
		newInstanceAction.setImageDescriptor(im);
		newInstanceAction.setEnabled(false);
		register(newInstanceAction);
	}

	protected void fillMenuBar(IMenuManager menuBar) {
		MenuManager sensormlEditorMenu = new MenuManager("&SensorML Editor",
				"SensorMLEditor.MenuBar");
		
		MenuManager newMenu = new MenuManager("&New","new");
		sensormlEditorMenu.add(newMenu);
		newMenu.add(newAction);
		newMenu.add(newInstanceAction);
		
		
	
		/*sensormlEditorMenu.add(refreshAction);
		sensormlEditorMenu.add(deleteAction);*/
		sensormlEditorMenu.add(importAction);
		sensormlEditorMenu.add(exportAction);
		sensormlEditorMenu.add(closeAction);
		sensormlEditorMenu.add(closeAllAction);
		sensormlEditorMenu.add(saveAction);
		sensormlEditorMenu.add(saveAsAction);
		sensormlEditorMenu.add(exitAction);
		sensormlEditorMenu.add(new GroupMarker("other-actions"));
		
		MenuManager helpMenu = new MenuManager("&Help","help");
		helpMenu.add(helpAction);
		helpMenu.add(aboutAction);
		
		menuBar.add(sensormlEditorMenu);
		sensormlEditorMenu.add(newMenu);
		menuBar.add(helpMenu);
	
	}
	
	protected void fillCoolBar(ICoolBarManager coolBar){
		IToolBarManager toolbar = new ToolBarManager(coolBar.getStyle());
		coolBar.add(toolbar);
		toolbar.add(newInstanceAction);
	}
	public void populateCoolBar(IActionBarConfigurer configurer){
		ICoolBarManager mgr = configurer.getCoolBarManager();
		IToolBarManager toolbar = new ToolBarManager(mgr.getStyle());
		mgr.add(toolbar);
		toolbar.add(newInstanceAction);
		toolbar.add(new Separator());
	}

}
