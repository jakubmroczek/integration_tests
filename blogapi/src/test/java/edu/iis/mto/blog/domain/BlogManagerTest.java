package edu.iis.mto.blog.domain;

import static edu.iis.mto.blog.builder.BlogPostBuilder.blogPost;
import static edu.iis.mto.blog.builder.UserBuilder.user;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.builder.Builder;
import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.BlogDataMapper;
import edu.iis.mto.blog.services.BlogService;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

    @MockBean
    UserRepository userRepository;

    @MockBean
    BlogPostRepository blogPostRepository;

    @MockBean
    LikePostRepository likePostRepository;

    @Autowired
    BlogDataMapper dataMapper;

    @Autowired
    BlogService blogService;

    private User userAddingALike;
    private BlogPost blogPost;

    @Before
    public void setUp() {
        userAddingALike = a(user().withId(1L));
        User blogPostOwner = a(user().withId(2L).withAccountStatus(AccountStatus.CONFIRMED));
        blogPost = a(blogPost().withId(2L).withUser(blogPostOwner));
    }

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test(expected = DomainError.class)
    public void addLikeToPost_shouldThrowException_whenUserStatusIsNew() {
        userAddingALike.setAccountStatus(AccountStatus.NEW);

        when(userRepository.findById(any())).thenReturn(Optional.of(userAddingALike));
        when(blogPostRepository.findById(any())).thenReturn(Optional.of(blogPost));

        blogService.addLikeToPost(userAddingALike.getId(), blogPost.getId());
    }

    @Test(expected = DomainError.class)
    public void addLikeToPost_shouldThrowException_whenUserStatusIsRemoved() {
        userAddingALike.setAccountStatus(AccountStatus.REMOVED);

        when(userRepository.findById(any())).thenReturn(Optional.of(userAddingALike));
        when(blogPostRepository.findById(any())).thenReturn(Optional.of(blogPost));

        blogService.addLikeToPost(userAddingALike.getId(), blogPost.getId());
    }

    @Test()
    public void addLikeToPost_shouldEndWithSuccess_whenUserStatusIsConfirmed() {
        userAddingALike.setAccountStatus(AccountStatus.CONFIRMED);

        when(userRepository.findById(any())).thenReturn(Optional.of(userAddingALike));
        when(blogPostRepository.findById(any())).thenReturn(Optional.of(blogPost));

        blogService.addLikeToPost(userAddingALike.getId(), blogPost.getId());

        ArgumentCaptor<LikePost> captor = ArgumentCaptor.forClass(LikePost.class);
        verify(likePostRepository).save(captor.capture());

        LikePost likePost = captor.getValue();
        assertThat(likePost, hasProperty("user", is(userAddingALike)));
        assertThat(likePost, hasProperty("post", is(blogPost)));
    }

    private <T> T a(Builder<T> builder) {
        return builder.build();
    }

}
