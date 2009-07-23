package org.vast.sensormleditor.properties.tabfilters;

import org.w3c.dom.Element;

public class InputTabFilter extends AbstractTabFilter {

	@Override
	public boolean select(Object toTest) {
		init();
		if (toTest instanceof Element){
			Element ele = (Element) toTest;
			if (ele.getNodeName().equals("sml:input"))
				return true;
			else if (ele.getNodeName().equals("sml:InputList"))
				return true;
		}
		return false;
	}

}
