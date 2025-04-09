package com.afterhoursdev.scavengerhunt.hunt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

/**
 * The MongoConfig class contains the Bean definitions for the Mongo Repository Service. Beans
 * defined in a @Configuration class are created and managed by the Spring IoC container when 
 * the application context is being initialized. Beans can be injected into other beans via 
 * constructor, setter, or field injection. 
 *   
 * @author  Jim Zombek
 * @version 1.0
 * @since   12-6-2024
*/

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {
		
	@Value("${spring.data.mongodb.database}") 
	private String database; 
	
	@Value("${spring.data.mongodb.uri}") 
	private String uri; 
	
	@Bean
	public MongoClient initMongoDB() {
	    MongoClient mongoClient = MongoClients.create(uri);
	    return mongoClient;
    }

	@Override
	protected String getDatabaseName() {
		return database;
	}
	
	// MongoTemplate acts as the central class for MongoDB data access in Spring. 
	// It offers fine-grained control over MongoDB operations like queries, inserts, 
	// updates, deletes, and aggregations.
	@Bean
	public MongoTemplate mongoTemplate() {
		//TODO: This will remove the Java class name when inserting documents in MongoDB
		//MappingMongoConverter converter = 
	    //        (MappingMongoConverter) mongoTemplate(mongoClient).getConverter();
	    //    converter.setTypeMapper(new DefaultMongoTypeMapper(null));
	    return new MongoTemplate(initMongoDB(), database);	
	}
}