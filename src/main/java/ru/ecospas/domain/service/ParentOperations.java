package ru.ecospas.domain.service;

import java.util.List;

public interface ParentOperations <T> extends CrudOperations<T>{
    T getOneById(int id);
    List<T> getMany();
}
