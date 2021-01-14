package com.epam.parkingcards.web.controller.ui;

import com.epam.parkingcards.config.security.annotation.SecuredForAdmin;
import com.epam.parkingcards.model.BrandEntity;
import com.epam.parkingcards.service.BrandService;
import com.epam.parkingcards.web.mapper.BrandMapper;
import com.epam.parkingcards.web.request.BrandCreateRequest;
import com.epam.parkingcards.web.request.BrandUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/ui/brands")
@SecuredForAdmin
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

    @GetMapping("/page/{pageNumber}")
    public String getAll(@Valid @PathVariable int pageNumber, Model model) {
        pageNumber--;
        Page<BrandEntity> all = brandService.findAllActive(pageNumber);
        model.addAttribute("brands", all);
        int totalPages = all.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "admin/brands/brands-list";
    }

    @GetMapping("/update-page/{id}")
    public String toUpdatePage(@PathVariable long id, Model model) {

        BrandEntity brandEntity = brandService.findById(id);

        model.addAttribute("brandEntity", brandEntity);
        model.addAttribute("brandUpdateRequest", new BrandUpdateRequest());

        return "admin/brands/brand-edit";
    }


    @PostMapping("/edit")
    public String update(@Valid BrandUpdateRequest request, Model model) {

        BrandEntity updated = brandService.update(mapper.toBrand(request));
        model.addAttribute("message", "ok");
        return MESSAGE_VIEW;
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable long id, Model model) {

        brandService.deleteSoftById(id);
        model.addAttribute("message", "ok");
        return MESSAGE_VIEW;
    }
}
