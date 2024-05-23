package com.jiocoders.empservice.config;

import java.net.UnknownHostException;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class MongoDBConfig implements InitializingBean {

    @Value("${spring.data.mongodb.database}")
    private String dbName;

    @Autowired
    @Lazy
    private MappingMongoConverter mappingMongoConverter;

    @Override
    public void afterPropertiesSet() throws Exception {
        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
    }

    // remove _class
    @PostConstruct
    public void setUpMongoEscapeCharacterConversion() {
        log.info("---------> --------setUpMongoEscapeCharacterConversion---check");
        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory databaseFactory, MappingMongoConverter converter) {
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return new MongoTemplate(databaseFactory, converter);
    }

    @Bean
    public MongoTemplate mongoTemplateFraud() throws UnknownHostException {
        log.info("---------> --------mongoTemplateFraud---check");
        MongoTemplate mongoTemplate = new MongoTemplate(DbConfig.clinet(), dbName);
        ((MappingMongoConverter) mongoTemplate.getConverter())
                .setTypeMapper(new DefaultMongoTypeMapper(null));// removes _class
        return mongoTemplate;
    }
}