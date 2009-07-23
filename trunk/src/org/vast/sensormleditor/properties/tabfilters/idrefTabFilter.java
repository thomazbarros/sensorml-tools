package org.vast.sensormleditor.properties.tabfilters;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class idrefTabFilter extends AbstractTabFilter {

	@Override
	public boolean select(Object toTest) {
		init();
		if (toTest instanceof Element) {
			Element ele = (Element) toTest;

			if (ele.getNodeName().equals("swe:elementCount")
					) {
				stop = false;
				Element retNode = null;
				Element chosenOne = getChosenField(ele, retNode);
				if (chosenOne != null) {
					if (dom.hasQName(chosenOne, "rng:attribute")
							&& dom.getAttributeValue(chosenOne, "name").equals(
									"ref")) {
						return true;
					}
				}
			}

		}
		return false;
	}


public Element getChosenField(Element element, Element returnEle) {
	
	NodeList children = dom.getChildElements(element); 
	for (int i = 0; i <children.getLength(); i++) { 
		Element oneEle = (Element) children.item(i); 
		if(!dom.hasQName(oneEle, "a:documentation") && !dom.hasQName(oneEle, "rng:ref")
			&& !dom.hasQName(oneEle, "rng:optional")) {
			if (dom.hasQName(oneEle, "rng:choice") || dom.hasQName(oneEle, "rng:group")){ 
				returnEle = getChosenField(oneEle, returnEle); if (stop) return returnEle;
			} else if (dom.hasQName(oneEle, "rng:attribute") && dom.getAttributeValue(oneEle,"name").equals("ref") &&
					dom.hasQName(oneEle,"selected")){ 
				stop = true;
				return oneEle;
			
			} else if (dom.existAttribute(oneEle, "@xng:selected")) { 
				stop = true; 
				return oneEle; 
			} 
		} 
		} 
	return returnEle; 
	}
}
 

