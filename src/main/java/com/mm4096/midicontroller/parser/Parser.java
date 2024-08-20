package com.mm4096.midicontroller.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.mm4096.midicontroller.classes.ConfigClass;
import com.mm4096.midicontroller.classes.PatchClass;
import com.mm4096.midicontroller.classes.PatchListClass;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.json.JSONException;
import org.json.JSONObject;

public class Parser {
    private String data;
    private final ObjectMapper mapper;

    public Parser(String data) {
        this.data = data;
        this.mapper = new ObjectMapper();
    }

    public Parser() {
        this.mapper = new ObjectMapper();
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public ConfigClass[] parseAsConfig() {
        try {
            JsonNode rootNode = mapper.readTree(data);
            List<ConfigClass> configList = new ArrayList<>();
            Iterator<String> fieldNames = rootNode.fieldNames();

            if (rootNode.isObject()) {
                while (fieldNames.hasNext()) {
                    String fieldName = fieldNames.next();
                    String value = String.valueOf(rootNode.get(fieldName));
                    configList.add(new ConfigClass(fieldName, value));
                }
            }
            else if (rootNode.isArray()) {
                for (JsonNode node : rootNode) {
                    ConfigClass config = mapper.treeToValue(node, ConfigClass.class);
                    configList.add(config);
                }
            }

            return configList.toArray(new ConfigClass[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ConfigClass[0];
    }

    public PatchClass[] parseAsPatch() throws ParseException, JSONException {
        // parse an array of json objects
        try {
            JsonNode rootNode = mapper.readTree(data);
            List<PatchClass> patchList = new ArrayList<>();

            if (rootNode.isArray()) {
                for (JsonNode node : rootNode) {
                    PatchClass patch = mapper.treeToValue(node, PatchClass.class);
                    patchList.add(patch);
                }
            }

            return patchList.toArray(new PatchClass[0]);
        } catch (JsonProcessingException e) {
            throw new ParseException("Error parsing patch: JSON Parse Error", 0);
        }
    }

    public PatchListClass parseAsPatchFile() throws ParseException, JSONException {
        String[] lines = data.split("\n");
        ArrayList<String> filteredLines = new ArrayList<>();

        for (String line : lines) {
            if (line.startsWith("#") || line.isEmpty()) {
                continue;
            }
            else {
                filteredLines.add(line);
            }
        }

        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> configLines = new ArrayList<>();
        ArrayList<String> patchLines = new ArrayList<>();

        boolean foundConfigBlock = false;
        boolean foundPatchBlock = false;
        for (String line : filteredLines) {
            if (Objects.equals(line, "startconfig")) {
                if (foundConfigBlock) {
                    throw new ParseException("Found a config block starting before a previous config block finished!", 0);
                }
                if (foundPatchBlock) {
                    throw new ParseException("Found a config block in the patch block!", 0);
                }
                foundConfigBlock = true;

            } else if (Objects.equals(line, "endconfig")) {
                if (!foundConfigBlock) {
                    throw new ParseException("Found a config block ending before a config block started!", 0);
                }
                foundConfigBlock = false;

            } else if (Objects.equals(line, "startlist")) {

                if (foundPatchBlock) {
                    throw new ParseException("Found a patch block starting before a previous patch block finished!", 0);
                }

                if (foundConfigBlock) {
                    throw new ParseException("Found a patch block in the config block!", 0);
                }

                foundPatchBlock = true;

            } else if (Objects.equals(line, "endlist")) {

                if (!foundPatchBlock) {
                    throw new ParseException("Found a patch block ending before a patch block started!", 0);
                }
                foundPatchBlock = false;

            } else if (foundConfigBlock) {
                configLines.add(line);
            } else if (foundPatchBlock) {
                patchLines.add(line);
            }
            else {
                names.add(line);
            }
        }

        String patchName = !names.isEmpty() ? names.get(0) : "";
        String configData = String.join("\n", configLines);
        String patchData = String.join("\n", patchLines);

        JSONObject patchListJson = new JSONObject(patchData);
        String patchList = patchListJson.getJSONArray("list").toString();
        PatchClass[] patches = new Parser(patchList).parseAsPatch();
        ConfigClass[] configs = new Parser(configData).parseAsConfig();

        return new PatchListClass(patchName, patches, configs);
    }
}