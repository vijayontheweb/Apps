package com.csc.fss.banking.soa.portal.provider.account;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import com.bowstreet.util.IXml;
import com.bowstreet.util.XmlUtil;

public class DirectMapper {
	private static final String MAPPING_XML_PATH = "/props/InterestRateMapping.xml";
	
	public  IXml fetchPageVariableFromService(IXml webSrvRs){
		IXml xml,pageVar = null;
		try{
			IXml mappingsXml = XmlUtil.parseXml(new InputStreamReader(
					DirectMapper.class
						.getResourceAsStream(MAPPING_XML_PATH)));
			
			List mapList = mappingsXml.findElement("mapping").getChildren();
			Iterator iterator = mapList.iterator();
			pageVar = XmlUtil.create("InterestRateData");
			pageVar.addChildElement("CreditInterestData");
			pageVar.addChildElement("BonusInterestData");
			pageVar.addChildElement("DebitInterestData");
			while(iterator.hasNext()){
				xml = (IXml)iterator.next();
				String pagePath = xml.getAttribute("page");
				String servicePath = xml.getAttribute("service");	
				constructTargetXml(webSrvRs, servicePath, pageVar, pagePath);
			}
		}catch (IOException io) {
		}
		return pageVar;
	}
	
	public  IXml updateServiceAggregate(IXml pageVar){
		IXml xml,webSrvRs = null;
		try{
			IXml mappingsXml = XmlUtil.parseXml(new InputStreamReader(
					DirectMapper.class
						.getResourceAsStream(MAPPING_XML_PATH)));
			
			List mapList = mappingsXml.findElement("mapping").getChildren();
			Iterator iterator = mapList.iterator();			 
			while(iterator.hasNext()){
				xml = (IXml)iterator.next();
				String pagePath = xml.getAttribute("page");
				String servicePath = xml.getAttribute("service");	
				constructTargetXml(pageVar, pagePath, webSrvRs, servicePath);
			}
		}catch (IOException io) {
		}
		return pageVar;
	}
	
	
	
	public  void constructTargetXml(IXml sourceXml, String sourcePath,
			IXml targetXml, String targetPath) {
		IXml sourceElement = sourceXml.findElement(sourcePath);
		IXml targetElement = targetXml.findElement(targetPath);
		if (isNotNullOrEmpty(sourceElement)) {
			if (targetElement == null) {
				targetElement = constructTargetElement(targetXml, targetPath);
			}
			targetElement.setText(sourceElement.getText());
		}else if(isNotNullOrEmpty(targetElement)){
			targetElement.setText(null);
		}		
	}

	private  boolean isNotNullOrEmpty(IXml element){
		return (element != null && element.getText() != null && !element.getText().equals(""));
	}
	
	private  IXml constructTargetElement(IXml targetXml,
			String targetPathStr) {
		String stableTargetPath = null;
		StringBuffer unstableTargetPath = new StringBuffer("/");
		
		String[] targetSubPaths = splitTargetPath(targetPathStr);
		for (int i = 0; i < targetSubPaths.length; i++) {
			String targetSubPathStr = targetSubPaths[i];
			unstableTargetPath.append("/" + targetSubPathStr);
			if (targetXml.findElement(unstableTargetPath.toString()) == null) {
				targetXml.findElement(stableTargetPath).addChildElement(
						buildUnstablePath(targetSubPathStr));	
			}
			stableTargetPath = unstableTargetPath.toString();
		}
		return targetXml.findElement(stableTargetPath);
	}

	private  String[] splitTargetPath(String targetPath) {
		String targetAbsolutePath = targetPath.substring(2);
		return targetAbsolutePath.split("/");
	}

	private  IXml buildUnstablePath(String targetElementStr) {
		IXml targetElement = null;
		int subElementIndex = targetElementStr.indexOf('[');
		if (subElementIndex > -1) {
			targetElement = XmlUtil.create(targetElementStr.substring(0,
					subElementIndex));
			String subElement = targetElementStr.substring(
					subElementIndex + 1, targetElementStr.length() - 1);
			String[] subElements = subElement.split("=");
			String name = subElements[0].trim();
			String value = subElements[1].substring(1,
					subElements[1].length() - 1).trim();

			targetElement.addChildWithText(name, value);
		} else {
			targetElement = XmlUtil.create(targetElementStr);
		}
		return targetElement;

	}
	
	
	
}
