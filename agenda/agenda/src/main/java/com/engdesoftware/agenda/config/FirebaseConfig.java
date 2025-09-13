package com.engdesoftware.agenda.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct // Esta anotação garante que o método é executado após a inicialização
    public void initializeFirebase() {
        try {
            // Procura o ficheiro de credenciais na pasta "resources"
            InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("serviceAccountKey.json");

            // Validação crucial: verifica se o ficheiro foi encontrado
            if (serviceAccount == null) {
                throw new IllegalStateException("Ficheiro 'serviceAccountKey.json' não encontrado. Verifique se ele está em src/main/resources.");
            }

            // Inicializa a aplicação Firebase se ainda não houver uma instância
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                FirebaseApp.initializeApp(options);
                System.out.println("Firebase Admin SDK inicializado com sucesso!");
            }
        } catch (Exception e) {
            // Lança uma exceção para impedir o arranque da aplicação se o Firebase falhar
            throw new RuntimeException("Erro ao inicializar o Firebase Admin SDK", e);
        }
    }
}