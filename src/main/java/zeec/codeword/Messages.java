package zeec.codeword;

/**
 * Created by Cesar-Melchor on 12/5/16.
 */

public class Messages {

    String fromUser;
    String message;
    String targetUser;
    String date;

    public Messages(String fromUser, String message, String targetUser, String date){
        this.fromUser = fromUser;
        this.message = message;
        this.targetUser = targetUser;
        this.date = date;

    }
    public String getFromUser(){
        return fromUser;
    }
    public String getMessage(){
        return message;
    }
    public String getTargetUser(){return targetUser;}
    public String getDate(){return date;}
}
