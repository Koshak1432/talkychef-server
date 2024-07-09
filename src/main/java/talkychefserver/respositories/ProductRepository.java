package talkychefserver.respositories;

import org.springframework.data.repository.CrudRepository;
import talkychefserver.model.entities.AvgMark;
import talkychefserver.model.entities.Product;
import talkychefserver.model.entities.Role;

import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
