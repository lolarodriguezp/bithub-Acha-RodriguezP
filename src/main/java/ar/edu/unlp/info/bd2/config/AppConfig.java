package ar.edu.unlp.info.bd2.config;

import ar.edu.unlp.info.bd2.repositories.HibernateBithubRepository;
import ar.edu.unlp.info.bd2.repositories.MongoDBBithubRepository;
import ar.edu.unlp.info.bd2.services.BithubService;
import ar.edu.unlp.info.bd2.services.HibernateBithubService;
import ar.edu.unlp.info.bd2.services.MongoDBBithubServiceImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

  @Bean
  public BithubService hibernateService() {
    HibernateBithubRepository repository = this.createRepository();
    return new HibernateBithubService(repository);
  }

  @Bean
  public HibernateBithubRepository createRepository() {
    return new HibernateBithubRepository();
  }

  @Bean
  public BithubService mongoDBService() {
    MongoDBBithubRepository repository = this.createMongoDBRepository();
    return new MongoDBBithubServiceImplementation(repository);
  }

  @Bean
  public MongoDBBithubRepository createMongoDBRepository() {
    return new MongoDBBithubRepository();
  }
}
