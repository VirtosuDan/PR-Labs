package com.company;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import java.io.*;
import java.util.*;

public class DataConvertor {

   String convertYamlToJson(String yaml) throws IOException {
       ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
       Object obj = yamlReader.readValue(yaml, Object.class);
       ObjectMapper jsonWriter = new ObjectMapper();
       return jsonWriter.writeValueAsString(obj);
   }

    String convertXMLtoJSON(String xml) throws JSONException{
        int INDENTATION = 4;
            JSONObject jsonObj = XML.toJSONObject(xml);
            return jsonObj.toString(INDENTATION);
    }

    String convertCSVtoJSON(String csv) throws IOException{
            CsvSchema csvSchema = CsvSchema.builder().setUseHeader(true).build();
            CsvMapper csvMapper = new CsvMapper();
            List<Object> readAll;
            readAll = csvMapper.readerFor(Map.class).with(csvSchema).readValues(csv).readAll();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(readAll);
    }


}
