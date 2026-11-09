package ru.group.lab3.repository;

import org.springframework.jdbc.core.RowMapper;
import ru.group.lab3.entity.Address;
import ru.group.lab3.entity.Car;
import ru.group.lab3.entity.Owner;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CarMapper implements RowMapper<Car> {

    @Override
    public Car mapRow(ResultSet rs, int rowNum) throws SQLException {
        Car car = new Car();
        car.setId(rs.getLong("id"));
        car.setBrand(rs.getString("brand"));
        car.setPlateNumber(rs.getString("plate_number"));
        car.setPassportNumber(rs.getString("passport_number"));

        Owner owner = new Owner();
        owner.setId(rs.getLong("owner_id"));
        owner.setSurname(rs.getString("surname"));
        owner.setName(rs.getString("name"));
        owner.setPatronymic(rs.getString("patronymic"));
        owner.setPhone(rs.getString("phone"));

        Address a = new Address();
        a.setId(rs.getLong("address_id"));
        a.setPostalCode(rs.getString("postal_code"));
        a.setCountry(rs.getString("country"));
        a.setRegion(rs.getString("region"));
        a.setDistrict(rs.getString("district"));
        a.setCity(rs.getString("city"));
        a.setStreet(rs.getString("street"));
        a.setHouse(rs.getString("house"));
        a.setApartment(rs.getString("apartment"));
        owner.setAddress(a);

        car.setOwner(owner);
        return car;
    }
}