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
import org.vast.sensormleditor.properties.descriptors.SMLCharacteristicsPropertySource;
import org.vast.sensormleditor.util.DOMHelperAddOn;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;

public class SMLCharacteristicsSection extends AbstractPropertySection {

	private CLabel descriptionlabelLabel;
	private Text codespaceText;
	private String codespaceLabel = "Characteristics:";
	private SMLTreeEditor smlEditor;
	private Element element;
	private DOMHelper dom;
	private TreeViewer treeViewer;
	private SMLCharacteristicsPropertySource propertySource;

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
		propertySource = new SMLCharacteristicsPropertySource(element, dom,
				treeViewer, smlEditor);
	}

	
	public void dispose() {
		descriptionlabelLabel.dispose();
		codespaceText.dispose();
	}

	private ModifyListener listener = new ModifyListener() {
		public void modifyText(ModifyEvent arg0) {
			propertySource
					.setPropertyValue(
							SMLCharacteristicsPropertySource.PROPERTY_SML_CHARACTERISTICS,
							codespaceText.getText());
		}
	};

	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite = getWidgetFactory()
				.createFlatFormComposite(parent);
		FormData data;

		codespaceText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
		codespaceText.setLayoutData(data);
		codespaceText.addModifyListener(listener);

		descriptionlabelLabel = getWidgetFactory().createCLabel(composite,
				codespaceLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(codespaceText,
				-ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
		descriptionlabelLabel.setLayoutData(data);
	}

	public void refresh() {

		if (SMLCharacteristicsPropertySource.PROPERTY_SML_CHARACTERISTICS != null) {

			String idValue = (String) propertySource
					.getPropertyValue(SMLCharacteristicsPropertySource.PROPERTY_SML_CHARACTERISTICS);
			if (idValue != null)
				codespaceText.setText(idValue);

			else
				codespaceText.setText("");
			codespaceText.addModifyListener(listener);
		}
	}

}
