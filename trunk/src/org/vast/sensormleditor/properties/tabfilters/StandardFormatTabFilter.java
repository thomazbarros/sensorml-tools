package org.vast.sensormleditor.properties.tabfilters;

import org.w3c.dom.Element;

public class StandardFormatTabFilter extends AbstractTabFilter {

	public boolean select(Object toTest) {
		init();
		if (toTest instanceof Element) {
			Element ele = (Element) toTest;

			if (ele.getNodeName().equals("swe:StandardFormat")){
				return true;
			}
		}
		return false;
	}
}