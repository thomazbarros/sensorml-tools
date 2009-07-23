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
import org.vast.sensormleditor.properties.descriptors.TermDefinitionPropertySource;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;

public class TermDefinitionSection extends AbstractPropertySection {

	private Text definitionText;
	private String definitionLabel = "Term Definition:";
	private CLabel definitionlabelLabel;
	private SMLTreeEditor smlEditor;
	private Element element;
	private DOMHelper dom;
	private TreeViewer treeViewer;

	private TermDefinitionPropertySource propertySource;

	private ModifyListener listener = new ModifyListener() {

		public void modifyText(ModifyEvent arg0) {
			propertySource.setPropertyValue(
					TermDefinitionPropertySource.PROPERTY_DEFINITION,
					definitionText.getText());
		}
	};

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

		propertySource = new TermDefinitionPropertySource(element, dom, treeViewer, smlEditor);

	}

	public void dispose() {
		definitionText.dispose();
		definitionlabelLabel.dispose();

	}

	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {

		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite = getWidgetFactory()
				.createFlatFormComposite(parent);

		FormData data;

		definitionText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
		definitionText.setLayoutData(data);
		definitionText.addModifyListener(listener);

		definitionlabelLabel = getWidgetFactory().createCLabel(composite,
				definitionLabel); //$NON-NLS-1$

		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(definitionText,
				ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
		definitionlabelLabel.setLayoutData(data);
	}

	public void refresh() {

		definitionText.removeModifyListener(listener);
		if (TermDefinitionPropertySource.PROPERTY_DEFINITION != null) {
			String definitionValue = (String) propertySource
					.getPropertyValue(TermDefinitionPropertySource.PROPERTY_DEFINITION);
			definitionText.setText(definitionValue);
		}
		definitionText.addModifyListener(listener);
	}
}
