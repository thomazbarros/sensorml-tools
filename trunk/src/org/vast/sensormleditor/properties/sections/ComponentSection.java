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
import org.vast.sensormleditor.properties.descriptors.ComponentPropertySource;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;

public class ComponentSection extends AbstractPropertySection {
	private Text nameText;
	private String nameLabel = "Component Name:";
	private CLabel namelabelLabel;
	private SMLTreeEditor smlEditor;
	private Element element;
	private DOMHelper dom;
	private TreeViewer treeViewer;
	
	private ComponentPropertySource propertySource;

	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		Assert.isTrue(selection instanceof IStructuredSelection);
		Object input = ((IStructuredSelection) selection).getFirstElement();
		Assert.isTrue(input instanceof Element);
		this.element = (Element) input;
		if (part instanceof SMLTreeEditor) {
			smlEditor = (SMLTreeEditor)part;
			dom = smlEditor.getModel();
			treeViewer = (TreeViewer) ((SMLTreeEditor) part).getTreeViewer();
		} else {
			SMLTreeEditor smlEditor = (SMLTreeEditor) ((SensorMLContentOutlinePage) part).smlEditor;
			dom = smlEditor.getModel();
			treeViewer = smlEditor.getTreeViewer();
		}
		propertySource = new ComponentPropertySource(element, dom, treeViewer,smlEditor);
	}
	
	public void dispose(){
		nameText.dispose();
		namelabelLabel.dispose();
	
	}
	
	private ModifyListener listener = new ModifyListener() {
		public void modifyText(ModifyEvent arg0) {
			propertySource.setPropertyValue(ComponentPropertySource.PROPERTY_COMPONENT_NAME, nameText.getText());
		}
	};
	
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite = getWidgetFactory()
				.createFlatFormComposite(parent);
	 
		FormData data;

		nameText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
		nameText.setLayoutData(data);
		nameText.addModifyListener(listener);
		
		namelabelLabel = getWidgetFactory().createCLabel(composite,
				nameLabel); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(nameText,
				ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
		namelabelLabel.setLayoutData(data);
	}
	
	public void refresh() {
		nameText.removeModifyListener(listener);
		if (ComponentPropertySource.PROPERTY_COMPONENT_NAME != null){
			String nameValue = (String) propertySource
					.getPropertyValue(ComponentPropertySource.PROPERTY_COMPONENT_NAME);
			if (nameValue != null)
					nameText.setText(nameValue);
			nameText.addModifyListener(listener);
		}
		nameText.addModifyListener(listener); 
	}
}
