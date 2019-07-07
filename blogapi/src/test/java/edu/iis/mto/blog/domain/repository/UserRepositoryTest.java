package edu.iis.mto.blog.domain.repository;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Doe");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
    }
    
    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {

        List<User> users = repository.findAll();

        assertThat(users, Matchers.hasSize(0));
    }

    @Test
    public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
        User persistedUser = entityManager.persist(user);
        List<User> users = repository.findAll();

        assertThat(users, Matchers.hasSize(1));
        assertThat(users.get(0).getEmail(), Matchers.equalTo(persistedUser.getEmail()));
    }

    @Test
    public void shouldStoreANewUser() {

        User persistedUser = repository.save(user);

        assertThat(persistedUser.getId(), Matchers.notNullValue());
    }

    @Test
    public void shouldFindUserByFirstName() {
        repository.save(user);

        List<User> returnedUsers = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
            "Jan",
            "Not existing surname",
            "notexistingemail@domain.com"
        );

        assertThat(returnedUsers, contains(user));
    }

    @Test
    public void shouldFindUserBySurname() {
        repository.save(user);

        List<User> returnedUsers = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
            "Not exisiting first name",
            "Doe",
            "notexistingemail@domain.com"
        );

        assertThat(returnedUsers, contains(user));
    }

}
