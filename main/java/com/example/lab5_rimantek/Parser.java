package com.example.lab5_rimantek;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class Parser {
    public static List<CurrencyRate> parse(InputStream input) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(false);
        SAXParser saxParser = factory.newSAXParser();


        RatesHandler handler = new RatesHandler();
        saxParser.parse(input, handler);
        return handler.items;
    }

    private static class RatesHandler extends DefaultHandler {
        private final List<CurrencyRate> items = new ArrayList<>();
        private final StringBuilder text = new StringBuilder();


        private String currentCode;
        private String currentName;
        private Double currentRate;
        private Date currentPubDate;
        private boolean insideItem = false;
        private final SimpleDateFormat rfc822 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);


        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            text.setLength(0);
            if ("item".equalsIgnoreCase(qName)) {
                insideItem = true;
                currentCode = null;
                currentName = null;
                currentRate = null;
                currentPubDate = null;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            text.append(ch, start, length);
        }


        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            String value = text.toString().trim();
            if (!insideItem) return;


            switch (qName) {
                case "targetCurrency":
                    currentCode = value; // e.g. EUR
                    break;
                case "targetName":
                    currentName = value; // e.g. Euro
                    break;
                case "exchangeRate":
                    try {
                        currentRate = Double.parseDouble(value);
                    } catch (NumberFormatException ignored) { /* skip bad values */ }
                    break;
                case "pubDate":
                    try {
                        currentPubDate = rfc822.parse(value);
                    } catch (ParseException ignored) { /* optional */ }
                    break;
                case "item":
                    if (currentCode != null && currentRate != null) {
                        items.add(new CurrencyRate(
                                currentCode,
                                currentName != null ? currentName : currentCode,
                                currentRate,
                                currentPubDate
                        ));
                    }
                    insideItem = false;
                    break;
            }
        }
    }
}