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

public class RngValuePropertySource extends AbstractPropertySource implements
		IPropertySource {

	public static final String PROPERTY_VALUE = "SensorMLEditor.rng.value";
	public TextPropertyDescriptor valueDescriptor;

	public RngValuePropertySource(Element element, DOMHelper domHelper,
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
			if (!children.item(j).getNamespaceURI().equals(rngaURI)) {
				if (dom.hasQName(children.item(j), "sml:value")) {
					handleValue(children.item(j));
				} else {
					recursiveInitProperties(children.item(j));
				}
			}
		}
	}

/*	public boolean isValue(Node node) {
		String refName = dom.getAttributeValue((Element) node, "name");
		if (refName.equals("swe.countValue")
				|| refName.equals("swe.categoryValue"))
			return true;
		return false;
	}*/

	public void handleValue(Node node) {
		Node n = node;
		valueDescriptor = new TextPropertyDescriptor(PROPERTY_VALUE, n
				.getNodeName());
		PropertyLabelProvider plp = new PropertyLabelProvider(n, dom);
		valueDescriptor.setLabelProvider(plp);
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
		if ((id == PROPERTY_VALUE) && (valueDescriptor != null)) {
			label = valueDescriptor.getLabelProvider().getText("");
			return label;
		}
		return "";
	}

	public void setPropertyValue(Object id, Object value) {
		String str = (String) value;
		if ((id == PROPERTY_VALUE) && (valueDescriptor != null)) {

			PropertyLabelProvider propProvider = (PropertyLabelProvider) valueDescriptor
					.getLabelProvider();
			Node changeNode = propProvider.getNode();
			if (str != null && !str.equals("")) {
				if (!dom.existAttribute((Element)changeNode, "rng:data/rng:value/@selected")) {
					dom.setAttributeValue((Element) changeNode,
							"rng:data/rng:value/@selected", "true");
				}
				dom.setElementValue((Element) changeNode, "rng:data/rng:value",
						value.toString());
				((Element) changeNode).setAttribute("xng:selected", "true");

			} else {

				Element valueNode = dom.getElement((Element) changeNode,
						"rng:data/rng:value");
				((Element) changeNode).removeAttribute("xng:selected");
				valueNode.setTextContent("");
				valueNode.removeAttribute("selected");

			}
			this.firePropertyChange("INSERT", null,null);
		}
		
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

}
