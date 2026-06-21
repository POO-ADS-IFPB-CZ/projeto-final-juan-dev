package dao;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
public class ConnectionFactory {
    private static final Properties propriedades = carregarPropriedades();
    private static Properties carregarPropriedades() {
        Properties props = new Properties();

        try (InputStream input = ConnectionFactory.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException(
                        "Arquivo config.properties não encontrado em src/main/resources/.\n" +
                                "Copie config.properties.example, renomeie para config.properties " +
                                "e preencha com os dados do seu banco.");
            }

            props.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler config.properties: " + e.getMessage(), e);
        }

        return props;
    }

    private static String montarUrl() {
        String host = propriedades.getProperty("db.host");
        String port = propriedades.getProperty("db.port");
        String database = propriedades.getProperty("db.database");

        return "jdbc:postgresql://" + host + ":" + port + "/" + database + "?sslmode=require";
    }

    public static Connection getConnection() {
        try {
            String url = montarUrl();
            String user = propriedades.getProperty("db.user");
            String password = propriedades.getProperty("db.password");

            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados: " + e.getMessage(), e);
        }
    }
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
}
