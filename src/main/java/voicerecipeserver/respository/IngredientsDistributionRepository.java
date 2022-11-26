package voicerecipeserver.respository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import voicerecipeserver.model.entities.IngredientsDistribution;
import voicerecipeserver.model.entities.IngredientsDistributionKey;

@Repository
public interface IngredientsDistributionRepository extends CrudRepository<IngredientsDistribution, IngredientsDistributionKey> {
}
