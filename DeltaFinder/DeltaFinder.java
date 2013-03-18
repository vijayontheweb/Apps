package snippet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

public class DeltaFinder {

	static Map keyMap = new HashMap();
	//private static KeyDS keyDS = KeyDS.getInstance();

	public static void main(String[] args) {
		keyMap.put("IntRateData","IntRateCategory");
		keyMap.put("RateMatrixTier","Tier");
		composeDelta();
	}

	public static void composeDelta() {
		Document oldAggregate = null, newAggregate = null;
		try {
			SAXBuilder builder = new SAXBuilder();
			oldAggregate = builder.build(new File("old.xml"));
			newAggregate = builder.build(new File("new.xml"));
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ValueObject valueObject = new ValueObject();
		if (oldAggregate != null && oldAggregate.getRootElement() != null
				&& oldAggregate.getRootElement().getChild("AcctInfo") != null) {
			Element element = oldAggregate.getRootElement()
					.getChild("AcctInfo");
			valueObject.setPath("//" + element.getName());
			valueObject = composeValueObject(element, valueObject);
		}
		/*
		if (newAggregate != null && newAggregate.getRootElement() != null
				&& oldAggregate.getRootElement().getChild("AcctInfo") != null) {
			Element element = newAggregate.getRootElement()
					.getChild("AcctInfo");
			valueObject = updateValueObject(element, valueObject);
		}
		*/
	}

	/*
	private static ValueObject updateValueObject(Element elt, ValueObject vo){
		int size = elt.getChildren().size();
		Element element = null;
		KeyBean keyBean = null;
		for (int i = 0; i < size; i++) {
			element = (Element) elt.getChildren().get(i);
			List voList = vo.getValueObjects();			
			for (int j = 0; j < voList.size(); j++) {
				ValueObject valueObject = (ValueObject)voList.get(j);
				if(isMatching(element,valueObject)))
			}
		}
		return vo;
	}
    */
	private static ValueObject composeValueObject(Element elt, ValueObject vo) {
		int size = elt.getChildren().size();
		Element element = null;
		//KeyBean keyBean = null;
		for (int i = 0; i < size; i++) {
			element = (Element) elt.getChildren().get(i);
			ValueObject valueObject = new ValueObject();
			valueObject.setAction("NOOP");
			valueObject.setValue(element.getText());
			/*
			keyBean = keyDS.getKeyBean(element.getName());			
			if (keyBean != null) {
				List attList = keyBean.getAttributeList();
				String attribPath = null;
				if (attList != null && attList.size() > 0) {
					attribPath = composeAttributePath(attList, element);
				}
				valueObject.setPath(vo.getPath() + "/" + element.getName()
						+ attribPath);
			}*/
			if(keyMap.containsKey(element.getName())){
				String value = (String) keyMap.get(element.getName());
				List attList = new ArrayList();
				attList.add(value);
				String attribPath = null;
				if (attList != null && attList.size() > 0) {
					attribPath = composeAttributePath(attList, element);
				}
				valueObject.setPath(vo.getPath() + "/" + element.getName()
						+ attribPath);
			}
			else {
				valueObject.setPath(vo.getPath() + "/" + element.getName());
			}
			if (element.getChildren().size() > 0) {
				composeValueObject(element, valueObject);
			}
			vo.addValueObject(valueObject);
		}
		return vo;
	}

	private static String composeAttributePath(List attList, Element element) {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for (int j = 0; j < attList.size(); j++) {
			String attribute = (String) attList.get(j);
			int elementSize = element.getChildren().size();
			for (int k = 0; k < elementSize; k++) {
				Element element2 = (Element) element.getChildren().get(k);
				if (element2 != null && element2.getName() != null
						&& element2.getName().equalsIgnoreCase(attribute)) {
					sb.append(element2.getName() + "='" + element2.getText()
							+ "'");
					if (k + 1 < elementSize) {
						sb.append(" and ");
					}
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}

}

class ValueObject {
	String value;
	String path;
	String action;
	List valueObjects;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List getValueObjects() {
		return valueObjects;
	}

	public void setValueObjects(List valueObjects) {
		this.valueObjects = valueObjects;
	}

	public void addValueObject(ValueObject valueObject) {
		valueObjects.add(valueObject);
	}

}
