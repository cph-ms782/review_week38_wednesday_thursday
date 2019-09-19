package facades;

import entities.Person;
import java.util.List;

/**
 *
 * @author msi
 */
public interface IPersonFacade {

    public Person addPerson(String fName, String lName, String phone);

    public void deletePerson(int id);

    public Person getPerson(int id);

    public List<Person> getAllPersons();

    public Person editPerson(Person p);
}
