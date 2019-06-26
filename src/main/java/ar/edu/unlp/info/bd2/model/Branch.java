package ar.edu.unlp.info.bd2.model;


import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

public class Branch extends PersistentObject {

    private String name;

    @OneToMany
    private List<Commit> commit = new ArrayList<>();

    public Branch() {
    }

    public Branch(String name) {
        this.name = name;
    }
}
