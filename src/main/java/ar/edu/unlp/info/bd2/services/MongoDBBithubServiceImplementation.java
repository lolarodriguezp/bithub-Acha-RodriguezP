package ar.edu.unlp.info.bd2.services;

import ar.edu.unlp.info.bd2.model.*;
import ar.edu.unlp.info.bd2.mongo.Association;
import ar.edu.unlp.info.bd2.repositories.MongoDBBithubRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MongoDBBithubServiceImplementation implements BithubService {

    private MongoDBBithubRepository repository;

    public MongoDBBithubServiceImplementation(MongoDBBithubRepository repository) {
        this.repository = repository;
    }

    @Override
    public User createUser(String email, String name) {
        return (User)this.repository.create(new User(email, name), "users", User.class);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Branch createBranch(String name) {
        return (Branch) this.repository.create(new Branch(name), "branches", Branch.class);
    }

    @Override
    public Tag createTagForCommit(String commitHash, String name) throws BithubException {
        Optional<Commit> commit = this.getCommitByHash(commitHash);
        if (!commit.isPresent()){
            throw new BithubException("El commit no existe");
        }
        try{
            return (Tag) this.repository.create(new Tag(commitHash, name), "tags", Tag.class);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Optional<Commit> getCommitByHash(String commitHash) {
        return Optional.empty();
    }

    @Override
    public File createFile(String name, String content) {
        return (File) this.repository.create(new File(name, content), "files", File.class);
    }

    @Override
    public Optional<Tag> getTagByName(String tagName) {
        return Optional.empty();
    }

    @Override
    public Review createReview(Branch branch, User user) {
        Review review = new Review();
        review.setAuthor(user);
        review.setBranch(branch);
        return null;
    }

    @Override
    public FileReview addFileReview(Review review, File file, int lineNumber, String comment) throws BithubException {
        return null;
    }

    @Override
    public Optional<Review> getReviewById(Object id) {
        return Optional.empty();
    }

    @Override
    public List<Commit> getAllCommitsForUser(Object userId) {
        return null;
    }

    @Override
    public Map getCommitCountByUser() {
        return null;
    }

    @Override
    public List<User> getUsersThatCommittedInBranch(String branchName) throws BithubException {
        return null;
    }

    @Override
    public Optional<Branch> getBranchByName(String branchName) {
        return Optional.empty();
    }

    @Override
    public Commit createCommit(String description, String hash, User author, List<File> files, Branch branch) {
        Commit commit = new Commit();
        commit.setFiles(files);
        commit.setMessage(description);
        commit.setBranch(branch);
        commit.setHash(hash);
        commit.setAuthor(author);
        Commit persistedCommit = (Commit) this.repository.create(commit, "commits", Commit.class);
        Association commits_branch = new Association(commit.getObjectId(), branch.getObjectId());
        this.repository.create(commits_branch, "commits_branch", Association.class);
        Association author_commits = new Association(author.getObjectId(), commit.getObjectId());
        this.repository.create(author_commits, "author_commits", Association.class);
        return persistedCommit;
    }
}
