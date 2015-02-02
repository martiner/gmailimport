package cz.geek.resources;

import javax.mail.Message;
import java.util.List;

public interface Resource {

    String name();

    void check();

    List<Resource> listResources();

    List<Message> list();

    Resource createResource(String name);

    void copy(Message msg);
}
