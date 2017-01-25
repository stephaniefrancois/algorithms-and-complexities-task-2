package com.francois.algo.pdb.data;

import com.francois.algo.pdb.core.domain.AppException;

import java.util.List;

public interface IRepository<TEntity> {
    List<TEntity> getAll() throws AppException;
}
