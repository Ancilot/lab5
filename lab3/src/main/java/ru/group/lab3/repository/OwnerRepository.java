package ru.group.lab3.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.group.lab3.entity.Owner;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class OwnerRepository {
    private final JdbcTemplate jdbcTemplate;
    private final AddressRepository addressRepository;

    public OwnerRepository(JdbcTemplate jdbcTemplate, AddressRepository addressRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.addressRepository = addressRepository;
    }

    public List<Owner> findAll() {
        String sql = """
        select o.id, o.surname, o.name, o.patronymic, o.phone,
               a.id as address_id, a.postal_code, a.country, a.region,
               a.district, a.city, a.street, a.house, a.apartment,
               (select count(*) from cars c where c.owner_id = o.id) as cars_count
        from owners o
        join addresses a on a.id = o.address_id
        order by o.id
        """;
        return jdbcTemplate.query(sql, new OwnerMapper());
    }

    public Optional<Owner> findById(Long id) {
        String sql = """
        select o.id, o.surname, o.name, o.patronymic, o.phone,
               a.id as address_id, a.postal_code, a.country, a.region,
               a.district, a.city, a.street, a.house, a.apartment,
               (select count(*) from cars c where c.owner_id = o.id) as cars_count
        from owners o
        join addresses a on a.id = o.address_id
        where o.id = ?
        """;
        List<Owner> list = jdbcTemplate.query(sql, new OwnerMapper(), id);
        return list.stream().findFirst();
    }

    public Long save(Owner owner) {
        Long addressId = addressRepository.save(owner.getAddress());

        String sql = "insert into owners(surname, name, patronymic, phone, address_id) values (?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, owner.getSurname());
            ps.setString(2, owner.getName());
            ps.setString(3, owner.getPatronymic());
            ps.setString(4, owner.getPhone());
            ps.setLong(5, addressId);
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        if (key == null) throw new IllegalStateException("Не удалось получить ключ");
        return key.longValue();
    }

    public int deleteById(Long id) {
        Long addressId = jdbcTemplate.queryForObject(
                "select address_id from owners where id = ?", Long.class, id);

        jdbcTemplate.update("delete from cars where owner_id = ?", id);

        int rows = jdbcTemplate.update("delete from owners where id = ?", id);

        if (addressId != null) {
            jdbcTemplate.update("delete from addresses where id = ?", addressId);
        }

        return rows;
    }

}

