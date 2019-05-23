package ar.edu.unlp.info.bd2.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @OneToMany
    private List<Commit> commits;

    public Branch() {
        super();
    }

    public Branch(String name) {
        super();
        this.name = name;
    }


    public long getId() {
        return id;
    }

    public void setId(long id){
        this.id=id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Commit> getCommits() {
        return commits;
    }

    public void setCommits(List<Commit> commits) {
        this.commits = commits;
    }

    public void addCommit(Commit commit){
        getCommits().add(commit);
    }
}
