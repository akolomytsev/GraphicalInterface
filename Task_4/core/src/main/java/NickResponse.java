public class NickResponse extends AbstractMessage{

    private String nick;

    public NickResponse(String nick){
        this.nick = nick;
    }

    public String getNick(){
        return nick;
    }

}
