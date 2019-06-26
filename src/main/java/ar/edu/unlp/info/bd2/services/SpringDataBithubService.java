package ar.edu.unlp.info.bd2.services;

import ar.edu.unlp.info.bd2.model.*;

import java.util.*;

import ar.edu.unlp.info.bd2.repositories.spring.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("springDataBithubService")
@Transactional(readOnly=true)
public class SpringDataBithubService implements BithubService<Long> {

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private CommitRepository commitRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileReviewRepository fileReviewRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public User createUser(String email, String name) {
        return userRepository.save(new User(email,name));
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
        return user;
    }

    @Override
    public Branch createBranch(String name) {
        return branchRepository.save(new Branch(name));
    }

    @Override
    public Tag createTagForCommit(String commitHash, String name) throws BithubException {
        Optional<Commit> commit = this.getCommitByHash(commitHash);
        if (!commit.isPresent()){
            throw new BithubException("El commit no existe");
        }
        return tagRepository.save(new Tag(commitHash, name));

    }

    @Override
    public Optional<Commit> getCommitByHash(String commitHash) {
        Optional<Commit> commit = Optional.ofNullable(commitRepository.findByHash(commitHash));
        return commit;
    }

    @Override
    public File createFile(String content, String name) {
        return fileRepository.save(new File(content,name));
    }

    @Override
    public Optional<Tag> getTagByName(String tagName) {
        Optional<Tag> tag = Optional.ofNullable(tagRepository.findByName(tagName));
        return tag;
    }

    @Override
    public Review createReview(Branch branch, User user) {
        return reviewRepository.save(new Review(branch,user));
    }

    @Override
    public FileReview addFileReview(Review review, File file, int lineNumber, String comment) throws BithubException {
        List<Commit> commits = review.getBranch().getCommits();
        for (Commit commit : commits){
            if (commit.getFiles().contains(file)){
               return fileReviewRepository.save(new FileReview(review, file, lineNumber, comment));
            }
        }
        throw new BithubException("El file no pertenece al branch");
    }

    @Override
    public Optional<Review> getReviewById(Long id) {
        Optional<Review> review = (Optional<Review>) reviewRepository.findById(id);
        return review;
    }

    @Override
    public List<Commit> getAllCommitsForUser(Long userId) {
        Optional<User> user= userRepository.findById(userId);
        return user.get().getCommits();
    }

    @Override
    public Map getCommitCountByUser() {
        Iterable<User> users = userRepository.findAll();
        Map<Long,Long> map = new HashMap();
        for (User user : users){
            map.put(new Long (user.getId()), new Long(user.getCommits().size()));
        }
        return map;
    }

    @Override
    public List<User> getUsersThatCommittedInBranch(String branchName) throws BithubException {
        Optional<Branch> branch = this.getBranchByName(branchName);
        if(branch.isPresent()){
            return commitRepository.findDistinctAuthorByBranch(branch.get());
        }else{
            throw new BithubException("El branch no existe");
        }
    }

    @Override
    public Optional<Branch> getBranchByName(String branchName) {
        Optional<Branch> branch = Optional.ofNullable(branchRepository.findByName(branchName));
        return branch;
    }

    @Override
    public Commit createCommit(String description, String hash, User author, List<File> files, Branch branch) {
        return commitRepository.save(new Commit(description,hash,author,files,branch));
    }
}
