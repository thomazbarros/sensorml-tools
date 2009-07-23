package org.vast.sensormleditor.properties.sections;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.outlineview.SensorMLContentOutlinePage;
import org.vast.sensormleditor.properties.descriptors.TextBlockPropertySource;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;

public class TextBlockSection extends AbstractPropertySection {
	private String headerLabel = "Text Based Encoding";
	private CLabel headerlabelLabel;

	private CLabel typeofExemplabelLabel;
	private String typeofExempLabel = "Token Separator:";
	private Text typeofExempText;
	
	private CLabel dateofExemplabelLabel;
	private String dateofExempLabel = "Block Separator:";
	private Text dateofExempText;
	
	private CLabel manReviewlabelLabel;
	private String manReviewLabel = "Decimal Separator:";
	private Text manReviewText;

	private SMLTreeEditor smlEditor;
	private Element element;
	private DOMHelper dom;
	private TreeViewer treeViewer;
	private TextBlockPropertySource securityPropertySource;
	private Font fontBold = new Font(PlatformUI.getWorkbench().getDisplay(),
			"Tahoma", 8, SWT.BOLD);

	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		Assert.isTrue(selection instanceof IStructuredSelection);
		Object input = ((IStructuredSelection) selection).getFirstElement();
		Assert.isTrue(input instanceof Element);
		this.element = (Element) input;
		if (part instanceof SMLTreeEditor) {
			smlEditor = ((SMLTreeEditor) part);
			dom = smlEditor.getModel();
			treeViewer = (TreeViewer) ((SMLTreeEditor) part).getTreeViewer();
		} else {
			smlEditor = (SMLTreeEditor) ((SensorMLContentOutlinePage) part).smlEditor;
			dom = smlEditor.getModel();
			treeViewer = smlEditor.getTreeViewer();
		}

		if (element != null) {
			securityPropertySource = new TextBlockPropertySource(
					element, dom, treeViewer, smlEditor);
		}
	}

	private ModifyListener listener = new ModifyListener() {
		public void modifyText(ModifyEvent arg0) {
			Object src = arg0.getSource();
			if (src == typeofExempText) {
				securityPropertySource.setPropertyValue(
						TextBlockPropertySource.PROPERTY_TOKEN_SEPARATOR,
						typeofExempText.getText());
			} else if (src == dateofExempText) {
				securityPropertySource.setPropertyValue(
						TextBlockPropertySource.PROPERTY_BLOCK_SEPARATOR,
						dateofExempText.getText());
			} else if (src == manReviewText) {
				securityPropertySource.setPropertyValue(
						TextBlockPropertySource.PROPERTY_DECIMAL_SEPARATOR,
						manReviewText.getText());
			} 
		}
	};

	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite = getWidgetFactory()
				.createFlatFormComposite(parent);

		FormData data;

		headerlabelLabel = getWidgetFactory().createCLabel(composite,
				headerLabel); //$NON-NLS-1$
		headerlabelLabel.setFont(fontBold);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);

		data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
		headerlabelLabel.setLayoutData(data);
		
		typeofExempText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(headerlabelLabel);
		typeofExempText.setLayoutData(data);
		typeofExempText.addModifyListener(listener);

		typeofExemplabelLabel = getWidgetFactory().createCLabel(composite,
				typeofExempLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(typeofExempText);
		data.top = new FormAttachment(headerlabelLabel);
		typeofExemplabelLabel.setLayoutData(data);
		
		dateofExempText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(typeofExempText);
		dateofExempText.setLayoutData(data);
		dateofExempText.addModifyListener(listener);

		dateofExemplabelLabel = getWidgetFactory().createCLabel(composite,
				dateofExempLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(dateofExempText);
		data.top = new FormAttachment(typeofExempText);
		dateofExemplabelLabel.setLayoutData(data);
		
		manReviewText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(dateofExempText);
		manReviewText.setLayoutData(data);
		manReviewText.addModifyListener(listener);

		manReviewlabelLabel = getWidgetFactory().createCLabel(composite,
				manReviewLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(manReviewText);
		data.top = new FormAttachment(dateofExempText);
		manReviewlabelLabel.setLayoutData(data);

	}

	public void refresh() {
		
		
		typeofExempText.removeModifyListener(listener);
		if (TextBlockPropertySource.PROPERTY_TOKEN_SEPARATOR != null) {
			String valueValue = (String) securityPropertySource
					.getPropertyValue(TextBlockPropertySource.PROPERTY_TOKEN_SEPARATOR);
			if (valueValue != null)
				typeofExempText.setText(valueValue);
		}
		typeofExempText.addModifyListener(listener);
		
		dateofExempText.removeModifyListener(listener);
		if (TextBlockPropertySource.PROPERTY_BLOCK_SEPARATOR != null) {
			String valueValue = (String) securityPropertySource
					.getPropertyValue(TextBlockPropertySource.PROPERTY_BLOCK_SEPARATOR);
			if (valueValue != null)
				dateofExempText.setText(valueValue);
		}
		dateofExempText.addModifyListener(listener);
		
		manReviewText.removeModifyListener(listener);
		if (TextBlockPropertySource.PROPERTY_DECIMAL_SEPARATOR != null) {
			String valueValue = (String) securityPropertySource
					.getPropertyValue(TextBlockPropertySource.PROPERTY_DECIMAL_SEPARATOR);
			if (valueValue != null)
				manReviewText.setText(valueValue);
		}
		manReviewText.addModifyListener(listener);



	}

}
