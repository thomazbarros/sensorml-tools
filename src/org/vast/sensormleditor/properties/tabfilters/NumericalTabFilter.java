package org.vast.sensormleditor.properties.tabfilters;

import org.w3c.dom.Element;

public class NumericalTabFilter extends AbstractTabFilter {

	@Override
	public boolean select(Object toTest) {
		init();
		if (toTest instanceof Element) {
			Element ele = (Element) toTest;
			if ((ele.getNodeName().equals("swe:field"))
					|| (ele.getNodeName().equals("sml:input"))
					|| (ele.getNodeName().equals("swe:coordinate"))
					|| (ele.getNodeName().equals("swe:condition"))
					|| (ele.getNodeName().equals("swe:data"))
					|| (ele.getNodeName().equals("swe:elementCount"))
					|| (ele.getNodeName().equals("sml:parameter"))
					|| (ele.getNodeName().equals("sml:output"))) {

				stop = false;
				Element retNode = null;
				Element chosenOne = getChosenField(ele, retNode);
				if (chosenOne != null) {
					if (chosenOne.getNodeName().equals("swe:Quantity")
							|| chosenOne.getNodeName().equals("swe:Count")
							|| chosenOne.getNodeName().equals("swe:Time"))
						return true;
				}
			}
		}
		return false;
	}

}
