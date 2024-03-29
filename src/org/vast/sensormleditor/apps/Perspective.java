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

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public static final String ID = "SensorMLEditor.perspective";

	public void createInitialLayout(IPageLayout layout) {
		
		
		 layout.setEditorAreaVisible(true); 
		 String editorArea = layout.getEditorArea();
		 layout.setFixed(false);
	
		
		 layout.addStandaloneView("SensorMLEditor.NavigationView", true, IPageLayout.LEFT, .18f, editorArea);
		 //layout.addView(NavigatorView.ID, IPageLayout.LEFT, 0.25f, editorArea);
		 //layout.addView(NavigatorView.ID,IPageLayout.LEFT, 0.25f, editorArea);
	
		 layout.addView(IPageLayout.ID_PROP_SHEET, IPageLayout.RIGHT, .35f,
				 editorArea); 
		// layout.addStandaloneView("org.vast.sensormleditor.preferences.SamplePreferencePage",true,IPageLayout.RIGHT,.35f,editorArea);
		 }
		
	}



