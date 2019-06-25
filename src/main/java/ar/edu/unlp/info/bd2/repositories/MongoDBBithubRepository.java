package ar.edu.unlp.info.bd2.repositories;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;

import ar.edu.unlp.info.bd2.model.*;
import ar.edu.unlp.info.bd2.mongo.Association;
import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import sun.rmi.transport.ObjectTable;

public class MongoDBBithubRepository {

  @Autowired private MongoClient client;

  public Commit createCommit(Commit commit){
    MongoCollection<Commit> commitCollection = client.getDatabase("bd2").getCollection("commits",Commit.class);
    commitCollection.insertOne(commit);
    return commitCollection.find(eq("hash", commit.getHash())).first();
  }

  public Branch createBranch(Branch branch){
    MongoCollection<Branch> branchCollection = client.getDatabase("bd2").getCollection("branches",Branch.class);
    branchCollection.insertOne(branch);
    FindIterable<Branch> branches = branchCollection.find(eq("name", branch.getName()));
    return branches.first();
  }

  public User createUser(User user){
    MongoCollection<User> userCollection = client.getDatabase("bd2").getCollection("users",User.class);
    userCollection.insertOne(user);
    return userCollection.find(eq("name", user.getName())).first();
  }

  public Tag createTag(Tag tag){
    MongoCollection<Tag> tagCollection = client.getDatabase("bd2").getCollection("tags",Tag.class);
    tagCollection.insertOne(tag);
    return tagCollection.find(eq("name", tag.getName())).first();
  }

  public File createFile(File file){
    MongoCollection<File> fileCollection = client.getDatabase("bd2").getCollection("files",File.class);
    fileCollection.insertOne(file);
    return fileCollection.find(eq("filename", file.getFilename())).first();
  }

  public Review createReview(Review review){
    MongoCollection<Review> reviewCollection = client.getDatabase("bd2").getCollection("reviews", Review.class);
    reviewCollection.insertOne(review);
    return reviewCollection.find(eq("branch", review.getBranch())).first();
  }

  public void create(Object object, String collection, Class tClass){
    MongoCollection<Object> objectMongoCollection = client.getDatabase("bd2").getCollection(collection,tClass);
    objectMongoCollection.insertOne(object);
  }

  public Commit findCommit(String commitHash){
    MongoCollection<Commit> commitCollection = client.getDatabase("bd2").getCollection("commits",Commit.class);
    Commit commit = commitCollection.find(eq("hash", commitHash)).first();
    return commit; // ver de hacer el return directamente
  }

  public Commit findCommit(ObjectId id){
    MongoCollection<Commit> commitCollection = client.getDatabase("bd2").getCollection("commits",Commit.class);
    Commit commit = commitCollection.find(eq("_id", id)).first();
    return commit; // ver de hacer el return directamente
  }

  public Tag findTag(String tagName){
    MongoCollection<Tag> tagCollection = client.getDatabase("bd2").getCollection("tags", Tag.class);
    return tagCollection.find(eq("name", tagName)).first();
  }

  public User findUser(String userEmail){
    MongoCollection<User> userCollection = client.getDatabase("bd2").getCollection("users", User.class);
    return userCollection.find(eq("email", userEmail)).first();
  }

  public Review findReview(ObjectId id){
    MongoCollection<Review> reviewCollection = client.getDatabase("bd2").getCollection("reviews", Review.class);
    return reviewCollection.find(eq("_id", id)).first();
  }

  public Optional<Branch> findBranch(String branchName){
    MongoCollection<Branch> branchCollection = client.getDatabase("bd2").getCollection("branches", Branch.class);
    Optional<Branch> branch = Optional.ofNullable(branchCollection.find(eq("name", branchName)).first());
    /*if(branch.isPresent()) {
      List<Commit> branchCommits = this.findCommitsOfBranch(branch.get().getObjectId());
      branch.get().setCommits(branchCommits);
    }*/
    return branch;
  }

  public List<Commit> findCommitsForUser (ObjectId id){
    MongoCollection<Association> authorCommitsCollection = client.getDatabase("bd2").getCollection("author_commits", Association.class);
    FindIterable<Association> commitsId = authorCommitsCollection.find(eq("source", id));
    List<Commit> commits = new ArrayList<>();
    for (Association idCommit : commitsId){
      commits.add(this.findCommit(idCommit.getDestination()));
    }
    return commits;
  }

  public List<Commit> findCommitsOfBranch (ObjectId branchId){
    MongoCollection<Association> branchCommitsCollection = client.getDatabase("bd2").getCollection("commits_branch", Association.class);
    FindIterable<Association> commitsId = branchCommitsCollection.find(eq("source", branchId));
    List<Commit> commits = new ArrayList<>();
    Iterator<Association> it = commitsId.iterator();
    while(it.hasNext()){
      Association assoc = (Association) it.next();
      commits.add(this.findCommit(assoc.getDestination()));
    }
    /*for (Association association : commitsId) {
      ObjectId idCommit = association.getDestination();
      commits.add(this.findCommit(idCommit));
    }*/
    return commits;
  }

  public List<User> findAllUsers(){
    MongoCollection<User> userCollection = client.getDatabase("bd2").getCollection("users", User.class);
    return (List<User>)userCollection.find(new Document());
  }

  //recorrer coleccion de commits, con los ids

}
