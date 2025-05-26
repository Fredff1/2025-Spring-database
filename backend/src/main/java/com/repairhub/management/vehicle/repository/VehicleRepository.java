package com.repairhub.management.vehicle.repository;

import java.util.List;
import java.util.Optional;

import com.repairhub.management.vehicle.entity.Vehicle;

public interface VehicleRepository {
    /** 新增一辆车 */
    int insert(Vehicle v);
    /** 更新车辆信息（品牌、车型、车牌、注册日期） */
    int update(Vehicle v);
    /** 删除 */
    int deleteById(Long vehicleId);
    /** 根据主键查 */
    Optional<Vehicle> findById(Long vehicleId);
    /** 查询某个用户的所有车辆 */
    List<Vehicle> findByOwner(Long ownerId);
    /** 查询所有车辆 */
    List<Vehicle> findAll();
    /** 按品牌查询 */
    List<Vehicle> findByBrand(String brand);
    /** 按车型查询 */
    List<Vehicle> findByModel(String model);
    /** 按车牌号查询 */
    Optional<Vehicle> findByLicensePlate(String licensePlate);
}
