package MongoDB;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class DatabaseManager {
    private static MongoClient mongoClient = null;
    private static MongoDatabase database;
    private static MongoAutoIncrement userIdIncrement;

    public static void connetcWithDataBase(String[] args) {
        if( mongoClient==null ) {
            return;
        }

        try  {
            mongoClient = MongoClients.create("mongodb://localhost:27017");
            database = mongoClient.getDatabase("InzApp");
            userIdIncrement = new MongoAutoIncrement(database, "IdUserCounter");
            System.out.println("Połączono z bazą: " + database.getName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void disconnect() {
        if( mongoClient != null ) {
            mongoClient.close();
        }
    }
}
