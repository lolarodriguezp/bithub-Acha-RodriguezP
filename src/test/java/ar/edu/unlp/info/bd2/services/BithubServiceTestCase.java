package ar.edu.unlp.info.bd2.services;

import ar.edu.unlp.info.bd2.config.AppConfig;
import ar.edu.unlp.info.bd2.config.HibernateConfiguration;
import ar.edu.unlp.info.bd2.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class, HibernateConfiguration.class}, loader = AnnotationConfigContextLoader.class)
@Transactional
@Rollback(true)
public class BithubServiceTestCase {

  @Autowired
  BithubService service;

  @Test
  public void testCreateBranchAndCommit() {
    Branch master = this.service.createBranch("master");
    assertNotNull(master.getId());
    assertEquals("master", master.getName());

    User user = this.service.createUser("user@bithub.bd2.info.unlp.edu.ar", "User");
    assertNotNull(user.getId());
    assertEquals("user@bithub.bd2.info.unlp.edu.ar", user.getEmail());
    assertEquals("User", user.getName());

    File file1 = this.service.createFile("System.out.println(\"Hello world\");", "Main.java");
    assertEquals("Main.java", file1.getFilename());
    assertEquals("System.out.println(\"Hello world\");", file1.getContent());
    assertNotNull(file1.getId());

    File file2 = this.service.createFile("print(\"Hello world\")", "Main.py");

    Commit commit = this.service.createCommit("Initial commit", "ab4f19z", user, Arrays.asList(file1, file2), master);
    assertEquals("Initial commit", commit.getMessage());
    assertEquals("ab4f19z", commit.getHash());
    assertNotNull(commit.getId());

    Optional<Commit> commitFromDb = this.service.getCommitByHash("ab4f19z");
    assertTrue(commitFromDb.isPresent());
    assertEquals(user.getId(), commitFromDb.get().getAuthor().getId());
    assertEquals(2, commitFromDb.get().getFiles().size());

    assertEquals("System.out.println(\"Hello world\");", commitFromDb.get().getFiles().get(0).getContent());
    assertEquals("Main.java", commitFromDb.get().getFiles().get(0).getFilename());

    assertEquals("print(\"Hello world\")", commitFromDb.get().getFiles().get(1).getContent());
    assertEquals("Main.py", commitFromDb.get().getFiles().get(1).getFilename());

    Optional<Branch> branchFromDb = this.service.getBranchByName("master");
    assertTrue(branchFromDb.isPresent());
    assertEquals(1, branchFromDb.get().getCommits().size());
  }

  @Test
  public void testCreateTag() throws BithubException {
    Branch master = this.service.createBranch("master");
    User user = this.service.createUser("user@bithub.bd2.info.unlp.edu.ar", "User");
    File file = this.service.createFile("System.out.println(\"Hello world\");", "Main.java");
    this.service.createCommit("Initial commit", "ab4f19z", user, Arrays.asList(file), master);

    assertThrows(BithubException.class,
            () -> this.service.createTagForCommit("fakeHash123", "tag1"));

    Tag tag = this.service.createTagForCommit("ab4f19z", "tag1");
    assertNotNull(tag.getId());
    assertEquals("tag1", tag.getName());

    Optional<Tag> savedTag = this.service.getTagByName("tag1");
    assertTrue(savedTag.isPresent());
    assertEquals(tag.getId(), savedTag.get().getId());

    assertFalse(this.service.getTagByName("fakeTag123").isPresent());
  }

  @Test
  void testCreateReview() throws BithubException {
    Branch master = this.service.createBranch("master");
    Branch develop = this.service.createBranch("develop");

    User user1 = this.service.createUser("user1@bithub.bd2.info.unlp.edu.ar", "User One");
    User user2 = this.service.createUser("user2@bithub.bd2.info.unlp.edu.ar", "User Two");

    File fileInMasterBranch = this.service.createFile("System.out.println(\"Hello wrld\");", "Main.java");
    this.service.createCommit("Initial commit", "ab4f19z", user1, Arrays.asList(fileInMasterBranch), master);

    File fileInDevelopBranch = this.service.createFile("System.out.println(\"Hello wrld\");", "Main.java");
    this.service.createCommit("Initial commit", "ab4f19z", user1, Arrays.asList(fileInDevelopBranch), develop);

    Review review = this.service.createReview(master, user2);
    assertNotNull(review.getId());
    assertEquals(master.getId(), review.getBranch().getId());
    assertEquals(user2.getId(), review.getAuthor().getId());

    FileReview fileReview = this.service.addFileReview(review, fileInMasterBranch, 1, "There is a typo ('wrld' should be 'world')");
    assertNotNull(fileReview.getId());
    assertEquals(fileInMasterBranch.getId(), fileReview.getReviewedFile().getId());
    assertEquals(new Integer(1), fileReview.getLineNumber());
    assertEquals("There is a typo ('wrld' should be 'world')", fileReview.getComment());

    // debe fallar: el branch en el cual se quiere hacer el review no corresponde con el branch en el cual se encuentra
    // el archivo
    assertThrows(BithubException.class,
            () -> this.service.addFileReview(review, fileInDevelopBranch, 1, "There is a typo ('wrld' should be 'world'"));

    Optional<Review> freshReview = this.service.getReviewById(review.getId());
    assertTrue(freshReview.isPresent());
    assertEquals(user2.getId(), freshReview.get().getAuthor().getId());
    assertEquals(master.getId(), freshReview.get().getBranch().getId());
    assertEquals(1, freshReview.get().getReviews().size());

    assertEquals(fileReview.getId(), freshReview.get().getReviews().get(0).getId());
    assertEquals(fileReview.getComment(), freshReview.get().getReviews().get(0).getComment());
    assertEquals(fileReview.getLineNumber(), freshReview.get().getReviews().get(0).getLineNumber());
  }

  @Test
  void testGetCommitsFromUser() {
    Branch master = this.service.createBranch("master");
    Branch develop = this.service.createBranch("develop");

    User user = this.service.createUser("user@bithub.bd2.info.unlp.edu.ar", "User");

    File fileInMasterBranch = this.service.createFile("System.out.println(\"Hello world\");", "Main.java");
    Commit commitInMaster = this.service.createCommit("Initial commit", "ab4f19z", user, Arrays.asList(fileInMasterBranch), master);

    File fileInDevelopBranch = this.service.createFile("System.out.println(\"Hello wrld\");", "Main.java");
    Commit commitInDevelop = this.service.createCommit("Another commit", "12g455", user, Arrays.asList(fileInDevelopBranch), develop);

    List<Commit> commits = this.service.getAllCommitsForUser(user.getId());
    assertEquals(2, commits.size());
    assertEquals(1, commits.stream().filter(s -> s.getId() == commitInDevelop.getId()).count());
    assertEquals(1, commits.stream().filter(s -> s.getId() == commitInMaster.getId()).count());
  }

  @Test
  void testGetTotalNumberOfCommitsPerUser() {
    Branch master = this.service.createBranch("master");

    User user1 = this.service.createUser("user1@bithub.bd2.info.unlp.edu.ar", "User One");
    User user2 = this.service.createUser("user2@bithub.bd2.info.unlp.edu.ar", "User Two");

    File file1 = this.service.createFile("System.out.println(\"Hello world\");", "Main.java");
    this.service.createCommit("Initial commit", "xyz123", user1, Arrays.asList(file1), master);

    File file2 = this.service.createFile("System.out.println(\"Goodbye world\");", "Main.java");
    this.service.createCommit("Initial commit", "rew838", user2, Arrays.asList(file2), master);

    File file3 = this.service.createFile("System.out.println(\"Ciao mondo\");", "Main.java");
    this.service.createCommit("Initial commit", "troz12", user2, Arrays.asList(file3), master);

    Map<Long, Long> commitCount = this.service.getCommitCountByUser();

    assertEquals(new Long(1), commitCount.get(user1.getId()));
    assertEquals(new Long(2), commitCount.get(user2.getId()));
  }

  @Test
  void testUsersThatCommitedInBranch() throws BithubException {
    Branch master = this.service.createBranch("master");
    Branch develop = this.service.createBranch("develop");

    User user1 = this.service.createUser("user1@bithub.bd2.info.unlp.edu.ar",  "User One");
    User user2 = this.service.createUser("user2@bithub.bd2.info.unlp.edu.ar", "User Two");
    User user3 = this.service.createUser("user3@bithub.bd2.info.unlp.edu.ar", "User Three");

    File file1 = this.service.createFile("System.out.println(\"Hello world\");", "Main.java");
    this.service.createCommit("Initial commit", "xyz123", user1, Arrays.asList(file1), master);

    File file2 = this.service.createFile("System.out.println(\"Goodbye world\");", "Main.java");
    this.service.createCommit("Initial commit", "rew838", user2, Arrays.asList(file2), master);

    File file3 = this.service.createFile("System.out.println(\"Ciao mondo\");", "Main.java");
    this.service.createCommit("Initial commit", "troz12", user1, Arrays.asList(file3), develop);

    File file4 = this.service.createFile("System.out.println(\"Chau mundo\");", "Main.java");
    this.service.createCommit("Initial commit", "troz12", user1, Arrays.asList(file3), master);

    assertThrows(BithubException.class,
            () -> this.service.getUsersThatCommittedInBranch("non-existent-branch"));

    List<User> users = this.service.getUsersThatCommittedInBranch("master");

    assertEquals(2, users.size());
    assertTrue(users.stream().anyMatch(u -> u.getEmail().equals("user1@bithub.bd2.info.unlp.edu.ar")));
    assertTrue(users.stream().anyMatch(u -> u.getEmail().equals("user2@bithub.bd2.info.unlp.edu.ar")));
    assertFalse(users.stream().anyMatch(u -> u.getEmail().equals("user3@bithub.bd2.info.unlp.edu.ar")));
  }

}
