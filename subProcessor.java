import java.util.ArrayList;
import java.util.Random;


public class subProcessor {
    private int myID;
    private int leaderID;
    private int sleep;
    private boolean interfacePoint;
    private Status myStatus;
    private subMessage preMessage;
    private subMessage sendMessage;
    private subProcessor preProcessor;

    //constructors
    public subProcessor () {}
    public subProcessor (int myID) {
        this.myID = myID;
        this.leaderID = -1;
        this.sleep = 0;
        this.myStatus = Status.UNKNOWN;
        this.preMessage = null;
        this.preProcessor = null;
        this.sendMessage = null;
    }

    //setters
    public void setMyStatus(Status status) {this.myStatus = status;}
    public void setPreProcessor(subProcessor pre) {this.preProcessor = pre;}
    private void setSleep(int sleep) {this.sleep = sleep;}

    //getters
    public int getMyID() {return this.myID;}
    public Status getMyStatus() {return this.myStatus;}
    public int getLeaderID() {return this.leaderID;}

    //执行者为当前processor， 所谓send即从上一个processor的内部读取信息
    public void sendPreMessage() {
        if(subMessage.getRound() > this.sleep && subMessage.getRound() > preProcessor.sleep) {
            if(preProcessor.sendMessage != null) {
                this.preMessage = preProcessor.sendMessage;
                preProcessor.sendMessage = null;
                subMessage.incNumOfMsg();
            }
        }
        else if(subMessage.getRound() <= this.sleep && subMessage.getRound() > preProcessor.sleep) {
            if(preProcessor.sendMessage != null) {
                preProcessor.sendMessage = null;
                subMessage.incNumOfMsg();
            }
        }

//        if(this.preMessage != null)
//            System.out.format("%-12s%-1s%-12s%-1s%-12s%-1s%-12s\n", myID, "|", preMessage.getID(), "|", myStatus, "|", sleep-subMessage.getRound());
//        else
//            System.out.format("%-12s%-1s%-12s%-1s%-12s%-1s%-12s\n", myID, "|", null, "|", myStatus, "|", sleep-subMessage.getRound());
    }

    //执行者为当前processor，所谓exe即根据可能收到的信息更新内部变量
    public void exePreMessage() {
        if(subMessage.getRound() == 1 && subMessage.getRound() > this.sleep) {
            this.sendMessage = new subMessage(this.myID);
        }
        else if(subMessage.getRound() > 1) {
            if(subMessage.getRound() > this.sleep) {  //Determine whether the current node is awake, if it is awake, then operate
                if(this.sleep + 1 == subMessage.getRound()) {  // The node has just woken up and needs to send myID first
                    setMyStatus(Status.UNKNOWN);
                    this.sendMessage = new subMessage(this.myID);
                }
                else{
                    if(this.preMessage != null) {
                        if(!this.preMessage.getTerminate()) {
                            if(this.preMessage.getID() > this.myID) {
                                this.sendMessage = this.preMessage;
                                this.preMessage = null;
                            }
                            else if (this.preMessage.getID() == this.myID) {
                                setMyStatus(Status.LEADER);
                                this.sendMessage = new subMessage(this.myID, true);
                                this.preMessage = null;
                            }
                            else {
                                this.sendMessage = new subMessage(this.myID);
                                this.preMessage = null;
                            }
                        }
                        else {
                            if(this.myStatus != Status.LEADER) {
                                this.setMyStatus(Status.KNOWN);
                                this.sendMessage = this.preMessage;
                                this.leaderID = this.preMessage.getID();
                                this.preMessage = null;
                            }
                            else {
                                this.leaderID = this.preMessage.getID();
                                this.preMessage = null;
                            }
                        }
                    }
                    else {
                        this.sendMessage = new subMessage(this.myID);
                    }
                }
            }
        }

//        if(sendMessage != null)
//            System.out.format("%-12s%-1s%-12s%-1s%-12s%-1s%-12s\n", myID, "|", sendMessage.getID(), "|", myStatus, "|", sleep-subMessage.getRound());
//        else
//            System.out.format("%-12s%-1s%-12s%-1s%-12s%-1s%-12s\n", myID, "|", null, "|", myStatus, "|", sleep-subMessage.getRound());
    }

    public static ArrayList<subProcessor> ringConstructor (ArrayList<Integer> processors) {
        ArrayList<subProcessor> processorList = new ArrayList<subProcessor>();
        int uniqueID;
        for (int index = 0; index < processors.size(); index++) {
            uniqueID = processors.get(index);
            subProcessor p = new subProcessor(uniqueID);
            processorList.add(p);
        }
        for (int i = 0; i < processorList.size(); i++) {
            if(i > 0)
                processorList.get(i).setPreProcessor(processorList.get(i-1));
            else
                processorList.get(i).setPreProcessor(processorList.get(processorList.size()-1));
        }
        return processorList;
    }

}
