import java.util.Collection;

public class UserListMessage extends AbstractMessage {

    private Collection<String> names;

    private UserListMessage(){}

    public static UserListMessage of(Collection<String> names){
        UserListMessage m = new UserListMessage();
        m.names = names;
        return m;
    }

    public Collection<String> getNames() {
        return names;
    }
}
