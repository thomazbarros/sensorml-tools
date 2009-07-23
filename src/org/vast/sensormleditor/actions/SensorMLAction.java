package org.vast.sensormleditor.actions;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.RefreshAction;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.vast.sensormleditor.config.Configuration;
import org.vast.sensormleditor.relaxNG.Hybrid2XML;
import org.vast.sensormleditor.relaxNG.Relax2Hybrid;
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;
import org.vast.xml.transform.DOMTransformException;

/**
 * Our sample action implements workbench action delegate. The action proxy will
 * be created by the workbench and shown in the UI. When the user tries to use
 * the action, this delegate will be created and execution will be delegated to
 * it.
 * 
 * @see IWorkbenchWindowActionDelegate
 */
public class SensorMLAction implements IWorkbenchWindowActionDelegate,
		IShellProvider {
	private IWorkbenchWindow window;
	private String coreSchema;
	private SaveAsDialog saveAsDialog;
	private IStructuredSelection selection;
	public final static String ID = "SensorMLEditor.SensorMLAction";

	/**
	 * The constructor.
	 */
	public SensorMLAction(IWorkbenchWindow window) {

		coreSchema = Configuration.getSchema();
	}

	/**
	 * The action has been activated. The argument of the method represents the
	 * 'real' action sitting in the workbench UI.
	 * 
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {

		saveAsDialog = new SaveAsDialog(window.getShell());
		int code = saveAsDialog.open();
		if (code == Window.OK) {
			IPath path = saveAsDialog.getResult();
			saveAsDialog.close();
			if (path != null) {
				IPath workspacePath = ResourcesPlugin.getWorkspace().getRoot()
						.getLocation();

				IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(
						path);
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
							hybrid.serialize(hybrid.getDocument(),
									outputStream, true);
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
			;
		}

	}

	/**
	 * Selection in the workbench has been changed. We can change the state of
	 * the 'real' action here if we want, but this can only happen after the
	 * delegate has been created.
	 * 
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection incoming) {
		if (incoming instanceof IStructuredSelection) {
			selection = (IStructuredSelection) incoming;
		}
		/*
		 * if (selection.size() == 1 && selection.getFirstElement() instanceof
		 * IProject) { } else { return; } }
		 */

	}

	/**
	 * We can use this method to dispose of any system resources we previously
	 * allocated.
	 * 
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to be able to provide parent shell
	 * for the message dialog.
	 * 
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	@Override
	public Shell getShell() {
		Shell currentShell = PlatformUI.getWorkbench().getDisplay()
				.getActiveShell();
		return currentShell;
	}
}