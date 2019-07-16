package ar.edu.unlp.info.bd2.model;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "User")
public class User extends PersistentObject {

    private String name;
    private String email;

    @OneToMany(mappedBy = "author")
    private List<Commit> commits;

    public User() {
        super();
    }

    public User(String name, String email) {
        super();
        this.name = name;
        this.email= email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Commit> getCommits() {
        return commits;
    }

    public void setCommits(List<Commit> commits) {
        this.commits = commits;
    }

    public void addCommit(Commit unCommit){
        getCommits().add(unCommit);
    }


}
