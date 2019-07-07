package edu.iis.mto.blog.domain.repository;

import static edu.iis.mto.blog.builder.BlogPostBuilder.blogPost;
import static edu.iis.mto.blog.builder.UserBuilder.user;
import static edu.iis.mto.blog.domain.model.AccountStatus.NEW;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import edu.iis.mto.blog.builder.Builder;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private LikePostRepository repository;

  private User user;
  private BlogPost post;
  private LikePost like;
  private User anotherUser;
  private BlogPost anotherPost;

  @Before
  public void setUp() {
    setUpTestUser();
    setUpTestPost();
    setUpTestLike();

  }

  private void setUpTestUser() {
    user = a(user().withFirstName("John").withLastName("Doe").withEmail("john@domain.com").withAccountStatus(NEW));
    entityManager.persist(user);

    anotherUser = a(user().withFirstName("Jakub").withLastName("Tynko").withEmail("tynko@domain.com").withAccountStatus(NEW));
    entityManager.persist(anotherUser);
  }

  private void setUpTestPost() {
    post = a(blogPost().withUser(user).withEntry("Lorem ipsum"));
    entityManager.persist(post);

    anotherPost = a(blogPost().withUser(anotherUser).withEntry("Hello world!"));
    entityManager.persist(anotherPost);
  }

  private void setUpTestLike() {
    like = new LikePost();
    like.setUser(user);
    like.setPost(post);
  }

  @Test
  public void savedLikeShouldBeInList() {
    repository.save(like);

    List<LikePost> returnedLikes = repository.findAll();

    assertThat(returnedLikes, hasSize(1));
    assertThat(returnedLikes, contains(like));
  }

  @Test
  public void shouldNotContainRemovedLike() {
    repository.save(like);
    repository.delete(like);

    List<LikePost> returnedLikes = repository.findAll();

    assertThat(returnedLikes, hasSize(0));
    assertThat(returnedLikes, not(contains(like)));
  }

  @Test
  public void shouldContainUpdatedLike() {
    repository.save(like);

    like.setUser(anotherUser);
    like.setPost(anotherPost);
    repository.save(like);

    List<LikePost> likes = repository.findAll();
    assertThat(likes, hasSize(1));
    assertThat(like, hasProperty("user", is(anotherUser)));
    assertThat(like, hasProperty("post", is(anotherPost)));
  }

  @Test
  public void shouldFindLikeByUserAndPost() {
    repository.save(like);
    LikePost result = repository.findByUserAndPost(user, post).orElse(new LikePost());
    assertThat(result, is(like));
  }

  @Test
  public void findLikeByUserAndPost_shouldNotFindAnything_whenWrongUserSupplied() {
    repository.save(like);
    Optional<LikePost> result = repository.findByUserAndPost(anotherUser, post);
    assertThat(result, is(Optional.empty()));
  }

  @Test
  public void findLikeByUserAndPost_shouldNotFindAnything_whenWrongPostSupplied() {
    repository.save(like);
    Optional<LikePost> result = repository.findByUserAndPost(user, anotherPost);
    assertThat(result, is(Optional.empty()));
  }

  private <T> T a(Builder<T> builder) {
    return builder.build();
  }

}
