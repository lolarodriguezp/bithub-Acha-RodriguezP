package ar.edu.unlp.info.bd2.model;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Branch")
public class Branch extends PersistentObject {

    private String name;

    @OneToMany(mappedBy = "branch")
   // @JoinColumn(name = "commit_id")
    private List<Commit> commits = new ArrayList<>();

    public Branch() {
    }

    public Branch(String name) {
        this.name = name;
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

    public void setCommits(List<Commit> commit) {
        this.commits = commit;
    }

    public void addCommit(Commit unCommit){
        getCommits().add(unCommit);
    }
}
