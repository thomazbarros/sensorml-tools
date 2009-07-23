package org.vast.sensormleditor.properties.descriptors;

import java.io.IOException;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.properties.labelproviders.ChoiceBoxLabelProvider;
import org.vast.sensormleditor.properties.labelproviders.PropertyLabelProvider;
import org.vast.sensormleditor.relaxNG.Relax2Hybrid;
import org.vast.sensormleditor.util.DOMHelperAddOn;
import org.vast.xml.DOMHelper;
import org.vast.xml.transform.DOMTransformException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DataArrayValuesPropertySource extends AbstractPropertySource
		implements IPropertySource {

	public static final String PROPERTY_SWELIST_VALUE = "SensorMLEditor.swelist.value";
	public static final String PROPERTY_XLINK_HREF = "SensorMLEditor.xlink.href";

	public static final String PROPERTY_CONSTRAINT_LIST = "SensorMLEditor.constraint.list";
	public static final String PROPERTY_CONSTRAINT_CHOICE = "SensorMLEditor.constraint.choice";

	public TextPropertyDescriptor xlinkDescriptor;
	public TextPropertyDescriptor listDescriptor;
	public SensorMLToolsComboPropertyDescriptor choiceDescriptor;

	/*protected static final String[] listproperties = new String[20];
	protected IPropertyDescriptor[] listpropertyDescriptors = new IPropertyDescriptor[20];
	protected int listpropertyCount;*/

	private PropertyDescriptor descriptor;
	public String[] choiceLabels;
	protected int labelCount;
	public String constraintType;
	protected boolean stop = false;
	// protected Node currentSelectedNode = null;
	protected String min;
	protected String max;
	protected DOMHelperAddOn domUtil;

	public DataArrayValuesPropertySource(Element element, DOMHelper domHelper,
			TreeViewer viewer, SMLTreeEditor smlEditor) {
		super(element, domHelper, viewer, smlEditor);
		domUtil = new DOMHelperAddOn(dom);
		labelCount = 0;
		choiceLabels = new String[20];
		initProperties();
	}

	public void initProperties() {
		Node retNode = null;

		Node dataArray = domUtil.findPreviousNode(ele, "DataArray", retNode);
		Node arrayValues = domUtil.findNode(dataArray, "arrayValues", retNode);
		if (arrayValues != null) {
			if (arrayValues.getNodeName().equals("rng:ref")) {
				Relax2Hybrid transform1 = new Relax2Hybrid();
				try {
					transform1.transform(srcDoc, dom, arrayValues);
					// now resolve the rng:ref
					transform1.retrieveRelaxNGRef((Element) arrayValues);
				} catch (DOMTransformException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		Node sweValues = domUtil.findNode(dataArray, "values", retNode);

		choiceLabels[labelCount] = new String();
		choiceLabels[labelCount++] = "xlink";
		choiceLabels[labelCount] = new String();
		choiceLabels[labelCount++] = "values";

		String[] retArray = new String[labelCount];
		System.arraycopy(choiceLabels, 0, retArray, 0, labelCount);

		choiceDescriptor = new SensorMLToolsComboPropertyDescriptor(
				PROPERTY_CONSTRAINT_CHOICE, "choice", retArray);
		ChoiceBoxLabelProvider prov = new ChoiceBoxLabelProvider(retArray,
				dataArray, dom);
		choiceDescriptor.setLabelProvider(prov);

		if (sweValues != null) {

			Node xlinkNode = domUtil.findNode(sweValues.getParentNode(),
					"href", retNode);
			if (xlinkNode != null) {

				if (xlinkNode.getNodeName().equals("rng:ref")) {
					Relax2Hybrid transform1 = new Relax2Hybrid();
					try {
						transform1.transform(srcDoc, dom, xlinkNode);
						// now resolve the rng:ref
						transform1.retrieveRelaxNGRef((Element) xlinkNode);
					} catch (DOMTransformException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		

					xlinkNode = domUtil.findNode(sweValues.getParentNode(),
							"href", retNode);

				}
				xlinkDescriptor = new TextPropertyDescriptor(
						PROPERTY_XLINK_HREF, "name");
				PropertyLabelProvider xlinkPLP = new PropertyLabelProvider(
						xlinkNode, dom);
				xlinkDescriptor.setLabelProvider(xlinkPLP);
				if (dom.existAttribute((Element) xlinkNode, "xng:selected")) {
					ChoiceBoxLabelProvider cblp = (ChoiceBoxLabelProvider) choiceDescriptor
							.getLabelProvider();
					cblp.setSelectedNode(xlinkNode);
				}
			}

			listDescriptor = new TextPropertyDescriptor(
					PROPERTY_CONSTRAINT_LIST, "name");
			PropertyLabelProvider listPLP = new PropertyLabelProvider(
					sweValues, dom);
			listDescriptor.setLabelProvider(listPLP);
			if (dom.existAttribute((Element) sweValues, "xng:selected")) {
				ChoiceBoxLabelProvider cblp = (ChoiceBoxLabelProvider) choiceDescriptor
						.getLabelProvider();
				cblp.setSelectedNode(sweValues);
			}

		}
	}

/*	public void removeAllowedValue(Object id, Object value) {
		Integer newInt = new Integer((Integer) value);
		int index = newInt.intValue();
		if (listpropertyDescriptors[index] != null
				&& listpropertyDescriptors[index].getId() == id) {
			PropertyLabelProvider propProvider = (PropertyLabelProvider) listpropertyDescriptors[index]
					.getLabelProvider();
			Node changeNode = propProvider.getNode();
			Node parentNode = changeNode.getParentNode();
			parentNode.removeChild(changeNode);
			// listpropertyDescriptors=null;
			// listpropertyCount=0;
			this.firePropertyChange("INSERT", null, null);

		}
	}*/

	public String getConstraintType() {
		return constraintType;
	}

	@Override
	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return null;
	}

/*	public IPropertyDescriptor[] getListPropertyDescriptors() {
		IPropertyDescriptor[] retArray;

		if (listpropertyDescriptors == null
				|| listpropertyDescriptors.length == 0
				|| listpropertyCount == 0)
			initProperties();

		retArray = new IPropertyDescriptor[listpropertyCount];
		System.arraycopy(listpropertyDescriptors, 0, retArray, 0,
				listpropertyCount);
		return retArray;
	}*/

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		/*IPropertyDescriptor[] retArray;
		if (listpropertyDescriptors == null
				|| listpropertyDescriptors.length == 0)
			initProperties();

		retArray = new IPropertyDescriptor[listpropertyCount];
		System.arraycopy(listpropertyDescriptors, 0, retArray, 0,
				listpropertyCount);*/
		//return retArray;
		return null;
	}

	@Override
	public Object getPropertyValue(Object id) {

		String label = null;

		if (id == PROPERTY_SWELIST_VALUE) {
			PropertyLabelProvider propProvider = (PropertyLabelProvider) listDescriptor
				.getLabelProvider();
			if (propProvider != null) {
				Node sweValues = propProvider.getNode();
				if (sweValues != null) {
					label = dom.getElementValue((Element) sweValues,"rng:text");
					//dom.setElementValue((Element) sweValues, "rng:text", str);
					return label;
				}
			}

		} else if (id == PROPERTY_CONSTRAINT_CHOICE && choiceDescriptor != null) {
			ChoiceBoxLabelProvider prov = (ChoiceBoxLabelProvider) choiceDescriptor
					.getLabelProvider();
			Node sel = (Node) prov.getSelectedNode();

			if (sel != null) {
				if (sel.getNodeName().equals("rng:attribute"))
					return "xlink";
				else
					return "values";
			}

		} else if ((id == PROPERTY_XLINK_HREF) && (xlinkDescriptor != null)) {
			PropertyLabelProvider plp = (PropertyLabelProvider) xlinkDescriptor
					.getLabelProvider();
			label = dom.getElementValue((Element) plp.getNode(),
					"rng:data/rng:value");
			return label;

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

		if ((id == PROPERTY_SWELIST_VALUE) && (listDescriptor != null)) {
			PropertyLabelProvider propProvider = (PropertyLabelProvider) listDescriptor
					.getLabelProvider();
			if (propProvider != null) {
				Node sweValues = propProvider.getNode();
				if (sweValues != null) {
					dom.setElementValue((Element) sweValues, "rng:text", str);
					if (!dom.existAttribute((Element) sweValues,"rng:text/@selected")) {
						dom.setAttributeValue((Element) sweValues,
								"rng:text/@selected", "true");
						
					}
					Node optional = sweValues.getParentNode().getParentNode();
					if (optional != null && optional.getNodeName().equals("rng:optional")){
						if (!dom.existAttribute((Element) optional, "xng:selected")) {
							dom.setAttributeValue((Element) optional,
								"xng:selected", "true");
						}
					}
				}
			}
			this.firePropertyChange("INSERT", null, null);
			
		} else if ((id == PROPERTY_XLINK_HREF) && (xlinkDescriptor != null)) {
			PropertyLabelProvider plp = (PropertyLabelProvider) xlinkDescriptor
					.getLabelProvider();
			dom.setElementValue((Element) plp.getNode(), "rng:data/rng:value",
					str);
			if (!dom.existAttribute((Element) plp.getNode(),
					"rng:data/rng:value/@selected")) {
				dom.setAttributeValue((Element) plp.getNode(),
						"rng:data/rng:value/@selected", "true");
			}
			Node optional = plp.getNode().getParentNode().getParentNode();
			if (optional != null && optional.getNodeName().equals("rng:optional")){
				if (!dom.existAttribute((Element) optional, "xng:selected")) {
					dom.setAttributeValue((Element) optional,
						"xng:selected", "true");
				}
			}
			this.firePropertyChange("INSERT", null, null);

		} else if ((id == PROPERTY_CONSTRAINT_CHOICE)
				&& (choiceDescriptor != null)) {
			ChoiceBoxLabelProvider choiceProvider = (ChoiceBoxLabelProvider) choiceDescriptor
					.getLabelProvider();
			PropertyLabelProvider xlinkProvider = (PropertyLabelProvider) xlinkDescriptor
					.getLabelProvider();
			PropertyLabelProvider listProvider = (PropertyLabelProvider) listDescriptor
					.getLabelProvider();

			if (str.equals("xlink")) {
				Node selectedOne = xlinkProvider.getNode();
				if (!dom.existAttribute((Element) selectedOne, "xng:selected")) {
					dom.setAttributeValue((Element) selectedOne,
							"xng:selected", "true");
					this.firePropertyChange("INSERT", null, null);
				}

				Node deSelected = listProvider.getNode();
				
				if (dom.existAttribute((Element) deSelected, "rng:text/@selected")) {
					((Element) deSelected).removeAttribute("rng:text/@selected");
					dom.setElementValue((Element) deSelected, "rng:text","");
					this.firePropertyChange("INSERT", null, null);
				}
				choiceProvider.setSelectedNode(selectedOne);

			} else if (str.equals("values")) {
				Node selectedOne = listProvider.getNode();
				if (!dom.existAttribute((Element) selectedOne, "xng:selected")) {
					dom.setAttributeValue((Element) selectedOne,
							"rng:text/@selected", "true");
					this.firePropertyChange("INSERT", null, null);
				}
				Node deSelected = xlinkProvider.getNode();
				if (dom.existAttribute((Element) deSelected, "xng:selected")) {
					((Element) deSelected).removeAttribute("xng:selected");
					Element dataElt = dom.getElement((Element) deSelected,
							"rng:data");
					Element valueElt = dom.getElement(dataElt, "rng:value");
					if (valueElt != null)
						((Element) dataElt).removeChild(valueElt);
					this.firePropertyChange("INSERT", null, null);
				}
				choiceProvider.setSelectedNode(selectedOne);
			}

		}
		try {
			dom.serialize(dom.getBaseElement(), System.out, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
