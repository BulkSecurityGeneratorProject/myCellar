package fr.hippo.mycellar.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.hippo.mycellar.domain.Category;
import fr.hippo.mycellar.domain.Vineward;
import fr.hippo.mycellar.repository.CategoryRepository;
import fr.hippo.mycellar.repository.VinewardRepository;
import fr.hippo.mycellar.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Category.
 */
@RestController
@RequestMapping("/api")
public class CategoryResource {

    private final Logger log = LoggerFactory.getLogger(CategoryResource.class);

    @Inject
    private CategoryRepository categoryRepository;

    @Inject
    private VinewardRepository vinewardRepository;

    /**
     * POST  /categorys -> Create a new category.
     */
    @RequestMapping(value = "/categorys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Category> create(@Valid @RequestBody Category category) throws URISyntaxException {
        log.debug("REST request to save Category : {}", category);
        if (category.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new category cannot already have an ID").body(null);
        }
        Category savedCategory = categoryRepository.save(category);
        return new ResponseEntity<>(savedCategory, HttpStatus.OK);
    }

    /**
     * PUT  /categorys -> Updates an existing category.
     */
    @RequestMapping(value = "/categorys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Category> update(@Valid @RequestBody Category category) throws URISyntaxException {
        log.debug("REST request to update Category : {}", category);
        if (category.getId() == null) {
            return create(category);
        }
        Category savedCategory = categoryRepository.save(category);
        return new ResponseEntity<>(savedCategory, HttpStatus.OK);
    }

    /**
     * GET  /categorys -> get all the categorys.
     */
    @RequestMapping(value = "/categorys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Category>> getAll(@RequestParam(value = "page", required = false) Integer offset,
                                                 @RequestParam(value = "per_page", required = false) Integer limit,
                                                 @RequestParam(value = "vinewardId", required = false) Long vinewardId)
        throws URISyntaxException {
        log.debug("REST request to get all categories");
        if (vinewardId != null) {
            return new ResponseEntity<>(categoryRepository.findAllByVineward(vinewardRepository.findOne(vinewardId)), HttpStatus.OK);
        }
        Page<Category> page = categoryRepository.findAllWithVineward(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/categorys", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /categorys/:id -> get the "id" category.
     */
    @RequestMapping(value = "/categorys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Category> get(@PathVariable Long id) {
        log.debug("REST request to get Category : {}", id);
        return Optional.ofNullable(categoryRepository.findOne(id))
            .map(category -> new ResponseEntity<>(
                category,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /categorys/:id -> delete the "id" category.
     */
    @RequestMapping(value = "/categorys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Category : {}", id);
        categoryRepository.delete(id);
    }
}
