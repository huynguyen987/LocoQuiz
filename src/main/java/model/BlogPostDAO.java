package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import entity.BlogPost;
import Module.DBConnect;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BlogPostDAO {
    // Add a blog post
    public boolean addBlogPost(BlogPost blogPost) {
        int n = 0;
        String sql = "INSERT INTO BlogPosts(title, content, author_id, status) VALUES(?, ?, ?, ?)";

        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, blogPost.getTitle());
            pre.setString(2, blogPost.getContent());
            pre.setInt(3, blogPost.getAuthorId());
            pre.setString(4, blogPost.getStatus());
            n = pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return n > 0;
    }

    // Get blog post by ID
    public BlogPost getBlogPostById(int id) {
        BlogPost blogPost = null;
        String sql = "SELECT * FROM BlogPosts WHERE id = ?";

        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setInt(1, id);
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    blogPost = new BlogPost();
                    blogPost.setId(rs.getInt("id"));
                    blogPost.setTitle(rs.getString("title"));
                    blogPost.setContent(rs.getString("content"));
                    blogPost.setAuthorId(rs.getInt("author_id"));
                    blogPost.setStatus(rs.getString("status"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return blogPost;
    }

    // Get all blog posts
    public List<BlogPost> getAllBlogPosts() {
        List<BlogPost> blogPosts = new ArrayList<>();
        String sql = "SELECT * FROM BlogPosts";

        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql);
             ResultSet rs = pre.executeQuery()) {
            while (rs.next()) {
                BlogPost blogPost = new BlogPost();
                blogPost.setId(rs.getInt("id"));
                blogPost.setTitle(rs.getString("title"));
                blogPost.setContent(rs.getString("content"));
                blogPost.setAuthorId(rs.getInt("author_id"));
                blogPost.setStatus(rs.getString("status"));
                blogPosts.add(blogPost);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return blogPosts;
    }

    // Update a blog post
    public boolean updateBlogPost(BlogPost blogPost) {
        int n = 0;
        String sql = "UPDATE BlogPosts SET title = ?, content = ?, author_id = ?, status = ? WHERE id = ?";

        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, blogPost.getTitle());
            pre.setString(2, blogPost.getContent());
            pre.setInt(3, blogPost.getAuthorId());
            pre.setString(4, blogPost.getStatus());
            pre.setInt(5, blogPost.getId());
            n = pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return n > 0;
    }

    // Remove a blog post
    public boolean removeBlogPost(int id) {
        int n = 0;
        String sql = "DELETE FROM BlogPosts WHERE id = ?";

        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setInt(1, id);
            n = pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return n > 0;
    }

    // Get all blog posts by author ID
    public List<BlogPost> getAllBlogPostsByAuthorId(int authorId) {
        List<BlogPost> blogPosts = new ArrayList<>();
        String sql = "SELECT * FROM BlogPosts WHERE author_id = ?";

        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setInt(1, authorId);
            try (ResultSet rs = pre.executeQuery()) {
                while (rs.next()) {
                    BlogPost blogPost = new BlogPost();
                    blogPost.setId(rs.getInt("id"));
                    blogPost.setTitle(rs.getString("title"));
                    blogPost.setContent(rs.getString("content"));
                    blogPost.setAuthorId(rs.getInt("author_id"));
                    blogPost.setStatus(rs.getString("status"));
                    blogPosts.add(blogPost);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return blogPosts;
    }

    // Get all blog posts by status
    public List<BlogPost> getAllBlogPostsByStatus(String status) {
        List<BlogPost> blogPosts = new ArrayList<>();
        String sql = "SELECT * FROM BlogPosts WHERE status = ?";

        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, status);
            try (ResultSet rs = pre.executeQuery()) {
                while (rs.next()) {
                    BlogPost blogPost = new BlogPost();
                    blogPost.setId(rs.getInt("id"));
                    blogPost.setTitle(rs.getString("title"));
                    blogPost.setContent(rs.getString("content"));
                    blogPost.setAuthorId(rs.getInt("author_id"));
                    blogPost.setStatus(rs.getString("status"));
                    blogPosts.add(blogPost);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return blogPosts;
    }

    // Get all blog posts by author ID and status
    public List<BlogPost> getAllBlogPostsByAuthorIdAndStatus(int authorId, String status) {
        List<BlogPost> blogPosts = new ArrayList<>();
        String sql = "SELECT * FROM BlogPosts WHERE author_id = ? AND status = ?";

        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setInt(1, authorId);
            pre.setString(2, status);
            try (ResultSet rs = pre.executeQuery()) {
                while (rs.next()) {
                    BlogPost blogPost = new BlogPost();
                    blogPost.setId(rs.getInt("id"));
                    blogPost.setTitle(rs.getString("title"));
                    blogPost.setContent(rs.getString("content"));
                    blogPost.setAuthorId(rs.getInt("author_id"));
                    blogPost.setStatus(rs.getString("status"));
                    blogPosts.add(blogPost);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return blogPosts;
    }

    // Main method for testing
    public static void main(String[] args) {
        BlogPostDAO dao = new BlogPostDAO();
        List<BlogPost> list = dao.getAllBlogPosts();

        for (BlogPost blogPost : list) {
            System.out.println(blogPost.getId() + " " +
                    blogPost.getTitle() + " " +
                    blogPost.getContent() + " " +
                    blogPost.getAuthorId() + " " +
                    blogPost.getStatus());
        }
    }
}
