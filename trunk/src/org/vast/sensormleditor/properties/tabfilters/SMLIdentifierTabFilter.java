package org.vast.sensormleditor.properties.tabfilters;

import org.w3c.dom.Element;

public class SMLIdentifierTabFilter extends AbstractTabFilter {

	@Override
	public boolean select(Object toTest) {
		init();
		if (toTest instanceof Element){
			Element ele = (Element) toTest;
			if (ele.getNodeName().equals("sml:identification")){
				stop = false;
				Element retNode = null;
				Element chosenOne = getChosenField(ele, retNode);
				if (chosenOne != null) {
					if (dom.hasQName(chosenOne, "sml:IdentifierList"))
						return true;
				}
			}
		}
		return false;
	}

}
