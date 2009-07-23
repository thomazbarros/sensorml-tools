package org.vast.sensormleditor.properties.descriptors;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;

public class SensorMLToolsComboPropertyDescriptor extends
		ComboBoxPropertyDescriptor {

	private String[] labels;

	public SensorMLToolsComboPropertyDescriptor(Object id, String displayName,
			String[] labelsArray) {
		super(id, displayName, labelsArray);
		this.labels = labelsArray;

	}

	public CellEditor createPropertyEditor(Composite parent) {
		CellEditor editor = new ComboBoxCellEditor(parent, labels,
				SWT.NONE);
		if (getValidator() != null) {
			editor.setValidator(getValidator());
		}
		return editor;
	}
	
	public String[] getLabels() {
		return this.labels;
	}
}
