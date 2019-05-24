package ar.edu.unlp.info.bd2.model;


import javax.persistence.*;

@Entity
public class FileReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Review review;

    @ManyToOne
    private File file;

    private Integer lineNumber;
    private String comment;

    public FileReview(){
        super();
    }

    public FileReview(Review review, File file, Integer lineNumber, String comment){
        super();
        this.review=review;
        this.file=file;
        this.lineNumber=lineNumber;
        this.comment=comment;
        review.addFileReview(this);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public File getReviewedFile() {
        return file;
    }

    public void setReviewedFile(File file) {
        this.file = file;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
