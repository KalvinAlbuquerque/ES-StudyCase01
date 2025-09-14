package com.engdesoftware.agenda.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;
import java.io.InputStream;

/*
 * Esta classe estabelece a ligação entre o backend Java e os serviços do Firebase.
 * 
 * O Firebase Admin SDK é um conjunto de bibliotecas que permite que o backend interaja com os serviços
 * do Firebase com privilégios de Administrador. É necessário para, por exemplo, verificar tokens de autenticação
 * ou aceder à base de dados Firestore com permissões elevadas.
 * 
 * A notação @Configuration indica que esta classe contém definições de beans Spring.
 */
@Configuration
public class FirebaseConfig {

    /**
     * A notação @PostConstruct garante que o método initializeFirebase() será executado automaticamente
     * logo após o Spring ter terminado de construir o objeto FirebaseConfig.
     */
    @PostConstruct 
    /**
     * Inicializa o Firebase Admin SDK utilizando o ficheiro de credenciais localizado em "src/main/resources/serviceAccountKey.json".
     * <p>
     * Este método é chamado automaticamente após a construção do bean (via {@code @PostConstruct}).
     * Ele verifica se o ficheiro de credenciais existe e inicializa o Firebase apenas se ainda não houver instância ativa.
     * Caso o ficheiro não seja encontrado ou ocorra algum erro durante a inicialização, uma exceção é lançada para impedir o arranque da aplicação.
     *
     * @throws RuntimeException se o ficheiro de credenciais não for encontrado ou ocorrer erro na inicialização do Firebase.
     */
    public void initializeFirebase() {
        try 
        {
            //
            //  Carrega o ficheiro de credenciais do Firebase a partir do classpath
            //
            InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("serviceAccountKey.json");

            //
            //  Verifica se o ficheiro de credenciais foi encontrado
            //
            if (serviceAccount == null) 
            {
                throw new IllegalStateException("Ficheiro 'serviceAccountKey.json' não encontrado. Verifique se ele está em src/main/resources.");
            }

            //
            //  Previne que a aplicação seja inicializada mais do que uma vez. A inicialização só acontece se nenhuma outra instância do
            //  FirebaseApp estiver ativa.
            //
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                FirebaseApp.initializeApp(options);
                System.out.println("Firebase Admin SDK inicializado com sucesso!");
            }
        } catch (Exception e) {
            //
            //  Lança uma exceção para impedir o arranque da aplicação se o Firebase falhar
            //
            throw new RuntimeException("Erro ao inicializar o Firebase Admin SDK", e);
        }
    }
}