package ar.edu.unlp.info.bd2.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Review")
public class Review extends PersistentObject {

    @ManyToOne
    private Branch branch;

    @ManyToOne
    private User author;

    @OneToMany
    @JoinColumn(name = "review_id")
    private List<FileReview> reviews;

    public Review(){
        super();
    }

    public Review(Branch branch, User user){
        super();
        this.branch=branch;
        this.author=user;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<FileReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<FileReview> reviews) {
        this.reviews = reviews;
    }

    public void addFileReview(FileReview fileReview) {
        getReviews().add(fileReview);
    }

}
