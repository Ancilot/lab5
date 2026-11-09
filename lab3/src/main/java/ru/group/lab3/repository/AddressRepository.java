package ru.group.lab3.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.group.lab3.entity.Address;

import java.sql.PreparedStatement;

@Repository
public class AddressRepository {

    private final JdbcTemplate jdbcTemplate;

    public AddressRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(Address address) {
        String sql = """
            insert into addresses(postal_code, country, region, district, city, street, house, apartment)
            values (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, address.getPostalCode());
            ps.setString(2, address.getCountry());
            ps.setString(3, address.getRegion());
            ps.setString(4, address.getDistrict());
            ps.setString(5, address.getCity());
            ps.setString(6, address.getStreet());
            ps.setString(7, address.getHouse());
            ps.setString(8, address.getApartment());   // null допустим
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("Не удалось получить сгенерированный ключ адреса");
        }
        return key.longValue();
    }
}