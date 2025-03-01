package com.mindex.challenge.config;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.dao.CompensationRepository;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.lang.NonNull;

import java.net.InetSocketAddress;

//The number of classes had to be expanded in order to create a new Compensation repository
@EnableMongoRepositories(basePackageClasses = {EmployeeRepository.class, CompensationRepository.class})
@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Override
    @NonNull
    protected String getDatabaseName() {
        return "test";
    }

    @Override
    @NonNull
    public MongoClient mongoClient() {
        MongoServer server = new MongoServer(new MemoryBackend());
        InetSocketAddress serverAddress = server.bind();
        String mongoConnectionString = String.format("mongodb://%s:%d", serverAddress.getHostName(), serverAddress.getPort());
        return MongoClients.create(mongoConnectionString);
    }
}


