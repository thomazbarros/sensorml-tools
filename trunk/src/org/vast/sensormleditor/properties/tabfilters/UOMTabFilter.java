package org.vast.sensormleditor.properties.tabfilters;

import org.w3c.dom.Element;

public class UOMTabFilter extends AbstractTabFilter {

	
	@Override
	public boolean select(Object toTest) {
		init();
		if (toTest instanceof Element){
			Element ele = (Element) toTest;
			if ( ele.getNodeName().equals("swe:field") ||
					ele.getNodeName().equals("sml:input") ||
					ele.getNodeName().equals("sml:output") ||
					ele.getNodeName().equals("sml:component") ||
					ele.getNodeName().equals("swe:coordinate") ||
					ele.getNodeName().equals("swe:condition") ||
					ele.getNodeName().equals("swe:elementType") ||
					ele.getNodeName().equals("swe:data") ||
					ele.getNodeName().equals("swe:quality") ||
					ele.getNodeName().equals("sml:parameter")){
				
				stop = false;
				Element retNode = null;
				Element chosenOne = getChosenField(ele, retNode);
				if (chosenOne != null) {
					if (chosenOne.getNodeName().equals("swe:Time") ||
							chosenOne.getNodeName().equals("swe:Quantity") ||
							chosenOne.getNodeName().equals("swe:QuantityRange") ||
						chosenOne.getNodeName().equals("swe:TimeRange"))
							return true;
				}
			}
		}
		return false;
	}
}
