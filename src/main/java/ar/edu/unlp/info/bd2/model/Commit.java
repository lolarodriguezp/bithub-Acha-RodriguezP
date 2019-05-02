package ar.edu.unlp.info.bd2.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Commit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String description;
    private String hash;

    @ManyToOne
    private User author;

    @OneToMany
    private List<File> files;

    public Commit(){
        super();
    }

    public Commit(String description, String hash,  User user, List<File> files){
        super();
        this.description=description;
        this.hash=hash;
        this.author=user;
        this.files=files;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return description;
    }

    public void setMessage(String description) {
        this.description = description;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User user) {
        this.author = user;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

  //  public void addFile(File file){
  //      this.files.add(file);
  //  }
}
