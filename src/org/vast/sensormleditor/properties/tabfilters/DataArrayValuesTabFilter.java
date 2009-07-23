package org.vast.sensormleditor.properties.tabfilters;

import org.w3c.dom.Element;

public class DataArrayValuesTabFilter extends AbstractTabFilter {

	public boolean select(Object toTest) {
		init();
		if (toTest instanceof Element) {
			Element ele = (Element) toTest;

			if (ele.getNodeName().equals("swe:StandardFormat") ||
					ele.getNodeName().equals("swe:BinaryBlock") ||
					ele.getNodeName().equals("swe:TextBlock")){
				return true;
			}
		}
		return false;
	}
}