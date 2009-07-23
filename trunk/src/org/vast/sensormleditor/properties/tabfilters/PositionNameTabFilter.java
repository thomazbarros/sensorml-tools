package org.vast.sensormleditor.properties.tabfilters;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PositionNameTabFilter extends AbstractTabFilter {

	@Override
	public boolean select(Object toTest) {
		init();
		if (toTest instanceof Element){
			Element ele = (Element) toTest;
			if (ele.getNodeName().equals("sml:contact")){
				if (hasPositionName(ele))
					return true;
			}
		}
		return false;
	}
	
	public boolean hasPositionName(Node node){
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++){
			
			if (children.item(i).getNodeName().equals("sml:positionName")) {
				return true;
			}
			else {
				if (hasPositionName(children.item(i)))
					return true;	
			}
		}
		return false;
	}

}
