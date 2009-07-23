package org.vast.sensormleditor.outlineview;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.vast.sensormleditor.editors.SMLTreeContentProvider;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.editors.SMLTreeLabelProvider;
import org.vast.xml.DOMHelper;

/*
 Title:  SensorMLContentOutlinePage.java

 Description: TODO

 Copyright (c) 2005

 Author:  Susan P. Ingenthron 
 Date:    Aug 6, 2007
 Version: 1.0

 */

public class SensorMLContentOutlinePage extends ContentOutlinePage implements
		IContentOutlinePage {

	public static final String ID = "SensorMLEditor.OutlineView";

	public SMLTreeEditor smlEditor;

	private DOMHelper domOutline;

	private TreeViewer outlineViewer;

	public SensorMLContentOutlinePage(SMLTreeEditor smlEditor, DOMHelper dom) {
		super();
		this.smlEditor = smlEditor;
		domOutline = new DOMHelper();
	}

	public void makeContributions(IMenuManager menuManager,
			IToolBarManager toolBarManager, IStatusLineManager statusLineManager) {
		super.makeContributions(menuManager, toolBarManager, statusLineManager);

	}

	public void setActionBars(IActionBars actionBars) {
		super.setActionBars(actionBars);
		// getSite().getWorkbenchWindow().getWorkbench().get
		// getSite().shareGlobalActions(this, actionBars);
	}

	public void createControl(Composite parent) {
		super.createControl(parent);

		outlineViewer = getTreeViewer();
		outlineViewer.setLabelProvider(new SMLTreeLabelProvider(outlineViewer,
				domOutline));
		outlineViewer.setContentProvider(new SMLTreeContentProvider(
				outlineViewer, domOutline));

		Object input = smlEditor.getInput();
		outlineViewer.setInput(input);

		//createContextMenu();

		outlineViewer.expandToLevel(6);
		//outlineViewer.setSelection(new StructuredSelection(domOutline.getBaseElement()));

		outlineViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						ISelection selection = event.getSelection();
						if (!selection.isEmpty()) {
							smlEditor.handleContentOutlineSelection(selection);
						}
					}
				});
	}

/*	protected void createContextMenu() {
		MenuManager contextMenu = new MenuManager("#PopUp");
		contextMenu.add(new Separator("additions"));
		contextMenu.setRemoveAllWhenShown(true);
		contextMenu.addMenuListener(this);
		Menu menu = contextMenu.createContextMenu(outlineViewer.getControl());
		outlineViewer.getControl().setMenu(menu);
		// getSite().registerContextMenu(contextMenu, getTreeViewer());

	}
*/
	public TreeViewer getViewer() {
		return (TreeViewer) outlineViewer;
	}

/*	public void menuAboutToShow(IMenuManager manager) {
		// TODO Auto-generated method stub

	}*/

}
