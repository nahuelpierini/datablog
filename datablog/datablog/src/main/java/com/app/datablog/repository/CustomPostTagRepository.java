package com.app.datablog.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CustomPostTagRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void deleteTagReferencesByTagId(Long tagId) {
        String sql = "DELETE FROM post_tag WHERE id_tag = :tagId";
        entityManager.createNativeQuery(sql)
                .setParameter("tagId", tagId)
                .executeUpdate();
    }

    @Transactional
    public void deleteTagsByPostId(Long postId) {
        String sql = "DELETE FROM post_tag WHERE id_post = :postId";
        entityManager.createNativeQuery(sql)
                .setParameter("postId", postId)
                .executeUpdate();
    }

}

