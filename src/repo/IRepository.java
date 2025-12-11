package repo;

import java.util.Optional;

public interface IRepository<ID, T> {

    void add(ID id, T entity) throws RepositoryException;

    Optional<T> delete(ID id);

    void modify(ID id, T entity) throws RepositoryException;

    Optional<T> findById(ID id);

    Iterable<T> getAll();
}
