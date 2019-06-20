package ar.edu.unlp.info.bd2.model;

public class Tag extends PersistentObject {

    private String commitHash;
    private String name;

    public Tag(){
        super();
    }

    public Tag(String commitHash, String name){
        super();
        this.commitHash=commitHash;
        this.name=name;
    }

    public String getCommitHash() {
        return commitHash;
    }

    public void setCommitHash(String commitHash) {
        this.commitHash = commitHash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
