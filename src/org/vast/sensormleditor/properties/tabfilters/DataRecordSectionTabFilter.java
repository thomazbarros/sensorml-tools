package org.vast.sensormleditor.properties.tabfilters;

import org.w3c.dom.Element;

public class DataRecordSectionTabFilter extends AbstractTabFilter {

	@Override
	public boolean select(Object toTest) {
		init();
		if (toTest instanceof Element){
			Element ele = (Element) toTest;
			if (ele.getNodeName().equals("sml:characteristics") ||
					ele.getNodeName().equals("sml:capabilities") ||
					ele.getNodeName().equals("swe:elementType") ||
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
					if (dom.hasQName(chosenOne, "swe:DataRecord") ||
							(dom.hasQName(chosenOne,"swe:SimpleDataRecord")))
						return true;
				}
			} else if (ele.getNodeName().equals("swe:DataRecord"))
				return true;
		}
		return false;
	}
}

