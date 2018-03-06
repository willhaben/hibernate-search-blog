package at.willhaben.test.search;


import at.willhaben.test.search.models.AdData;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AdDataRepo extends CrudRepository<AdData, Long>{

    AdData save(AdData adData);
}
