package se.typedef.grpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

class BlogPostService extends se.typedef.grpc.BlogPostServiceGrpc.BlogPostServiceImplBase {

  private static final Map<Long, Post> POSTS = new HashMap<>();
  private static final AtomicLong POST_COUNTER = new AtomicLong(1);

  @Override
  public void getPost(
      final GetPostRequest request, final StreamObserver<GetPostResponse> responseObserver) {
    if (!POSTS.containsKey(request.getId())) {
      responseObserver.onError(
          new io.grpc.StatusRuntimeException(
              Status.fromCode(Status.Code.INVALID_ARGUMENT)
                  .withDescription("No post with id " + request.getId())));
      return;
    }

    final Post post = POSTS.get(request.getId());

    final GetPostResponse response =
        GetPostResponse.newBuilder()
            .setId(post.id)
            .setAuthor(post.author)
            .setText(post.text)
            .build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void putPost(
      final PutPostRequest request, final StreamObserver<PutPostResponse> responseObserver) {
    final long id = POST_COUNTER.getAndIncrement();
    POSTS.put(id, new Post(id, request.getAuthor(), request.getText()));

    final PutPostResponse response = PutPostResponse.newBuilder().setId(id).build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void deletePost(
      final DeletePostRequest request, final StreamObserver<DeletePostResponse> responseObserver) {
    if (!POSTS.containsKey(request.getId())) {
      responseObserver.onError(
          new io.grpc.StatusRuntimeException(
              Status.fromCode(Status.Code.INVALID_ARGUMENT)
                  .withDescription("No post with id " + request.getId())));
      return;
    }

    POSTS.remove(request.getId());

    final DeletePostResponse response =
        DeletePostResponse.newBuilder().setId(request.getId()).build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }
}
