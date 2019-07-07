package edu.iis.mto.blog.domain;

import static edu.iis.mto.blog.builder.BlogPostBuilder.blogPost;
import static edu.iis.mto.blog.builder.UserBuilder.user;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import edu.iis.mto.blog.builder.Builder;
import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.BlogDataMapper;
import edu.iis.mto.blog.services.BlogService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

    @MockBean
    UserRepository userRepository;

    @MockBean
    BlogPostRepository blogPostRepository;

    @MockBean
    LikePostRepository likeRepository;

    @Autowired
    BlogDataMapper dataMapper;

    @Autowired
    BlogService blogService;

    private User userAddingALike;
    private User blogPostOwner;
    private BlogPost blogPost;

    @Before
    public void setUp() {
        userAddingALike = a(user().withId(1L));
        blogPostOwner = a(user().withId(2L).withAccountStatus(AccountStatus.CONFIRMED));
        blogPost = a(blogPost().withId(2L).withUser(blogPostOwner));
    }

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        Assert.assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test(expected = DomainError.class)
    public void addLikeToPost_shouldThrowException_whenUserStatusIsNew() {
        userAddingALike.setAccountStatus(AccountStatus.NEW);

        when(userRepository.findById(any())).thenReturn(Optional.of(blogPostOwner));
        when(blogPostRepository.findById(any())).thenReturn(Optional.of(blogPost));

        blogService.addLikeToPost(userAddingALike.getId(), blogPost.getId());
    }

    @Test(expected = DomainError.class)
    public void addLikeToPost_shouldThrowException_whenUserStatusIsRemoved() {
        userAddingALike.setAccountStatus(AccountStatus.REMOVED);

        when(userRepository.findById(any())).thenReturn(Optional.of(blogPostOwner));
        when(blogPostRepository.findById(any())).thenReturn(Optional.of(blogPost));

        blogService.addLikeToPost(userAddingALike.getId(), blogPost.getId());
    }

    @Test()
    public void addLikeToPost_shouldEndWithSuccess_whenUserStatusIsConfirmed() {
        userAddingALike.setAccountStatus(AccountStatus.CONFIRMED);

        when(userRepository.findById(any())).thenReturn(Optional.of(blogPostOwner));
        when(blogPostRepository.findById(any())).thenReturn(Optional.of(blogPost));

        blogService.addLikeToPost(userAddingALike.getId(), blogPost.getId());
    }

    private <T> T a(Builder<T> builder) {
        return builder.build();
    }

}
