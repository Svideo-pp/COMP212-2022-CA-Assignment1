import java.util.ArrayList;


public class subRingLCRSimulator {

    public static int simulator(ArrayList<Integer> processors) {
        int leaderID = -1;  // 存储当前选出的leader 的 myID
        int leaderRound = -1;  // 存储直到选出leader 时所用的 round数
        int leaderMessageSent = -1;  // 存储直到选出leader 时所消耗的信息数
        boolean leaderFlag = false;  // 存储当前round有没有选出leader

        ArrayList<subProcessor> processorList = subProcessor.ringConstructor(processors);
        subMessage.resetRound();

        while(!leaderFlag) {
            for (int index = 0; index < processorList.size(); index++) {
                processorList.get(index).exePreMessage();
                if(processorList.get(index).getMyStatus() == Status.LEADER) {
                    leaderFlag = true;
                    leaderID = processorList.get(index).getMyID();
                    leaderRound = subMessage.getRound();
                }
            }

            for (int index = 0; index < processorList.size(); index++) {
                processorList.get(index).sendPreMessage();
            }

            subMessage.incRound();
        }

        return leaderID;
    }

}
