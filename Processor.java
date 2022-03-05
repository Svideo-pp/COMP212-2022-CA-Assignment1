import java.util.ArrayList;
import java.util.Random;


enum Status {UNKNOWN, LEADER, KNOWN, ASLEEP}

public class Processor {

    private int myID;
    private int leaderID;
    private int sleep;
    private boolean interfacePoint;
    private Status myStatus;
    private Message preMessage;
    private Message sendMessage;
    private Processor preProcessor;
    private ArrayList<Integer> subring;

    //constructors
    public Processor () {}
    public Processor (int myID) {
        this.myID = myID;
        this.leaderID = -2;
        this.sleep = 0;
        this.myStatus = Status.UNKNOWN;
        this.preMessage = null;
        this.preProcessor = null;
        this.sendMessage = null;
    }

    public Processor (boolean isInterface, ArrayList<Integer> subring) {
        this.myID = -1;
        this.leaderID = -2;
        this.sleep = 0;
        this.interfacePoint = isInterface;
        this.myStatus = Status.UNKNOWN;
        this.preMessage = null;
        this.preProcessor = null;
        this.sendMessage = null;
        this.subring = subring;
    }

    //setters
    public void setMyStatus(Status status) {this.myStatus = status;}
    public void setPreProcessor(Processor pre) {this.preProcessor = pre;}
    private void setSleep(int sleep) {this.sleep = sleep;}

    //getters
    public int getMyID() {return this.myID;}
    public Status getMyStatus() {return this.myStatus;}
    public int getLeaderID() {return this.leaderID;}

    //执行者为当前processor， 所谓send即从上一个processor的内部读取信息
    public void sendPreMessage1() {
        if(Message.getRound() > this.sleep && Message.getRound() > preProcessor.sleep) {
            if(preProcessor.sendMessage != null) {
                this.preMessage = preProcessor.sendMessage;
                preProcessor.sendMessage = null;
                Message.incNumOfMsg();
            }
        }
        else if(Message.getRound() <= this.sleep && Message.getRound() > preProcessor.sleep) {
            if(preProcessor.sendMessage != null) {
                preProcessor.sendMessage = null;
                Message.incNumOfMsg();
            }
        }

        if(this.preMessage != null)
            System.out.format("%-12s%-1s%-12s%-1s%-12s%-1s%-12s\n", myID, "|", preMessage.getID(), "|", myStatus, "|", sleep-Message.getRound());
        else
            System.out.format("%-12s%-1s%-12s%-1s%-12s%-1s%-12s\n", myID, "|", null, "|", myStatus, "|", sleep-Message.getRound());
    }

    public void sendPreMessage2() {
        if(Message.getRound() > this.sleep && Message.getRound() > preProcessor.sleep) {
            if(preProcessor.sendMessage != null) {
                this.preMessage = preProcessor.sendMessage;
                preProcessor.sendMessage = null;
                Message.incNumOfMsg();
            }
        }
        else if(Message.getRound() <= this.sleep && Message.getRound() > preProcessor.sleep) {
            if(preProcessor.sendMessage != null) {
                preProcessor.sendMessage = null;
                Message.incNumOfMsg();
            }
        }

        if(this.preMessage != null)
            System.out.format("%-12s%-1s%-12s%-1s%-12s%-1s%-12s\n", myID, "|", preMessage.getID(), "|", myStatus, "|", sleep-Message.getRound());
        else
            System.out.format("%-12s%-1s%-12s%-1s%-12s%-1s%-12s\n", myID, "|", null, "|", myStatus, "|", sleep-Message.getRound());
    }

    //执行者为当前processor，所谓exe即根据可能收到的信息更新内部变量
    public void exePreMessage1() {
        if(Message.getRound() == 1 && Message.getRound() > this.sleep) {
            this.sendMessage = new Message(this.myID);
        }
        else if(Message.getRound() > 1) {
            if(Message.getRound() > this.sleep) {  //Determine whether the current node is awake, if it is awake, then operate
                if(this.sleep + 1 == Message.getRound()) {  // The node has just woken up and needs to send myID first
                    setMyStatus(Status.UNKNOWN);
                    this.sendMessage = new Message(this.myID);
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
                                this.sendMessage = new Message(this.myID, true);
                                this.preMessage = null;
                            }
                            else {
                                this.sendMessage = new Message(this.myID);
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
                        this.sendMessage = new Message(this.myID);
                    }
                }
            }
        }

        if(sendMessage != null)
            System.out.format("%-12s%-1s%-12s%-1s%-12s%-1s%-12s\n", myID, "|", sendMessage.getID(), "|", myStatus, "|", sleep-Message.getRound());
        else
            System.out.format("%-12s%-1s%-12s%-1s%-12s%-1s%-12s\n", myID, "|", null, "|", myStatus, "|", sleep-Message.getRound());
    }

    public void exePreMessage2() {
        if(Message.getRound() == 1 && Message.getRound() > this.sleep) {
            this.sendMessage = new Message(this.myID);
        }
        else if(Message.getRound() > 1) {
            if(Message.getRound() > this.sleep) {  //Determine whether the current node is awake, if it is awake, then operate
                if(this.sleep + 1 == Message.getRound()) {  // The node has just woken up and needs to send myID first
                    setMyStatus(Status.UNKNOWN);
                    this.myID = subRingLCRSimulator.simulator(this.subring);
                    System.out.println("Interface processor get myID: " + this.myID + " from subring: " + this.subring);
                    this.sendMessage = new Message(this.myID);
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
                                this.sendMessage = new Message(this.myID, true);
                                this.preMessage = null;
                            }
                            else {
                                this.sendMessage = new Message(this.myID);
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
                        this.sendMessage = new Message(this.myID);
                    }
                }
            }
        }

        if(sendMessage != null)
            System.out.format("%-12s%-1s%-12s%-1s%-12s%-1s%-12s\n", myID, "|", sendMessage.getID(), "|", myStatus, "|", sleep-Message.getRound());
        else
            System.out.format("%-12s%-1s%-12s%-1s%-12s%-1s%-12s\n", myID, "|", null, "|", myStatus, "|", sleep-Message.getRound());
    }

    public static ArrayList<Processor> ringConstructor (int numOfProcessors, int numOfAsleepProcessors, int maxSleepRound) {
        ArrayList<Processor> processorList = new ArrayList<Processor>();
        ArrayList<Integer> sleepProcessors = new ArrayList<Integer>();
        int alpha = 3;
        Random rand = new Random();

        // 首先生成每个processor 的 独立ID;
        // 然后在[0, n) 中随机选择 numOfAsleepProcessors 个独立位置
        // 最后为每个独立位置 随机生成一个在 [0, maxSleepRound] 值
        // 根据随机生成的数据 构造processorList
        // 然后链接成环

        for (int i = 0; i < numOfProcessors; i++) {
            boolean uniqueIDFlag = false;
            int uniqueID = -1;
            while (!uniqueIDFlag) {
                uniqueIDFlag = true;
                uniqueID = rand.nextInt(alpha * numOfProcessors) + 1;
                for (int index = 0; index < processorList.size(); index++) {
                    if(processorList.get(index).getMyID() == uniqueID) uniqueIDFlag = false;
                }
            }

            Processor p = new Processor(uniqueID);
            processorList.add(p);
        }

        for (int i = 0; i < numOfAsleepProcessors; i++) {
            boolean uniqueFlag = false;
            int sleepPos = -1;
            while (!uniqueFlag) {
                uniqueFlag = true;
                sleepPos = rand.nextInt(numOfProcessors);
                for(int index = 0; index < sleepProcessors.size(); index++) {
                    if(sleepProcessors.get(index) == sleepPos) uniqueFlag = false;
                }
            }

            sleepProcessors.add(sleepPos);
        }

        int sleepRound = -1;
        for (int i = 0; i < sleepProcessors.size(); i++) {
            sleepRound = rand.nextInt(maxSleepRound) + 1;
            processorList.get(sleepProcessors.get(i)).setSleep(sleepRound);
            processorList.get(sleepProcessors.get(i)).setMyStatus(Status.ASLEEP);
        }

        System.out.println("--------------------------------------------------------");
        for (int index = 0; index < processorList.size(); index++) {
            if(index > 0)
                processorList.get(index).setPreProcessor(processorList.get(index-1));
            else
                processorList.get(index).setPreProcessor(processorList.get(processorList.size()-1));
        }

        return processorList;
    }

    public static ArrayList<Processor> ringOfRingConstructor (int numOfNonInterface, int numOfMainProcessor, int numOfInterface) {
        ArrayList<Processor> processorList = new ArrayList<Processor>();
        ArrayList<Integer> uniqueIDs = new ArrayList<Integer>();
        ArrayList<Integer> subRingsSize = new ArrayList<Integer>();
        ArrayList<Integer> interfacePos = new ArrayList<Integer>();
        int alpha = 3;
        int numOfSubProcessors;
        int allocatableSubProcessors;
        Random rand = new Random();

        // processorList size应为主环上的节点个数，处理成符合3.1的格式
        // processorList 中有的节点处理为interface构造; 有的节点用普通的构造函数
        // 因为要使用interface processor构造函数，需要先与处理好子环，子环应用一个ArrayList<Integer> 来表示，每个元素为 subProcessor 的 unique ID

        // 首先生成 numOfNonInterface 个 独立的 ID
        // 然后根据子环上processor 的个数(numOfNonInterface - (numOfMainProcessor - numOfInterface)) 随机生成各各子环的 size
        // 然后在 numOfMainProcessor 个节点中 随机选择 numOfInterface 个位置作为子环的 interface
        // 完成 processorList的构造
        // 最后链接成环

        for (int i = 0; i < numOfNonInterface; i++) {
            boolean uniqueIDFlag = false;
            int uniqueID = -1;
            while (!uniqueIDFlag) {
                uniqueIDFlag = true;
                uniqueID = rand.nextInt(alpha * numOfNonInterface) + 1;
                for (int index = 0; index < uniqueIDs.size(); index++) {
                    if(uniqueIDs.get(index) == uniqueID) uniqueIDFlag = false;
                }
            }
            uniqueIDs.add(uniqueID);
        }

        numOfSubProcessors = (numOfNonInterface - (numOfMainProcessor - numOfInterface));
        if(numOfSubProcessors < numOfInterface * 2) {
            System.out.println("Invalid interface processor number. Please check your input.");
            System.exit(1);
        }
        for (int i = 0; i < numOfInterface; i++) {subRingsSize.add(2);}
        allocatableSubProcessors = numOfSubProcessors - (numOfInterface * 2);
        for (int i = 0; i < allocatableSubProcessors; i++) {
            int index, num;
            index = rand.nextInt(numOfInterface);
            num = subRingsSize.get(index);
            subRingsSize.set(index, num + 1);
        }

        for (int i = 0; i < numOfInterface; i++) {
            boolean uniquePOSFlag = false;
            int uniquePOS = -1;
            while(!uniquePOSFlag) {
                uniquePOSFlag = true;
                uniquePOS = rand.nextInt(numOfMainProcessor);
                for (int index = 0; index < interfacePos.size(); index++) {
                    if(interfacePos.get(index) == uniquePOS) uniquePOSFlag = false;
                }
            }
            interfacePos.add(uniquePOS);
        }

        int subRingIndex = 0;
        int index = 0;
        for (int indexOnMain = 0; indexOnMain < numOfMainProcessor; indexOnMain++) {
            boolean isInterface = false;
            ArrayList<Integer> subRing = new ArrayList<Integer>();
            int subRingSize;
            for (int i = 0; i < interfacePos.size(); i++) {
                if(interfacePos.get(i) == indexOnMain) isInterface = true;
            }
            if(isInterface) {
                subRingSize = subRingsSize.get(subRingIndex);
                subRingIndex ++;
                for (int j = index; j < index + subRingSize; j++) {
                    subRing.add(uniqueIDs.get(j));
                }
                Processor p = new Processor(isInterface, subRing);
                p.setSleep(subRingSize);
                p.setMyStatus(Status.ASLEEP);
                processorList.add(p);
                index = index + subRingSize;
            }
            else {
                Processor p = new Processor(uniqueIDs.get(index));
                processorList.add(p);
                index ++;
            }
        }

        System.out.println("--------------------------------------------------------");
        for (int i = 0; i < processorList.size(); i++) {
            if(i > 0)
                processorList.get(i).setPreProcessor(processorList.get(i-1));
            else
                processorList.get(i).setPreProcessor(processorList.get(processorList.size()-1));
        }

        return processorList;
    }

}
