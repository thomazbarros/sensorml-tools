package org.vast.sensormleditor.properties.tabfilters;

import org.w3c.dom.Element;

public class ConditionalValueSectionTabFilter extends AbstractTabFilter {

	@Override
	public boolean select(Object toTest) {
		init();
		if (toTest instanceof Element){
			Element ele = (Element) toTest;
			if (ele.getNodeName().equals("swe:case") ||
				(ele.getNodeName().equals("swe:field"))){
				stop = false;
				Element retNode = null;
				Element chosenOne = getChosenField(ele, retNode);
				if (chosenOne != null) {
					if (dom.hasQName(chosenOne, "swe:ConditionalValue"))
						return true;
				}
			/*} else if (ele.getNodeName().equals("swe:DataRecord"))
				return true;*/
			}
		}
		return false;
	}
}

