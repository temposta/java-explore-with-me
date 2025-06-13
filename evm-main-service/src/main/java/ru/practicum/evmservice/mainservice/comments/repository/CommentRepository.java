package ru.practicum.evmservice.mainservice.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.evmservice.mainservice.comments.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("select c " +
           "from Comment as c " +
           "where c.user.id = :userId " +
           "order by c.createdDate " +
           "limit :size " +
           "offset :from ")
    List<Comment> findUserComments(int userId, int from, int size);
}
