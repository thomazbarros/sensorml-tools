package org.vast.sensormleditor.properties.tabfilters;

import org.w3c.dom.Element;

public class SWEComponentTabFilter extends AbstractTabFilter {

	@Override
	public boolean select(Object toTest) {
		init();
		if (toTest instanceof Element){
			Element ele = (Element) toTest;
			if ( ele.getNodeName().equals("swe:member")){
				
				stop = false;
				Element retNode = null;
				Element chosenOne = getChosenField(ele, retNode);
				if (chosenOne != null) {
					if (chosenOne.getNodeName().equals("swe:Component"))
						return true;
				}
			}
		}
		return false;
	}


}
