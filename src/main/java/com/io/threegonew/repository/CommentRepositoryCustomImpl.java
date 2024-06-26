package com.io.threegonew.repository;

import com.io.threegonew.domain.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.io.threegonew.domain.QComment.comment;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Integer maxGroup(Long reviewId) {
        Integer maxGroupNum = jpaQueryFactory
                .select(comment.group.max())
                .from(comment)
                .where(comment.reviewId.eq(reviewId))
                .fetchFirst();
        return maxGroupNum == null ? 0 : maxGroupNum;
    }

    @Override
    public Integer maxOrder(Long reviewId, Integer group) {
        Integer maxOrderNum = jpaQueryFactory
                .select(comment.order.max())
                .from(comment)
                .where(
                        comment.reviewId.eq(reviewId)
                                .and(comment.group.eq(group)))
                .fetchFirst();
        return maxOrderNum == null ? 0 : maxOrderNum;
    }

    @Override
    public Page<Comment> findComments(Pageable pageable, Long reviewId) {
        List<Comment> commentList = jpaQueryFactory
                .select(comment)
                .from(comment)
                .where(
                        comment.reviewId.eq(reviewId)
                                .and(comment.order.eq(1)))
                .orderBy(comment.group.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(commentList, pageable, countComments(reviewId));
    }

    @Override
    public Long countComments(Long reviewId) {
        Long count = jpaQueryFactory
                .select(comment.count())
                .from(comment)
                .where(
                        comment.reviewId.eq(reviewId)
                        .and(comment.order.eq(1)))
                .fetchFirst();

        return count;
    }

    @Override
    public List<Comment> findRecentComments(Long reviewId, LocalDateTime lastRegDate) {
        List<Comment> recentCommentList = jpaQueryFactory
                .select(comment)
                .from(comment)
                .where(
                        comment.reviewId.eq(reviewId)
                                .and(comment.order.eq(1))
                                .and(comment.regDate.gt(lastRegDate)))
                .orderBy(comment.group.asc())
                .fetch();

        return recentCommentList;
    }

    @Override
    public Boolean existNewComments(Long reviewId, LocalDateTime lastRegDate) {
        return jpaQueryFactory
                .select(comment.commentId)
                .from(comment)
                .where(
                        comment.reviewId.eq(reviewId)
                                .and(comment.order.eq(1))
                                .and(comment.regDate.gt(lastRegDate)))
                .fetchFirst() != null;
    }

    @Override
    public Page<Comment> findReplies(Pageable pageable, Long reviewId, Integer group) {
        List<Comment> replyList = jpaQueryFactory
                .select(comment)
                .from(comment)
                .where(
                        comment.reviewId.eq(reviewId)
                                .and(comment.group.eq(group))
                                .and(comment.order.gt(1)))
                .orderBy(comment.order.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(replyList, pageable, countReplies(reviewId, group));
    }

    @Override
    public Long countReplies(Long reviewId, Integer group) {
        Long count = jpaQueryFactory
                .select(comment.count())
                .from(comment)
                .where(
                        comment.reviewId.eq(reviewId)
                                .and(comment.group.eq(group))
                                .and(comment.order.gt(1)))
                .fetchFirst();

        return count;
    }
}
