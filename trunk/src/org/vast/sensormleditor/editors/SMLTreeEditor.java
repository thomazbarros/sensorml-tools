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
 Mike Botts <mike.botts@uah.edu>
 Susan Ingenthron <susan.ingenthron@nsstc.uah.edu>
 
 ******************************* END LICENSE BLOCK ***************************/

package org.vast.sensormleditor.editors;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.internal.resources.Resource;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RefreshAction;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.IWorkbenchActionDefinitionIds;
import org.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.vast.sensormleditor.actions.DOMDeleteAction;
import org.vast.sensormleditor.outlineview.SensorMLContentOutlinePage;
import org.vast.sensormleditor.properties.SMLTabbedPropertySheetPage;
import org.vast.sensormleditor.relaxNG.Hybrid2XML;
import org.vast.sensormleditor.util.DOMHelperAddOn;
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;
import org.vast.xml.transform.DOMTransformException;
import org.w3c.dom.Element;

public class SMLTreeEditor extends EditorPart implements
		ITabbedPropertySheetPageContributor, IShellProvider {

	public static final String ID = "SensorMLEditor.TreeEditor";

	protected TreeViewer viewer;

	protected TreeViewer outlineViewer;

	public DOMHelper dom;
	public DOMHelper rngDOM;
	public DOMHelperAddOn domUtil;
	public boolean stop = false;

	protected boolean fileIsDirty = false;

	protected SensorMLContentOutlinePage sensorMLOutline;

	// protected PropertySheetPage defaultPropertyPage;

	protected SMLTabbedPropertySheetPage tabbedPropertyPage;

	protected SMLPopupMenuProvider popUpProvider;

	public IPropertyChangeListener propertyChangeListener = new IPropertyChangeListener() {
		public void propertyChange(String property, Object oldValue,
				Object newVal) {
			String prop = property;
			if (!prop.equals("INSERT"))
				return;
			
			
			//Assert.isTrue(selection instanceof IStructuredSelection);
			//Object input = ((IStructuredSelection) selection).getFirstElement();
			//Assert.isTrue(input instanceof Element);
			//this.element = (Element) input;
			ISelection sel = SMLTreeEditor.this.getViewer().getSelection();
			if (sel instanceof IStructuredSelection){
				Object selEle = ((IStructuredSelection) sel).getFirstElement();
				SMLTreeEditor.this.getViewer().setExpandedState(selEle,true);
				SMLTreeEditor.this.getViewer().refresh(selEle,false);
			}
			
			SMLTreeEditor.this.fileIsDirty = true;
			SMLTreeEditor.this.firePropertyChange(PROP_DIRTY);
			SMLTreeEditor.this.getViewer().refresh();
			
			if (tabbedPropertyPage!= null){
				
			    //tabbedPropertyPage.getCurrentTab().setInput(SMLTreeEditor.this, sel);
			 //   tabbedPropertyPage.getCurrentTab().refresh();
			
			}
			Object newValue = newVal;
			Object oldVal = oldValue;
		}

		public void propertyChange(PropertyChangeEvent event) {
			String property = event.getProperty();
			Object newValue = event.getNewValue();
			Object oldValue = event.getOldValue();

			if (!property.equals("INSERT"))
				return;
			
			ISelection sel = SMLTreeEditor.this.getViewer().getSelection();
			if (sel instanceof IStructuredSelection){
				Object selEle = ((IStructuredSelection) sel).getFirstElement();
				SMLTreeEditor.this.getViewer().setExpandedState(selEle,true);
				SMLTreeEditor.this.getViewer().refresh(selEle,false);
			}
			
			
			
			SMLTreeEditor.this.fileIsDirty = true;
			SMLTreeEditor.this.firePropertyChange(PROP_DIRTY);
			SMLTreeEditor.this.getViewer().refresh();
			
			
			if (tabbedPropertyPage!= null){
				//ISelection sel = SMLTreeEditor.this.getViewer().getSelection();
			    //tabbedPropertyPage.getCurrentTab().setInput(SMLTreeEditor.this, sel);
			   // tabbedPropertyPage.getCurrentTab().refresh();    
			
			}
		}
	};

	/**
	 * The constructor.
	 */
	public SMLTreeEditor() {
		super();

	}

	public IPropertyChangeListener getPropertyListener() {
		return SMLTreeEditor.this.propertyChangeListener;
	}

	public void refresh() {

		if (tabbedPropertyPage != null)
			tabbedPropertyPage.refresh();
		//this.getSite();
	}

	protected void updateStatusLine(IStructuredSelection selection) {
		String msg = getStatusLineMessage(selection);
		getEditorSite().getActionBars().getStatusLineManager().setMessage(msg);
	}

	protected String getStatusLineMessage(IStructuredSelection selection) {
		if (selection.size() == 1) {
			Object o = selection.getFirstElement();
			if (o instanceof Element) {
				return ((Element) o).getLocalName();
			}
			return "1 Item Selected";
		}
		return "";
	}

	protected IPartListener partListener = new IPartListener() {
		public void partActivated(IWorkbenchPart p) {

			if (p instanceof ContentOutline) {
				if (((ContentOutline) p).getCurrentPage() == sensorMLOutline) {
					if (getEditorSite().getActionBarContributor() != null)
						getEditorSite().getActionBarContributor()
								.setActiveEditor(SMLTreeEditor.this);
				}
			}
			if (p instanceof PropertySheet) {
				if (((PropertySheet) p).getCurrentPage() instanceof IPropertySheetPage) {
					if (getEditorSite().getActionBarContributor() != null)
						getEditorSite().getActionBarContributor()
								.setActiveEditor(SMLTreeEditor.this);
				}
			}

		}

		public void partBroughtToTop(IWorkbenchPart part) {
		}

		public void partClosed(IWorkbenchPart part) {
		}

		public void partDeactivated(IWorkbenchPart part) {
		}

		public void partOpened(IWorkbenchPart part) {
		}
	};

	public void createPartControl(Composite parent) {
		TreeViewer viewer = createViewer(parent);
		// create the tree viewer
		this.viewer = viewer;
		final Tree tree = viewer.getTree();
		viewer.setAutoExpandLevel(3);
		this.getSite().setSelectionProvider(viewer);

		tree.setLayoutData(new FillLayout());
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);

		TreeColumn elementColumn = new TreeColumn(tree, SWT.NONE);
		elementColumn.setText("SensorML Element");
		elementColumn.setResizable(true);
		elementColumn.setWidth(200);

		TreeColumn valueColumn = new TreeColumn(tree, SWT.NONE);
		valueColumn.setText("Value");
		valueColumn.setWidth(100);
		valueColumn.setResizable(true);

		// create initial context menu
		createContextMenu();
		// setup column IDs and editors
		viewer.setColumnProperties(new String[] { "element", "value" });
		if (dom != null) {
			Object input = dom.getXmlDocument().getDocument();
			viewer.setInput(input);
		}

	}

	protected TreeViewer createViewer(Composite parent) {
		TreeViewer viewer = new TreeViewer(parent, SWT.FULL_SELECTION
				| SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setUseHashlookup(true);
		initContentProvider(viewer);
		initLabelProvider(viewer);
		initListeners(viewer);

		return viewer;
	}

	protected void initContentProvider(TreeViewer viewer) {
		viewer.setContentProvider(new SMLTreeContentProvider(viewer, dom));
	}

	protected void initLabelProvider(TreeViewer viewer) {
		// cell content/label providers and cell modifier
		viewer.setLabelProvider(new SMLTreeLabelProvider(viewer, dom));
	}

	protected void initListeners(TreeViewer viewer) {
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				handleSelectionChanged(event);
			}

		});
	}

	public TreeViewer getTreeViewer() {
		return viewer;
	}

	public TreeViewer getViewer() {
		return viewer;
	}

	public DOMHelper getModel() {
		return dom;
	}

	private void createContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);

		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				SMLTreeEditor.this.fillContextMenu(manager);
			}
		});

		Menu menu = menuMgr.createContextMenu(viewer.getControl());

		DOMDeleteAction keydeleteAction = new DOMDeleteAction(viewer, dom);
		keydeleteAction.setEnabled(true);
		keydeleteAction.setText("Delete Element");
		keydeleteAction.setToolTipText("Delete Element from model");
		keydeleteAction.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages().getImageDescriptor(
						ISharedImages.IMG_TOOL_DELETE));
		keydeleteAction
				.setActionDefinitionId(IWorkbenchActionDefinitionIds.DELETE);
		this.getActionBars().setGlobalActionHandler(
				ActionFactory.DELETE.getId(), keydeleteAction);
		keydeleteAction.addPropertyChangeListener(propertyChangeListener);
		viewer.getControl().setMenu(menu);
		//DO NOT REGISTER THIS MENU AS OTHERS CAN THEN CONTRIBUTE
		//getSite().registerContextMenu(menuMgr, viewer);
	}


	public void fillContextMenu(IMenuManager menuMgr) {

		MenuManager insertMenu = new MenuManager("Insert:");
		menuMgr.add(insertMenu);
		menuMgr.add(new Separator());
		// menuMgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));

		DOMDeleteAction deleteAction = new DOMDeleteAction(viewer, dom);
		deleteAction.setEnabled(true);
		deleteAction.setText("Delete Element");
		deleteAction.setToolTipText("Delete Element from model");
		deleteAction.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages().getImageDescriptor(
						ISharedImages.IMG_TOOL_DELETE));
		deleteAction
				.setActionDefinitionId(IWorkbenchActionDefinitionIds.DELETE);
		// this.getActionBars().setGlobalActionHandler(ActionFactory.DELETE.getId(),
		// deleteAction);
		deleteAction.addPropertyChangeListener(propertyChangeListener);
		menuMgr.add(deleteAction);

		IStructuredSelection selection = (IStructuredSelection) getViewer()
				.getSelection();
		if (selection.getFirstElement() instanceof Element) {
			Element ele = (Element) selection.getFirstElement();
			popUpProvider.setSelectedElement(ele);
			Element retNode = null;
			DOMHelperAddOn domUtil = new DOMHelperAddOn(dom);
			Element chosenOne = domUtil.getChosenField(ele, retNode);
			if (chosenOne != null)
				ele = chosenOne;
			popUpProvider.fillContextMenuHelper(ele, insertMenu);

		}
		insertMenu.dispose();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		FileOutputStream out;
		IFile file = null;
		if (getEditorInput() instanceof IFileEditorInput) {
			file = ((IFileEditorInput) getEditorInput()).getFile();
			try {
				String fileName = this.getTitle();
				out = new FileOutputStream(fileName);
				FileEditorInput inp = (FileEditorInput) this.getEditorInput();
				fileName = inp.getPath().toString();
				if (fileName.endsWith(".xml") || fileName.endsWith(".rng"))
					fileName = fileName.substring(0, fileName.length() - 4);

				FileOutputStream outputStream;
				// Saving the rng file
				try {
					outputStream = new FileOutputStream(fileName + ".rng");
					try {
						dom.serialize(dom.getBaseElement(), outputStream, null);
						outputStream.toString();
						// try {
						// file.setContents(new
						// ByteArrayInputStream(outputStream.toString().getBytes()),
						// IResource.KEEP_HISTORY, monitor);
						fileIsDirty = false;
						this.firePropertyChange(PROP_DIRTY);
						// } catch (CoreException e) {
						// TODO Auto-generated catch block
						// e.printStackTrace();
						// }
					} catch (IOException e) {
						e.printStackTrace();
					}

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Save the xml file
				outputStream = null;
				Hybrid2XML transform2 = new Hybrid2XML();
				DOMHelper xmlFormat = null;
				try {
					xmlFormat = transform2.transform(dom);
				} catch (DOMTransformException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					outputStream = new FileOutputStream(fileName + ".xml");
					try {
						xmlFormat.serialize(xmlFormat.getBaseElement(),
								outputStream, null);
						outputStream.toString();

					} catch (IOException e) {
						e.printStackTrace();
					}

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {

		}
		RefreshAction raction = new RefreshAction(this);
		raction.refreshAll();
		this.fileIsDirty = false;

	}

	@Override
	public void doSaveAs() {

		SaveAsDialog saveAsDialog = new SaveAsDialog(getSite().getShell());
		saveAsDialog.open();
		IPath path = saveAsDialog.getResult();
		if (path != null) {
			IPath workspacePath = ResourcesPlugin.getWorkspace().getRoot()
					.getLocation();

			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			if (file != null) {
				String filePath = file.getFullPath().toString();
				this.setPartName(this.getPartName());
				writeFiles(workspacePath + filePath);
			}
		}
		saveAsDialog.close();
		RefreshAction raction = new RefreshAction(this);
		raction.refreshAll();
	}

	public void writeFiles(String path) {

		if (path.endsWith(".xml") || path.endsWith(".rng"))
			path = path.substring(0, path.length() - 4);

		FileOutputStream outputStream;
		// Saving the rng file
		try {
			outputStream = new FileOutputStream(path + ".rng");
			try {
				dom.serialize(dom.getBaseElement(), outputStream, null);
				outputStream.toString();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Save the xml file
		outputStream = null;
		Hybrid2XML transform2 = new Hybrid2XML();
		DOMHelper xmlFormat = null;
		try {
			xmlFormat = transform2.transform(dom);
		} catch (DOMTransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			outputStream = new FileOutputStream(path + ".xml");
			try {
				xmlFormat.serialize(xmlFormat.getBaseElement(), outputStream,
						null);
				outputStream.toString();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		outputStream = null;
		fileIsDirty = false;
		this.firePropertyChange(PROP_DIRTY);
		this.getViewer().refresh();
	}

	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		this.setSite(site);
		this.setInput(input);

		parseXML(input);
		popUpProvider = new SMLPopupMenuProvider(dom, propertyChangeListener);
		setPartName(input.getName());
		site.setSelectionProvider(getTreeViewer());

		makeActions();
		site.getPage().addPropertyChangeListener(propertyChangeListener);
		site.getPage().addPartListener(partListener);
	}

	/**
	 * Parses the XML file and extracts the DOM object
	 * 
	 * @param input
	 * @return a W3C Document object
	 */
	protected void parseXML(IEditorInput input) {
		if (input instanceof FileEditorInput) {
			try {
				IFile file = ((FileEditorInput) input).getFile();
				file.refreshLocal(Resource.DEPTH_INFINITE, null);
				InputStream is = file.getContents();

				dom = new DOMHelper(is, true);

			} catch (CoreException e) {
				e.printStackTrace();
			} catch (DOMHelperException e) {
				e.printStackTrace();
			}
		}
	}

	public void dispose() {
		super.dispose();
		dom = null;
	}

	/**
	 * Add editor specific actions to menu bar
	 */
	private void makeActions() {

	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		if (getTreeViewer() != null)
			getTreeViewer().getControl().setFocus();
	}

	@Override
	public boolean isDirty() {
		return fileIsDirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	public boolean showOutlineView() {
		return true;
	}

	public Object getAdapter(Class key) {
		if (key.equals(IPropertySheetPage.class)) {
			return getTabbedSheet();
		} else if (key.equals(IContentOutlinePage.class)) {
			return showOutlineView() ? getSensorMLOutlinePage() : null;
		} else {
			return super.getAdapter(key);
		}
	}

	public TabbedPropertySheetPage getTabbedSheet() {
		if (tabbedPropertyPage == null) {
			tabbedPropertyPage = new SMLTabbedPropertySheetPage(this);
			
		}
		return tabbedPropertyPage;
	}

	public SensorMLContentOutlinePage getSensorMLOutlinePage() {
		if (sensorMLOutline == null) {
			sensorMLOutline = new SensorMLContentOutlinePage(this, dom);
		}
		return sensorMLOutline;
	}

	public void handleContentOutlineSelection(ISelection selection) {
		if (!selection.isEmpty() && selection instanceof IStructuredSelection) {
			Iterator selectedElements = ((IStructuredSelection) selection)
					.iterator();
			if (selectedElements.hasNext()) {
				Object selectedElement = selectedElements.next();
				ArrayList selectionList = new ArrayList();
				selectionList.add(selectedElement);
				while (selectedElements.hasNext()) {
					selectionList.add(selectedElements.next());
				}
				getTreeViewer().setSelection(
						new StructuredSelection(selectionList));
			}
		}
	}

	public Object getInput() {
		return getTreeViewer().getInput();
	}

	public void handleSelectionChanged(SelectionChangedEvent event) {

		final IStructuredSelection sel = (IStructuredSelection) event
				.getSelection();
		updateStatusLine(sel);
		ISelection sel1 = viewer.getSelection();
		if (tabbedPropertyPage != null) {
			tabbedPropertyPage.selectionChanged(this, sel1);
		}
	}

	public IActionBars getActionBars() {
		return getEditorSite().getActionBars();
	}

	@Override
	public String getContributorId() {
		// TODO Auto-generated method stub
		String id = getSite().getId();
		return id;
	}

	@Override
	public Shell getShell() {
		Shell currentShell = PlatformUI.getWorkbench().getDisplay()
			.getActiveShell();
		return currentShell;
	}

}