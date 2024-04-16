package com.io.threegonew.repository;

import com.io.threegonew.domain.Follow;
import com.io.threegonew.domain.User;
import com.io.threegonew.dto.FollowDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    // a가 b를 팔로우 하는 걸 찾기
    Optional<Follow> findByToUserAndFromUser(User toUser, User fromUser);

    // a가 b를 팔로우하는지 확인하기
    boolean existsByToUserAndFromUser(User toUser, User fromUser);

    // a가 팔로우한 유저의 리스트를 뽑기
    List<Follow> findByToUser(User toUser);

    // b를 팔로우한 유저의 리스트를 뽑기
    List<Follow> findByFromUser(User fromUser);

    // 팔로우 state 가 1이면 팔로우 되어있는 상태 0이면 팔로우 되어있지 않은 상태
    @Query(value = "SELECT to_user_id =:loginId, from_user_id =:pageId, (SELECT COUNT(*) FROM follows f1 WHERE f1.from_user_id = f2.to_user_id AND f1.to_user_id = f2.to_user_id) from follows f2", nativeQuery = true)
    int countFollowState(@Param("toUser") String loginId, @Param("fromUser") String pageId);

    @Query(value = "SELECT COUNT(*) FROM follows WHERE to_user_id =:pageId", nativeQuery = true)
    int countFollowing(@Param("toUser") String pageId);

    @Query(value = "SELECT COUNT(*) FROM follows WHERE from_user_id =:pageId", nativeQuery = true)
    int countFollower(@Param("fromUser") String pageId);


    @Query(value = "SELECT f1.*, " +
            "CASE WHEN f2.from_user_id IS NOT NULL THEN 1 ELSE 0 END AS follow_state, " +
            "CASE WHEN f1.from_user_id = :loggedInId THEN 1 ELSE 0 END AS same_user_state " +
            "FROM follows f1 " +
            "LEFT JOIN follows f2 ON f1.from_user_id = f2.from_user_id AND f2.to_user_id = :loggedInId " +
            "WHERE f1.to_user_id = :myPageId", nativeQuery = true)
    List<Follow> ShowFollowingList(@Param("loggedInId") String loggedInId, @Param("myPageId") String myPageId);

    @Query(value = "SELECT f1.*, " +
            "CASE WHEN f2.to_user_id IS NOT NULL THEN 1 ELSE 0 END AS follow_state, " +
            "CASE WHEN f2.to_user_id = :loggedInId THEN 1 ELSE 0 END AS same_user_state " +
            "FROM follows f1 " +
            "LEFT JOIN follows f2 ON f1.to_user_id = f2.from_user_id AND f2.to_user_id = :loggedInId " +
            "WHERE f1.from_user_id = :myPageId", nativeQuery = true)
    List<Follow> showFollowerList(@Param("loggedInId") String loggedInId, @Param("myPageId") String myPageId);
}