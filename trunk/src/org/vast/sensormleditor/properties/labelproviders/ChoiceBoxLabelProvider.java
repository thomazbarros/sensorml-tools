package org.vast.sensormleditor.properties.labelproviders;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Node;

public class ChoiceBoxLabelProvider implements ILabelProvider {

	DOMHelper dom;
	Node node;
	private String[] values;
	private Object selected;
	 

	public ChoiceBoxLabelProvider(String[] values, Node n, DOMHelper d) {
		this.values = values;
		this.node = n;
		this.dom = d;
	}
	
	public Node getNode() {
		return this.node;
	}

	@Override
	public Image getImage(Object element) {
		// TODO Auto-generated method stub
		return null;
	}
	
    public String[] getValues() {
        return values;
    }
    
    public void setSelectedNode(Object selected){
    	this.selected = selected;
    }
    
    
    public Object getSelectedNode(){
    	return selected;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

	@Override
	public String getText(Object element) {
		if (element == null) {
            return ""; //$NON-NLS-1$
        }

        if (element instanceof Integer) {
            int index = ((Integer) element).intValue();
            if (index >= 0 && index < values.length) {
                return values[index];
            } else {
                return ""; //$NON-NLS-1$
            }
        }

        return ""; //$NON-NLS-1$
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
