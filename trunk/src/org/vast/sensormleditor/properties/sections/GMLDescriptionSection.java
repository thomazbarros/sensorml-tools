package org.vast.sensormleditor.properties.sections;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
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
import org.vast.sensormleditor.properties.descriptors.GMLDescriptionPropertySource;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;

public class GMLDescriptionSection extends AbstractPropertySection {

	private CLabel descriptionlabelLabel;
	private Text descriptionText;
	private String descriptionLabel = "gml.description:";
	private SMLTreeEditor smlEditor;
	private Element element;
	private DOMHelper dom;
	private TreeViewer treeViewer;
	private GMLDescriptionPropertySource propertySource;

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
		propertySource = new GMLDescriptionPropertySource(element, dom,
				treeViewer, smlEditor);
	}



	private ModifyListener listener = new ModifyListener() {

		public void modifyText(ModifyEvent arg0) {
			propertySource.setPropertyValue(
					GMLDescriptionPropertySource.PROPERTY_GML_DESCRIPTION,
					descriptionText.getText());
		}
	};

	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite = getWidgetFactory()
				.createFlatFormComposite(parent);

		FormData data;

		descriptionText = getWidgetFactory().createText(composite, "",
				SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		descriptionText.setBackground(new Color(null, 224, 255, 255));
		//descriptionText.addListener(SWT.Resize, listener)
		//Point size = descriptionText.computeSize(125, SWT.DEFAULT);
		//descriptionText.setSize(size.x / 2 , 200);
	
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
		data.height = 100;
		data.width = 300;
		descriptionText.setLayoutData(data);
		descriptionText.addModifyListener(listener);

		descriptionlabelLabel = getWidgetFactory().createCLabel(composite,
				descriptionLabel); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(descriptionText,
				ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
		descriptionlabelLabel.setLayoutData(data);
	}

	public void refresh() {

		if (GMLDescriptionPropertySource.PROPERTY_GML_DESCRIPTION != null) {
			descriptionText.removeModifyListener(listener);
			String idValue = (String) propertySource
					.getPropertyValue(GMLDescriptionPropertySource.PROPERTY_GML_DESCRIPTION);
			if (idValue != null)
				descriptionText.setText(idValue);
			else
				descriptionText.setText("");
			descriptionText.addModifyListener(listener);
		}
	}
}
