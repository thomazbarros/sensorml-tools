package org.vast.sensormleditor.properties.sections;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.outlineview.SensorMLContentOutlinePage;
import org.vast.sensormleditor.properties.descriptors.IdentifierPropertySource;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;

public class IdentifierSecton extends AbstractPropertySection {

	private String headerLabel = "Identifier";
	private CLabel headerlabelLabel;

	private Text nameText;
	private String nameLabel = "Name:";
	private CLabel namelabelLabel;

/*	private Text valueText;
	private String valueLabel = "Identifier Value:";
	private CLabel valuelabelLabel;*/

	private Element element;
	private DOMHelper dom;
	private TreeViewer treeViewer;
	private SMLTreeEditor smlEditor;
	private IdentifierPropertySource propertySource;
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
		} else {
			SMLTreeEditor smlEditor = (SMLTreeEditor) ((SensorMLContentOutlinePage) part).smlEditor;
			dom = smlEditor.getModel();
			treeViewer = smlEditor.getTreeViewer();
		}

		propertySource = new IdentifierPropertySource(element, dom, treeViewer, smlEditor);
		headerlabelLabel.setText(headerLabel);

	}

	public void dispose() {
	/*	valueText.dispose();
		valuelabelLabel.dispose();*/
		nameText.dispose();
		namelabelLabel.dispose();
		fontBold.dispose();

	}

	private ModifyListener listener = new ModifyListener() {
		public void modifyText(ModifyEvent arg0) {
			/*propertySource.setPropertyValue(
					IdentifierPropertySource.PROPERTY_VALUE, valueText
							.getText());*/
			propertySource.setPropertyValue(
					IdentifierPropertySource.PROPERTY_NAME, nameText.getText());
		}
	};

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

		nameText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(headerlabelLabel);
		nameText.setLayoutData(data);
		nameText.addModifyListener(listener);

		namelabelLabel = getWidgetFactory().createCLabel(composite, nameLabel); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(nameText,
				ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(headerlabelLabel);
		namelabelLabel.setLayoutData(data);

/*		valueText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(nameText);
		valueText.setLayoutData(data);
		valueText.addModifyListener(listener);

		valuelabelLabel = getWidgetFactory()
				.createCLabel(composite, valueLabel); //$NON-NLS-1$

		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(valueText,
				ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(nameText);
		valuelabelLabel.setLayoutData(data);*/
	}

	public void refresh() {
		
		if (IdentifierPropertySource.PROPERTY_NAME != null) {
			nameText.removeModifyListener(listener);
			String valueValue = (String) propertySource
					.getPropertyValue(IdentifierPropertySource.PROPERTY_NAME);
			if (valueValue != null)
				nameText.setText(valueValue);
			nameText.addModifyListener(listener);
		}
/*		if (IdentifierPropertySource.PROPERTY_VALUE != null) {
			valueText.removeModifyListener(listener);
			String valueValue = (String) propertySource
					.getPropertyValue(IdentifierPropertySource.PROPERTY_VALUE);
			if (valueValue != null)
				valueText.setText(valueValue);
			valueText.addModifyListener(listener);
		}*/
	
	}

}
