package org.vast.sensormleditor.properties.sections;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.vast.sensormleditor.properties.descriptors.BinaryBlockPropertySource;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;

public class BinaryBlockSection extends AbstractPropertySection {

	private String headerLabel = "Custom Binary Encoding";
	private CLabel headerlabelLabel;

	private CCombo encodingChoice;
	private String encodingLabel = "Byte Encoding:";
	private CLabel encodinglabelLabel;
	
	private CCombo orderChoice;
	private String orderLabel = "Byte Order:";
	private CLabel orderlabelLabel;
	
	private CLabel byteLengthlabelLabel;
	private String byteLengthLabel = "Byte Length:";
	private Text byteLengthText;

	private SMLTreeEditor smlEditor;
	private Element element;
	private DOMHelper dom;
	private TreeViewer treeViewer;
	private BinaryBlockPropertySource binaryBlock;
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
			binaryBlock = new BinaryBlockPropertySource(
					element, dom, treeViewer, smlEditor);
		}
	}

	private ModifyListener listener = new ModifyListener() {
		public void modifyText(ModifyEvent arg0) {
			Object src = arg0.getSource();
			if (src == byteLengthText) {
				binaryBlock.setPropertyValue(
						BinaryBlockPropertySource.PROPERTY_BYTE_LENGTH,
						byteLengthText.getText());
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
		
		encodingChoice = getWidgetFactory().createCCombo(composite,
				SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(headerlabelLabel);
		encodingChoice.setLayoutData(data);
		encodingChoice.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int index = encodingChoice.getSelectionIndex();
				binaryBlock
						.setPropertyValue(
								BinaryBlockPropertySource.PROPERTY_ENCODING_CHOICE,
								encodingChoice.getItem(index));

			}
		});

		encodinglabelLabel = getWidgetFactory()
				.createCLabel(composite, encodingLabel); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(encodingChoice,
				ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(headerlabelLabel);
		encodinglabelLabel.setLayoutData(data);
		
		orderChoice = getWidgetFactory().createCCombo(composite,
				SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(encodingChoice);
		orderChoice.setLayoutData(data);
		orderChoice.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int index = orderChoice.getSelectionIndex();
				binaryBlock
						.setPropertyValue(
								BinaryBlockPropertySource.PROPERTY_ORDER_CHOICE,
								orderChoice.getItem(index));

			}
		});

		orderlabelLabel = getWidgetFactory()
				.createCLabel(composite, orderLabel); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(orderChoice,
				ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(encodinglabelLabel);
		orderlabelLabel.setLayoutData(data);
		
		byteLengthText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(orderChoice);
		byteLengthText.setLayoutData(data);
		byteLengthText.addModifyListener(listener);

		byteLengthlabelLabel = getWidgetFactory().createCLabel(composite,
				byteLengthLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(byteLengthText);
		data.top = new FormAttachment(orderChoice);
		byteLengthlabelLabel.setLayoutData(data);

	}

	public void refresh() {
		encodingChoice.removeAll();
		if (BinaryBlockPropertySource.PROPERTY_ENCODING_CHOICE != null) {
			String[] choices = (String[]) binaryBlock
					.getEncodingChoices(BinaryBlockPropertySource.PROPERTY_ENCODING_CHOICE
							);
			for (int i = 0; i < choices.length; i++) {
				if (choices[i] != null)
					encodingChoice.add(choices[i]);
			}
			String selected = (String) binaryBlock
					.getPropertyValue(BinaryBlockPropertySource.PROPERTY_ENCODING_CHOICE);
			int index = encodingChoice.indexOf(selected);
			encodingChoice.select(index);
		}
		
		orderChoice.removeAll();
		if (BinaryBlockPropertySource.PROPERTY_ORDER_CHOICE != null) {
			String[] choices = (String[]) binaryBlock
					.getOrderChoices(BinaryBlockPropertySource.PROPERTY_ORDER_CHOICE);
			for (int i = 0; i < choices.length; i++) {
				if (choices[i] != null)
					orderChoice.add(choices[i]);
			}
			String selected = (String) binaryBlock
					.getPropertyValue(BinaryBlockPropertySource.PROPERTY_ORDER_CHOICE);
			int index = orderChoice.indexOf(selected);
			orderChoice.select(index);
		}
		
		
		byteLengthText.removeModifyListener(listener);
		if (BinaryBlockPropertySource.PROPERTY_BYTE_LENGTH != null) {
			String valueValue = (String) binaryBlock
					.getPropertyValue(BinaryBlockPropertySource.PROPERTY_BYTE_LENGTH);
			if (valueValue != null)
				byteLengthText.setText(valueValue);
		}
		byteLengthText.addModifyListener(listener);
	}
}