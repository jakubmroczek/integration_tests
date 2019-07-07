package edu.iis.mto.blog.domain.repository;

import static edu.iis.mto.blog.domain.model.AccountStatus.NEW;

import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import org.junit.Before;
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

  @Before
  public void setUp() {
    setUpTestUser();
    setUpTestPost();
    setUpTestLike();
  }

  private void setUpTestUser() {
    user = new User();
    user.setFirstName("John");
    user.setLastName("Doe");
    user.setEmail("john@domain.com");
    user.setAccountStatus(NEW);

    entityManager.persist(user);
  }

  private void setUpTestPost() {
    post = new BlogPost();
    post.setUser(user);
    post.setEntry("Lorem ipsum");

    entityManager.persist(post);
  }

  private void setUpTestLike() {
    like = new LikePost();
    like.setUser(user);
    like.setPost(post);
  }
  
}