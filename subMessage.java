public class subMessage {
    private static int round = 1;
    private static int numOfMsgSent = 0;
    private int sendID;
    private boolean terminate;

    public subMessage() {}
    public subMessage (int myID) {sendID = myID; terminate = false;}
    public subMessage (int myID, boolean end) {sendID = myID; terminate = end;}

    public static void incRound() {round++; }
    public static void incNumOfMsg() {numOfMsgSent++; }
    public static void resetRound() {round = 1;}

    //getters
    public int getID() {return sendID;}
    public boolean getTerminate() {return terminate;}
    public static int getRound() {return round;}
    public static int getNumOfMsgSent() {return numOfMsgSent;}
}
