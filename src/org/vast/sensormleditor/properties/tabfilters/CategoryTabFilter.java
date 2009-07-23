package org.vast.sensormleditor.properties.tabfilters;

import org.w3c.dom.Element;

public class CategoryTabFilter extends AbstractTabFilter {

	@Override
	public boolean select(Object toTest) {
		init();
		if (toTest instanceof Element){
			Element ele = (Element) toTest;
			if (( ele.getNodeName().equals("swe:field")) || 
					(ele.getNodeName().equals("sml:input")) ||
					(ele.getNodeName().equals("swe:quality")) ||
					(ele.getNodeName().equals("swe:condition")) ||
					(ele.getNodeName().equals("swe:data")) ||
					(ele.getNodeName().equals("swe:elementType")) ||
					(ele.getNodeName().equals("swe:constraint")) ||
					(ele.getNodeName().equals("sml:parameter")) ||
					(ele.getNodeName().equals("sml:output"))){
				
				stop = false;
				Element retNode = null;
				Element chosenOne = getChosenField(ele, retNode);
				if (chosenOne != null) {
					if (chosenOne.getNodeName().equals("swe:Category"))
						return true;
				}
			}
		}
		return false;
	}
	
/*	public boolean hasCountValue(Node node){
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++){
			
			if (children.item(i).getNodeName().equals("swe:Category")) {
				return true;
			} else if (children.item(i).getNodeName().equals("rng:ref")) {
				if (children.item(i).hasAttributes()) {
					NamedNodeMap attList = children.item(i).getAttributes();
					for (int attIndex = 0; attIndex < attList.getLength(); attIndex++) {
						if (attList.item(attIndex).getNodeName().equals("name")) {
							String value = attList.item(attIndex)
									.getNodeValue();
							if (value.equals("swe.categoryValue"))
								return true;
						}
					}
				}
			} else {
				if (hasCountValue(children.item(i)))
					return true;	
			}
		}
		return false;
	}*/

}
