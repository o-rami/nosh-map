package nosh.nosh_map_server.domain;

import nosh.nosh_map_server.data.CommentRepository;
import nosh.nosh_map_server.models.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
class CommentServiceTest {

    @MockBean
    CommentRepository repository;

    @Autowired
    CommentService service;

    @Test
    void shouldFindByUserId() {
        List<Comment> expected = List.of(new Comment(
                1,
                "Test of UserId",
                2,
                1,
                LocalDateTime.parse("2023-02-22T14:27:00"),
                true));

        when(repository.findByUserId(1)).thenReturn(expected);

        List<Comment> actual = service.findByUserId(1);

        assertEquals(expected.get(0), actual.get(0));
    }

    @Test
    void shouldFindByRestaurantId() {
        List<Comment> expected = List.of(
                new Comment(
                        2,
                        "Test of RestaurantId",
                        2,
                        2,
                        LocalDateTime.parse("2023-02-22T14:27:00"),
                        false));

        when(repository.findByRestaurantId(2)).thenReturn(expected);

        List<Comment> actual = service.findByRestaurantId(2);

        assertEquals(expected.get(0), actual.get(0));
    }

    @Test
    void shouldGetPublicComments() {
        List<Comment> expected = List.of(new Comment(
                1,
                "Test of Publics for RestaurantId 2",
                2,
                1,
                LocalDateTime.parse("2023-02-22T14:27:00"),
                true));

        when(repository.getPublic(2)).thenReturn(expected);

        List<Comment> actual = service.getPublic(2);

        assertEquals(expected.get(0), actual.get(0));
    }

    @Test
    void shouldAddValidComment() {
        Comment comment = new Comment(
                0,
                "Test 4",
                2,
                1,
                LocalDateTime.parse("2023-02-22T14:27:00"),
                true);
        Comment expected = new Comment(
                4,
                "Test Add 4",
                2,
                1,
                LocalDateTime.parse("2023-02-22T14:27:00"),
                true);

        when(repository.add(comment)).thenReturn(expected);

        Result<Comment> actual = service.add(comment);

        assertTrue(actual.isSuccess());
        assertEquals(expected, actual.getPayload());
    }

    @Test
    void shouldNotAddNullComment() {
        Result<Comment> result = service.add(null);

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertTrue(result.getMessages().contains("comment cannot be null."));
    }

    @Test
    void shouldNotAddWithBlankDescription() {
        Result<Comment> result = service.add(new Comment(
                4,
                "",
                2,
                1,
                LocalDateTime.parse("2023-02-22T14:27:00"),
                true));

        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("message cannot be blank."));
    }

    @Test
    void shouldNotAddWithNullDescription() {
        Result<Comment> result = service.add(new Comment(
                4,
                null,
                2,
                1,
                LocalDateTime.parse("2023-02-22T14:27:00"),
                true));

        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("message cannot be null."));
    }

    @Test
    void shouldNotAddWithInvalidRestaurantId() {
        Result<Comment> result = service.add(new Comment(
                4,
                "Testing",
                0,
                1,
                LocalDateTime.parse("2023-02-22T14:27:00"),
                true));

        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("restaurantId must be greater than 0."));
    }

    @Test
    void shouldNotAddWithInvalidUserId() {
        Result<Comment> result = service.add(new Comment(
                4,
                "Testing",
                1,
                0,
                LocalDateTime.parse("2023-02-22T14:27:00"),
                true));

        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("appUserId must be greater than 0."));
    }

    @Test
    void shouldNotAddWithFuturePostDate() {
        Result<Comment> result = service.add(new Comment(
                4,
                "Testing",
                1,
                1,
                LocalDateTime.parse("2025-02-22T14:27:00"),
                true));

        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("date cannot be in the future."));
    }

    @Test
    void shouldNotAddWithNullPostDate() {
        Result<Comment> result = service.add(new Comment(
                4,
                "Testing",
                1,
                1,
                null,
                true));

        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("date and time cannot be null."));
    }

    @Test
    void shouldUpdateComment() {
        Comment comment = new Comment(
                1,
                "Test 4",
                2,
                1,
                LocalDateTime.parse("2023-02-22T14:27:00"),
                true);

        when(repository.update(comment)).thenReturn(true);

        Result<Comment> actual = service.update(comment);

        assertTrue(actual.isSuccess());
        assertEquals(ActionStatus.SUCCESS, actual.getStatus());
    }

    @Test
    void shouldNotUpdateMissing() {
        Comment comment = new Comment(
                99,
                "Test 4",
                2,
                1,
                LocalDateTime.parse("2023-02-22T14:27:00"),
                true);

        when(repository.update(comment)).thenReturn(false);

        Result<Comment> actual = service.update(comment);

        assertFalse(actual.isSuccess());
        assertEquals(ActionStatus.NOT_FOUND, actual.getStatus());
    }


    @Test
    void shouldNotUpdateWhenInvalid() {
        Comment comment = new Comment(
                1,
                null,
                0,
                1,
                LocalDateTime.parse("2023-02-22T14:27:00"),
                true);

        Result<Comment> actual = service.update(comment);

        assertFalse(actual.isSuccess());
        assertEquals(ActionStatus.INVALID, actual.getStatus());
    }

    @Test
    void shouldDeleteById() {
        when(repository.deleteById(anyInt())).thenReturn(true);
        assertEquals(ActionStatus.SUCCESS, service.deleteById(2).getStatus());
    }
}