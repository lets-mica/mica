/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.dreamlu.mica.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.io.StringReader;

/**
 * xpath解析xml
 *
 * <pre>
 *     文档地址：
 *     http://www.w3school.com.cn/xpath/index.asp
 * </pre>
 *
 * @author L.cm
 */
public class XmlHelper {
	private final XPath path;
	private final Document doc;

	private XmlHelper(InputSource inputSource, boolean unsafe) throws Exception {
		DocumentBuilderFactory dbf = unsafe ?
			XmlHelper.getUnsafeDocumentBuilderFactory() :
			XmlHelper.getDocumentBuilderFactory();
		DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
		doc = documentBuilder.parse(inputSource);
		path = XmlHelper.getXpathFactory().newXPath();
	}

	private static XmlHelper createSafe(InputSource inputSource) {
		return create(inputSource, false);
	}

	private static XmlHelper createUnsafe(InputSource inputSource) {
		return create(inputSource, true);
	}

	private static XmlHelper create(InputSource inputSource, boolean unsafe) {
		try {
			return new XmlHelper(inputSource, unsafe);
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static XmlHelper safe(InputStream is) {
		InputSource inputSource = new InputSource(is);
		return createSafe(inputSource);
	}

	public static XmlHelper safe(String xmlStr) {
		StringReader sr = new StringReader(xmlStr.trim());
		InputSource inputSource = new InputSource(sr);
		XmlHelper xmlHelper = XmlHelper.createSafe(inputSource);
		IoUtil.closeQuietly(sr);
		return xmlHelper;
	}

	public static XmlHelper unsafe(InputStream is) {
		InputSource inputSource = new InputSource(is);
		return createUnsafe(inputSource);
	}

	public static XmlHelper unsafe(String xmlStr) {
		StringReader sr = new StringReader(xmlStr.trim());
		InputSource inputSource = new InputSource(sr);
		XmlHelper xmlHelper = XmlHelper.createUnsafe(inputSource);
		IoUtil.closeQuietly(sr);
		return xmlHelper;
	}

	/**
	 * 执行 xpath 语法
	 *
	 * @param expression xpath 语法
	 * @param item       子节点
	 * @param returnType 返回的类型
	 * @return {Object}
	 */
	public Object evalXPath(String expression, @Nullable Object item, QName returnType) {
		item = null == item ? doc : item;
		try {
			return path.evaluate(expression, item, returnType);
		} catch (XPathExpressionException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 获取String
	 *
	 * @param expression 路径
	 * @return String
	 */
	public String getString(String expression) {
		return (String) evalXPath(expression, null, XPathConstants.STRING);
	}

	/**
	 * 获取Boolean
	 *
	 * @param expression 路径
	 * @return String
	 */
	public Boolean getBoolean(String expression) {
		return (Boolean) evalXPath(expression, null, XPathConstants.BOOLEAN);
	}

	/**
	 * 获取Number
	 *
	 * @param expression 路径
	 * @return {Number}
	 */
	public Number getNumber(String expression) {
		return (Number) evalXPath(expression, null, XPathConstants.NUMBER);
	}

	/**
	 * 获取某个节点
	 *
	 * @param expression 路径
	 * @return {Node}
	 */
	public Node getNode(String expression) {
		return (Node) evalXPath(expression, null, XPathConstants.NODE);
	}

	/**
	 * 获取子节点
	 *
	 * @param expression 路径
	 * @return NodeList
	 */
	public NodeList getNodeList(String expression) {
		return (NodeList) evalXPath(expression, null, XPathConstants.NODESET);
	}

	/**
	 * 获取String
	 *
	 * @param node       节点
	 * @param expression 相对于node的路径
	 * @return String
	 */
	public String getString(Object node, String expression) {
		return (String) evalXPath(expression, node, XPathConstants.STRING);
	}

	/**
	 * 获取
	 *
	 * @param node       节点
	 * @param expression 相对于node的路径
	 * @return String
	 */
	public Boolean getBoolean(Object node, String expression) {
		return (Boolean) evalXPath(expression, node, XPathConstants.BOOLEAN);
	}

	/**
	 * 获取
	 *
	 * @param node       节点
	 * @param expression 相对于node的路径
	 * @return {Number}
	 */
	public Number getNumber(Object node, String expression) {
		return (Number) evalXPath(expression, node, XPathConstants.NUMBER);
	}

	/**
	 * 获取某个节点
	 *
	 * @param node       节点
	 * @param expression 路径
	 * @return {Node}
	 */
	public Node getNode(Object node, String expression) {
		return (Node) evalXPath(expression, node, XPathConstants.NODE);
	}

	/**
	 * 获取子节点
	 *
	 * @param node       节点
	 * @param expression 相对于node的路径
	 * @return NodeList
	 */
	public NodeList getNodeList(Object node, String expression) {
		return (NodeList) evalXPath(expression, node, XPathConstants.NODESET);
	}

	private static DocumentBuilderFactory getDocumentBuilderFactory() {
		return XmlHelper.XmlHelperHolder.DOCUMENT_BUILDER_FACTORY;
	}

	/**
	 * 不安全的 Document 构造器，用来解析部分可靠的 html、xml
	 *
	 * @return DocumentBuilderFactory
	 */
	private static DocumentBuilderFactory getUnsafeDocumentBuilderFactory() {
		return XmlHelper.XmlHelperHolder.UNSAFE_DOCUMENT_BUILDER_FACTORY;
	}

	private static XPathFactory getXpathFactory() {
		return XmlHelper.XmlHelperHolder.XPATH_FACTORY;
	}

	/**
	 * 内部类单例
	 */
	@Slf4j
	private static class XmlHelperHolder {
		private static final String FEATURE_HTTP_XML_ORG_SAX_FEATURES_EXTERNAL_GENERAL_ENTITIES = "http://xml.org/sax/features/external-general-entities";
		private static final String FEATURE_HTTP_XML_ORG_SAX_FEATURES_EXTERNAL_PARAMETER_ENTITIES = "http://xml.org/sax/features/external-parameter-entities";
		private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = XmlHelperHolder.newDocumentBuilderFactory();
		private static final DocumentBuilderFactory UNSAFE_DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();
		private static final XPathFactory XPATH_FACTORY = XPathFactory.newInstance();

		private static DocumentBuilderFactory newDocumentBuilderFactory() {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			// Set parser features to prevent against XXE etc.
			// Note: setting only the external entity features on DocumentBuilderFactory instance
			// (ala how safeTransform does it for SAXTransformerFactory) does seem to work (was still
			// processing the entities - tried Oracle JDK 7 and 8 on OSX). Setting seems a bit extreme,
			// but looks like there's no other choice.
			documentBuilderFactory.setXIncludeAware(false);
			documentBuilderFactory.setExpandEntityReferences(false);
			setDocumentBuilderFactoryFeature(documentBuilderFactory, XMLConstants.FEATURE_SECURE_PROCESSING, true);
			setDocumentBuilderFactoryFeature(documentBuilderFactory, FEATURE_HTTP_XML_ORG_SAX_FEATURES_EXTERNAL_GENERAL_ENTITIES, false);
			setDocumentBuilderFactoryFeature(documentBuilderFactory, FEATURE_HTTP_XML_ORG_SAX_FEATURES_EXTERNAL_PARAMETER_ENTITIES, false);
			setDocumentBuilderFactoryFeature(documentBuilderFactory, "http://apache.org/xml/features/disallow-doctype-decl", true);
			setDocumentBuilderFactoryFeature(documentBuilderFactory, "http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			return documentBuilderFactory;
		}

		private static void setDocumentBuilderFactoryFeature(DocumentBuilderFactory documentBuilderFactory, String feature, boolean state) {
			try {
				documentBuilderFactory.setFeature(feature, state);
			} catch (Exception e) {
				log.warn(String.format("Failed to set the XML Document Builder factory feature %s to %s", feature, state), e);
			}
		}
	}

}
