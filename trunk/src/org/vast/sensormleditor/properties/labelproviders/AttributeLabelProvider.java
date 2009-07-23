package org.vast.sensormleditor.properties.labelproviders;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class AttributeLabelProvider implements ILabelProvider {
	DOMHelper dom;
	Node node;

	public AttributeLabelProvider(DOMHelper dom, Node n) {
		this.dom = dom;
		this.node = n;

	}

	@Override
	public Image getImage(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	public Node getNode() {
		return this.node;
	}

	@Override
	public String getText(Object element) {
		String attributeName = (String) element;
		String name = null;
		
		if (!attributeName.equals(""))
			
			name = dom.getAttributeValue((Element) node, attributeName);

		if ((name == null) || name.equals("")) {
			return node.getTextContent();
		} else {
			return name;
		}

	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

}
