package ru.pyrinoff.chatjobparser.service;

import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import ru.pyrinoff.chatjobparser.exception.model.dto.IdEmptyException;
import ru.pyrinoff.chatjobparser.model.dto.AbstractDTOModel;
import ru.pyrinoff.chatjobparser.repository.AbstractDTORepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Comparator;
import java.util.List;

public abstract class AbstractDTOService<M extends AbstractDTOModel, R extends AbstractDTORepository<M>> {

    @Getter
    @PersistenceContext
    protected @NotNull EntityManager entityManager;

    @Getter
    @Autowired
    protected @NotNull ApplicationContext context;

    protected abstract @NotNull AbstractDTORepository<M> getRepository();

    @NotNull
    @SneakyThrows
    @Transactional
    public M add(@NotNull final M model) {
        getRepository().add(model);
        return model;
    }

    @SneakyThrows
    @Transactional
    public void clear() {
        getRepository().clear();
    }

    @SneakyThrows
    public boolean existsById(@Nullable final String id) {
        if (id == null || id.isEmpty()) return false;
        return getRepository().existsById(id);
    }

    @SneakyThrows
    public @Nullable List<M> findAll() {
        return getRepository().findAll();
    }

    @Nullable
    @SneakyThrows
    public M findOneById(@Nullable final String id) {
        if (id == null || id.isEmpty()) throw new IdEmptyException();
        return getRepository().findOneById(id);
    }

    public int getSize() throws Exception {
        return getRepository().getSize();
    }

    @Transactional
    public void remove(@Nullable final M model) throws Exception {
        getRepository().remove(model);
    }

    @Transactional
    public void removeById(@Nullable final String id) throws Exception {
        @Nullable M result = findOneById(id);
        remove(result);
    }

    @Transactional
    public void update(@Nullable final M model) throws Exception {
        if (model == null) return;
        getRepository().update(model);
    }

}
