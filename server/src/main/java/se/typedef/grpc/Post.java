package se.typedef.grpc;

public class Post {

  final long id;
  final String author;
  final String text;

  Post(final long id, final String author, final String text) {
    this.id = id;
    this.author = author;
    this.text = text;
  }
}
