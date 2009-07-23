package org.vast.sensormleditor.properties.tabfilters;

import org.w3c.dom.Element;

public class ParameterListTabFilter extends AbstractTabFilter {

	@Override
	public boolean select(Object toTest) {
		init();
		if (toTest instanceof Element){
			Element ele = (Element) toTest;
			if (ele.getNodeName().equals("sml:parameters")) {
				stop = false;
				Element retNode = null;
				Element chosenOne = getChosenField(ele, retNode);
				if (chosenOne != null) {
					if (dom.hasQName(chosenOne, "sml:ParameterList"))
						return true;
				}
			}
		}
		return false;
	}

	

/*	@Override
	public boolean select(Object toTest) {
		if (toTest instanceof Element){
			Element ele = (Element) toTest;
			if (ele.getNodeName().equals("sml:ParameterList"))
				return true;
			else if (ele.getNodeName().equals("sml:parameters"))
				return true;
			else if (ele.getNodeName().equals("sml:parameter") && dataRecordSelected(ele))
				return true;
	
		}
		return false;
	}
	
	public boolean dataRecordSelected(Node node){
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++){
			if (children.item(i).getNodeName().equals("swe:DataRecord")){
				if (children.item(i).hasAttributes()){
					NamedNodeMap attList = children.item(i).getAttributes();
					for (int attIndex = 0; attIndex < attList.getLength(); attIndex++) {
						if (attList.item(attIndex).getNodeName().equals("xng:selected")){
							String value = attList.item(attIndex).getNodeValue();
							if (value.equals("true"))
								return true;
						}
					}
				}
			}
				
			else
				if (dataRecordSelected(children.item(i)))
					return true;	
		}
		return false;
	}*/
}

