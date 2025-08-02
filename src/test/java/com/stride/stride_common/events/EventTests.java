package com.stride.stride_common.events;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@DisplayName("Event System and Serialization Tests")
class EventTests {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("UserCreatedEvent implements UserEvent and BaseEvent correctly")
    void testUserCreatedEvent() {
        Instant now = Instant.now();
        String eventId = UUID.randomUUID().toString();
        String correlationId = UUID.randomUUID().toString();
        
//  String eventId,
//     String correlationId,
//     Instant timestamp,
//     String userId,
//     String organizationId,
//     String email,
//     String fullName

        UserCreatedEvent event = new UserCreatedEvent(
            eventId,
            correlationId,
            now,
            "user123",
            "org456",
            "john@example.com",
            "John Doe"
        );
        
        // Test BaseEvent interface methods
        
        assertEquals(eventId, event.eventId());
        assertEquals(correlationId, event.correlationId());
        assertEquals(now, event.timestamp());
        assertEquals("USER_CREATED", event.getEventType());
        assertEquals("user123", event.getAggregateId());
        //assertEquals(1, event.getVersion());
        
        // Test UserCreatedEvent specific data
        assertEquals("user123", event.userId());
        assertEquals("org456", event.organizationId());
        assertEquals("john@example.com", event.email());
        assertEquals("John Doe", event.fullName());
        
        // Test interface inheritance
        assertTrue(event instanceof UserEvent);
        assertTrue(event instanceof BaseEvent);
    }

    @Test
    @DisplayName("UserUpdatedEvent works correctly")
    void testUserUpdatedEvent() {
        Instant now = Instant.now();
        String eventId = UUID.randomUUID().toString();
        String correlationId = UUID.randomUUID().toString();
        
        UserUpdatedEvent event = new UserUpdatedEvent(
            eventId,
            correlationId,
            now,
            "user123",
            "org456",
            "johnsmith@example.com",
            "John Smith"
        );
        
        assertEquals("USER_UPDATED", event.getEventType());
        assertEquals("user123", event.getAggregateId());
        assertEquals("johnsmith@example.com", event.email());
        assertEquals("John Smith", event.fullName());
        assertTrue(event instanceof UserEvent);
    }

    @Test
    @DisplayName("TaskCreatedEvent includes team and project context")
    void testTaskCreatedEvent() {
        Instant now = Instant.now();
        String eventId = UUID.randomUUID().toString();
        String correlationId = UUID.randomUUID().toString();
    //      String eventId,
    // String correlationId,
    // Instant timestamp,
    // String taskId,
    // String teamId,
    // String projectId,
    // String title,
    // String assignedTo,
    // String createdBy,
    // String organizationId
        TaskCreatedEvent event = new TaskCreatedEvent(
            eventId,
            correlationId,
            now,
            "task123",
            "team456",
            "project789",
            "Implement authentication",
            "user456",
            "user789",
            "org123"
        );
        
        assertEquals("TASK_CREATED", event.getEventType());
        assertEquals("task123", event.getAggregateId());
        assertEquals("Implement authentication", event.title());
        assertEquals("user789", event.createdBy());
        assertEquals("user456", event.assignedTo());
        assertEquals("org123", event.organizationId());
        assertTrue(event instanceof TaskEvent);
        assertTrue(event instanceof BaseEvent);
    }


  
    @Test
    @DisplayName("TaskUpdatedEvent tracks status changes")
    void testTaskUpdatedEvent() {
        Instant now = Instant.now();
        String eventId = UUID.randomUUID().toString();
        String correlationId = UUID.randomUUID().toString();
        
        
        TaskUpdatedEvent event = new TaskUpdatedEvent(
            eventId,
            correlationId,
            now,
            "task123",
            "team456",
            "IN_PROGRESS",
            "TODO",
            "user456"
        );
        
        assertEquals("TASK_UPDATED", event.getEventType());
        assertEquals("task123", event.getAggregateId());
        assertEquals("IN_PROGRESS", event.status());
        assertEquals("user456", event.updatedBy());
        assertTrue(event instanceof TaskEvent);
    }


    //   String eventId,
    // String correlationId,
    // Instant timestamp,
    // String taskId,
    // String teamId,
    // String assignedTo,
    // String assignedBy,
    // String previousAssignee
    @Test
    @DisplayName("TaskAssignedEvent includes assignment context")
    void testTaskAssignedEvent() {
        Instant now = Instant.now();
        String eventId = UUID.randomUUID().toString();
        String correlationId = UUID.randomUUID().toString();
        
        TaskAssignedEvent event = new TaskAssignedEvent(
            eventId,
            correlationId,
            now,
            "task123",
            "team456",
            "user789",
            "user456", 
            "user111"
        );
        
        assertEquals("TASK_ASSIGNED", event.getEventType());
        assertEquals("task123", event.getAggregateId());
        assertEquals("user789", event.assignedTo());
        assertEquals("user456", event.assignedBy());
        assertEquals("user111", event.previousAssignee());
        assertTrue(event instanceof TaskEvent);
    }

    @Test
    @DisplayName("TeamCreatedEvent works correctly")
    void testTeamCreatedEvent() {
        Instant now = Instant.now();
        
        TeamCreatedEvent event = new TeamCreatedEvent(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            now,
            "team123",
            "org456",
            "Developmenent Team",
            "user789"
        );
        
        assertEquals("TEAM_CREATED", event.getEventType());
        assertEquals("team123", event.getAggregateId());
        assertEquals("Developmenent Team", event.teamName());
        assertEquals("org456", event.organizationId());
        assertEquals("user789", event.createdBy());
        assertTrue(event instanceof TeamEvent);
        assertTrue(event instanceof BaseEvent);
    }

    @Test
    @DisplayName("TeamMemberAddedEvent includes role information")
    void testTeamMemberAddedEvent() {
        Instant now = Instant.now();
        
        TeamMemberAddedEvent event = new TeamMemberAddedEvent(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            now,
            "team123",
            "user456",
            "ADMIN",
            "user789"
        );
        
        assertEquals("TEAM_MEMBER_ADDED", event.getEventType());
        assertEquals("team123", event.getAggregateId());
        assertEquals("user456", event.userId());
        assertEquals("ADMIN", event.role());
        assertEquals("user789", event.addedBy());
        assertTrue(event instanceof TeamEvent);
    }

    // @Test
    // @DisplayName("Events serialize to JSON correctly with type information")
    // void testEventJsonSerialization() throws Exception {
    //     Instant now = Instant.now();
        
    //     // Test UserCreatedEvent serialization
    //     UserCreatedEvent userEvent = new UserCreatedEvent(
    //         "event-123",
    //         "corr-456",
    //         now,
    //         "user123",
    //         "org456",
    //         "john@example.com",
    //         "John Doe"
    //     );
        
    //     String json = objectMapper.writeValueAsString(userEvent);
        
    //     // Verify JSON contains type information and data
    //     assertTrue(json.contains("\"eventType\":\"USER_CREATED\""));
    //     assertTrue(json.contains("\"eventId\":\"event-123\""));
    //     assertTrue(json.contains("\"userId\":\"user123\""));
    //     assertTrue(json.contains("\"email\":\"john@example.com\""));
        
    //     // Test deserialization back to BaseEvent (polymorphic)
    //     BaseEvent deserializedEvent = objectMapper.readValue(json, BaseEvent.class);
        
    //     assertTrue(deserializedEvent instanceof UserCreatedEvent);
    //     // assertEquals("USER_CREATED", deserializedEvent.getEventType());
    //     // assertEquals("event-123", deserializedEvent.getEventId());
    //     // assertEquals("user123", deserializedEvent.getAggregateId());
        
    //     // Cast and verify specific event data
    //     UserCreatedEvent deserializedUserEvent = (UserCreatedEvent) deserializedEvent;
    //     assertEquals("john@example.com", deserializedUserEvent.email());
    //     assertEquals("John Doe", deserializedUserEvent.fullName());
    // }

//     @Test
//     @DisplayName("Different event types serialize with correct type discriminators")
//    void testPolymorphicEventSerialization() throws Exception {
//        Instant now = Instant.now();
//         String eventId = UUID.randomUUID().toString();
//         String correlationId = UUID.randomUUID().toString();

//         TaskCreatedEvent taskEvent = new TaskCreatedEvent(
//             eventId,
//             correlationId,
//             now,
//             "task123",
//             "team456",
//             "project789",
//             "Implement authentication",
//             "user456",
//             "user789",
//             "org123"
//         );
//         TeamCreatedEvent teamEvent = new TeamCreatedEvent(
//             "team-event-123",
//             "corr-789",
//             now,
//             "team456",
//             "New Team",
//             "org789",
//             "user123"
//         );
        
//         // Serialize as BaseEvent
//         String taskJson = objectMapper.writeValueAsString((BaseEvent) taskEvent);
//         String teamJson = objectMapper.writeValueAsString((BaseEvent) teamEvent);
        
//         // Verify type discriminators
//         assertTrue(taskJson.contains("\"eventType\":\"TASK_CREATED\""));
//         assertTrue(teamJson.contains("\"eventType\":\"TEAM_CREATED\""));
        
//         // Deserialize back and verify types
//         BaseEvent deserializedTask = objectMapper.readValue(taskJson, BaseEvent.class);
//         BaseEvent deserializedTeam = objectMapper.readValue(teamJson, BaseEvent.class);
        
//         assertTrue(deserializedTask instanceof TaskCreatedEvent);
//         assertTrue(deserializedTeam instanceof TeamCreatedEvent);
        
//         assertEquals("TASK_CREATED", deserializedTask.getEventType());
//         assertEquals("TEAM_CREATED", deserializedTeam.getEventType());
//     }

    // @Test
    // @DisplayName("Event inheritance hierarchy works with sealed classes")
    // void testSealedClassHierarchy() {
    //     Instant now = Instant.now();
        
    //     // Create instances of each event type
    //     UserCreatedEvent userEvent = new UserCreatedEvent("1", "c1", now, "u1", "o1", "email", "name");
    //     TaskCreatedEvent taskEvent = new TaskCreatedEvent("2", "c2", now, "t1", "title", "u1", "u2", "o1");
    //     TeamCreatedEvent teamEvent = new TeamCreatedEvent("3", "c3", now, "team1", "name", "o1", "u1");
        
    //     // Test that all events implement BaseEvent
    //     assertTrue(userEvent instanceof BaseEvent);
    //     assertTrue(taskEvent instanceof BaseEvent);
    //     assertTrue(teamEvent instanceof BaseEvent);
        
    //     // Test intermediate interfaces
    //     assertTrue(userEvent instanceof UserEvent);
    //     assertTrue(taskEvent instanceof TaskEvent);
    //     assertTrue(teamEvent instanceof TeamEvent);
        
    //     // Test that intermediate interfaces extend BaseEvent
    //     BaseEvent userAsBase = userEvent;
    //     BaseEvent taskAsBase = taskEvent;
    //     BaseEvent teamAsBase = teamEvent;
        
    //     assertEquals("USER_CREATED", userAsBase.getEventType());
    //     assertEquals("TASK_CREATED", taskAsBase.getEventType());
    //     assertEquals("TEAM_CREATED", teamAsBase.getEventType());
    // }

    // @Test
    // @DisplayName("Event correlation IDs enable distributed tracing")
    // void testEventCorrelation() {
    //     Instant now = Instant.now();
    //     String correlationId = "correlation-12345";
        
    //     // Create related events with same correlation ID
    //     UserCreatedEvent userEvent = new UserCreatedEvent(
    //         "event-1", correlationId, now, "user123", "org456", "john@example.com", "John"
    //     );
        
    //     TeamMemberAddedEvent memberEvent = new TeamMemberAddedEvent(
    //         "event-2", correlationId, now, "team123", "user123", "MEMBER", "admin456"
    //     );
        
    //     TaskAssignedEvent taskEvent = new TaskAssignedEvent(
    //         "event-3", correlationId, now, "task123", "user123", "admin456", null
    //     );
        
    //     // Verify all events share the same correlation ID
    //     assertEquals(correlationId, userEvent.getCorrelationId());
    //     assertEquals(correlationId, memberEvent.getCorrelationId());
    //     assertEquals(correlationId, taskEvent.getCorrelationId());
        
    //     // Verify events are different but related
    //     assertNotEquals(userEvent.getEventId(), memberEvent.getEventId());
    //     assertNotEquals(memberEvent.getEventId(), taskEvent.getEventId());
        
    //     // Verify they reference the same user in the workflow
    //     assertEquals("user123", userEvent.userId());
    //     assertEquals("user123", memberEvent.userId());
    //     assertEquals("user123", taskEvent.assignedTo());
    // }

    // @Test
    // @DisplayName("Event versioning supports schema evolution")
    // void testEventVersioning() {
    //     Instant now = Instant.now();
        
    //     UserCreatedEvent event = new UserCreatedEvent(
    //         "event-123", "corr-456", now, "user123", "org456", "john@example.com", "John Doe"
    //     );
        
    //     // Test default version
    //     assertEquals(1, event.getVersion());
        
    //     // Test that version is included in base interface
    //     BaseEvent baseEvent = event;
    //     assertEquals(1, baseEvent.getVersion());
    // }
}