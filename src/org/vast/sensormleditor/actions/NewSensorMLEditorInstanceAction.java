package org.vast.sensormleditor.actions;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RefreshAction;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.ide.IDE;
import org.vast.sensormleditor.config.Configuration;
import org.vast.sensormleditor.relaxNG.Hybrid2XML;
import org.vast.sensormleditor.relaxNG.Relax2Hybrid;
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;
import org.vast.xml.transform.DOMTransformException;

public class NewSensorMLEditorInstanceAction extends Action implements
		ISelectionListener, ActionFactory.IWorkbenchAction, IShellProvider {

	String coreSchema;
	private SaveAsDialog saveAsDialog;
	private final IWorkbenchWindow window;
	public final static String ID = "SensorMLEditor.NewInstance";
	private IStructuredSelection selection;

/*	public NewSensorMLEditorInstanceAction() {
		window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		setId(ID);
		setText("&New SensorML Instance");
		setToolTipText("Create New SensorML Instance");
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(IDE.SharedImages.IMG_OBJS_TASK_TSK));
		window.getSelectionService().addSelectionListener(this);
		coreSchema = Configuration.getSchema();
	}*/
	
	public NewSensorMLEditorInstanceAction(IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setText("&New SensorML Instance");
		setActionDefinitionId (ID);
		setToolTipText("Create New SensorML Instance");
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(IDE.SharedImages.IMG_OBJS_TASK_TSK));
		window.getSelectionService().addSelectionListener(this);
		coreSchema = Configuration.getSchema();
	}

	@Override
	public void dispose() {
		window.getSelectionService().removeSelectionListener(this);
	}

	@Override
	public void run() {

		saveAsDialog = new SaveAsDialog(window.getShell());
		int code = saveAsDialog.open();
		if (code == Window.OK){
			IPath path = saveAsDialog.getResult();
			saveAsDialog.close();
			if (path != null) {
				IPath workspacePath = ResourcesPlugin.getWorkspace().getRoot()
					.getLocation();

			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			if (file != null) {
				String filePath = file.getFullPath().toString();
				String newFile = workspacePath + filePath;
				if (newFile.endsWith(".xml") || newFile.endsWith(".rng"))
					newFile = newFile.substring(0, newFile.length() - 4);

				FileOutputStream outputStream;
				DOMHelper sourceDoc = null;
				try {
					sourceDoc = new DOMHelper(coreSchema, false);
					
				} catch (DOMHelperException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Relax2Hybrid transform2Hybrid = new Relax2Hybrid();
				DOMHelper hybrid = null;
				try {
					hybrid = transform2Hybrid.transform(sourceDoc);
					
				} catch (DOMTransformException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// Saving the rng file
				try {
					outputStream = new FileOutputStream(newFile + ".rng");
					try {
						hybrid.serialize(hybrid.getDocument(), outputStream,
								true);
					} catch (IOException e) {
						e.printStackTrace();
					}

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Save the xml file
				outputStream = null;
				Hybrid2XML transform2XML = new Hybrid2XML();
				DOMHelper xmlFormat = null;

				try {
					xmlFormat = transform2XML.transform(hybrid);
				} catch (DOMTransformException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					outputStream = new FileOutputStream(newFile + ".xml");
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
			}
		}

		 RefreshAction raction = new RefreshAction(this);
		 raction.refreshAll();
		;}

	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection incoming) {
		if (incoming instanceof IStructuredSelection){
			selection = (IStructuredSelection)incoming;
			//setEnabled(selection.size() == 1 && selection.getFirstElement() instanceof IResource);
			setEnabled(true);
		} else {
			setEnabled(false);
		}
		
	}

	@Override
	public Shell getShell() {
		Shell currentShell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		return currentShell;
	}

}
