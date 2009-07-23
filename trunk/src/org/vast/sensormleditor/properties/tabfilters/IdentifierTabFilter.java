package org.vast.sensormleditor.properties.tabfilters;

import org.w3c.dom.Element;

public class IdentifierTabFilter extends AbstractTabFilter {

	@Override
	public boolean select(Object toTest) {
		init();
		if (toTest instanceof Element){
			Element ele = (Element) toTest;
		
			if ( ele.getNodeName().equals("sml:identifier")) {
					return true;
			}
		}
	
		return false;
	}
	
/*	public boolean hasNumericalData(Node node){
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++){
			if (children.item(i).getNodeName().equals("swe:Quantity") ||
				children.item(i).getNodeName().equals("swe:Count"))
				return true;
			else
				if (hasNumericalData(children.item(i)))
					return true;	
		}
		return false;
	}*/

}
