package ar.edu.unlp.info.bd2.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
@Table(name = "Branch")
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long branchId;

    private String name;

    @OneToMany
    private List<Commit> commit = new ArrayList<>();

    public Branch() {
    }

    public Branch(String name) {
        this.name = name;
    }


    public long getId() {
        return branchId;
    }

    public void setId(long id){
        this.branchId = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Commit> getCommits() {
        return commit;
    }

    public void setCommits(List<Commit> commits) {
        this.commit = commits;
    }

    public void addCommit(Commit unCommit){
        getCommits().add(unCommit);
    }

}
