package edu.iis.mto.blog.builder;

import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.User;

public class BlogPostBuilder implements Builder<BlogPost> {

  private User user;
  private String entry = "";
  private long id;

  public static BlogPostBuilder blogPost() {
    return new BlogPostBuilder();
  }

  public BlogPostBuilder withId(long id) {
    this.id = id;
    return this;
  }

  private BlogPostBuilder() {
  }

  public BlogPostBuilder withUser(User user) {
    this.user = user;
    return this;
  }

  public BlogPostBuilder withEntry(String entry) {
    this.entry = entry;
    return this;
  }

  @Override
  public BlogPost build() {
    BlogPost post = new BlogPost();
    post.setId(id);
    post.setUser(user);
    post.setEntry(entry);
    return post;
  }
}
