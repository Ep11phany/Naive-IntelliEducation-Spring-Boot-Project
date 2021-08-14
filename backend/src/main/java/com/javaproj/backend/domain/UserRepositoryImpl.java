package com.javaproj.backend.domain;

import com.javaproj.backend.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserRepositoryImpl implements CustomRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<User> findByName(String name) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.where(builder.like(root.get("name"), "%" + name + "%"));
        return em.createQuery(query.select(root)).getResultList();
    }
}
