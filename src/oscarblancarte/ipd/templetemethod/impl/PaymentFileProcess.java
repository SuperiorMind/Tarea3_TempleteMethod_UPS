package oscarblancarte.ipd.templetemethod.impl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileOutputStream;

public class PaymentFileProcess extends AbstractFileProcessTemplete {

    private String log = "";

    public PaymentFileProcess(File file, String logPath, String movePath) {
        super(file, logPath, movePath);
    }

    @Override
    protected void validateName() throws Exception {
        String fileName = file.getName();
        if (!fileName.endsWith(".xml")) {
            throw new Exception("Invalid file name" + ", must end with .xml");
        }
        if (fileName.length() != 12) {
            throw new Exception("Invalid document format");
        }
    }

    @Override
    protected void processFile() throws Exception {
        try{
            File Archivo = new File(file.getPath());
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(Archivo);
            document.getDocumentElement().normalize();
            NodeList nList = document.getElementsByTagName("payments");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nNode;
                    String Nombre = element.getElementsByTagName("bank").item(0).getTextContent();
                    String Tarjeta = element.getElementsByTagName("card").item(0).getTextContent();
                    String Monto = element.getElementsByTagName("amount").item(0).getTextContent();
                    String Cliente = element.getElementsByTagName("client").item(0).getTextContent();
                    String Estado = element.getElementsByTagName("state").item(0).getTextContent();
                    if(Nombre.equals("")){
                        log += "";
                    }
                    else{
                        log += Nombre.trim().toLowerCase();
                    }
                    if(Tarjeta.equals("Credit")){
                        log += "00100";
                    }
                    else{
                        log += "00200";
                    }
                    log += Monto.replace(".", "");
                    log += Cliente.trim().toLowerCase();
                    if(Estado.equals("C")){
                        log += "00100";
                    }
                    else{
                        log += "00200";
                    }
                }
            }
        }catch(Exception ex){
        }
    }

    @Override
    protected void createLog() throws Exception {
        FileOutputStream out = null;
        try {
            File outFile = new File(logPath + "/" + file.getName());
            if (!outFile.exists()) {
                outFile.createNewFile();
            }
            out = new FileOutputStream(outFile, false);
            out.write(log.getBytes());
            out.flush();
        }catch(Exception ex){
        }finally {
            out.close();
        }
    }
}
