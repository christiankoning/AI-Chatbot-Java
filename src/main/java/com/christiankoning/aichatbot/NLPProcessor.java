package com.christiankoning.aichatbot;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class NLPProcessor {
    private Tokenizer tokenizer;
    private POSTaggerME posTagger;

    public NLPProcessor() {
        try {
            // Load Tokenizer Model
            InputStream tokenModelStream = new FileInputStream("models/opennlp-en-ud-ewt-tokens-1.2-2.5.0.bin");
            TokenizerModel tokenModel = new TokenizerModel(tokenModelStream);
            tokenizer = new TokenizerME(tokenModel);
            tokenModelStream.close();

            // Load POS Tagging Model
            InputStream posModelStream = new FileInputStream("models/opennlp-en-ud-ewt-pos-1.2-2.5.0.bin");
            POSModel posModel = new POSModel(posModelStream);
            posTagger = new POSTaggerME(posModel);
            posModelStream.close();
        } catch (Exception e) {
            System.out.println("Error loading NLP models: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String detectIntent(String userInput) {
        // Tokenize Input
        String[] tokens = tokenizer.tokenize(userInput);

        // Tag Parts of Speech
        String[] posTags = posTagger.tag(tokens);

        // Simple intent detection using POS tagging
        Map<String, String> intentMapping = new HashMap<>();
        intentMapping.put("PRON", "question");
        intentMapping.put("AUX", "action");
        intentMapping.put("VERB", "action");
        intentMapping.put("NOUN", "object");
        intentMapping.put("PROPN", "object");

        StringBuilder detectedIntent = new StringBuilder("Detected intent: ");

        for (int i = 0; i < tokens.length; i++) {
            String tagCategory = intentMapping.get(posTags[i]);
            if(tagCategory != null) {
                detectedIntent.append(tagCategory).append(" (").append(tokens[i]).append("), ");
            }
        }
        return detectedIntent.toString();
    }

    public static void main(String[] args) {
        NLPProcessor nlpProcessor = new NLPProcessor();
        System.out.println(nlpProcessor.detectIntent("What is Java?"));
    }
}