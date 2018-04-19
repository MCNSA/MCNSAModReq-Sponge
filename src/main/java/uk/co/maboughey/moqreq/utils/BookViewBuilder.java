package uk.co.maboughey.moqreq.utils;

import org.spongepowered.api.text.BookView;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import uk.co.maboughey.moqreq.database.DBModRequest;
import uk.co.maboughey.moqreq.type.ModRequest;

import java.util.List;

public class BookViewBuilder {

    public static BookView playerBook(List<ModRequest> requests) {

        BookView.Builder book = BookView.builder()
                .title(Text.of("Your Mod Requests"));
        //Lets output
        for (int i = 0; i < requests.size(); i++) {
            ModRequest request = requests.get(i);
            String responseText = "";

            //If claimed
            if (request.status == 1) {
                responseText = "&6Claimed by: \n&r"+request.getResponder();
            }
            //if closed
            else if (request.status == 2) {
                responseText = "&6Closed by: &r"+request.getResponder()+"\n&6Comment: \n&r"+request.response;
                //Set read
                request.status = 3;
                DBModRequest.updateRequestRead(request);
            }
            book.addPage(Messaging.colour(
                    "&6Date: &r"+request.date
                            +"\n&6Status: "+request.getStatus()+
                            "\n&r"+request.message+"\n"+responseText
            ));
        }

        return book.build();
    }
    public static BookView viewRequests(List<ModRequest> requests, int status) {
        Text title = Text.of("");

        switch (status){
            case (0): title = Text.of("Open Mod Requests"); break;
            case (1): title = Text.of("Claimed Mod Requests"); break;
            case (2): title = Text.of("Closed Mod Requests"); break;
            case (3): title = Text.of("Closed Mod Requests"); break;
        }


        BookView.Builder book = BookView.builder()
                .title(title);
        //Lets output
        for (int i = 0; i < requests.size(); i++) {
            ModRequest request = requests.get(i);
            String responseText = "";

            Text claimLink = Text.of("");
            Text unClaim = Text.of("");
            Text reset = Messaging.colour("&r");
            Text more = Text.of("");
            if (request.status == 0) {
                claimLink = Text.builder("\n[Claim Request]")
                        .style(TextStyles.UNDERLINE)
                        .color(TextColors.BLUE)
                        .onClick(TextActions.runCommand("/modreq claim "+request.id)).build();
            }
            if (request.status == 1) {
                claimLink = Text.builder("\n[Teleport]")
                        .color(TextColors.BLUE)
                        .onClick(TextActions.runCommand("/modreq tp "+request.id)).build();
                unClaim = Text.builder("  [Unclaim]")
                                .color(TextColors.BLUE)
                                .onClick(TextActions.runCommand("/modreq unclaim "+request.id)).build();
            }
            //If claimed
            if (request.status == 1) {
                responseText = "\n&6Claimed by: \n&r"+request.getResponder();
            }
            //if closed
            else if (request.status == 2 || request.status == 3) {
                responseText = "&6Closed by: &r"+request.getResponder()+"\n&6Comment: \n&r"+request.response;
            }

            //Build the output
            String basicString = "&6User: &r"+request.getUser()+"\n" +
                                 "&6Date: &r"+request.date+"\n" +
                                 "&6Status: "+request.getStatus() +
                                 "&r\n";
            if (request.message.length() > 30) {
                basicString += request.message.substring(0, 30)+"...\n";
                more = Text.builder("[View More]")
                        .color(TextColors.BLUE)
                        .onClick(TextActions.runCommand("/modreq showmessage "+request.id)).build();
            }
            else {
                basicString += request.message;
            }

            book.addPage(Messaging.colour(basicString)
                    .concat(more)
                    .concat(reset)
                    .concat(Messaging.colour(responseText))
                    .concat(claimLink)
                    .concat(reset)
                    .concat(unClaim));
        }

        return book.build();
    }
    public static BookView viewMessage(ModRequest request) {
        BookView.Builder book = BookView.builder()
                .title(Text.of("Viewing request "+request.id));

        if (request.message.length() <= 255) {
            book.addPage(Text.of(request.message));
        }

        return book.build();
    }
}
