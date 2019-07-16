package ar.edu.unlp.info.bd2.model;


import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "File")
public class File extends PersistentObject {

    private String filename;
    private String content;


    public File(){
        super();
    }

    public File(String fileName, String content){
        super();
        this.filename=fileName;
        this.content=content;
    }


    public String getFilename() {
        return filename;
    }

    public void setFilename(String fileName) {
        this.filename = fileName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
