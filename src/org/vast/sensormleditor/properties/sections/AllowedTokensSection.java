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
import org.vast.sensormleditor.outlineview.SensorMLContentOutlinePage;
import org.vast.sensormleditor.properties.descriptors.AllowedTokensPropertySource;
import org.vast.sensormleditor.util.DOMHelperAddOn;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;

public class AllowedTokensSection extends AbstractPropertySection {

	private String headerLabel = "Allowed Tokens";
	private CLabel headerlabelLabel;


	private Element element;
	private DOMHelper dom;
	private TreeViewer treeViewer;
	
	private Text newListText;
	private String newListLabel = "New List Item:";
	private CLabel newListlabelLabel;
	private Button addListButton;
	private Button removeListButton;

	private List sweValueList;
	private String sweListLabel = "List Items:";
	private CLabel sweListlabelLabel;
	
	private SMLTreeEditor smlEditor;
	private AllowedTokensPropertySource propertySource;
	private Font fontBold = new Font(PlatformUI.getWorkbench().getDisplay(),
			"Tahoma", 8, SWT.BOLD);
	private IPropertyDescriptor[] desc;


	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		Assert.isTrue(selection instanceof IStructuredSelection);
		Object input = ((IStructuredSelection) selection).getFirstElement();
		Assert.isTrue(input instanceof Element);
		this.element = (Element) input;

		if (part instanceof SMLTreeEditor) {
			smlEditor = (SMLTreeEditor) part;
			dom = smlEditor.getModel();
			treeViewer = (TreeViewer) ((SMLTreeEditor) part).getTreeViewer();
		} else {
			smlEditor = (SMLTreeEditor) ((SensorMLContentOutlinePage) part).smlEditor;
			dom = smlEditor.getModel();
			treeViewer = smlEditor.getTreeViewer();
		}
		Element retNode = null;
		DOMHelperAddOn domUtil = new DOMHelperAddOn(dom);
		Element chosenOne = domUtil.getChosenField(element, retNode);
		if (chosenOne != null)
			element = chosenOne;
		if (element != null) {
			propertySource = new AllowedTokensPropertySource(element, dom,
					treeViewer, smlEditor);
		}
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
		
		newListText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(headerlabelLabel);
		newListText.setLayoutData(data);

		newListlabelLabel = getWidgetFactory().createCLabel(composite,
				newListLabel); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(newListText,
				ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(headerlabelLabel);
		newListlabelLabel.setLayoutData(data);
		
		addListButton = getWidgetFactory().createButton(composite,
				"Add List Value", SWT.PUSH);
		data = new FormData();
		data.left = new FormAttachment(newListText,
				ITabbedPropertyConstants.HSPACE);
		data.right = new FormAttachment(75);
		data.top = new FormAttachment(headerlabelLabel);
		addListButton.setLayoutData(data);
		addListButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String newidentifier = newListText.getText();
				if (newidentifier != null && newidentifier != "") {
					sweValueList.add(newidentifier);
					propertySource
							.setPropertyValue(
									AllowedTokensPropertySource.PROPERTY_SWELIST_VALUE,
									newListText.getText());
					newListText.setText("");
					newListText.setFocus();
				}
			}
		});
		
		removeListButton = getWidgetFactory().createButton(composite,
				"Remove List Value", SWT.PUSH);
		data = new FormData();
		data.left = new FormAttachment(newListText,
				ITabbedPropertyConstants.HSPACE);
		data.right = new FormAttachment(75);
		data.top = new FormAttachment(addListButton);
		removeListButton.setLayoutData(data);
		removeListButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int index = sweValueList.getSelectionIndex();
				String remfield = sweValueList.getItem(index);
				if (remfield != null && remfield != "") {

					desc = propertySource.getPropertyDescriptors();
					for (int i = 0; i < desc.length; i++) {
						String item = (String) propertySource
								.getPropertyValue(desc[i].getId());

						if (item.equals(remfield)) {
							propertySource.removeAllowedValue(desc[i].getId(),
									index);
							break;
						}
					}
					sweValueList.remove(index);
				}
			}
		});
		
		sweValueList = getWidgetFactory().createList(composite,
				SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		data = new FormData();
		sweValueList.setBackground(new Color(null, 224, 255, 255));
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(newListText,
				ITabbedPropertyConstants.VSPACE);
		sweValueList.setLayoutData(data);

		sweListlabelLabel = getWidgetFactory().createCLabel(composite,
				sweListLabel); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(sweValueList,
				ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(newListText,
				ITabbedPropertyConstants.VSPACE);
		sweListlabelLabel.setLayoutData(data);
	}

	public void refresh() {
		sweValueList.removeAll();
		desc = propertySource.getPropertyDescriptors();
		for (int i = 0; i < desc.length; i++) {
			String item = (String) propertySource.getPropertyValue(desc[i]
					.getId());
			if ((item != null) && !item.equals(""))
				sweValueList.add(item);
		}
	}

}
