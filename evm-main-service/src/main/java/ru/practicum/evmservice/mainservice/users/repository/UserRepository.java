package ru.practicum.evmservice.mainservice.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.evmservice.mainservice.users.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select u from User as u order by u.id limit :limit offset :offset")
    List<User> findAllWithOffsetAndLimit(int offset, int limit);

    @Query("select u from User as u where u.id in :ids order by u.id limit :limit offset :offset")
    List<User> findAllByIdsInWithOffsetAndLimit(Integer[] ids, int offset, int limit);
}
