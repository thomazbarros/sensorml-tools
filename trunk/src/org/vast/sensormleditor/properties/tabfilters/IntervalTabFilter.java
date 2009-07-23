package org.vast.sensormleditor.properties.tabfilters;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class IntervalTabFilter extends AbstractTabFilter {

	@Override
	public boolean select(Object toTest) {
		init();
		if (toTest instanceof Element){
			Element ele = (Element) toTest;
			if ( ele.getNodeName().equals("swe:field") ||
					ele.getNodeName().equals("sml:input") ||
					ele.getNodeName().equals("sml:output") ||
					ele.getNodeName().equals("sml:component") ||
					ele.getNodeName().equals("sml:parameter")){
				
				stop = false;
				Element retNode = null;
				Element chosenOne = getChosenField(ele, retNode);
				if (chosenOne != null) {
					if (chosenOne.getNodeName().equals("swe:Time") ||
						chosenOne.getNodeName().equals("swe:Quantity") ||
						chosenOne.getNodeName().equals("swe:QuantityRange") ||
						chosenOne.getNodeName().equals("swe:Category") ||
						chosenOne.getNodeName().equals("swe:Count") ||
						chosenOne.getNodeName().equals("swe:CountRange") ||
						chosenOne.getNodeName().equals("swe:TimeRange"))
							return true;
				}
			}
		}
		return false;
	}

	public boolean hasMaxData(Node node){
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++){
			if (children.item(i).getNodeName().equals("rng:param") ){
				if (children.item(i).hasAttributes()){
					NamedNodeMap attList = children.item(i).getAttributes();
					for (int attIndex = 0; attIndex < attList.getLength(); attIndex++) {
						if (attList.item(attIndex).getNodeName().equals("name")){
							String value = attList.item(attIndex).getNodeValue();
							if (value.equals("maxExclusive"))
								return true;
						}
					}
				}
			 
			}
			else
				if (hasMaxData(children.item(i)))
					return true;	
		}
		return false;
	}


}
