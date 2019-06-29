package ar.edu.unlp.info.bd2.services;

import ar.edu.unlp.info.bd2.model.*;
import ar.edu.unlp.info.bd2.repositories.HibernateBithubRepository;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HibernateBithubService implements BithubService<Long> {
    public HibernateBithubService(HibernateBithubRepository repository) { this.repositorio= repository;
    }

    private HibernateBithubRepository repositorio;


    @Transactional
    @Override
    public User createUser(String email, String name){
        if (this.getUserByEmail(email) == null) {
            try{
                return (User) repositorio.save(new User(name, email));
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return Optional.ofNullable(repositorio.findByEmail(email));
    }

    @Transactional
    @Override
    public Branch createBranch(String name){
        try {
            return (Branch) repositorio.save(new Branch(name));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Transactional
    @Override
    public Commit createCommit(String description, String hash, User author, List<File> files, Branch branch) {
        try {
            return (Commit) repositorio.save(new Commit(description,hash,author,files,branch));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    @Override
    public Tag createTagForCommit(String commitHash, String name) throws BithubException {
        Optional<Commit> commit= this.getCommitByHash(commitHash);
        if (!commit.isPresent()){
            throw new BithubException("El commit no existe");
        }
        try{
            return (Tag) repositorio.save(new Tag(commitHash,name));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Optional<Commit> getCommitByHash(String commitHash) {
        return Optional.ofNullable(repositorio.findByHash(commitHash));
    }

    @Override
    public File createFile(String content, String name) {
        try{
            return (File) repositorio.save(new File(name,content));

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Optional<Tag> getTagByName(String tagName) {
        try {
            return  Optional.ofNullable(repositorio.findTagByName(tagName));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Review createReview(Branch branch, User user) {
        try {
            return (Review) repositorio.save(new Review(branch,user));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public FileReview addFileReview(Review review, File file, int lineNumber, String comment) throws BithubException {

        List<Commit> commits = review.getBranch().getCommits();

        for (Commit commit : commits){
            if (commit.getFiles().contains(file)){
                try{
                    return (FileReview) repositorio.save(new FileReview(review, file, lineNumber, comment));
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
        throw new BithubException("El file no pertenece al branch");
    }

    @Override
    public Optional<Review> getReviewById(Long id) {
        return  Optional.ofNullable(repositorio.findReviewById(id));
    }

    @Override
    public List<Commit> getAllCommitsForUser(Long userId) {
        return repositorio.findCommitsByUser(userId);
    }

    @Override
    public Map<Long, Long> getCommitCountByUser() {
        List<User> users = repositorio.findAllUsers();
        Map<Long,Long> map = new HashMap();

        for (User user : users){
            map.put(new Long (user.getId()), new Long(user.getCommits().size()));
        }
        return map;
    }

    @Override
    public List<User> getUsersThatCommittedInBranch(String branchName) throws BithubException {
        Optional<Branch> branch = getBranchByName(branchName);
        if(branch.isPresent()){
            return repositorio.commitsInBranch(branchName);
        }else{
            throw new BithubException("El branch no existe");
        }
    }

    @Override
    public Optional<Branch> getBranchByName(String branchName) {
        return Optional.ofNullable(repositorio.findBranchByName(branchName));

    }
}
