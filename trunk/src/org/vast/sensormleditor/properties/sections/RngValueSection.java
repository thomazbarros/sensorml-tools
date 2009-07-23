package org.vast.sensormleditor.properties.sections;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
import org.vast.sensormleditor.properties.descriptors.RngValuePropertySource;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;

public class RngValueSection extends AbstractPropertySection {
	private Text valueText;
	private String valueLabel = "Value:";
	private CLabel valuelabelLabel;

	private Element element;
	private DOMHelper dom;
	private TreeViewer treeViewer;
	public SMLTreeEditor smlEditor;
	private RngValuePropertySource propertySource;

	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		Assert.isTrue(selection instanceof IStructuredSelection);
		Object input = ((IStructuredSelection) selection).getFirstElement();
		Assert.isTrue(input instanceof Element);
		this.element = (Element) input;
		if (part instanceof SMLTreeEditor) {
			smlEditor = (SMLTreeEditor) part;
			dom = ((SMLTreeEditor) part).getModel();
			treeViewer = (TreeViewer) ((SMLTreeEditor) part).getTreeViewer();
		} else {
			SMLTreeEditor smlEditor = (SMLTreeEditor) ((SensorMLContentOutlinePage) part).smlEditor;
			dom = smlEditor.getModel();
			treeViewer = smlEditor.getTreeViewer();
		}
		propertySource = new RngValuePropertySource(element, dom, treeViewer, smlEditor);

	}

	private ModifyListener listener = new ModifyListener() {
		public void modifyText(ModifyEvent arg0) {
			propertySource.setPropertyValue(
					RngValuePropertySource.PROPERTY_VALUE, valueText.getText());
		}
	};

	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {

		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite = getWidgetFactory()
				.createFlatFormComposite(parent);

		FormData data;

		valueText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
		valueText.setLayoutData(data);
		valueText.addModifyListener(listener);

		valuelabelLabel = getWidgetFactory()
				.createCLabel(composite, valueLabel); //$NON-NLS-1$

		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(valueText,
				ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
		valuelabelLabel.setLayoutData(data);

	}

	public void refresh() {

		if (RngValuePropertySource.PROPERTY_VALUE != null) {
			valueText.removeModifyListener(listener);
			String valueValue = (String) propertySource
					.getPropertyValue(RngValuePropertySource.PROPERTY_VALUE);
			if (valueValue != null)
				valueText.setText(valueValue);
			valueText.addModifyListener(listener);
		}

	}

}
