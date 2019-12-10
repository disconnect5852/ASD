package asd.wasd.qd;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Tibizator {

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("nincs param!");
			return;
		}
		Path path = Paths.get(args[0]);
		System.out.println("peth: " + path);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Set<Path> filez = new HashSet<Path>();
		try (Stream<Path> walk = Files.walk(path)) {
			// xml kilistázás, rekurzívan, végtelen mélységig
			filez = walk.filter(f -> f.toString().endsWith("xml") && !f.toString().contains("target"))
					.collect(Collectors.toSet());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {

			for (Path entry : filez) {
				try {
					int counter = 1;
					InputStream lofajl= Files.newInputStream(entry, StandardOpenOption.READ);
					Document input = factory.newDocumentBuilder()
							.parse(lofajl);
					XPath xpath = XPathFactory.newInstance().newXPath();
					// sequence név kiszedése
					String secu = xpath.evaluate("string(//sequence/@name)", input);
					if (secu == null || secu.isEmpty()) {
						// nem sequence xml szóval kihagyjuk
						continue;
					}
					System.out.println(secu);
					NodeList nodez = (NodeList) xpath.evaluate("//property[@name='OPENAPI_STEP_ID' and @expression]", input,
							XPathConstants.NODESET);
					if (nodez.getLength()==0) continue;
					for (int i = 0; i < nodez.getLength(); i++) {
						Element node = (Element) nodez.item(i);
						Attr expression = node.getAttributeNode("expression");
						// itt kell valahogy valamit kitalálni, hogy mit, mire cseréljen, és számozzon
						String[] sajt = expression.getTextContent().split("_");
						if (sajt.length > 3) {
							expression.setTextContent(sajt[0] + "_" + sajt[sajt.length - 3] + "_" + secu + "_STEP" + counter+"')");
						} else {
							expression.setTextContent(sajt[0] + "_" + secu + "_STEP" + counter+"')");
						}
						counter++;
						// expression.setTextContent(sajt[0]+"_"+secu+"_"+sajt[sajt.length-1]);
					}
					TransformerFactory fucktory = TransformerFactory.newInstance();
					fucktory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
					Transformer xformer = fucktory.newTransformer();
					xformer.setOutputProperty(OutputKeys.INDENT, "yes");
					Writer output = new StringWriter();
					xformer.transform(new DOMSource(input), new StreamResult(output));
					lofajl.close();
					BufferedWriter writer = Files.newBufferedWriter(entry, Charset.forName("UTF-8"));
					writer.write(output.toString());
					writer.flush();
					writer.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//System.out.println(output.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
