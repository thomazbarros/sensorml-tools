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
import org.vast.sensormleditor.properties.descriptors.SMLClassifierPropertySource;
import org.vast.sensormleditor.util.DOMHelperAddOn;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;

public class SMLClassifierSection extends AbstractPropertySection {

	private String headerLabel = "Classifier List";
	private CLabel headerlabelLabel;

	private Text newClassifierText;
	private String newClassifierLabel = "New Classifier:";
	private CLabel newClassifierlabelLabel;
	private Button addClassifierButton;
	private Button removeClassifierButton;

	private List smlClassifiersList;
	private String smlClassifiersLabel = "Classifiers:";
	private CLabel smlClassifierslabelLabel;
	private IPropertyDescriptor[] desc;
	private SMLTreeEditor smlEditor;
	private Element element;
	private DOMHelper dom;
	private TreeViewer treeViewer;
	private SMLClassifierPropertySource propertySource;
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
		propertySource = new SMLClassifierPropertySource(element, dom,
				treeViewer, smlEditor);

	}
	

	public void dispose() {
		headerlabelLabel.dispose();
		smlClassifiersList.dispose();
		smlClassifierslabelLabel.dispose();
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

		newClassifierText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(headerlabelLabel);
		newClassifierText.setLayoutData(data);

		newClassifierlabelLabel = getWidgetFactory().createCLabel(composite,
				newClassifierLabel); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(newClassifierText,
				ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(headerlabelLabel);
		newClassifierlabelLabel.setLayoutData(data);

		addClassifierButton = getWidgetFactory().createButton(composite,
				"Add Classifier", SWT.PUSH);
		data = new FormData();
		data.left = new FormAttachment(newClassifierText,
				ITabbedPropertyConstants.HSPACE);
		data.right = new FormAttachment(75);
		data.top = new FormAttachment(headerlabelLabel);
		addClassifierButton.setLayoutData(data);
		addClassifierButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String newclassifier = newClassifierText.getText();
				if (newclassifier != null && newclassifier != "") {
					smlClassifiersList.add(newclassifier);
					propertySource
							.setPropertyValue(
									SMLClassifierPropertySource.PROPERTY_SML_CLASSIFIER,
									newClassifierText.getText());
					newClassifierText.setText("");
					newClassifierText.setFocus();
				}
			}
		});
		smlClassifiersList = getWidgetFactory().createList(composite,
				SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		data = new FormData();
		smlClassifiersList.setBackground(new Color(null, 224, 255, 255));
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(newClassifierText,
				ITabbedPropertyConstants.VSPACE);

		smlClassifiersList.setLayoutData(data);

		smlClassifierslabelLabel = getWidgetFactory().createCLabel(composite,
				smlClassifiersLabel); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(smlClassifiersList,
				ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(newClassifierText,
				ITabbedPropertyConstants.VSPACE);
		smlClassifierslabelLabel.setLayoutData(data);

		removeClassifierButton = getWidgetFactory().createButton(composite,
				"Remove Classifier", SWT.PUSH);
		data = new FormData();
		data.left = new FormAttachment(smlClassifiersList,
				ITabbedPropertyConstants.HSPACE);
		data.right = new FormAttachment(75);
		data.top = new FormAttachment(newClassifierText,
				ITabbedPropertyConstants.VSPACE);
		removeClassifierButton.setLayoutData(data);
		removeClassifierButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int selectionIndex = smlClassifiersList.getSelectionIndex();
				desc = propertySource.getPropertyDescriptors();
				for (int i = 0; i < desc.length; i++) {
					String item = (String) propertySource
							.getPropertyValue(desc[i].getId());
					if (item.equals(smlClassifiersList.getItem(selectionIndex))) {
						propertySource.setPropertyValue(desc[i].getId(), "");
					}
				}
				smlClassifiersList.remove(selectionIndex);
			}
		});
	}

	public void refresh() {
	
		smlClassifiersList.removeAll();
		if (propertySource != null) {
			desc = propertySource.getPropertyDescriptors();
			for (int i = 0; i < desc.length; i++) {
				String item = (String) propertySource.getPropertyValue(desc[i]
						.getId());

				if ((item != null) && !item.equals(""))
					smlClassifiersList.add(item);
			}
			smlClassifiersList.select(0);
		}

		String newValue = (String) propertySource
				.getPropertyValue(SMLClassifierPropertySource.PROPERTY_SML_CLASSIFIER);
		if (newValue != null)
			newClassifierText.setText(newValue);
	}

}
