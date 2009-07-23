package org.vast.sensormleditor.properties.sections;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.outlineview.SensorMLContentOutlinePage;
import org.vast.sensormleditor.properties.descriptors.ElementChoicePropertySource;
import org.vast.sensormleditor.properties.descriptors.VectorPropertySource;
import org.vast.sensormleditor.util.DOMHelperAddOn;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;

public class VectorSection extends AbstractPropertySection {

	//private String headerLabel = "Data Record";
	//private CLabel headerlabelLabel;

	private String selectedFieldsLabel = "Selected Fields:";
	private CLabel selectedlabelLabel;
	private String availableFieldsLabel = "Available Fields:";
	private CLabel availablelabelLabel;

	private Button removeFieldButton;
	private Button addFieldButton;
	private List sweChoicesList;

	private List sweFieldList;
	private SMLTreeEditor smlEditor;
	private Element element;
	private DOMHelper dom;
	private TreeViewer treeViewer;
	private IPropertyDescriptor[] desc;
	private VectorPropertySource propertySource;
	private ElementChoicePropertySource choicepropertySource;

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
			smlEditor = (SMLTreeEditor) ((SensorMLContentOutlinePage) part).smlEditor;
			dom = smlEditor.getModel();
			treeViewer = smlEditor.getTreeViewer();
		}

		if (dom.hasQName(element, "swe:Vector")) {
			propertySource = new VectorPropertySource(element, dom,
					treeViewer, smlEditor);
			choicepropertySource = new ElementChoicePropertySource(element,
					dom, treeViewer, smlEditor);
		} else {
			
			Element retNode = null;
			DOMHelperAddOn domUtil = new DOMHelperAddOn(dom);
			Element chosenOne = domUtil.getChosenField(element, retNode);
			if (chosenOne != null)
				element = chosenOne;
			if (element != null) {
				propertySource = new VectorPropertySource(element, dom,
						treeViewer, smlEditor);
				choicepropertySource = new ElementChoicePropertySource(element,
						dom, treeViewer, smlEditor);
			}
		}
	}



	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite = getWidgetFactory()
				.createFlatFormComposite(parent);

		FormData data;
		/*
		 * headerlabelLabel = getWidgetFactory().createCLabel(composite,
		 * headerLabel); //$NON-NLS-1$ headerlabelLabel.setFont(fontBold); data
		 * = new FormData(); data.left = new FormAttachment(0, 0); data.right =
		 * new FormAttachment(50, ITabbedPropertyConstants.HSPACE); data.top =
		 * new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
		 * headerlabelLabel.setLayoutData(data);
		 */
		availablelabelLabel = getWidgetFactory().createCLabel(composite,
				availableFieldsLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 250);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
		availablelabelLabel.setLayoutData(data);

		sweChoicesList = getWidgetFactory().createList(composite,
				SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		data = new FormData();
		sweChoicesList.setBackground(new Color(null, 224, 255, 255));
		data.left = new FormAttachment(0, 250);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(availablelabelLabel,
				ITabbedPropertyConstants.VSPACE);
		sweChoicesList.setLayoutData(data);

		selectedlabelLabel = getWidgetFactory().createCLabel(composite,
				selectedFieldsLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0,
				ITabbedPropertyConstants.VSPACE);
		selectedlabelLabel.setLayoutData(data);

		addFieldButton = getWidgetFactory().createButton(composite,
				"Add Field", SWT.PUSH);
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(sweChoicesList);
		data.top = new FormAttachment(selectedlabelLabel);
		addFieldButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int index = sweChoicesList.getSelectionIndex();
				String newfield = sweChoicesList.getItem(index);
				if (newfield != null && newfield != "") {
					sweFieldList.add(newfield);
					propertySource.setPropertyValue(
							VectorPropertySource.PROPERTY_SWE_COORDINATE,
							"Enter name...");
					Element field = (Element) propertySource.getProvider()
							.getNode();
					choicepropertySource = new ElementChoicePropertySource(
							field, dom, treeViewer, smlEditor);
					choicepropertySource
							.setPropertyValue(
									ElementChoicePropertySource.PROPERTY_ELEMENT_CHOICE,
									newfield);
				}
			}
		});
		addFieldButton.setLayoutData(data);

		removeFieldButton = getWidgetFactory().createButton(composite,
				"Remove Field", SWT.PUSH);
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(sweChoicesList);
		data.top = new FormAttachment(addFieldButton);
		removeFieldButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int index = sweFieldList.getSelectionIndex();
				String remfield = sweFieldList.getItem(index);
				if (remfield != null && remfield != "") {

					desc = choicepropertySource.getPropertyDescriptors();
					for (int i = 0; i < desc.length; i++) {
						String item = (String) choicepropertySource
								.getPropertyValue(desc[i].getId());

						if (item.equals(remfield)) {
							choicepropertySource.removeField(desc[i].getId(),
									index);
							break;
						}
					}
					sweFieldList.remove(index);
				}
			}
		});
		removeFieldButton.setLayoutData(data);

		sweFieldList = getWidgetFactory().createList(composite,
				SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		data = new FormData();
		sweFieldList.setBackground(new Color(null, 224, 255, 255));
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(addFieldButton);
		data.top = new FormAttachment(selectedlabelLabel, 0);
		sweFieldList.setLayoutData(data);

	}

	public void refresh() {

		sweFieldList.removeAll();
		desc = choicepropertySource.getPropertyDescriptors();
		for (int i = 0; i < desc.length; i++) {
			String item = (String) choicepropertySource
					.getPropertyValue(desc[i].getId());

			if ((item != null) && !item.equals("")) {
				sweFieldList.add(item);
			}
		}

		sweChoicesList.removeAll();
		if (ElementChoicePropertySource.PROPERTY_ELEMENT_CHOICE != null) {
			String[] choices = (String[]) choicepropertySource
					.getChoices(ElementChoicePropertySource.PROPERTY_ELEMENT_CHOICE);
			if (choices != null) {
				for (int i = 0; i < choices.length; i++) {
					if (choices[i] != null)
						sweChoicesList.add(choices[i]);
				}
			}
		}

	}
}
