package org.vast.sensormleditor.properties.tabfilters;

import org.w3c.dom.Element;

public class ObservablePropertyTabFilter extends AbstractTabFilter {

	@Override
	public boolean select(Object toTest) {
		init();
		if (toTest instanceof Element){
			Element ele = (Element) toTest;
			if (( ele.getNodeName().equals("swe:field")) || 
					(ele.getNodeName().equals("sml:input")) ||
					(ele.getNodeName().equals("sml:parameter")) ||
					(ele.getNodeName().equals("swe:condition")) ||
					(ele.getNodeName().equals("swe:data")) ||
					(ele.getNodeName().equals("sml:output"))){
				
				stop = false;
				Element retNode = null;
				Element chosenOne = getChosenField(ele, retNode);
				if (chosenOne != null) {
					if (chosenOne.getNodeName().equals("swe:ObservableProperty"))
						return true;
				}
			}
		}
		return false;
	}
	
/*	public boolean hasNumericalData(Node node){
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++){
			if (children.item(i).getNodeName().equals("swe:ObservableProperty") ||
				children.item(i).getNodeName().equals("swe:Count"))
				return true;
			else
				if (hasNumericalData(children.item(i)))
					return true;	
		}
		return false;
	}*/

}
