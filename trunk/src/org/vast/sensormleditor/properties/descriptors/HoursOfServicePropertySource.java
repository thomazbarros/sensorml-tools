package org.vast.sensormleditor.properties.descriptors;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.properties.labelproviders.PropertyLabelProvider;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class HoursOfServicePropertySource extends AbstractPropertySource
		implements IPropertySource {
	public static final String PROPERTY_HOURSOFSERVICE = "SensorMLEditor.sml.hoursOfService";
	public TextPropertyDescriptor hoursDescriptor;


	public HoursOfServicePropertySource(Element element, DOMHelper domHelper,
			TreeViewer viewer, SMLTreeEditor smlEditor) {
		super(element, domHelper, viewer, smlEditor);
		initProperties();
	}
	
	public void initProperties() {
		recursiveInitProperties(ele);
	}

	public void recursiveInitProperties(Node node) {
		NodeList children = dom.getChildElements(node);
		for (int j = 0; j < children.getLength(); j++) {
			if (dom.hasQName(children.item(j), "sml:hoursOfService")) {
				hoursDescriptor = new TextPropertyDescriptor(PROPERTY_HOURSOFSERVICE,
							"name");
					
				PropertyLabelProvider plp = new PropertyLabelProvider(node, dom);
				hoursDescriptor.setLabelProvider(plp);
				hoursDescriptor.setCategory(node.getLocalName());
				/*
				 * String selected = dom.getAttributeValue((Element) node,
				 * "@xng:selected");
				 */
				/*if ((selected != null && selected
								.equalsIgnoreCase("TRUE")))
					plp.setOptionalSelected(true);
				else
					plp.setOptionalSelected(false);*/
			} else if (dom.hasQName(children.item(j), "a:documentation")) {
				continue;
			} else {
				recursiveInitProperties(children.item(j));
			}
		}
	}
	
/*	public void enableOptional(Object id, boolean add) {
		boolean enable = add;
		if ((id == PROPERTY_HOURSOFSERVICE )
				&& (hoursDescriptor != null)) {
			PropertyLabelProvider propProvider = (PropertyLabelProvider) hoursDescriptor
					.getLabelProvider();
			Node changeNode = propProvider.getNode();
			if (!enable) {
				domUtil.deSelectOptionalChild(changeNode);
				propProvider.setOptionalSelected(false);
			} else {
				domUtil.selectOptionalChild(changeNode);
				propProvider.setOptionalSelected(true);
			}
			hoursDescriptor = null;
		}
		initProperties();
		this.treeViewer.refresh();
	}
	
	public boolean isEnabled(Object id) {
		if ((id == PROPERTY_HOURSOFSERVICE )
				&& (hoursDescriptor != null)) {
			return ((PropertyLabelProvider) hoursDescriptor.getLabelProvider()).isEnabled();
		} 
		return true;
	}
	*/
	@Override
	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getPropertyValue(Object id) {
		String label = null;
		if ((id == PROPERTY_HOURSOFSERVICE )
				&& (hoursDescriptor != null)) {
			String displayName = (String) hoursDescriptor
					.getDisplayName();
			label = hoursDescriptor.getLabelProvider().getText(
					displayName);
			return label;
		}
		return "";
	}

	@Override
	public boolean isPropertySet(Object id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		String str = (String) value;
		if ((id == PROPERTY_HOURSOFSERVICE )
				&& (hoursDescriptor != null)) {
			PropertyLabelProvider propProvider = (PropertyLabelProvider) hoursDescriptor
					.getLabelProvider();
			/*
			 * if (!propProvider.isEnabled()) return;
			 */
			Node changeNode = propProvider.getNode();
			dom.setElementValue((Element)changeNode, str);
			this.firePropertyChange("INSERT", null,null);
		} 
		
	}

}
