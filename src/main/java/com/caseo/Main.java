package com.caseo;

import com.caseo.app.Bootstrap;
import com.caseo.app.ApplicationContext;

public class Main {
    public static void main(String[] args) throws Exception {

        ApplicationContext context = Bootstrap.init();

        // TAG
        context.wordGenerationService().tagGenerate(1);

        // PLACEHOLDER
        //context.wordGenerationService().placeholderGenerate(1);// ← document_set_id
    }
}