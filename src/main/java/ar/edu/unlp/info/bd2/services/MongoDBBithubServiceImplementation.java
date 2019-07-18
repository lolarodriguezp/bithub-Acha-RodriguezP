package ar.edu.unlp.info.bd2.services;

import ar.edu.unlp.info.bd2.model.*;
import ar.edu.unlp.info.bd2.mongo.Association;
import ar.edu.unlp.info.bd2.repositories.MongoDBBithubRepository;
import org.bson.types.ObjectId;

import javax.swing.text.html.Option;
import java.util.*;

public class MongoDBBithubServiceImplementation implements BithubService<ObjectId> {

    private MongoDBBithubRepository repository;

    public MongoDBBithubServiceImplementation(MongoDBBithubRepository repository) {
        this.repository = repository;
    }

    @Override
    public User createUser(String email, String name) {
        return this.repository.createUser(new User(name, email));
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return Optional.ofNullable(this.repository.findUser(email));
    }

    @Override
    public Branch createBranch(String name) {
        return this.repository.createBranch(new Branch(name));
    }

    @Override
    public Tag createTagForCommit(String commitHash, String name) throws BithubException {
        Optional<Commit> commit = this.getCommitByHash(commitHash);
        if (!commit.isPresent()){
            throw new BithubException("El commit no existe");
        }
        try{
            return this.repository.createTag(new Tag(commitHash, name));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Optional<Commit> getCommitByHash(String commitHash) {
        return Optional.ofNullable(this.repository.findCommit(commitHash));
    }

    @Override
    public File createFile(String content, String name) {
        return this.repository.createFile(new File(name, content));
    }

    @Override
    public Optional<Tag> getTagByName(String tagName) {
        return Optional.ofNullable(this.repository.findTag(tagName));
    }

    @Override
    public Review createReview(Branch branch, User user) {
        return this.repository.createReview(new Review(branch, user));
    }

    @Override
    public FileReview addFileReview(Review review, File file, int lineNumber, String comment) throws BithubException {
        List<Commit> commits = this.repository.findCommitsOfBranch(review.getBranch().getObjectId());

        for (Commit commit : commits){
            for (File fileOfCommit : commit.getFiles()){
                if (fileOfCommit.getObjectId().equals(file.getObjectId())) {
                    FileReview fileReview = new FileReview();
                    fileReview.setComment(comment);
                    fileReview.setLineNumber(lineNumber);
                    fileReview.setReviewedFile(file);
                    fileReview.setReview(review);
                    FileReview persistedFileReview = this.repository.createFileReview(fileReview);

                    Association reviews = new Association(review.getObjectId(), persistedFileReview.getObjectId());
                    this.repository.create(reviews, "reviews_fileReview", Association.class);

                    return persistedFileReview;
                }
            }
        }
        throw new BithubException("El file no pertenece al branch");
    }

    @Override
    public Optional<Review> getReviewById(ObjectId id) {
        return this.repository.findReview(id);
    }

    @Override
    public List<Commit> getAllCommitsForUser(ObjectId userId) {
        return this.repository.findCommitsForUser(userId);
    }

    @Override
    public Map<ObjectId, Long> getCommitCountByUser() {
        try
        {
            List<User> users = this.repository.findAllUsers();
            Map<ObjectId,Long> map = new HashMap<>();
            for (User user : users){
                map.put(user.getObjectId(), new Long(repository.findCommitsForUser(user.getObjectId()).size()) );
            }
            return map;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public List<User> getUsersThatCommittedInBranch(String branchName) throws BithubException {
        Optional<Branch> branch = getBranchByName(branchName);
        if(branch.isPresent()){
            List<Commit> commits = this.repository.findCommitsOfBranch(branch.get().getObjectId());
            List<User> users = this.repository.getUsersThatCommitedInBranch(branch.get());
            for (Commit commit : commits){
                    users.add(commit.getAuthor());
            }
            return users;
        }else{
            throw new BithubException("El branch no existe");
        }
    }

    @Override
    public Optional<Branch> getBranchByName(String branchName) {
        return this.repository.findBranch(branchName);
    }

    @Override
    public Commit createCommit(String description, String hash, User author, List<File> files, Branch branch) {
        Commit commit = new Commit();
        commit.setFiles(files);
        commit.setMessage(description);
        commit.setBranch(branch);
        commit.setHash(hash);
        commit.setAuthor(author);
        Commit persistedCommit = this.repository.createCommit(commit);

        Association commits_branch = new Association(branch.getObjectId(), persistedCommit.getObjectId());
        this.repository.create(commits_branch, "commits_branch", Association.class);

        Association author_commits = new Association(author.getObjectId(), persistedCommit.getObjectId());
        this.repository.create(author_commits, "author_commits", Association.class);

        return persistedCommit;
    }
}
