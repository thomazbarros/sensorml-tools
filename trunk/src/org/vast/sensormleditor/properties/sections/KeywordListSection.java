package org.vast.sensormleditor.properties.sections;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.outlineview.SensorMLContentOutlinePage;
import org.vast.sensormleditor.properties.descriptors.KeywordListPropertySource;
import org.vast.sensormleditor.util.DOMHelperAddOn;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;

public class KeywordListSection extends AbstractPropertySection {

	private String headerLabel = "Keyword List";
	private CLabel headerlabelLabel;
	
	private Text newKeyWordText;
	private String newKeyWordLabel = "New Keyword:";
	private CLabel newKeyWordlabelLabel;
	private Button addKeyWordButton;

	private List keywordList;
	private String keywordLabel = "Keywords:";
	private CLabel keywordlabelLabel;
	private Button removeKeyWordButton;
	private SMLTreeEditor smlEditor;
	private Element element;
	
	private DOMHelper dom;
	private TreeViewer treeViewer;
	private IPropertyDescriptor[] desc;
	private KeywordListPropertySource propertySource;
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
			SMLTreeEditor smlEditor = (SMLTreeEditor) ((SensorMLContentOutlinePage) part).smlEditor;
			dom = smlEditor.getModel();
			treeViewer = smlEditor.getTreeViewer();
		}
		
	
		Element retNode = null;
		DOMHelperAddOn domUtil = new DOMHelperAddOn(dom);
		Element chosenOne = domUtil.getChosenField(element, retNode);
		Element xngItem = (Element)element.getParentNode();
		if (chosenOne != null)
			element = chosenOne;
		propertySource = new KeywordListPropertySource(element, dom, treeViewer, smlEditor,xngItem);
	}
	
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

		newKeyWordText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(headerlabelLabel);
		newKeyWordText.setLayoutData(data);

		newKeyWordlabelLabel = getWidgetFactory().createCLabel(composite,
				newKeyWordLabel); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(newKeyWordText,
				ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(headerlabelLabel);
		newKeyWordlabelLabel.setLayoutData(data);

		addKeyWordButton = getWidgetFactory().createButton(composite,
				"Add Keyword", SWT.PUSH);
		data = new FormData();
		data.left = new FormAttachment(newKeyWordText,
				ITabbedPropertyConstants.HSPACE);
		data.right = new FormAttachment(75);
		data.top = new FormAttachment(headerlabelLabel);
		addKeyWordButton.setLayoutData(data);
		addKeyWordButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String newkeyword = newKeyWordText.getText();
				if (newkeyword != null && newkeyword != "") {
					keywordList.add(newkeyword);
					propertySource.setPropertyValue(
							KeywordListPropertySource.PROPERTY_KEYWORD,
							newKeyWordText.getText());
					newKeyWordText.setText("");
					newKeyWordText.setFocus();
				}
			}
		});

		keywordList = getWidgetFactory().createList(composite,
				SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		data = new FormData();
		keywordList.setBackground(new Color(null, 224, 255, 255));
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(newKeyWordText,
				ITabbedPropertyConstants.VSPACE);
		keywordList.setLayoutData(data);

		keywordlabelLabel = getWidgetFactory().createCLabel(composite,
				keywordLabel); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(keywordList,
				ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(newKeyWordText,
				ITabbedPropertyConstants.VSPACE);
		keywordlabelLabel.setLayoutData(data);

		removeKeyWordButton = getWidgetFactory().createButton(composite,
				"Remove Keyword", SWT.PUSH);
		data = new FormData();
		data.left = new FormAttachment(keywordList,
				ITabbedPropertyConstants.HSPACE);
		data.right = new FormAttachment(75);
		data.top = new FormAttachment(newKeyWordText,
				ITabbedPropertyConstants.VSPACE);
		removeKeyWordButton.setLayoutData(data);
		removeKeyWordButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int selectionIndex = keywordList.getSelectionIndex();
				desc = propertySource.getPropertyDescriptors();
				for (int i = 0; i < desc.length; i++) {
					String item = (String) propertySource
							.getPropertyValue(desc[i].getId());
					if (item.equals(keywordList.getItem(selectionIndex))) {
						propertySource.setPropertyValue(desc[i].getId(), "");
					}
				}
				keywordList.remove(selectionIndex);
			}
		});


	}
	
/*	public Element getChosenField(Element element, Element returnEle) {

		NodeList children = dom.getChildElements(element);
		for (int i = 0; i < children.getLength(); i++) {
			Element oneEle = (Element) children.item(i);
			if (!dom.hasQName(oneEle, "rng:attribute")
					&& !dom.hasQName(oneEle, "a:documentation")
					&& !dom.hasQName(oneEle, "rng:ref")
					&& !dom.hasQName(oneEle, "rng:optional")) {

				if (dom.hasQName(oneEle, "rng:choice")
						|| dom.hasQName(oneEle, "rng:group")) {
					returnEle = getChosenField(oneEle, returnEle);
					if (stop)
						return returnEle;

				} else if (dom.existAttribute(oneEle, "@xng:selected")) {
					stop = true;
					return oneEle;
				}
			}
		}
		return returnEle;
	}*/



	public void refresh() {
		keywordList.removeAll();
		desc = propertySource.getPropertyDescriptors();
		for (int i = 0; i < desc.length; i++) {
			String item = (String) propertySource.getPropertyValue(desc[i]
					.getId());
			if ((item != null) && !item.equals(""))
				keywordList.add(item);
		}
		String newValue = (String) propertySource
				.getPropertyValue(KeywordListPropertySource.PROPERTY_KEYWORD);
		if (newValue != null)
			newKeyWordText.setText(newValue);
	}
}