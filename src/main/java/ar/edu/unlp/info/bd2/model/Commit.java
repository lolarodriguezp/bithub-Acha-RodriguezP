package ar.edu.unlp.info.bd2.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Commit")
public class Commit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commitId;

    private String description;
    private String hash;

    @ManyToOne
    private User author;

    @ManyToOne
    private Branch branch;

    @OneToMany
    private List<File> files;



    public Commit(){
    }

    public Commit(String description, String hash,  User user, List<File> files, Branch branch){
        this.description=description;
        this.hash=hash;
        this.author=user;
        this.files=files;
        this.branch=branch;
        branch.addCommit(this);
    }

    public long getId() {
        return commitId;
    }

    public void setId(long id) {
        this.commitId = id;
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

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    //  public void addFile(File file){
  //      this.files.add(file);
  //  }
}
