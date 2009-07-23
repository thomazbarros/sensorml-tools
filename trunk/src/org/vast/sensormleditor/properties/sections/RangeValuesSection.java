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
import org.vast.sensormleditor.properties.descriptors.RangeValuesPropertySource;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;

public class RangeValuesSection extends AbstractPropertySection {

	private CLabel minlabelLabel;
	private CLabel maxlabelLabel;
	private Text minText;
	private Text maxText;
	private String minLabel = "Minimum Value:";
	private String maxLabel = "Maximum Value:";
	private Element element;
	private DOMHelper dom;
	private TreeViewer treeViewer;
	private RangeValuesPropertySource propertySource;
	private SMLTreeEditor smlEditor;
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
		propertySource = new RangeValuesPropertySource(element, dom,
				treeViewer, smlEditor);
	}

	public void dispose() {
		fontBold.dispose();
	}

	private ModifyListener listener = new ModifyListener() {

		public void modifyText(ModifyEvent arg0) {
			if (minText.getText()!= "") {
			propertySource.setPropertyValue(
					RangeValuesPropertySource.PROPERTY_MIN_VALUE, minText
							.getText());
			}
			if (maxText.getText()!=""){
			propertySource.setPropertyValue(
					RangeValuesPropertySource.PROPERTY_MAX_VALUE, maxText
							.getText());
			}

		}
	};

	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite = getWidgetFactory()
				.createFlatFormComposite(parent);

		FormData data;

		minText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
		minText.setLayoutData(data);
		
		minText.addModifyListener(listener);

		minlabelLabel = getWidgetFactory().createCLabel(composite,
				minLabel); // $NON
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(minText,
				-ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
		minlabelLabel.setLayoutData(data);
		
		maxText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(minText, ITabbedPropertyConstants.VSPACE);
		maxText.setLayoutData(data);
		maxText.addModifyListener(listener);

		maxlabelLabel = getWidgetFactory().createCLabel(composite,
				maxLabel); // $NON
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(maxText,
				-ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(minText, ITabbedPropertyConstants.VSPACE);
		maxlabelLabel.setLayoutData(data);

	}

	public void refresh() {

		minText.removeModifyListener(listener);
		if (RangeValuesPropertySource.PROPERTY_MIN_VALUE != null) {

			String idValue = (String) propertySource
					.getPropertyValue(RangeValuesPropertySource.PROPERTY_MIN_VALUE);
			if (idValue != null)
				minText.setText(idValue);
			else
				minText.setText("");

		}
		minText.addModifyListener(listener);
		
		maxText.removeModifyListener(listener);
		if (RangeValuesPropertySource.PROPERTY_MAX_VALUE != null) {

			String idValue = (String) propertySource
					.getPropertyValue(RangeValuesPropertySource.PROPERTY_MAX_VALUE);
			if (idValue != null)
				maxText.setText(idValue);
			else
				maxText.setText("");

		}
		maxText.addModifyListener(listener);
		
	}
}
