package org.vast.sensormleditor.properties.tabfilters;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ContactInstructionsTabFilter extends AbstractTabFilter {

	@Override
	public boolean select(Object toTest) {
		init();
	/*	if (toTest instanceof Element){
			Element ele = (Element) toTest;
			if (ele.getNodeName().equals("sml:contact")){
				if (hasContactInstructions(ele))
					return true;
			}
		}*/
		return false;
	}
	
	public boolean hasContactInstructions(Node node){
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++){
			
			if (children.item(i).getNodeName().equals("sml:contactInstructions")) {
				return true;
			}
			else {
				if (hasContactInstructions(children.item(i)))
					return true;	
			}
		}
		return false;
	}

}
