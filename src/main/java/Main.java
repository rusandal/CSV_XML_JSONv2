import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        toFile("new_data(from_csv).json", json);
        List<Employee> myList = parseXML("data.xml");
        String json2 = listToJson(myList);
        toFile("new_data(from_xml).json", json2);
        String json3 = readString("new_data(from_csv).json");
        List<Employee> list1 = jsonToList(json3);
        for (Employee employee : list1) {
            System.out.println(employee);
        }

    }

    private static List<Employee> jsonToList(String json) {
        //JSONParser parser = new JSONParser();
        //List<Employee> list = new ArrayList<>();
        //try {
            //Object obj = parser.parse(json);
            //JSONArray arr = (JSONArray) obj;
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            Type listType = new TypeToken<List<Employee>>() {
            }.getType();
            List<Employee> list = gson.fromJson(json, listType);
            return list;
        //}catch (ParseException e) {
        //    e.printStackTrace();
        //}
        //return null;
    }

    private static String readString(String s) {
        try (BufferedReader br = new BufferedReader(new FileReader(s))) {
            //чтение построчно
            String p;
            while ((p = br.readLine()) != null) {
                return p;
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    private static List<Employee> parseXML(String s) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        List<Employee> list = new ArrayList<>();
        int id = 0;
        String firstName = null;
        String lastName = null;
        String country = null;
        int age = 0;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(s));
            Node node = doc.getDocumentElement();
            NodeList nodeList = node.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node_ = nodeList.item(i);
                if (Node.ELEMENT_NODE == node_.getNodeType()) {
                    NodeList nodeList1 = nodeList.item(i).getChildNodes();
                    for (int j = 0; j < nodeList1.getLength(); j++) {
                        if (nodeList1.item(j).getNodeName() == "id") {
                            id = Integer.parseInt(nodeList1.item(j).getTextContent());
                        } else if (nodeList1.item(j).getNodeName() == "firstName") {
                            firstName = nodeList1.item(j).getTextContent();
                        } else if (nodeList1.item(j).getNodeName() == "lastName") {
                            lastName = nodeList1.item(j).getTextContent();
                        } else if (nodeList1.item(j).getNodeName() == "country") {
                            country = nodeList1.item(j).getTextContent();
                        } else if (nodeList1.item(j).getNodeName() == "age") {
                            age = Integer.parseInt(nodeList1.item(j).getTextContent());
                        }
                    }
                    list.add(new Employee(id, firstName, lastName, country, age));
                }
            }
            return list;
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.getMessage();
        }
        return null;
    }

    private static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String json = gson.toJson(list, listType);

        return json;
    }

    public static List<Employee> parseCSV(String[] columnMapping, String filename) {
        try (CSVReader reader = new CSVReader(new FileReader(filename))) {
            ColumnPositionMappingStrategy<Employee> strategy =
                    new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            List<Employee> list = csv.parse();
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void toFile(String name, String json){
        try (FileWriter file = new
                FileWriter(name)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
