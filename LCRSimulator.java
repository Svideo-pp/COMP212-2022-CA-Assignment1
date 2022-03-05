import java.util.*;


public class LCRSimulator {

    public static void Simulator1(int numOfProcessors, int numOfAsleepProcessors, int maxSleepRound) {
        int leaderID = -1;  // 存储当前选出的leader 的 myID
        int leaderRound = -1;  // 存储直到选出leader 时所用的 round数
        int leaderMessageSent = -1;  // 存储直到选出leader 时所消耗的信息数
        int totalRound = -1;
        int totalMessageSent = -1;
        boolean leaderFlag = false;  // 存储当前round有没有选出leader
        boolean leaderSpread = false;  // 判断当前是否所有节点均以terminate
        ArrayList<Processor> processorList = Processor.ringConstructor(numOfProcessors, numOfAsleepProcessors, maxSleepRound);

        while(!leaderFlag || !leaderSpread) {
            System.out.println("----------------------------After LCRProcess in round " + Message.getRound());
            System.out.format("%-12s%-1s%-12s%-1s%-12s%-1s%-12s\n", "MyID", "|" , "IDtoSend", "|", "Status", "|", "roundsTillWake");
            for (int index = 0; index < processorList.size(); index++) {
                processorList.get(index).exePreMessage1();
                if(processorList.get(index).getMyStatus() == Status.LEADER && !leaderFlag) {
                    leaderFlag = true;
                    leaderID = processorList.get(index).getMyID();
                    leaderRound = Message.getRound();
                    leaderMessageSent = Message.getNumOfMsgSent();
                }
                if(processorList.get(index).getLeaderID() == processorList.get(index).getMyID() && !leaderSpread) {
                    leaderSpread = true;
                    totalRound = Message.getRound();
                    totalMessageSent = Message.getNumOfMsgSent();
                }
            }
            System.out.println("----------------------------After LCRMessage sent in round " + Message.getRound());
            System.out.format("%-12s%-1s%-12s%-1s%-12s%-1s%-12s\n", "MyID", "|" , "receivedID", "|", "Status", "|", "roundsTillWake");

            for (int index = 0; index < processorList.size(); index++) {
                processorList.get(index).sendPreMessage1();
            }

            Message.incRound();
        }

        System.out.println("--------------------------------------------------------");
        System.out.println("The leader ID is: " + leaderID + " elected in round: " + leaderRound + " with number of sent message: " + leaderMessageSent);
        System.out.println("The network terminates in round: " + totalRound +
                " with all processors in the ring knew the leader's ID and terminated." + "The message sent in total is: " + totalMessageSent);
    }

    public static void Simulator2(int numOfNonInterface, int numOfMainProcessor, int numOfInterface) {
        int leaderID = -1;  // 存储当前选出的leader 的 myID
        int leaderRound = -1;  // 存储直到选出leader 时所用的 round数
        int leaderMessageSent = -1;  // 存储直到选出leader 时所消耗的信息数
        int totalRound = -1;
        int totalMessageSent = -1;
        boolean leaderFlag = false;  // 存储当前round有没有选出leader
        boolean leaderSpread = false;  // 判断当前是否所有节点均以terminate
        ArrayList<Processor> processorList = Processor.ringOfRingConstructor(numOfNonInterface, numOfMainProcessor, numOfInterface);

        while(!leaderFlag || !leaderSpread) {
            System.out.println("----------------------------After LCRProcess in round " + Message.getRound());
            System.out.format("%-12s%-1s%-12s%-1s%-12s%-1s%-12s\n", "MyID", "|" , "IDtoSend", "|", "Status", "|", "roundsTillWake");
            for (int index = 0; index < processorList.size(); index++) {
                processorList.get(index).exePreMessage2();
                if(processorList.get(index).getMyStatus() == Status.LEADER && !leaderFlag) {
                    leaderFlag = true;
                    leaderID = processorList.get(index).getMyID();
                    leaderRound = Message.getRound();
                    leaderMessageSent = Message.getNumOfMsgSent() + subMessage.getNumOfMsgSent();
                }
                if(processorList.get(index).getLeaderID() == processorList.get(index).getMyID() && !leaderSpread) {
                    leaderSpread = true;
                    totalRound = Message.getRound();
                    totalMessageSent = Message.getNumOfMsgSent() + subMessage.getNumOfMsgSent();
                }
            }
            System.out.println("----------------------------After LCRMessage sent in round " + Message.getRound());
            System.out.format("%-12s%-1s%-12s%-1s%-12s%-1s%-12s\n", "MyID", "|" , "receivedID", "|", "Status", "|", "roundsTillWake");

            for (int index = 0; index < processorList.size(); index++) {
                processorList.get(index).sendPreMessage2();
            }

            Message.incRound();
        }

        System.out.println("--------------------------------------------------------");
        System.out.println("The leader ID is: " + leaderID + " elected in round: " + leaderRound + " with number of sent message: " + leaderMessageSent);
        System.out.println("The network terminates in round: " + totalRound +
                " with all processors in the ring knew the leader's ID and terminated." + "The message sent in total is: " + totalMessageSent);
    }

}

