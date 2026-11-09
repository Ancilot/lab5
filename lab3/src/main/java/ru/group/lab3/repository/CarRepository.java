package ru.group.lab3.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.group.lab3.entity.Car;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CarRepository {

    private final JdbcTemplate jdbcTemplate;

    public CarRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Car> findAll() {
        String sql = """
            select c.id, c.brand, c.plate_number, c.passport_number,
                   o.id as owner_id, o.surname, o.name, o.patronymic, o.phone,
                   a.id as address_id, a.postal_code, a.country, a.region,
                   a.district, a.city, a.street, a.house, a.apartment
            from cars c
            join owners o on o.id = c.owner_id
            join addresses a on a.id = o.address_id
            order by c.id
            """;
        return jdbcTemplate.query(sql, new CarMapper());
    }

    public Optional<Car> findById(Long id) {
        String sql = """
            select c.id, c.brand, c.plate_number, c.passport_number,
                   o.id as owner_id, o.surname, o.name, o.patronymic, o.phone,
                   a.id as address_id, a.postal_code, a.country, a.region,
                   a.district, a.city, a.street, a.house, a.apartment
            from cars c
            join owners o on o.id = c.owner_id
            join addresses a on a.id = o.address_id
            where c.id = ?
            """;
        List<Car> list = jdbcTemplate.query(sql, new CarMapper(), id);
        return list.stream().findFirst();
    }

    public Long save(Car car) {
        String sql = "insert into cars(brand, plate_number, passport_number, owner_id) values (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, car.getBrand());
            ps.setString(2, car.getPlateNumber());
            ps.setString(3, car.getPassportNumber());
            if (car.getOwner() != null && car.getOwner().getId() != null) {
                ps.setLong(4, car.getOwner().getId());
            } else {
                ps.setNull(4, Types.BIGINT);
            }
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        if (key == null) throw new IllegalStateException("Не удалось получить ключ автомобиля");
        return key.longValue();
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update("delete from cars where id = ?", id);
    }

    public List<Car> searchCars(String brand, String plate, String surname, String city) {
        StringBuilder sql = new StringBuilder("""
            select c.id, c.brand, c.plate_number, c.passport_number,
                   o.id as owner_id, o.surname, o.name, o.patronymic, o.phone,
                   a.id as address_id, a.postal_code, a.country, a.region,
                   a.district, a.city, a.street, a.house, a.apartment
            from cars c
            join owners o on o.id = c.owner_id
            join addresses a on a.id = o.address_id
            where 1=1
            """);
        List<Object> params = new ArrayList<>();
        if (brand != null)   { sql.append(" and lower(c.brand) like lower(?)");        params.add("%" + brand + "%"); }
        if (plate != null)   { sql.append(" and lower(c.plate_number) like lower(?)"); params.add("%" + plate + "%"); }
        if (surname != null) { sql.append(" and lower(o.surname) like lower(?)");      params.add("%" + surname + "%"); }
        if (city != null)    { sql.append(" and lower(a.city) like lower(?)");         params.add("%" + city + "%"); }
        sql.append(" order by c.id");
        return jdbcTemplate.query(sql.toString(), new CarMapper(), params.toArray());
    }
}