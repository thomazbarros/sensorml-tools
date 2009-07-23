package org.vast.sensormleditor.properties.descriptors;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.properties.labelproviders.ChoiceBoxLabelProvider;
import org.vast.sensormleditor.relaxNG.Relax2Hybrid;
import org.vast.sensormleditor.util.DOMHelperAddOn;
import org.vast.xml.DOMHelper;
import org.vast.xml.transform.DOMTransformException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ElementChoicePropertySource extends AbstractPropertySource
		implements IPropertySource {

	public static final String PROPERTY_ELEMENT_CHOICE = "SensorMLEditor.element.choice";
	public SensorMLToolsComboPropertyDescriptor choiceDescriptor;
	public String[] choiceLabels;
	protected int labelCount;
	protected static final String[] properties = new String[20];
	protected IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[20];
	protected int propertyCount;
	private PropertyDescriptor descriptor;
	protected String title;
	boolean done = false;
	protected Node currentSelectedNode;
	boolean stop = false;
	protected DOMHelperAddOn domUtil;

	public ElementChoicePropertySource(Element element, DOMHelper domHelper,
			TreeViewer viewer, SMLTreeEditor smlEditor) {
		super(element, domHelper, viewer, smlEditor);
		domUtil = new DOMHelperAddOn(dom);
		initProperties();
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void initProperties() {
		recursiveInitProperties((Node) ele);
	}

	public void recursiveInitProperties(Node node) {
		NodeList children = dom.getChildElements(node);
		for (int j = 0; j < children.getLength(); j++) {
			if (dom.hasQName(children.item(j), "a:documentation")
					|| dom.hasQName(children.item(j), "rng:optional")) {
				continue;
			} else if (dom.hasQName(children.item(j), "rng:choice")) {
				handleChoice(children.item(j));
				done = true;
				break;
				
			} else if (dom.hasQName(children.item(j), "swe:elementCount")) {
				continue;
				
			} else if (dom.hasQName(children.item(j), "rng:oneOrMore")
					|| dom.hasQName(children.item(j), "rng:zeroOrMore")) {

				if (dom.existElement((Element) children.item(j), "xng:item")) {
					handleoneOrMore(children.item(j));
				} else if (dom.existElement((Element) children.item(j),
						"swe:field")) {
					Node choicele = dom.getElement((Element) children.item(j),
							"swe:field/rng:choice");
					labelCount = 0;
					choiceLabels = new String[20];
					getValidChoices(choicele);
				} else if (dom.existElement((Element) children.item(j),
						"sml:output")) {
					Node choicele = dom.getElement((Element) children.item(j),
							"sml:output/rng:choice");
					labelCount = 0;
					choiceLabels = new String[20];
					getValidChoices(choicele);
				} else if (dom.existElement((Element) children.item(j),
						"sml:input")) {
					Node choicele = dom.getElement((Element) children.item(j),
							"sml:input/rng:choice");
					labelCount = 0;
					choiceLabels = new String[20];
					getValidChoices(choicele);
				} else if (dom.existElement((Element) children.item(j),
						"sml:parameter")) {
					Node choicele = dom.getElement((Element) children.item(j),
							"sml:parameter/rng:choice");
					labelCount = 0;
					choiceLabels = new String[20];
					getValidChoices(choicele);
				} else if (dom.existElement((Element) children.item(j),
						"sml:component")) {
					Node choicele = dom.getElement((Element) children.item(j),
							"sml:component/rng:choice");
					labelCount = 0;
					choiceLabels = new String[20];
					getValidChoices(choicele);
				} else if (dom.existElement((Element) children.item(j),"swe:coordinate")) {
					Node choicele = dom.getElement((Element) children.item(j),
									"swe:coordinate/rng:choice");
					labelCount = 0;
					choiceLabels = new String[20];
					getValidChoices(choicele);
					
				} else if (dom.existElement((Element) children.item(j),"swe:condition")) {
					Node choicele = dom.getElement((Element) children.item(j),
									"swe:condition/rng:choice");
					labelCount = 0;
					choiceLabels = new String[20];
					getValidChoices(choicele);
				}
			} else {
				recursiveInitProperties(children.item(j));
				if (done)
					return;
			}
		}
	}

	public void removeField(Object id, Object value) {
		Integer newInt = new Integer((Integer) value);
		int index = newInt.intValue();
		if (propertyDescriptors[index] != null) {
			ChoiceBoxLabelProvider comboProv = (ChoiceBoxLabelProvider) ((ComboBoxPropertyDescriptor) propertyDescriptors[index])
					.getLabelProvider();
			Node changeNode = comboProv.getNode();
			Node parentNode = changeNode.getParentNode();
			Node xngNode = parentNode.getParentNode();
			Node pNode = xngNode.getParentNode();
			pNode.removeChild(xngNode);
			this.firePropertyChange("INSERT", null, null);
		}
	}

	public void handleoneOrMore(Node node) {
		NodeList c = dom.getElements((Element) node, "xng:item");
		for (int j = 0; j < c.getLength(); j++) {
			Node choicele = null;
			if (dom.existElement((Element) node, "swe:field")) {
				choicele = dom.getElement((Element) c.item(j),
						"swe:field/rng:choice");
			} else if (dom.existElement((Element) node, "sml:output")) {
				choicele = dom.getElement((Element) c.item(j),
						"sml:output/rng:choice");
			} else if (dom.existElement((Element) node, "sml:input")) {
				choicele = dom.getElement((Element) c.item(j),
						"sml:input/rng:choice");
			} else if (dom.existElement((Element) node, "sml:parameter")) {
				choicele = dom.getElement((Element) c.item(j),
						"sml:parameter/rng:choice");
			} else if (dom.existElement((Element) node, "sml:component")) {
				choicele = dom.getElement((Element) c.item(j),
						"sml:component/rng:choice");
			} else if (dom.existElement((Element) node, "swe:coordinate")) {
				choicele = dom.getElement((Element) c.item(j),
						"swe:coordinate/rng:choice");
			} else if (dom.existElement((Element) node, "swe:condition")) {
				choicele = dom.getElement((Element) c.item(j),
						"swe:condition/rng:choice");
			}
			labelCount = 0;
			choiceLabels = new String[20];
			getValidChoices(choicele);

			String[] retArray = new String[labelCount];
			System.arraycopy(choiceLabels, 0, retArray, 0, labelCount);

			properties[propertyCount] = "SensorML.Property" + propertyCount;
			descriptor = new SensorMLToolsComboPropertyDescriptor(
					properties[propertyCount], choicele.getParentNode()
							.getLocalName(), retArray);
			ChoiceBoxLabelProvider prov = new ChoiceBoxLabelProvider(retArray,
					choicele, dom);
			descriptor.setLabelProvider(prov);

			prov.setSelectedNode(currentSelectedNode);
			// descriptor.setCategory(node.getLocalName());
			propertyDescriptors[propertyCount++] = descriptor;
		}
	}

	public void handleChoice(Node node) {

		labelCount = 0;
		choiceLabels = new String[20];
		getValidChoices(node);
		String[] retArray = new String[labelCount];
		System.arraycopy(choiceLabels, 0, retArray, 0, labelCount);

		choiceDescriptor = new SensorMLToolsComboPropertyDescriptor(
				PROPERTY_ELEMENT_CHOICE, node.getParentNode().getLocalName(),
				retArray);
		ChoiceBoxLabelProvider prov = new ChoiceBoxLabelProvider(retArray,
				node, dom);
		prov.setSelectedNode(currentSelectedNode);
		choiceDescriptor.setLabelProvider(prov);
		choiceDescriptor.setCategory(node.getLocalName());

		return;
	}

	public void getValidChoices(Node node) {

		if (node==null)return;
		NodeList children = dom.getChildElements(node);
		for (int k = 0; k < children.getLength(); k++) {
			Element ele1 = (Element) children.item(k);
			if (!ele1.getNamespaceURI().equals(rngaURI)
					&& !ele1.getNodeName().equals("rng:optional")
					&& !ele1.getNamespaceURI().equals(xngURI)) {

				if (ele1.getNodeName().equals("rng:ref")) {
					if (dom.getAttributeValue(ele1, "name").equals(
							"xlink.href")) {
						choiceLabels[labelCount] = new String();
						choiceLabels[labelCount++] = dom.getAttributeValue(
								ele1, "name");
						String selected = dom.getAttributeValue((Element) ele1,
								"xng:selected");
						if (selected != null
								&& selected.equalsIgnoreCase("TRUE")) {
							currentSelectedNode = ele1;
						}
						continue;
					} else if (dom.getAttributeValue(ele1, "name").contains(
							"Any") || (dom.getAttributeValue(ele1,"name").equals("sml.ProcessGroup"))) {
						Relax2Hybrid transform1 = new Relax2Hybrid();
						try {
							transform1.transform(srcDoc, dom, ele1);
							// now resolve the rng:ref
							transform1.retrieveRelaxNGRef(ele1);
						} catch (DOMTransformException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						getValidChoices(node);
						break;

					} else {
						choiceLabels[labelCount] = new String();
						String localName = dom.getAttributeValue(
								ele1, "name");
						if (localName.contains(".")){
							int startIndex = localName.lastIndexOf(".");
							choiceLabels[labelCount++] = localName.substring(startIndex+1);
						} else {
							choiceLabels[labelCount++] = localName;
						}
						String selected = dom.getAttributeValue((Element) ele1,
								"xng:selected");
						if (selected != null
								&& selected.equalsIgnoreCase("TRUE")) {
							currentSelectedNode = ele1;
						}
						continue;
					}

				} else if (ele1.getNodeName().equals("rng:choice")
						|| (ele1.getNodeName().equals("rng:group"))) {
					getValidChoices(ele1);

				} else {
					choiceLabels[labelCount] = new String();
					if (ele1.getNodeName().equals("rng:attribute"))
						choiceLabels[labelCount++] = dom.getAttributeValue(
								ele1, "name");
					else 
						choiceLabels[labelCount++] = ele1.getLocalName();
					
					String selected = dom.getAttributeValue((Element) ele1,
							"xng:selected");
					if (selected != null && selected.equalsIgnoreCase("TRUE")) {
						currentSelectedNode = ele1;
					}
				}
			}
		}
		return;
	}

	public Object getPropertyValue(Object id) {
		if ((id == PROPERTY_ELEMENT_CHOICE) && (choiceDescriptor != null)) {
			return "";
		} else {
			for (int i = 0; i < propertyCount; i++) {
				if (propertyDescriptors[i] != null
						&& id.equals(propertyDescriptors[i].getId())) {
					ChoiceBoxLabelProvider comboProv = (ChoiceBoxLabelProvider) ((ComboBoxPropertyDescriptor) propertyDescriptors[i])
							.getLabelProvider();
					Node selected = (Node) comboProv.getSelectedNode();

					if (selected != null) {
						if (dom.hasQName(selected, "rng:attribute")){
							return dom.getAttributeValue((Element) selected,"name");
						}
						else {
							String name = selected.getLocalName();
							return name;
						}
					}
				}
			}
		}

		return "";
	}

	public void setPropertyValue(Object id, Object value) {
		String str = (String) value;

		// selectedIndex = intVal.intValue();
		if ((id == PROPERTY_ELEMENT_CHOICE) && (choiceDescriptor != null)) {

			ChoiceBoxLabelProvider prov = (ChoiceBoxLabelProvider) choiceDescriptor
					.getLabelProvider();
			Node retNode = null;
			if (str.contains(".")) {
				int startIndex = str.lastIndexOf(".");
				str = str.substring(startIndex + 1);
			} else if (str.contains(":")) {
				int startIndex = str.lastIndexOf(":");
				str = str.substring(startIndex + 1);
			}
			Node selectedNode = domUtil.findNode(prov.getNode(), str, retNode);
			if (selectedNode != null) {
				//Node baseElement = dom.getBaseElement();
				Node endElement = selectedNode.getParentNode();

				Node resolvedandSelected = null;
				//String saveName = ((Element) selectedNode).getAttribute("name");
				if (dom.hasQName(selectedNode, "rng:ref")) {
					Relax2Hybrid transform1 = new Relax2Hybrid();
					try {
						transform1.transform(srcDoc, dom, selectedNode);
						// now resolve the rng:ref
						transform1.retrieveRelaxNGRef((Element) selectedNode);
					} catch (DOMTransformException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}


					retNode = null;
					resolvedandSelected = domUtil.findNode(endElement, str,
							retNode);
					if (resolvedandSelected != null) {
						dom.setAttributeValue((Element) resolvedandSelected,
								"xng:selected", "true");
						currentSelectedNode = resolvedandSelected;
					}

				} else {
					dom.setAttributeValue((Element) selectedNode,
							"xng:selected", "true");
				}


				prov.setSelectedNode(currentSelectedNode);
				// currentSelectedIndex = selectedIndex;
				this.firePropertyChange("INSERT", null, null);
			
				
			}
		}
	}


/*	public Node findNode(Node n, String str, Node retNode) {
		NodeList nodes = dom.getChildElements(n);
		for (int i = 0; i < nodes.getLength(); i++) {
			Node oneNode = nodes.item(i);
			if (!oneNode.getNamespaceURI().equals(rngaURI)
					&& !oneNode.getNodeName().equals("rng:optional")
					&& !oneNode.getNamespaceURI().equals(xngURI)) {
				if (str.equals(oneNode.getLocalName())) {
					return oneNode;

				} else if (dom.hasQName(oneNode, "rng:ref")) {
					String name = dom.getAttributeValue((Element) oneNode, "name");
					int startIndex = name.lastIndexOf(".");
					name = name.substring(startIndex+1);
					if (name.equals(str))
						return oneNode;
					
				} else if (dom.hasQName(oneNode, "rng:attribute")) {
					String name = dom.getAttributeValue((Element) oneNode,
							"name");
					int startIndex = name.lastIndexOf(":");
					name = name.substring(startIndex + 1);
					if (name.equals(str))
						return oneNode;
					
				} else if (dom.hasQName(oneNode, "rng:choice")
						|| (dom.hasQName(oneNode, "rng:group"))) {
					retNode = findNode(oneNode, str, retNode);
					if (retNode != null)
						return retNode;
				}
			}
		}
		return retNode;
	}*/

	public String[] getChoices(Object id) {
		if (choiceLabels == null)
			initProperties();

		return choiceLabels;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] retArray;
		if (propertyDescriptors == null || propertyDescriptors.length == 0)
			initProperties();

		retArray = new IPropertyDescriptor[propertyCount];
		System.arraycopy(propertyDescriptors, 0, retArray, 0, propertyCount);
		return retArray;

	}

	@Override
	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return null;
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

}
