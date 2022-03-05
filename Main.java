import java.sql.SQLOutput;
import java.util.Scanner;


public class Main {

    static public Scanner keyInput = new Scanner(System.in);

    static public int IDParser() {
        try{
            int num = keyInput.nextInt();
            if (num == 1 || num == 2)
                return num;
            else {
                System.out.println("Invalid input. The ID must be an integer number in 1 or 2.");
                return 0;
            }
        }
        catch (Exception e) {
            System.out.println("Invalid input. The ID must be an integer number in 1 or 2.");
            System.exit(1);
        }
        return 0;
    }

    static public int ProcessorsNumberParser() {
        try{
            int num = keyInput.nextInt();
            if (num >= 2)
                return num;
            else {
                System.out.println("Invalid input. The processors' number must be an integer bigger than 1.");
                return 0;
            }
        }
        catch (Exception e) {
            System.out.println("Invalid input. The processors' number must be an integer bigger than 1.");
            System.exit(1);
        }
        return 0;
    }

    static public int asleepProcessorNumberParser(int processorNum) {
        try{
            int num = keyInput.nextInt();
            if (num >=0 && num <= processorNum)
                return num;
            else {
                System.out.println("Invalid input. This number should be a non-negative integer and " +
                        "can not bigger than the number of processors.");
                return -1;
            }
        }
        catch (Exception e) {
            System.out.println("Invalid input. This number should be a non-negative integer and " +
                    "can not bigger than the number of processors.");
            System.exit(1);
        }
        return -1;
    }

    static public int SleepRoundParser() {
        try{
            int num = keyInput.nextInt();
            if (num >= 1)
                return num;
            else {
                System.out.println("Invalid input. This number should be a positive integer.");
                return 0;
            }
        }
        catch (Exception e) {
            System.out.println("Invalid input. This number should be a positive integer.");
            System.exit(1);
        }
        return 0;
    }

    static public int nonInterfaceParser() {
        try{
            int num = keyInput.nextInt();
            if (num >= 2)
                return num;
            else {
                System.out.println("Invalid input. This number should be an integer and bigger than 1.");
                return 0;
            }
        }
        catch (Exception e) {
            System.out.println("Invalid input. This number should be an integer and bigger than 1.");
            System.exit(1);
        }
        return 0;
    }

    static public int interfaceParser(int numOfNonInterface, int numOfMainProcessor) {
        int interval_L = 0, interval_R = numOfMainProcessor;
        if(numOfNonInterface - numOfMainProcessor > 0) {
            interval_L = 1; interval_R = 1;
            while ((interval_R * 2 + (numOfMainProcessor - interval_R)) <= numOfNonInterface && interval_R <= numOfMainProcessor) {
                interval_R ++;
            }
            if ((interval_R * 2 + (numOfMainProcessor - interval_R)) > numOfNonInterface) interval_R --;
            if (interval_R > numOfMainProcessor) interval_R --;
        }
        if(numOfNonInterface == numOfMainProcessor) {interval_R = 0;}
        System.out.println("According to the previous input, you can choose the integer numbers between interval [" + interval_L + ", " + interval_R + "].");
        try{
            int num = keyInput.nextInt();
            if (num >= interval_L && num <= interval_R)
                return num;
            else {
                System.out.println("Invalid input. This number should an integer and " +
                        "between the interval [" + interval_L + ", " + interval_R + "].");
                return -1;
            }
        }
        catch (Exception e) {
            System.out.println("Invalid input. This number should between an integer and " +
                    "between the interval [" + interval_L + ", " + interval_R + "].");
            System.exit(1);
        }
        return -1;
    }

    static public int mainProcessorParser() {
        try{
            int num = keyInput.nextInt();
            if (num >= 2)
                return num;
            else {
                System.out.println("Invalid input. This number should be an integer that bigger than 1 and " +
                        "no more than the number of nonInterface processors.");
                return 0;
            }
        }
        catch (Exception e) {
            System.out.println("Invalid input. This number should be an integer that bigger than 1 and " +
                    "no more than the number of nonInterface processors.");
            System.exit(1);
        }
        return 0;
    }

    public static void main(String[] args) {
        int algorithmID = 0;


        System.out.println("Please choose a simulator for assignment 3.1 or 3.2.");
        System.out.println("Input integer number 1 for 3.1 or 2 for 3.2.");

        while(algorithmID == 0){
            algorithmID = IDParser();
        }

        if(algorithmID == 1){
            int numOfProcessors = 0;
            int numOfAsleepProcessors = -1;
            int maxSleepRound = 0;

            System.out.println("Please enter the number of processors. " +
                    "This number should be an integer bigger than 1. ");
            while(numOfProcessors == 0) {
                numOfProcessors = ProcessorsNumberParser();
            }

            System.out.println("Please enter the number of asleep processors at the beginning.");
            System.out.println("This number should be a non-negative integer and " +
                    "can not bigger than the number of processors.");
            while(numOfAsleepProcessors == -1) {
                numOfAsleepProcessors = asleepProcessorNumberParser(numOfProcessors);
            }

            System.out.println("Please enter the maximum sleep rounds for each processor.");
            System.out.println("This number should be a positive integer.");
            while(maxSleepRound == 0) {
                maxSleepRound = SleepRoundParser();
            }
            LCRSimulator.Simulator1(numOfProcessors, numOfAsleepProcessors, maxSleepRound);
        }
        else {
            int numOfNonInterface = 0;
            int numOfInterface = -1;
            int numOfMainProcessor = 0;  //The number of processor that in the main ring

            System.out.println("Please enter the number of nonInterface processors.");
            System.out.println("This number should be an integer and bigger than 1.");
            while (numOfNonInterface == 0) {
                numOfNonInterface = nonInterfaceParser();
            }

            System.out.println("Please enter the number of processor that in the main ring.");
            System.out.println("This number should be an integer that bigger than 1 and " +
                    "no more than the number of nonInterface processors.");
            while(numOfMainProcessor == 0) {
                numOfMainProcessor = mainProcessorParser();
            }

            System.out.println("Please enter the number of interface processor.");
            System.out.println("""
                    This number should be a non negative integer and should satisfy\s
                    1) No more than the number of processor that in the main ring\s
                    2) Ensure that the subring corresponding to each interface processor has at least two nonInterface processors\s
                    3) Ensure the number of processor in the subrings add the number of nonInterface processor in the main ring equal to the number of interface processor.""");
            while(numOfInterface == -1) {
                numOfInterface = interfaceParser(numOfNonInterface, numOfMainProcessor);
            }
            LCRSimulator.Simulator2(numOfNonInterface, numOfMainProcessor, numOfInterface);
        }
    }

}
