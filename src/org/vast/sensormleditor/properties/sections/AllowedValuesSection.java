package org.vast.sensormleditor.properties.sections;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.outlineview.SensorMLContentOutlinePage;
import org.vast.sensormleditor.properties.descriptors.AllowedValuesPropertySource;
import org.vast.sensormleditor.util.DOMHelperAddOn;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;

public class AllowedValuesSection extends AbstractPropertySection {

	private String headerLabel = "Allowed Values";
	private CLabel headerlabelLabel;

	private Button listRadioButton;
	private String listRadioLabel = "Intervals and Lists";

	private Button minRadioButton;
	private String minRadioLabel = "Min Only Value";

	private Button maxRadioButton;
	private String maxRadioLabel = "Max Only Value";

	private static int DATA_HEIGHT = 30;
	private Element element;
	private DOMHelper dom;
	private TreeViewer treeViewer;

	private Text minText;
	private Text maxText;

	private Text intervalMinText;
	private CLabel intervalMinlabelLabel;
	private String intervalMinLabel = "Interval Min:";

	private Text intervalMaxText;
	private CLabel intervalMaxlabelLabel;
	private String intervalMaxLabel = "Interval Max:";

	private List intervalList;
	private CLabel intervalListlabelLabel;
	private String intervalListLabel = "Intervals:";
	private Button addIntervalButton;
	private Button removeIntervalButton;

	private Text listText;
	private String newListLabel = "New List:";
	private CLabel newListlabelLabel;
	private Button addListButton;
	private Button removeListButton;

	private List sweValueList;
	private String sweListLabel = "Lists:";
	private CLabel sweListlabelLabel;

	private SMLTreeEditor smlEditor;
	private AllowedValuesPropertySource propertySource;
	private Font fontBold = new Font(PlatformUI.getWorkbench().getDisplay(),
			"Tahoma", 8, SWT.BOLD);

	private ModifyListener listener = new ModifyListener() {

		public void modifyText(ModifyEvent arg0) {
			if (minText.getText() != "") {
				propertySource.setPropertyValue(
						AllowedValuesPropertySource.PROPERTY_CONSTRAINT_MIN,
						minText.getText());
			}
			if (maxText.getText() != "") {
				propertySource.setPropertyValue(
						AllowedValuesPropertySource.PROPERTY_CONSTRAINT_MAX,
						maxText.getText());
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
			propertySource = new AllowedValuesPropertySource(element, dom,
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

		minText = getWidgetFactory().createText(composite, "");
		minText.setToolTipText("Minimum Decimal Value");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(30, -5);
		data.top = new FormAttachment(headerlabelLabel, 5);
		minText.setLayoutData(data);
		minText.setEnabled(false);
		minText.addModifyListener(listener);

		minRadioButton = getWidgetFactory().createButton(composite,
				minRadioLabel, SWT.RADIO);
		data = new FormData();
		data.left = new FormAttachment(0, ITabbedPropertyConstants.HSPACE);
		data.right = new FormAttachment(minText,
				ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(headerlabelLabel, 5);
		data.height = DATA_HEIGHT;
		minRadioButton.setLayoutData(data);
		minRadioButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Button button = (Button) event.widget;
				if (!button.getSelection())
					return;
				enableMinOnly();
				propertySource.setPropertyValue(
						AllowedValuesPropertySource.PROPERTY_CONSTRAINT_CHOICE,
						"min");
			}
		});

		maxText = getWidgetFactory().createText(composite, "");
		maxText.setToolTipText("Maximum Decimal Value");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(30, -5);
		data.top = new FormAttachment(minRadioButton, 5);
		maxText.setLayoutData(data);
		maxText.setEnabled(false);
		maxText.addModifyListener(listener);

		maxRadioButton = getWidgetFactory().createButton(composite,
				maxRadioLabel, SWT.RADIO);
		data = new FormData();
		data.left = new FormAttachment(0, ITabbedPropertyConstants.HSPACE);
		data.right = new FormAttachment(maxText,
				ITabbedPropertyConstants.HSPACE);
		data.height = DATA_HEIGHT;
		data.top = new FormAttachment(minRadioButton, 5);
		maxRadioButton.setLayoutData(data);

		maxRadioButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Button button = (Button) event.widget;
				if (!button.getSelection())
					return;
				enableMaxOnly();
				propertySource.setPropertyValue(
						AllowedValuesPropertySource.PROPERTY_CONSTRAINT_CHOICE,
						"max");
			}
		});

		listText = getWidgetFactory().createText(composite, "");
		listText
				.setToolTipText("List of space separated decimals \n(e.g. 3.4 5.6 10 300.222)");
		listRadioButton = getWidgetFactory().createButton(composite,
				listRadioLabel, SWT.RADIO);
		newListlabelLabel = getWidgetFactory().createCLabel(composite,
				newListLabel); //$NON-NLS-1$

		data = new FormData();
		data.left = new FormAttachment(0, ITabbedPropertyConstants.HSPACE);
		data.right = new FormAttachment(listText,
				ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(maxRadioButton, 5);
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
						AllowedValuesPropertySource.PROPERTY_CONSTRAINT_CHOICE,
						"list");
			}
		});

		data = new FormData();
		data.left = new FormAttachment(0, 30);
		data.right = new FormAttachment(10, 0);
		data.top = new FormAttachment(listRadioButton, 5);
		newListlabelLabel.setEnabled(false);
		newListlabelLabel.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(newListlabelLabel, 5);
		data.right = new FormAttachment(30, -5);
		data.top = new FormAttachment(listRadioButton, 5);
		listText.setEnabled(false);
		listText.setLayoutData(data);

		sweListlabelLabel = getWidgetFactory().createCLabel(composite,
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
							AllowedValuesPropertySource.PROPERTY_SWELIST_VALUE,
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
		});

		intervalMinlabelLabel = getWidgetFactory().createCLabel(composite,
				intervalMinLabel); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 30);
		data.right = new FormAttachment(10, 0);
		intervalMinlabelLabel.setEnabled(false);
		data.top = new FormAttachment(sweValueList, 30);
		intervalMinlabelLabel.setLayoutData(data);

		intervalMinText = getWidgetFactory().createText(composite, "");
		intervalMinText.setToolTipText("Interval Minimum Decimal Value");
		data = new FormData();
		data.left = new FormAttachment(intervalMinlabelLabel, 5);
		data.right = new FormAttachment(30, -5);
		data.top = new FormAttachment(sweValueList, 30);
		intervalMinText.setLayoutData(data);
		intervalMinText.setEnabled(false);
		intervalMinText.addModifyListener(listener);

		intervalMaxlabelLabel = getWidgetFactory().createCLabel(composite,
				intervalMaxLabel); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 30);
		data.right = new FormAttachment(10, 0);
		intervalMaxlabelLabel.setEnabled(false);
		data.top = new FormAttachment(intervalMinText, 5);
		intervalMaxlabelLabel.setLayoutData(data);

		intervalMaxText = getWidgetFactory().createText(composite, "");
		intervalMaxText.setToolTipText("Interval Maximum Decimal Value");
		data = new FormData();
		data.left = new FormAttachment(intervalMaxlabelLabel, 5);
		data.right = new FormAttachment(30, -5);
		data.top = new FormAttachment(intervalMinText, 5);
		intervalMaxText.setLayoutData(data);
		intervalMaxText.setEnabled(false);
		intervalMaxText.addModifyListener(listener);

		intervalListlabelLabel = getWidgetFactory().createCLabel(composite,
				intervalListLabel); //$NON-NLS-1$
		intervalList = getWidgetFactory().createList(composite,
				SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);

		data = new FormData();
		data.left = new FormAttachment(0, 30);
		data.right = new FormAttachment(10, 0);
		data.top = new FormAttachment(intervalMaxlabelLabel, 5);
		intervalListlabelLabel.setEnabled(false);
		intervalListlabelLabel.setLayoutData(data);

		data = new FormData();
		intervalList.setBackground(new Color(null, 224, 255, 255));
		data.left = new FormAttachment(intervalListlabelLabel, 5);
		data.right = new FormAttachment(30, -5);
		intervalList.setEnabled(false);
		data.top = new FormAttachment(intervalMaxText, 5);
		intervalList.setLayoutData(data);

		addIntervalButton = getWidgetFactory().createButton(composite,
				"Add Interval", SWT.PUSH);
		data = new FormData();
		data.left = new FormAttachment(intervalMaxText,
				ITabbedPropertyConstants.HSPACE);
		data.right = new FormAttachment(40, -5);
		data.top = new FormAttachment(intervalMinText, 5);
		addIntervalButton.setLayoutData(data);
		addIntervalButton.setEnabled(false);
		addIntervalButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String minValue = intervalMinText.getText();
				String maxValue = intervalMaxText.getText();
				if (minValue != null && minValue != "" && maxValue != null
						&& maxValue != "") {

					intervalList.add(minValue + " " + maxValue);
					propertySource
							.setPropertyValue(
									AllowedValuesPropertySource.PROPERTY_INTERVALLIST_VALUE,
									minValue + " " + maxValue);
					intervalMinText.setText("");
					intervalMinText.setFocus();
					intervalMaxText.setText("");
					intervalMaxText.setFocus();

				}
			}
		});

		removeIntervalButton = getWidgetFactory().createButton(composite,
				"Remove Interval", SWT.PUSH);
		data = new FormData();
		data.left = new FormAttachment(intervalList, 5);
		data.right = new FormAttachment(40, -5);
		data.top = new FormAttachment(addIntervalButton, 5);
		removeIntervalButton.setLayoutData(data);
		removeIntervalButton.setEnabled(false);
		removeIntervalButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int index = intervalList.getSelectionIndex();
				String remfield = intervalList.getItem(index);
				if (remfield != null && remfield != "") {
					IPropertyDescriptor[] desc = propertySource
							.getIntervalPropertyDescriptors();
					for (int i = 0; i < desc.length; i++) {
						String item = (String) propertySource
								.getPropertyValue(desc[i].getId());

						if (item.equals(remfield)) {
							propertySource.removeAllowedValue(desc[i].getId(),
									index);
							intervalList.remove(index);
							break;
						}
					}
				}
			}
		});
	}

	public void refresh() {
		minText.removeModifyListener(listener);
		maxText.removeModifyListener(listener);
		if (AllowedValuesPropertySource.PROPERTY_CONSTRAINT_CHOICE != null) {
			String selected = (String) propertySource
					.getPropertyValue(AllowedValuesPropertySource.PROPERTY_CONSTRAINT_CHOICE);
			if (selected.equals("min")) {
				minRadioButton.setSelection(true);
				enableMinOnly();
			} else if (selected.equals("max")) {
				maxRadioButton.setSelection(true);
				enableMaxOnly();
			} else if (selected.equals("list")) {
				listRadioButton.setSelection(true);
				enableListOnly();
			}
		}
		sweValueList.removeAll();
		IPropertyDescriptor[] desc = propertySource
				.getListPropertyDescriptors();
		for (int i = 0; i < desc.length; i++) {
			String item = (String) propertySource.getPropertyValue(desc[i]
					.getId());
			if ((item != null) && !item.equals(""))
				sweValueList.add(item);
		}
		
		intervalList.removeAll();
		desc = propertySource
				.getIntervalPropertyDescriptors();
		for (int i = 0; i < desc.length; i++) {
			String item = (String) propertySource.getPropertyValue(desc[i]
					.getId());
			if ((item != null) && !item.equals(""))
				intervalList.add(item);
		}

		if (AllowedValuesPropertySource.PROPERTY_CONSTRAINT_MIN != null) {

			String idValue = (String) propertySource
					.getPropertyValue(AllowedValuesPropertySource.PROPERTY_CONSTRAINT_MIN);
			if (idValue != null)
				minText.setText(idValue);
			else
				minText.setText("");

		}
		minText.addModifyListener(listener);

		if (AllowedValuesPropertySource.PROPERTY_CONSTRAINT_MAX != null) {

			String idValue = (String) propertySource
					.getPropertyValue(AllowedValuesPropertySource.PROPERTY_CONSTRAINT_MAX);
			if (idValue != null)
				maxText.setText(idValue);
			else
				maxText.setText("");

		}
		maxText.addModifyListener(listener);

	}

	public void enableMinOnly() {
		minText.setEnabled(true);
		maxText.setText("");
		maxText.setEnabled(false);
		listText.setEnabled(false);
		sweListlabelLabel.setEnabled(false);
		sweValueList.removeAll();
		sweValueList.setEnabled(false);
		addListButton.setEnabled(false);
		removeListButton.setEnabled(false);
		intervalMinText.setEnabled(false);
		intervalMinlabelLabel.setEnabled(false);
		intervalMaxText.setEnabled(false);
		intervalMaxlabelLabel.setEnabled(false);
		intervalList.removeAll();
		intervalList.setEnabled(false);
		intervalListlabelLabel.setEnabled(false);
		addIntervalButton.setEnabled(false);
		removeIntervalButton.setEnabled(false);
	}

	public void enableMaxOnly() {
		maxText.setEnabled(true);
		minText.setText("");
		minText.setEnabled(false);
		listText.setEnabled(false);
		sweListlabelLabel.setEnabled(false);
		sweValueList.setEnabled(false);
		sweValueList.removeAll();
		addListButton.setEnabled(false);
		removeListButton.setEnabled(false);
		intervalMinText.setEnabled(false);
		intervalMinlabelLabel.setEnabled(false);
		intervalMaxText.setEnabled(false);
		intervalMaxlabelLabel.setEnabled(false);
		intervalList.setEnabled(false);
		intervalList.removeAll();
		intervalListlabelLabel.setEnabled(false);
		addIntervalButton.setEnabled(false);
		removeIntervalButton.setEnabled(false);
	}

	public void enableListOnly() {
		maxText.setText("");
		maxText.setEnabled(false);
		minText.setText("");
		minText.setEnabled(false);
		listText.setEnabled(true);
		sweListlabelLabel.setEnabled(true);
		sweValueList.setEnabled(true);
		addListButton.setEnabled(true);
		removeListButton.setEnabled(true);
		intervalMinText.setEnabled(true);
		intervalMinlabelLabel.setEnabled(true);
		intervalMaxText.setEnabled(true);
		intervalMaxlabelLabel.setEnabled(true);
		intervalList.setEnabled(true);
		intervalListlabelLabel.setEnabled(true);
		addIntervalButton.setEnabled(true);
		removeIntervalButton.setEnabled(true);
	}

}
