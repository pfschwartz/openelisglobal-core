package oe.analyzerfilebroker.config;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;

/**
 * Created by paulsc on 9/27/2015.
 */
public class XMLParser {
    public boolean parse(GeneralConfig generalConfig){
        File file = generalConfig.getFile();

        if( !file.canRead()){
            file.setReadable(true);
        }

        SAXReader xmlReader = new SAXReader();
        Document configDoc;
        try {
            configDoc = xmlReader.read(file);
            Element root = configDoc.getRootElement();
            return new GeneralParser().parse(generalConfig, root);
        } catch (DocumentException e) {
            e.printStackTrace();
            return false;
        }
    }
}
