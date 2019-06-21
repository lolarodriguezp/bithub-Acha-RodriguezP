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
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

public class MongoDBBithubRepository {

  @Autowired private MongoClient client;

  public Commit createCommit(Commit commit){
    MongoCollection<Commit> commitCollection = client.getDatabase("bd2").getCollection("commits",Commit.class);
    commitCollection.insertOne(commit);
    return (Commit) commitCollection.find(new BasicDBObject("commitHash", commit.getHash() ));
  }

  public Branch createBranch(Branch branch){
    MongoCollection<Branch> branchCollection = client.getDatabase("bd2").getCollection("branches",Branch.class);
    branchCollection.insertOne(branch);
    return (Branch) branchCollection.find(new BasicDBObject("name", branch.getName() ));
  }

  public User createUser(User user){
    MongoCollection<User> userCollection = client.getDatabase("bd2").getCollection("users",User.class);
    userCollection.insertOne(user);
    return (User) userCollection.find(new BasicDBObject("name", user.getName()));
  }

  public Tag createTag(Tag tag){
    MongoCollection<Tag> tagCollection = client.getDatabase("bd2").getCollection("tags",Tag.class);
    tagCollection.insertOne(tag);
    return (Tag) tagCollection.find(new BasicDBObject("name", tag.getName()));
  }

  public File createFile(File file){
    MongoCollection<File> fileCollection = client.getDatabase("bd2").getCollection("files",File.class);
    fileCollection.insertOne(file);
    return (File) fileCollection.find(new BasicDBObject("name", file.getFilename()));
  }

  public void create(Object object, String collection, Class tClass){
    MongoCollection<Object> objectMongoCollection = client.getDatabase("bd2").getCollection(collection,tClass);
    objectMongoCollection.insertOne(object);
  }

  public Commit findCommit(String commitHash){
    MongoCollection<Commit> commitCollection = client.getDatabase("bd2").getCollection("commits",Commit.class);
    Commit commit = (Commit) commitCollection.find(new BasicDBObject("commitHash", commitHash));
    return commit;
  }

  //recorrer coleccion de commits, con los ids

}
