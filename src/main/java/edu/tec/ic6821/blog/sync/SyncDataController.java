package edu.tec.ic6821.blog.sync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/admin/api/external")
public class SyncDataController {

    private final Logger logger = LoggerFactory.getLogger(SyncDataController.class);

    private final SyncDataService syncDataService;

    public SyncDataController(final SyncDataService syncDataService) {
        this.syncDataService = syncDataService;
    }

    @PutMapping
    public final SyncDataResultDTO sync()  {
        try {
            logger.info("[sync()] Pulling data from external system");
            final SyncDataResult result = syncDataService.sync();
            logger.info(String.format("[sync()] Synchronization finished: saved %,d users , %,d posts",
                result.getCreatedUsersCount(), result.getCreatedPostsCount()));

            return SyncDataResultDTO.from(result);
        } catch (ResponseStatusException rse) {
            throw rse;
        } catch (Exception e) {
            logger.error("[sync()] Unexpected error ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                String.format("%s: %s", e.getClass().getName(), e.getMessage()), e);
        }
    }
}
