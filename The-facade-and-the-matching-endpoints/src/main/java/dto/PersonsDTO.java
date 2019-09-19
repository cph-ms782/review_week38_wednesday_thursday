package dto;

import entities.Person;

/**
 *
 * @author msi
 */
public class PersonDTO {

    private long id;
    private String fName;
    private String lName;
    private String phone;

    public PersonDTO(Person p) {
        this.fName = p.getFirstName();
        this.lName = p.getLastName();
        this.phone = p.getPhone();
        this.id = p.getId();
    }

    public PersonDTO(String fn, String ln, String phone) {
        this.fName = fn;
        this.lName = ln;
        this.phone = phone;
    }

    public PersonDTO() {
    }

    
}
