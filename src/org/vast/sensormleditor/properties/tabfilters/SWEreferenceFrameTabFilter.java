package org.vast.sensormleditor.properties.tabfilters;

import org.w3c.dom.Element;

public class SWEreferenceFrameTabFilter extends AbstractTabFilter {

	public boolean select(Object toTest) {
		init();
		if (toTest instanceof Element) {
			Element ele = (Element) toTest;

			if (ele.getNodeName().equals("swe:field") ||
					ele.getNodeName().equals("sml:capabilities") ||
					ele.getNodeName().equals("sml:characteristics") ||
					ele.getNodeName().equals("swe:coordinate") ||
					ele.getNodeName().equals("swe:elementType") ||
					ele.getNodeName().equals("swe:case") ||
					ele.getNodeName().equals("sml:input") ||
					ele.getNodeName().equals("sml:parameter") ||
					ele.getNodeName().equals("swe:quality") ||
					ele.getNodeName().equals("sml:output")){
				stop = false;
				Element retNode = null;
				Element chosenOne = getChosenField(ele, retNode);
				if (chosenOne != null) {
					if (dom.hasQName(chosenOne, "swe:Quantity") ||
							dom.hasQName(chosenOne, "swe:Boolean") ||
							dom.hasQName(chosenOne, "swe:Count") ||
							dom.hasQName(chosenOne, "swe:QuantityRange") ||
							dom.hasQName(chosenOne, "swe:CountRange") ||
							dom.hasQName(chosenOne, "swe:Vector") ||
							dom.hasQName(chosenOne, "swe:Envelope") ||
							dom.hasQName(chosenOne, "swe:GeoLocationArea") ||
							dom.hasQName(chosenOne, "swe:Position") ||
							dom.hasQName(chosenOne, "swe:SquareMatrix") ||
							dom.hasQName(chosenOne, "swe:Category"))
						return true;
				}
			}
		}
		return false;
	}

}

/*if (ele.getNodeName().equals("swe:DataRecord")
		|| ele.getNodeName().equals("swe:Quantity")
		|| ele.getNodeName().equals("swe:Time")
		|| ele.getNodeName().equals("swe:Boolean")
		|| ele.getNodeName().equals("swe:Category")
		|| ele.getNodeName().equals("swe:Text")
		|| ele.getNodeName().equals("swe:QuantityRange")
		|| ele.getNodeName().equals("swe:CountRange")
		|| ele.getNodeName().equals("swe:TimeRange")
		|| ele.getNodeName().equals("swe:ConditionalData")
		|| ele.getNodeName().equals("swe:ConditionalValue")
		|| ele.getNodeName().equals("swe:Envelope")
		|| ele.getNodeName().equals("swe:GeolocationArea")
		|| ele.getNodeName().equals("swe:NormalizedCurve")
		|| ele.getNodeName().equals("swe:Position")
		|| ele.getNodeName().equals("swe:SimpleDataRecord")
		|| ele.getNodeName().equals("swe:Vector")
		|| ele.getNodeName().equals("swe:Curve")
		|| ele.getNodeName().equals("swe:DataArray")
		|| ele.getNodeName().equals("swe:SquareMatrix")
		|| ele.getNodeName().equals("swe:Count")) {
*/