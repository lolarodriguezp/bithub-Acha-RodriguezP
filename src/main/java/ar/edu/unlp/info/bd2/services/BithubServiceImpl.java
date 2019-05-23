package ar.edu.unlp.info.bd2.services;

import ar.edu.unlp.info.bd2.model.*;
import ar.edu.unlp.info.bd2.repositories.BithubRepository;

import javax.transaction.Transactional;
import java.rmi.server.ExportException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BithubServiceImpl implements BithubService {

    private BithubRepository repositorio;

    public BithubServiceImpl(BithubRepository repository) {
        this.repositorio = repository;
    }

    @Transactional
    @Override
    public User createUser(String email, String name){
        User user = this.getUserByEmail(email);
        if (user == null) {
            try {
                user = new User(name, email);
                return repositorio.saveUser(user);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        try {
            User user = repositorio.findByEmail(email);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    @Override
    public Branch createBranch(String name) {
        //HABRIA QUE VERIFICAR SI HAY UNA RAMA CON ESE NOMBRE COMO EN EL USUARIO O NO IMPORTA?

        try {
            Branch branch = new Branch(name);
            return repositorio.saveBranch(branch);
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    @Override
    public Commit createCommit(String description, String hash, User author, List<File> files, Branch branch) {
        try {
            Commit commit = new Commit(description,hash,author,files,branch);
            return repositorio.saveCommit(commit);
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public Tag createTagForCommit(String commitHash, String name) throws BithubException {
        Optional<Commit> commit= this.getCommitByHash(commitHash);
        if (commit == null){
            throw new BithubException("El commit no existe");
        }
        try{
            Tag tag= new Tag(commitHash,name);
            return repositorio.saveTag(tag);
        }catch (Exception e){
            return null;
        }

    }

    @Override
    public Optional<Commit> getCommitByHash(String commitHash) {
        try {
            Commit commit = repositorio.findByHash(commitHash);
            return  Optional.ofNullable(commit);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public File createFile(String content, String name) {
        try{
            File file= new File(name,content);
            return repositorio.saveFile(file);

        }catch (Exception e){
            return null;
        }
    }

    @Override
    public Optional<Tag> getTagByName(String tagName) {
        try {
            Tag tag = repositorio.findTagByName(tagName);
            return  Optional.ofNullable(tag);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Review createReview(Branch branch, User user) {
        try {
            Review review= new Review(branch,user);
            return repositorio.saveReview(review);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public FileReview addFileReview(Review review, File file, int lineNumber, String comment) throws BithubException {
        List<File> files = repositorio.fileCommit(review.getBranch().getId());
        if (!files.contains(file))
        {
            throw new BithubException("El file no pertenece al branch");
        }
        try{
            FileReview fileReview = new FileReview(review, file, lineNumber, comment);
            return repositorio.saveFileReview(fileReview);
        }
        catch (Exception e)
        {
            return null;
        }

    }

    @Override
    public Optional<Review> getReviewById(long id) {
        try {
            Review review = repositorio.findReviewById(id);
            return  Optional.ofNullable(review);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Commit> getAllCommitsForUser(long userId) {
        try{
            List<Commit> commitsForUser = repositorio.findCommitsByUser(userId);
            return commitsForUser;
        }catch(Exception e){
            return null;
        }
    }

    @Override
    public Map<Long, Long> getCommitCountByUser() {
        try
        {
            List<User> users = repositorio.findAllUsers();
            Map<Long, Long> map = new HashMap<Long, Long>();
            for(User u : users){
                Long userId = u.getId();
                Long commitsCount =  repositorio.countCommits(userId);
                map.put(userId, commitsCount);
            }
            return map;
        }
        catch (Exception e)
        {
            return null;
        }
    }
                    //se necesita try catch con bithub exception??
    @Override
    public List<User> getUsersThatCommittedInBranch(String branchName) throws BithubException {
        try{
            return repositorio.commitsInBranch(branchName);
        }
        catch (Exception e){
            return null;
        }

    } //ver si hay q hacer la consulta mas general (?

    @Override
    public Optional<Branch> getBranchByName(String branchName) {
        try {
            Branch branch = repositorio.findBranchByName(branchName);
            return  Optional.ofNullable(branch);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
