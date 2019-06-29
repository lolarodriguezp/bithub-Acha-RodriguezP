package ar.edu.unlp.info.bd2.repositories.spring.data;


import ar.edu.unlp.info.bd2.model.File;
import ar.edu.unlp.info.bd2.model.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Repository
public interface FileRepository extends CrudRepository<File, Long> {


}