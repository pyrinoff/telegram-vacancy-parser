package ru.pyrinoff.chatjobparser.repository;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.pyrinoff.chatjobparser.model.dto.AbstractDTOModel;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public abstract class AbstractDTORepository<M extends AbstractDTOModel> {

    @Getter
    @PersistenceContext
    protected @NotNull EntityManager entityManager;

    abstract protected Class<M> getEntityClass();

    @NotNull
    public M add(@NotNull final M model) {
        entityManager.persist(model);
        return model;
    }

    public void clear() {
        @NotNull final String jpql = "DELETE FROM " + getEntityClass().getSimpleName() + " m";
        entityManager.createQuery(jpql).executeUpdate();
    }

    public boolean existsById(@NotNull final String id) {
        return findOneById(id) != null;
    }

    @Nullable
    public List<M> findAll() {
        @NotNull final String jpql = "SELECT m FROM " + getEntityClass().getSimpleName() + " m";
        return entityManager.createQuery(jpql, getEntityClass()).getResultList();
    }

    @Nullable
    public M findOneById(@NotNull final String id) {
        return entityManager.find(getEntityClass(), id);
    }

    public int getSize() {
        @NotNull final String jpql = "SELECT COUNT(m) FROM " + getEntityClass().getSimpleName() + " m";
        return entityManager.createQuery(jpql, Long.class).getSingleResult().intValue();
    }

    public void remove(@NotNull final M model) {
        entityManager.remove(entityManager.contains(model) ? model : entityManager.merge(model));
    }

    public void update(@NotNull final M model) {
        entityManager.merge(model);
    }

}
