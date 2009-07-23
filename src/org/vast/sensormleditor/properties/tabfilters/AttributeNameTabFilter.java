package org.vast.sensormleditor.properties.tabfilters;

import org.w3c.dom.Element;

public class AttributeNameTabFilter extends AbstractTabFilter {

	@Override
	public boolean select(Object toTest) {
		init();
		if (toTest instanceof Element) {
			Element ele = (Element) toTest;

			if (ele.getNodeName().equals("swe:field")
					|| ele.getNodeName().equals("sml:capabilities")
					|| ele.getNodeName().equals("sml:characteristics")
					|| ele.getNodeName().equals("sml:input")
					|| ele.getNodeName().equals("swe:coordinate")
					|| ele.getNodeName().equals("swe:case")
					|| ele.getNodeName().equals("sml:parameter")
					|| ele.getNodeName().equals("sml:output")) {
				stop = false;
				Element retNode = null;
				Element chosenOne = getChosenField(ele, retNode);
				if (chosenOne != null) {
					if (dom.hasQName(chosenOne, "swe:DataRecord")
						|| (dom.hasQName(chosenOne, "sml:Component")
						|| (dom.hasQName(chosenOne, "swe:ConditionalData")
						|| (dom.hasQName(chosenOne, "swe:ConditionalValue")
						|| (dom.hasQName(chosenOne, "swe:Vector"))))))
						return true;
				}
			} else if (ele.getNodeName().equals("sml:component")
					|| ele.getNodeName().equals("sml:connection")
					|| ele.getNodeName().equals("swe:elementType")) {
				return true;

			}
		}

		return false;
	}

	/*
	 * public boolean hasNumericalData(Node node){ NodeList children =
	 * node.getChildNodes(); for (int i = 0; i < children.getLength(); i++){ if
	 * (children.item(i).getNodeName().equals("swe:Quantity") ||
	 * children.item(i).getNodeName().equals("swe:Count")) return true; else if
	 * (hasNumericalData(children.item(i))) return true; } return false; }
	 */

}
