package org.vast.sensormleditor.properties.tabfilters;
import org.w3c.dom.Element;


public class SMLonlineResourceTabFilter extends AbstractTabFilter {

	@Override
	
	public boolean select(Object toTest) {
		init();
		if (toTest instanceof Element){
			Element ele = (Element) toTest;
			
		
			if ( ele.getNodeName().equals("sml:documentation") ||
					ele.getNodeName().equals("sml:member") ||
					ele.getNodeName().equals("sml:contact")) {		
				stop = false;
				Element retNode = null;
				Element chosenOne = getChosenField(ele, retNode);
				if (chosenOne != null) {
					if (chosenOne.getNodeName().equals("sml:Document") ||
							(chosenOne.getNodeName().equals("sml:ResponsibleParty"))){
						return true;
					}
				}
			}
			
		}
		return false;
	}
/*	public boolean select(Object toTest) {
		init();
		if (toTest instanceof Element){
			Element ele = (Element) toTest;
			if (ele.getNodeName().equals("sml:documentation") ||
					(ele.getNodeName().equals("sml:contact"))){
				if (hasSMLonlineResource(ele))
					return true;
			}
		}
		return false;
	}
	
	public boolean hasSMLonlineResource(Node node){
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++){
			if (children.item(i).getNodeName().equals("sml:onlineResource"))
				return true;
			else if	(children.item(i).getNodeName().equals("rng:ref")){
				if (children.item(i).hasAttributes()){
					NamedNodeMap attList = children.item(i).getAttributes();
					for (int attIndex = 0; attIndex < attList.getLength(); attIndex++) {
						if (attList.item(attIndex).getNodeName().equals("name")){
							String value = attList.item(attIndex).getNodeValue();
							if (value.equals("sml.onlineResource"))
								return true;
						}
					}
				}
			}
			else
				if (hasSMLonlineResource(children.item(i)))
					return true;	
		}
		return false;
	}*/

}
