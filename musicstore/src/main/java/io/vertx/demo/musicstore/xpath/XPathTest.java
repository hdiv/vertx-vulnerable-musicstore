/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package io.vertx.demo.musicstore.xpath;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author breakthesec
 */

public class XPathTest {

	protected void doAttack(final String user, final String pass) throws ServletException, IOException {

		try {
			// Parsing XML:
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document xDoc = builder.parse(getResourceAsStream("users.xml"));

			XPath xPath = XPathFactory.newInstance().newXPath();

			// XPath Query:
			String xPression = "/users/user[username='" + user + "' and password='" + pass + "']/name";

			// running Xpath query:
			String name = xPath.compile(xPression).evaluate(xDoc);
			System.out.println("hacked xpath" + name);

		}
		catch (ParserConfigurationException | XPathExpressionException | SAXException e) {
			e.printStackTrace();
		}

	}

	public InputStream getResourceAsStream(String name) {
		name = resolveName(name);
		ClassLoader cl = this.getClass().getClassLoader();
		if (cl == null) {
			// A system class.
			return ClassLoader.getSystemResourceAsStream(name);
		}
		return cl.getResourceAsStream(name);
	}

	/**
	 * Add a package name prefix if the name is not absolute Remove leading "/" if name is absolute
	 */
	private String resolveName(String name) {
		if (name == null) {
			return name;
		}
		if (!name.startsWith("/")) {
			Class c = this.getClass();
			while (c.isArray()) {
				c = c.getComponentType();
			}
			String baseName = c.getName();
			int index = baseName.lastIndexOf('.');
			if (index != -1) {
				name = baseName.substring(0, index).replace('.', '/') + "/" + name;
			}
		}
		else {
			name = name.substring(1);
		}
		return name;
	}

}
