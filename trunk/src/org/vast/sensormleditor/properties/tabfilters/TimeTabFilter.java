package org.vast.sensormleditor.properties.tabfilters;

import org.w3c.dom.Element;

public class TimeTabFilter extends AbstractTabFilter {

	@Override
	public boolean select(Object toTest) {
		init();
		if (toTest instanceof Element) {
			Element ele = (Element) toTest;
			if ((ele.getNodeName().equals("swe:field"))
					|| (ele.getNodeName().equals("sml:input"))
					|| (ele.getNodeName().equals("swe:coordinate"))
					|| (ele.getNodeName().equals("sml:parameter"))
					|| (ele.getNodeName().equals("swe:condition")) 
					|| (ele.getNodeName().equals("swe:elementType")) 
					|| (ele.getNodeName().equals("swe:data")) 
					|| (ele.getNodeName().equals("sml:output"))) {

				stop = false;
				Element retNode = null;
				Element chosenOne = getChosenField(ele, retNode);
				if (chosenOne != null) {
					if (chosenOne.getNodeName().equals("swe:Time"))
						return true;
				}
			}
		}
		return false;
	}
}
