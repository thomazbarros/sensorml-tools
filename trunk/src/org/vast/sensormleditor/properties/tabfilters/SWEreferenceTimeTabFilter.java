package org.vast.sensormleditor.properties.tabfilters;

import org.w3c.dom.Element;

public class SWEreferenceTimeTabFilter extends AbstractTabFilter {

	public boolean select(Object toTest) {
		init();
		if (toTest instanceof Element) {
			Element ele = (Element) toTest;

			if (ele.getNodeName().equals("swe:field") ||
					ele.getNodeName().equals("sml:capabilities") ||
					ele.getNodeName().equals("sml:characteristics") ||
					ele.getNodeName().equals("swe:coordinate") ||
					ele.getNodeName().equals("swe:elementType") ||
					ele.getNodeName().equals("swe:case") ||
					ele.getNodeName().equals("sml:input") ||
					ele.getNodeName().equals("sml:parameter") ||
					ele.getNodeName().equals("swe:quality") ||
					ele.getNodeName().equals("sml:output")){
				stop = false;
				Element retNode = null;
				Element chosenOne = getChosenField(ele, retNode);
				if (chosenOne != null) {
					if (dom.hasQName(chosenOne, "swe:Time") ||
							dom.hasQName(chosenOne, "swe:TimeRange"))
						return true;
				}
			}
		}
		return false;
	}
}
