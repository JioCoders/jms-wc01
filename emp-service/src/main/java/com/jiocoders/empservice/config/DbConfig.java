package com.jiocoders.empservice.config;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class DbConfig {

	public static MongoClient mongoClient = null;

	@Value("${spring.data.mongodb.database}")
	private static String dbName;

	public static MongoClient clinet() {
		log.info("---------> --------dbconfig---check");
		if (mongoClient != null) {
			return mongoClient;
		}
		String connectionString = "put-url-here";
		ServerApi serverApi = ServerApi.builder()
				.version(ServerApiVersion.V1)
				.build();
		MongoClientSettings settings = MongoClientSettings.builder()
				.applyConnectionString(new ConnectionString(connectionString))
				.serverApi(serverApi)
				.build();
		// Create a new client and connect to the server
		try (MongoClient mc = MongoClients.create(settings)) {
			try {
				// Send a ping to confirm a successful connection
				MongoDatabase database = mc.getDatabase(dbName);
				database.runCommand(new Document("ping", 1));
				System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
				mongoClient = mc;
				return mongoClient;
			} catch (MongoException e) {
				e.printStackTrace();
				return mc;
			}
		}

	}
}
