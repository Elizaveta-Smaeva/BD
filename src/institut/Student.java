package institut;

public class Student {
    private int id;
    private String surname;
    private String name;
    private int age;

    // Конструкторы
    public Student(String surname, String name, int age) {
        this.surname = surname;
        this.name = name;
        this.age = age;
    }

    public Student(int id, String surname, String name, int age) {
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.age = age;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}