package nosh.nosh_map_server.data;

import nosh.nosh_map_server.models.Comment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class CommentJdbcTemplateRepositoryTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CommentJdbcTemplateRepository repository;

    @BeforeEach
    void setup() {
        jdbcTemplate.update("call set_known_good_state();");
    }

    @Test
    void shouldFindGetchuCommentById() {
        Comment comment = repository.findByCommentId(3);

        assertEquals("Getchu some.", comment.getDescription());
        assertTrue(comment.isPublic());
        assertEquals(1, comment.getAppUserId());
        assertEquals(2, comment.getRestaurantId());
    }

    @Test
    void shouldFind2CommentsByUserId() {
        List<Comment> comments = repository.findByUserId(2);

        assertTrue(comments.size() >= 1);
        assertTrue(comments.stream()
                .anyMatch(c -> c.getCommentId() == 1 && c.getDescription().equalsIgnoreCase("I'll be back...")));
    }

    @Test
    void shouldFind2CommentsByRestaurantId() {
        List<Comment> comments = repository.findByRestaurantId(1);

        assertTrue(comments.size() >= 2);

        assertTrue(comments.stream()
                .anyMatch(c -> c.getCommentId() == 2 && c.getDescription().equalsIgnoreCase("It was okay.")));
    }

    @Test
    void shouldFind2PublicComments() {
        List<Comment> comments = repository.getPublic(1);

        assertTrue(comments.size() >= 2);

        assertTrue(comments.stream()
                .anyMatch(c -> c.getCommentId() == 2 && c.getDescription().equalsIgnoreCase("It was okay.")));
    }

    @Test
    void shouldAddComment4() {
        Comment comment = new Comment(
                0,
                "Test 4",
                2,
                1,
                LocalDateTime.parse("2023-02-22T14:27:00"),
                true);

        Comment expected = new Comment(
                4,
                "Test 4",
                2,
                1,
                LocalDateTime.parse("2023-02-22T14:27:00"),
                true);

        Comment actual = repository.add(comment);

        assertEquals(expected, actual);
    }

    @Test
    void shouldUpdateComment2() {
        Comment okay = repository.findByCommentId(2);

        assertEquals("It was okay.", okay.getDescription());

        okay.setDescription("silly new test description");

        assertTrue(repository.update(okay));

        Comment updated = repository.findByCommentId(2);
        assertEquals("silly new test description", updated.getDescription());
    }

    @Test
    void shouldNotUpdateMissing() {
        Comment comment = new Comment(
                99,
                "Failed",
                2,
                2,
                LocalDateTime.parse("2023-02-22T14:27:00"),
                false);

        assertFalse(repository.update(comment));
    }

    @Test
    void shouldDeleteById() {
        assertTrue(repository.deleteById(3));
        assertFalse(repository.deleteById(3));
    }
}