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
import org.vast.sensormleditor.properties.descriptors.UOMPropertySource;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;

public class UOMSection extends AbstractPropertySection {

	private Text uomText;
	private String uomLabel = "UOM value:";
	private CLabel uomlabelLabel;
	
	private CCombo valueChoice;
	private String valueLabel = "UOM type:";
	private CLabel valuelabelLabel;
	private SMLTreeEditor smlEditor;
	private Element element;
	private DOMHelper dom;
	private TreeViewer treeViewer;
	private UOMPropertySource propertySource;
	private String defaultChoice="code";

	
	private ModifyListener listener = new ModifyListener() {

		public void modifyText(ModifyEvent arg0) {
			propertySource.setPropertyValue(
					UOMPropertySource.PROPERTY_UOM,
					uomText.getText());
		}
	};

	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		Assert.isTrue(selection instanceof IStructuredSelection);
		Object input = ((IStructuredSelection) selection).getFirstElement();
		Assert.isTrue(input instanceof Element);
		this.element = (Element) input;
		
		if (part instanceof SMLTreeEditor) {
			smlEditor = ((SMLTreeEditor)part);
			dom = smlEditor.getModel();
			treeViewer = (TreeViewer) ((SMLTreeEditor) part).getTreeViewer();
		} else {
			SMLTreeEditor smlEditor = (SMLTreeEditor) ((SensorMLContentOutlinePage) part).smlEditor;
			dom = smlEditor.getModel();
			treeViewer = smlEditor.getTreeViewer();
		}

		propertySource = new UOMPropertySource(element, dom, treeViewer,smlEditor);

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
							UOMPropertySource.PROPERTY_UOM_CHOICE,
							valueChoice.getItem(index));
				defaultChoice = valueChoice.getItem(index);
					
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
		
		uomText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(valuelabelLabel, ITabbedPropertyConstants.VSPACE);
		uomText.setLayoutData(data);
		uomText.addModifyListener(listener);

		uomlabelLabel = getWidgetFactory().createCLabel(composite, uomLabel); //$NON-NLS-1$

		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(uomText,
				ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(valueChoice, ITabbedPropertyConstants.VSPACE);
		uomlabelLabel.setLayoutData(data);
	}

	public void refresh() {
		
		valueChoice.removeAll();
		if (UOMPropertySource.PROPERTY_UOM_CHOICE != null) {
			String[] choices = (String[]) propertySource
					.getChoices(UOMPropertySource.PROPERTY_UOM_CHOICE);
			for (int i = 0; i < choices.length; i++) {
				if (choices[i]!= null)
					valueChoice.add(choices[i]);
				valueChoice.setText(defaultChoice);
				
			}
			String selected = (String)propertySource.getPropertyValue(UOMPropertySource.PROPERTY_UOM_CHOICE);
			int index = valueChoice.indexOf(selected);
			valueChoice.select(index);
		}
		
		uomText.removeModifyListener(listener);
		if (UOMPropertySource.PROPERTY_UOM != null) {
			String uomValue = (String) propertySource
					.getPropertyValue(UOMPropertySource.PROPERTY_UOM);

			if (uomValue != null)
				uomText.setText(uomValue);
		}
		uomText.addModifyListener(listener);

	}

}
