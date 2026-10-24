
package com.example.first_java_crud;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("")
public class StudentView extends VerticalLayout {

    private final StudentRepository repository;
    private final Grid<Student> grid = new Grid<>(Student.class);

    private final TextField nameField = new TextField("Name");
    private final TextField emailField = new TextField("Email");

    private Student editingStudent;

    public StudentView(StudentRepository repository) {
        this.repository = repository;

        grid.setColumns("id", "name", "email");

        Button saveButton = new Button("Add Student", event -> saveStudent());
        Button updateButton = new Button("Update Selected", event -> updateStudent());
        Button deleteButton = new Button("Delete Selected", event -> deleteStudent());

        grid.asSingleSelect().addValueChangeListener(event -> {
            editingStudent = event.getValue();

            if (editingStudent != null) {
                nameField.setValue(editingStudent.getName());
                emailField.setValue(editingStudent.getEmail());
            } else {
                nameField.clear();
                emailField.clear();
            }
        });

        add(
                nameField,
                emailField,
                saveButton,
                updateButton,
                deleteButton,
                grid
        );

        refreshGrid();
    }

    private void saveStudent() {
        if (nameField.isEmpty() || emailField.isEmpty()) {
            return;
        }

        repository.save(new Student(
                nameField.getValue(),
                emailField.getValue()
        ));

        clearForm();
        refreshGrid();
    }

    private void updateStudent() {
        if (editingStudent == null) {
            return;
        }

        editingStudent.setName(nameField.getValue());
        editingStudent.setEmail(emailField.getValue());

        repository.save(editingStudent);

        clearForm();
        refreshGrid();
    }

    private void deleteStudent() {
        if (editingStudent == null) {
            return;
        }

        repository.delete(editingStudent);

        clearForm();
        refreshGrid();
    }

    private void clearForm() {
        editingStudent = null;
        nameField.clear();
        emailField.clear();
        grid.asSingleSelect().clear();
    }

    private void refreshGrid() {
        grid.setItems(repository.findAll());
    }
}
