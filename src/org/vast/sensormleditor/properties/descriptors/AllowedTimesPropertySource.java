package org.vast.sensormleditor.properties.descriptors;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AllowedTimesPropertySource extends AbstractPropertySource implements
		IPropertySource {

	public String constraintType;
	public static final String PROPERTY_MIN_VALUE = "SensorMLEditor.min.value";
	public static final String PROPERTY_MAX_VALUE = "SensorMLEditor.max.value";
	public TextPropertyDescriptor minDescriptor;
	public TextPropertyDescriptor maxDescriptor;
	public static final String PROPERTY_ALLOWED_VALUES_CHOICE = "SensorMLEditor.allowedValues.choicem";
	public static final String PROPERTY_UOM_CHOICE = "SensorMLEditor.uom.choice";
	public PropertyDescriptor uomDescriptor;
	public SensorMLToolsComboPropertyDescriptor choiceDescriptor;
	public String[] choiceLabels;
	protected int labelCount;
	protected boolean stop = false;
	protected Node currentSelectedNode = null;

	public String min;
	public String max;

	public AllowedTimesPropertySource(Element element, DOMHelper domHelper,
			TreeViewer viewer, SMLTreeEditor smlEditor) {
		super(element, domHelper, viewer, smlEditor);
		initProperties();
	}

	public void initProperties() {
		recursiveInitProperties(ele);
	}

	public void recursiveInitProperties(Node node) {
		NodeList children = dom.getChildElements(node);
		for (int j = 0; j < children.getLength(); j++) {
			if (dom.hasQName(children.item(j), "swe:constraint")) {
				constraintType = setConstraintType(children.item(j));
			} else if (dom.hasQName(children.item(j), "a:documentation")) {
				continue;
			} else {
				recursiveInitProperties(children.item(j));
			}
		}
	}

	public String getConstraintType() {
		return constraintType;
	}
	
	public boolean hasMin(Node node){
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++){
			if (children.item(i).getNodeName().equals("rng:param") ){
				if (children.item(i).hasAttributes()){
					NamedNodeMap attList = children.item(i).getAttributes();
					for (int attIndex = 0; attIndex < attList.getLength(); attIndex++) {
						if (attList.item(attIndex).getNodeName().equals("name")){
							String value = attList.item(attIndex).getNodeValue();
							if (value.equals("minExclusive"))
								return true;
						}
					}
				}
			}
			else
				if (hasMin(children.item(i)))
					return true;	
		}
		return false;
	}
	
	public boolean hasMax(Node node){
		
		return false;
	}
	
	public boolean hasList(Node node){
		
		return false;
	}
	
	public String setConstraintType(Node node){
		if (hasMin(node) && hasMax(node))
			return "Interval";
		if (hasMin(node))
			return "Min";
		if (hasMax(node))
			return "Max";
		if (hasList(node))
			return "List";
		return "";
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

	@Override
	public Object getPropertyValue(Object id) {
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

	@Override
	public void setPropertyValue(Object id, Object value) {
		this.firePropertyChange("INSERT", null,null);
	}

}
