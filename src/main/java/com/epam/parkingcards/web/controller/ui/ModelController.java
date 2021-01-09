package com.epam.parkingcards.web.controller.ui;

import com.epam.parkingcards.config.security.annotation.SecuredForAdmin;
import com.epam.parkingcards.model.ModelEntity;
import com.epam.parkingcards.service.ModelService;
import com.epam.parkingcards.web.mapper.ModelMapper;
import com.epam.parkingcards.web.request.ModelCreateRequest;
import com.epam.parkingcards.web.request.ModelUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/ui/models")
public class ModelController {

    public static final String MESSAGE_VIEW = "success-message-page";

    @Autowired
    private ModelService modelService;
    @Autowired
    private ModelMapper mapper;

    @GetMapping("/create-page")
    public String toCreatePage(Model model) {
        model.addAttribute("modelCreateRequest", new ModelCreateRequest());
        return "admin/models/model-create";
    }

    @PostMapping
    public String create(@Valid ModelCreateRequest request, Model model) {
        long id = modelService.create(mapper.toModel(request));
        model.addAttribute("message", "ok, new id = " + id);
        return MESSAGE_VIEW;
    }

    @SecuredForAdmin
    @GetMapping("/page/{pageNumber}")
    public String getAll( @PathVariable int pageNumber, Model model) {
        List<ModelEntity> all = modelService.findAllActive(pageNumber);
        model.addAttribute("models", all);
        return "admin/models/models-list";
    }

    @SecuredForAdmin
    @GetMapping("/update-page/{id}")
    public String toUpdatePage(@PathVariable long id, Model model) {

        ModelEntity modelEntity = modelService.findById(id);

        model.addAttribute("modelEntity", modelEntity);
        model.addAttribute("modelUpdateRequest", new ModelUpdateRequest());

        return "admin/models/model-edit";
    }

    @SecuredForAdmin
    @PostMapping("/edit")
    public String update(@Valid ModelUpdateRequest request, Model model) {

        ModelEntity updated = modelService.update(mapper.toModel(request));
        model.addAttribute("message", "ok");
        return "success-message-page";
    }

    @SecuredForAdmin
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable long id, Model model) {

        modelService.deleteSoftById(id);
        model.addAttribute("message", "ok");
        return "success-message-page";
    }
}
