package dcom.refrigerator.api.domain.food.repositroy;

import dcom.refrigerator.api.domain.food.FoodDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodDocumentRepository extends ElasticsearchRepository<FoodDocument, Integer> {
}
