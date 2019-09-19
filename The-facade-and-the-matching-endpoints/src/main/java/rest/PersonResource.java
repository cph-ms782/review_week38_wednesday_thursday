package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.Person;
import static entities.Person_.id;
import utils.EMF_Creator;
import facades.PersonFacade;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("person")
public class PersonResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    private static final PersonFacade FACADE = PersonFacade.getPersonFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("{id}")
    public String getPerson(@PathParam("id") int id) {
        Person p = FACADE.getPerson(id);
        return GSON.toJson(p);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("all")
    public String getAllPersons() {
        List<Person> p = FACADE.getAllPersons();
        return GSON.toJson(p);
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String addPerson(String personAsJson) {
        Person newPerson = GSON.fromJson(personAsJson, Person.class);
        Person addedPerson = FACADE.addPerson(newPerson.getFirstName(), newPerson.getLastName(), newPerson.getPhone());
        return GSON.toJson(addedPerson);
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("{id}")
    public String editPerson(String personAsJson, @PathParam("id") int id) {
        Person newPerson = GSON.fromJson(personAsJson, Person.class);
        Person changedPerson = FACADE.editPerson(newPerson);
        return GSON.toJson(changedPerson);
    }
    
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @Path("{id}")
    public String deletePerson(@PathParam("id") int id) {
        Person pOriginal = FACADE.deletePerson(id);
        return "{\"Status\" : \"Person deleted\"}";
    }
    
}