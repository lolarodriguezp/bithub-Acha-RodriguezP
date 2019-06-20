package ar.edu.unlp.info.bd2.services;

import ar.edu.unlp.info.bd2.model.PersistentObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class HibernateBithubTestCase extends BithubServiceTestCase<Long> {

  @Autowired
  @Qualifier("hibernateService")
  private BithubService service;

  @Override
  protected BithubService getService() {
    return this.service;
  }

  @Override
  protected Long getId(PersistentObject object) {
    return object.getId();
  }
}
