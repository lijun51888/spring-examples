package hello.dao.oneToMany.impl;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import hello.BaseTest;
import hello.container.FieldHolder;
import hello.container.OrderType;
import hello.container.QueryParams;
import hello.dao.oneToMany.CommentDao;
import hello.entity.oneToMany.Comment;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@DatabaseSetup({"/post_comment_author.xml"})
public class CommentDaoImplTest extends BaseTest {

    @Autowired
    private CommentDao commentDao;

    @Test
    public void findByPostId() {
        List<Comment> comments = commentDao.findByPostId(1L);

        assertThat(comments.size(), equalTo(2));
        for(Comment comment : comments) {
            assertThat(comment.getPost().getId(), equalTo(1L));
        }
    }

    @Test
    public void findNotInPassedIds() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        ids.add(3L);
        ids.add(4L);
        ids.add(5L);
        ids.add(6L);
        ids.add(7L);
        List<Comment> comments = commentDao.findNotInPassedIds(ids);

        assertThat(comments.size(), equalTo(1));
        Comment comment = comments.iterator().next();
        assertThat(comment.getId(), equalTo(8L));
    }

    @Test
    public void findLikeName() {
        List<Comment> comments = commentDao.findLikeName("omm");

        assertThat(comments.size(), equalTo(7));
    }

    @Test
    public void getByProps_WhenFieldEqualNull() {
        Map<String, List<?>> props = new HashMap<>();
        props.put("name", singletonList(null));
        List<Comment> comments = commentDao.getByProps(props);

        assertThat(comments.size(), equalTo(1));
        for(Comment comment : comments) {
            assertThat(comment.getId(), equalTo(5L));
        }
    }

    @Test
    public void getByProps_WhenFieldEqualNullAnNonNull() {
        Map<String, List<?>> props = new HashMap<>();
        props.put("name", asList(null, "Comment#1"));
        List<Comment> comments = commentDao.getByProps(props);

        assertThat(comments.size(), equalTo(2));
        assertThat(comments, containsInAnyOrder(
                hasProperty("id", is(1L)),
                hasProperty("id", is(5L))
        ));
    }

    @Test
    public void getByProps_WhenFieldPostNameIsNull() {
        Map<String, List<?>> props = new HashMap<>();
        props.put("post.name", singletonList(null));
        List<Comment> comments = commentDao.getByProps(props);

        assertThat(comments.size(), equalTo(3));
        assertThat(comments, containsInAnyOrder(
                hasProperty("id", is(6L)),
                hasProperty("id", is(7L)),
                hasProperty("id", is(8L))
        ));
    }

    @Test
    public void getByProps_WhenFieldPostNameIsNullAndNonNull() {
        Map<String, List<?>> props = new HashMap<>();
        props.put("post.name", asList(null, "Post#2"));
        List<Comment> comments = commentDao.getByProps(props);

        assertThat(comments.size(), equalTo(6));
        assertThat(comments, containsInAnyOrder(
                hasProperty("id", is(3L)),
                hasProperty("id", is(4L)),
                hasProperty("id", is(5L)),
                hasProperty("id", is(6L)),
                hasProperty("id", is(7L)),
                hasProperty("id", is(8L))
        ));
    }

    @Test
    public void getByProps_WhenId() {
        Map<String, List<?>> props = new HashMap<>();
        props.put("id", singletonList(1L));
        List<Comment> comments = commentDao.getByProps(props);

        assertThat(comments.size(), equalTo(1));
    }

    @Test
    public void getByProps_WhenRelationId() {
        Map<String, List<?>> props = new HashMap<>();
        props.put("post.id", singletonList(1L));
        List<Comment> comments = commentDao.getByProps(props);

        assertThat(comments.size(), equalTo(2));
        for(Comment comment : comments) {
            assertThat(comment.getPost().getId(), equalTo(1L));
        }
    }

    @Test
    public void getByProps_WhenRelationIds() {
        Map<String, List<?>> props = new HashMap<>();
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        props.put("post.id", ids);
        List<Comment> comments = commentDao.getByProps(props);

        assertThat(comments.size(), equalTo(5));
    }

    @Test
    public void getByProps_WhenRelationId_WhenIdIsString() {
        Map<String, List<?>> props = new HashMap<>();
        props.put("post.id", singletonList("1"));
        List<Comment> comments = commentDao.getByProps(props);

        assertThat(comments.size(), equalTo(2));
        for(Comment comment : comments) {
            assertThat(comment.getPost().getId(), equalTo(1L));
        }
    }

    @Test
    public void getByProps_WhenRelationId_AndName() {
        Map<String, List<?>> props = new HashMap<>();
        props.put("post.id", singletonList(1L));
        props.put("name", singletonList("Comment#1"));
        List<Comment> comments = commentDao.getByProps(props);

        assertThat(comments.size(), equalTo(1));
        for(Comment comment : comments) {
            assertThat(comment.getPost().getId(), equalTo(1L));
        }
    }

    @Test
    public void getByProps_WhenRelationName() {
        Map<String, List<?>> props = new HashMap<>();
        props.put("post.name", singletonList("Post#1"));
        List<Comment> comments = commentDao.getByProps(props);

        assertThat(comments.size(), equalTo(2));
        for(Comment comment : comments) {
            assertThat(comment.getPost().getId(), equalTo(1L));
        }
    }

    @Test
    public void getByProps_WhenRelationNamePostAndAuthor() {
        Map<String, List<?>> props = new HashMap<>();
        props.put("post.name", singletonList("Post#1"));
        props.put("author.name", singletonList("Author#1"));
        List<Comment> comments = commentDao.getByProps(props);

        assertThat(comments.size(), equalTo(1));
        for(Comment comment : comments) {
            assertThat(comment.getPost().getId(), equalTo(1L));
            assertThat(comment.getAuthor().getId(), equalTo(1L));
        }
    }

    @Test
    public void getByFields_WhenEmptyFields() {
        ArrayList<FieldHolder> fields = new ArrayList<>();
        List<Comment> comments = commentDao.getByFields(fields);

        assertThat(comments.size(), equalTo(0));
    }

    @Test(expected = NullPointerException.class)
    public void getByFields_WhenPassedNull() {
        List<Comment> comments = commentDao.getByFields(null);
    }

    @Test
    public void getByFields_ByName_WhenNotExist() {
        ArrayList<FieldHolder> fields = new ArrayList<>();
        fields.add(new FieldHolder("name", "NotExistName"));
        List<Comment> comments = commentDao.getByFields(fields);

        assertThat(comments.size(), equalTo(0));
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void getByFields_ByName_WhenNotExistFieldName() {
        ArrayList<FieldHolder> fields = new ArrayList<>();
        fields.add(new FieldHolder("nameNotExist", "Comment#1"));
        List<Comment> comments = commentDao.getByFields(fields);

        assertThat(comments.size(), equalTo(0));
    }

    @Test
    public void getByFields_ById() {
        ArrayList<FieldHolder> fields = new ArrayList<>();
        fields.add(new FieldHolder("id", 1));
        List<Comment> comments = commentDao.getByFields(fields);

        assertThat(comments.size(), equalTo(1));
        assertThat(comments, containsInAnyOrder(
                hasProperty("id", is(1L))
        ));
    }

    @Test
    public void getByFields_ById_WheIdIsString() {
        ArrayList<FieldHolder> fields = new ArrayList<>();
        fields.add(new FieldHolder("id", "1"));
        List<Comment> comments = commentDao.getByFields(fields);

        assertThat(comments.size(), equalTo(1));
        assertThat(comments, containsInAnyOrder(
                hasProperty("id", is(1L))
        ));
    }

    @Test
    public void getByFields_ByNameAndPostId_WhenExist() {
        ArrayList<FieldHolder> fields = new ArrayList<>();
        fields.add(new FieldHolder("name", "Comment#1"));
        fields.add(new FieldHolder("id", 1L, "post"));
        List<Comment> comments = commentDao.getByFields(fields);

        assertThat(comments.size(), equalTo(1));
        assertThat(comments, containsInAnyOrder(
                hasProperty("name", is("Comment#1"))
        ));
    }

    @Test
    public void getByFields_ByPostIdAndAuthorId_WhenExist() {
        ArrayList<FieldHolder> fields = new ArrayList<>();
        fields.add(new FieldHolder("id", 1L, "post"));
        fields.add(new FieldHolder("id", 1L, "author"));
        List<Comment> comments = commentDao.getByFields(fields);

        assertThat(comments.size(), equalTo(1));
        assertThat(comments, containsInAnyOrder(
                hasProperty("name", is("Comment#1"))
        ));
    }

    @Test
    public void getByFields_ByPostIdNameAndAuthorIdName_WhenExist() {
        ArrayList<FieldHolder> fields = new ArrayList<>();
        fields.add(new FieldHolder("id", 1L, "post"));
        fields.add(new FieldHolder("name", "Post#1", "post"));
        fields.add(new FieldHolder("id", 1L, "author"));
        fields.add(new FieldHolder("name", "Author#1", "author"));
        List<Comment> comments = commentDao.getByFields(fields);

        assertThat(comments.size(), equalTo(1));
        assertThat(comments, containsInAnyOrder(
                hasProperty("name", is("Comment#1"))
        ));
    }

    @Test
    public void universalQuery_WhenEmptyFields_SortByName_LimitOne() {
        Map<String, List<?>> props = new HashMap<>();
        QueryParams queryParams = QueryParams.of("name", OrderType.DESC, 1, null);
        List<Comment> comments = commentDao.universalQuery(props, queryParams);

        assertThat(comments.size(), equalTo(1));
        for(Comment comment : comments) {
            assertThat(comment.getName(), equalTo("Comment#8"));
        }
    }

    @Test
    public void universalQuery_WhenEmptyFields_SortNullOrderNullLimit2() {
        Map<String, List<?>> props = new HashMap<>();
        QueryParams queryParams = QueryParams.of(null, null, 2, null);
        List<Comment> comments = commentDao.universalQuery(props, queryParams);

        assertThat(comments.size(), equalTo(2));
    }

    @Test
    public void universalQuery_WhenFieldByPostId_SortByName_LimitOne() {
        Map<String, List<?>> props = new HashMap<>();
        props.put("post.id", singletonList(1L));
        QueryParams queryParams = QueryParams.of("name", OrderType.DESC, 1, null);
        List<Comment> comments = commentDao.universalQuery(props, queryParams);

        assertThat(comments.size(), equalTo(1));
        for(Comment comment : comments) {
            assertThat(comment.getName(), equalTo("Comment#2"));
        }
    }

    @Test
    public void universalQuery_WhenFieldByPostId_SortById_LimitOne() {
        Map<String, List<?>> props = new HashMap<>();
        props.put("post.id", singletonList(1L));
        QueryParams queryParams = QueryParams.of("id", OrderType.DESC, 1, null);
        List<Comment> comments = commentDao.universalQuery(props, queryParams);

        assertThat(comments.size(), equalTo(1));
        for(Comment comment : comments) {
            assertThat(comment.getName(), equalTo("Comment#2"));
        }
    }

    @Test
    public void universalQuery_WhenFieldByPostId_SortById_WithoutOrderAndLimit() {
        Map<String, List<?>> props = new HashMap<>();
        props.put("post.id", singletonList(1L));
        QueryParams queryParams = QueryParams.of("id", null, null, null);
        List<Comment> comments = commentDao.universalQuery(props, queryParams);

        assertThat(comments.size(), equalTo(2));
        assertThat(comments.get(0).getId(), equalTo(1L));
    }

    @Test
    public void universalQuery_WhenFieldByPostId_SortById_WithoutOrder_WithLimitStart() {
        Map<String, List<?>> props = new HashMap<>();
        props.put("post.id", singletonList(1L));
        QueryParams queryParams = QueryParams.of("id", null, 1, 1);
        List<Comment> comments = commentDao.universalQuery(props, queryParams);

        assertThat(comments.size(), equalTo(1));
        assertThat(comments.get(0).getId(), equalTo(2L));
    }
}