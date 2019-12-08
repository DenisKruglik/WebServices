package by.deniskruglik.queuemessagingreceiver;

public class Main {
    public static void main(String[] args) {
        Receiver receiver = new Receiver();
        receiver.listen();
    }
}
