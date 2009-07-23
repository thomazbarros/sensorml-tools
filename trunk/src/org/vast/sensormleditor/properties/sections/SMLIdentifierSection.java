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
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.properties.descriptors.SMLIdentifierPropertySource;
import org.vast.sensormleditor.util.DOMHelperAddOn;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;

public class SMLIdentifierSection extends AbstractPropertySection {

	private String headerLabel = "Identifier List";
	private CLabel headerlabelLabel;

	private Text newIdentiferText;
	private String newIdentifierLabel = "New Identifier:";
	private CLabel newIdentifierlabelLabel;
	private Button addIdentiferButton;
	private Button removeIdentiferButton;

	private List smlIdentifiersList;
	private String smlIdentifiers = "Identifiers:";
	private CLabel smlIdentifierslabelLabel;
	private IPropertyDescriptor[] desc;
	private SMLTreeEditor smlEditor;
	private Element element;
	private DOMHelper dom;
	
	private TreeViewer treeViewer;
	private SMLIdentifierPropertySource propertySource;
	private Font fontBold = new Font(PlatformUI.getWorkbench().getDisplay(),
			"Tahoma", 8, SWT.BOLD);

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
		}
		
		
		Element retNode = null;
		DOMHelperAddOn domUtil = new DOMHelperAddOn(dom);
		Element chosenOne = domUtil.getChosenField(element, retNode);
		if (chosenOne != null)
			element = chosenOne;
		propertySource = new SMLIdentifierPropertySource(element, dom,
				treeViewer, smlEditor);

	}
	


	public void dispose() {
		headerlabelLabel.dispose();
		smlIdentifiersList.dispose();
		smlIdentifierslabelLabel.dispose();
		fontBold.dispose();

	}

	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite = getWidgetFactory()
				.createFlatFormComposite(parent);
		FormData data;

		headerlabelLabel = getWidgetFactory().createCLabel(composite,
				headerLabel); //$NON-NLS-1$
		headerlabelLabel.setFont(fontBold);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);

		data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
		headerlabelLabel.setLayoutData(data);

		newIdentiferText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(headerlabelLabel);
		newIdentiferText.setLayoutData(data);

		newIdentifierlabelLabel = getWidgetFactory().createCLabel(composite,
				newIdentifierLabel); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(newIdentiferText,
				ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(headerlabelLabel);
		newIdentifierlabelLabel.setLayoutData(data);

		addIdentiferButton = getWidgetFactory().createButton(composite,
				"Add Identifier", SWT.PUSH);
		data = new FormData();
		data.left = new FormAttachment(newIdentiferText,
				ITabbedPropertyConstants.HSPACE);
		data.right = new FormAttachment(75);
		data.top = new FormAttachment(headerlabelLabel);
		addIdentiferButton.setLayoutData(data);
		addIdentiferButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String newidentifier = newIdentiferText.getText();
				if (newidentifier != null && newidentifier != "") {
					smlIdentifiersList.add(newidentifier);
					propertySource
							.setPropertyValue(
									SMLIdentifierPropertySource.PROPERTY_SML_IDENTIFIER,
									newIdentiferText.getText());
					newIdentiferText.setText("");
					newIdentiferText.setFocus();
				}
			}
		});

		smlIdentifiersList = getWidgetFactory().createList(composite,
				SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		data = new FormData();
		smlIdentifiersList.setBackground(new Color(null, 224, 255, 255));
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(newIdentiferText,
				ITabbedPropertyConstants.VSPACE);

		smlIdentifiersList.setLayoutData(data);

		smlIdentifierslabelLabel = getWidgetFactory().createCLabel(composite,
				smlIdentifiers); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(smlIdentifiersList,
				ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(newIdentiferText,
				ITabbedPropertyConstants.VSPACE);
		smlIdentifierslabelLabel.setLayoutData(data);
		
		removeIdentiferButton = getWidgetFactory().createButton(composite,
				"Remove Identifier", SWT.PUSH);
		data = new FormData();
		data.left = new FormAttachment(smlIdentifiersList,
				ITabbedPropertyConstants.HSPACE);
		data.right = new FormAttachment(75);
		data.top = new FormAttachment(newIdentiferText,
				ITabbedPropertyConstants.VSPACE);
		removeIdentiferButton.setLayoutData(data);
		removeIdentiferButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int selectionIndex = smlIdentifiersList.getSelectionIndex();
				desc = propertySource.getPropertyDescriptors();
				for (int i = 0; i < desc.length; i++) {
					String item = (String) propertySource
							.getPropertyValue(desc[i].getId());
					if (item.equals(smlIdentifiersList.getItem(selectionIndex))) {
						propertySource.setPropertyValue(desc[i].getId(), "");
					}
				}
				smlIdentifiersList.remove(selectionIndex);
			}
		});
	}

	public void refresh() {

		smlIdentifiersList.removeAll();
		if (propertySource != null) {
			desc = propertySource.getPropertyDescriptors();
			for (int i = 0; i < desc.length; i++) {
				String item = (String) propertySource.getPropertyValue(desc[i]
						.getId());

				if ((item != null) && !item.equals(""))
					smlIdentifiersList.add(item);
			}
			smlIdentifiersList.select(0);
		}
		String newValue = (String) propertySource
				.getPropertyValue(SMLIdentifierPropertySource.PROPERTY_SML_IDENTIFIER);
		if (newValue != null)
			newIdentiferText.setText(newValue);
	}

}
