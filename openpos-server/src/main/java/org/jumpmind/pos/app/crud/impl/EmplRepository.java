package org.jumpmind.pos.app.crud.impl;

import org.jumpmind.pos.app.crud.IEmplRepository;
import org.jumpmind.pos.app.model.Empl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class EmplRepository extends AbstractRepository implements IEmplRepository {
    
    @Override
    public <S extends Empl> S save(S entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterable<Empl> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long count() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void delete(Empl entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub

    }

    @Override
    public Iterable<Empl> findAll(Sort sort) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Page<Empl> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Empl> Iterable<S> save(Iterable<S> entities) {
        return null;
    }

    @Override
    public Empl findOne(Long id) {
        return null;
    }

    @Override
    public boolean exists(Long id) {
        return false;
    }

    @Override
    public Iterable<Empl> findAll(Iterable<Long> ids) {
        return null;
    }

    @Override
    public void delete(Long id) {
    }

    @Override
    public void delete(Iterable<? extends Empl> entities) {
    }

}
