package org.vast.sensormleditor.properties.tabfilters;

import org.eclipse.jface.viewers.IFilter;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class AbstractTabFilter implements IFilter {
	
	DOMHelper dom = new DOMHelper();
	protected boolean stop = false;
	String xngURI = "http://xng.org/1.0";
	
	
	@Override
	public boolean select(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void init(){
		dom.addUserPrefix("xng", xngURI);
	}
	
	public Element getChosenField(Element element, Element returnEle) {

		NodeList children = dom.getChildElements(element);
		for (int i = 0; i < children.getLength(); i++) {
			Element oneEle = (Element) children.item(i);
			if (!dom.hasQName(oneEle, "rng:attribute")
					&& !dom.hasQName(oneEle, "a:documentation")
					&& !dom.hasQName(oneEle, "rng:ref")
					&& !dom.hasQName(oneEle, "rng:optional")) {

				if (dom.hasQName(oneEle, "rng:choice")
						|| dom.hasQName(oneEle, "rng:group")) {
					returnEle = getChosenField(oneEle, returnEle);
					if (stop)
						return returnEle;

				} else if (dom.existAttribute(oneEle, "@xng:selected")) {
					stop = true;
					return oneEle;
				}
			}
		}
		return returnEle;
	}

}
