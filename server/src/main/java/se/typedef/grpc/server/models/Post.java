package se.typedef.grpc.server.models;

public class Post {

  public final long id;
  public final String author;
  public final String text;

  public Post(final long id, final String author, final String text) {
    this.id = id;
    this.author = author;
    this.text = text;
  }
}
