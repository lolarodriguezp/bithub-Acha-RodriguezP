package ar.edu.unlp.info.bd2.config;

import ar.edu.unlp.info.bd2.repositories.BithubRepository;
import ar.edu.unlp.info.bd2.services.BithubService;
import ar.edu.unlp.info.bd2.services.BithubServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

  @Bean
  public BithubService createService() {
    BithubRepository repository = this.createRepository();
    return new BithubServiceImpl(repository);
  }

  @Bean
  public BithubRepository createRepository() {
    return new BithubRepository();
  }

}

