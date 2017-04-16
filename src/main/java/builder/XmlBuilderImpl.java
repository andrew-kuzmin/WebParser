/*
package builder;

import interfaces.XmlBuilder;
import model.Offer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Set;

public class XmlBuilderImpl implements XmlBuilder {
    @Override
    public void buildXml(Set<Offer> offers) {
        try {
            DocumentBuilderFactory dbFactory =  DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            // root element
            Element rootElement = doc.createElement("offers");
            doc.appendChild(rootElement);
            offers.forEach(offer -> {
                Element offer1 = doc.createElement("offer");
                Element name = doc.createElement("name");
                name.appendChild(doc.createTextNode(offer.getName()));
                offer1.appendChild(name);
                Element brand = doc.createElement("brand");
                brand.appendChild(doc.createTextNode(offer.getBrand()));
                Element color = doc.createElement("color");
                color.appendChild(doc.createTextNode(offer.getColor()));
                Element price = doc.createElement("price");
                price.appendChild(doc.createTextNode(offer.getPrice()));
                Element initialPrice = doc.createElement("initialPrice");
                initialPrice.appendChild(doc.createTextNode(offer.getInitialPrice()));
                Element description = doc.createElement("description");
                description.appendChild(doc.createTextNode(offer.getDescription()));
                Element articleId = doc.createElement("articleId");
                articleId.appendChild(doc.createTextNode(offer.getArticleId()));
                Element shippingCosts = doc.createElement("shippingCosts");
                shippingCosts.appendChild(doc.createTextNode(offer.getShippingCosts()));

                offer1.appendChild(brand);
                offer1.appendChild(color);
                offer1.appendChild(price);
                offer1.appendChild(initialPrice);
                offer1.appendChild(description);
                offer1.appendChild(articleId);
                offer1.appendChild(shippingCosts);
                rootElement.appendChild(offer1);
            });
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            File file = new File("offers.xml");
            StreamResult result = new StreamResult(file);

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);

            System.out.println("File saved!");

        } catch (Exception e) {

        }
    }
}
*/
