package com.caseo.domain.service;

import java.util.List;

public interface ChildOperations<T> extends CrudOperations<T> {
    List<T> getManyByParentId(int parentId);
    T getOneByParentId(int parentId);
}
