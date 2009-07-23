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

public class PositionNamePropertySource extends AbstractPropertySource
		implements IPropertySource {
	public static final String PROPERTY_POSITION_NAME = "SensorMLEditor.sml.positionName";
	public TextPropertyDescriptor nameDescriptor;

	public PositionNamePropertySource(Element element, DOMHelper domHelper,
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
			if (dom.hasQName(children.item(j), "sml:positionName")) {
				nameDescriptor = new TextPropertyDescriptor(PROPERTY_POSITION_NAME,
							"name");
					
				PropertyLabelProvider plp = new PropertyLabelProvider(node, dom);
				nameDescriptor.setLabelProvider(plp);
				nameDescriptor.setCategory(node.getLocalName());
	
			} else if (dom.hasQName(children.item(j), "a:documentation")) {
				continue;
			} else {
				recursiveInitProperties(children.item(j));
			}
		}
	}
	

	
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
		if ((id == PROPERTY_POSITION_NAME )
				&& (nameDescriptor != null)) {
			String displayName = (String) nameDescriptor
					.getDisplayName();
			label = nameDescriptor.getLabelProvider().getText(
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

/*	@Override
	public void setPropertyValue(Object id, Object value) {
		String str = (String) value;
		if ((id == PROPERTY_POSITION_NAME )
				&& (nameDescriptor != null)) {
			PropertyLabelProvider propProvider = (PropertyLabelProvider) nameDescriptor
					.getLabelProvider();
			Node changeNode = propProvider.getNode();
			domUtil.setElementValue(changeNode, str);
		} 
		this.firePropertyChange("INSERT", null,null);
	}*/
	
	@Override
	public void setPropertyValue(Object id, Object value) {
		String str = (String) value;
		if ((id == PROPERTY_POSITION_NAME) && (nameDescriptor != null)) {
			PropertyLabelProvider propProvider = (PropertyLabelProvider) nameDescriptor
					.getLabelProvider();
			Node changeNode = propProvider.getNode();
			Element gmlName = dom.getElement((Element) changeNode,
					"sml:positionName");
			if (str != null && !str.equals("")) {
				if (!dom.existAttribute(gmlName, "rng:value/@selected")) {
					dom.setAttributeValue((Element) gmlName,
							"rng:data/rng:value/@selected", "true");
				}
				dom.setElementValue((Element) gmlName, "rng:data/rng:value",
						value.toString());
				((Element) changeNode).setAttribute("xng:selected", "true");

			} else {
				Element valueNode = dom.getElement((Element) changeNode,
						"sml:positionName/rng:data/rng:value");
				((Element) changeNode).removeAttribute("xng:selected");
				valueNode.setTextContent("");
				valueNode.removeAttribute("selected");
			}
			this.firePropertyChange("INSERT", null, null);
		}
	
	}

}
