package org.vast.sensormleditor.properties.descriptors;

import java.util.StringTokenizer;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.properties.labelproviders.ChoiceBoxLabelProvider;
import org.vast.sensormleditor.properties.labelproviders.PropertyLabelProvider;
import org.vast.sensormleditor.util.DOMHelperAddOn;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AllowedValuesPropertySource extends AbstractPropertySource
		implements IPropertySource {

	public static final String PROPERTY_SWELIST_VALUE = "SensorMLEditor.swelist.value";
	public static final String PROPERTY_CONSTRAINT_MIN = "SensorMLEditor.constraint.min";
	public static final String PROPERTY_CONSTRAINT_MAX = "SensorMLEditor.constraint.max";
	public static final String PROPERTY_CONSTRAINT_LIST = "SensorMLEditor.constraint.list";
	public static final String PROPERTY_CONSTRAINT_CHOICE = "SensorMLEditor.constraint.choice";
	public static final String PROPERTY_INTERVALLIST_VALUE = "SensorMLEditor.intervalList.value";
	public TextPropertyDescriptor minDescriptor;
	public TextPropertyDescriptor maxDescriptor;
	public TextPropertyDescriptor listDescriptor;
	public TextPropertyDescriptor intervalDescriptor;
	public SensorMLToolsComboPropertyDescriptor choiceDescriptor;
	public PropertyDescriptor tokenDescriptor;
	public PropertyDescriptor minintervalDescriptor;
	public PropertyDescriptor maxintervalDescriptor;
	protected static final String[] listproperties = new String[20];
	protected IPropertyDescriptor[] listpropertyDescriptors = new IPropertyDescriptor[20];
	protected int listpropertyCount;
	protected static final String[] intervalproperties = new String[20];
	protected IPropertyDescriptor[] intervalpropertyDescriptors = new IPropertyDescriptor[20];
	protected int intervalpropertyCount;
	private PropertyDescriptor descriptor;
	public String[] choiceLabels;
	protected int labelCount;
	public String constraintType;
	protected boolean stop = false;
	// protected Node currentSelectedNode = null;
	protected String min;
	protected String max;
	protected DOMHelperAddOn domUtil;

	public AllowedValuesPropertySource(Element element, DOMHelper domHelper,
			TreeViewer viewer, SMLTreeEditor smlEditor) {
		super(element, domHelper, viewer, smlEditor);
		domUtil = new DOMHelperAddOn(dom);
		labelCount = 0;
		choiceLabels = new String[20];
		initProperties();
	}

	public void initProperties() {
		Element retNode = null;
		Node allowedValues = null;
		
		
		if (ele.getLocalName().equals("AllowedValues"))
			allowedValues = ele;
		else
			allowedValues = domUtil.findNode(ele, "AllowedValues", retNode);

		choiceLabels[labelCount] = new String();
		choiceLabels[labelCount++] = "min";
		choiceLabels[labelCount] = new String();
		choiceLabels[labelCount++] = "max";
		choiceLabels[labelCount] = new String();
		choiceLabels[labelCount++] = "list";

		String[] retArray = new String[labelCount];
		System.arraycopy(choiceLabels, 0, retArray, 0, labelCount);

		choiceDescriptor = new SensorMLToolsComboPropertyDescriptor(
				PROPERTY_CONSTRAINT_CHOICE, "choice", retArray);
		ChoiceBoxLabelProvider prov = new ChoiceBoxLabelProvider(retArray,
				allowedValues, dom);
		choiceDescriptor.setLabelProvider(prov);

		if (allowedValues != null) {

			Node minNode = domUtil.findNode(allowedValues, "min", retNode);
			if (minNode != null) {
				minDescriptor = new TextPropertyDescriptor(
						PROPERTY_CONSTRAINT_MIN, "name");
				PropertyLabelProvider minPLP = new PropertyLabelProvider(
						minNode, dom);
				minDescriptor.setLabelProvider(minPLP);
				if (dom.existAttribute((Element) minNode, "xng:selected")) {
					ChoiceBoxLabelProvider cblp = (ChoiceBoxLabelProvider) choiceDescriptor
							.getLabelProvider();
					cblp.setSelectedNode(minNode);
				}
			}

			Node maxNode = domUtil.findNode(allowedValues, "max", retNode);
			if (maxNode != null) {
				maxDescriptor = new TextPropertyDescriptor(
						PROPERTY_CONSTRAINT_MAX, "name");
				PropertyLabelProvider maxPLP = new PropertyLabelProvider(
						maxNode, dom);
				maxDescriptor.setLabelProvider(maxPLP);
				if (dom.existAttribute((Element) maxNode, "xng:selected")) {
					ChoiceBoxLabelProvider cblp = (ChoiceBoxLabelProvider) choiceDescriptor
							.getLabelProvider();
					cblp.setSelectedNode(maxNode);
				}
			}

			Node oneOrMore = domUtil.findNode(allowedValues, "oneOrMore",
					retNode);
			if (oneOrMore != null) {
				listDescriptor = new TextPropertyDescriptor(
						PROPERTY_CONSTRAINT_LIST, "name");
				PropertyLabelProvider listPLP = new PropertyLabelProvider(
						oneOrMore, dom);
				listDescriptor.setLabelProvider(listPLP);

				// set up for adding another list
				tokenDescriptor = new TextPropertyDescriptor(
						PROPERTY_SWELIST_VALUE, "name");
				PropertyLabelProvider plp = new PropertyLabelProvider(
						oneOrMore, dom);
				tokenDescriptor.setLabelProvider(plp);

				minintervalDescriptor = new TextPropertyDescriptor(
						PROPERTY_INTERVALLIST_VALUE, "name");
				plp = new PropertyLabelProvider(oneOrMore, dom);
				minintervalDescriptor.setLabelProvider(plp);

				maxintervalDescriptor = new TextPropertyDescriptor(
						PROPERTY_INTERVALLIST_VALUE, "name");
				plp = new PropertyLabelProvider(oneOrMore, dom);
				maxintervalDescriptor.setLabelProvider(plp);

				if (dom.existElement((Element) oneOrMore, "xng:item")) {
					ChoiceBoxLabelProvider cblp = (ChoiceBoxLabelProvider) choiceDescriptor
							.getLabelProvider();
					cblp.setSelectedNode(oneOrMore);
					NodeList items = dom.getElements((Element) oneOrMore,
							"xng:item");
					for (int i = 0; i < items.getLength(); i++) {
						Node intervalNode = domUtil.findNode(items.item(i),
								"interval", retNode);
						if (intervalNode != null
								&& dom.existAttribute((Element) intervalNode,
										"xng:selected")) {
						
							NodeList intervalItems = dom.getElements(
									(Element) intervalNode,
									"rng:list/rng:data/");
							String intervalValue[] = {"",""};
							for (int k = 0; k < intervalItems.getLength(); k++) {
								Node intervalItem = intervalItems.item(k);
								intervalValue[k] = dom.getElementValue(
										(Element) intervalItem, "rng:value/");
							}
							intervalproperties[intervalpropertyCount] = "SensorML.IntervalProperty"
								+ intervalpropertyCount;
							descriptor = new TextPropertyDescriptor(
									intervalproperties[intervalpropertyCount],
									intervalValue[0] + " " + intervalValue[1]);
							plp = new PropertyLabelProvider(items.item(i), dom);
							descriptor.setLabelProvider(plp);
							intervalpropertyDescriptors[intervalpropertyCount++] = descriptor;
							continue;
						}

						Node listNode = domUtil.findNode(items.item(i),
								"valueList", retNode);
						String list = "";
						if (listNode != null
								&& dom.existAttribute((Element) listNode,
										"xng:selected")) {
							
							Element rngOne = dom.getElement((Element) listNode,
									"rng:list/rng:oneOrMore/");
							if (rngOne != null) {
								if (dom.existElement((Element) rngOne,
										"xng:item")) {
									NodeList listItems = dom.getElements(
											(Element) rngOne, "xng:item");
									for (int k = 0; k < listItems.getLength(); k++) {
										Node listItem = listItems.item(k);
										String value = dom.getElementValue(
												(Element) listItem,
												"rng:data/rng:value/");
										list = list + value + " ";
									}
								}
							}
							listproperties[listpropertyCount] = "SensorML.ListProperty"
								+ listpropertyCount;
							descriptor = new TextPropertyDescriptor(
									listproperties[listpropertyCount], list);
							plp = new PropertyLabelProvider(items.item(i), dom);
							descriptor.setLabelProvider(plp);
							listpropertyDescriptors[listpropertyCount++] = descriptor;
							continue;
						}
					}
				}
			}
		}
	}

	public void removeAllowedValue(Object id, Object value) {
		Integer newInt = new Integer((Integer) value);
		int index = newInt.intValue();
		if (listpropertyDescriptors[index] != null && listpropertyDescriptors[index].getId()==id) {
			PropertyLabelProvider propProvider = (PropertyLabelProvider) listpropertyDescriptors[index]
					.getLabelProvider();
			Node changeNode = propProvider.getNode();
			Node parentNode = changeNode.getParentNode();
			parentNode.removeChild(changeNode);
			//listpropertyDescriptors=null;
			//listpropertyCount=0;
			this.firePropertyChange("INSERT", null, null);
			
		} else if (intervalpropertyDescriptors[index]!=null && intervalpropertyDescriptors[index].getId()==id) {
			PropertyLabelProvider propProvider = (PropertyLabelProvider) intervalpropertyDescriptors[index]
			                                      .getLabelProvider();
			Node changeNode = propProvider.getNode();
			Node parentNode = changeNode.getParentNode();
			parentNode.removeChild(changeNode);
			//intervalpropertyDescriptors=null;
			//intervalpropertyCount=0;
			this.firePropertyChange("INSERT", null, null);
		}
	}

	public String getConstraintType() {
		return constraintType;
	}

	@Override
	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public IPropertyDescriptor[] getListPropertyDescriptors() {
		IPropertyDescriptor[] retArray;
		
		if (listpropertyDescriptors == null
				|| listpropertyDescriptors.length == 0 || listpropertyCount==0)
			initProperties();

		retArray = new IPropertyDescriptor[listpropertyCount];
		System.arraycopy(listpropertyDescriptors, 0, retArray, 0,
				listpropertyCount);
		return retArray;
	}

	public IPropertyDescriptor[] getIntervalPropertyDescriptors() {
		IPropertyDescriptor[] retArray;
		if (intervalpropertyDescriptors == null
				|| intervalpropertyDescriptors.length == 0 || intervalpropertyCount==0)
			initProperties();

		retArray = new IPropertyDescriptor[intervalpropertyCount];
		System.arraycopy(intervalpropertyDescriptors, 0, retArray, 0,
				intervalpropertyCount);
		return retArray;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] retArray;
		if (listpropertyDescriptors == null
				|| listpropertyDescriptors.length == 0)
			initProperties();

		retArray = new IPropertyDescriptor[listpropertyCount];
		System.arraycopy(listpropertyDescriptors, 0, retArray, 0,
				listpropertyCount);
		return retArray;
	}

	@Override
	public Object getPropertyValue(Object id) {

		String label = null;

		if (id == PROPERTY_SWELIST_VALUE || id == PROPERTY_INTERVALLIST_VALUE) {
			return "";

		} else if (id == PROPERTY_CONSTRAINT_CHOICE && choiceDescriptor != null) {
			ChoiceBoxLabelProvider prov = (ChoiceBoxLabelProvider) choiceDescriptor
					.getLabelProvider();
			Node sel = (Node) prov.getSelectedNode();

			if (sel != null) {
				if (sel.getNodeName().equals("rng:oneOrMore"))
					return "list";
				else
					return sel.getLocalName();
			}

		} else if ((id == PROPERTY_CONSTRAINT_MIN) && (minDescriptor != null)) {
			PropertyLabelProvider plp = (PropertyLabelProvider) minDescriptor
					.getLabelProvider();
			label = dom.getElementValue((Element) plp.getNode(),
					"rng:data/rng:value");
			return label;

		} else if ((id == PROPERTY_CONSTRAINT_MAX) && (maxDescriptor != null)) {
			PropertyLabelProvider plp = (PropertyLabelProvider) maxDescriptor
					.getLabelProvider();
			label = dom.getElementValue((Element) plp.getNode(),
					"rng:data/rng:value");
			return label;

		} else {
			for (int i = 0; i < listpropertyCount; i++) {
				if (id.equals(listpropertyDescriptors[i].getId())) {
					String displayName = (String) listpropertyDescriptors[i]
							.getDisplayName();
					return displayName;
				}
			}
			for (int i = 0; i < intervalpropertyCount; i++) {
				if (id.equals(intervalpropertyDescriptors[i].getId())) {
					String displayName = (String) intervalpropertyDescriptors[i]
							.getDisplayName();
					return displayName;
				}
			}
		}
		return "";
	}

	@Override
	public boolean isPropertySet(Object id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		String str = (String) value;

		if ((id == PROPERTY_SWELIST_VALUE) && (tokenDescriptor != null)) {
			PropertyLabelProvider propProvider = (PropertyLabelProvider) tokenDescriptor
					.getLabelProvider();
			if (propProvider != null) {
				Node oneOrMore = propProvider.getNode();
				if (oneOrMore != null) {
					Node xngNode = null;
					xngNode = domUtil.createXNGItem(oneOrMore);
					Node copyNode = dom.getElement((Element) oneOrMore,
							"rng:choice");
					Node nNode = dom.getDocument().importNode(copyNode, true);
					xngNode.appendChild(nNode);
					oneOrMore.appendChild(xngNode);
					Node retNode = null;
					Node listNode = domUtil.findNode(xngNode, "valueList",
							retNode);
					if (listNode != null) {
						Element oneOrMore2 = dom.getElement((Element) listNode,
								"rng:list/rng:oneOrMore");
						if (oneOrMore2 != null) {
							StringTokenizer tok = new StringTokenizer(str, " ");
							while (tok.hasMoreElements()) {

								Node xngNode2 = null;
								xngNode2 = domUtil.createXNGItem(oneOrMore2);
								Node copyNode2 = dom.getElement(
										(Element) oneOrMore2, "rng:data");
								Node nNode2 = dom.getDocument().importNode(
										copyNode2, true);
								xngNode2.appendChild(nNode2);
								oneOrMore2.appendChild(xngNode2);

								dom.setElementValue((Element) nNode2,
										"rng:value", tok.nextToken());
								dom.setAttributeValue((Element) nNode2,
										"rng:value/@selected", "true");
								// propProvider.setNode((Element) xngNode2);
								dom.setAttributeValue((Element) listNode,
										"xng:selected", "true");
							}
							listproperties[listpropertyCount] = "SensorML.ListProperty"
									+ listpropertyCount;
							descriptor = new TextPropertyDescriptor(
									listproperties[listpropertyCount], str);
							PropertyLabelProvider plp = new PropertyLabelProvider(
									xngNode, dom);
							descriptor.setLabelProvider(plp);
							listpropertyDescriptors[listpropertyCount++] = descriptor;

							this.firePropertyChange("INSERT", null, null);
						}
					}
				}
			}

		} else if (id == PROPERTY_INTERVALLIST_VALUE
				&& minintervalDescriptor != null
				&& maxintervalDescriptor != null) {

			PropertyLabelProvider propProvider = (PropertyLabelProvider) minintervalDescriptor
					.getLabelProvider();
			if (propProvider != null) {
				Node oneOrMore = propProvider.getNode();
				if (oneOrMore != null) {
					Node xngNode = null;
					xngNode = domUtil.createXNGItem(oneOrMore);
					Node copyNode = dom.getElement((Element) oneOrMore,
							"rng:choice");
					Node nNode = dom.getDocument().importNode(copyNode, true);
					xngNode.appendChild(nNode);
					oneOrMore.appendChild(xngNode);
					Node retNode = null;
					Node intervalNode = domUtil.findNode(xngNode, "interval",
							retNode);
					if (intervalNode != null) {
						StringTokenizer tok = new StringTokenizer(str, " ");
							
						NodeList intervalItems = dom.getElements(
									(Element) intervalNode,
									"rng:list/rng:data/");
							
						for (int k = 0; k < intervalItems.getLength(); k++) {
							Node intervalItem = intervalItems.item(k);
							dom.setElementValue(
										(Element) intervalItem, "rng:value/",tok.nextToken());
							dom.setAttributeValue((Element) intervalItem,
									"rng:value/@selected", "true");
						}
							
						dom.setAttributeValue((Element) intervalNode,
									"xng:selected", "true");
						
						intervalproperties[intervalpropertyCount] = "SensorML.IntervalProperty"
								+ intervalpropertyCount;
						descriptor = new TextPropertyDescriptor(
								intervalproperties[intervalpropertyCount], str);
						PropertyLabelProvider plp = new PropertyLabelProvider(
								xngNode, dom);
						descriptor.setLabelProvider(plp);
						intervalpropertyDescriptors[intervalpropertyCount++] = descriptor;
						this.firePropertyChange("INSERT", null, null);
					}
				}
			}
		} else if ((id == PROPERTY_CONSTRAINT_CHOICE)
				&& (choiceDescriptor != null)) {
			ChoiceBoxLabelProvider choiceProvider = (ChoiceBoxLabelProvider) choiceDescriptor
					.getLabelProvider();
			PropertyLabelProvider minProvider = (PropertyLabelProvider) minDescriptor
					.getLabelProvider();
			PropertyLabelProvider maxProvider = (PropertyLabelProvider) maxDescriptor
					.getLabelProvider();
			PropertyLabelProvider listProvider = (PropertyLabelProvider) listDescriptor
					.getLabelProvider();

			if (str.equals("min")) {
				Node selectedOne = minProvider.getNode();
				if (!dom.existAttribute((Element) selectedOne, "xng:selected")) {
					dom.setAttributeValue((Element) selectedOne,
							"xng:selected", "true");
					this.firePropertyChange("INSERT", null, null);
				}
				Node deSelected = maxProvider.getNode();
				if (dom.existAttribute((Element) deSelected, "xng:selected")) {
					((Element) deSelected).removeAttribute("xng:selected");
					Element dataElt = dom.getElement((Element) deSelected,
							"rng:data");
					Element valueElt = dom.getElement(dataElt, "rng:value");
					if (valueElt!=null)
						((Element) dataElt).removeChild(valueElt);
					this.firePropertyChange("INSERT", null, null);
				}

				deSelected = listProvider.getNode();
				listpropertyCount = 0;
				if (dom.existAttribute((Element) deSelected, "xng:selected")) {
					((Element) deSelected).removeAttribute("xng:selected");
					NodeList items = dom.getElements((Element) deSelected,
							"xng:item");
					for (int j = 0; j < items.getLength(); j++) {
						deSelected.removeChild(items.item(j));
					}
					this.firePropertyChange("INSERT", null, null);
				}
				choiceProvider.setSelectedNode(selectedOne);

			} else if (str.equals("max")) {
				Node selectedOne = maxProvider.getNode();
				if (!dom.existAttribute((Element) selectedOne, "xng:selected")) {
					dom.setAttributeValue((Element) selectedOne,
							"xng:selected", "true");
					this.firePropertyChange("INSERT", null, null);
				}
				Node deSelected = minProvider.getNode();
				listpropertyCount = 0;
				if (dom.existAttribute((Element) deSelected, "xng:selected")) {
					((Element) deSelected).removeAttribute("xng:selected");
					Element dataElt = dom.getElement((Element) deSelected,
							"rng:data");
					Element valueElt = dom.getElement(dataElt, "rng:value");
					if (valueElt!=null)
						((Element) dataElt).removeChild(valueElt);
					this.firePropertyChange("INSERT", null, null);
				}

				deSelected = listProvider.getNode();
				if (dom.existAttribute((Element) deSelected, "xng:selected")) {
					((Element) deSelected).removeAttribute("xng:selected");
					NodeList items = dom.getElements((Element) deSelected,
							"xng:item");
					for (int j = 0; j < items.getLength(); j++) {
						deSelected.removeChild(items.item(j));
					}
					this.firePropertyChange("INSERT", null, null);
				}
				choiceProvider.setSelectedNode(selectedOne);

			} else if (str.equals("list")) {
				Node selectedOne = listProvider.getNode();
				if (!dom.existAttribute((Element) selectedOne, "xng:selected")) {
					dom.setAttributeValue((Element) selectedOne,
							"xng:selected", "true");
					this.firePropertyChange("INSERT", null, null);
				}
				Node deSelected = maxProvider.getNode();
				if (dom.existAttribute((Element) deSelected, "xng:selected")) {
					((Element) deSelected).removeAttribute("xng:selected");
					Element dataElt = dom.getElement((Element) deSelected,
							"rng:data");
					Element valueElt = dom.getElement(dataElt, "rng:value");
					if (valueElt!=null)
						((Element) dataElt).removeChild(valueElt);
					this.firePropertyChange("INSERT", null, null);
				}
				deSelected = minProvider.getNode();
				if (dom.existAttribute((Element) deSelected, "xng:selected")) {
					((Element) deSelected).removeAttribute("xng:selected");
					Element dataElt = dom.getElement((Element) deSelected,
							"rng:data");
					Element valueElt = dom.getElement(dataElt, "rng:value");
					if (valueElt!=null)
						((Element) dataElt).removeChild(valueElt);
					this.firePropertyChange("INSERT", null, null);
				}
				choiceProvider.setSelectedNode(selectedOne);
			}

		} else if ((id == PROPERTY_CONSTRAINT_MIN) && (minDescriptor != null)) {
			PropertyLabelProvider plp = (PropertyLabelProvider) minDescriptor
					.getLabelProvider();
			dom.setElementValue((Element) plp.getNode(), "rng:data/rng:value",
					str);
			if (!dom.existAttribute((Element) plp.getNode(),
					"rng:data/rng:value/@selected")) {
				dom.setAttributeValue((Element) plp.getNode(),
						"rng:data/rng:value/@selected", "true");
			}
			this.firePropertyChange("INSERT", null, null);

		} else if ((id == PROPERTY_CONSTRAINT_MAX) && (maxDescriptor != null)) {
			PropertyLabelProvider plp = (PropertyLabelProvider) maxDescriptor
					.getLabelProvider();
			dom.setElementValue((Element) plp.getNode(), "rng:data/rng:value",
					str);
			if (!dom.existAttribute((Element) plp.getNode(),
					"rng:data/rng:value/@selected")) {
				dom.setAttributeValue((Element) plp.getNode(),
						"rng:data/rng:value/@selected", "true");
			}
			this.firePropertyChange("INSERT", null, null);
		}

	}

}
