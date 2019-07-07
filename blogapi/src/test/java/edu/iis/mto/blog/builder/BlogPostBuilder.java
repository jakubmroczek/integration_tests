package edu.iis.mto.blog.builder;

import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.User;

public class BlogPostBuilder implements Builder<BlogPost> {

  private User user;
  private String entry;

  public static BlogPostBuilder blogPost() {
    return new BlogPostBuilder();
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
    post.setUser(user);
    post.setEntry(entry);
    return post;
  }

}
