package org.vast.sensormleditor.properties.sections;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AdvancedPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.vast.sensormleditor.editors.SMLTreeContentProvider;
import org.vast.xml.DOMHelper;

public class SMLAdvancedPropertySection extends AdvancedPropertySection {

	public SMLAdvancedPropertySection() {
		
	}
	
	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ISection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);
		DOMHelper dom = new DOMHelper();
		page.setPropertySourceProvider(new SMLTreeContentProvider( null, dom));
	}
	
	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		page.selectionChanged(part, selection);
	
	}


}
