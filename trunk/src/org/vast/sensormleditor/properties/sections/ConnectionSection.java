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
import org.vast.sensormleditor.properties.descriptors.ConnectionPropertySource;
import org.vast.sensormleditor.util.DOMHelperAddOn;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;

public class ConnectionSection extends AbstractPropertySection {

	private CLabel sourcelabelLabel;
	private String sourceLabel = "Source:";
	private Text sourceText;

	private CLabel destlabelLabel;
	private String destLabel = "Destination:";
	private Text destText;

	private SMLTreeEditor smlEditor;
	private Element element;
	private DOMHelper dom;
	private TreeViewer treeViewer;
	private ConnectionPropertySource connectionPropertySource;

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
			smlEditor = (SMLTreeEditor) ((SensorMLContentOutlinePage) part).smlEditor;
			dom = smlEditor.getModel();
			treeViewer = smlEditor.getTreeViewer();
		}

		// stop = false;
		Element retNode = null;
		DOMHelperAddOn domUtil = new DOMHelperAddOn(dom);
		Element chosenOne = domUtil.getChosenField(element, retNode);
		if (chosenOne != null)
			element = chosenOne;
		if (element != null) {
			connectionPropertySource = new ConnectionPropertySource(element,
					dom, treeViewer, smlEditor);
		}
	}

	private ModifyListener listener = new ModifyListener() {
		public void modifyText(ModifyEvent arg0) {

			connectionPropertySource.setPropertyValue(
					ConnectionPropertySource.PROPERTY_LINK_SOURCE, sourceText
							.getText());
			connectionPropertySource.setPropertyValue(
					ConnectionPropertySource.PROPERTY_LINK_DESTINATION,
					destText.getText());

		}
	};

	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite = getWidgetFactory()
				.createFlatFormComposite(parent);

		FormData data;

		sourceText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0);
		sourceText.setLayoutData(data);
		sourceText.addModifyListener(listener);

		sourcelabelLabel = getWidgetFactory().createCLabel(composite,
				sourceLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(sourceText);
		data.top = new FormAttachment(0);
		sourcelabelLabel.setLayoutData(data);

		destText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(sourceText,
				ITabbedPropertyConstants.VSPACE);
		destText.setLayoutData(data);
		destText.addModifyListener(listener);

		destlabelLabel = getWidgetFactory().createCLabel(composite, destLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(destText);
		data.top = new FormAttachment(sourceText,
				ITabbedPropertyConstants.VSPACE);
		destlabelLabel.setLayoutData(data);

	}

	public void refresh() {

		sourceText.removeModifyListener(listener);
		if (ConnectionPropertySource.PROPERTY_LINK_SOURCE != null) {
			String valueValue = (String) connectionPropertySource
					.getPropertyValue(ConnectionPropertySource.PROPERTY_LINK_SOURCE);
			if (valueValue != null)
				sourceText.setText(valueValue);
		}
		sourceText.addModifyListener(listener);

		destText.removeModifyListener(listener);
		if (ConnectionPropertySource.PROPERTY_LINK_DESTINATION != null) {
			String valueValue = (String) connectionPropertySource
					.getPropertyValue(ConnectionPropertySource.PROPERTY_LINK_DESTINATION);
			if (valueValue != null)
				destText.setText(valueValue);
		}
		destText.addModifyListener(listener);

	}

}
