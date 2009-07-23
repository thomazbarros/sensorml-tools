package org.vast.sensormleditor.properties.labelproviders;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PropertyLabelProvider implements ILabelProvider {
	DOMHelper dom;
	Node node;
	String xngURI = "http://xng.org/1.0";
	String rngURI = "http://relaxng.org/ns/structure/1.0";
	String rngaURI = "http://relaxng.org/ns/compatibility/annotations/1.0";
	String smlURI = "http://www.opengis.net/sensorML/1.0.1";
	String sweURI = "http://www.opengis.net/swe/1.0.1";
	String gmlURI = "http://www.opengis.net/gml";

	public PropertyLabelProvider(Node n, DOMHelper dom) {
		this.dom = dom;
		this.node = n;
	}

	public Node getNode() {
		return this.node;
	}
	
	public void setNode(Element ele){
		this.node = ele;
	}

	public String getText(Object element) {
		Node startNode = node;
		if (dom.hasQName(node, "rng:data"))
			startNode = node.getParentNode();
		String label = "";
		String str = locateValue((Element) startNode, "rng:value",label);
		if (str == null || str.equals("")) {
			if (node.getNodeValue() != null) {
				return node.getNodeValue();
			}
		} else {

			return str;
		}

		return "";
	}

	public Image getImage(Object element) {
		return null;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	public String locateValue(Element element, String tag, String label) {

		NodeList c = dom.getChildElements(element);

		for (int j = 0; j < c.getLength(); j++) {
			if (!(dom.hasQName(c.item(j), "a:documentation"))) {
			
				if (dom.hasQName(c.item(j), tag)) {
					label = c.item(j).getTextContent();
					return label;
				}
				label = locateValue((Element) c.item(j), tag, label);
				if (!label.equals(""))
					return label;
			}
		}
		return label;
	}	

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

}
