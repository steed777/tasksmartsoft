package com.github.steed777.tasksmartsoft.controller;

import com.github.steed777.tasksmartsoft.model.Valute;
import com.github.steed777.tasksmartsoft.repository.ValuteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Controller
    public class MainController {

    private ValuteRepository valuteRepository;

    @Autowired
    public MainController(ValuteRepository valuteRepository) {
        this.valuteRepository = valuteRepository;
    }

    @GetMapping("/")
    public String greeting(Model model) {
        model.addAttribute("name", "132");
        return "greeting";
    }
    @GetMapping("/main")
    public String main(Model model) {

        List<Valute> valuteValues = new ArrayList<>();
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputStream stream = new URL("http://www.cbr.ru/scripts/XML_daily.asp").openStream();
            Document document = builder.parse(stream);
            Element valCurs = document.getDocumentElement();
            String date = valCurs.getAttribute("Date");
            NodeList valuteList = valCurs.getChildNodes();

            for (int i = 0; i < valuteList.getLength(); i++) {
                Node valute = valuteList.item(i);
                if (valute instanceof Element) {
                    Element valuteElement = (Element) valute;
                    Valute valuteObject = new Valute();
                    NodeList valuteElementList = valuteElement.getChildNodes();
                    for (int j = 0; j < valuteElementList.getLength(); j++) {
                        Node valuteProperty = valuteElementList.item(j);
                        if (valuteProperty instanceof Element) {
                            Element property = (Element) valuteProperty;
                            Text textNode = (Text) property.getFirstChild();
                            String text = textNode.getData().trim();
                            if (property.getTagName().equals("num_code"))
                                valuteObject.setNum_code(text);
                            else if (property.getTagName().equals("CharCode"))
                                valuteObject.setChar_code(text);
                            else if (property.getTagName().equals("Nominal"))
                                valuteObject.setNominal(text);
                            else if (property.getTagName().equals("Name"))
                                valuteObject.setName(text);
                            else if (property.getTagName().equals("Value"))
                                valuteObject.setValue(text);
                        }
                    }
                    valuteValues.add(valuteObject);
                }
            }
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/smartsoft?user=postgres&password=alberto&ssl=false");
            PreparedStatement ps = connection.prepareStatement("insert into valute (num_code, char_code, nominal, name," +
                    "value) values (?, ?, ?, ?, ?)");
            for (Valute valuteValue : valuteValues) {
                ps.setString(1, valuteValue.getNum_code());
            ps.setString(2, valuteValue.getChar_code());
            ps.setString(3, valuteValue.getNominal());
            ps.setString(4, valuteValue.getName());
            ps.setString(5, valuteValue.getValue());
                ps.executeUpdate();
            }

            Iterable<Valute> valutes = this.valuteRepository.findByValutes();
            model.addAttribute("valutes", valutes);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "main";
    }
}