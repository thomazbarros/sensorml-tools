package org.vast.sensormleditor.properties.sections;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.outlineview.SensorMLContentOutlinePage;
import org.vast.sensormleditor.properties.descriptors.DataArrayElementCountPropertySource;
import org.vast.sensormleditor.util.DOMHelperAddOn;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;

public class DataArrayElementCountSection extends AbstractPropertySection {

	private Text countText;
	private String countLabel = "Element Count Code:";
	private CLabel countlabelLabel;
	
	private CCombo valueChoice;
	private String valueLabel = "Count Type:";
	private CLabel valuelabelLabel;
	private SMLTreeEditor smlEditor;
	private Element element;
	private DOMHelper dom;
	private TreeViewer treeViewer;
	private DataArrayElementCountPropertySource propertySource;

	
	private ModifyListener listener = new ModifyListener() {

		public void modifyText(ModifyEvent arg0) {
			propertySource.setPropertyValue(
					DataArrayElementCountPropertySource.PROPERTY_ELEMENT_COUNT,
					countText.getText());
		}
	};

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

		//propertySource = new DataArrayElementCountPropertySource(element, dom, treeViewer,smlEditor);
		Element retNode = null;
		DOMHelperAddOn domUtil = new DOMHelperAddOn(dom);
		Element chosenOne = domUtil.getChosenField(element, retNode);
		if (chosenOne != null)
			element = chosenOne;
		if (element != null) {
			propertySource = new DataArrayElementCountPropertySource(element,
					dom, treeViewer, smlEditor);
		}
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
				int index = valueChoice.getSelectionIndex();
				
					propertySource.setPropertyValue(
							DataArrayElementCountPropertySource.PROPERTY_COUNT_CHOICE,
							valueChoice.getItem(index));
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
		
		countText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(valuelabelLabel, ITabbedPropertyConstants.VSPACE);
		countText.setLayoutData(data);
		countText.addModifyListener(listener);

		countlabelLabel = getWidgetFactory().createCLabel(composite, countLabel); //$NON-NLS-1$

		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(countText,
				ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(valueChoice, ITabbedPropertyConstants.VSPACE);
		countlabelLabel.setLayoutData(data);
	}

	public void refresh() {
		
		valueChoice.removeAll();
		if (DataArrayElementCountPropertySource.PROPERTY_COUNT_CHOICE != null) {
			String[] choices = (String[]) propertySource
					.getChoices(DataArrayElementCountPropertySource.PROPERTY_COUNT_CHOICE);
			for (int i = 0; i < choices.length; i++) {
				if (choices[i]!= null)
					valueChoice.add(choices[i]);
			}
			String selected = (String)propertySource.getPropertyValue(DataArrayElementCountPropertySource.PROPERTY_COUNT_CHOICE);
			int index = valueChoice.indexOf(selected);
			valueChoice.select(index);
		}
		
		countText.removeModifyListener(listener);
		if (DataArrayElementCountPropertySource.PROPERTY_ELEMENT_COUNT != null) {
			String uomValue = (String) propertySource
					.getPropertyValue(DataArrayElementCountPropertySource.PROPERTY_ELEMENT_COUNT);

			if (uomValue != null)
				countText.setText(uomValue);
		}
		countText.addModifyListener(listener);

	}

}
