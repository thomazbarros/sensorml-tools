package org.vast.sensormleditor.properties.tabfilters;

import org.w3c.dom.Element;

public class ComponentTabFilter extends AbstractTabFilter {

	@Override
	public boolean select(Object toTest) {
		init();
		if (toTest instanceof Element){
			//Element ele = (Element) toTest;
			/*if (ele.getNodeName().equals("sml:component"))
				return true;
			else if (ele.getNodeName().equals("sml:ComponentList"))
				return true;*/
		}
		return false;
	}

}
