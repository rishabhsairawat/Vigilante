package vigilante;
public interface dbInterface {
		static final String JDBC_DRIVER = "org.sqlite.JDBC";  
		static final String DB_URL = "jdbc:sqlite:VigilanteDatabase.sqlite";
		void connect();
		void close();
		void deleteAll();
}
