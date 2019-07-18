package ar.edu.unlp.info.bd2.repositories;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;

import ar.edu.unlp.info.bd2.model.*;
import ar.edu.unlp.info.bd2.mongo.Association;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
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
    //return commitCollection.find(eq("hash", commit.getHash())).first();
    return commit;
  }

  public Branch createBranch(Branch branch){
    MongoCollection<Branch> branchCollection = client.getDatabase("bd2").getCollection("branches",Branch.class);
    branchCollection.insertOne(branch);
    //Branch branch = branchCollection.find(eq("name", branch.getName())).first();
    return branch;
  }

  public User createUser(User user){
    MongoCollection<User> userCollection = client.getDatabase("bd2").getCollection("users",User.class);
    userCollection.insertOne(user);
    //return userCollection.find(eq("name", user.getName())).first();
    return user;
  }

  public Tag createTag(Tag tag){
    MongoCollection<Tag> tagCollection = client.getDatabase("bd2").getCollection("tags",Tag.class);
    tagCollection.insertOne(tag);
    //return tagCollection.find(eq("name", tag.getName())).first();
    return tag;
  }

  public File createFile(File file){
    MongoCollection<File> fileCollection = client.getDatabase("bd2").getCollection("files",File.class);
    fileCollection.insertOne(file);
    //return fileCollection.find(eq("filename", file.getFilename())).first();
    return file;
  }

  public Review createReview(Review review){
    MongoCollection<Review> reviewCollection = client.getDatabase("bd2").getCollection("reviews", Review.class);
    reviewCollection.insertOne(review);
    //return reviewCollection.find(eq("branch", review.getBranch())).first();
    return review;
  }

  public FileReview createFileReview(FileReview fileReview){
    MongoCollection<FileReview> fileReviewCollection = client.getDatabase("bd2").getCollection("fileReviews", FileReview.class);
    fileReviewCollection.insertOne(fileReview);
    return fileReview;
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

  public Optional<Review> findReview(ObjectId id){
    MongoCollection<Review> reviewCollection = client.getDatabase("bd2").getCollection("reviews", Review.class);
    Optional<Review> review = Optional.ofNullable(reviewCollection.find(eq("_id", id)).first());
    if(review.isPresent()){
      List<FileReview> filesReview = this.findFileReviewsOf(review.get().getObjectId());
      review.get().setReviews(filesReview);
    }
    return review;
  }

  public FileReview findFileReview(ObjectId id){
    MongoCollection<FileReview> fileReviewCollection = client.getDatabase("bd2").getCollection("fileReviews", FileReview.class);
    FileReview fileReview = fileReviewCollection.find(eq("_id", id)).first();
    return fileReview;
  }

  public Optional<Branch> findBranch(String branchName){
    MongoCollection<Branch> branchCollection = client.getDatabase("bd2").getCollection("branches", Branch.class);
    Optional<Branch> branch = Optional.ofNullable(branchCollection.find(eq("name", branchName)).first());
    if(branch.isPresent()) {
      List<Commit> branchCommits = this.findCommitsOfBranch(branch.get().getObjectId());
      branch.get().setCommits(branchCommits);
    }
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
    for (Association association : commitsId) {
      ObjectId idCommit = association.getDestination();
      commits.add(this.findCommit(idCommit));
    }
    return commits;
  }

  public List<FileReview> findFileReviewsOf(ObjectId reviewId){
    MongoCollection<Association> fileReviewsCollection = client.getDatabase("bd2").getCollection("reviews_fileReview", Association.class);
    FindIterable<Association> fileReviewsId = fileReviewsCollection.find(eq("source", reviewId));
    List<FileReview> fileReviews = new ArrayList<>();
    for (Association association : fileReviewsId) {
      ObjectId fileReviewId = association.getDestination();
      fileReviews.add(this.findFileReview(fileReviewId));
    }
    return fileReviews;
  }

  public List<User> findAllUsers(){
    MongoCollection<User> userCollection = client.getDatabase("bd2").getCollection("users", User.class);
    MongoCursor<User> usersCollection = userCollection.find().iterator();
    try {
      List<User> users = new ArrayList<>();
      while (usersCollection.hasNext()) {
        users.add(usersCollection.next());
      }
      return users;
    } finally {
      usersCollection.close();
    }

  }



}
