package org.vast.sensormleditor.properties.tabfilters;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XlinkHrefTabFilter extends AbstractTabFilter {

	@Override
	public boolean select(Object toTest) {
		init();
		if (toTest instanceof Element) {
			Element ele = (Element) toTest;

			if (ele.getNodeName().equals("sml:method"))
				return true;
			else if (ele.getNodeName().equals("sml:keywords")
					|| ele.getNodeName().equals("sml:identification")
					|| ele.getNodeName().equals("sml:characteristics")
					|| ele.getNodeName().equals("sml:capabilities")
					|| ele.getNodeName().equals("swe:coordinate")
					|| ele.getNodeName().equals("swe:case")
					|| ele.getNodeName().equals("swe:condition")
					|| ele.getNodeName().equals("swe:data")
					|| ele.getNodeName().equals("sml:inputs")
					|| ele.getNodeName().equals("sml:input")
					|| ele.getNodeName().equals("sml:outputs")
					|| ele.getNodeName().equals("sml:output")
					|| ele.getNodeName().equals("sml:parameters")
					|| ele.getNodeName().equals("sml:parameter")
					|| ele.getNodeName().equals("sml:components")
					|| ele.getNodeName().equals("sml:component")
					|| ele.getNodeName().equals("swe:field")
					|| ele.getNodeName().equals("swe:constraint")
					|| ele.getNodeName().equals("sml:documentation")
					|| ele.getNodeName().equals("sml:validTime")
					|| ele.getNodeName().equals("sml:contact")
					|| ele.getNodeName().equals("sml:member")
					|| ele.getNodeName().equals("sml:classification")) {
				stop = false;
				Element retNode = null;
				Element chosenOne = getChosenField(ele, retNode);
				if (chosenOne != null) {
					if (dom.hasQName(chosenOne, "rng:attribute")
							&& dom.getAttributeValue(chosenOne, "name").equals(
									"xlink:href")) {
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
			} else if (dom.hasQName(oneEle, "rng:attribute") && dom.getAttributeValue(oneEle,"name").equals("xlink:href") &&
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
 

