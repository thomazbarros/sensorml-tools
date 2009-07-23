package org.vast.sensormleditor.properties.tabfilters;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class SMLDocumentationTabFilter extends AbstractTabFilter {

	@Override
	public boolean select(Object toTest) {
		init();
		/*if (toTest instanceof Element){
			Element ele = (Element) toTest;
			if (ele.getNodeName().equals("sml:System") ||
				ele.getNodeName().equals("sml:ProcessModel") ||
				ele.getNodeName().equals("sml:ProcessChain")) {
				if (hasSMLDocumentation(ele))
					return true;
			}
		}*/
		return false;
	}
	
	public boolean hasSMLDocumentation(Node node){
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++){
			if (children.item(i).getNodeName().equals("sml:documentation"))
				return true;
			else if	(children.item(i).getNodeName().equals("rng:ref")){
				if (children.item(i).hasAttributes()){
					NamedNodeMap attList = children.item(i).getAttributes();
					for (int attIndex = 0; attIndex < attList.getLength(); attIndex++) {
						if (attList.item(attIndex).getNodeName().equals("name")){
							String value = attList.item(attIndex).getNodeValue();
							if (value.equals("sml.documentation"))
								return true;
						}
					}
				}
			}
			else
				if (hasSMLDocumentation(children.item(i)))
					return true;	
		}
		return false;
	}

}
