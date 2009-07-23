package org.vast.sensormleditor.properties.sections;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.outlineview.SensorMLContentOutlinePage;
import org.vast.sensormleditor.properties.descriptors.DataArrayValuesPropertySource;
import org.vast.sensormleditor.util.DOMHelperAddOn;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;

public class DataArrayValuesSection extends AbstractPropertySection {

	private String headerLabel = "Data Array Values (optional)";
	private CLabel headerlabelLabel;

	private Button listRadioButton;
	private String listRadioLabel = "swe.values";

	private Button xlinkRadioButton;
	private String xlinkRadioLabel = "xlink.href";

	private static int DATA_HEIGHT = 30;
	private Element element;
	private DOMHelper dom;
	private TreeViewer treeViewer;

	private Text xlinkText;
	
	private Text listText;
	/*private String newListLabel = "Array Values:";
	private CLabel newListlabelLabel;*/
	/*private Button addListButton;
	private Button removeListButton;

	private List sweValueList;
	private String sweListLabel = "Array Values:";
	private CLabel sweListlabelLabel;*/

	private SMLTreeEditor smlEditor;
	private DataArrayValuesPropertySource propertySource;
	private Font fontBold = new Font(PlatformUI.getWorkbench().getDisplay(),
			"Tahoma", 8, SWT.BOLD);

	private ModifyListener listener = new ModifyListener() {

		public void modifyText(ModifyEvent arg0) {
			if (xlinkText.getText() != "") {
				propertySource.setPropertyValue(
						DataArrayValuesPropertySource.PROPERTY_XLINK_HREF,
						xlinkText.getText());
			}else if (listText.getText() != "") {
				propertySource.setPropertyValue(
						DataArrayValuesPropertySource.PROPERTY_SWELIST_VALUE,
						listText.getText());
			}

		}
	};

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
			propertySource = new DataArrayValuesPropertySource(element, dom,
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
		data.left = new FormAttachment(0, ITabbedPropertyConstants.HSPACE);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);

		xlinkText = getWidgetFactory().createText(composite, "");
		xlinkText.setToolTipText("xlink.href");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(30, -5);
		data.top = new FormAttachment(headerlabelLabel, 5);
		xlinkText.setLayoutData(data);
		xlinkText.setEnabled(false);
		xlinkText.addModifyListener(listener);

		xlinkRadioButton = getWidgetFactory().createButton(composite,
				xlinkRadioLabel, SWT.RADIO);
		data = new FormData();
		data.left = new FormAttachment(0, ITabbedPropertyConstants.HSPACE);
		data.right = new FormAttachment(xlinkText,
				ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(headerlabelLabel, 5);
		data.height = DATA_HEIGHT;
		xlinkRadioButton.setLayoutData(data);
		xlinkRadioButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Button button = (Button) event.widget;
				if (!button.getSelection())
					return;
				enableXlinkOnly();
				propertySource.setPropertyValue(
						DataArrayValuesPropertySource.PROPERTY_CONSTRAINT_CHOICE,
						"xlink");
			}
		});

		listText = getWidgetFactory().createText(composite, "",
				SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		listText.setBackground(new Color(null, 224, 255, 255));
		
		listText
				.setToolTipText("List of space separated decimals \n(e.g. 3.4 5.6 10 300.222)");
		
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(30, -5);
		data.top = new FormAttachment(xlinkText, 5);
		data.height = 100;
		data.width = 300;
		listText.setEnabled(false);
		listText.setLayoutData(data);
		listRadioButton = getWidgetFactory().createButton(composite,
				listRadioLabel, SWT.RADIO);
		/*newListlabelLabel = getWidgetFactory().createCLabel(composite,
				newListLabel); //$NON-NLS-1$
*/
		data = new FormData();
		data.left = new FormAttachment(0, ITabbedPropertyConstants.HSPACE);
		data.right = new FormAttachment(listText,
				ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(xlinkRadioButton, 5);
		data.height = DATA_HEIGHT;
		listRadioButton.setLayoutData(data);
		listRadioButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Button button = (Button) event.widget;
				if (!button.getSelection())
					return;
				enableListOnly();
				propertySource.setPropertyValue(
						DataArrayValuesPropertySource.PROPERTY_CONSTRAINT_CHOICE,
						"values");
			}
		});

		/*data = new FormData();
		data.left = new FormAttachment(0, 30);
		data.right = new FormAttachment(10, 0);
		data.top = new FormAttachment(listRadioButton, 5);*/
		/*newListlabelLabel.setEnabled(false);
		newListlabelLabel.setLayoutData(data);*/

		

		/*sweListlabelLabel = getWidgetFactory().createCLabel(composite,
				sweListLabel); //$NON-NLS-1$
		sweValueList = getWidgetFactory().createList(composite,
				SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);

		data = new FormData();
		data.left = new FormAttachment(0, 30);
		data.right = new FormAttachment(10, 0);
		data.top = new FormAttachment(newListlabelLabel, 5);
		sweListlabelLabel.setEnabled(false);
		sweListlabelLabel.setLayoutData(data);

		data = new FormData();
		sweValueList.setBackground(new Color(null, 224, 255, 255));
		data.left = new FormAttachment(sweListlabelLabel, 5);
		data.right = new FormAttachment(30, -5);
		sweValueList.setEnabled(false);
		data.top = new FormAttachment(newListlabelLabel, 5);
		sweValueList.setLayoutData(data);

		addListButton = getWidgetFactory().createButton(composite, "Add List",
				SWT.PUSH);
		data = new FormData();
		data.left = new FormAttachment(listText,
				ITabbedPropertyConstants.HSPACE);
		data.right = new FormAttachment(40, -5);
		data.top = new FormAttachment(listRadioButton, 5);
		addListButton.setLayoutData(data);
		addListButton.setEnabled(false);
		addListButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String newidentifier = listText.getText();
				if (newidentifier != null && newidentifier != "") {
					sweValueList.add(newidentifier);
					propertySource.setPropertyValue(
							DataArrayValuesPropertySource.PROPERTY_SWELIST_VALUE,
							listText.getText());
					listText.setText("");
					listText.setFocus();
				}
			}
		});

		removeListButton = getWidgetFactory().createButton(composite,
				"Remove List", SWT.PUSH);
		data = new FormData();
		data.left = new FormAttachment(sweValueList, 5);
		data.right = new FormAttachment(40, -5);
		data.top = new FormAttachment(addListButton, 5);
		removeListButton.setLayoutData(data);
		removeListButton.setEnabled(false);
		removeListButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int index = sweValueList.getSelectionIndex();
				String remfield = sweValueList.getItem(index);
				if (remfield != null && remfield != "") {
					IPropertyDescriptor[] desc = propertySource
							.getListPropertyDescriptors();
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
		});*/


	}

	public void refresh() {
		xlinkText.removeModifyListener(listener);
		listText.removeModifyListener(listener);
		
		if (DataArrayValuesPropertySource.PROPERTY_CONSTRAINT_CHOICE != null) {
			String selected = (String) propertySource
					.getPropertyValue(DataArrayValuesPropertySource.PROPERTY_CONSTRAINT_CHOICE);
			if (selected.equals("xlink")) {
				xlinkRadioButton.setSelection(true);
				enableXlinkOnly();
			} else if (selected.equals("values")) {
				listRadioButton.setSelection(true);
				enableListOnly();
			}
		}
	
		if (DataArrayValuesPropertySource.PROPERTY_SWELIST_VALUE != null){
			String idValue = (String) propertySource
				.getPropertyValue(DataArrayValuesPropertySource.PROPERTY_SWELIST_VALUE);
			if (idValue != null)
				listText.setText(idValue);
			else
				listText.setText("");
			}
		listText.addModifyListener(listener);
		
		if (DataArrayValuesPropertySource.PROPERTY_XLINK_HREF != null) {

			String idValue = (String) propertySource
					.getPropertyValue(DataArrayValuesPropertySource.PROPERTY_XLINK_HREF);
			if (idValue != null)
				xlinkText.setText(idValue);
			else
				xlinkText.setText("");

		}
		xlinkText.addModifyListener(listener);


	}

	public void enableXlinkOnly() {
		xlinkText.setEnabled(true);
		
		listText.setText("");
		listText.setEnabled(false);
		
		/*sweListlabelLabel.setEnabled(false);
		sweValueList.removeAll();
		sweValueList.setEnabled(false);
		addListButton.setEnabled(false);
		removeListButton.setEnabled(false);*/
	}


	public void enableListOnly() {
		xlinkText.setText("");
		xlinkText.setEnabled(false);
		listText.setEnabled(true);
		/*sweListlabelLabel.setEnabled(true);
		sweValueList.setEnabled(true);
		addListButton.setEnabled(true);
		removeListButton.setEnabled(true);*/
	}

}
