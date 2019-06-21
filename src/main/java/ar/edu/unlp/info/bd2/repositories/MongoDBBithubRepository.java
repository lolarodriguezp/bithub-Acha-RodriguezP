package ar.edu.unlp.info.bd2.repositories;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;

import ar.edu.unlp.info.bd2.model.*;
import ar.edu.unlp.info.bd2.mongo.Association;
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

  public Object create(Object object, String collection, Class tClass){
    MongoCollection<Object> objectMongoCollection = client.getDatabase("bd2").getCollection(collection,tClass);
    objectMongoCollection.insertOne(object);
    return object;
  }

//  public Object find(Object object, String collection, Class tClass){
 //   MongoCollection<Object> objectMongoCollection = client.getDatabase("bd2").getCollection(collection,tClass);
 //   objectMongoCollection.insertOne(object);
 //   return object;
 // }

}
