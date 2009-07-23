package org.vast.sensormleditor.properties.tabfilters;

import org.w3c.dom.Element;

public class RangeTabFilter extends AbstractTabFilter {

	public boolean select(Object toTest) {
		init();
		if (toTest instanceof Element) {
			Element ele = (Element) toTest;
			if ((ele.getNodeName().equals("swe:field"))
					|| (ele.getNodeName().equals("sml:input"))
					|| (ele.getNodeName().equals("sml:parameter"))
					|| (ele.getNodeName().equals("sml:output"))) {

				stop = false;
				Element retNode = null;
				Element chosenOne = getChosenField(ele, retNode);
				if (chosenOne != null) {
					if (chosenOne.getNodeName().equals("swe:QuantityRange")
							|| chosenOne.getNodeName().equals("swe:TimeRange")
							|| chosenOne.getNodeName().equals("swe:CountRange"))
						return true;
				}
			}
		}
		return false;
	}
}
