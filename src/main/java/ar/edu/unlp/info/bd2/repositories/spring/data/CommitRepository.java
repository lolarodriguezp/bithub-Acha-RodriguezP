package ar.edu.unlp.info.bd2.repositories.spring.data;

import ar.edu.unlp.info.bd2.model.Branch;
import ar.edu.unlp.info.bd2.model.Commit;
import ar.edu.unlp.info.bd2.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Repository
public interface CommitRepository extends CrudRepository<Commit, Long> {
     Commit findByHash(String hash);
     List<User> findDistinctAuthorByBranch(Branch branch);
}