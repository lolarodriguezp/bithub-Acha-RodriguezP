package ar.edu.unlp.info.bd2.repositories;


import ar.edu.unlp.info.bd2.model.*;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;

@Transactional
public class BithubRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public User saveUser(User user) throws Exception {
        this.sessionFactory.getCurrentSession().save(user);
        return user;
    }

    public User findByEmail(String email) {
        String hql = "from User " + "where email = :user_email ";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("user_email", email);
        List<User> users = query.getResultList();

        return !users.isEmpty() ? users.get(query.getFirstResult()) : null;

    }

    public Branch saveBranch(Branch branch) throws Exception {
        this.sessionFactory.getCurrentSession().save(branch);
        return branch;
    }

    public Commit saveCommit(Commit commit) throws Exception {
        this.sessionFactory.getCurrentSession().save(commit);
        return commit;
    }


    public Commit findByHash(String hash) {
        String hql = "from Commit " + "where hash = :commit_hash ";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("commit_hash", hash);
        List<Commit> commits = query.getResultList();

        return !commits.isEmpty() ? commits.get(query.getFirstResult()) : null;
    }
}

