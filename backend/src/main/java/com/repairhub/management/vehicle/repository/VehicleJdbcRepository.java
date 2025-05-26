package com.repairhub.management.vehicle.repository;


import org.springframework.boot.autoconfigure.task.TaskExecutionProperties.Simple;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.repairhub.management.vehicle.entity.Vehicle;
import com.repairhub.management.vehicle.entity.VehicleRowMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class VehicleJdbcRepository implements VehicleRepository {
    private final NamedParameterJdbcTemplate jdbc;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final VehicleRowMapper mapper = new VehicleRowMapper();

    public VehicleJdbcRepository(
        NamedParameterJdbcTemplate jdbc,
        SimpleJdbcInsert simpleJdbcInsert) {
        this.jdbc = jdbc;
        this.simpleJdbcInsert = simpleJdbcInsert
            .withTableName("vehicle")
            .usingGeneratedKeyColumns("vehicle_id");
    }

    @Override
    public int insert(Vehicle vehicle) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(vehicle);
        Number key = simpleJdbcInsert.executeAndReturnKey(params);
        long vehicleId = key.longValue();
        vehicle.setVehicleId(vehicleId);
        return 1;
    }

    @Override
    public int update(Vehicle v) {
        String sql = """
            UPDATE vehicle
               SET owner_id      = :ownerId,
                   brand         = :brand,
                   model         = :model,
                   license_plate = :licensePlate,
                   register_date = :registerDate
             WHERE vehicle_id = :vehicleId
            """;
        return jdbc.update(sql, new BeanPropertySqlParameterSource(v));
    }

    @Override
    public int deleteById(Long vehicleId) {
        String sql = "DELETE FROM vehicle WHERE vehicle_id = :vehicleId";
        return jdbc.update(sql, Map.of("vehicleId", vehicleId));
    }

    @Override
    public Optional<Vehicle> findById(Long vehicleId) {
        String sql = "SELECT * FROM vehicle WHERE vehicle_id = :vehicleId";
        return jdbc.query(sql, Map.of("vehicleId", vehicleId), mapper)
                   .stream().findFirst();
    }

    @Override
    public List<Vehicle> findByOwner(Long ownerId) {
        String sql = "SELECT * FROM vehicle WHERE owner_id = :ownerId";
        return jdbc.query(sql, Map.of("ownerId", ownerId), mapper);
    }

    @Override
    public List<Vehicle> findAll() {
        String sql = "SELECT * FROM vehicle";
        return jdbc.query(sql, Map.of(), mapper);
    }

    @Override
    public List<Vehicle> findByBrand(String brand) {
        String sql = "SELECT * FROM vehicle WHERE brand = :brand";
        return jdbc.query(sql, Map.of("brand", brand), mapper);
    }

    @Override
    public List<Vehicle> findByModel(String model) {
        String sql = "SELECT * FROM vehicle WHERE model = :model";
        return jdbc.query(sql, Map.of("model", model), mapper);
    }

    @Override
    public Optional<Vehicle> findByLicensePlate(String licensePlate) {
        String sql = "SELECT * FROM vehicle WHERE license_plate = :licensePlate";
        return jdbc.query(sql, Map.of("licensePlate", licensePlate), mapper)
                   .stream().findFirst();
    }
}
