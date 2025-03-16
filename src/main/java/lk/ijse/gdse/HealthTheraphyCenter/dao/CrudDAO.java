package lk.ijse.gdse.HealthTheraphyCenter.dao;

import lk.ijse.gdse.HealthTheraphyCenter.Entity.SuperEntity;

import java.util.List;
import java.util.Optional;

public interface CrudDAO<T extends SuperEntity, ID> extends SuperDAO {
    public boolean save(T t);

    public boolean update(T t);

    public boolean deleteByPK(ID pk) throws Exception;

    public List<T> getAll();

    public Optional<T> findByPK(ID pk);

    public Optional<String> getLastPK();
}
