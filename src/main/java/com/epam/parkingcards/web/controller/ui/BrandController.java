package com.epam.parkingcards.web.controller.ui;

import com.epam.parkingcards.config.security.annotation.SecuredForAdmin;
import com.epam.parkingcards.model.BrandEntity;
import com.epam.parkingcards.service.BrandService;
import com.epam.parkingcards.web.mapper.BrandMapper;
import com.epam.parkingcards.web.request.BrandCreateRequest;
import com.epam.parkingcards.web.request.BrandUpdateRequest;
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
@RequestMapping("/ui/brands")
public class BrandController {

    public static final String MESSAGE_VIEW = "success-message-page";

    @Autowired
    private BrandService brandService;
    @Autowired
    private BrandMapper mapper;

    @GetMapping("/create-page")
    public String toCreatePage(Model model) {
        model.addAttribute("brandCreateRequest", new BrandCreateRequest());
        return "admin/brands/brand-create";
    }

    @PostMapping
    public String create(@Valid BrandCreateRequest request, Model model) {
        long id = brandService.create(mapper.toBrand(request));
        model.addAttribute("message", "ok, new id = " + id);
        return MESSAGE_VIEW;
    }

    @SecuredForAdmin
    @GetMapping("/page/{pageNumber}")
    public String getAll(@Valid @PathVariable int pageNumber, Model model) {
        List<BrandEntity> all = brandService.findAllActive(pageNumber);
        model.addAttribute("brands", all);
        return "admin/brands/brands-list";
    }

    @SecuredForAdmin
    @GetMapping("/update-page/{id}")
    public String toUpdatePage(@PathVariable long id, Model model) {

        BrandEntity brandEntity = brandService.findById(id);

        model.addAttribute("brandEntity", brandEntity);
        model.addAttribute("brandUpdateRequest", new BrandUpdateRequest());

        return "admin/brands/brand-edit";
    }

    @SecuredForAdmin
    @PostMapping("/edit")
    public String update(@Valid BrandUpdateRequest request, Model model) {

        BrandEntity updated = brandService.update(mapper.toBrand(request));
        model.addAttribute("message", "ok");
        return MESSAGE_VIEW;
    }

    @SecuredForAdmin
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable long id, Model model) {

        brandService.deleteSoftById(id);
        model.addAttribute("message", "ok");
        return MESSAGE_VIEW;
    }
}
