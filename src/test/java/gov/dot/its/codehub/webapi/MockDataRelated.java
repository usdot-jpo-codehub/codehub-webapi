package gov.dot.its.codehub.webapi;

import java.util.ArrayList;
import java.util.List;

import gov.dot.its.codehub.webapi.model.RelatedItemModel;
import gov.dot.its.codehub.webapi.model.RelatedModel;

public class MockDataRelated {

	public RelatedModel generatedFakeRelatedModel() {
		RelatedModel relatedModel = new RelatedModel();
		relatedModel.setId("id");
		relatedModel.setName("name");
		relatedModel.setUrls(generateFakeRelatedItemModels());

		return relatedModel;
	}

	public List<RelatedItemModel> generateFakeRelatedItemModels() {
		List<RelatedItemModel> relatedItems = new ArrayList<>();
		for(int i=1; i<=3; i++) {
			RelatedItemModel relatedItem = generateFakeRelatedItem(i);
			relatedItems.add(relatedItem);
		}
		return relatedItems;
	}

	public RelatedItemModel generateFakeRelatedItem(int index) {
		RelatedItemModel relatedItem = new RelatedItemModel();
		relatedItem.setId(String.format("id-%s", index));
		relatedItem.setName(String.format("name-%s", index));
		relatedItem.setUrl(String.format("http://related-%s.url", index));
		return relatedItem;
	}
}
