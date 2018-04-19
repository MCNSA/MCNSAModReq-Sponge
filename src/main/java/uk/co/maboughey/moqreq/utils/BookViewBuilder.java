package uk.co.maboughey.moqreq.utils;

import org.spongepowered.api.text.BookView;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import uk.co.maboughey.moqreq.database.DBModRequest;
import uk.co.maboughey.moqreq.type.ModRequest;

import java.util.ArrayList;
import java.util.List;

public class BookViewBuilder {

    public static BookView playerBook(List<ModRequest> requests) {
        int pageNumber = 0;
        int messagePage = 0;
        //RequestMessageStorage
        List<Text> messages = new ArrayList<Text>();


        BookView.Builder book = BookView.builder()
                .title(Text.of("Your Mod Requests"));
        //Lets output
        for (int i = 0; i < requests.size(); i++) {
            //Keep track of pages
            pageNumber++;
            //Get the request
            ModRequest request = requests.get(i);

            //Empty texts for later use
            String responseText = "";
            Text reset = Messaging.colour("&r");
            Text more = Text.of("");
            Text responseMore = Text.of("");


            //Build the output
            String basicString = "&6Date: &r"+request.date+"\n" +
                    "&6Status: "+request.getStatus() +
                    "&r\n";

            //Check if long message
            if (request.message.length() > 30) {
                basicString += request.message.substring(0, 30)+"...\n";
                more = Text.builder("[View More]")
                        .color(TextColors.BLUE)
                        .onClick(TextActions.changePage(requests.size()+1+messagePage)).build();

                //Add new page
                Text back = Text.builder("\n[Back]")
                        .color(TextColors.BLUE)
                        .onClick(TextActions.changePage(i+1))
                        .build();
                messages.add(Text.of(request.message).concat(back));
                messagePage++;
            }
            else {
                basicString += request.message;
            }

            //If claimed
            if (request.status == 1) {
                responseText = "\n&6Claimed by: \n&r"+request.getResponder();
            }
            //if closed
            else if (request.status == 2 || request.status == 3) {
                responseText = "&6\nClosed by: &r"+request.getResponder()+"\n&6Comment: \n&r";

                //set as read
                if (request.status == 2) {
                    request.status = 3;
                    //Tell the database
                    DBModRequest.updateRequestRead(request);
                }

                //Check for long message
                if (request.response.length() > 30) {
                    //Only show portion of message
                    responseText += request.response.substring(0, 30);
                    //Create link
                    responseMore = Text.builder("\n[View More]")
                            .color(TextColors.BLUE)
                            .onClick(TextActions.changePage(requests.size()+1+messagePage)).build();

                    //Add new page
                    Text back = Text.builder("\n[Back]")
                            .color(TextColors.BLUE)
                            .onClick(TextActions.changePage(i+1))
                            .build();
                    messages.add(Text.of(request.response).concat(back));
                    messagePage++;
                }
                else {
                    //Just add the message
                    responseText += request.response;
                }
            }
            //Add to book
            book.addPage(Messaging.colour(basicString)
                    .concat(more)
                    .concat(reset)
                    .concat(Messaging.colour(responseText))
                    .concat(responseMore)
                    .concat(reset));
        }
        //Add the message texts
        for (int i = 0; i < messages.size(); i++) {
            book.addPage(messages.get(i));
        }
        return book.build();
    }
    public static BookView viewRequests(List<ModRequest> requests, int status) {
        //Set the title of the book
        Text title = Text.of("");
        switch (status){
            case (0): title = Text.of("Open Mod Requests"); break;
            case (1): title = Text.of("Claimed Mod Requests"); break;
            case (2): title = Text.of("Closed Mod Requests"); break;
            case (3): title = Text.of("Closed Mod Requests"); break;
        }

        //Tracking ints
        int pageNumber = 0;
        int messagePage = 0;

        //RequestMessageStorage
        List<Text> messages = new ArrayList<Text>();


        BookView.Builder book = BookView.builder()
                .title(title);
        //Lets output
        for (int i = 0; i < requests.size(); i++) {
            //Keep track of pages
            pageNumber++;
            //Get the request
            ModRequest request = requests.get(i);

            //Empty texts for later use
            String responseText = "";
            Text reset = Messaging.colour("&r");
            Text more = Text.of("");
            Text responseMore = Text.of("");
            Text claimLink = Text.of("");
            Text unClaim = Text.of("");
            Text close = Text.of("");


            //Build the output
            String basicString = "&6ID: &r"+request.id+"\n&6User: &r"+request.getUser()+"\n" +
                    "&6Date: &r"+request.date+"\n" +
                    "&6Status: "+request.getStatus() +
                    "&r\n";

            //Check if long message
            if (request.message.length() > 30) {
                basicString += request.message.substring(0, 30)+"...\n";
                more = Text.builder("[View More]")
                        .color(TextColors.BLUE)
                        .onClick(TextActions.changePage(requests.size()+1+messagePage)).build();

                //Add new page
                Text back = Text.builder("\n[Back]")
                        .color(TextColors.BLUE)
                        .onClick(TextActions.changePage(i+1))
                        .build();
                messages.add(Text.of(request.message).concat(back));
                messagePage++;
            }
            else {
                basicString += request.message;
            }

            //If claimed
            if (request.status == 1) {
                responseText = "\n&6Claimed by: \n&r"+request.getResponder();
            }
            //if closed
            else if (request.status == 2 || request.status == 3) {
                responseText = "&6\nClosed by: &r"+request.getResponder()+"\n&6Comment: \n&r";

                //set as read
                if (request.status == 2) {
                    request.status = 3;
                    //Tell the database
                    DBModRequest.updateRequestRead(request);
                }

                //Check for long message
                if (request.response.length() > 30) {
                    //Only show portion of message
                    responseText += request.response.substring(0, 30);
                    //Create link
                    responseMore = Text.builder("\n[View More]")
                            .color(TextColors.BLUE)
                            .onClick(TextActions.changePage(requests.size()+1+messagePage)).build();

                    //Add new page
                    Text back = Text.builder("\n[Back]")
                            .color(TextColors.BLUE)
                            .onClick(TextActions.changePage(i+1))
                            .build();
                    messages.add(Text.of(request.response).concat(back));
                    messagePage++;
                }
                else {
                    //Just add the message
                    responseText += request.response;
                }
            }

            //Add the commands
            if (request.status == 0) {
                claimLink = Text.builder("\n[Claim Request]")
                        .color(TextColors.BLUE)
                        .onClick(TextActions.runCommand("/modreq claim "+request.id)).build();

                close = Text.builder("\n[Close without note]")
                        .color(TextColors.BLUE)
                        .onClick(TextActions.runCommand("/modreq close "+request.id+" No note"))
                        .build();
            }
            if (request.status == 1) {
                //This request has been claimed. Show teleport link and unclaim link
                claimLink = Text.builder("\n[Teleport]")
                        .color(TextColors.BLUE)
                        .onClick(TextActions.runCommand("/modreq tp "+request.id))
                        .build();
                unClaim = Text.builder("  [Unclaim]")
                        .color(TextColors.BLUE)
                        .onClick(TextActions.runCommand("/modreq unclaim "+request.id))
                        .build();
                close = Text.builder("\n[Close without note]")
                        .color(TextColors.BLUE)
                        .onClick(TextActions.runCommand("/modreq close "+request.id+" No note"))
                        .build();
            }
            //Add to book
            book.addPage(Messaging.colour(basicString)
                    .concat(more)
                    .concat(reset)
                    .concat(Messaging.colour(responseText))
                    .concat(responseMore)
                    .concat(claimLink)
                    .concat(unClaim)
                    .concat(close));
        }
        //Add the message texts
        for (int i = 0; i < messages.size(); i++) {
            book.addPage(messages.get(i));
        }
        return book.build();
    }
}
