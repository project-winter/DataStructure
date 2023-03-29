package co.edu.uptc.models.cod202115100;

import co.edu.uptc.pojos.Person;
import co.edu.uptc.presenters.ContratBills;

import java.util.List;

public class MyManager implements ContratBills.Model {
    ContratBills.Presenter presenter;
    private MyManagerPerson managerPerson;

    public MyManager() {
        managerPerson = new MyManagerPerson(this);
    }

    @Override
    public void setPresenter(ContratBills.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void addPerson(Person person) {
        managerPerson.addPerson(person.clone());
    }

    @Override
    public String getPerson(int pos) {
        return null;
    }

    @Override
    public List<Person> getPeople() {
        return managerPerson.getPeople();
    }

    @Override
    public Person getPerson(String attribute) {
        return managerPerson.getPerson(attribute);
    }

    @Override
    public void editPerson(Person person) {
        managerPerson.editPerson(person);
    }

    @Override
    public String getAuthor() {
        return "Santiago Andres Orjuela Lopez";
    }
}
