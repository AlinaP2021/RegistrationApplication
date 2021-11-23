package sbregapp.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sbregapp.model.Country;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;


@Repository
public class CountryDAO {

    @Autowired
    private EntityManager entityManager;

    public CountryDAO() {

    }

    public Country findCountryByCode(String countryCode) {
        return entityManager.find(Country.class, countryCode);
    }

    public List<Country> getCountries() {
        String sql = "select * from countries";
        Query query = entityManager.createNativeQuery(sql, Country.class);
        return query.getResultList();
    }
}
