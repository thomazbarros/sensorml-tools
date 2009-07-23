package org.vast.sensormleditor.properties.descriptors;

import java.util.StringTokenizer;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
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

public class MemberChoicePropertySource extends AbstractPropertySource {

	private DOMHelperAddOn domUtil;
	public static final String PROPERTY_ELEMENT_CHOICE = "SensorMLEditor.member.choice";
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

	public MemberChoicePropertySource(Element element, DOMHelper domHelper,
			TreeViewer viewer, SMLTreeEditor smlEditor) {
		super(element, domHelper, viewer, smlEditor);
		domUtil = new DOMHelperAddOn(dom);

		// selectedIndex=currentSelectedIndex = -1;

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
			} else if (dom.hasQName(children.item(j), "rng:oneOrMore")
					|| dom.hasQName(children.item(j), "rng:zeroOrMore")) {
				handleoneOrMore(children.item(j));
				recursiveInitProperties(children.item(j));
				if (done)
					return;
			} else {
				recursiveInitProperties(children.item(j));
				if (done)
					return;
			}
		}
	}

	public void removeField(Object id, Object value) {
		String str = (String) value;
		if (str != null && !str.equals("")) {
			for (int i = 0; i < propertyCount; i++) {
				if (propertyDescriptors[i] != null
						&& id.equals(propertyDescriptors[i].getId())) {
					ChoiceBoxLabelProvider comboProv = (ChoiceBoxLabelProvider) ((ComboBoxPropertyDescriptor) propertyDescriptors[i])
							.getLabelProvider();
					Node changeNode = comboProv.getNode();
					Node parentNode = changeNode.getParentNode();
					Node xngNode = parentNode.getParentNode();
					Node pNode = xngNode.getParentNode();
					pNode.removeChild(xngNode);
					this.treeViewer.refresh();
					break;
				}
			}
		}
	}

	public void handleoneOrMore(Node node) {
		NodeList c = dom.getElements((Element) node, "xng:item");
		for (int j = 0; j < c.getLength(); j++) {
			Node choicele = null;
			if (dom.existElement((Element) node, "sml:member")) {
				choicele = dom.getElement((Element) c.item(j),
						"sml:member/rng:choice");
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

		NodeList children = dom.getChildElements(node);
		for (int k = 0; k < children.getLength(); k++) {
			Element ele1 = (Element) children.item(k);
			if (!ele1.getNamespaceURI().equals(rngaURI)
					&& !ele1.getNodeName().equals("rng:optional")
					&& !ele1.getNamespaceURI().equals(xngURI)) {

				if (ele1.getNodeName().equals("rng:ref")) {
					if (dom.getAttributeValue(ele1, "name").equals(
							"xlink.refGroup")) {
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
							"Group")) {
						// domUtil.resolveRNGREF(ele1);
						Relax2Hybrid transform1 = new Relax2Hybrid();
						try {
							transform1.transform(srcDoc, dom, ele1);
							// now resolve the rng:ref
							transform1.retrieveRelaxNGRef((Element) ele1);
						} catch (DOMTransformException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						getValidChoices(node);
						break;

					} else {
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
					}

				} else if (ele1.getNodeName().equals("rng:choice")
						|| (ele1.getNodeName().equals("rng:group"))) {
					getValidChoices(ele1);

				} else {
					choiceLabels[labelCount] = new String();
					choiceLabels[labelCount++] = ele1.getNodeName();
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
						String name = selected.getLocalName();
						return name;
					}
				}
			}
		}

		return "";
	}

	public void setPropertyValue(Object id, Object value) {
		String str = (String) value;

		if ((id == PROPERTY_ELEMENT_CHOICE) && (choiceDescriptor != null)) {

			Node baseNode = dom.getBaseElement();
			Element oneOrMore = dom.getElement(ele, "rng:oneOrMore");
			String nodepath = domUtil.findPath(baseNode, oneOrMore);
			String newpath = domUtil.createXNGItemPath(baseNode,nodepath + "sml:member");

			Element xngpath = null;
			NodeList choices = dom.getElements(newpath);
			for (int k = 0; k < choices.getLength(); k++) {
				Element choiceNode = (Element) choices.item(k);
				if (!dom.existAttribute(choiceNode, "@xng:selected")) {
					xngpath = (Element) choices.item(k);
				}
			}
			Element newNode = dom.getElement((Element) xngpath, "");

			ChoiceBoxLabelProvider prov = (ChoiceBoxLabelProvider) choiceDescriptor
					.getLabelProvider();
			Node retNode = null;
			Node selectedNode = domUtil.findNode(newNode, str, retNode);
			Node baseElement = dom.getBaseElement();
			Node endElement = selectedNode.getParentNode();

			Node resolvedandSelected = null;
			if (dom.hasQName(selectedNode, "rng:ref")) {
				// domUtil.resolveRNGREF(selectedNode);
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
				resolvedandSelected = domUtil.findNode(newNode, str, retNode);
				dom.setAttributeValue((Element) resolvedandSelected,
						"xng:selected", "true");
				currentSelectedNode = resolvedandSelected;

			} else {
				dom.setAttributeValue((Element) selectedNode, "xng:selected",
						"true");
			}

			String path = domUtil.findPath(baseElement, endElement);
			StringTokenizer tokens = new StringTokenizer(path, "/");

			String lastToken = tokens.nextToken();
			String newpath2 = lastToken;
			while (tokens.hasMoreElements()) {
				String currentToken = tokens.nextToken();
				if (currentToken.equals("rng:group")) {

					NodeList groupNodeCandidates = dom.getElements(newpath2
							+ "/" + currentToken);
					for (int k = 0; k < groupNodeCandidates.getLength(); k++) {
						Element groupNode = (Element) groupNodeCandidates
								.item(k);
						if (!dom.existAttribute((Element) groupNode,
								"@xng:selected")) {
							dom.setAttributeValue((Element) groupNode,
									"xng:selected", "true");
						}
						String newID1 = domUtil.findUniqueID();
						((Element) groupNode).removeAttribute("id");
						dom
								.setAttributeValue((Element) groupNode, "id",
										newID1);
					}

				}
				newpath2 += "/" + currentToken;
				lastToken = currentToken;
			}

			prov.setSelectedNode(currentSelectedNode);
			this.firePropertyChange("INSERT", null, null);
		}
	}

/*	public Node findNode(Node n, String str, Node retNode) {
		NodeList nodes = dom.getChildElements(n);
		for (int i = 0; i < nodes.getLength(); i++) {
			Node oneNode = nodes.item(i);
			if (!oneNode.getNamespaceURI().equals(rngaURI)
					&& !oneNode.getNodeName().equals("rng:optional")
					&& !oneNode.getNamespaceURI().equals(xngURI)) {
				if (str.contains("." + oneNode.getLocalName())) {
					return oneNode;

				} else if (dom.hasQName(oneNode, "rng:ref")
						&& dom.getAttributeValue((Element) oneNode, "name")
								.equals(str)) {
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
	}
*/
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

}
