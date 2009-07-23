package org.vast.sensormleditor.properties.sections;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.outlineview.SensorMLContentOutlinePage;
import org.vast.sensormleditor.properties.descriptors.ElementChoicePropertySource;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;

public class ElementChoicePropertySection extends AbstractPropertySection {

	private CCombo valueChoice;
	private String valueLabel = "Type:";
	private CLabel valuelabelLabel;
	private SMLTreeEditor smlEditor;
	private Element element;
	private DOMHelper dom;
	private TreeViewer treeViewer;

	private ElementChoicePropertySource propertySource;

	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		Assert.isTrue(selection instanceof IStructuredSelection);
		Object input = ((IStructuredSelection) selection).getFirstElement();
		Assert.isTrue(input instanceof Element);
		this.element = (Element) input;
		if (part instanceof SMLTreeEditor) {
			smlEditor = ((SMLTreeEditor) part);
			dom = smlEditor.getModel();
			treeViewer = (TreeViewer) ((SMLTreeEditor) part).getTreeViewer();
		} else {
			SMLTreeEditor smlEditor = (SMLTreeEditor) ((SensorMLContentOutlinePage) part).smlEditor;
			dom = smlEditor.getModel();
			treeViewer = smlEditor.getTreeViewer();
		}

		propertySource = new ElementChoicePropertySource(element, dom,
				treeViewer, smlEditor);

		 String numType = propertySource.getTitle();
		 valuelabelLabel.setText(numType+":");
	}

	public void dispose() {
		valueChoice.dispose();
		valuelabelLabel.dispose();

	}

	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {

		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite = getWidgetFactory()
				.createFlatFormComposite(parent);

		FormData data;
		valueChoice = getWidgetFactory().createCCombo(composite,
				SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
		valueChoice.setLayoutData(data);
		valueChoice.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
					propertySource.setPropertyValue(
							ElementChoicePropertySource.PROPERTY_ELEMENT_CHOICE,
							valueChoice.getSelectionIndex());
			}	
		});

		valuelabelLabel = getWidgetFactory()
				.createCLabel(composite, valueLabel); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(valueChoice,
				ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
		valuelabelLabel.setLayoutData(data);
	}

	public void refresh() {
		valueChoice.removeAll();
		if (ElementChoicePropertySource.PROPERTY_ELEMENT_CHOICE != null) {
			String[] choices = (String[]) propertySource
					.getChoices(ElementChoicePropertySource.PROPERTY_ELEMENT_CHOICE);
			for (int i = 0; i < choices.length; i++) {
				if (choices[i]!= null)
					valueChoice.add(choices[i]);
			}
			Object selected = propertySource.getPropertyValue(ElementChoicePropertySource.PROPERTY_ELEMENT_CHOICE);
			Integer intVal = (Integer) selected;
			
			valueChoice.select(intVal.intValue());
		}
	}

}
