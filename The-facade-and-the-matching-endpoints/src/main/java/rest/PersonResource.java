package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Person;
import exceptions.PersonNotFoundException;
import utils.EMF_Creator;
import facades.PersonFacade;
import java.time.LocalDate;
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
    public String getPerson(@PathParam("id") int id) throws PersonNotFoundException {
        Person p = FACADE.getPerson(id);
        return GSON.toJson(new PersonDTO(p));
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("all")
    public String getAllPersons() {
        List<Person> p = FACADE.getAllPersons();
        return GSON.toJson(new PersonsDTO(p));
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String addPerson(String personAsJson) {
        Person newPerson = GSON.fromJson(personAsJson, Person.class);
        Person addedPerson = FACADE.addPerson(newPerson.getFirstName(), newPerson.getLastName(), newPerson.getPhone());
        return GSON.toJson(new PersonDTO(addedPerson));
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("{id}")
    public String editPerson(String personAsJson, @PathParam("id") int id) throws PersonNotFoundException {
        Person newPerson = GSON.fromJson(personAsJson, Person.class);
        newPerson.setId(id);
        newPerson.setLastEdited(LocalDate.now());
        Person changedPerson = FACADE.editPerson(newPerson);
        return GSON.toJson(new PersonDTO(changedPerson));
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @Path("{id}")
    public String deletePerson(@PathParam("id") int id) throws PersonNotFoundException {
        FACADE.deletePerson(id);
        return "{\"Status\":\"Person deleted\"}";
    }
}
