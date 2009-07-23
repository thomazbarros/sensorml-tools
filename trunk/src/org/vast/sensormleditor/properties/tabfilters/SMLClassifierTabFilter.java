package org.vast.sensormleditor.properties.tabfilters;
import org.w3c.dom.Element;


public class SMLClassifierTabFilter extends AbstractTabFilter {

	@Override
	public boolean select(Object toTest) {
		init();
		if (toTest instanceof Element){
			Element ele = (Element) toTest;
			if (ele.getNodeName().equals("sml:classification")){
				stop = false;
				Element retNode = null;
				Element chosenOne = getChosenField(ele, retNode);
				if (chosenOne != null) {
					if (dom.hasQName(chosenOne, "sml:ClassifierList"))
						return true;
				}
			}
		}
		return false;
	}
}
