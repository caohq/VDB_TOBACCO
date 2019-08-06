package cn.csdb.portal.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @program: DataSync
 * @description: a person Test class
 * @author: xiajl
 * @create: 2018-10-22 09:50
 **/
@Document(collection = "t_person")
public class Person {
    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("age")
    private int age;

    @Field("address")
    private String address;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
