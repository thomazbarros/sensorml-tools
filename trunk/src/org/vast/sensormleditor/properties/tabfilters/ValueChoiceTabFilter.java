package org.vast.sensormleditor.properties.tabfilters;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ValueChoiceTabFilter extends AbstractTabFilter{

	@Override
	public boolean select(Object toTest) {
		init();
		if (toTest instanceof Element){
			Element ele = (Element) toTest;
			
			if ((ele.getNodeName().equals("sml:value")) || 
				(ele.getNodeName().equals("sml:identifer")) ||
				(ele.getNodeName().equals("sml:classifier"))){
				if (hasRngChoiceData(ele))
					return true;
			}
		}
	
		return false;
	}
	
	public boolean hasRngChoiceData(Node node){
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++){
			if (children.item(i).getNodeName().equals("rng:choice"))
				return true;
			else
				if (hasRngChoiceData(children.item(i)))
					return true;
		}
		return false;
	}

}
