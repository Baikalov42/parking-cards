package com.epam.parkingcards.web.mapper;

import com.epam.parkingcards.web.request.admin.ModelCreateRequest;
import com.epam.parkingcards.web.request.admin.ModelUpdateRequest;
import com.epam.parkingcards.web.response.ModelResponse;
import com.epam.parkingcards.model.BrandEntity;
import com.epam.parkingcards.model.ModelEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ModelMapper {

    @Autowired
    private BrandMapper brandMapper;

    public ModelEntity toModel(ModelUpdateRequest modelUpdateRequest) {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(modelUpdateRequest.getBrandId());

        ModelEntity modelEntity = new ModelEntity();
        modelEntity.setId(modelUpdateRequest.getId());
        modelEntity.setName(modelUpdateRequest.getName());
        modelEntity.setBrandEntity(brandEntity);

        return modelEntity;
    }

    public ModelEntity toModel(ModelCreateRequest modelCreateRequest) {

        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(modelCreateRequest.getBrandId());

        ModelEntity modelEntity = new ModelEntity();

        modelEntity.setName(modelCreateRequest.getName());
        modelEntity.setBrandEntity(brandEntity);

        return modelEntity;
    }

    public ModelResponse toModelResponse(ModelEntity modelEntity) {
        ModelResponse modelResponse = new ModelResponse();

        modelResponse.setId(modelEntity.getId());
        modelResponse.setName(modelEntity.getName());
        modelResponse.setBrandId(modelEntity.getBrandEntity().getId());
        modelResponse.setDeleted(modelEntity.isDeleted());

        return modelResponse;
    }

    public List<ModelResponse> toModelResponses(List<ModelEntity> modelEntities) {

        List<ModelResponse> modelResponses = new ArrayList<>();
        for (ModelEntity model : modelEntities) {
            modelResponses.add(toModelResponse(model));
        }
        return modelResponses;
    }
}
