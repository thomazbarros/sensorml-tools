package org.vast.sensormleditor.properties.tabfilters;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RngValueTabFilter extends AbstractTabFilter {

		public boolean select(Object toTest) {
			init();
			if (toTest instanceof Element){
				Element ele = (Element) toTest;
				if ((ele.getNodeName().equals("sml:value")) || 
					//(ele.getNodeName().equals("swe:field")) ||
					(ele.getNodeName().equals("sml:identifier")) ||
					(ele.getNodeName().equals("sml:classifier"))){
						//if (hasRngValueData(ele))
							return true;
					}
			}
		
			return false;
		}

		public boolean hasRngValueData(Node node){
			NodeList children = node.getChildNodes();
			for (int i = 0; i < children.getLength(); i++){
				if (children.item(i).getNodeName().equals("rng:data")){
					return true;
				}
				else
					if (hasRngValueData(children.item(i)))
						return true;	
			}
			return false;
		}
	} 


