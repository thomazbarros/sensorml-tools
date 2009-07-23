package org.vast.sensormleditor.properties.tabfilters;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TermDefinitionTabFilter extends AbstractTabFilter {

	public boolean select(Object toTest) {
		init();
		if (toTest instanceof Element) {
			Element ele = (Element) toTest;
			if (ele.getNodeName().equals("sml:identifier") ||
				ele.getNodeName().equals("sml:classifier")) {
				//if (hasDefinitionAttribute(ele))
					return true;
			}
		}

		return false;
	}

	public boolean hasDefinitionAttribute(Node node) {
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			/*if ((children.item(i).getNodeName().equals("swe:Quantity")) || 
			   (children.item(i).getNodeName().equals("swe:Count")) ||
			   (children.item(i).getNodeName().equals("swe:Category")) ||
			   (children.item(i).getNodeName().equals("swe:ObservableProperty")) ||
			   (children.item(i).getNodeName().equals("sml:Term")) ||
			   (children.item(i).getNodeName().equals("swe:QuantityRange"))
			   ){*/
				if (children.item(i).hasAttributes()) {
					NamedNodeMap attList = children.item(i).getAttributes();
					for (int attIndex = 0; attIndex < attList.getLength(); attIndex++) {
						if (attList.item(attIndex).getNodeValue().equals("definition") ||
							attList.item(attIndex).getNodeName().equals("definition")){
								return true;
						}
					}
			//	}

			} else if (hasDefinitionAttribute(children.item(i)))
				return true;
		}
		return false;
	}

}
