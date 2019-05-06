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

    public Tag saveTag(Tag tag) {
        this.sessionFactory.getCurrentSession().save(tag);
        return tag;
    }

    public Tag findTagByName(String name) {
        String hql = "from Tag " + "where name = :name_tag ";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("name_tag", name);
        List<Tag> tags = query.getResultList();

        return !tags.isEmpty() ? tags.get(query.getFirstResult()) : null;
    }


    public File saveFile(File file) {
        this.sessionFactory.getCurrentSession().save(file);
        return file;
    }

    public Review saveReview(Review review){
        this.sessionFactory.getCurrentSession().save(review);
        return review;
    }

    public Review findReviewById(long id){
        String hql = "from Review " + "where id = :id_review ";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id_review", id);
        List<Review> reviews = query.getResultList();

        return !reviews.isEmpty() ? reviews.get(query.getFirstResult()) : null;
    }

    public FileReview saveFileReview(FileReview fileReview){
        this.sessionFactory.getCurrentSession().save(fileReview);
        return fileReview;
    }

    public Branch findBranchByName(String name){
        String hql = "from Branch " + "where name = :name_branch ";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("name_branch", name);
        List<Branch> branchs = query.getResultList();

        return !branchs.isEmpty() ? branchs.get(query.getFirstResult()) : null;
    }

    public List<Commit> findCommitsByUser(long id){
        String hql = "from Commit as c where c.author.id = :user_id ";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("user_id", id);

        return (List<Commit>) query.getResultList();
    }




}

