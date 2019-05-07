package ar.edu.unlp.info.bd2.model;


import javax.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String email;

    public long getId() {
        return id;
    }

    public void setId(long id){ this.id=id; }

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

}