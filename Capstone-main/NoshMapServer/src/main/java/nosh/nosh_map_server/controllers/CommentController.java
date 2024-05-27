package nosh.nosh_map_server.controllers;

import nosh.nosh_map_server.domain.CommentService;
import nosh.nosh_map_server.domain.Result;
import nosh.nosh_map_server.models.Comment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService service;

    public CommentController(CommentService service) {
        this.service = service;
    }

    @GetMapping("/commentId/{commentId}")
    public ResponseEntity<Comment> findbyCommentId(@PathVariable int commentId) {
        Comment comment = service.findByCommentId(commentId);
        if (comment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/appUserId/{appUserId}")
    public List<Comment> findbyUserId(@PathVariable int appUserId) {
        return service.findByUserId(appUserId);
    }

    @GetMapping("/restaurantId/{restaurantId}")
    public List<Comment> findbyRestaurantId(@PathVariable int restaurantId) {
        return service.findByRestaurantId(restaurantId);
    }

    @GetMapping("/public/{restaurantId}")
    public List<Comment> getPublic(@PathVariable int restaurantId) {
        return service.getPublic(restaurantId);
    }

    @PostMapping
    public ResponseEntity<Comment> add(@RequestBody Comment comment) {
        System.out.println("COMMENT " + comment);
        Result<Comment> result = service.add(comment);
        return new ResponseEntity<>(result.getPayload(), getStatus(result, HttpStatus.CREATED));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Void> update(@PathVariable int commentId, @RequestBody Comment comment) {
        if (comment != null && comment.getCommentId() != commentId) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Result<Comment> result = service.update(comment);
        return new ResponseEntity<>(getStatus(result, HttpStatus.NO_CONTENT));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteById(@PathVariable int commentId) {
        Result<Comment> result = service.deleteById(commentId);
        return new ResponseEntity<>(getStatus(result, HttpStatus.NO_CONTENT));
    }

    private HttpStatus getStatus(Result<Comment> result, HttpStatus statusDefault) {
        switch (result.getStatus()) {
            case INVALID:
                return HttpStatus.BAD_REQUEST;
            case DUPLICATE:
                return HttpStatus.FORBIDDEN;
            case NOT_FOUND:
                return HttpStatus.NOT_FOUND;
        }
        return statusDefault;
    }
}
