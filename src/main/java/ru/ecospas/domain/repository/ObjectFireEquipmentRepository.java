package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecospas.domain.model.ObjectFireEquipment;

import java.util.List;

public interface ObjectFireEquipmentRepository extends JpaRepository<ObjectFireEquipment, Integer> {

    List<ObjectFireEquipment> findAllByObjectIdOrderByNumber(Integer objectId);

}