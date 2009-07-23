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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.outlineview.SensorMLContentOutlinePage;
import org.vast.sensormleditor.properties.descriptors.AllowedTimesPropertySource;
import org.vast.sensormleditor.properties.descriptors.AllowedValuesPropertySource;
import org.vast.sensormleditor.util.DOMHelperAddOn;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;

public class AllowedTimesSection extends AbstractPropertySection {

	private String headerLabel = "Allowed Times";
	private CLabel headerlabelLabel;
	private Button listRadioButton;
	private String listRadioLabel = "List Values";

	private Button minRadioButton;
	private String minRadioLabel = "Min Value";

	private Button maxRadioButton;
	private String maxRadioLabel = "Max Value";
	
	private Button intervalRadioButton;
	private String intervalRadioLabel = "Interval Value";

	private Group allowedValueGroup;
	private String allowedValueLabel = "Constraint Types";

	private Element element;
	private DOMHelper dom;
	private TreeViewer treeViewer;

	private Text minText;
	private CLabel minlabelLabel;
	private String minLabel = "Minimum Value:";
	
	private Text maxText;
	private CLabel maxlabelLabel;
	private String maxLabel = "Maximum Value:";

	private Text newIntervalText;
	private String newIntervalLabel = "New Interval Value:";
	private CLabel newIntervallabelLabel;
	private Button addIntervalButton;
	private Button removeIntervalButton;

	private List sweIntervalList;
	private String sweIntervalLabel = "Closed Interval List:";
	private CLabel sweIntervallabelLabel;
	
	private Text newListText;
	private String newListLabel = "New List Value:";
	private CLabel newListlabelLabel;
	private Button addListButton;
	private Button removeListButton;

	private List sweValueList;
	private String sweListLabel = "List Values:";
	private CLabel sweListlabelLabel;
	
	private SMLTreeEditor smlEditor;
	private AllowedTimesPropertySource propertySource;
	private Font fontBold = new Font(PlatformUI.getWorkbench().getDisplay(),
			"Tahoma", 8, SWT.BOLD);

	private ModifyListener listener = new ModifyListener() {

		public void modifyText(ModifyEvent arg0) {

			//propertySource.setPropertyValue(ConstraintsPropertySource.,
			// minText.getText());

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
			propertySource = new AllowedTimesPropertySource(element, dom,
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

		allowedValueGroup = getWidgetFactory().createGroup(composite,
				allowedValueLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(headerlabelLabel);

		GridLayout fillLayout = new GridLayout(2, true);
		fillLayout.horizontalSpacing=20;
		fillLayout.marginLeft=10;
		fillLayout.marginRight =10;
		fillLayout.marginTop = 10;
		fillLayout.marginBottom =5;
		fillLayout.verticalSpacing=20;
		allowedValueGroup.setLayout(fillLayout);
		allowedValueGroup.setLayoutData(data);

		
		minRadioButton = getWidgetFactory().createButton(allowedValueGroup,
				minRadioLabel, SWT.RADIO);
		minRadioButton.addListener(SWT.Selection,new Listener() {
			@Override
			public void handleEvent(Event event) {
				Button button = (Button)event.widget;
				if (!button.getSelection())return;
				minText.setVisible(true);
				minlabelLabel.setVisible(true);
				maxText.setVisible(false);
				maxlabelLabel.setVisible(false);
				newListText.setVisible(false);
				newListlabelLabel.setVisible(false);
				sweListlabelLabel.setVisible(false);
				sweValueList.setVisible(false);
				addListButton.setVisible(false);
				removeListButton.setVisible(false);
				newIntervalText.setVisible(false);
				newIntervallabelLabel.setVisible(false);
				sweIntervallabelLabel.setVisible(false);
				sweIntervalList.setVisible(false);
				removeIntervalButton.setVisible(false);
				addIntervalButton.setVisible(false);
			}
		});
		maxRadioButton = getWidgetFactory().createButton(allowedValueGroup,
				maxRadioLabel, SWT.RADIO);
		maxRadioButton.addListener(SWT.Selection,new Listener() {
			@Override
			public void handleEvent(Event event) {
				Button button = (Button)event.widget;
				if (!button.getSelection())return;
				maxText.setVisible(true);
				maxlabelLabel.setVisible(true);
				minText.setVisible(false);
				minlabelLabel.setVisible(false);
				newListText.setVisible(false);
				newListlabelLabel.setVisible(false);
				sweListlabelLabel.setVisible(false);
				sweValueList.setVisible(false);
				addListButton.setVisible(false);
				removeListButton.setVisible(false);
				newIntervalText.setVisible(false);
				newIntervallabelLabel.setVisible(false);
				sweIntervallabelLabel.setVisible(false);
				sweIntervalList.setVisible(false);
				removeIntervalButton.setVisible(false);
				addIntervalButton.setVisible(false);
			}
		});
		intervalRadioButton = getWidgetFactory().createButton(
				allowedValueGroup, intervalRadioLabel, SWT.RADIO);
		intervalRadioButton.addListener(SWT.Selection,new Listener() {
			@Override
			public void handleEvent(Event event) {
				Button button = (Button)event.widget;
				if (!button.getSelection())return;
				maxText.setVisible(false);
				maxlabelLabel.setVisible(false);
				minText.setVisible(false);
				minlabelLabel.setVisible(false);
				newListText.setVisible(false);
				newListlabelLabel.setVisible(false);
				sweListlabelLabel.setVisible(false);
				sweValueList.setVisible(false);
				addListButton.setVisible(false);
				removeListButton.setVisible(false);
				newIntervallabelLabel.setVisible(true);
				newIntervalText.setVisible(true);
				sweIntervallabelLabel.setVisible(true);
				sweIntervalList.setVisible(true);
				removeIntervalButton.setVisible(true);
				addIntervalButton.setVisible(true);
			}
		});
		
		
		listRadioButton = getWidgetFactory().createButton(allowedValueGroup,
				listRadioLabel, SWT.RADIO);
		listRadioButton.addListener(SWT.Selection,new Listener() {
			@Override
			public void handleEvent(Event event) {
				Button button = (Button)event.widget;
				if (!button.getSelection())return;
				maxText.setVisible(false);
				maxlabelLabel.setVisible(false);
				minText.setVisible(false);
				minlabelLabel.setVisible(false);
				newListText.setVisible(true);
				newListlabelLabel.setVisible(true);
				sweListlabelLabel.setVisible(true);
				sweValueList.setVisible(true);
				addListButton.setVisible(true);
				removeListButton.setVisible(true);
				newIntervalText.setVisible(false);
				newIntervallabelLabel.setVisible(false);
				sweIntervallabelLabel.setVisible(false);
				sweIntervalList.setVisible(false);
				removeIntervalButton.setVisible(false);
				addIntervalButton.setVisible(false);
			}
		});

		minText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(allowedValueGroup);
		minText.setLayoutData(data);
		minText.setVisible(false);
		minText.addModifyListener(listener);

		minlabelLabel = getWidgetFactory().createCLabel(composite, minLabel); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(minText,
				ITabbedPropertyConstants.HSPACE);
		minlabelLabel.setVisible(false);
		data.top = new FormAttachment(allowedValueGroup);
		minlabelLabel.setLayoutData(data);
		
		maxText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(allowedValueGroup);
		maxText.setLayoutData(data);
		maxText.setVisible(false);
		maxText.addModifyListener(listener);

		maxlabelLabel = getWidgetFactory().createCLabel(composite, maxLabel); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(maxText,
				ITabbedPropertyConstants.HSPACE);
		maxlabelLabel.setVisible(false);
		data.top = new FormAttachment(allowedValueGroup);
		maxlabelLabel.setLayoutData(data);
		
		newListText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(allowedValueGroup);
		newListText.setVisible(false);
		newListText.setLayoutData(data);

		newListlabelLabel = getWidgetFactory().createCLabel(composite,
				newListLabel); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(newListText,
				ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(allowedValueGroup);
		newListlabelLabel.setVisible(false);
		newListlabelLabel.setLayoutData(data);
		
		addListButton = getWidgetFactory().createButton(composite,
				"Add List Value", SWT.PUSH);
		data = new FormData();
		data.left = new FormAttachment(newListText,
				ITabbedPropertyConstants.HSPACE);
		data.right = new FormAttachment(75);
		data.top = new FormAttachment(newListText);
		addListButton.setLayoutData(data);
		addListButton.setVisible(false);
		addListButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String newidentifier = newListText.getText();
				if (newidentifier != null && newidentifier != "") {
					sweValueList.add(newidentifier);
					/*propertySource
							.setPropertyValue(
									ConstraintsPropertySource.,
									newListText.getText());*/
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
		removeListButton.setVisible(false);
		removeListButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int selectionIndex = sweValueList.getSelectionIndex();
				/*desc = propertySource.getPropertyDescriptors();
				for (int i = 0; i < desc.length; i++) {
					String item = (String) propertySource
							.getPropertyValue(desc[i].getId());
					if (item.equals(sweValueList.getItem(selectionIndex))) {
						propertySource.setPropertyValue(desc[i].getId(), "");
					}
				}*/
				sweValueList.remove(selectionIndex);
				}
		});
		
		sweValueList = getWidgetFactory().createList(composite,
				SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		data = new FormData();
		sweValueList.setBackground(new Color(null, 224, 255, 255));
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		sweValueList.setVisible(false);
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
		sweListlabelLabel.setVisible(false);
		sweListlabelLabel.setLayoutData(data);
		
		newIntervalText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(allowedValueGroup);
		newIntervalText.setVisible(false);
		newIntervalText.setLayoutData(data);

		newIntervallabelLabel = getWidgetFactory().createCLabel(composite,
				newIntervalLabel); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(newIntervalText,
				ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(allowedValueGroup);
		newIntervallabelLabel.setVisible(false);
		newIntervallabelLabel.setLayoutData(data);
		
		
		addIntervalButton = getWidgetFactory().createButton(composite,
				"Add List Value", SWT.PUSH);
		data = new FormData();
		data.left = new FormAttachment(newIntervalText,
				ITabbedPropertyConstants.HSPACE);
		data.right = new FormAttachment(75);
		data.top = new FormAttachment(newIntervalText);
		addIntervalButton.setLayoutData(data);
		addIntervalButton.setVisible(false);
		addIntervalButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String newidentifier = newIntervalText.getText();
				if (newidentifier != null && newidentifier != "") {
					sweIntervalList.add(newidentifier);
					/*propertySource
							.setPropertyValue(
									ConstraintsPropertySource.,
									newListText.getText());*/
					newIntervalText.setText("");
					newIntervalText.setFocus();
				}
			}
		});
		
		removeIntervalButton = getWidgetFactory().createButton(composite,
				"Remove Interval", SWT.PUSH);
		data = new FormData();
		data.left = new FormAttachment(newIntervalText,
				ITabbedPropertyConstants.HSPACE);
		data.right = new FormAttachment(75);
		data.top = new FormAttachment(addIntervalButton);
		removeIntervalButton.setLayoutData(data);
		removeIntervalButton.setVisible(false);
		removeIntervalButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int selectionIndex = sweIntervalList.getSelectionIndex();
				/*desc = propertySource.getPropertyDescriptors();
				for (int i = 0; i < desc.length; i++) {
					String item = (String) propertySource
							.getPropertyValue(desc[i].getId());
					if (item.equals(sweValueList.getItem(selectionIndex))) {
						propertySource.setPropertyValue(desc[i].getId(), "");
					}
				}*/
				sweIntervalList.remove(selectionIndex);
				}
		});
		
		sweIntervalList = getWidgetFactory().createList(composite,
				SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		data = new FormData();
		sweIntervalList.setBackground(new Color(null, 224, 255, 255));
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		sweIntervalList.setVisible(false);
		data.top = new FormAttachment(newIntervalText,
				ITabbedPropertyConstants.VSPACE);
		sweIntervalList.setLayoutData(data);

		sweIntervallabelLabel = getWidgetFactory().createCLabel(composite,
				sweIntervalLabel); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(sweIntervalList,
				ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(newIntervalText,
				ITabbedPropertyConstants.VSPACE);
		sweIntervallabelLabel.setVisible(false);
		sweIntervallabelLabel.setLayoutData(data);
		
	}

	public void refresh() {
		/*String constraintType = propertySource.getConstraintType();
		listRadioButton.setSelection(false);
		maxRadioButton.setSelection(false);
		minRadioButton.setSelection(false);
		intervalRadioButton.setSelection(false);
		if (constraintType.equals("List"))
			listRadioButton.setSelection(true);
		else if (constraintType.equals("Max")) {
			maxRadioButton.setSelection(true);
		} else if (constraintType.equals("Min")) {
			minRadioButton.setSelection(true);
		} else if (constraintType.equals("Interval")) 
			intervalRadioButton.setSelection(true);*/

	}

}
