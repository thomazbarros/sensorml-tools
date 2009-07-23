package org.vast.sensormleditor.properties.tabfilters;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CodespaceTabFilter extends AbstractTabFilter {

	@Override
	public boolean select(Object toTest) {
		init();
		if (toTest instanceof Element) {
			Element ele = (Element) toTest;
			if ((ele.getNodeName().equals("sml:classifier")) ||
					(ele.getNodeName().equals("sml:keywords")) ||
					(ele.getNodeName().equals("sml:identifier"))){
						return true;
			} else if (ele.getNodeName().equals("swe:field")) {
				stop = false;
				Element retNode = null;
				Element chosenOne = getChosenField(ele, retNode);
				if (chosenOne != null) {
					if (chosenOne.getNodeName().equals("swe:Category"))
							return true;
				}
			}
		}
		return false;
	}

	

	public boolean hasCodeSpace(Node node) {
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			if (!children.item(i).getNodeName().equals("a:documentation")) {
				
				if (children.item(i).getNodeName().equals("swe:codeSpace"))
					return true;
				else if (children.item(i).getNodeName().equals("sml:codeSpace"))
					return true;
				else if (children.item(i).getNodeName().equals("rng:ref")) {
					if (children.item(i).hasAttributes()) {
						NamedNodeMap attList = children.item(i).getAttributes();
						for (int attIndex = 0; attIndex < attList.getLength(); attIndex++) {
							if (attList.item(attIndex).getNodeName().equals(
									"name")) {
								String value = attList.item(attIndex)
										.getNodeValue();
								if (value.equals("swe.codeSpace"))
									return true;
							}
						}
					}
				} else if (children.item(i).getNodeName().equals(
						"rng:attribute")) {
					if (children.item(i).hasAttributes()) {
						NamedNodeMap attList = children.item(i).getAttributes();
						for (int attIndex = 0; attIndex < attList.getLength(); attIndex++) {
							if (attList.item(attIndex).getNodeName().equals(
									"name")) {
								String value = attList.item(attIndex)
										.getNodeValue();
								if (value.equals("codeSpace"))
									return true;
							}
						}
					}
				}
				else if (children.item(i).getNodeName().equals("rng:optional"))
					if (hasCodeSpace(children.item(i))) return true;
				
				else if (hasCodeSpace(children.item(i))) return true;
				 
			}
		}
		return false;
	}

}
