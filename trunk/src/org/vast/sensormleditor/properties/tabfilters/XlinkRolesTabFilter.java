package org.vast.sensormleditor.properties.tabfilters;

import org.w3c.dom.Element;

public class XlinkRolesTabFilter extends AbstractTabFilter {

	public boolean select(Object toTest) {
		init();
		if (toTest instanceof Element) {
			Element ele = (Element) toTest;

			if (ele.getNodeName().equals("swe:field") ||
					ele.getNodeName().equals("sml:keywords") ||
					ele.getNodeName().equals("sml:characteristics") ||
					ele.getNodeName().equals("sml:capabilities") ||
					ele.getNodeName().equals("sml:member") ||
					ele.getNodeName().equals("sml:input") ||
					ele.getNodeName().equals("sml:inputs") ||
					ele.getNodeName().equals("sml:parameter") ||
					ele.getNodeName().equals("sml:parameters") ||
					ele.getNodeName().equals("sml:documentation") ||
					ele.getNodeName().equals("sml:contact") ||
					ele.getNodeName().equals("swe:case") ||
					ele.getNodeName().equals("swe:condition") ||
					ele.getNodeName().equals("swe:data") ||
					ele.getNodeName().equals("sml:history") ||
					ele.getNodeName().equals("sml:location") ||
					ele.getNodeName().equals("sml:position") ||
					ele.getNodeName().equals("sml:positions") ||
					ele.getNodeName().equals("sml:timePosition") ||
					ele.getNodeName().equals("sml:interfaces") ||
					ele.getNodeName().equals("sml:interface") ||
					ele.getNodeName().equals("sml:component") ||
					ele.getNodeName().equals("sml:components") ||
					ele.getNodeName().equals("sml:identification") ||
					ele.getNodeName().equals("sml:classification") ||
					ele.getNodeName().equals("sml:outputs") ||
					ele.getNodeName().equals("sml:output")){
				return true;
			}
		}
		return false;
	}

}
