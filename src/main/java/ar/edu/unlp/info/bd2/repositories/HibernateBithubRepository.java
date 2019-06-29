package ar.edu.unlp.info.bd2.repositories;


import ar.edu.unlp.info.bd2.model.*;
import ar.edu.unlp.info.bd2.services.BithubException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import sun.rmi.transport.ObjectTable;


import javax.persistence.Query;
import java.util.List;

public class HibernateBithubRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public Object save (Object obj) throws Exception{
        this. sessionFactory.getCurrentSession().save(obj);
        return obj;
    }
/*
    public User saveUser(User user) throws Exception {
        this.sessionFactory.getCurrentSession().save(user);
        return user;
    }

    public Branch saveBranch(Branch branch) throws Exception {
        this.sessionFactory.getCurrentSession().save(branch);
        return branch;
    }

    public Commit saveCommit(Commit commit) throws Exception {
        this.sessionFactory.getCurrentSession().saveOrUpdate(commit);
        return commit;
    }

    public Tag saveTag(Tag tag) {
        this.sessionFactory.getCurrentSession().save(tag);
        return tag;
    }

    public File saveFile(File file) {
        this.sessionFactory.getCurrentSession().save(file);
        return file;
    }

    public Review saveReview(Review review){
        this.sessionFactory.getCurrentSession().save(review);
        return review;
    }


    public FileReview saveFileReview(FileReview fileReview){
        this.sessionFactory.getCurrentSession().save(fileReview);
        return fileReview;
    }
*/

    public User findByEmail(String email) {
        String hql = "from User " + "where email = :user_email ";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("user_email", email);
        List<User> users = query.getResultList();

        return !users.isEmpty() ? users.get(query.getFirstResult()) : null;

    }

    public Commit findByHash(String hash) {
        String hql = "from Commit " + "where hash = :commit_hash ";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("commit_hash", hash);
        List<Commit> commits = query.getResultList();

        return !commits.isEmpty() ? commits.get(query.getFirstResult()) : null;
    }

    public Tag findTagByName(String name) {
        String hql = "from Tag " + "where name = :name_tag ";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("name_tag", name);
        List<Tag> tags = query.getResultList();

        return !tags.isEmpty() ? tags.get(query.getFirstResult()) : null;
    }

    public Review findReviewById(long id){
        String hql = "from Review " + "where id = :id_review ";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id_review", id);
        List<Review> reviews = query.getResultList();

        return !reviews.isEmpty() ? reviews.get(query.getFirstResult()) : null;
    }

    public Branch findBranchByName(String name){
        String hql = "from Branch " + "where name = :name_branch ";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("name_branch", name);
        List<Branch> branchs = query.getResultList();

        return !branchs.isEmpty() ? branchs.get(query.getFirstResult()) : null;
    }

    public List<Commit> findCommitsByUser(long userId){
        String hql = "from User where id = :id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", userId);
        User user = (User) query.getSingleResult();

        return user.getCommits();
    }

    public List<User> findAllUsers(){
        String hql = "from User";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        return (List<User>) query.getResultList();
    }

    public Long countCommits (Long userId)
    {
        String hql = "select count(c) from Commit c where c.author.id = :user_id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("user_id", userId);

        return  (Long) query.getSingleResult();

    }

    public List<User> commitsInBranch (String branchName)
    {
        String hql = "select distinct author from Commit c where c.branch.name = :branch_name";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("branch_name", branchName);
        List<User> users = (List<User>) query.getResultList();
        return users;

    }


    public List<File> fileCommit (Long branchId)
    {
        String hql = "select files from Commit c where c.branch.id = :branch_id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("branch_id", branchId);

        return (List<File>) query.getResultList();
    }

}
