import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Scanner;

final class ConsoleTalker extends AbstractTalker {
    ConsoleTalker(@NonNull final IDataStorage dataStorage, @NonNull final IRequestHandler requestHandler) {
        super(dataStorage, requestHandler);
    }

    private @NonNull Request genRequest(@NonNull final String requestOwnerName, @NonNull final String requestString) {
        @NonNull final Client requestOwner = ClientIdentificationHandler.getClient(dataStorage, requestOwnerName);
        return new Request(requestOwner, requestString);
    }

    public void run() {
        @NonNull final Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println("Enter your name or \"quit\" to quit:");
            @NonNull final String input = in.nextLine();
            if (input.equals("quit")) {
                break;
            }

            System.out.println("Enter your request:");
            @NonNull final String requestString = in.nextLine();

            System.out.println("Generating response...");
            @NonNull final Request request = genRequest(input, requestString);
            @NonNull final Response response = requestHandler.handleRequest(request);

            System.out.println("Response:");
            System.out.println(response.getText());
        }

        in.close();
    }
}