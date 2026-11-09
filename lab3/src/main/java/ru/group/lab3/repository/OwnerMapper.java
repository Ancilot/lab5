package ru.group.lab3.repository;

import ru.group.lab3.entity.Address;
import ru.group.lab3.entity.Car;
import ru.group.lab3.entity.Owner;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

class OwnerMapper implements RowMapper<Owner> {
    @Override
    public Owner mapRow(ResultSet rs, int rowNum) throws SQLException {
        Owner owner = new Owner();
        owner.setId(rs.getLong("id"));
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
        long carsCount = rs.getLong("cars_count");
        Car placeholder = null;
        for (long i = 0; i < carsCount; i++) {
            owner.getCars().add(new Car());
        }
        return owner;
    }
}
