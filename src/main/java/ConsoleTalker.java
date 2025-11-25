import java.util.Scanner;

class ConsoleTalker extends AbstractTalker {
    ConsoleTalker(IDataStorage dataStorage, IRequestHandler requestHandler) {
        super(dataStorage, requestHandler);
    }

    private Request genRequest(String requestOwnerName, String requestString) {
        Client requestOwner = ClientIdentificationHandler.getClient(dataStorage, requestOwnerName);
        return new Request(requestOwner, requestString);
    }

    public void run() {
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println("Enter your name or \"quit\" to quit:");
            String input = in.nextLine();
            if (input.equals("quit")) {
                break;
            }

            System.out.println("Enter your request:");
            String requestString = in.nextLine();

            System.out.println("Generating response...");
            Request request = genRequest(input, requestString);
            Response response = requestHandler.handleRequest(request);

            System.out.println("Response:");
            System.out.println(response.getText());
        }

        in.close();
    }
}