package org.acme;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.transaction.Transactional;
import java.util.List;

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskResource {

    @GET
    public List<Task> getAllTasks() {
        return Task.listAll();
    }

    @POST
    @Transactional
    public void addTask(Task task) {
        task.persist();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public void updateTask(@PathParam("id") Long id, Task updatedTask) {
        Task task = Task.findById(id);
        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setCreationDate(updatedTask.getCreationDate());
        task.setDueDate(updatedTask.getDueDate());
        task.setStatus(updatedTask.getStatus());
        task.persist();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void deleteTask(@PathParam("id") Long id) {
        Task.deleteById(id);
    }
}
