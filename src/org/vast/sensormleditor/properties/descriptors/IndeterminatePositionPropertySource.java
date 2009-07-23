package org.vast.sensormleditor.properties.descriptors;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.properties.labelproviders.ChoiceBoxLabelProvider;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class IndeterminatePositionPropertySource extends AbstractPropertySource
		implements IPropertySource {

	public static final String PROPERTY_INDETERMINATE_CHOICE = "SensorMLEditor.indeterminate.choice";
	public SensorMLToolsComboPropertyDescriptor choiceDescriptor;
	public String[] choiceLabels;
	protected int labelCount;
	protected boolean stop = false;
	protected Node currentSelectedNode = null;

	// private DOMHelperAddOn domUtil;

	public IndeterminatePositionPropertySource(Element element,
			DOMHelper domHelper, TreeViewer viewer, SMLTreeEditor smlEditor) {
		super(element, domHelper, viewer, smlEditor);
		// domUtil = new DOMHelperAddOn(dom);
		labelCount = 0;
		choiceLabels = new String[20];
		initProperties();
	}

	public void initProperties() {

		if (ele.getNodeName().equals("rng:attribute")
				&& dom.getAttributeValue(ele, "name").equals(
						"indeterminatePosition")) {
			Node indeterminate = ele;
			if (indeterminate != null) {
				Element choice = dom.getElement(ele, "rng:choice");
				NodeList children = dom.getChildElements(choice);
				for (int j = 0; j < children.getLength(); j++) {
					choiceLabels[labelCount] = new String();
					choiceLabels[labelCount++] = children.item(j)
							.getTextContent();
					String selected = dom.getAttributeValue((Element) children
							.item(j), "xng:selected");
					if (selected != null && selected.equalsIgnoreCase("TRUE")) {
						currentSelectedNode = children.item(j);
					}
				}

				String[] retArray = new String[labelCount];
				System.arraycopy(choiceLabels, 0, retArray, 0, labelCount);

				choiceDescriptor = new SensorMLToolsComboPropertyDescriptor(
						PROPERTY_INDETERMINATE_CHOICE,
						"interdeterminatePosition", retArray);
				ChoiceBoxLabelProvider prov = new ChoiceBoxLabelProvider(
						retArray, indeterminate, dom);
				prov.setSelectedNode(currentSelectedNode);
				choiceDescriptor.setLabelProvider(prov);
			}
		}
	}

	public String[] getChoices(Object id) {
		/*
		 * if ((id == PROPERTY_UOM_CHOICE) && (choiceDescriptor != null)) {
		 * ChoiceBoxLabelProvider cProv = (ChoiceBoxLabelProvider)
		 * choiceDescriptor.getLabelProvider(); return cProv.getValues(); }
		 * return null;
		 */
		if (choiceLabels == null)
			initProperties();
		return choiceLabels;
	}

	@Override
	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return null;

	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getPropertyValue(Object id) {
		String attValue = "";
		if (choiceDescriptor != null) {
			ChoiceBoxLabelProvider prov = (ChoiceBoxLabelProvider) choiceDescriptor
					.getLabelProvider();

			Node selected = (Node) prov.getSelectedNode();
			if (selected != null) {
				if ((id == PROPERTY_INDETERMINATE_CHOICE)
						&& (choiceDescriptor != null)) {
					attValue = selected.getTextContent();
				}
			}
		}
		return attValue;

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
		if ((id == PROPERTY_INDETERMINATE_CHOICE) && (choiceDescriptor != null)) {
			ChoiceBoxLabelProvider prov = (ChoiceBoxLabelProvider) choiceDescriptor
					.getLabelProvider();
			Node choice = dom
					.getElement((Element) prov.getNode(), "rng:choice");
			NodeList cList = dom.getChildElements(choice);
			for (int i = 0; i < cList.getLength(); i++) {
				if (dom.hasQName(cList.item(i), "rng:value")) {
					String name = cList.item(i).getTextContent();

					if (name.equals(str)) {
						dom.setAttributeValue((Element) cList.item(i),
								"xng:selected", "true");
						currentSelectedNode = cList.item(i);
						prov.setSelectedNode(currentSelectedNode);
					} else {
						((Element) cList.item(i))
								.removeAttribute("xng:selected");
					}

					this.treeViewer.refresh();
				}

			}
			this.firePropertyChange("INSERT", null, null);
		}

	}

}
