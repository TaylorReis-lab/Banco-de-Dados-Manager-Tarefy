package com.example;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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
        public Response addTask(Task task) {
                if (task == null) {
                        return Response.status(Response.Status.BAD_REQUEST).build();
                }
                task.persist();
                return Response.status(Response.Status.CREATED).entity(task).build();
        }

        @PUT
        @Path("/{id}")
        @Transactional
        public Response updateTask(@PathParam("id") Long id, Task updatedTask) {
                Task task = Task.findById(id);
                if (task == null) {
                        return Response.status(Response.Status.NOT_FOUND).build();
                }
                if (updatedTask == null) {
                        return Response.status(Response.Status.BAD_REQUEST).build();
                }
                task.title = updatedTask.title;
                task.description = updatedTask.description;
                task.creationDate = updatedTask.creationDate;
                task.dueDate = updatedTask.dueDate;
                task.status = updatedTask.status;
                task.persist();
                return Response.ok(task).build();
        }

        @DELETE
        @Path("/{id}")
        @Transactional
        public Response deleteTask(@PathParam("id") Long id) {
                boolean deleted = Task.deleteById(id);
                if (deleted) {
                        return Response.noContent().build();
                } else {
                        return Response.status(Response.Status.NOT_FOUND).build();
                }
        }
}