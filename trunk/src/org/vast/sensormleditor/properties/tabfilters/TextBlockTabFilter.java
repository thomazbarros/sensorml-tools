package org.vast.sensormleditor.properties.tabfilters;

import org.w3c.dom.Element;

public class TextBlockTabFilter extends AbstractTabFilter {

	@Override
	public boolean select(Object toTest) {
		init();
		if (toTest instanceof Element){
			Element ele = (Element) toTest;
			if ( ele.getNodeName().equals("swe:TextBlock")){
				return true;
				
				}
			}
		return false;
	}


}
