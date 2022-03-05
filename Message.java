

public class Message {
    private static int round = 1;
    private static int numOfMsgSent = 0;
    private int sendID;
    private boolean terminate;

    public Message() {}
    public Message (int myID) {sendID = myID; terminate = false;}
    public Message (int myID, boolean end) {sendID = myID; terminate = end;}

    public static void incRound(){ round++; }
    public static void incNumOfMsg(){ numOfMsgSent++; }

    //getters
    public int getID(){ return sendID;}
    public boolean getTerminate() {return terminate;}
    public static int getRound(){ return round; }
    public static int getNumOfMsgSent(){ return  numOfMsgSent; }


}
