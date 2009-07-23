package org.vast.sensormleditor.properties.tabfilters;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DataArrayElementCountTabFilter extends AbstractTabFilter {

	@Override
	public boolean select(Object toTest) {

		init();
		/*if (toTest instanceof Element){
			Element ele = (Element) toTest;
			if (ele.getNodeName().equals("sml:characteristics") ||
					ele.getNodeName().equals("sml:capabilities") ||
					ele.getNodeName().equals("sml:input") ||
					(ele.getNodeName().equals("swe:condition")) ||
					(ele.getNodeName().equals("swe:data")) ||
					ele.getNodeName().equals("sml:parameter") ||
					ele.getNodeName().equals("sml:output") ||
					ele.getNodeName().equals("swe:field")){
				stop = false;
				Element retNode = null;
				Element chosenOne = getChosenField(ele, retNode);
				if (chosenOne != null) {
					if (dom.hasQName(chosenOne, "swe:DataArray"))
						return true;
				}
			} else if (ele.getNodeName().equals("swe:DataArray"))
				return true;
		}*/
		return false;
	}
}
