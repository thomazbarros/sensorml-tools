package org.vast.sensormleditor.properties.tabfilters;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GMLNameTabFilter extends AbstractTabFilter {

	@Override
	public boolean select(Object toTest) {
		init();
		if (toTest instanceof Element) {
			Element ele = (Element) toTest;
			if ((ele.getNodeName().equals("swe:DataRecord"))||
					(ele.getNodeName().equals("sml:member")) ||
					(ele.getNodeName().equals("swe:field")) ||
					(ele.getNodeName().equals("sml:input")) ||
					(ele.getNodeName().equals("sml:output")) ||
					(ele.getNodeName().equals("sml:component")) ||
					(ele.getNodeName().equals("sml:parameter")) 
					|| (ele.getNodeName().equals("sml:InputList"))
					|| (ele.getNodeName().equals("sml:OutputList"))
					|| (ele.getNodeName().equals("sml:ParameterList"))
					|| (ele.getNodeName().equals("sml:KeywordList"))
					|| (ele.getNodeName().equals("sml:System"))
					|| (ele.getNodeName().equals("swe:case"))
					|| (ele.getNodeName().equals("swe:condition"))
					|| (ele.getNodeName().equals("swe:elementCount"))
					|| (ele.getNodeName().equals("swe:elementType"))
					|| (ele.getNodeName().equals("swe:data"))
					|| (ele.getNodeName().equals("sml:ComponentList"))
					|| (ele.getNodeName().equals("sml:ProcessModel"))
					|| (ele.getNodeName().equals("sml:IdentifierList"))
					|| (ele.getNodeName().equals("sml:ClassifierList"))
					|| (ele.getNodeName().equals("sml:ProcessChain"))
					|| (ele.getNodeName().equals("gml:TimeInstant"))
					|| (ele.getNodeName().equals("gml:TimePeriod"))
					|| (ele.getNodeName().equals("swe:quality"))
					|| (ele.getNodeName().equals("swe:coordinate"))
					|| (ele.getNodeName().equals("sml:DocumentList"))
					|| (ele.getNodeName().equals("sml:Document"))
					|| (ele.getNodeName().equals("sml:contact"))) {
				if (hasGMLName(ele))
					return true;
			}
		}
		return false;
	}

	public boolean hasGMLName(Node node) {
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			if (children.item(i).getNodeName().equals("gml:name"))
				return true;
			else if (children.item(i).getNodeName().equals("rng:ref")) {
				if (children.item(i).hasAttributes()) {
					NamedNodeMap attList = children.item(i).getAttributes();
					for (int attIndex = 0; attIndex < attList.getLength(); attIndex++) {
						if (attList.item(attIndex).getNodeName().equals("name")) {
							String value = attList.item(attIndex)
									.getNodeValue();
							if (value.equals("gml.name"))
								return true;
						}
					}
				}
				
			} else if (hasGMLName(children.item(i)))
				return true;
		}
		return false;
	}

}
