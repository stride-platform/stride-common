package com.stride.stride_common.dto;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@DisplayName("DTO Serialization and Validation Tests")
class DtoTests {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("ApiResponse success factory methods work correctly")
    void testApiResponseSuccessFactoryMethods() {
        String testData = "test data";
        
        // Test simple success
        ApiResponse<String> response1 = ApiResponse.success(testData);
        assertTrue(response1.isSuccess());
        assertEquals(testData, response1.getData());
        assertNotNull(response1.getTimestamp());
        assertNull(response1.getMessage());
        
        // Test success with message
        ApiResponse<String> response2 = ApiResponse.success(testData, "Operation completed");
        assertTrue(response2.isSuccess());
        assertEquals(testData, response2.getData());
        assertEquals("Operation completed", response2.getMessage());
        
        // Test success with metadata
        Map<String, Object> metadata = Map.of("count", 5, "source", "test");
        ApiResponse<String> response3 = ApiResponse.success(testData, metadata);
        assertTrue(response3.isSuccess());
        assertEquals(testData, response3.getData());
        assertEquals(metadata, response3.getMetadata());
    }

    @Test
    @DisplayName("ApiResponse error factory methods work correctly")
    void testApiResponseErrorFactoryMethods() {
        // Test simple error
        ApiResponse<String> response1 = ApiResponse.error("Something went wrong", "ERROR_001");
        assertFalse(response1.isSuccess());
        assertNull(response1.getData());
        assertEquals("Something went wrong", response1.getMessage());
        assertEquals("ERROR_001", response1.getErrorCode());
        assertNotNull(response1.getTimestamp());
        
        // Test error with metadata
        Map<String, Object> metadata = Map.of("attemptCount", 3, "lastAttempt", "2025-01-01");
        ApiResponse<String> response2 = ApiResponse.error("Retry limit exceeded", "RETRY_ERROR", metadata);
        assertFalse(response2.isSuccess());
        assertEquals("Retry limit exceeded", response2.getMessage());
        assertEquals("RETRY_ERROR", response2.getErrorCode());
        assertEquals(metadata, response2.getMetadata());
    }

    @Test
    @DisplayName("ApiResponse serializes correctly to JSON")
    void testApiResponseSerialization() throws Exception {
        ApiResponse<String> response = ApiResponse.success("test", "Success message");
        
        String json = objectMapper.writeValueAsString(response);
        
        // Verify JSON contains expected fields
        assertTrue(json.contains("\"success\":true"));
        assertTrue(json.contains("\"data\":\"test\""));
        assertTrue(json.contains("\"message\":\"Success message\""));
        assertTrue(json.contains("\"timestamp\""));
        
        // Test deserialization
        ApiResponse<?> deserializedResponse = objectMapper.readValue(json, ApiResponse.class);
        assertTrue(deserializedResponse.isSuccess());
        assertEquals("test", deserializedResponse.getData());
        assertEquals("Success message", deserializedResponse.getMessage());
    }

    @Test
    @DisplayName("PaginatedResponse creates correct pagination info")
    void testPaginatedResponsePagination() {
        List<String> content = List.of("item1", "item2", "item3");
        int page = 1;
        int size = 3;
        long totalElements = 10;
        
        PaginatedResponse<String> response = PaginatedResponse.of(content, page, size, totalElements);
        
        assertEquals(content, response.getContent());
        assertEquals(totalElements, response.getTotalElements());
        assertEquals(4, response.getTotalPages()); // ceil(10/3) = 4
        
        PaginatedResponse.PageInfo pageInfo = response.getPageInfo();
        assertEquals(page, pageInfo.getPage());
        assertEquals(size, pageInfo.getSize());
        assertTrue(pageInfo.isHasNext()); // page 1 of 4 has next
        assertTrue(pageInfo.isHasPrevious()); // page 1 has previous (page 0)
        assertFalse(pageInfo.isFirst()); // page 1 is not first
        assertFalse(pageInfo.isLast()); // page 1 of 4 is not last
    }

    @Test
    @DisplayName("PaginatedResponse handles edge cases correctly")
    void testPaginatedResponseEdgeCases() {
        List<String> content = List.of("item1");
        
        // Test first page
        PaginatedResponse<String> firstPage = PaginatedResponse.of(content, 0, 5, 1);
        assertTrue(firstPage.getPageInfo().isFirst());
        assertTrue(firstPage.getPageInfo().isLast());
        assertFalse(firstPage.getPageInfo().isHasNext());
        assertFalse(firstPage.getPageInfo().isHasPrevious());
        
        // Test last page of multi-page result
        PaginatedResponse<String> lastPage = PaginatedResponse.of(content, 2, 5, 11);
        assertFalse(lastPage.getPageInfo().isFirst());
        assertTrue(lastPage.getPageInfo().isLast());
        assertFalse(lastPage.getPageInfo().isHasNext());
        assertTrue(lastPage.getPageInfo().isHasPrevious());
    }

    @Test
    @DisplayName("ErrorResponse factory methods work correctly")
    void testErrorResponseFactoryMethods() {
        // Test simple error response
        ErrorResponse response1 = ErrorResponse.of("Not Found", "404", "Resource not found");
        assertEquals("Not Found", response1.getError());
        assertEquals("404", response1.getErrorCode());
        assertEquals("Resource not found", response1.getMessage());
        assertNotNull(response1.getTimestamp());
        
        // Test error response with field errors
        Map<String, String> fieldErrors = Map.of(
            "email", "Invalid format",
            "password", "Too short"
        );
        ErrorResponse response2 = ErrorResponse.withFieldErrors(
            "Validation Failed", "VALIDATION_ERROR", "Request validation failed", fieldErrors
        );
        assertEquals("Validation Failed", response2.getError());
        assertEquals("VALIDATION_ERROR", response2.getErrorCode());
        assertEquals(fieldErrors, response2.getFieldErrors());
    }

    @Test
    @DisplayName("UserDto validation works correctly")
    void testUserDtoValidation() {
        Instant now = Instant.now();
        
        // Test valid UserDto
        UserDto validUser = new UserDto(
            "user123",
            "john@example.com",
            "John Doe",
            "org456",
            List.of("USER", "ADMIN"),
            true,
            now,
            now
        );
        
        assertEquals("user123", validUser.id());
        assertEquals("john@example.com", validUser.email());
        assertEquals("John Doe", validUser.fullName());
        assertTrue(validUser.active());
        
        // Test validation failures
        assertThrows(IllegalArgumentException.class, () -> 
            new UserDto(null, "john@example.com", "John", "org", List.of(), true, now, now)
        );
        
        assertThrows(IllegalArgumentException.class, () -> 
            new UserDto("", "john@example.com", "John", "org", List.of(), true, now, now)
        );
        
        assertThrows(IllegalArgumentException.class, () -> 
            new UserDto("user123", null, "John", "org", List.of(), true, now, now)
        );
        
        assertThrows(IllegalArgumentException.class, () -> 
            new UserDto("user123", "", "John", "org", List.of(), true, now, now)
        );
    }

    @Test
    @DisplayName("TaskDto validation and serialization work correctly")
    void testTaskDtoValidation() {
        Instant now = Instant.now();
        Instant due = now.plusSeconds(86400); // 1 day later
        
        // Test valid TaskDto
        TaskDto validTask = new TaskDto(
            "task123",
            "Complete feature",
            "Implement user authentication",
            "IN_PROGRESS",
            "HIGH",
            "user456",
            "user789",
            "team123",
            "project456",
            List.of("backend", "security"),
            due,
            now,
            now
        );
        
        assertEquals("task123", validTask.id());
        assertEquals("Complete feature", validTask.title());
        assertEquals("team123", validTask.teamId());
        assertEquals("project456", validTask.projectId());
        assertEquals(List.of("backend", "security"), validTask.tags());
        
        // Test validation failures
        assertThrows(IllegalArgumentException.class, () -> 
            new TaskDto(null, "title", "desc", "status", "priority", "assignee", "creator", "team", "project", List.of(), due, now, now)
        );
        
        assertThrows(IllegalArgumentException.class, () -> 
            new TaskDto("task123", "", "desc", "status", "priority", "assignee", "creator", "team", "project", List.of(), due, now, now)
        );
    }

    @Test
    @DisplayName("TeamDto and TeamMemberDto work correctly")
    void testTeamDtoValidation() {
        Instant now = Instant.now();
        
        // Test TeamMemberDto
        TeamMemberDto member1 = new TeamMemberDto("user1", "John Doe", "john@example.com", "ADMIN", now);
        TeamMemberDto member2 = new TeamMemberDto("user2", "Jane Smith", "jane@example.com", "MEMBER", now);
        
        List<TeamMemberDto> members = List.of(member1, member2);
        
        // Test valid TeamDto
        TeamDto validTeam = new TeamDto(
            "team123",
            "Development Team",
            "Core development team",
            "org456",
            members,
            now,
            now
        );
        
        assertEquals("team123", validTeam.id());
        assertEquals("Development Team", validTeam.name());
        assertEquals(2, validTeam.members().size());
        assertEquals("ADMIN", validTeam.members().get(0).role());
        
        // Test TeamMemberDto validation
        assertThrows(IllegalArgumentException.class, () -> 
            new TeamMemberDto(null, "John", "john@example.com", "ADMIN", now)
        );
        
        assertThrows(IllegalArgumentException.class, () -> 
            new TeamMemberDto("user1", "John", "john@example.com", "", now)
        );
        
        // Test TeamDto validation
        assertThrows(IllegalArgumentException.class, () -> 
            new TeamDto(null, "Team", "desc", "org", members, now, now)
        );
        
        assertThrows(IllegalArgumentException.class, () -> 
            new TeamDto("team123", "", "desc", "org", members, now, now)
        );
    }

    @Test
    @DisplayName("OrganizationDto validation works correctly")
    void testOrganizationDtoValidation() {
        Instant now = Instant.now();
        
        // Test valid OrganizationDto
        OrganizationDto validOrg = new OrganizationDto(
            "org123",
            "Acme Corp",
            "acme.com",
            true,
            now,
            now
        );
        
        assertEquals("org123", validOrg.id());
        assertEquals("Acme Corp", validOrg.name());
        assertEquals("acme.com", validOrg.domain());
        assertTrue(validOrg.active());
        
        // Test validation failures
        assertThrows(IllegalArgumentException.class, () -> 
            new OrganizationDto(null, "Acme", "acme.com", true, now, now)
        );
        
        assertThrows(IllegalArgumentException.class, () -> 
            new OrganizationDto("org123", "", "acme.com", true, now, now)
        );
    }

    @Test
    @DisplayName("DTOs serialize and deserialize correctly with Jackson")
    void testDtoJsonSerialization() throws Exception {
        Instant now = Instant.now();
        
        // Test UserDto serialization
        UserDto user = new UserDto("user1", "john@example.com", "John Doe", "org1", List.of("USER"), true, now, now);
        String userJson = objectMapper.writeValueAsString(user);
        UserDto deserializedUser = objectMapper.readValue(userJson, UserDto.class);
        
        assertEquals(user.id(), deserializedUser.id());
        assertEquals(user.email(), deserializedUser.email());
        assertEquals(user.roles(), deserializedUser.roles());
        
        // Test TaskDto serialization
        TaskDto task = new TaskDto("task1", "Test Task", "Description", "TODO", "HIGH", "user1", "user2", "team1", "project1", List.of("tag1"), now, now, now);
        String taskJson = objectMapper.writeValueAsString(task);
        TaskDto deserializedTask = objectMapper.readValue(taskJson, TaskDto.class);
        
        assertEquals(task.id(), deserializedTask.id());
        assertEquals(task.title(), deserializedTask.title());
        assertEquals(task.teamId(), deserializedTask.teamId());
        assertEquals(task.tags(), deserializedTask.tags());
    }
}