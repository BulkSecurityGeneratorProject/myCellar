package fr.hippo.mycellar.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.hippo.mycellar.domain.Appellation;
import fr.hippo.mycellar.repository.AppellationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Appellation.
 */
@RestController
@RequestMapping("/api")
public class AppellationResource {

    private final Logger log = LoggerFactory.getLogger(AppellationResource.class);

    @Inject
    private AppellationRepository appellationRepository;

    /**
     * POST  /appellations -> Create a new appellation.
     */
    @RequestMapping(value = "/appellations",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Appellation appellation) throws URISyntaxException {
        log.debug("REST request to save Appellation : {}", appellation);
        if (appellation.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new appellation cannot already have an ID").build();
        }
        appellationRepository.save(appellation);
        return ResponseEntity.created(new URI("/api/appellations/" + appellation.getId())).build();
    }

    /**
     * PUT  /appellations -> Updates an existing appellation.
     */
    @RequestMapping(value = "/appellations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Appellation appellation) throws URISyntaxException {
        log.debug("REST request to update Appellation : {}", appellation);
        if (appellation.getId() == null) {
            return create(appellation);
        }
        appellationRepository.save(appellation);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /appellations -> get all the appellations.
     */
    @RequestMapping(value = "/appellations",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Appellation> getAll() {
        log.debug("REST request to get all Appellations");
        return appellationRepository.findAll();
    }

    /**
     * GET  /appellations/:id -> get the "id" appellation.
     */
    @RequestMapping(value = "/appellations/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Appellation> get(@PathVariable Long id) {
        log.debug("REST request to get Appellation : {}", id);
        return Optional.ofNullable(appellationRepository.findOne(id))
            .map(appellation -> new ResponseEntity<>(
                appellation,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /appellations/:id -> delete the "id" appellation.
     */
    @RequestMapping(value = "/appellations/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Appellation : {}", id);
        appellationRepository.delete(id);
    }
}
